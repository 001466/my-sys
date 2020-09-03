package org.easy.auth.handler;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.easy.auth.utils.TokenUtil;
import org.easy.secure.exception.UserNotFoundException;
import org.easy.tool.util.DateUtil;
import org.easy.tool.util.JsonUtil;
import org.easy.tool.util.WebUtil;
import org.easy.tool.web.R;
import org.easy.user.entity.User;
import org.easy.user.entity.UserInfo;
import org.easy.user.feign.IUserFeign;
import org.easy.user.vo.UserVO;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Configuration
public class MobileLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler implements ApplicationRunner, DisposableBean {

    private static final String LOGIN_FAILURE_COUNT="LOGIN_FAILURE_COUNT:";

    @Autowired
    private IUserFeign iUserFeign;

    @Value("${deocean.security.oauth2.login-retry-max:5}")
    private Integer retryMax;
    @Value("${deocean.security.oauth2.login-bound-minutes:15}")
    private Integer boundMinutes;



    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<String, Integer>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat(DateUtil.PATTERN_DATETIME));

        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        return template;
    }



    @Autowired
    RedisTemplate<String, Integer> integerRedisTemplate;




    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof org.springframework.security.authentication.LockedException){
            exception=new org.springframework.security.authentication.LockedException("用户已锁定，请"+boundMinutes+"分钟后再试");
            printResult(request,response,exception);
            return;
        }


        String username=null;
        username=request.getParameter("username");
        if(StringUtils.isEmpty(username)){
            InputStream input =  request.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            }
            Map<String,Object> map=JsonUtil.toMap(stringBuffer.toString());
            username=String.valueOf(map.get("username"));
        }


        Integer failureCount=integerRedisTemplate.opsForValue().get(getFailureKey(username));
        System.err.println("failureCount="+failureCount);
        if(failureCount==null){failureCount=0;}
        integerRedisTemplate.opsForValue().set(getFailureKey(username), ++failureCount , boundMinutes, TimeUnit.MINUTES);

        Integer retryRemain=retryMax-failureCount;

        if(retryRemain<1) {
            R<UserInfo> result = iUserFeign.loadUserByUsername(username);
            if(result!=null && result.isSuccess() && result.getData()!=null){
                UserInfo userInfo = result.getData();
                User user=userInfo.getUser();
                if(user==null){
                    throw new UserNotFoundException(TokenUtil.USER_NOT_FOUND);
                }
                user.setLocked(1);
                iUserFeign.submit(user);
                exception=new org.springframework.security.authentication.LockedException("用户已锁定，请"+boundMinutes+"分钟后再试");
            }else{
                throw new UserNotFoundException(TokenUtil.USER_NOT_FOUND);
            }

        }else if(exception instanceof BadCredentialsException){
            exception=new BadCredentialsException("再输错"+(retryRemain)+"次,用户会被锁定");
        }
        printResult(request,response,exception);

    }

    private String getFailureKey(String username){
        return LOGIN_FAILURE_COUNT+username;
    }

    private void printResult(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(WebUtil.isMobile(request)){
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtil.toJson(R.fail(exception.getMessage())));
        }else {
            super.onAuthenticationFailure(request,response,exception);
        }
    }




    @Override
    public void destroy() throws Exception {
        unLock();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(new UnLockTask()).start();
    }

    private void unLock(){

        User user=new User();
        user.setLocked(1);

        R<List<UserVO>>  lockedRes=  iUserFeign.list(user);
        if(lockedRes==null || !lockedRes.isSuccess() || lockedRes.getData()==null){
            return;
        }
        List<UserVO> users=lockedRes.getData();
        if(users==null || users.size()==0){return;}


        for(User u:users){
            Date updateTime=u.getUpdateTime();
            if(updateTime==null){
                continue;
            }
            Long t=updateTime.getTime();
            if((System.currentTimeMillis()-t)> (boundMinutes*60*1000)){
                u.setLocked(0);
                iUserFeign.submit(u);
                integerRedisTemplate.delete(getFailureKey(u.getAccount()));
                integerRedisTemplate.delete(getFailureKey(u.getPhone()));
                integerRedisTemplate.delete(getFailureKey(String.valueOf(u.getId())));
                System.err.println("解锁用户："+u);
            }
        }
    }

    private class UnLockTask implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    unLock();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }finally {
                    try {
                        TimeUnit.MINUTES.sleep(boundMinutes);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }





}

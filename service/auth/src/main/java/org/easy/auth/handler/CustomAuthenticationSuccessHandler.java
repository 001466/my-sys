package org.easy.auth.handler;//package org.springblade.auth.handler;
//
//import com.deocean.annotation.Log;
//import com.deocean.common.util.SpringUtil;
//import com.deocean.entity.LogType;
//import lombok.extern.slf4j.Slf4j;
//import org.springblade.auth.endpoint.OauthTokenEndPoint;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@Slf4j
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    @Override
//    @Log(LogType.LOG_TYPE_SMS_LOGIN_SUCCESS)
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.info("登录成功");
//
//        OauthTokenEndPoint bean = SpringUtil.getBean(OauthTokenEndPoint.class);
//        bean.currentUser(authentication,request,response);
//         /*String[] tokens = TokenUtil.extractAndDecodeHeader();
//        assert tokens.length == 2;
//        String client_id = tokens[0];
//        String client_secret = tokens[1];
//        StringBuilder url=new StringBuilder("/oauth/token/create").append("?client_id=").append(client_id).append("&client_secret=").append(client_secret);
//        redirectStrategy.sendRedirect(request, response, url.toString());*/
//    }
//}

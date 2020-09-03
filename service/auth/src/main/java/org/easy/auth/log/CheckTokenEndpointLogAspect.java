package org.easy.auth.log;//package org.springblade.auth.log;
//
//import com.deocean.common.util.JsonUtil;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author 240018840@qq.com
// */
//@Slf4j
//@Aspect
//@Component
//public class CheckTokenEndpointLogAspect {
//	@Pointcut("execution(* org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint.*(..))")
//	public void checkTokenEndpointLogAspect() {	}
//
//	@AfterReturning(value="checkTokenEndpointLogAspect()",returning="result")
//	public void afterReturningMethod(JoinPoint joinPoint,Object result){
//    	try {
//			log.info("/oauth/check_token   res = " + JsonUtil.toJson(result));
//		} catch (Exception e){
//			log.error(e.getMessage());
//			e.printStackTrace();
//		}
//    }
//}

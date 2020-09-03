package org.easy.auth.log;//package org.springblade.auth.log;
//
//import com.deocean.common.util.JsonUtil;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//
///**
// * @author 240018840@qq.com
// */
//@Slf4j
//@Aspect
//@Component
//public class TokenEndpointLogAspect  {
//
//
//
//	@Pointcut("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.*(..))")
//	public void tokenEndpointLogAspect() {	}
//
//	@AfterReturning(value="tokenEndpointLogAspect()",returning="result")
//	public void afterReturningMethod(JoinPoint joinPoint,Object result){
//    	try {
//			log.info("/oauth/token   res = " + JsonUtil.toJson(result));
//		} catch (Exception e){
//			log.error(e.getMessage());
//			e.printStackTrace();
//		}
//    }
//
//
//
//
//
//	/*@Before("tokenEndpointLogAspect()")
//	public void doBefore(JoinPoint joinPoint) throws Throwable {
//
//		StringBuilder sb = new StringBuilder();
//		for (Object o : joinPoint.getArgs()) {
//			if(o!=null) {
//				sb.append(o.toString()).append(",");
//			}
//		}
//		log.debug("doBefore:"+joinPoint.getTarget().getClass().getName()+" rece: "+sb.toString());
//	}*/
//
//
//	/*@Around("tokenEndpointLogAspect()")
//	public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
//		Object args[]=joinPoint.getArgs();
//		MethodSignature signature=(MethodSignature)joinPoint.getSignature();
//		Method method=signature.getMethod();
//		log.info("ControllerLogAspect{},{}:请求参数:{}",signature.getClass(),method.getName(),StringUtil.join(args,";"));
//		Object result=joinPoint.proceed();
//		log.info("ControllerLogAspect 返回值 ："+JsonUtil.toJson(result));
//		return  result;
//	}*/
//
//
//
//	/*@AfterThrowing(value = "tokenEndpointLogAspect()", throwing = "error")
//	public void doThrowing(JoinPoint joinPoint,Throwable error) throws Throwable {
//		log.warn("ControllerLogAspect doThrowing:"+joinPoint.getTarget().getClass().getName(),error);
//	}*/
//
//
//
//
//}

package org.easy.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class ForbiddenHandler implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = context;
	}

	private static String getTraceId() {
		try {
			 return context.getBean("tracer",brave.Tracer.class).currentSpan().context().traceIdString();
		} catch (Exception e) {

		}
		return null;
	}

	public static Mono<Void> unauthorized(ServerHttpResponse response) {
		return result(response, HttpStatus.UNAUTHORIZED, "登陆失效");
	}

	public static Mono<Void> forbidden(ServerHttpResponse response) {
		return result(response, HttpStatus.FORBIDDEN, "请求被拒绝");
	}

	public static Mono<Void> result(ServerHttpResponse response, HttpStatus httpStatus, String msg) {

		response.setStatusCode(httpStatus);

		try {
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("success", false);
			map.put("traceId", getTraceId());
			map.put("code", httpStatus.value());
			map.put("message", msg);
			 
			
			ObjectMapper objectMapper = new ObjectMapper();
			 
			byte[] data = objectMapper.writeValueAsBytes(map);
			// 输出错误信息到页面
			DataBuffer buffer = response.bufferFactory().wrap(data);
			response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
			return response.writeWith(Mono.just(buffer));

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			byte[] data = e.getMessage().getBytes();
			DataBuffer buffer = response.bufferFactory().wrap(data);
			response.getHeaders().add("Content-Type", "text/html;charset=UTF-8");
			return response.writeWith(Mono.just(buffer));

		}

	}

}

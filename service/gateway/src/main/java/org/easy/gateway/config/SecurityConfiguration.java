package org.easy.gateway.config;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.easy.gateway.jwt.JwtOAuth2AuthenticationTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Autowired
	ReactiveAuthenticationManager jwtReactiveAuthenticationManager;

	@Autowired
	ServerSecurityContextRepository jwtAuthenticationContextRepository;

	@Autowired
	JwtOAuth2AuthenticationTokenConverter jwtOAuth2AuthenticationTokenConverter;

	private static final String ALLOWED_HEADERS = "x-requested-with,blade-auth,Content-Type,Authorization,credential,X-XSRF-TOKEN,token,username,client,client_id,client_secret,openid,request-id,hostId,hostMac";
	private static final String ALLOWED_METHODS = "*";
	private static final String ALLOWED_ORIGIN = "*";
	private static final String ALLOWED_EXPOSE = "*";
	private static final String MAX_AGE = "18000L";

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		http.httpBasic().disable();
		http.formLogin().disable();
		http.csrf().disable();
		//http.logout().disable();


		http.authorizeExchange().pathMatchers("/proxy-tuya/*","/proxy-weiguo/seneor/notify","/proxy-weiguo/event/notify","/blade-notify/msg/**","/atom-file/resource/**","/blade-auth/login/**","/blade-auth/oauth/**","/actuator/**", "/login","/login/**", "/auth/**", "/oauth/**","/blade-user/account/**","/blade-user/api/account/**").permitAll();

		http.authorizeExchange().anyExchange().authenticated();

		http.oauth2Login();

		http.addFilterAt((exchange, chain) -> {
			/** 跨域处理 **/
			ServerHttpResponse response = exchange.getResponse();
			ServerHttpRequest request = exchange.getRequest();
			HttpHeaders headers = response.getHeaders();
			log.debug("------>{}",headers.entrySet());

			if (!StringUtil.isNullOrEmpty(request.getHeaders().getOrigin())) {
				headers.add("Access-Control-Allow-Origin", request.getHeaders().getOrigin());
			} else {
				headers.add("Access-Control-Allow-Origin", "*");
			}
			headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
			headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
			headers.add("Access-Control-Allow-Credentials", "true");
			if (request.getMethod() == HttpMethod.OPTIONS) {
				return responseJson(response, "{\"status\":\"ok\"}");
			}
			return chain.filter(exchange);
		}, SecurityWebFiltersOrder.FIRST)
				.oauth2ResourceServer().jwt()
				.jwtAuthenticationConverter(jwtOAuth2AuthenticationTokenConverter)
				.authenticationManager(jwtReactiveAuthenticationManager);

//		 Add custom security.
//		 http.authenticationManager(jwtReactiveAuthenticationManager);

		http.securityContextRepository(jwtAuthenticationContextRepository);

//		http.exceptionHandling().authenticationEntryPoint(new ServerAuthenticationEntryPoint() {
//			@Override
//			public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
//				ServerHttpRequest request = exchange.getRequest();
//				ServerHttpResponse response = exchange.getResponse();
//				return ForbiddenHandler.unauthorized(response);
//			}
//		});

		http.exceptionHandling().accessDeniedHandler(new ServerAccessDeniedHandler() {

			@Override
			public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
				System.err.println("---->");
				System.err.println("---->");
				System.err.println("---->");
				return Mono.empty();
			}
		});

		return http.build();
	}


	protected Mono<Void> responseJson(ServerHttpResponse response, String message){
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		response.setStatusCode(HttpStatus.OK);
		return response.writeWith(Mono.empty());
	}
}

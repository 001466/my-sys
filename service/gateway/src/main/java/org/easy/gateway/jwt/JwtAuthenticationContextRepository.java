package org.easy.gateway.jwt;

import com.deocean.common.constant.TokenConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

public class JwtAuthenticationContextRepository extends WebSessionServerSecurityContextRepository {
    private Converter<Jwt, JwtOAuth2User> jwtToOAuth2UserConverter = new JwtToOAuth2UserConverter();
	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		return exchange.getSession().map(WebSession::getAttributes).flatMap(attrs -> {
			SecurityContext context = (SecurityContext) attrs.get(DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
			if(context!=null) {
                return Mono.justOrEmpty(context);
            }else{

                ServerHttpRequest request = exchange.getRequest();
                org.springframework.http.server.reactive.ServerHttpResponse response = exchange.getResponse();
                String token = getToken(request);
				if (StringUtils.isEmpty(token)) {
					return Mono.empty();
				}
                Jwt jwt = JwtDecoder.createJwt(token);
                JwtOAuth2User jwtOAuth2User = jwtToOAuth2UserConverter.convert(jwt);
                OAuth2AuthenticationToken auth2AuthenticationToken = new OAuth2AuthenticationToken(jwtOAuth2User,
                        jwtOAuth2User.getAuthorities(), (String) jwt.getClaims().get("client_id"));


                return Mono.just(new SecurityContextImpl(auth2AuthenticationToken));

            }

		});
	}
	
//
//	public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
//
//		try {
//			System.err.println("SecurityContextRepository load ");
//
//			ServerHttpRequest request = serverWebExchange.getRequest();
//			org.springframework.http.server.reactive.ServerHttpResponse response = serverWebExchange.getResponse();
//			String token = getToken(request);
//			Jwt jwt = JwtDecoder.createJwt(token);
//			JwtOAuth2User jwtOAuth2User = jwtToOAuth2UserConverter.convert(jwt);
//			OAuth2AuthenticationToken auth2AuthenticationToken = new OAuth2AuthenticationToken(jwtOAuth2User,
//					jwtOAuth2User.getAuthorities(), (String) jwt.getClaims().get("client_id"));
//
//			System.err.println("SecurityContextRepository load " + auth2AuthenticationToken);
//
//			return Mono.just(new SecurityContextImpl(auth2AuthenticationToken));
//		} catch (Exception e) {
//			throw new BadCredentialsException(e.getMessage());
//		}
//
//	}

	public static String getToken(ServerHttpRequest request) {
		String auth = request.getHeaders().getFirst(TokenConstant.AUTHORIZATION);
		if (StringUtils.isEmpty(auth)) {
			auth = request.getHeaders().getFirst(TokenConstant.AUTHORIZATION);
		}

		if (StringUtils.isEmpty(auth)) {
			return null;
		}

		String[] arr = auth.split(" ");
		String jsonWebToken = null;
		if (arr.length == 2) {
			jsonWebToken = arr[1];
		} else {
			jsonWebToken = arr[0];
		}
		return jsonWebToken;
	}

}
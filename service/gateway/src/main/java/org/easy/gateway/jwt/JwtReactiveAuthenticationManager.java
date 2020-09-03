package org.easy.gateway.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Slf4j
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
	private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverterAdapter(
			new JwtAuthenticationConverter());

	private final ReactiveJwtDecoder jwtDecoder;

	public JwtReactiveAuthenticationManager(ReactiveJwtDecoder jwtDecoder,
			Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter) {
		Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
		this.jwtDecoder = jwtDecoder;
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {

		return Mono.justOrEmpty(authentication).filter(a -> a instanceof BearerTokenAuthenticationToken)
				.cast(BearerTokenAuthenticationToken.class).map(BearerTokenAuthenticationToken::getToken)
				.flatMap(this.jwtDecoder::decode).flatMap(this.jwtAuthenticationConverter::convert)
				.cast(Authentication.class).onErrorMap(JwtException.class, this::onError);

	}

//	@Override
//	public Mono<Authentication> authenticate(Authentication authentication) {
//		try {
//			log.info("ReactiveAuthenticationManager authenticate  -> {}",authentication);
//			return Mono.justOrEmpty(authentication).filter(a -> a instanceof BearerTokenAuthenticationToken)
//					.cast(BearerTokenAuthenticationToken.class).map(BearerTokenAuthenticationToken::getToken)
//					.flatMap(this.jwtDecoder::decode).flatMap(this.jwtAuthenticationConverter::convert)
//					.cast(Authentication.class);
//			
//		} catch (Exception e) {
//			System.err.println("ReactiveAuthenticationManager authenticate  false -> "+e);
//			log.error(e.getMessage());
//			return Mono.error(onError(e));
//		}
//	}

	/**
	 * Use the given {@link Converter} for converting a {@link Jwt} into an
	 * {@link AbstractAuthenticationToken}.
	 *
	 * @param jwtAuthenticationConverter the {@link Converter} to use
	 */
	public void setJwtAuthenticationConverter(
			Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter) {
		Assert.notNull(jwtAuthenticationConverter, "jwtAuthenticationConverter cannot be null");
		this.jwtAuthenticationConverter = jwtAuthenticationConverter;
	}

	private OAuth2AuthenticationException onError(JwtException e) {
		System.err.println("OAuth2AuthenticationException -> "+e.getMessage());
		OAuth2Error invalidRequest = invalidToken(e.getMessage());
		return new OAuth2AuthenticationException(invalidRequest, e.getMessage());
	}

	private static OAuth2Error invalidToken(String message) {
		return new BearerTokenError(BearerTokenErrorCodes.INVALID_TOKEN, HttpStatus.UNAUTHORIZED, message,
				"https://tools.ietf.org/html/rfc6750#section-3.1");
	}
}

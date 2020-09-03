package org.easy.gateway.jwt;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class JwtDecoder implements ReactiveJwtDecoder {

	private static OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();

	private WebClient webClient;


	public JwtDecoder(WebClient wc){
		webClient=wc;
	}


	@Value("${spring.security.oauth2.authorization.check-token-access:http://blade-auth/oauth/check_token}")
	String checkTokenUrl="http://blade-auth/oauth/check_token";



	@Override
	public Mono<Jwt> decode(String tokenValue) throws JwtException {


		String check_token_url=	checkTokenUrl+"?token="+tokenValue;

		Mono<Map> mono = webClient.get().uri(check_token_url ).retrieve().bodyToMono(Map.class);
		return mono.flatMap(m->{
			if(!m.containsKey("error")){
				return Mono.just(createJwt(tokenValue));
			}else{
				throw new JwtException(String.valueOf(m.get("error_description")));
			}
		}).onErrorMap(Exception.class, e->{
			throw new JwtException(check_token_url+"  :"+e.getMessage());
		});

	}


//	@Override
//	public Mono<Jwt> decode(String tokenValue) throws JwtException {
//		try {
//			return Mono.just(validateJwt(createJwt(tokenValue)));
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			throw new JwtException(e.getMessage());
//		}
//	}

	public static Jwt createJwt(String tokenValue) throws JwtException {
		try {
			JWT jwtOrigin = JWTParser.parse(tokenValue);
			Jwt jwtTarget = createJwt(jwtOrigin, jwtOrigin.getJWTClaimsSet());
			return jwtTarget;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JwtException(e.getMessage());
		}
	}

	public static Jwt validateJwt(Jwt jwt) {
		OAuth2TokenValidatorResult result = jwtValidator.validate(jwt);
		if (result.hasErrors()) {
			String message = result.getErrors().iterator().next().getDescription();
			throw new JwtValidationException(message, result.getErrors());
		}
		return jwt;
	}

	private static Jwt createJwt(JWT parsedJwt, JWTClaimsSet jwtClaimsSet) {

		Instant expiresAt = null;
		if (jwtClaimsSet.getExpirationTime() != null) {
			expiresAt = jwtClaimsSet.getExpirationTime().toInstant();
		}

		Instant issuedAt = null;
		if (jwtClaimsSet.getIssueTime() != null) {
			issuedAt = jwtClaimsSet.getIssueTime().toInstant();
		} else if (expiresAt != null) {
			issuedAt = Instant.from(expiresAt).minusSeconds(1L);
		}

		Map<String, Object> headers = new LinkedHashMap(parsedJwt.getHeader().toJSONObject());
		return new Jwt(parsedJwt.getParsedString(), issuedAt, expiresAt, headers, jwtClaimsSet.getClaims());
	}

}

package org.easy.gateway.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class JwtConfiguration {
	
	@Bean
	ServerSecurityContextRepository jwtAuthenticationContextRepository() {
		return new JwtAuthenticationContextRepository();
	}

	@Bean
	ReactiveAuthenticationManager jwtReactiveAuthenticationManager() {
		return new JwtReactiveAuthenticationManager(jwtDecoder(), jwtOAuth2AuthenticationTokenConverter());
	}

	@Bean
	JwtOAuth2AuthenticationTokenConverter jwtOAuth2AuthenticationTokenConverter() {
		return new JwtOAuth2AuthenticationTokenConverter();
	}
	
	

	@Bean
	ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> userService(ReactiveJwtDecoder jwtDecoder) {
		return new JwtReactiveOAuth2UserService(jwtDecoder);
	}

	@Autowired
	WebClient webClient;

	@Bean
	ReactiveJwtDecoder jwtDecoder() {
		return new JwtDecoder(webClient);
	}
	
	

//    @Bean
//    ReactiveJwtDecoder jwtDecoder() throws IOException, InvalidKeyException {
//
//        return WebClient.create().get().uri(URI.create(jwtKeyUri)).header("Authorization",getBasicAuthHeader())
//                .exchange()
//                .flatMap(clientResponse -> clientResponse.bodyToMono(JwtPublicKey.class))
//                .map(jwtPublicKey -> parsePublicKey(jwtPublicKey.getValue()))
//                .map(NimbusReactiveJwtDecoder::new).block();
//        
//    }
//    
//    
//    
//	private  String getBasicAuthHeader() {
//		String auth = clientId + ":" + clientSecret;
//		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
//		String authHeader = "Basic " + new String(encodedAuth);
//		System.out.println("-------"+authHeader);
//		return authHeader;
//	}
//	
//
//    
//    private RSAPublicKey parsePublicKey(String keyValue) {
//        PemReader pemReader = new PemReader(new StringReader(keyValue));
//        PemObject pem = null;
//        try {
//            pem = pemReader.readPemObject();
//            return new RSAPublicKeyImpl(pem.getContent());
//        } catch (IOException | InvalidKeyException e) {
//            LOGGER.error("Unable to parse public key",e);
//        }
//        return null;
//    }

}

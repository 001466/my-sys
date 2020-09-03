package org.easy.auth.feign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenFeignFallbackFactory implements FallbackFactory<TokenFeign> {
    @Override
    public TokenFeign create(Throwable cause) {
        return new TokenFeign(){
            @Override
            public Map<String, ?> checkToken(String token) {
                return new HashMap<>();
            }

            @Override
            public Map<String, ?> login(String grant_type, String client_id, String client_secret, String username, String password, String scope) {
                return new HashMap<>();
            }

            @Override
            public Map<String, ?> logout(String token) {
                return new HashMap<>();
            }

        };
    }
}

package org.easy.auth.feign;

import org.easy.auth.AutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = AutoConfiguration.SERVICE_NAME, fallbackFactory = TokenFeignFallbackFactory.class)
public interface TokenFeign {

    @RequestMapping({"/oauth/check_token"})
    public Map<String, ?> checkToken(@RequestParam("token") String token);


    @PostMapping({"/oauth/token"})
    public Map<String, ?> login(
            @RequestParam("grant_type") String grant_type,
            @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("scope") String scope
    );

    @RequestMapping({"/logout"})
    public Map<String, ?> logout(@RequestHeader("Authorization") String token);

}

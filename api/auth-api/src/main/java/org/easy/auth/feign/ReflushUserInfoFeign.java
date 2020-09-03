package org.easy.auth.feign;


import org.easy.auth.AutoConfiguration;
import org.easy.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = AutoConfiguration.SERVICE_NAME, path = ReflushUserInfoFeign.PATH,fallback = ReflushUserInfoFeignFallback.class)
public interface ReflushUserInfoFeign {

    String PATH="/token";

    @RequestMapping("/reflush")
    void flushUserInfo(@RequestParam("accessToken") String accessToken, @RequestBody User user);
}

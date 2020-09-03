package org.easy.auth.feign;

import org.easy.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReflushUserInfoFeignFallback implements ReflushUserInfoFeign {
    @Override
    public void flushUserInfo(String accessToken, User user) {
        return;
    }
}

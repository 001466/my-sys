package org.easy.auth.endpoint;


import org.easy.user.entity.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class ReflushUserInfoController {


    @Resource
    private TokenStore tokenStore;

    @RequestMapping("/reflush")
    public void flushUserInfo(@RequestParam("accessToken") String accessToken, @RequestBody User user){
        accessToken = accessToken.replaceAll("Bearer ", "").trim();
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
        if (!StringUtils.isEmpty(user.getName())){
            additionalInformation.put("userName",user.getName());
            additionalInformation.put("user_name",user.getName());
        }
        if (!StringUtils.isEmpty(user.getAvatar())){
            additionalInformation.put("avatar",user.getAvatar());
        }
        tokenStore.storeAccessToken(oAuth2AccessToken,oAuth2Authentication);

    }
}

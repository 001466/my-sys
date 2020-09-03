package org.easy.auth.handler;


import lombok.extern.slf4j.Slf4j;
import org.easy.secure.constant.TokenConstant;
import org.easy.secure.util.SecureWrapUtil;
import org.easy.tool.util.JsonUtil;
import org.easy.tool.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RedisTokenLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);


        String accessToken = request.getParameter("access_token");
        if(StringUtils.isEmpty(accessToken)){
            String header=SecureWrapUtil.getHeader();
            accessToken=header.substring(TokenConstant.BEARER.length()+1,header.length());
        }

        if(StringUtils.isEmpty(accessToken)){
            response.getWriter().write(JsonUtil.toJson(R.fail("退出失败access_token为空值")));
            log.error("退出失败access_token为空值");
            return;
        }

        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        if(oAuth2AccessToken==null){
            response.getWriter().write(JsonUtil.toJson(R.fail("退出失败access_token找不到")));
            log.error("退出失败access_token找不到");
            return;
        }

        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
        tokenStore.removeRefreshToken(oAuth2RefreshToken);
        tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
        tokenStore.removeAccessToken(oAuth2AccessToken);

        response.getWriter().write(JsonUtil.toJson(R.success("退出成功")));

    }
}

package org.easy.auth.handler;


import lombok.extern.slf4j.Slf4j;
import org.easy.auth.service.BladeUserDetails;
import org.easy.auth.utils.TokenUtil;
import org.easy.tool.util.JsonUtil;
import org.easy.tool.util.SpringUtil;
import org.easy.tool.util.WebUtil;
import org.easy.tool.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class MobileLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


//    @Autowired
//    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    //@Log(LogType.LOG_TYPE_NORMAL_LOGIN_SUCCESS)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        System.err.println("authentication："+authentication);
        System.err.println("isMobile："+WebUtil.isMobile(request));

        String[] tokens = TokenUtil.extractAndDecodeHeader();
        assert tokens.length == 2;
        String client_id = tokens[0];
        String client_secret = tokens[1];

        if(WebUtil.isMobile(request)||(client_id!=null && (client_id.toLowerCase().equals("ios")||client_id.toLowerCase().equals("android")))){

            BladeUserDetails principal = (BladeUserDetails) authentication.getPrincipal();


            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(client_id);
            OAuth2Request oAuth2Request = null;
            TokenRequest tokenRequest = new TokenRequest(new HashMap<>(16), client_id, clientDetails.getScope(), "app");
//		if (clientDetails.getRegisteredRedirectUri().size() > 0) {
//			Map<String, String> requestParameters = tokenRequest.getRequestParameters();
//			HashMap<String, String> modifiable = new HashMap<String, String>(requestParameters);
//			oAuth2Request = new OAuth2Request(modifiable, clientDetails.getClientId(), clientDetails.getAuthorities(), true, tokenRequest.getScope(),
//					clientDetails.getResourceIds(), clientDetails.getRegisteredRedirectUri().iterator().next(), null, null);
//		} else {
            oAuth2Request=tokenRequest.createOAuth2Request(clientDetails);
//		}

            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken token = SpringUtil.getBean(AuthorizationServerTokenServices.class).createAccessToken(oAuth2Authentication);

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

            if(WebUtil.isMobile(request)) {
                response.getWriter().write(JsonUtil.toJson(R.success().setData(token)));
            }else{
                response.getWriter().write(JsonUtil.toJson(token));
            }

//            StringBuilder url=new StringBuilder("/oauth/token/create").append("?client_id=").append(client_id).append("&client_secret=").append(client_secret);
//            getRedirectStrategy().sendRedirect(request, response, url.toString());

        }else {
            super.onAuthenticationSuccess(request,response,authentication);
        }
    }





}

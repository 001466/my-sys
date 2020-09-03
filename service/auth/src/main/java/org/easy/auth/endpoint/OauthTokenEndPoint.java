/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.easy.auth.endpoint;



import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.easy.auth.service.BladeUserDetails;
import org.easy.auth.utils.TokenUtil;
import org.easy.tool.util.JsonUtil;
import org.easy.tool.util.WebUtil;
import org.easy.tool.web.R;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * BladeEndPoint
 *
 * @author Chill
 */
@Slf4j
@RestController
@AllArgsConstructor
public class OauthTokenEndPoint {


	private AuthorizationServerTokenServices authorizationServerTokenServices;
	private ClientDetailsService clientDetailsService;


	@RequestMapping(value = {"/oauth/token/create"})
	public void currentUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws IOException {

		BladeUserDetails principal = (BladeUserDetails) authentication.getPrincipal();
		String[] tokens = TokenUtil.extractAndDecodeHeader();
		assert tokens.length == 2;
		String clientId = tokens[0];
		String clientSecret = tokens[1];

		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
		OAuth2Request oAuth2Request = null;
		TokenRequest tokenRequest = new TokenRequest(new HashMap<>(16), clientId, clientDetails.getScope(), "app");
//		if (clientDetails.getRegisteredRedirectUri().size() > 0) {
//			Map<String, String> requestParameters = tokenRequest.getRequestParameters();
//			HashMap<String, String> modifiable = new HashMap<String, String>(requestParameters);
//			oAuth2Request = new OAuth2Request(modifiable, clientDetails.getClientId(), clientDetails.getAuthorities(), true, tokenRequest.getScope(),
//					clientDetails.getResourceIds(), clientDetails.getRegisteredRedirectUri().iterator().next(), null, null);
//		} else {
			oAuth2Request=tokenRequest.createOAuth2Request(clientDetails);
//		}

		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		if(WebUtil.isMobile(request)) {
			response.getWriter().write(JsonUtil.toJson(R.success().setData(token)));
		}else{
			response.getWriter().write(JsonUtil.toJson(token));
		}

	}





}

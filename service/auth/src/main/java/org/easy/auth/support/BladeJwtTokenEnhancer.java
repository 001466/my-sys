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
package org.easy.auth.support;

import lombok.extern.slf4j.Slf4j;
import org.easy.auth.service.BladeUserDetails;
import org.easy.secure.constant.TokenConstant;
import org.easy.tool.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;

/**
 * jwt返回参数增强
 *
 * @author Chill
 */
@Slf4j
public class BladeJwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	AuthorizationCodeServices authorizationCodeServices;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		BladeUserDetails principal = (BladeUserDetails) authentication.getUserAuthentication().getPrincipal();
		Map<String, Object> additionalInfo=BeanUtil.toMap(principal.getBladeUser());

		additionalInfo.put(TokenConstant.AUTHORIZATION_CODE,authorizationCodeServices.createAuthorizationCode(authentication));
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		System.err.println("additionalInfo:"+additionalInfo);
		return accessToken;
	}
}

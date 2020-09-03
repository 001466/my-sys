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
package org.easy.auth.service;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.easy.system.feign.IClientFeign;
import org.easy.system.vo.ClientVO;
import org.easy.tool.util.JsonUtil;
import org.easy.tool.web.R;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 客户端信息
 *
 * @author Chill
 */
@Slf4j
public class BladeClientDetailsServiceImpl implements ClientDetailsService {

	IClientFeign iClientFeign;

	public BladeClientDetailsServiceImpl(IClientFeign iClientFeign){
		this.iClientFeign=iClientFeign;
	}

	@Override
	@SneakyThrows
	public ClientDetails loadClientByClientId(String clientId) {

		//log.warn("loadClientByClientId:{}",clientId);
		R<ClientVO> result = iClientFeign.loadClientByClientId(clientId);
		if(result==null || !result.isSuccess() || result.getData()==null){
			return null;
		}
        ClientVO authClient=result.getData();
		return new ClientDetails(){
			@Override
			public String getClientId() {
				return authClient.getClientId();
			}

			@Override
			public Set<String> getResourceIds() {
				Set set=new HashSet();
				if(StringUtils.isEmpty(authClient.getResourceIds())){
					return set;
				}
				set.addAll(CollectionUtils.arrayToList(authClient.getResourceIds().split(",")));
				return set;
			}

			@Override
			public boolean isSecretRequired() {
				return false;
			}

			@Override
			public String getClientSecret() {
				return authClient.getClientSecret();
			}

			@Override
			public boolean isScoped() {
				return true;
			}

			@Override
			public Set<String> getScope() {
				Set set=new HashSet();
				if(StringUtils.isEmpty(authClient.getScope())){
					return set;
				}
				set.addAll(CollectionUtils.arrayToList(authClient.getScope().split(",")));
				return set;
			}

			@Override
			public Set<String> getAuthorizedGrantTypes() {
				Set set=new HashSet();
				if(StringUtils.isEmpty(authClient.getAuthorizedGrantTypes())){
					return set;
				}
				set.addAll(CollectionUtils.arrayToList(authClient.getAuthorizedGrantTypes().split(",")));
				return set;
			}

			@Override
			public Set<String> getRegisteredRedirectUri() {
				Set set=new HashSet();
				if(StringUtils.isEmpty(authClient.getWebServerRedirectUri())){
					return set;
				}
				set.addAll(CollectionUtils.arrayToList(authClient.getWebServerRedirectUri().split(",")));
				return set;
			}

			@Override
			public Collection<GrantedAuthority> getAuthorities() {
				Set set=new HashSet();
				if(StringUtils.isEmpty(authClient.getAuthorities())){
					return set;
				}
				set.addAll(CollectionUtils.arrayToList(authClient.getAuthorities().split(",")));
				return set;
			}

			@Override
			public Integer getAccessTokenValiditySeconds() {
				return authClient.getAccessTokenValidity().intValue();
			}

			@Override
			public Integer getRefreshTokenValiditySeconds() {
				return authClient.getRefreshTokenValidity().intValue();
			}

			@Override
			public boolean isAutoApprove(String s) {
				if(StringUtils.isEmpty(s)){
					return false;
				}
				return Boolean.valueOf(authClient.getAutoapprove());
			}

			@Override
			public Map<String, Object> getAdditionalInformation() {
				 return JsonUtil.toMap(authClient.getAdditionalInformation());
			}
		};
 	}
}

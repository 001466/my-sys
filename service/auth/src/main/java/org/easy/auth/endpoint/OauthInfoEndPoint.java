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


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.easy.auth.service.BladeUserDetails;
import org.easy.secure.BladeUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BladeEndPoint
 *
 * @author Chill
 */
@Slf4j
@RestController
@AllArgsConstructor
public class OauthInfoEndPoint {

	@RequestMapping(value = {"/oauth/user-info","/oauth/userinfo"},produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BladeUser currentUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if(authentication==null){
            return null;
        }
	    BladeUserDetails principal = (BladeUserDetails) authentication.getPrincipal();
		if(principal==null){
		    return null;
        }
		return principal.getBladeUser();
	}

}

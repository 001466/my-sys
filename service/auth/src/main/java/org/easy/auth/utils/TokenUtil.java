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
package org.easy.auth.utils;

import org.easy.tool.util.Charsets;
import org.easy.tool.util.StringPool;
import org.easy.tool.util.StringUtil;
import org.easy.tool.util.WebUtil;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;

/**
 * 认证工具类
 *
 * @author Chill
 */
public class TokenUtil {


	public final static String USER_NOT_FOUND = "该账号未注册，请注册后再登录";
	public final static String HEADER_KEY = "Authorization";
	public final static String HEADER_PREFIX = "Basic ";
	public final static String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";

	/**
	 * 解码
	 */
	@SneakyThrows
	public static String[] extractAndDecodeHeader() {
		try{
			return extractAndDecodeFromHeader();
		}catch (Exception e){
			return extractAndDecodeFromRequest();
		}
	}

	private static String[] extractAndDecodeFromRequest() throws UnsupportedEncodingException {
		String client_id=WebUtil.getRequest().getParameter("client_id");
		String client_secret=WebUtil.getRequest().getParameter("client_secret");
		/*if(StringUtil.isBlank(client_id) || StringUtil.isBlank(client_secret)){
			throw new BadCredentialsException("Failed to get client_id,client_secret from header");
		}*/
		return new String[]{client_id, client_secret};
	}

	private static String[] extractAndDecodeFromHeader()  {
		String header = WebUtil.getRequest().getHeader(TokenUtil.HEADER_KEY);
		if(StringUtil.isBlank(header) ) {
			header=WebUtil.getRequest().getParameter(TokenUtil.HEADER_KEY.toLowerCase());
		}
		if(StringUtil.isBlank(header) ) {
			header=WebUtil.getRequest().getParameter(TokenUtil.HEADER_KEY);
		}
		String token="";
		try {
			byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);
			byte[] decoded = Base64.getDecoder().decode(base64Token);
			token = new String(decoded, Charsets.UTF_8_NAME);
		} catch (IllegalArgumentException|UnsupportedEncodingException var7) {
			throw new BadCredentialsException("Failed to decode basic authentication token from header:"+var7.getMessage());
		}
		int index = token.indexOf(StringPool.COLON);
		if (index == -1) {
			throw new BadCredentialsException("Invalid basic authentication token from header:"+token);
		} else {
			return new String[]{token.substring(0, index), token.substring(index + 1)};
		}
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader() {
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		return tokens[0];
	}

	/**
	 * 获取token过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static int getTokenValiditySecond() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
	}

	/**
	 * 获取refreshToken过期时间
	 *
	 * @return expire
	 */
	public static int getRefreshTokenValiditySeconds() {
		return 60 * 60 * 24 * 15;
	}

}

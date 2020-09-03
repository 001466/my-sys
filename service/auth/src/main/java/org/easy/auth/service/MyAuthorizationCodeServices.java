package org.easy.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Service
public class MyAuthorizationCodeServices extends InMemoryAuthorizationCodeServices {
	
	@Autowired
	RedisTemplate redisTemplate;
	
	
	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(authentication);
			redisTemplate.opsForValue().set(code,stream.toByteArray(),30000,TimeUnit.SECONDS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public OAuth2Authentication remove(String code) {
		 Object o=null;
		 try {
			ByteArrayInputStream bri = new ByteArrayInputStream((byte[])redisTemplate.opsForValue().get(code));
			 ObjectInputStream outs = new ObjectInputStream(bri);
			 o = outs.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (OAuth2Authentication)o;
	}
	
	
	



}

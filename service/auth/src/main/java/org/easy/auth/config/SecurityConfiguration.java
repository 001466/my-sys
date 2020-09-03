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
package org.easy.auth.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.easy.auth.handler.MobileLoginFailureHandler;
import org.easy.auth.handler.MobileLoginSuccessHandler;
import org.easy.auth.handler.RedisTokenLogoutSuccessHandler;
import org.easy.auth.support.BladePasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置
 *
 * @author Chill
 */
@Order(2)
@Configuration
@AllArgsConstructor
//@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;



    @Autowired
    MobileLoginSuccessHandler successHandler;

    @Autowired
    MobileLoginFailureHandler failureHandler;


    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        AuthenticationManager authenticationManager= super.authenticationManagerBean();
        return authenticationManager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return BladePasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Autowired
    protected void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public RedisTokenLogoutSuccessHandler redisTokenLogoutSuccessHandler(){
        return new RedisTokenLogoutSuccessHandler();
    }



    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http.csrf().disable();

        //使HttpSecurity接收以"/login/","/oauth/"开头请求。
        http.requestMatchers()
                .antMatchers("/oauth/**", "/login/**", "/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated();


        http.formLogin().loginPage("/login").successHandler(successHandler).failureHandler(failureHandler).permitAll();
        http.logout().logoutSuccessHandler(redisTokenLogoutSuccessHandler()).permitAll();
        http.rememberMe().userDetailsService(userDetailsService);

    }








}

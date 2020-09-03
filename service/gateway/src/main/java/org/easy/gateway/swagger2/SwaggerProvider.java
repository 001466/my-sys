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

package org.easy.gateway.swagger2;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合接口文档注册
 *
 * @author Sywd
 */
@Primary
@Component
@AllArgsConstructor
//@Profile({"dev", "test"})
public class SwaggerProvider implements SwaggerResourcesProvider {
	//public static final String API_URI = "/v2/api-docs-ext";
	public static final String API_URI = "/v2/api-docs";
	private final RouteLocator routeLocator;

	@Override
	public List<SwaggerResource> get() {

		List<SwaggerResource> resources = new ArrayList<>();

		routeLocator.getRoutes().subscribe(route -> {

			String lb=route.getUri().toString();

			String ph=lb.replace("lb://","/");

			resources.add(swaggerResource(route.getId(),new StringBuilder(ph).append(API_URI).toString()));

		});

		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion("2.0");
				return swaggerResource;
	}

}

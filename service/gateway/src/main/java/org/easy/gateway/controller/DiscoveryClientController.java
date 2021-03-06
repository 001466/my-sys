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
package org.easy.gateway.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务发现控制器
 *
 * @author Chill
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/discovery")
@Api(value = "服务实例", tags = { "服务实例" })
public class DiscoveryClientController {

	private final DiscoveryClient discoveryClient;
    private final Registration registration; // 服务注册
    
    @GetMapping("/my")
	@ApiOperation(value = "查看", notes = "查看")
	public List<ServiceInstance> my() {
     	return discoveryClient.getInstances(registration.getServiceId());
    }
    

	/**
	 * 获取服务实例
	 */
	@GetMapping("/instances")
	@ApiOperation(value = "查看", notes = "查看")
	public Map<String, List<ServiceInstance>> instances() {
  		Map<String, List<ServiceInstance>> instances = new HashMap<>(16);
		List<String> services = discoveryClient.getServices();
		services.forEach(s -> {
			List<ServiceInstance> list = discoveryClient.getInstances(s);
			instances.put(s, list);
		});
		return instances;
	}

}

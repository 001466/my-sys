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
package org.easy.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.easy.mybatisplus.support.Condition;
import org.easy.mybatisplus.support.Query;
import org.easy.tool.web.R;
import org.easy.tool.util.Func;
//import org.springblade.system.feign.IDictClient;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.easy.system.entity.Client;
import org.easy.system.vo.ClientVO;
import org.easy.system.wrapper.ClientWrapper;
import org.easy.system.service.IClientService;
import java.util.List;

/**
 *  控制器
 *
 * @author EasyX
 * @since 2020-09-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/client")
@Api(value = "", tags = "接口")
public class ClientController {

	private IClientService clientService;

	//private IDictClient dictClient;

	/**
	* 详情
	*/
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入client", position = 1)
	public R<ClientVO> detail(Client client) {
		Client detail = clientService.getOne(Condition.getQueryWrapper(client));
		ClientWrapper clientWrapper = new ClientWrapper();
		return R.success(clientWrapper.entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入client", position = 2)
	public R<IPage<ClientVO>> list(Client client, Query query) {
		IPage<Client> pages = clientService.page(Condition.getPage(query), Condition.getQueryWrapper(client));
		ClientWrapper clientWrapper = new ClientWrapper();
		return R.success(clientWrapper.pageVO(pages));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入client", position = 3)
	public R<IPage<ClientVO>> page(ClientVO client, Query query) {
		IPage<ClientVO> pages = clientService.selectClientPage(Condition.getPage(query), client);
		return R.success(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入client", position = 4)
	public R save(@Valid @RequestBody Client client) {
		return R.status(clientService.save(client));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入client", position = 5)
	public R update(@Valid @RequestBody Client client) {
		return R.status(clientService.updateById(client));
	}

	/**
	* 新增或修改 
	*/
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入client", position = 6)
	public R submit(@Valid @RequestBody Client client) {
		return R.status(clientService.saveOrUpdate(client));
	}

	
	/**
	* 删除 
	*/
	@PostMapping("/remove")
	@ApiOperation(value = "逻辑删除", notes = "传入ids", position = 7)
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(clientService.deleteLogic(Func.toIntList(ids)));
	}

	
}

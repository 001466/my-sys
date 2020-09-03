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
package org.easy.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.easy.mybatisplus.support.Condition;
import org.easy.mybatisplus.support.Query;
import org.easy.tool.web.R;
import org.easy.tool.util.Func;
//import org.springblade.system.feign.IDictClient;

import org.easy.tool.web.ResultCode;
import org.easy.user.entity.UserInfo;
import org.easy.user.feign.IUserFeign;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.easy.user.entity.User;
import org.easy.user.vo.UserVO;
import org.easy.user.wrapper.UserWrapper;
import org.easy.user.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  控制器
 *
 * @author EasyX
 * @since 2020-09-03
 */
@RestController
@AllArgsConstructor
@RequestMapping(IUserFeign.PATH)
@Api(value = "", tags = "接口")
@Slf4j
public class UserController implements IUserFeign {

	private IUserService userService;

	//private IDictClient dictClient;

	/**
	* 详情
	*/
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入user", position = 1)
	public R<UserVO> detail(User user) {
		User detail = userService.getOne(Condition.getQueryWrapper(user));
		UserWrapper userWrapper = new UserWrapper();
		return R.success(userWrapper.entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入user", position = 2)
	public R<IPage<UserVO>> list(User user, Query query) {
		IPage<User> pages = userService.page(Condition.getPage(query), Condition.getQueryWrapper(user));
		UserWrapper userWrapper = new UserWrapper();
		return R.success(userWrapper.pageVO(pages));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入user", position = 3)
	public R<IPage<UserVO>> page(UserVO user, Query query) {
		IPage<UserVO> pages = userService.selectUserPage(Condition.getPage(query), user);
		return R.success(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入user", position = 4)
	public R save(@Valid @RequestBody User user) {
		return R.status(userService.save(user));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入user", position = 5)
	public R update(@Valid @RequestBody User user) {
		return R.status(userService.updateById(user));
	}

	/**
	* 新增或修改 
	*/
	@Override
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入user", position = 6)
	public R submit(@Valid @RequestBody User user) {
		return R.status(userService.saveOrUpdate(user));
	}


	/**
	* 删除 
	*/
	@PostMapping("/remove")
	@ApiOperation(value = "逻辑删除", notes = "传入ids", position = 7)
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(userService.deleteLogic(Func.toIntList(ids)));
	}


	@Override
	public R<UserInfo> loadUserByUsername(String account) {

		UserInfo userInfo = new UserInfo();
		QueryWrapper<User> queryWrapper=new QueryWrapper<>();
		queryWrapper.
				or().eq("account",account).
				or().eq("phone",account).
				or().eq("email",account).
				or().eq("id",account);
		List<User> users=userService.list(queryWrapper);

		if(users==null || users.size()==0){
			return R.fail(ResultCode.USER_NOT_FOUND,ResultCode.USER_NOT_FOUND.getMessage());
		}
		User user=users.get(0);
		userInfo.setUser(user);

		userInfo.setRoles(CollectionUtils.arrayToList(new String[]{"guest"}));
		userInfo.setPermissions(CollectionUtils.arrayToList(new String[]{"guest"}));
		log.info("loadUserByUsername {},{}",account,userInfo);

 		return R.success(userInfo);
	}
}

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
import org.easy.system.entity.Dict;
import org.easy.system.vo.DictVO;
import org.easy.system.wrapper.DictWrapper;
import org.easy.system.service.IDictService;
import java.util.List;

/**
 * 字典管理 控制器
 *
 * @author EasyX
 * @since 2020-09-03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Api(value = "字典管理", tags = "字典管理")
public class DictController {

	private IDictService dictService;

	//private IDictClient dictClient;

	/**
	* 详情
	*/
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入dict", position = 1)
	public R<DictVO> detail(Dict dict) {
		Dict detail = dictService.getOne(Condition.getQueryWrapper(dict));
		DictWrapper dictWrapper = new DictWrapper();
		return R.success(dictWrapper.entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入dict", position = 2)
	public R<List<DictVO>> list(Dict dict) {
		List<Dict> list = dictService.list(Condition.getQueryWrapper(dict));
		DictWrapper dictWrapper = new DictWrapper();
		return R.success(dictWrapper.listVO(list));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入dict", position = 3)
	public R<IPage<DictVO>> page(DictVO dict, Query query) {
		IPage<DictVO> pages = dictService.selectDictPage(Condition.getPage(query), dict);
		return R.success(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入dict", position = 4)
	public R save(@Valid @RequestBody Dict dict) {
		return R.status(dictService.save(dict));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入dict", position = 5)
	public R update(@Valid @RequestBody Dict dict) {
		return R.status(dictService.updateById(dict));
	}

	/**
	* 新增或修改 
	*/
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入dict", position = 6)
	public R submit(@Valid @RequestBody Dict dict) {
		return R.status(dictService.saveOrUpdate(dict));
	}

	
	/**
	* 删除 
	*/
	@PostMapping("/remove")
	@ApiOperation(value = "逻辑删除", notes = "传入ids", position = 7)
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(dictService.deleteLogic(Func.toIntList(ids)));
	}

	
}

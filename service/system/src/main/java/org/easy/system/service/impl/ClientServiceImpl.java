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
package org.easy.system.service.impl;

import org.easy.system.entity.Client;
import org.easy.system.vo.ClientVO;
import org.easy.system.mapper.ClientMapper;
import org.easy.system.service.IClientService;
import org.easy.mybatisplus.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 *  服务实现类
 *
 * @author EasyX
 * @since 2020-09-01
 */
@Service
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, Client> implements IClientService {

	@Override
	public IPage<ClientVO> selectClientPage(IPage<ClientVO> page, ClientVO client) {
		return page.setRecords(baseMapper.selectClientPage(page, client));
	}

}

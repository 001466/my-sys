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
package org.easy.develop;


import org.easy.develop.support.BladeCodeGenerator;

/**
 * 代码生成器
 *
 * @author Chill
 */
public class CodeGenerator {

	/**
	 * 代码生成的模块名
	 */
	public static String JAVA_CODE_NAME = "字典管理";
	/**
	 * 代码所在服务名
	 */
	public static String JAVA_SERVICE_NAME = "system";
	/**
	 * 代码生成的包名
	 */
	public static String JAVA_PACKAGE_NAME = "org.easy.system";
	/**
	 * 代码生成的路径
	 */
	public static String JAVA_PACKAGE_PATH = "F:\\workspace\\my-sys\\service\\system";


	/**
	 * 前端代码生成所属系统
	 */
	public static String WEB_NAME = "saber";
	/**
	 * 前端代码生成地址
	 */
	public static String WEB_PATH = "/Users/chill/Workspaces/product/Saber";



	/**
	 * 需要去掉的表前缀
	 */
	public static String[] TABLE_PREFIX = {};
	/**
	 * 需要生成的表名(两者只能取其一)
	 */
	public static String[] TABLES_INCLUDE = {"dict"};
	/**
	 * 需要排除的表名(两者只能取其一)
	 */
	public static String[] TABLES_EXCLUDE = {};
	/**
	 * 是否包含基础业务字段
	 */
	public static Boolean HAS_SUPER_ENTITY = Boolean.FALSE;
	/**
	 * 基础业务字段
	 */
	public static String[] SUPER_ENTITY_COLUMNS = {"id", "create_time", "create_user", "update_time", "update_user", "status", "is_deleted"};


	/**
	 * RUN THIS
	 */
	public static void main(String[] args) {
		BladeCodeGenerator generator = new BladeCodeGenerator();

		generator.setCodeName(JAVA_CODE_NAME);
		generator.setServiceName(JAVA_SERVICE_NAME);
		generator.setPackageName(JAVA_PACKAGE_NAME);
		generator.setPackagePath(JAVA_PACKAGE_PATH);

		generator.setWebName(WEB_NAME);
		generator.setWebPath(WEB_PATH);

		generator.setTablePrefix(TABLE_PREFIX);
		generator.setTablesInclude(TABLES_INCLUDE);
		generator.setTablesExclude(TABLES_EXCLUDE);
		generator.setHasSuperEntity(HAS_SUPER_ENTITY);
		generator.setSuperEntityColumns(SUPER_ENTITY_COLUMNS);
		generator.run();
	}

}

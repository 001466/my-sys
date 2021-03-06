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
package org.easy.develop.support;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.easy.tool.constant.SystemConstant;
import org.easy.tool.util.Func;
import org.easy.tool.util.StringUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 代码生成器配置类
 *
 * @author Chill
 */
@Data
@Slf4j
public class BladeCodeGenerator {
	/**
	 * 代码所在系统
	 */
	private String webName = SystemConstant.SWORD_NAME;
	/**
	 * 代码前端生成的地址
	 */
	private String webPath;



	/**
	 * 代码模块名称
	 */
	private String codeName;
	/**
	 * 代码所在服务名
	 */
	private String serviceName = "my-service";
	/**
	 * 代码生成的包名
	 */
	private String packageName = "org.easy.test";


	/**
	 * 代码后端生成的地址
	 */
	private String packagePath;

	/**
	 * 需要去掉的表前缀
	 */
	private String[] tablePrefix = {"sys_"};
	/**
	 * 需要生成的表名(两者只能取其一)
	 */
	private String[] tablesInclude = {"sys_test"};
	/**
	 * 需要排除的表名(两者只能取其一)
	 */
	private String[] tablesExclude = {};
	/**
	 * 是否包含基础业务字段
	 */
	private Boolean hasSuperEntity = Boolean.FALSE;
	/**
	 * 基础业务字段
	 */
	private String[] superEntityColumns = {"id", "create_time", "create_user", "update_time", "update_user", "status", "is_deleted"};
	/**
	 * 租户字段
	 */
	private String tenantColumn = "tenant_code";
	/**
	 * 是否启用swagger
	 */
	private Boolean isSwagger2 = Boolean.TRUE;

	public void run() {

		Properties props = getProperties();

		AutoGenerator autoGenerator = new AutoGenerator();

		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setOutputDir(getOutputDirOfJava());
		globalConfig.setAuthor(props.getProperty("author"));
		globalConfig.setFileOverride(true);
		globalConfig.setOpen(false);
		globalConfig.setActiveRecord(false);
		globalConfig.setEnableCache(false);
		globalConfig.setBaseResultMap(true);
		globalConfig.setBaseColumnList(true);
		globalConfig.setMapperName("%sMapper");
		globalConfig.setXmlName("%sMapper");
		globalConfig.setServiceName("I%sService");
		globalConfig.setServiceImplName("%sServiceImpl");
		globalConfig.setControllerName("%sController");
		globalConfig.setSwagger2(isSwagger2);
		autoGenerator.setGlobalConfig(globalConfig);




		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		String driverName = props.getProperty("spring.datasource.driver-class-name");
		if (StringUtil.containsAny(driverName, DbType.MYSQL.getDb())) {
			dataSourceConfig.setDbType(DbType.MYSQL);
			dataSourceConfig.setTypeConvert(new MySqlTypeConvert());
		} else {
			dataSourceConfig.setDbType(DbType.POSTGRE_SQL);
			dataSourceConfig.setTypeConvert(new PostgreSqlTypeConvert());
		}
		dataSourceConfig.setUrl(props.getProperty("spring.datasource.url"));
		dataSourceConfig.setDriverName(driverName);
		dataSourceConfig.setUsername(props.getProperty("spring.datasource.username"));
		dataSourceConfig.setPassword(props.getProperty("spring.datasource.password"));
		autoGenerator.setDataSource(dataSourceConfig);



		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// strategy.setCapitalMode(true);// 全局大写命名
		// strategy.setDbColumnUnderline(true);//全局下划线命名
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setTablePrefix(tablePrefix);
		if (tablesInclude.length > 0) {
			strategy.setInclude(tablesInclude);
		}
		if (tablesExclude.length > 0) {
			strategy.setExclude(tablesExclude);
		}
		if (hasSuperEntity) {
			strategy.setSuperEntityClass("org.easy.tool.support.BaseEntity");
			strategy.setSuperEntityColumns(superEntityColumns);
			strategy.setSuperServiceClass("org.easy.mybatisplus.base.BaseService");
			strategy.setSuperServiceImplClass("org.easy.mybatisplus.base.BaseServiceImpl");
		} else {
			strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
			strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
		}
		// 自定义 controller 父类
		//strategy.setSuperControllerClass("org.springblade.core.boot.ctrl.BladeController");
		strategy.setEntityBuilderModel(false);
		strategy.setEntityLombokModel(true);
		strategy.setControllerMappingHyphenStyle(true);
		autoGenerator.setStrategy(strategy);



		// 包配置
		PackageConfig packageConfig = new PackageConfig();
		packageConfig.setModuleName(null);
		packageConfig.setParent(packageName);
		packageConfig.setController("controller");
		packageConfig.setEntity("entity");
		packageConfig.setXml("mapper");
		packageConfig.setMapper("mapper");
		autoGenerator.setPackageInfo(packageConfig);


		autoGenerator.setCfg(getInjectionConfig());
		autoGenerator.execute();
	}

	private InjectionConfig getInjectionConfig() {

		String servicePackage = serviceName.split("-").length > 1 ? serviceName.split("-")[1] : serviceName;
		// 自定义配置
		Map<String, Object> map = new HashMap<>(16);
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				map.put("codeName", codeName);
				map.put("serviceName", serviceName);
				map.put("servicePackage", servicePackage);
				map.put("tenantColumn", tenantColumn);
				this.setMap(map);
			}
		};

		List<FileOutConfig> focList = new ArrayList<>();

		//生成Java文件
		focList.add(new FileOutConfig("/templates/sql/menu.sql.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				map.put("entityKey", (tableInfo.getEntityName().toLowerCase()));
				return getOutputDirOfJava() + "/" + "/sql/menu.mysql";
			}
		});
		focList.add(new FileOutConfig("/templates/entityVO.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDirOfJava() + "/" + packageName.replace(".", "/") + "/" + "vo" + "/" + tableInfo.getEntityName() + "VO" + StringPool.DOT_JAVA;
			}
		});
		focList.add(new FileOutConfig("/templates/entityDTO.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDirOfJava() + "/" + packageName.replace(".", "/") + "/" + "dto" + "/" + tableInfo.getEntityName() + "DTO" + StringPool.DOT_JAVA;
			}
		});
		focList.add(new FileOutConfig("/templates/wrapper.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDirOfJava() + "/" + packageName.replace(".", "/") + "/" + "wrapper" + "/" + tableInfo.getEntityName() + "Wrapper" + StringPool.DOT_JAVA;
			}
		});

		//生成Web Js文件
//		if (Func.isNotBlank(webPath)) {
//			if (Func.equals(webName, SystemConstant.SWORD_NAME)) {
//				focList.add(new FileOutConfig("/templates/sword/action.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/actions" + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/sword/model.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/models" + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/sword/service.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/services" + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/sword/list.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/pages" + "/" + StringUtil.firstCharToUpper(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + ".js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/sword/add.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/pages" + "/" + StringUtil.firstCharToUpper(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "Add.js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/sword/edit.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/pages" + "/" + StringUtil.firstCharToUpper(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "Edit.js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/sword/view.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/pages" + "/" + StringUtil.firstCharToUpper(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "View.js";
//					}
//				});
//			} else if (Func.equals(webName, SystemConstant.SABER_NAME)) {
//				focList.add(new FileOutConfig("/templates/saber/api.js.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/api" + "/" + servicePackage.toLowerCase() + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
//					}
//				});
//				focList.add(new FileOutConfig("/templates/saber/crud.vue.vm") {
//					@Override
//					public String outputFile(TableInfo tableInfo) {
//						return getOutputDirOfWeb() + "/views" + "/" + servicePackage.toLowerCase() + "/" + tableInfo.getEntityName().toLowerCase() + ".vue";
//					}
//				});
//			}
//		}


		cfg.setFileOutConfigList(focList);
		return cfg;
	}

	/**
	 * 获取配置文件
	 *
	 * @return 配置Props
	 */
	private Properties getProperties() {
		// 读取配置文件
		Resource resource = new ClassPathResource("/templates/props/generator.properties");
		Properties props = new Properties();
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	/**
	 * 生成到项目中
	 *
	 * @return outputDir
	 */
	public String getOutputDirOfJava() {
		return (Func.isBlank(packagePath) ? System.getProperty("user.dir") + "/code-generator/"+getServiceName() : packagePath) + "/src/main/java";
	}

	/**
	 * 生成到Web项目中
	 *
	 * @return outputDir
	 */
	public String getOutputDirOfWeb() {
		return (Func.isBlank(webPath) ? System.getProperty("user.dir") : webPath) + "/src";
	}

	/**
	 * 页面生成的文件名
	 */
	private String getGeneratorViewPath(String viewOutputDir, TableInfo tableInfo, String suffixPath) {
		String name = StringUtils.firstToLowerCase(tableInfo.getEntityName());
		String path = viewOutputDir + "/" + name + "/" + name + suffixPath;
		File viewDir = new File(path).getParentFile();
		if (!viewDir.exists()) {
			viewDir.mkdirs();
		}
		return path;
	}

}

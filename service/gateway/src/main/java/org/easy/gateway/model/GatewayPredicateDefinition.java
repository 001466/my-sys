package org.easy.gateway.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ApiModel(value = "predicates" ,description = "predicates")
public class GatewayPredicateDefinition {
	//断言对应的Name
	private String name;
	//配置的断言规则
	private Map<String, String> args = new LinkedHashMap<>();
}

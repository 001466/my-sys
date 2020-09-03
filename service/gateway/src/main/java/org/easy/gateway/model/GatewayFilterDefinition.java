package org.easy.gateway.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ApiModel(value = "filters" ,description = "filters")
public class GatewayFilterDefinition {
	//Filter Name
    private String name;
    //对应的路由规则
    private Map<String, String> args = new LinkedHashMap<>();
    //此处省略Get和Set方法
}

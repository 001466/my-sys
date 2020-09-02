package org.easy.system.feign;

import io.swagger.annotations.ApiOperation;
import org.easy.system.AutoConfiguration;
import org.easy.system.vo.ClientVO;
import org.easy.tool.web.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = AutoConfiguration.SERVICE_NAME,path = IClientFeign.PATH,fallback = IClientFeignFallback.class
)

public interface IClientFeign {

    String PATH="/client";

    @RequestMapping(value = "/client-info",method = RequestMethod.GET)
    public R<ClientVO> loadClientByClientId(@RequestParam("clientId") String clientId);
}

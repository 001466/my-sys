package org.easy.system.feign;

import org.easy.system.vo.ClientVO;
import org.easy.tool.web.R;
import org.springframework.stereotype.Component;

@Component
public class IClientFeignFallback implements IClientFeign{
    @Override
    public R<ClientVO> loadClientByClientId(String clientId) {
        return R.fail();
    }
}

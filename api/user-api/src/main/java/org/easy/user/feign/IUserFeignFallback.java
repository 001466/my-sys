package org.easy.user.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.easy.mybatisplus.support.Query;
import org.easy.tool.web.R;
import org.easy.user.entity.User;
import org.easy.user.entity.UserInfo;
import org.easy.user.vo.UserVO;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class IUserFeignFallback implements IUserFeign{
    @Override
    public R<UserInfo> loadUserByUsername(String account) {
        return R.fail();
    }

    @Override
    public R submit(@Valid User user) {
        return R.fail();
    }

    @Override
    public R<IPage<UserVO>> list(User user, Query query) {
        return R.fail();
    }
}

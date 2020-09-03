package org.easy.user.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.easy.tool.web.R;
import org.easy.user.AutoConfiguration;
import org.easy.user.entity.User;
import org.easy.user.entity.UserInfo;
import org.easy.user.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.validation.Valid;

@FeignClient(
        value = AutoConfiguration.SERVICE_NAME,path = IUserFeign.PATH,fallback = IUserFeignFallback.class
)
public interface IUserFeign {
    String PATH="/user";

    @RequestMapping(value = "/user-info",method = RequestMethod.GET)
    R<UserInfo> loadUserByUsername(@RequestParam("account") String account);

    @PostMapping("/submit")
    R submit(@Valid @RequestBody User user);

    @GetMapping("/list")
    @ApiOperation(value = "分页", notes = "传入user", position = 2)
    R<IPage<UserVO>> list(User user, org.easy.mybatisplus.support.Query query) ;
}

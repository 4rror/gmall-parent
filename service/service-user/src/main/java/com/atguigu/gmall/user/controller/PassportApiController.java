package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user/passport")
public class PassportApiController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * /api/user/passport/login
     * 用户登录接口
     */
    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        Map<String, String> result = userInfoService.login(userInfo, request);
        if (!CollectionUtils.isEmpty(result)) {
            return Result.ok(result);
        } else {
            return Result.fail().message("用户名或密码错误");
        }
    }
}

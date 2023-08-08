package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserInfoService {
    /**
     * 用户登录接口
     */
    Map<String, String> login(UserInfo userInfo, HttpServletRequest request);
}

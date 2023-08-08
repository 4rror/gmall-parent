package com.atguigu.gmall.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Map<String, String> login(UserInfo userInfo, HttpServletRequest request) {
        String password = DigestUtils.md5DigestAsHex(userInfo.getPasswd().getBytes());
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserInfo::getLoginName, userInfo.getLoginName())
                .eq(UserInfo::getPasswd, password);

        UserInfo userInfoFromDb = userInfoMapper.selectOne(queryWrapper);

        if (userInfoFromDb != null) {
            JSONObject jsonObject = new JSONObject();
            // 生成token
            String token = UUID.randomUUID().toString().replaceAll("-", " ");
            String ip = IpUtil.getIpAddress(request);
            jsonObject.put("ip", ip);
            jsonObject.put("userId", userInfoFromDb.getId());
            // 封装数据存储到redis
            stringRedisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + token, jsonObject.toString(), RedisConst.USERKEY_TIMEOUT, TimeUnit.SECONDS);

            // 封装数据响应到页面
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("nickName", userInfoFromDb.getNickName());
            return map;
        }
        return null;
    }
}

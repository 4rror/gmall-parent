package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserAddress;

import java.util.List;

public interface UserAddressService {
    /**
     * 获取用户地址
     */
    List<UserAddress> findUserAddressList(Long userId);
}

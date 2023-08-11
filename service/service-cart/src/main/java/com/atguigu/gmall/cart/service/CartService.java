package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;

import java.util.List;

public interface CartService {

    /**
     * 添加购物车
     *
     * @param skuId  商品id
     * @param skuNum 数量
     * @param userId 用户id
     */
    void addToCart(Long skuId, Integer skuNum, String userId);

    /**
     * 查询购物车列表
     *
     * @param userId 用户id
     */
    List<CartInfo> cartList(String userId);

    /**
     * 更新选中状态
     *
     * @param userId    用户id
     * @param skuId     商品id
     * @param isChecked 是否被选中
     */
    void checkCart(String userId, Long skuId, Integer isChecked);

    /**
     * 删除购物车项
     *
     * @param userId 用户id
     * @param skuId  商品id
     */
    void deleteCart(String userId, Long skuId);
}

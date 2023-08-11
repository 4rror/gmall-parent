package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    public void addToCart(Long skuId, Integer skuNum, String userId) {
        String cartKey = getCartKey(userId);
        BoundHashOperations<String, Object, CartInfo> boundHashOps = redisTemplate.boundHashOps(cartKey);

        // 存在当前sku的key，购物车计算
        CartInfo cartInfo = null;
        if (Boolean.TRUE.equals(boundHashOps.hasKey(String.valueOf(skuId)))) {
            cartInfo = boundHashOps.get(String.valueOf(skuId));
            if (cartInfo != null) {
                BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
                cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
                cartInfo.setIsChecked(1);
                cartInfo.setSkuPrice(skuPrice);
                cartInfo.setUpdateTime(new Date());
            }
        } else {
            // 购物车中新建sku项
            cartInfo = new CartInfo();
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setCartPrice(skuPrice);
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        if (cartInfo != null) {
            boundHashOps.put(String.valueOf(skuId), cartInfo);
            boundHashOps.expire(RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
        }
    }

    @Override
    public List<CartInfo> cartList(String userId) {
        String cartKey = getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOps = redisTemplate.boundHashOps(cartKey);

        List<CartInfo> cartInfoList = boundHashOps.values();
        if (!CollectionUtils.isEmpty(cartInfoList)) {
            // 展示购物车时排序，使用更改时间排序
            cartInfoList.sort((c1, c2) ->
                    DateUtil.truncatedCompareTo(c2.getUpdateTime(), c1.getUpdateTime(), Calendar.SECOND)
            );
        }
        return cartInfoList;
    }

    @Override
    public void checkCart(String userId, Long skuId, Integer isChecked) {
        String cartKey = getCartKey(userId);

        BoundHashOperations<String, Object, CartInfo> boundHashOps = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = boundHashOps.get(String.valueOf(skuId));
        if (cartInfo == null) return;
        cartInfo.setIsChecked(isChecked);
        boundHashOps.put(String.valueOf(skuId), cartInfo);
    }

    @Override
    public void deleteCart(String userId, Long skuId) {
        String cartKey = getCartKey(userId);

        BoundHashOperations<String, Object, CartInfo> boundHashOps = redisTemplate.boundHashOps(cartKey);
        if (boundHashOps.hasKey(String.valueOf(skuId))) {
            boundHashOps.delete(String.valueOf(skuId));
        }
    }

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {
        String cartKey = getCartKey(userId);

        BoundHashOperations<String, Object, CartInfo> boundHashOps = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> cartInfoList = boundHashOps.values();
        if (CollectionUtils.isEmpty(cartInfoList)) return new ArrayList<>();

        return cartInfoList.stream().filter(cartInfo -> {
            if (cartInfo.getIsChecked() == 1) {
                // 确认商品价格
                BigDecimal skuPrice = productFeignClient.getSkuPrice(cartInfo.getSkuId());
                cartInfo.setCartPrice(skuPrice);
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    private String getCartKey(String userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }
}

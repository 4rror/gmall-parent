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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            cartInfo.setSkuPrice(skuPrice);
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        if (cartInfo != null) {
            boundHashOps.put(String.valueOf(skuId), cartInfo);
            boundHashOps.expire(RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
        }
    }

    // @Override
    // public void checkCart(String userId, Long skuId, Integer isChecked) {
    //     String cartKey = getCartKey(userId);
    //
    //     BoundHashOperations<String, Object, CartInfo> boundHashOps = redisTemplate.boundHashOps(cartKey);
    //     CartInfo cartInfo = boundHashOps.get(String.valueOf(skuId));
    //     if (cartInfo == null) return;
    //     cartInfo.setIsChecked(isChecked);
    //     boundHashOps.put(String.valueOf(skuId), cartInfo);
    // }

    @Override
    public List<CartInfo> cartList(String userId, String userTempId) {
        List<CartInfo> tempCartInfoList = null;
        BoundHashOperations<String, Object, CartInfo> tempBoundHashOps = null;
        String tempCartKey = getCartKey(userTempId);
        if (!StringUtils.isEmpty(userTempId)) {
            tempBoundHashOps = redisTemplate.boundHashOps(tempCartKey);
            tempCartInfoList = tempBoundHashOps.values();
        }

        // 用户未登录，直接返回临时用户的购物车
        if (StringUtils.isEmpty(userId)) {
            return tempCartInfoList;
        }

        String cartKey = getCartKey(userId);
        BoundHashOperations<String, Object, CartInfo> userBoundHashOps = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> cartInfoList = userBoundHashOps.values();

        // 用户的购物车如果为空，将临时用户的购物车排序，保存到用户的购物车
        if (CollectionUtils.isEmpty(cartInfoList)) {
            if (CollectionUtils.isEmpty(tempCartInfoList)) return new ArrayList<>();
            tempCartInfoList.sort((o1, o2) -> DateUtil.truncatedCompareTo(o2.getUpdateTime(), o1.getUpdateTime(), Calendar.SECOND));
            cartInfoList = new ArrayList<>(tempCartInfoList);
            cartInfoList.forEach(cartInfo -> userBoundHashOps.put(String.valueOf(cartInfo.getSkuId()), cartInfo));
            return cartInfoList;
        }

        if (CollectionUtils.isEmpty(tempCartInfoList)) return cartInfoList;

        // 用户购物车和临时用户购物车都有数据
        List<CartInfo> tmp = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            tempCartInfoList.forEach(t -> {
                if (Objects.equals(t.getSkuId(), cartInfo.getSkuId())) {
                    cartInfo.setSkuNum(cartInfo.getSkuNum() + t.getSkuNum());
                    cartInfo.setSkuPrice(productFeignClient.getSkuPrice(cartInfo.getSkuId()));
                    cartInfo.setUpdateTime(new Date());
                } else tmp.add(t);
            });
        }
        cartInfoList.addAll(tmp);
        // tempBoundHashOps.delete();
        Object[] array = tempCartInfoList.stream().map(i -> String.valueOf(i.getSkuId())).toArray();
        redisTemplate.opsForHash().delete(tempCartKey, array);
        cartInfoList.forEach(i -> userBoundHashOps.put(String.valueOf(i.getSkuId()), i));

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
                cartInfo.setSkuPrice(skuPrice);
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    private String getCartKey(String userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }
}

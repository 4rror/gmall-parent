package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.cart.CartInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * /api/cart/deleteCart/{skuId}
     * 删除购物车项
     */
    @ApiOperation("删除购物车项")
    @DeleteMapping("/deleteCart/{skuId}")
    public Result<Object> deleteCart(@PathVariable Long skuId, HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        if (StringUtils.isEmpty(userId))
            userId = AuthContextHolder.getUserTempId(request);
        cartService.deleteCart(userId, skuId);
        return Result.ok();
    }

    /**
     * /api/cart/checkCart/{skuId}/{isChecked}
     * 更新选中状态
     */
    @GetMapping("/checkCart/{skuId}/{isChecked}")
    @ApiOperation("更新选中状态")
    public Result<Object> checkCart(@PathVariable Long skuId, @PathVariable Integer isChecked, HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        if (StringUtils.isEmpty(userId))
            userId = AuthContextHolder.getUserTempId(request);
        cartService.checkCart(userId, skuId, isChecked);
        return Result.ok();
    }

    /**
     * /api/cart/cartList
     * 查询购物车列表
     */
    @ApiOperation("查询购物车列表")
    @GetMapping("/cartList")
    public Result<List<CartInfo>> cartList(HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        if (StringUtils.isEmpty(userId))
            userId = AuthContextHolder.getUserTempId(request);
        List<CartInfo> cartInfoList = cartService.cartList(userId);
        return Result.ok(cartInfoList);
    }

    /**
     * /api/cart/addToCart/{skuId}/{skuNum}
     * 添加购物车
     */
    @ApiOperation("添加购物车")
    @GetMapping("/addToCart/{skuId}/{skuNum}")
    public Result<Object> addToCart(@PathVariable Long skuId, @PathVariable Integer skuNum, HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        String userTempId = AuthContextHolder.getUserTempId(request);
        if (!StringUtils.isEmpty(userId)) {
            cartService.addToCart(skuId, skuNum, userId);
        } else if (!StringUtils.isEmpty(userTempId)) {
            cartService.addToCart(skuId, skuNum, userTempId);
        } else {
            return Result.fail();
        }
        return Result.ok();
    }
}

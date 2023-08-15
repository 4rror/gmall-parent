package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.cart.client.CartFeignClient;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.product.client.ProductFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private CartFeignClient cartFeignClient;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * /api/order/auth/submitOrder
     * 提交订单
     */
    @ApiOperation("提交订单")
    @PostMapping("/auth/submitOrder")
    public Result<Object> submitOrder(@RequestBody OrderInfo orderInfo, HttpServletRequest request) {
        // 获取用户id
        String userId = AuthContextHolder.getUserId(request);
        orderInfo.setUserId(Long.valueOf(userId));

        // 提交订单前检验流水号
        String tradeNo = request.getParameter("tradeNo");
        boolean flag = orderInfoService.checkTradeNo(userId, tradeNo);
        if (!flag) {
            return Result.fail().message("订单重复提交");
        }

        for (OrderDetail orderDetail : orderInfo.getOrderDetailList()) {
            // 库存检查
            boolean hasStock = orderInfoService.checkStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
            if (!hasStock) {
                // 处理
                return Result.fail().message(orderDetail.getSkuName() + " 库存不足");
            }

            // 金额检查
            BigDecimal skuPrice = productFeignClient.getSkuPrice(orderDetail.getSkuId());
            if (orderDetail.getOrderPrice().compareTo(skuPrice) != 0) {
                // 重新获取
                List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);
                // 更新redis
                for (CartInfo cartInfo : cartCheckedList) {
                    redisTemplate.opsForHash().put(
                            RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX,
                            cartInfo.getSkuId().toString(),
                            cartInfo);
                }
                return Result.fail().message(orderDetail.getSkuName() + " 价格变动，请从购物车重新提交");
            }

        }


        Long orderId = orderInfoService.submitOrder(orderInfo);

        // 删除流水号
        orderInfoService.deleteTradeNo(userId);

        return Result.ok(orderId);
    }

    /**
     * /api/order/auth/trade
     * 去结算
     */
    @ApiOperation("去结算")
    @GetMapping("/auth/trade")
    public Result<Map<String, Object>> trade(HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        Map<String, Object> map = orderInfoService.trade(userId);
        return Result.ok(map);
    }
}

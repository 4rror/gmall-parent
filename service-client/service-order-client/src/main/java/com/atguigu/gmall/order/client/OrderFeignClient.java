package com.atguigu.gmall.order.client;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "service-order")
public interface OrderFeignClient {

    /**
     * /api/order/auth/getOrderInfo/{orderId}
     * 根据id查询订单详情
     */
    @GetMapping("/api/order/auth/getOrderInfo/{orderId}")
    Result<OrderInfo> getOrderInfo(@PathVariable Long orderId);

    /**
     * /api/order/auth/trade
     * 去结算
     */
    @GetMapping("/api/order/auth/trade")
    Result<Map<String, Object>> trade();
}

package com.atguigu.gmall.order.client;

import com.atguigu.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(value = "service-order")
public interface OrderFeignClient {
    /**
     * /api/order/auth/trade
     * 去结算
     */
    @GetMapping("/api/order/auth/trade")
    Result<Map<String, Object>> trade();
}

package com.atguigu.gmall.order.client.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.client.OrderFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderDegradeFeignClient implements OrderFeignClient {
    @Override
    public Result<OrderInfo> getOrderInfo(Long orderId) {
        return Result.fail();
    }

    @Override
    public Result<Map<String, Object>> trade() {
        return Result.fail();
    }
}

package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderInfo;

import java.util.Map;

public interface OrderInfoService {
    /**
     * 去结算
     */
    Map<String, Object> trade(String userId);

    /**
     * 提交订单
     */
    Long submitOrder(OrderInfo orderInfo);
}

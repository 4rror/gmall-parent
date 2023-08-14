package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderInfo;

import java.util.Map;

public interface OrderInfoService {

    void deleteTradeNo(String userId);

    String getTradeNo(String userId);

    /**
     * 检查当前用户下单的流水号是否正确
     */
    boolean checkTradeNo(String userId, String tradeNo);

    /**
     * 去结算
     */
    Map<String, Object> trade(String userId);

    /**
     * 提交订单
     */
    Long submitOrder(OrderInfo orderInfo);
}

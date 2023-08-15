package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

    /**
     * 检验库存
     */
    boolean checkStock(Long skuId, Integer skuNum);

    /**
     * 根据用户id获取订单列表
     */
    IPage<OrderInfo> orderInfoPage(String userId, Long page, Integer limit);
}

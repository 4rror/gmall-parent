package com.atguigu.gmall.payment.service;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.payment.PaymentInfo;

import java.util.Map;

public interface PaymentInfoService {
    /**
     * 保存支付记录
     */
    void savePaymentInfo(OrderInfo orderInfo);

    /**
     * 根据outTradeNo获取支付记录
     */
    PaymentInfo getPaymentInfo(String outTradeNo, String name);

    /**
     * 修改支付记录为成功状态
     */
    boolean paySuccess(String outTradeNo, String name, Map<String, String> paramsMap);

    /**
     * 更新支付记录状态
     */
    void updatePaymentInfo(String out_trade_no, String paymentType, PaymentInfo paymentInfo);
}

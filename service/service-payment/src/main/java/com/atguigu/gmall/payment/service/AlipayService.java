package com.atguigu.gmall.payment.service;

public interface AlipayService {
    /**
     * 预支付功能
     */
    String submit(Long orderId);

    /**
     * 退款
     */
    boolean refund(Long orderId);
}

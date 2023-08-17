package com.atguigu.gmall.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.payment.PaymentInfo;
import com.atguigu.gmall.order.client.OrderFeignClient;
import com.atguigu.gmall.payment.config.AlipayClientConfig;
import com.atguigu.gmall.payment.service.AlipayService;
import com.atguigu.gmall.payment.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Resource
    private OrderFeignClient orderFeignClient;

    @Override
    public String submit(Long orderId) {
        // 判断订单状态情况
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        // 判断
        if (orderInfo != null && !"UNPAID".equals(orderInfo.getOrderStatus())) {
            return "订单状态已关闭或者订单已支付！！！";
        }

        // 保存支付记录
        paymentInfoService.savePaymentInfo(orderInfo);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(AlipayClientConfig.notifyPaymentUrl);
        // 同步跳转地址，仅支持http/https
        request.setReturnUrl(AlipayClientConfig.returnPaymentUrl);
        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        // 商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", orderInfo.getOutTradeNo());
        // 支付金额，最小值0.01元
        bizContent.put("total_amount", 0.01);
        // 订单标题，不可使用特殊符号
        bizContent.put("subject", orderInfo.getTradeBody());
        // 电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        /******可选参数******/
        bizContent.put("timeout_express", "3m");


        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }
}

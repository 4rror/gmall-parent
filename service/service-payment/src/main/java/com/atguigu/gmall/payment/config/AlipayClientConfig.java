package com.atguigu.gmall.payment.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayClientConfig implements InitializingBean {

    public static String alipayPublicKey;
    public static String alipayUrl;
    public static String appId;
    public static String appPrivateKey;
    public static String notifyPaymentUrl;
    public static String returnOrderUrl;
    public static String returnPaymentUrl;

    public final static String format = "json";
    public final static String charset = "utf-8";
    public final static String signType = "RSA2";

    @Value("${alipay_public_key}")
    private String postAlipayPublicKey;
    @Value("${alipay_url}")
    private String postAlipayUrl;
    @Value("${app_id}")
    private String postAppId;
    @Value("${app_private_key}")
    private String postAppPrivateKey;
    @Value("${notify_payment_url}")
    private String postNotifyPaymentUrl;
    @Value("${return_order_url}")
    private String postReturnOrderUrl;
    @Value("${return_payment_url}")
    private String postReturnPaymentUrl;


    @Override
    public void afterPropertiesSet() {
        alipayPublicKey = postAlipayPublicKey;
        alipayUrl = postAlipayUrl;
        appId = postAppId;
        appPrivateKey = postAppPrivateKey;
        notifyPaymentUrl = postNotifyPaymentUrl;
        returnOrderUrl = postReturnOrderUrl;
        returnPaymentUrl = postReturnPaymentUrl;
    }

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                alipayUrl,
                appId,
                appPrivateKey,
                format,
                charset,
                alipayPublicKey,
                signType
        );
    }
}

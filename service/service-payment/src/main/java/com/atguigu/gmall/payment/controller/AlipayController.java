package com.atguigu.gmall.payment.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.gmall.model.enums.PaymentType;
import com.atguigu.gmall.model.payment.PaymentInfo;
import com.atguigu.gmall.payment.config.AlipayClientConfig;
import com.atguigu.gmall.payment.service.AlipayService;
import com.atguigu.gmall.payment.service.PaymentInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api/payment/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 异步回调
     */
    @SneakyThrows
    @ResponseBody
    @PostMapping("/callback/notify")
    public String callbackNotify(@RequestParam Map<String, String> paramsMap) {
        // 验签
        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(
                paramsMap,
                AlipayClientConfig.alipayPublicKey,
                AlipayClientConfig.charset,
                AlipayClientConfig.signType);
        // 验签成功
        if (signVerified) {
            // 获取商家交易订单编号
            String outTradeNo = paramsMap.get("out_trade_no");
            // 获取支付宝返回的支付金额
            String totalAmount = paramsMap.get("total_amount");
            // 获取app_id
            String appId = paramsMap.get("app_id");
            // 获取支付记录中支付状态
            String tradeStatus = paramsMap.get("trade_status");
            // 获取notify_id
            String notifyId = paramsMap.get("notify_id");
            // 根据out_trade_no查询支付记录--支付宝
            PaymentInfo paymentInfo = paymentInfoService.getPaymentInfo(outTradeNo, PaymentType.ALIPAY.name());
            // 判断
            if (paymentInfo != null
                    && new BigDecimal("0.01").compareTo(new BigDecimal(totalAmount)) == 0
                    && AlipayClientConfig.appId.equals(appId)) {
                // 判断
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    // 存储异步回调执行的唯一标识
                    Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(notifyId, notifyId, 1464, TimeUnit.MINUTES);
                    if (result) {
                        // 修改支付记录paymentInfo状态为PAID
                        boolean flag = paymentInfoService.paySuccess(outTradeNo, PaymentType.ALIPAY.name(), paramsMap);
                        if (flag) {
                            return "success";
                        }
                    }
                }
                return "failure";
            }
            // 响应成功的条件必须是TRADE_SUCCESS 或 TRADE_FINISHED
            return "failure";
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
        } else {
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            return "failure";
        }

    }

    /**
     * 重定向到
     */
    @GetMapping("/callback/return")
    public String callbackReturn() {
        // 重定向到成功界面
        return "redirect:" + AlipayClientConfig.returnOrderUrl;
    }

    /**
     * /api/payment/alipay/submit/{orderId}
     * 对接支付-预支付功能
     */
    @ResponseBody
    @GetMapping("/submit/{orderId}")
    public String submit(@PathVariable Long orderId) {
        return alipayService.submit(orderId);
    }
}

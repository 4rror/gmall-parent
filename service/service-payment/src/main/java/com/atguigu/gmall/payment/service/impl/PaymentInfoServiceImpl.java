package com.atguigu.gmall.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.service.RabbitService;
import com.atguigu.gmall.model.enums.PaymentStatus;
import com.atguigu.gmall.model.enums.PaymentType;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.payment.PaymentInfo;
import com.atguigu.gmall.payment.mapper.PaymentInfoMapper;
import com.atguigu.gmall.payment.service.PaymentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitService rabbitService;

    @Override
    public void savePaymentInfo(OrderInfo orderInfo) {
        // 封装记录对象
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID.name());
        paymentInfo.setTotalAmount(orderInfo.getTotalAmount());
        paymentInfo.setSubject(orderInfo.getTradeBody());
        paymentInfo.setPaymentType(PaymentType.ALIPAY.name());

        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo, String name) {
        LambdaQueryWrapper<PaymentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentInfo::getOutTradeNo, outTradeNo);
        queryWrapper.eq(PaymentInfo::getPaymentType, name);
        return paymentInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean paySuccess(String outTradeNo, String name, Map<String, String> paramsMap) {
        try {
            // 封装修改数据对象
            PaymentInfo paymentInfo = new PaymentInfo();
            // 设置支付状态
            paymentInfo.setPaymentStatus(PaymentStatus.PAID.name());
            // 设置支付宝交易编号
            paymentInfo.setTradeNo(paramsMap.get("trade_no"));
            // 设置回调时间
            paymentInfo.setCallbackTime(new Date());
            // 设置回调信息
            paymentInfo.setCallbackContent(JSONObject.toJSONString(paramsMap));
            this.updatePaymentInfo(outTradeNo, name, paymentInfo);
            // 发送更新订单状态的消息
            rabbitService.sendMessage(
                    MqConst.EXCHANGE_DIRECT_PAYMENT_PAY,
                    MqConst.ROUTING_PAYMENT_PAY,
                    outTradeNo
            );
            return true;
        } catch (Exception e) {
            // 删除redis中notify_id
            stringRedisTemplate.delete(paramsMap.get("notify_id"));
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updatePaymentInfo(String out_trade_no, String paymentType, PaymentInfo paymentInfo) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no", out_trade_no);
        queryWrapper.eq("payment_type", paymentType);
        paymentInfoMapper.update(paymentInfo, queryWrapper);
    }
}

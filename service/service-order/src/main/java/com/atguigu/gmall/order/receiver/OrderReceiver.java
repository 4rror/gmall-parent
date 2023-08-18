package com.atguigu.gmall.order.receiver;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class OrderReceiver {

    @Autowired
    private OrderInfoService orderInfoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_WARE_ORDER, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_WARE_ORDER),
            key = {MqConst.ROUTING_WARE_ORDER}
    ))
    @SneakyThrows
    public void deductionWare(String mapJson, Message message, Channel channel) {
        Map<String, String> map = JSONObject.parseObject(mapJson, Map.class);
        String orderId = map.get("orderId");
        String status = map.get("status");
        if ("DEDUCTED".equals(status)) {
            // 构件库存成功，更改订单状态为代发货
            orderInfoService.updateOrderStatus(Long.parseLong(orderId), ProcessStatus.WAITING_DELEVER);
        } else {
            // 库存扣减失败，人工处理
            // 补货 || 人工客服
            orderInfoService.updateOrderStatus(Long.parseLong(orderId), ProcessStatus.STOCK_EXCEPTION);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PAYMENT_PAY, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_PAYMENT_PAY),
            key = {MqConst.ROUTING_PAYMENT_PAY}
    ))
    @SneakyThrows
    public void paySuccess(String outTradeNo, Message message, Channel channel) {
        // 判断
        if (!StringUtils.isEmpty(outTradeNo)) {
            // 查询orderInfo
            OrderInfo orderInfo = orderInfoService.getOrderInfoByOutTradeNo(outTradeNo);
            if (orderInfo != null) { // 此时状态可以不判断
                // 修改订单状态
                orderInfoService.updateOrderStatus(orderInfo.getId(), ProcessStatus.PAID);
                // 发送消息，通知仓库
                orderInfoService.sendOrderStatus(orderInfo.getId());
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = MqConst.QUEUE_ORDER_CANCEL)
    @SneakyThrows
    public void cancelOrder(Long orderId, Message message, Channel channel) {
        try {
            if (orderId != null) {
                OrderInfo orderInfo = orderInfoService.getOrderInfoById(orderId);
                if (orderInfo != null
                        && "UNPAID".equals(orderInfo.getOrderStatus())
                        && "UNPAID".equals(orderInfo.getProcessStatus())) {
                    // 订单超时
                    orderInfoService.execExpireOrder(orderId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 确认消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

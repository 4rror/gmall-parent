package com.atguigu.gmall.order.receiver;

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

@Component
public class OrderReceiver {

    @Autowired
    private OrderInfoService orderInfoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PAYMENT_PAY, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_PAYMENT_PAY),
            key = {MqConst.ROUTING_PAYMENT_PAY}
    ))
    public void paySuccess(String outTradeNo, Message message, Channel channel) {
        // 判断
        if (!StringUtils.isEmpty(outTradeNo)) {
            // 查询orderInfo
            OrderInfo orderInfo = orderInfoService.getOrderInfoByOutTradeNo(outTradeNo);
            if (orderInfo != null) { // 此时状态可以不判断
                // 修改订单状态
                orderInfoService.updateOrderStatus(orderInfo.getId(), ProcessStatus.PAID);
            }
        }
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

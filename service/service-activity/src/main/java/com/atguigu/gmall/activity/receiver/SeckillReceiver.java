package com.atguigu.gmall.activity.receiver;

import com.atguigu.gmall.activity.service.SeckillGoodsService;
import com.atguigu.gmall.common.constant.MqConst;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeckillReceiver {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_1),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK),
            key = MqConst.ROUTING_TASK_1
    ))
    public void importToRedis(Message message, Channel channel) {
        log.info("import seckill goods start.");
        seckillGoodsService.importSeckillGoodsToRedis();
        log.info("import seckill goods end.");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

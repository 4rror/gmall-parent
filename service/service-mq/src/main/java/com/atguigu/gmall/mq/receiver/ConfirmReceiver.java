package com.atguigu.gmall.mq.receiver;

import com.atguigu.gmall.mq.config.DeadLetterMqConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ConfirmReceiver {

    @SneakyThrows
    @RabbitListener(queues = DeadLetterMqConfig.queue_dead_2)
    public void get(String msg, Message message, Channel channel) {
        log.info("Receive: {}", msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("Receive queue_dead_2: {}. Delay receive. {}", sdf.format(new Date()), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue.confirm", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "exchange.confirm"),
            key = {"routing.confirm"}))
    public void process(Message message, Channel channel) {
        log.info("RabbitListener: {}", new String(message.getBody()));
        // false 确认一个消息，true 批量确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

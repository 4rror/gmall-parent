package com.atguigu.gmall.mq.receiver;

import com.atguigu.gmall.mq.config.DelayedMqConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DelayReceiver {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @SneakyThrows
    @RabbitListener(queues = DelayedMqConfig.queue_delay_1)
    public void get(String msg, Message message, Channel channel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String messageId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        // redis中使用setnx，将当前消息存储，再次接收消息时就可以避免消息重复消费
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("delay:" + messageId, "0", 20, TimeUnit.SECONDS);
        if (result) {
            // 如果设置成功，消费消息
            log.info("Receive queue_delay_1: {}. Delay receive: {}", sdf.format(new Date()), msg);
            // 消费成功后，更新redis
            stringRedisTemplate.opsForValue().set("delay:" + messageId, "1", 20, TimeUnit.SECONDS);
        } else {
            // 设置不成功，先判断redis中的值是否是未消费，未消费则消费
            String s = stringRedisTemplate.opsForValue().get("delay:" + messageId);
            if ("0".equals(s)) {
                log.info("Receive queue_delay_1: {}. Delay receive: {}", sdf.format(new Date()), msg);
                stringRedisTemplate.opsForValue().set("delay:" + messageId, "1", 20, TimeUnit.SECONDS);
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

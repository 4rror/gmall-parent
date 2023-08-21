package com.atguigu.gmall.common.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.pojo.GmallCorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean sendDelayMessage(String exchange, String routingKey, Object message, int delayTime) {
        //  将发送的消息 赋值到 自定义的实体类
        GmallCorrelationData gmallCorrelationData = new GmallCorrelationData();
        // 声明一个correlationId的变量
        String correlationId = UUID.randomUUID().toString().replaceAll("-", "");
        gmallCorrelationData.setId(correlationId);
        gmallCorrelationData.setExchange(exchange);
        gmallCorrelationData.setRoutingKey(routingKey);
        gmallCorrelationData.setMessage(message);
        // 设置延迟类型
        gmallCorrelationData.setDelay(true);
        // 设置延迟时间
        gmallCorrelationData.setDelayTime(delayTime);

        // 存储redis
        stringRedisTemplate.opsForValue().set(MqConst.MQ_REDIS_PREFIX + correlationId, JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);

        // 发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
            msg.getMessageProperties().setDelay(delayTime * 1000);
            return msg;
        }, gmallCorrelationData);

        return true;
    }

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        //  将发送的消息 赋值到 自定义的实体类
        GmallCorrelationData gmallCorrelationData = new GmallCorrelationData();
        // 声明一个correlationId的变量
        String correlationId = UUID.randomUUID().toString().replaceAll("-", "");
        gmallCorrelationData.setId(correlationId);
        gmallCorrelationData.setExchange(exchange);
        gmallCorrelationData.setRoutingKey(routingKey);
        gmallCorrelationData.setMessage(message);

        // 发送消息的时候，将这个gmallCorrelationData 对象放入缓存。
        stringRedisTemplate.opsForValue().set(MqConst.MQ_REDIS_PREFIX + correlationId, JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);
        //  调用发送消息方法
        // this.rabbitTemplate.convertAndSend(exchange, routingKey, message);
        this.rabbitTemplate.convertAndSend(exchange, routingKey, message, gmallCorrelationData);
        //  默认返回true
        return true;
    }

}

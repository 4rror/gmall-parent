package com.atguigu.gmall.common.config;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.pojo.GmallCorrelationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MqProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 指定rabbitTemplate发布确认回调和消息返回回调
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 用于发布者的确认回调
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送成功：{}", correlationData);
        } else {
            log.error("消息发送失败：{}，错误原因：{}", correlationData, cause);
            this.retrySendMsg(correlationData);
        }
    }

    /**
     * 返回消息的回调
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体：{}", new String(message.getBody()));
        log.info("响应码：{}", replyCode);
        log.info("描述：{}", replyText);
        log.info("交换机：{}", exchange);
        log.info("路由键：{}", routingKey);

        //  获取这个CorrelationData对象的Id  spring_returned_message_correlation
        String correlationDataId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        //  因为在发送消息的时候，已经将数据存储到缓存，通过 correlationDataId 来获取缓存的数据
        String strJson = this.stringRedisTemplate.opsForValue().get(correlationDataId);
        //  消息没有到队列的时候，则会调用重试发送方法
        GmallCorrelationData gmallCorrelationData = JSON.parseObject(strJson, GmallCorrelationData.class);
        //  调用方法  gmallCorrelationData 这对象中，至少的有，交换机，路由键，消息等内容.
        this.retrySendMsg(gmallCorrelationData);
    }

    /**
     * 重试发送方法
     *
     * @param correlationData 父类对象 它下面还有个子类对象 GmallCorrelationData
     */
    private void retrySendMsg(CorrelationData correlationData) {
        //  数据类型转换  统一转换为子类处理
        GmallCorrelationData gmallCorrelationData = (GmallCorrelationData) correlationData;
        //  获取到重试次数 初始值 0
        int retryCount = gmallCorrelationData.getRetryCount();
        //  判断
        if (retryCount >= 3) {
            //  不需要重试了
            log.error("重试次数已到，发送消息失败:" + JSON.toJSONString(gmallCorrelationData));
        } else {
            //  变量更新
            retryCount += 1;
            //  重新赋值重试次数 第一次重试 0->1 1->2 2->3
            gmallCorrelationData.setRetryCount(retryCount);
            log.info("重试次数：{}", retryCount);

            //  更新缓存中的数据
            this.stringRedisTemplate.opsForValue().set(gmallCorrelationData.getId(), JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);

            //  调用发送消息方法 表示发送普通消息  发送消息的时候，不能调用 new RabbitService().sendMsg() 这个方法
            this.rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(), gmallCorrelationData.getRoutingKey(), gmallCorrelationData.getMessage(), gmallCorrelationData);
        }
    }
}

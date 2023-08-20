package com.atguigu.gmall.activity.config;

import com.atguigu.gmall.activity.receiver.MessageReceiver;
import com.atguigu.gmall.common.constant.RedisConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisChannelConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic(RedisConst.SECKILL_STOCK_STATUS_PUBSUB));
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(MessageReceiver messageReceiver) {
        return new MessageListenerAdapter(messageReceiver, "receive");
    }

    @SuppressWarnings("all")
    @Bean
    public RedisTemplate pubSubRedisTemplate(RedisTemplate redisTemplate, RedisConnectionFactory redisConnectionFactory) {
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}

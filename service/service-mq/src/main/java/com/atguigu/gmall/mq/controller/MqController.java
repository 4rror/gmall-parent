package com.atguigu.gmall.mq.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.service.RabbitService;
import com.atguigu.gmall.mq.config.DeadLetterMqConfig;
import com.atguigu.gmall.mq.config.DelayedMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/mq")
public class MqController {


    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDelay")
    public Result<Object> sendDelay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("send:"+sdf.format(new Date()));
        rabbitService.sendRelayMessage(DelayedMqConfig.exchange_delay, DelayedMqConfig.routing_delay, sdf.format(new Date()), 10);

        // this.rabbitTemplate.convertAndSend(DelayedMqConfig.exchange_delay, DelayedMqConfig.routing_delay, sdf.format(new Date()), new MessagePostProcessor() {
        //     @Override
        //     public Message postProcessMessage(Message message) throws AmqpException {
        //         message.getMessageProperties().setDelay(10 * 1000);
        //         log.info("{} Delay sent.", sdf.format(new Date()));
        //         return message;
        //     }
        // });
        return Result.ok();
    }


    @GetMapping("/sendDeadLetter")
    public Result<Object> sendDeadLetter() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rabbitTemplate.convertAndSend(DeadLetterMqConfig.exchange_dead, DeadLetterMqConfig.routing_dead_1, "ok");
        System.out.println(sdf.format(new Date()) + " Delay sent.");
        return Result.ok();
    }

    /**
     * <a href="http://localhost:8282/mq/sendConfirm">http://localhost:8282/mq/sendConfirm</a>
     * 消息发送
     */
    @GetMapping("/sendConfirm")
    public Result<Object> sendConfirm() {
        rabbitService.sendMessage("exchange.confirm", "routing.confirm", "发送消息");
        return Result.ok();
    }
}

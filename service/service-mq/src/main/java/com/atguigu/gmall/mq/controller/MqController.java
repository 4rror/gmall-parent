package com.atguigu.gmall.mq.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.service.RabbitService;
import com.atguigu.gmall.mq.config.DeadLetterMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/mq")
public class MqController {


    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

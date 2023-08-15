package com.atguigu.gmall.mq.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
public class MqController {


    @Autowired
    private RabbitService rabbitService;

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

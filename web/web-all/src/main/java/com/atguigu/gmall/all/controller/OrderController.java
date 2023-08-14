package com.atguigu.gmall.all.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.order.client.OrderFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class OrderController {

    @Resource
    private OrderFeignClient orderFeignClient;

    @GetMapping("/trade.html")
    public String trade(Model model) {

        Result<Map<String, Object>> trade = orderFeignClient.trade();
        model.addAllAttributes(trade.getData());

        return "order/trade";
    }
}

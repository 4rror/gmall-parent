package com.atguigu.gmall.all.controller;

import com.atguigu.gmall.activity.client.ActivityFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.activity.SeckillGoods;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class SeckillGoodsController {
    @Resource
    private ActivityFeignClient activityFeignClient;

    @GetMapping("/seckill.html")
    public String index(Model model) {
        Result<List<SeckillGoods>> result = activityFeignClient.findAll();
        model.addAttribute("list", result.getData());
        return "seckill/index";
    }
}

package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * /api/order/auth/trade
     * 去结算
     */
    @GetMapping("/auth/trade")
    public Result<Map<String, Object>> trade(HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        Map<String, Object> map = orderInfoService.trade(userId);
        return Result.ok(map);
    }
}

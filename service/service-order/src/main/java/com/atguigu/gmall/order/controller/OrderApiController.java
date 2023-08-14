package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * /api/order/auth/submitOrder
     * 提交订单
     */
    @ApiOperation("提交订单")
    @PostMapping("/auth/submitOrder")
    public Result<Object> submitOrder(@RequestBody OrderInfo orderInfo, HttpServletRequest request) {
        // 获取用户id
        String userId = AuthContextHolder.getUserId(request);
        orderInfo.setUserId(Long.valueOf(userId));

        // 提交订单前检验流水号
        String tradeNo = request.getParameter("tradeNo");
        boolean flag = orderInfoService.checkTradeNo(userId, tradeNo);
        if (!flag) {
            return Result.fail().message("订单重复提交");
        }

        Long orderId = orderInfoService.submitOrder(orderInfo);

        // 删除流水号
        orderInfoService.deleteTradeNo(userId);

        return Result.ok(orderId);
    }

    /**
     * /api/order/auth/trade
     * 去结算
     */
    @ApiOperation("去结算")
    @GetMapping("/auth/trade")
    public Result<Map<String, Object>> trade(HttpServletRequest request) {
        String userId = AuthContextHolder.getUserId(request);
        Map<String, Object> map = orderInfoService.trade(userId);
        return Result.ok(map);
    }
}

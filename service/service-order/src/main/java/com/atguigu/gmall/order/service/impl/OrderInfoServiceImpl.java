package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.cart.client.CartFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.user.client.UserFeignClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private CartFeignClient cartFeignClient;

    @Override
    public Map<String, Object> trade(String userId) {

        Map<String, Object> map = new HashMap<>();

        // 用户地址列表
        List<UserAddress> userAddressList = userFeignClient.findUserAddressList(Long.valueOf(userId));
        map.put("userAddressList", userAddressList);

        // 获取用户送货清单
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);
        // 转换数据
        if (!CollectionUtils.isEmpty(cartCheckedList)) {
            AtomicInteger count = new AtomicInteger();
            List<OrderDetail> ordetialList = cartCheckedList.stream().map(cartInfo -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setSkuId(cartInfo.getSkuId());
                orderDetail.setSkuName(cartInfo.getSkuName());
                orderDetail.setImgUrl(cartInfo.getImgUrl());
                orderDetail.setSkuNum(cartInfo.getSkuNum());
                orderDetail.setOrderPrice(cartInfo.getSkuPrice());
                count.addAndGet(orderDetail.getSkuNum());
                return orderDetail;
            }).collect(Collectors.toList());
            map.put("detailArrayList", ordetialList);

            // 计算总金额
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderDetailList(ordetialList);
            orderInfo.sumTotalAmount();
            map.put("totalAmount", orderInfo.getTotalAmount());
            // 计算数量
            map.put("totalNum", count.get());
        }

        return map;
    }
}

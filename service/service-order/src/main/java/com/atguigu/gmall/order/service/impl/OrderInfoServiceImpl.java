package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.cart.client.CartFeignClient;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.order.mapper.OrderDetailMapper;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private CartFeignClient cartFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void deleteTradeNo(String userId) {
        stringRedisTemplate.delete(getTradeKey(userId));
    }

    @Override
    public String getTradeNo(String userId) {
        String tradeNo = UUID.randomUUID().toString().replaceAll("-", "");
        stringRedisTemplate.opsForValue().set(getTradeKey(userId), tradeNo, 1, TimeUnit.HOURS);
        return tradeNo;
    }

    @Override
    public boolean checkTradeNo(String userId, String tradeNo) {
        String tradeNoFromRedis = stringRedisTemplate.opsForValue().get(getTradeKey(userId));
        if (StringUtils.isEmpty(tradeNo)) return false;
        return tradeNo.equals(tradeNoFromRedis);
    }

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
        map.put("tradeNo", getTradeNo(userId));

        return map;
    }

    private String getTradeKey(String userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.TRADENO_SUFFIX;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitOrder(OrderInfo orderInfo) {

        // 补全数据
        orderInfo.sumTotalAmount();
        orderInfo.setTotalAmount(orderInfo.getTotalAmount());
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        // 订单交易编号，支付宝对接
        String outTradeNo = "ATGUIGU" + System.currentTimeMillis() + new Random().nextInt(1000);
        orderInfo.setOutTradeNo(outTradeNo);
        // 拼接订单描述
        StringBuilder sb = new StringBuilder();
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        orderDetailList.forEach(orderDetail -> sb.append(orderDetail.getSkuName()).append(" "));
        // 判断长度
        if (sb.length() >= 50) {
            orderInfo.setTradeBody(sb.substring(0, 50));
        } else {
            orderInfo.setTradeBody(sb.toString().trim());
        }
        // 操作时间
        orderInfo.setOperateTime(new Date());
        // 失效时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date expireTime = calendar.getTime();
        orderInfo.setExpireTime(expireTime);
        // 进度状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());

        orderInfoMapper.insert(orderInfo);
        orderDetailList.forEach(orderDetail -> {
            orderDetail.setOrderId(orderInfo.getId());
            orderDetailMapper.insert(orderDetail);
        });
        return orderInfo.getId();
    }
}

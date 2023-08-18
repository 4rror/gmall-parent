package com.atguigu.gmall.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.cart.client.CartFeignClient;
import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.service.RabbitService;
import com.atguigu.gmall.common.util.HttpClientUtil;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private RabbitService rabbitService;

    @Value("${ware.url}")
    private String stockUrl;

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

        // 发送延迟队列消息
        rabbitService.sendDelayMessage(MqConst.EXCHANGE_DIRECT_ORDER_CANCEL, MqConst.ROUTING_ORDER_CANCEL, orderInfo.getId(), MqConst.DELAY_TIME);

        return orderInfo.getId();
    }

    @Override
    public boolean checkStock(Long skuId, Integer skuNum) {
        String url = stockUrl + "/hasStock?skuId=" + skuId + "&num=" + skuNum;
        String result = HttpClientUtil.doGet(url);
        return "1".equals(result);
    }

    @Override
    public IPage<OrderInfo> orderInfoPage(String userId, Long page, Integer limit) {
        Page<OrderInfo> orderInfoPage = new Page<>(page, limit);

        IPage<OrderInfo> orderInfoIPage = orderInfoMapper.selectPageByUserId(userId, orderInfoPage);
        List<OrderInfo> records = orderInfoIPage.getRecords();
        records.forEach(orderInfo -> {
            String orderStatus = orderInfo.getOrderStatus();
            orderInfo.setOrderStatusName(OrderStatus.getStatusNameByStatus(orderStatus));
        });
        return orderInfoIPage;
    }

    @Override
    public OrderInfo getOrderInfoById(Long orderId) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getId, orderId);

        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        // 设置OrderDetailList
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper);

        orderInfo.setOrderDetailList(orderDetailList);

        return orderInfo;
    }

    @Override
    public void execExpireOrder(Long orderId) {
        updateOrderStatus(orderId, ProcessStatus.CLOSED);
    }

    @Override
    public OrderInfo getOrderInfoByOutTradeNo(String outTradeNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no", outTradeNo);
        return orderInfoMapper.selectOne(queryWrapper);
    }

    /**
     * 更新订单状态
     *
     * @param orderId       订单id
     * @param processStatus 订单状态枚举
     */
    @Override
    public void updateOrderStatus(Long orderId, ProcessStatus processStatus) {
        // 创建修改内容
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(processStatus.getOrderStatus().name());
        orderInfo.setProcessStatus(processStatus.name());
        // 设置条件对象
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getId, orderId);
        // 执行修改
        orderInfoMapper.update(orderInfo, queryWrapper);
    }

    @Override
    public void sendOrderStatus(Long orderId) {
        // 更新订单状态
        updateOrderStatus(orderId, ProcessStatus.NOTIFIED_WARE);

        // 构造库存系统需要的json数据
        String json = initWareOrder(orderId);

        // 发送消息
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_WARE_STOCK, MqConst.ROUTING_WARE_STOCK, json);
    }

    /**
     * 构建json数据
     */
    private String initWareOrder(Long orderId) {
        OrderInfo orderInfo = getOrderInfoById(orderId);

        // 将订单对象转为map
        Map<String, Object> map = initWareOrder(orderInfo);

        return JSONObject.toJSONString(map);
    }

    private Map<String, Object> initWareOrder(OrderInfo orderInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        map.put("consignee", orderInfo.getConsignee());
        map.put("consigneeTel", orderInfo.getConsigneeTel());
        map.put("orderComment", orderInfo.getOrderComment());
        map.put("orderBody", orderInfo.getTradeBody());
        map.put("deliveryAddress", orderInfo.getDeliveryAddress());
        map.put("paymentWay", "1");

        List<Object> details = null;
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        if (!CollectionUtils.isEmpty(orderDetailList)) {
            details = orderDetailList.stream().map(orderDetail -> {
                Map<String, Object> odMap = new HashMap<>();
                odMap.put("skuId", orderDetail.getSkuId());
                odMap.put("skuNum", orderDetail.getSkuNum());
                odMap.put("skuName", orderDetail.getSkuName());
                return odMap;
            }).collect(Collectors.toList());
        }

        map.put("details", details);

        return map;
    }

}

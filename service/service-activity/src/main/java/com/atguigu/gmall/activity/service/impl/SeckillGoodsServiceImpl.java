package com.atguigu.gmall.activity.service.impl;

import com.atguigu.gmall.activity.cache.CacheHelper;
import com.atguigu.gmall.activity.mapper.SeckillGoodsMapper;
import com.atguigu.gmall.activity.service.SeckillGoodsService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.model.activity.SeckillGoods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 1. 商品的列表数据和详情数据
    //   1. 时间 今天
    //   2. 状态 1
    //      0 未开始 1, 可秒杀， 2 秒杀结束
    //   3. 库存必须大于0
    //
    //   redis
    //   key: seckill_goods
    //   field: skuId
    //   value: 秒杀商品对象
    //
    // 2. 控制库存的实现
    //
    // 3. 状态为
    //
    // 存储商品列表到 key-hash
    // SECKILL_GOODS = "seckill:goods"
    // 秒杀的临时订单存储
    // SECKILL_ORDERS = "seckill:orders"
    // 秒杀订单存储
    // SECKILL_ORDERS_USERS = "seckill:orders:users"
    // 秒杀库存数量
    // SECKILL_STOCK_PREFIX = "seckill:stock:"
    // 控制用户抢购量
    // SECKILL_USER = "seckill:user:"
    @Override
    public void importSeckillGoodsToRedis() {
        List<SeckillGoods> seckillGoodsList = selectSeckillableGoods();
        if (!CollectionUtils.isEmpty(seckillGoodsList)) {
            BoundHashOperations<String, String, SeckillGoods> boundHashOperations = redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS);
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                if (boundHashOperations.hasKey(seckillGoods.getId().toString())) {
                    continue;
                }
                // 计算秒杀结束时间差
                Long timeSubtract = DateUtil.getTimeSubtract(seckillGoods.getEndTime(), new Date());
                // 存储到redis
                boundHashOperations.put(seckillGoods.getId().toString(), seckillGoods);
                // 设置过期时间
                boundHashOperations.expire(timeSubtract, TimeUnit.SECONDS);

                // 控制库存
                for (int i = 0; i < seckillGoods.getStockCount(); i++) {
                    redisTemplate.opsForList().leftPush(RedisConst.SECKILL_STOCK_PREFIX + seckillGoods.getSkuId(), seckillGoods.getSkuId().toString());
                }

                // 设置状态为，用于快速判断是否还有库存
                CacheHelper.put(seckillGoods.getId().toString(), "1");

                redisTemplate.convertAndSend(RedisConst.SECKILL_STOCK_STATUS_PUBSUB, seckillGoods.getSkuId() + ":1");
            }
        }
    }

    /**
     * 获取当天可秒杀的商品列表
     */
    private List<SeckillGoods> selectSeckillableGoods() {
        // 查询符合秒杀的商品
        QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
        // 状态为1
        queryWrapper.eq("status", "1");
        // 有库存
        queryWrapper.gt("stock_count", 0);
        // 当天秒杀的商品
        queryWrapper.eq("date_format(start_time, '%Y-%m-%d')", DateUtil.formatDate(new Date()));
        return seckillGoodsMapper.selectList(queryWrapper);
    }
}

package com.atguigu.gmall.activity.service;

import com.atguigu.gmall.model.activity.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService {
    /**
     * 秒杀商品缓存预热
     */
    void importSeckillGoodsToRedis();

    /**
     * 获取秒杀商品列表
     */
    List<SeckillGoods> findAll();
}

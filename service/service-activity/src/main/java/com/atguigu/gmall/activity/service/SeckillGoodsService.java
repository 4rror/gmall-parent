package com.atguigu.gmall.activity.service;

public interface SeckillGoodsService {
    /**
     * 秒杀商品缓存预热
     */
    void importSeckillGoodsToRedis();
}

package com.atguigu.gmall.list.service;

public interface SearchService {
    /**
     * 上架商品到es
     */
    void upperGoods(Long skuId);

    /**
     * 商品从es下架
     */
    void lowerGoods(Long skuId);

    /**
     * 更新商品的热度排名
     */
    void incrHotScore(Long skuId);
}

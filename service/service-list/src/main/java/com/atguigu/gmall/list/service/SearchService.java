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
}

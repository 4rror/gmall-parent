package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;

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

    /**
     * 商品搜索
     */
    SearchResponseVo search(SearchParam searchParam);
}

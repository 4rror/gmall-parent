package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {
    /**
     * 根据category3Id获取品牌列表
     */
    List<BaseTrademark> findTrademarkListByCategory3Id(Long category3Id);

    /**
     * 根据category3Id获取可选品牌列表
     */
    List<BaseTrademark> findCurrentTrademarkListByCategory3Id(Long category3Id);

    /**
     * 保存分类品牌关联
     */
    void saveBaseCategoryTrademark(CategoryTrademarkVo categoryTrademarkVo);

    /**
     * 删除分类品牌关联
     */
    void removeBaseCategoryTrademark(Long category3Id, Long trademarkId);
}

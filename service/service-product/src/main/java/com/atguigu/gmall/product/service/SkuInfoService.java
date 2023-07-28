package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface SkuInfoService {
    /**
     * 保存SkuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * sku分页列表
     */
    IPage<SkuInfo> getSkuInfoPage(Long page, Long limit);

    /**
     * 上架sku
     */
    void onSale(Long skuId);

    /**
     * cancelSale
     */
    void cancelSale(Long skuId);
}

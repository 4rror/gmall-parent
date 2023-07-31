package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;

import java.util.List;

public interface BaseAttrInfoService {
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据skuId获取平台属性数据
     */
    List<BaseAttrInfo> getAttrList(Long skuId);
}

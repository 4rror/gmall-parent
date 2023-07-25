package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrValue;

import java.util.List;

public interface BaseAttrValueService {
    /**
     * 根据平台属性Id 获取到平台属性值集合
     */
    List<BaseAttrValue> getAttrValueListById(Long attrId);
}

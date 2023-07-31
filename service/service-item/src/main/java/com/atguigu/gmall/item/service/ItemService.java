package com.atguigu.gmall.item.service;

import java.util.Map;

public interface ItemService {
    /**
     * 详情接口
     */
    Map<String, Object> getItemBySkuId(Long skuId);
}

package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.item.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Override
    public Map<String, Object> getItemBySkuId(Long skuId) {
        // 返回结果集
        Map<String, Object> map = new HashMap<>();


        map.put("categoryView", "");
        map.put("skuAttrList", "");
        map.put("skuInfo", "");
        map.put("price", "");
        map.put("spuPosterList", "");
        map.put("spuSaleAttrList", "");
        map.put("valuesSkuJson", "");
        return map;
    }
}

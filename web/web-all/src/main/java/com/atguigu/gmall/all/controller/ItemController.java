package com.atguigu.gmall.all.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.client.ItemFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class ItemController {

    @Resource
    private ItemFeignClient itemFeignClient;

    /**
     * sku详情页面
     */
    @GetMapping("/{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model) {
        // 通过skuId 查询skuInfo
        Result<Map<String, Object>> result = itemFeignClient.item(skuId);
        model.addAllAttributes(result.getData());
        return "item/item";
    }
}
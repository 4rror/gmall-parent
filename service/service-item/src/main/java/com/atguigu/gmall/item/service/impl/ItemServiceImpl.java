package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.list.client.ListFeignClient;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ListFeignClient listFeignClient;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public Map<String, Object> getItemBySkuId(Long skuId) {
        // 返回结果集
        Map<String, Object> map = new HashMap<>();

        CompletableFuture<SkuInfo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            map.put("skuInfo", skuInfo);
            return skuInfo;
        }, threadPoolExecutor);


        CompletableFuture<Void> categoryViewCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
                map.put("categoryView", categoryView);
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> spuPosterListCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                List<SpuPoster> spuPosterList = productFeignClient.findSpuPosterBySpuId(skuInfo.getSpuId());
                map.put("spuPosterList", spuPosterList);
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> spuSaleAttrListCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
                map.put("spuSaleAttrList", spuSaleAttrList);
            }
        }, threadPoolExecutor);
        CompletableFuture<Void> valuesSkuJsonCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                Map<String, Long> skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
                String jsonString = JSON.toJSONString(skuValueIdsMap);
                map.put("valuesSkuJson", jsonString);
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> skuAttrListCompletableFuture = CompletableFuture.runAsync(() -> {
            List<BaseAttrInfo> attrList = productFeignClient.getAttrList(skuId);
            List<Map<String, String>> skuAttrList = attrList.stream().map((baseAttrInfo) -> {
                Map<String, String> attrMap = new HashMap<>();
                attrMap.put("attrName", baseAttrInfo.getAttrName());
                attrMap.put("attrValue", baseAttrInfo.getAttrValueList().get(0).getValueName());
                return attrMap;
            }).collect(Collectors.toList());
            map.put("skuAttrList", skuAttrList);
        }, threadPoolExecutor);


        CompletableFuture<Void> skuPriceCompletableFuture = CompletableFuture.runAsync(() -> {
            BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
            map.put("price", skuPrice);
        }, threadPoolExecutor);

        CompletableFuture<Void> incrHotScoreCompletableFuture = CompletableFuture.runAsync(() -> listFeignClient.incrHotScore(skuId), threadPoolExecutor);

        CompletableFuture.allOf(
                skuInfoCompletableFuture,
                categoryViewCompletableFuture,
                spuPosterListCompletableFuture,
                spuSaleAttrListCompletableFuture,
                valuesSkuJsonCompletableFuture,
                skuAttrListCompletableFuture,
                skuPriceCompletableFuture,
                incrHotScoreCompletableFuture
        ).join();

        return map;
    }
}

package com.atguigu.gmall.list.client;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.client.impl.ListDegradeFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-list", fallback = ListDegradeFeignClientImpl.class)
public interface ListFeignClient {
    /**
     * /api/list/inner/incrHotScore/{skuId}
     * 更新商品的热度排名
     */
    @GetMapping("/api/list/inner/incrHotScore/{skuId}")
    void incrHotScore(@PathVariable Long skuId);
}

package com.atguigu.gmall.list.client;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.client.impl.ListDegradeFeignClientImpl;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-list", fallback = ListDegradeFeignClientImpl.class)
public interface ListFeignClient {

    /**
     * /api/list
     * 商品搜索
     */
    @PostMapping("/api/list")
    Result<SearchResponseVo> search(@RequestBody SearchParam searchParam);

    /**
     * /api/list/inner/lowerGoods/{skuId}
     * 商品从es下架
     */
    @ApiOperation("商品从es下架")
    @GetMapping("/api/list/inner/lowerGoods/{skuId}")
    Result<Object> lowerGoods(@PathVariable Long skuId);

    /**
     * /api/list/inner/upperGoods/{skuId}
     * 上架商品到es
     */
    @ApiOperation("上架商品到es")
    @GetMapping("/api/list/inner/upperGoods/{skuId}")
    Result<Object> upperGoods(@PathVariable Long skuId);

    /**
     * /api/list/inner/incrHotScore/{skuId}
     * 更新商品的热度排名
     */
    @GetMapping("/api/list/inner/incrHotScore/{skuId}")
    void incrHotScore(@PathVariable Long skuId);
}

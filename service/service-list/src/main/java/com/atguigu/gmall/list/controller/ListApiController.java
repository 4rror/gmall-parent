package com.atguigu.gmall.list.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.list.service.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/list")
public class ListApiController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private SearchService searchService;

    /**
     * /api/list/inner/incrHotScore/{skuId}
     * 更新商品的热度排名
     */
    @ApiOperation("更新商品的热度排名")
    @GetMapping("/inner/incrHotScore/{skuId}")
    public void incrHotScore(@PathVariable Long skuId) {
        searchService.incrHotScore(skuId);
    }

    /**
     * /api/list/inner/lowerGoods/{skuId}
     * 商品从es下架
     */
    @ApiOperation("商品从es下架")
    @GetMapping("/inner/lowerGoods/{skuId}")
    public Result<Object> lowerGoods(@PathVariable Long skuId) {
        searchService.lowerGoods(skuId);
        return Result.ok();
    }

    /**
     * /api/list/inner/upperGoods/{skuId}
     * 上架商品到es
     */
    @ApiOperation("上架商品到es")
    @GetMapping("/inner/upperGoods/{skuId}")
    public Result<Object> upperGoods(@PathVariable Long skuId) {
        searchService.upperGoods(skuId);
        return Result.ok();
    }

    /**
     * /api/list/createIndex
     * 创建索引
     */
    @ApiOperation("创建索引")
    @GetMapping("/createIndex")
    public Result<Object> createIndex() {
        // 创建索引库
        elasticsearchRestTemplate.createIndex(Goods.class);
        // 创建映射关系
        elasticsearchRestTemplate.putMapping(Goods.class);
        return Result.ok();
    }
}

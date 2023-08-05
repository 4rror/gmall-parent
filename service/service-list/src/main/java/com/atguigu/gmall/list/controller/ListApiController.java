package com.atguigu.gmall.list.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/list")
public class ListApiController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * /api/list/createIndex
     * 创建索引
     */
    @GetMapping("/createIndex")
    public Result<Object> createIndex() {
        // 创建索引库
        elasticsearchRestTemplate.createIndex(Goods.class);
        // 创建映射关系
        elasticsearchRestTemplate.putMapping(Goods.class);
        return Result.ok();
    }
}

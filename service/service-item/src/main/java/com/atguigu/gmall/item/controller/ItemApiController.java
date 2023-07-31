package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.service.ItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/item")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    /**
     * 详情接口
     */
    @ApiOperation("详情接口")
    @GetMapping("/{skuId}")
    public Result<Map<String, Object>> item(@PathVariable Long skuId) {
        Map<String, Object> map = itemService.getItemBySkuId(skuId);

        return Result.ok(map);
    }
}

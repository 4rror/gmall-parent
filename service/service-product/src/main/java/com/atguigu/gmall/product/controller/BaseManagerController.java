package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseManagerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseManagerController {

    @Autowired
    private BaseManagerService baseManagerService;

    /**
     * 根据分类Id获取平台属性集合
     */
    @ApiOperation("根据分类Id获取平台属性集合")
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable Long category1Id,
                                                   @PathVariable Long category2Id,
                                                   @PathVariable Long category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = baseManagerService.attrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(baseAttrInfoList);
    }

    /**
     * 获取三级分类
     * /admin/product/getCategory1
     */
    @ApiOperation("获取三级分类列表")
    @GetMapping("/getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable Long category2Id) {
        List<BaseCategory3> baseCategory3List = baseManagerService.getCategory3List(category2Id);
        return Result.ok(baseCategory3List);
    }

    /**
     * 获取二级分类
     * /admin/product/getCategory1
     */
    @ApiOperation("获取二级分类列表")
    @GetMapping("/getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable Long category1Id) {
        List<BaseCategory2> baseCategory2List = baseManagerService.getCategory2List(category1Id);
        return Result.ok(baseCategory2List);
    }

    /**
     * 获取一级分类
     * /admin/product/getCategory1
     */
    @ApiOperation("获取一级分类列表")
    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getCategory1() {
        List<BaseCategory1> baseCategory1List = baseManagerService.getCategory1List();
        return Result.ok(baseCategory1List);
    }
}

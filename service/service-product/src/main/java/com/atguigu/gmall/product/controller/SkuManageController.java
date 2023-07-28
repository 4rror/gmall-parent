package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.atguigu.gmall.product.service.SkuInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SkuManageController {

    @Autowired
    private BaseManagerService baseManagerService;

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 保存SkuInfo
     */
    @ApiOperation("保存SkuInfo")
    @PostMapping("/saveSkuInfo")
    public Result<Object> saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    /**
     * 根据spuId获取spuImage集合
     */
    @ApiOperation("根据spuId获取spuImage集合")
    @GetMapping("/spuImageList/{spuId}")
    public Result<List<SpuImage>> getSpuImageListBySpuId(@PathVariable Long spuId) {
        List<SpuImage> spuImageList = baseManagerService.getSpuImageListBySpuId(spuId);
        return Result.ok(spuImageList);
    }

    /**
     * 根据spuId查询销售属性
     */
    @ApiOperation("根据spuId查询销售属性")
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListBySpuId(@PathVariable Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = baseManagerService.getSpuSaleAttrListBySpuId(spuId);
        return Result.ok(spuSaleAttrList);
    }
}

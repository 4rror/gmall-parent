package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SpuManageController {


    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private BaseManagerService baseManagerService;

    /**
     * 保存spu
     */
    @ApiOperation("保存spu")
    @PostMapping("/saveSpuInfo")
    public Result<Object> saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }
    // baseSaleAttrList

    /**
     * 获取销售属性数据
     */
    @ApiOperation("获取销售属性数据")
    @GetMapping("/baseSaleAttrList")
    public Result<List<BaseSaleAttr>> getBaseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrList = baseManagerService.getBaseSaleAttrList();
        return Result.ok(baseSaleAttrList);
    }

    /**
     * spu分页列表
     *
     * @param page  当前页
     * @param limit 页大小
     */
    @ApiOperation("spu分页列表")
    @GetMapping("/{page}/{limit}")
    public Result<IPage<SpuInfo>> getSpuInfoPageByCategory3Id(@PathVariable Integer page, @PathVariable Integer limit, Long category3Id) {
        IPage<SpuInfo> infoIPage = spuInfoService.getSpuInfoPageByCategory3Id(page, limit, category3Id);
        return Result.ok(infoIPage);
    }
}

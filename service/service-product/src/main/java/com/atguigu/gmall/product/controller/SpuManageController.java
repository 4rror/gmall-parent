package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class SpuManageController {


    @Autowired
    private SpuInfoService spuInfoService;

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

package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {

    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;

    /**
     * 删除分类品牌关联
     * /admin/product/baseCategoryTrademark/remove/{category3Id}/{trademarkId}
     */
    @DeleteMapping("/remove/{category3Id}/{trademarkId}")
    public Result<Object> removeBaseCategoryTrademark(@PathVariable Long category3Id, @PathVariable Long trademarkId) {
        baseCategoryTrademarkService.removeBaseCategoryTrademark(category3Id, trademarkId);
        return Result.ok();
    }

    /**
     * 保存分类品牌关联
     */
    @ApiOperation("保存分类品牌关联")
    @PostMapping("/save")
    public Result<Object> saveBaseCategoryTrademark(@RequestBody CategoryTrademarkVo categoryTrademarkVo) {
        baseCategoryTrademarkService.saveBaseCategoryTrademark(categoryTrademarkVo);
        return Result.ok();
    }

    /**
     * 根据category3Id获取可选品牌列表
     */
    @ApiOperation("根据category3Id获取可选品牌列表")
    @GetMapping("/findCurrentTrademarkList/{category3Id}")
    public Result<List<BaseTrademark>> findCurrentTrademarkList(@PathVariable Long category3Id) {
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.findCurrentTrademarkListByCategory3Id(category3Id);
        return Result.ok(baseTrademarkList);
    }

    /**
     * 根据category3Id获取品牌列表
     */
    @ApiOperation("根据category3Id获取品牌列表")
    @GetMapping("/findTrademarkList/{category3Id}")
    public Result<List<BaseTrademark>> findTrademarkListByCategory3Id(@PathVariable Long category3Id) {
        List<BaseTrademark> baseCategoryTrademarkList = baseCategoryTrademarkService.findTrademarkListByCategory3Id(category3Id);
        return Result.ok(baseCategoryTrademarkList);
    }
}

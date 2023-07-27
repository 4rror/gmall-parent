package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    /**
     * 删除BaseTrademark
     */
    @ApiOperation("删除BaseTrademark")
    @DeleteMapping("/remove/{id}")
    public Result<Object> deleteBaseTrademark(@PathVariable Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    /**
     * 更新BaseTrademark
     */
    @ApiOperation("更新BaseTrademark")
    @PutMapping("/update")
    public Result<Object> updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 保存BaseTrademark
     */
    @ApiOperation("保存BaseTrademark")
    @PostMapping("/save")
    public Result<Object> saveBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 根据品牌Id 获取品牌对象
     */
    @ApiOperation("根据品牌Id获取品牌对象")
    @GetMapping("/get/{id}")
    public Result<BaseTrademark> getTrademarkById(@PathVariable Long id) {
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    /**
     * 品牌分页列表
     */
    @ApiOperation("品牌分页列表")
    @GetMapping("/{page}/{limit}")
    public Result<IPage<BaseTrademark>> getBaseTrademarkPage(@PathVariable Integer page, @PathVariable Integer limit) {
        IPage<BaseTrademark> baseTrademarkPage = baseTrademarkService.getBaseTrademarkPage(page, limit);
        return Result.ok(baseTrademarkPage);
    }
}

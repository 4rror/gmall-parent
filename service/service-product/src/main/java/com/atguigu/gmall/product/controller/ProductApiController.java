package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.atguigu.gmall.product.service.SkuInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/inner")
public class ProductApiController {

    @Autowired
    private BaseManagerService baseManagerService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

    /**
     * /api/product/inner/getAttrList/{skuId}
     * 根据skuId获取平台属性数据
     */
    @ApiOperation("根据skuId获取平台属性数据")
    @GetMapping("/getAttrList/{skuId}")
    public List<BaseAttrInfo> getAttrList(@PathVariable Long skuId) {
        return baseAttrInfoService.getAttrList(skuId);
    }

    /**
     * /api/product/inner/findSpuPosterBySpuId/{spuId}
     * 根据spuId获取海报数据
     */
    @ApiOperation("根据spuId获取海报数据")
    @GetMapping("/findSpuPosterBySpuId/{spuId}")

    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId) {
        return baseManagerService.findSpuPosterBySpuId(spuId);
    }

    /**
     * /api/product/inner/getSkuValueIdsMap/{spuId}
     * 根据spuId获取到销售属性值Id与skuId组成的数据集
     */
    @ApiOperation("根据spuId获取到销售属性值Id与skuId组成的数据集")
    @GetMapping("/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable Long spuId) {
        return baseManagerService.getSkuValueIdsMap(spuId);
    }

    /**
     * /api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}
     * 根据spuId, skuId获取销售属性集合
     */
    @ApiOperation("根据spuId, skuId获取销售属性集合")
    @GetMapping("/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")

    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId, @PathVariable Long spuId) {
        return baseManagerService.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    /**
     * /api/product/inner/getSkuPrice/{skuId}
     * 根据skuId获取最新的商品价格
     */
    @ApiOperation("根据skuId获取最新的商品价格")
    @GetMapping("/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable Long skuId) {
        return baseManagerService.getSkuPrice(skuId);
    }

    /**
     * /api/product/inner/getCategoryView/{category3Id}
     * 根据三级分类id获取分类信息
     */
    @ApiOperation("根据三级分类id获取分类信息")
    @GetMapping("/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable Long category3Id) {
        return baseManagerService.getCategoryView(category3Id);
    }

    /**
     * /api/product/inner/getSkuInfo/{skuId}
     * 根据skuId获取SkuInfo
     */
    @ApiOperation("根据skuId获取SkuInfo")
    @GetMapping("/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId) {
        return skuInfoService.getSkuInfoById(skuId);
    }
}

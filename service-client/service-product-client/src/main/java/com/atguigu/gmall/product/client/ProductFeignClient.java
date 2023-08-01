package com.atguigu.gmall.product.client;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.impl.ProductDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "service-product", fallback = ProductDegradeFeignClient.class)
public interface ProductFeignClient {
    /**
     * /api/product/inner/getAttrList/{skuId}
     * 根据skuId获取平台属性数据
     */
    @GetMapping("/api/product/inner/getAttrList/{skuId}")
    List<BaseAttrInfo> getAttrList(@PathVariable Long skuId);

    /**
     * /api/product/inner/findSpuPosterBySpuId/{spuId}
     * 根据spuId获取海报数据
     */
    @GetMapping("/api/product/inner/findSpuPosterBySpuId/{spuId}")
    List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId);

    /**
     * /api/product/inner/getSkuValueIdsMap/{spuId}
     * 根据spuId获取到销售属性值Id与skuId组成的数据集
     */
    @GetMapping("/api/product/inner/getSkuValueIdsMap/{spuId}")
    Map<String, Long> getSkuValueIdsMap(@PathVariable Long spuId);

    /**
     * /api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}
     * 根据spuId, skuId获取销售属性集合
     */
    @GetMapping("/api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId, @PathVariable Long spuId);

    /**
     * /api/product/inner/getSkuPrice/{skuId}
     * 根据skuId获取最新的商品价格
     */
    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    BigDecimal getSkuPrice(@PathVariable Long skuId);

    /**
     * /api/product/inner/getCategoryView/{category3Id}
     * 根据三级分类id获取分类信息
     */
    @GetMapping("/api/product/inner/getCategoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable Long category3Id);

    /**
     * /api/product/inner/getSkuInfo/{skuId}
     * 根据skuId获取SkuInfo
     */
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    SkuInfo getSkuInfo(@PathVariable Long skuId);
}

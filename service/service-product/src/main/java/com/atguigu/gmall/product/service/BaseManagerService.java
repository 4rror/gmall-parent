package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BaseManagerService {

    /**
     * 获取一级分类列表
     */
    List<BaseCategory1> getCategory1List();

    /**
     * 通过一级分类id获取二级分类列表
     *
     * @param category1Id 一级分类的id
     */
    List<BaseCategory2> getCategory2List(Long category1Id);

    /**
     * 获取三级分类列表
     */
    List<BaseCategory3> getCategory3List(Long category2Id);

    /**
     * 根据分类Id 获取平台属性集合
     */
    List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id);

    /**
     * 获取销售属性数据
     */
    List<BaseSaleAttr> getBaseSaleAttrList();

    /**
     * 根据spuId查询销售属性
     */
    List<SpuSaleAttr> getSpuSaleAttrListBySpuId(Long spuId);

    /**
     * 根据spuId获取spuImage集合
     */
    List<SpuImage> getSpuImageListBySpuId(Long spuId);

    /**
     * 根据三级分类id获取分类信息
     */
    BaseCategoryView getCategoryView(Long category3Id);

    /**
     * 根据skuId获取最新的商品价格
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 根据spuId, skuId获取销售属性集合
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);

    /**
     * 根据spuId获取到销售属性值Id与skuId组成的数据集
     */
    Map<String, Long> getSkuValueIdsMap(Long spuId);

    /**
     * 根据spuId获取海报数据
     */
    List<SpuPoster> findSpuPosterBySpuId(Long spuId);
}

package com.atguigu.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.cache.GmallCache;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BaseManagerServiceImpl implements BaseManagerService {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    // @Autowired
    // private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuPosterMapper spuPosterMapper;


    @Override
    public List<BaseCategory1> getCategory1List() {
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2List(Long category1Id) {
        LambdaQueryWrapper<BaseCategory2> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory2::getCategory1Id, category1Id);
        return baseCategory2Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseCategory3> getCategory3List(Long category2Id) {
        LambdaQueryWrapper<BaseCategory3> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory3::getCategory2Id, category2Id);
        return baseCategory3Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        return baseAttrInfoMapper.selectAttrInfoList(category1Id, category2Id, category3Id);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListBySpuId(Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    public List<SpuImage> getSpuImageListBySpuId(Long spuId) {
        return spuImageMapper.selectList(new LambdaQueryWrapper<SpuImage>().eq(SpuImage::getSpuId, spuId));
    }

    @Override
    @GmallCache(prefix = "categoryView:", suffix = ":info")
    public BaseCategoryView getCategoryView(Long category3Id) {
        return baseCategoryViewMapper.selectCategoryView(category3Id);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo != null) return skuInfo.getPrice();
        return new BigDecimal("0");
    }

    @Override
    @GmallCache(prefix = "spuSaleAttrListCheckBySku:", suffix = ":info")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    @Override
    @GmallCache(prefix = "skuValueIdsMap:", suffix = ":info")
    public Map<String, Long> getSkuValueIdsMap(Long spuId) {
        List<Map<String, Long>> mapList = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);

        HashMap<String, Long> map = new HashMap<>();
        mapList.forEach(i -> map.put(String.valueOf(i.get("value_ids")), i.get("sku_id")));
        return map;
    }

    @Override
    @GmallCache(prefix = "spuPosterBySpuId:", suffix = ":info")
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        return spuPosterMapper.selectList(new LambdaQueryWrapper<SpuPoster>().eq(SpuPoster::getSpuId, spuId));
    }

    @Override
    @GmallCache(prefix = "categoryViewList:", suffix = ":info")
    public List<JSONObject> getBaseCategoryList() {
        List<BaseCategoryView> baseCategoryViewList = baseCategoryViewMapper.selectList(null);

        // 分组遍历
        Map<Long, List<BaseCategoryView>> baseCategory1List = baseCategoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));

        List<JSONObject> resultList = new ArrayList<>();

        final int[] index = {1};
        baseCategory1List.forEach((k1, v1) -> {
            // 封装一级分类
            JSONObject category1 = new JSONObject();
            category1.put("index", index[0]);
            category1.put("categoryId", k1);
            category1.put("categoryName", v1.get(0).getCategory1Name());
            // 变量迭代
            index[0]++;

            // 二级分类分组
            Map<Long, List<BaseCategoryView>> baseCategory2List = v1.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            List<JSONObject> category2Child = new ArrayList<>();
            baseCategory2List.forEach((k2, v2) -> {
                // 封装二级分类
                JSONObject category2 = new JSONObject();
                category2.put("categoryId", k2);
                category2.put("categoryName", v2.get(0).getCategory2Name());

                List<JSONObject> category3Child = new ArrayList<>();
                v2.forEach(category3View -> {
                    JSONObject category3 = new JSONObject();
                    category3.put("categoryId", category3View.getCategory3Id());
                    category3.put("categoryName", category3View.getCategory3Name());

                    category3Child.add(category3);
                });

                category2.put("categoryChild", category3Child);

                // 将二级分类的项添加到集合中
                category2Child.add(category2);
            });

            // 将一级分类的子分类添加到返回数据中
            category1.put("categoryChild", category2Child);

            // 将一级分类添加到返回结果中
            resultList.add(category1);
        });
        return resultList;
    }
}

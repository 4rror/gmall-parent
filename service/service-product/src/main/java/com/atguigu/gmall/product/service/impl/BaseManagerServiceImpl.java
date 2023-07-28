package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
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
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;


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
        // 查询spuSaleAttrList
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper
                .selectList(new LambdaQueryWrapper<SpuSaleAttr>().eq(SpuSaleAttr::getSpuId, spuId));
        if (CollectionUtils.isEmpty(spuSaleAttrList)) return null;

        // 查询spuSaleAttrValueList
        List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttrValueMapper
                .selectList(new LambdaQueryWrapper<SpuSaleAttrValue>().eq(SpuSaleAttrValue::getSpuId, spuId));
        if (CollectionUtils.isEmpty(spuSaleAttrValueList)) return spuSaleAttrList;

        // 设置销售属性值
        spuSaleAttrList.forEach(spuSaleAttr -> {
            List<SpuSaleAttrValue> collect =
                    spuSaleAttrValueList
                            .stream()
                            .filter(spuSaleAttrValue ->
                                    Objects.equals(spuSaleAttr.getSaleAttrName(), spuSaleAttrValue.getSaleAttrName()))
                            .collect(Collectors.toList());
            spuSaleAttr.setSpuSaleAttrValueList(collect);
        });

        return spuSaleAttrList;
    }

    @Override
    public List<SpuImage> getSpuImageListBySpuId(Long spuId) {
        return spuImageMapper.selectList(new LambdaQueryWrapper<SpuImage>().eq(SpuImage::getSpuId, spuId));
    }
}

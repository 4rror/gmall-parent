package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.list.repository.GoodsRepository;
import com.atguigu.gmall.list.service.SearchService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    private ProductFeignClient productFeignClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void upperGoods(Long skuId) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo == null) return;

        Goods goods = new Goods();

        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());

        BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
        if (skuPrice != null)
            goods.setPrice(skuPrice.doubleValue());

        goods.setCreateTime(new Date());
        goods.setTmId(skuInfo.getTmId());

        // 获取品牌信息
        BaseTrademark baseTrademark = productFeignClient.getTrademarkById(skuInfo.getTmId()).getData();
        if (baseTrademark != null) {
            goods.setTmName(baseTrademark.getTmName());
            goods.setTmLogoUrl(baseTrademark.getLogoUrl());
        }

        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        if (categoryView != null) {
            goods.setCategory1Id(categoryView.getCategory1Id());
            goods.setCategory1Name(categoryView.getCategory1Name());
            goods.setCategory2Id(categoryView.getCategory2Id());
            goods.setCategory2Name(categoryView.getCategory2Name());
            goods.setCategory3Id(categoryView.getCategory3Id());
            goods.setCategory3Name(categoryView.getCategory3Name());
        }

        List<BaseAttrInfo> attrList = productFeignClient.getAttrList(skuId);

        if (attrList != null) {
            List<SearchAttr> attrs = attrList.stream().map(baseAttrInfo -> {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(baseAttrInfo.getId());
                searchAttr.setAttrName(baseAttrInfo.getAttrName());
                searchAttr.setAttrValue(baseAttrInfo.getAttrValueList().get(0).getValueName());
                return searchAttr;
            }).collect(Collectors.toList());
            goods.setAttrs(attrs);
        }

        // 保存
        goodsRepository.save(goods);
    }

    @Override
    public void lowerGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public void incrHotScore(Long skuId) {

        // 定义key
        String hotKey = "hotScore";
        String hotValue = "skuId:" + skuId;
        Double score = stringRedisTemplate.opsForZSet().incrementScore(hotKey, hotValue, 1L);

        if (score % 10 == 0) {
            // 从es中查询商品
            Optional<Goods> goodsOptional = goodsRepository.findById(skuId);
            if (goodsOptional.isPresent()) {
                Goods goods = goodsOptional.get();
                Long esHotScore = goods.getHotScore();
                // 确认redis中的分数和es中的分数一致性
                if (score < esHotScore) {
                    stringRedisTemplate.opsForZSet().add(hotKey, hotValue, esHotScore + Math.round(score % 10));
                } else {
                    goods.setHotScore(Math.round(score));
                    goodsRepository.save(goods);
                }
            }
        }

    }
}

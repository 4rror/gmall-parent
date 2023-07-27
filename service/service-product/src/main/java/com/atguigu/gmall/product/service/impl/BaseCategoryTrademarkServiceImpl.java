package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark> implements BaseCategoryTrademarkService {

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public List<BaseTrademark> findTrademarkListByCategory3Id(Long category3Id) {
        LambdaQueryWrapper<BaseCategoryTrademark> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseMapper.selectList(queryWrapper);

        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            List<Long> ids = baseCategoryTrademarkList.stream().map(BaseCategoryTrademark::getTrademarkId).collect(Collectors.toList());
            return baseTrademarkMapper.selectBatchIds(ids);
        }
        return null;
    }

    @Override
    public List<BaseTrademark> findCurrentTrademarkListByCategory3Id(Long category3Id) {
        LambdaQueryWrapper<BaseCategoryTrademark> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseMapper.selectList(queryWrapper);

        List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectList(null);

        // 排除已存在的品牌
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            List<Long> existsIds = baseCategoryTrademarkList.stream().map(BaseCategoryTrademark::getTrademarkId).collect(Collectors.toList());
            return baseTrademarkList.stream().filter(i -> !existsIds.contains(i.getId())).collect(Collectors.toList());
        }
        // 如果没有已存在的id，就查询全部
        return baseTrademarkList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBaseCategoryTrademark(CategoryTrademarkVo categoryTrademarkVo) {
        Long category3Id = categoryTrademarkVo.getCategory3Id();
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
        List<BaseCategoryTrademark> baseCategoryTrademarkList = new ArrayList<>();
        trademarkIdList.forEach(trademarkId -> {
            BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
            baseCategoryTrademark.setCategory3Id(category3Id);
            baseCategoryTrademark.setTrademarkId(trademarkId);
            baseCategoryTrademarkList.add(baseCategoryTrademark);
        });
        this.saveBatch(baseCategoryTrademarkList);
    }

    @Override
    public void removeBaseCategoryTrademark(Long category3Id, Long trademarkId) {
        LambdaQueryWrapper<BaseCategoryTrademark> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(BaseCategoryTrademark::getCategory3Id, category3Id)
                .eq(BaseCategoryTrademark::getTrademarkId, trademarkId);
        this.remove(queryWrapper);
    }
}

package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SkuAttrValueMapper;
import com.atguigu.gmall.product.mapper.SkuImageMapper;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.mapper.SkuSaleAttrValueMapper;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SkuInfoServiceImpl implements SkuInfoService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        // 保存skuInfo
        skuInfoMapper.insert(skuInfo);

        // 保存skuAttrValueList
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            skuAttrValueList.forEach(skuAttrValue -> {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            });
        }

        // 保存skuSaleAttrValueList
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)) {
            skuSaleAttrValueList.forEach(skuSaleAttrValue -> {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            });
        }

        // 保存skuImageList
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (!CollectionUtils.isEmpty(skuImageList)) {
            skuImageList.forEach(skuImage -> {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            });
        }
    }

    @Override
    public IPage<SkuInfo> getSkuInfoPage(Long page, Long limit) {
        IPage<SkuInfo> skuInfoPage = skuInfoMapper.selectPage(new Page<>(page, limit), new LambdaQueryWrapper<SkuInfo>().orderByDesc(SkuInfo::getId));

        // 封装结果
        // List<SkuInfo> records = skuInfoPage.getRecords();
        //
        // records.forEach(skuInfo -> {
        //     // 封装skuAttrValueList
        //     List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.selectList(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, skuInfo.getId()));
        //     skuInfo.setSkuAttrValueList(skuAttrValueList);
        //
        //     // 封装skuSaleAttrValueList
        //     List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.selectList(new LambdaQueryWrapper<SkuSaleAttrValue>().eq(SkuSaleAttrValue::getSkuId, skuInfo.getId()));
        //     skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);
        //
        //     // 封装skuImageList
        //     List<SkuImage> skuImageList = skuImageMapper.selectList(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, skuInfo.getId()));
        //     skuInfo.setSkuImageList(skuImageList);
        // });

        return skuInfoPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);
        skuInfoMapper.updateById(skuInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public SkuInfo getSkuInfoById(Long skuId) {
        return getSkuInfoRedis(skuId);
    }

    @Nullable
    private SkuInfo getSkuInfoRedis(Long skuId) {
        try {
            String key = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKUKEY_SUFFIX;

            // 获取缓存中
            SkuInfo skuInfo = (SkuInfo) redisTemplate.opsForValue().get(key);
            if (skuInfo == null) {
                // 没有，获取分布式锁
                String lockKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKULOCK_SUFFIX;
                String lockValue = UUID.randomUUID().toString();
                Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);

                if (!flag) {
                    Thread.sleep(50L);
                    return getSkuInfoById(skuId);
                }

                // 获取到锁
                try {
                    // 查询数据库
                    skuInfo = getSkuInfoDB(skuId);
                    // 数据库没有数据时，设置，空键
                    if (skuInfo == null) {
                        redisTemplate.opsForValue().set(key, new SkuInfo(), RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                    } else {
                        redisTemplate.opsForValue().set(key, skuInfo, RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                    }
                    System.out.println(123);
                } finally {
                    // 释放锁
                    String lua = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
                    defaultRedisScript.setScriptText(lua);
                    defaultRedisScript.setResultType(Long.class);
                    redisTemplate.execute(defaultRedisScript, Collections.singletonList(lockKey), lockValue);
                }
            } else {
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoDB(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo == null) return null;

        List<SkuImage> skuImageList = skuImageMapper.selectList(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, skuId));
        skuInfo.setSkuImageList(skuImageList);

        return skuInfo;
    }
}

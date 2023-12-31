package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.cache.GmallCache;
import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.service.RabbitService;
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
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

@Slf4j
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

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitService rabbitService;

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
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_GOODS, MqConst.ROUTING_GOODS_UPPER, skuId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);
        skuInfoMapper.updateById(skuInfo);
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_GOODS, MqConst.ROUTING_GOODS_LOWER, skuId);
    }

    @Override
    @GmallCache(prefix = RedisConst.SKUKEY_PREFIX, suffix = RedisConst.SKUKEY_SUFFIX)
    public SkuInfo getSkuInfoById(Long skuId) {
        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoRedisson(Long skuId) {
        try {
            String key = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKUKEY_SUFFIX;
            SkuInfo skuInfo = (SkuInfo) redisTemplate.opsForValue().get(key);
            if (skuInfo == null) {
                String lockKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKULOCK_SUFFIX;
                RLock lock = redissonClient.getLock(lockKey);
                // 尝试获取锁
                // boolean flag = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                // 自旋
                while (!lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS)) {
                    // 自旋
                    Thread.sleep(50L);
                }

                // 再次查询是否命中缓存
                skuInfo = (SkuInfo) redisTemplate.opsForValue().get(key);
                if (skuInfo != null) {
                    return skuInfo;
                }

                // 获取到锁
                try {
                    skuInfo = getSkuInfoDB(skuId);
                    if (skuInfo == null) {
                        skuInfo = new SkuInfo();
                        redisTemplate.opsForValue().set(key, skuInfo, RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                    } else {
                        redisTemplate.opsForValue().set(key, skuInfo, RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                    }
                    return skuInfo;
                } finally {
                    // 释放锁
                    lock.unlock();
                }

            } else {
                return skuInfo;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return getSkuInfoDB(skuId);
    }

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

                if (flag) {
                    // 获取到锁
                    try {
                        // 查询数据库
                        skuInfo = getSkuInfoDB(skuId);
                        // 数据库没有数据时，设置，空键
                        if (skuInfo == null) {
                            redisTemplate.opsForValue().set(key, new SkuInfo(), RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                        } else {
                            redisTemplate.opsForValue().set(key, skuInfo, RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                            return skuInfo;
                        }
                    } finally {
                        // 释放锁
                        String lua = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
                        defaultRedisScript.setScriptText(lua);
                        defaultRedisScript.setResultType(Long.class);
                        redisTemplate.execute(defaultRedisScript, Collections.singletonList(lockKey), lockValue);
                    }
                } else {
                    Thread.sleep(50L);
                    return getSkuInfoRedis(skuId);
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

package com.atguigu.gmall.common.cache;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.constant.RedisConst;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@SuppressWarnings("all")
public class GmallCacheAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.atguigu.gmall.common.cache.GmallCache)")
    public Object cacheAroundAdvice(ProceedingJoinPoint pjp) {

        Object o;

        // 获取方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取方法上的注解
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        // 前缀
        String prefix = annotation.prefix();
        // 后缀
        String suffix = annotation.suffix();
        // 获取方法形参
        Object[] args = pjp.getArgs();

        // 拼接缓存key
        String cacheKey = prefix + Arrays.toString(args) + suffix;

        // 查询缓存
        o = cacheHit(cacheKey, signature);

        try {
            // 缓存未命中，从数据库查询
            if (o == null) {
                // 拼接锁key
                String lockKey = prefix + Arrays.toString(args) + RedisConst.SKULOCK_SUFFIX;
                RLock lock = redissonClient.getLock(lockKey);
                // 尝试获取锁
                while (!lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS)) {
                    // 未获取到锁，等待50微秒，自旋
                    Thread.sleep(50L);
                }
                // 成功获取锁
                try {
                    // 双重验证
                    o = cacheHit(cacheKey, signature);
                    if (o != null) {
                        return o;
                    }

                    // 执行方法查询数据库，
                    o = pjp.proceed(pjp.getArgs());
                    if (o == null) {
                        // 没有数据，写入空key
                        o = new Object();
                        stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(o), RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                    } else {
                        // 查询到数据，将对象转换为json字符串存入redis
                        stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(o), RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                    }
                    return o;
                } finally {
                    lock.unlock();
                }
            } else {
                return o;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 兜底，执行方法
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    private Object cacheHit(String cacheKey, MethodSignature signature) {
        String cacheValue = stringRedisTemplate.opsForValue().get(cacheKey);
        // 缓存命中
        if (!StringUtils.isEmpty(cacheValue)) {
            // 获取返回值类型
            Class<?> returnType = signature.getReturnType();
            return JSON.parseObject(cacheValue, returnType);
        }
        return null;
    }

}

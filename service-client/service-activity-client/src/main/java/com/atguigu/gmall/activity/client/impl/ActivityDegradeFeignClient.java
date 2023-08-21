package com.atguigu.gmall.activity.client.impl;

import com.atguigu.gmall.activity.client.ActivityFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.activity.SeckillGoods;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityDegradeFeignClient implements ActivityFeignClient {
    @Override
    public Result<List<SeckillGoods>> findAll() {
        return Result.fail();
    }
}

package com.atguigu.gmall.list.client.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.client.ListFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.stereotype.Component;

@Component
public class ListDegradeFeignClientImpl implements ListFeignClient {
    @Override
    public Result<SearchResponseVo> search(SearchParam searchParam) {
        return Result.fail();
    }

    @Override
    public Result<Object> lowerGoods(Long skuId) {
        return Result.fail();
    }

    @Override
    public Result<Object> upperGoods(Long skuId) {
        return Result.fail();
    }

    @Override
    public void incrHotScore(Long skuId) {
    }
}

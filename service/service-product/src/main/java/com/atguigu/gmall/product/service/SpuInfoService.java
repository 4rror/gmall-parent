package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.PathVariable;

public interface SpuInfoService {
    /**
     * spu分页列表
     */
    IPage<SpuInfo> getSpuInfoPageByCategory3Id(Integer page, Integer limit, Long category3Id);

    /**
     * 保存spu
     */
    void saveSpuInfo(SpuInfo spuInfo);
}

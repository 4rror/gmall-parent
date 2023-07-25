package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() != null) {
            baseAttrInfoMapper.updateById(baseAttrInfo);
        } else {
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
    }
}

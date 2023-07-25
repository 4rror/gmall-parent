package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;

import java.util.List;

public interface BaseManagerService {

    /**
     * 获取一级分类列表
     */
    List<BaseCategory1> getCategory1List();

    /**
     * 通过一级分类id获取二级分类列表
     *
     * @param category1Id 一级分类的id
     */
    List<BaseCategory2> getCategory2List(Long category1Id);

    /**
     * 获取三级分类列表
     */
    List<BaseCategory3> getCategory3List(Long category2Id);

    /**
     * 根据分类Id 获取平台属性集合
     */
    List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id);
}

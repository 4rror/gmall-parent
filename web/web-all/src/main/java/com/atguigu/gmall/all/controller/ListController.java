package com.atguigu.gmall.all.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.client.ListFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Controller
public class ListController {
    @Resource
    private ListFeignClient listFeignClient;

    @GetMapping("/list.html")
    public String list(SearchParam searchParam, Model model) {
        Result<SearchResponseVo> searchResponseVoResult = listFeignClient.search(searchParam);
        BeanMap beanMap = BeanMap.create(searchResponseVoResult.getData());

        // 拼接urlParam参数
        String urlParam = this.buildUrlParam(searchParam);
        model.addAttribute("urlParam", urlParam);

        // 拼接trademarkParam参数
        String trademarkParam = this.buildTrademarkParam(searchParam);
        model.addAttribute("trademarkParam", trademarkParam);

        // 拼接平台属性
        List<Map<String, Object>> propParamList = this.buildPropParamList(searchParam);
        model.addAttribute("propsParamList", propParamList);

        // 拼接排序
        Map<String, Object> orderMap = this.buildOrderMap(searchParam);
        model.addAttribute("orderMap", orderMap);

        model.addAllAttributes(beanMap);
        return "list/index";
    }

    private Map<String, Object> buildOrderMap(SearchParam searchParam) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(searchParam)) {
            String order = searchParam.getOrder();
            if (!StringUtils.isEmpty(order)) {
                String[] split = order.split(":");
                if (split.length == 2) {
                    map.put("type", split[0]);
                    map.put("sort", split[1]);
                }
            } else {
                // 首次默认根据热度降序排列
                map.put("type", 1);
                map.put("sort", "desc");
            }
        }
        return map;
    }

    private List<Map<String, Object>> buildPropParamList(SearchParam searchParam) {
        String[] props = searchParam.getProps();
        List<Map<String, Object>> list = new ArrayList<>();
        if (props != null && props.length > 0) {
            Arrays.stream(props).forEach(prop -> {
                String[] split = prop.split(":");
                if (split.length == 3) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("attrId", split[0]);
                    map.put("attrValue", split[1]);
                    map.put("attrName", split[2]);
                    list.add(map);
                }
            });
        }
        return list;
    }

    private String buildTrademarkParam(SearchParam searchParam) {
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (split.length == 2) {
                return "品牌: " + split[1];
            }
        }
        return "";
    }

    private String buildUrlParam(SearchParam searchParam) {
        StringBuilder sb = new StringBuilder();
        // 拼接关键字或分类id
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            sb.append("keyword=").append(searchParam.getKeyword());
        } else if (searchParam.getCategory1Id() != null && searchParam.getCategory1Id() != 0) {
            sb.append("category1Id=").append(searchParam.getCategory1Id());
        } else if (searchParam.getCategory2Id() != null && searchParam.getCategory2Id() != 0) {
            sb.append("category2Id=").append(searchParam.getCategory2Id());
        } else if (searchParam.getCategory3Id() != null && searchParam.getCategory3Id() != 0) {
            sb.append("category3Id=").append(searchParam.getCategory3Id());
        }

        // 二级参数
        if (!StringUtils.isEmpty(searchParam.getTrademark())) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append("trademark=").append(searchParam.getTrademark());
        }

        // 平台属性
        if (searchParam.getProps() != null && searchParam.getProps().length > 0) {
            Arrays.stream(searchParam.getProps()).forEach(prop -> {
                if (sb.length() > 0) {
                    sb.append("&props=");
                }
                sb.append(prop);
            });
        }

        return "list.html?" + sb;
    }
}

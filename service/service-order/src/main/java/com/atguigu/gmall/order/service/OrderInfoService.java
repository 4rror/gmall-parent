package com.atguigu.gmall.order.service;

import java.util.Map;

public interface OrderInfoService {
    /**
     * 去结算
     */
    Map<String, Object> trade(String userId);
}

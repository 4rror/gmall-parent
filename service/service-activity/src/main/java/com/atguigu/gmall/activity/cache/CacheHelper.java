package com.atguigu.gmall.activity.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheHelper {
    private static final Map<String, String> map = new ConcurrentHashMap<>();

    public static void put(String key, String value) {
        map.put(key, value);
    }

    public static String get(String key) {
        return map.get(key);
    }

    public static void remove(String key) {
        map.remove(key);
    }

    public static void clear() {
        map.clear();
    }
}

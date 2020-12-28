package com.ymx.driver.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewOrderFilterUtiles {
    public static final int DEFAULT_CACHE_LIMIT = 20;
    private static final Map<String, String> newOrderCache = new LinkedHashMap<String, String>(DEFAULT_CACHE_LIMIT , 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            if (size() > DEFAULT_CACHE_LIMIT ) {
                newOrderCache.remove(eldest.getKey());
                return true;
            } else {
                return false;
            }
        }
    };

    public static synchronized boolean hasOrder(String orderID) {
        if (!newOrderCache.containsKey(orderID)) {
            newOrderCache.put(orderID, orderID);
            return true;
        } else {
            return false;
        }

    }
}

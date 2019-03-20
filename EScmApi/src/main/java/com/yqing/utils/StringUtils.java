package com.yqing.utils;


import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    public StringUtils() {
    }

    public static Map<String, String> mapStringToMap(String str) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(",");
        Map<String, String> map = new HashMap();
        String[] var3 = strs;
        int var4 = strs.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String string = var3[var5];
            String key = string.split("=")[0];
            String value = string.split("=")[1];
            map.put(key, value);
        }

        return map;
    }
}

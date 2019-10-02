package com.nei.common.util;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bo27.li on 2018/10/25 19:45.
 */
public class MapUtil {

    public static <K, V> HashMap<K, V> newHashMap(K key, V value) {
        return new HashMap<K, V>() {
            {
                put(key, value);
            }
        };
    }

    /**
     * 将 key1, value1, key2, value2... 键值成对存放的数组转为 Map
     * key / value 类型必须相同
     *
     * @param array 长度必须为偶数
     */
    public static <K, V> Map<K, V> newHashMap(Object... array) {
        if (array == null || array.length == 0) {
            return Collections.emptyMap();
        }
        if (array.length % 2 != 0) {
            throw new IllegalArgumentException("array.length is " + array.length + " must be even numbers");
        }

        Map<K, V> map = new HashMap<>(array.length / 2);
        for (int i = 0; i < array.length; i += 2) {
            map.put((K) array[i], (V) array[i + 1]);
        }
        return map;
    }

    public static <K, V> Map<K, V> newHashMap(K[] keys, V[] values) {
        int length = keys.length;
        if (length != values.length) {
            throw new IllegalArgumentException("keys.length is " + length + " but values.length is " + values.length);
        }

        Map<K, V> map = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    /**
     * 将字符串转为 Map&lt;String, String>
     *
     * @param str 形如 ab=10; cd=20, ef=30
     * @return UnmodifiableMap
     */
    public static Map<String, String> newMap(String str) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyMap();
        }

        return Splitter.onPattern("\\s*[,;]{1,}\\s*") //正则表达式，第一次拆分
                .withKeyValueSeparator('=') //第二次拆分
                .split(str.trim());
    }

    /**
     * 判断 map 中是否存在指定 key，存在时直接添加到 list，不存在时创建 list 并添加
     */
    public static <K, T> void checkAndPut(Map<K, List<T>> map, K key, T t) {
        boolean containsKey = map.containsKey(key);
        if (containsKey) {
            map.get(key).add(t);
        } else {
            List<T> list = new ArrayList<>();
            list.add(t);
            map.put(key, list);
        }
    }

}

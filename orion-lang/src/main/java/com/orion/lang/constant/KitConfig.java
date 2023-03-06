package com.orion.lang.constant;

import com.orion.lang.define.wrapper.PageConfig;
import com.orion.lang.define.wrapper.WrapperConfig;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.reflect.Classes;

import java.util.Map;

/**
 * orion-kit 配置项
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/6 17:00
 */
public class KitConfig {

    private static final Map<String, Object> CONFIG = Maps.newMap();

    static {
        // 加载 wrapper 配置项
        Classes.loadClass(WrapperConfig.class.getName());
        // 加载分页配置项
        Classes.loadClass(PageConfig.class.getName());
    }

    /**
     * 覆盖配置 (会覆盖) 一般用于重写配置
     *
     * @param key key
     * @param v   value
     */
    public static void override(String key, Object v) {
        CONFIG.put(key, v);
    }

    /**
     * 设置配置 (不会覆盖) 一般用于默认配置
     *
     * @param key key
     * @param v   value
     */
    public static void init(String key, Object v) {
        CONFIG.putIfAbsent(key, v);
    }

    /**
     * 获取配置
     *
     * @param key key
     * @param <T> T
     * @return value
     */
    public static <T> T get(String key) {
        return (T) CONFIG.get(key);
    }

    /**
     * 获取配置
     *
     * @param key key
     * @param def def
     * @param <T> T
     * @return value
     */
    public static <T> T getOrDefault(String key, T def) {
        return (T) CONFIG.getOrDefault(key, def);
    }

    /**
     * 获取配置对象
     *
     * @return 配置
     */
    public static Map<String, Object> getConfig() {
        return CONFIG;
    }

}

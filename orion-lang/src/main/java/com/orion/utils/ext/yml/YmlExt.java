package com.orion.utils.ext.yml;

import com.orion.lang.collect.MutableHashSet;
import com.orion.lang.collect.MutableLinkedHashMap;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;
import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * yml 配置文件提取
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/6 13:29
 */
@SuppressWarnings("unchecked")
public class YmlExt {

    private MutableLinkedHashMap<String, Object> store;

    public YmlExt(String path) {
        this(new File(path));
    }

    public YmlExt(File file) {
        try (FileInputStream in = Files1.openInputStream(file)) {
            this.store = Yaml.loadType(in, MutableLinkedHashMap.class);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public YmlExt(InputStream in) {
        try {
            this.store = Yaml.loadType(in, MutableLinkedHashMap.class);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    private YmlExt() {
    }

    /**
     * 获取 YmlExt
     *
     * @param file file
     * @return YmlExt
     */
    public static YmlExt load(File file) {
        return new YmlExt(file);
    }

    /**
     * 获取 YmlExt
     *
     * @param in in
     * @return YmlExt
     */
    public static YmlExt load(InputStream in) {
        return new YmlExt(in);
    }

    /**
     * 获取 YmlExt
     *
     * @param yml ymlString
     * @return YmlExt
     */
    public static YmlExt load(String yml) {
        YmlExt ymlExt = new YmlExt();
        ymlExt.store = Yaml.loadType(yml, MutableLinkedHashMap.class);
        return ymlExt;
    }

    public MutableHashSet<String> getKeys() {
        return new MutableHashSet<>(store.keySet());
    }

    public MutableLinkedHashMap<String, Object> getValues() {
        return store;
    }

    /**
     * 获取值
     *
     * @param key key 用.隔开
     * @return value 有可能是string 有可能是Map
     */
    public Map<String, Object> getValues(String key) {
        try {
            Map<String, Object> value = null;
            String[] nodes = key.split("\\.");
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    value = store.getObject(nodes[0]);
                } else {
                    value = ((Map<String, Object>) value.get(nodes[i]));
                }
            }
            return value;
        } catch (Exception e) {
            throw Exceptions.argument("invalid key " + key);
        }
    }

    /**
     * 获取键
     *
     * @param key key 用.隔开
     * @return value keys
     */
    public Set<String> getKeys(String key) {
        try {
            Map<String, ?> map = null;
            String[] nodes = key.split("\\.");
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    map = store.getObject(nodes[0]);
                } else if (len - 1 > i) {
                    map = ((Map<String, ?>) map.get(nodes[i]));
                }
                if (len - 1 == i) {
                    if (i == 0) {
                        return map.keySet();
                    } else {
                        Object last = map.get(nodes[i]);
                        if (last instanceof Map) {
                            return ((Map<String, ?>) last).keySet();
                        } else {
                            return new HashSet<>();
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw Exceptions.argument("invalid key " + key);
        }
    }

    /**
     * 获取值
     *
     * @param key key 用.隔开
     * @return value
     */
    public String getValue(String key) {
        try {
            String value = null;
            Map<String, Object> map = null;
            String[] nodes = key.split("\\.");
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    if (len == 1) {
                        value = store.getObject(nodes[0]).toString();
                    } else {
                        map = store.getObject(nodes[0]);
                    }
                } else if (len - 1 > i) {
                    map = (Map<String, Object>) map.get(nodes[i]);
                } else if (len - 1 == i) {
                    value = map.get(nodes[i]).toString();
                }
            }
            return value;
        } catch (Exception e) {
            throw Exceptions.argument("invalid key " + key);
        }
    }

    public MutableLinkedHashMap<String, Object> getMap() {
        return store;
    }

    public void forEach(BiConsumer<Object, Object> action) {
        this.store.forEach(action);
    }

}

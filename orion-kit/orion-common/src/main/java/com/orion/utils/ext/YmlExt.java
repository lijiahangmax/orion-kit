package com.orion.utils.ext;

import com.orion.lang.collect.ConvertHashMap;
import com.orion.lang.collect.ConvertHashSet;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * yml 配置文件提取
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/6 13:29
 */
@SuppressWarnings("unchecked")
public class YmlExt {

    private ConvertHashMap<String, Object> convertHashMap;

    public YmlExt(String path) {
        InputStream in = null;
        try {
            in = PropertiesExt.class.getClassLoader().getResourceAsStream(path);
            convertHashMap = Yaml.loadType(in, ConvertHashMap.class);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        } finally {
            Streams.close(in);
        }
    }

    public YmlExt(File file) {
        FileInputStream in = null;
        try {
            in = Files1.openInputStream(file);
            convertHashMap = Yaml.loadType(in, ConvertHashMap.class);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        } finally {
            Streams.close(in);
        }
    }

    public YmlExt(InputStream in) {
        try {
            convertHashMap = Yaml.loadType(in, ConvertHashMap.class);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    private YmlExt() {
    }

    /**
     * 获取YmlExt
     *
     * @param yml ymlString
     * @return YmlExt
     */
    public static YmlExt toYmlExt(String yml) {
        YmlExt ymlExt = new YmlExt();
        ymlExt.convertHashMap = Yaml.loadType(yml, ConvertHashMap.class);
        return ymlExt;
    }

    public ConvertHashSet getKeys() {
        return new ConvertHashSet(convertHashMap.keySet());
    }

    public ConvertHashMap getValues() {
        return convertHashMap;
    }

    /**
     * 获取值
     *
     * @param key key 用.隔开
     * @return value 有可能是string 有可能是Map
     */
    public Map<String, Object> getValues(String key) {
        try {
            Map value = null;
            String[] nodes = key.split("\\.");
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    value = (Map) convertHashMap.getObject(nodes[0]);
                } else {
                    value = ((Map) value.get(nodes[i]));
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
            Map map = null;
            String[] nodes = key.split("\\.");
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    map = (Map) convertHashMap.getObject(nodes[0]);
                } else if (len - 1 > i) {
                    map = ((Map) map.get(nodes[i]));
                }
                if (len - 1 == i) {
                    if (i == 0) {
                        return map.keySet();
                    } else {
                        Object last = map.get(nodes[i]);
                        if (last instanceof Map) {
                            return ((Map) last).keySet();
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
            Map map = null;
            String[] nodes = key.split("\\.");
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    if (len == 1) {
                        value = convertHashMap.getObject(nodes[0]).toString();
                    } else {
                        map = (Map) convertHashMap.getObject(nodes[0]);
                    }
                } else if (len - 1 > i) {
                    map = (Map) map.get(nodes[i]);
                } else if (len - 1 == i) {
                    value = map.get(nodes[i]).toString();
                }
            }
            return value;
        } catch (Exception e) {
            throw Exceptions.argument("invalid key " + key);
        }
    }

    public ConvertHashMap<String, Object> getMap() {
        return convertHashMap;
    }
}

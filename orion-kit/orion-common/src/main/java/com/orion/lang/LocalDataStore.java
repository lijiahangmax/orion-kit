package com.orion.lang;

import com.orion.utils.file.Files1;
import com.orion.utils.Streams;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 将对象存储到本地
 * 对象必须实现序列化接口
 * 如果解析不存在的对象会为null, 但是数据存在
 * getMap 不可以用于操作元素, 如果操作注意序列化问题, 以及使用save()存储
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/22 14:41
 */
public class LocalDataStore {

    /**
     * 本地文件
     */
    private static File localDataStoreFile = new File(System.getProperty("user.dir") + "/data/dataStore.map");

    /**
     * 数据容器
     */
    private static Map<Object, Object> localStore = null;

    static {
        init();
    }

    /**
     * 初始化
     */
    private static void init() {
        Files1.touch(localDataStoreFile);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(localDataStoreFile));
            localStore = (Map<Object, Object>) in.readObject();
        } catch (Exception e) {
            localStore = new HashMap<>(4);
        } finally {
            Streams.closeQuietly(in);
        }
    }

    /**
     * 写入
     */
    private static void write() {
        if (localStore != null) {
            forceClean();
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(localDataStoreFile));
                out.writeObject(localStore);
                out.writeObject(null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Streams.closeQuietly(out);
            }
        }
    }

    /**
     * 清空文件数据
     */
    public static void forceClean() {
        BufferedWriter clean = null;
        try {
            clean = new BufferedWriter(new FileWriter(localDataStoreFile));
            clean.write("");
            clean.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Streams.closeQuietly(clean);
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile() {
        return localDataStoreFile.delete();
    }

    /**
     * 插入元素
     */
    public static void put(Object key, Object value) {
        if (!(key instanceof Serializable)) {
            throw new RuntimeException("NotSerializable");
        }
        if (!(value instanceof Serializable)) {
            throw new RuntimeException("NotSerializable");
        }
        localStore.put(key, value);
        write();
    }

    /**
     * 插入元素
     *
     * @param map 集合
     */
    public static void putAll(Map<Object, Object> map) {
        localStore.putAll(map);
        write();
    }

    /**
     * 获取元素
     *
     * @param key key
     * @return 元素
     */
    public static Object get(Object key) {
        return localStore.get(key);
    }

    /**
     * 获取元素容器
     * 不能插入
     *
     * @return 元素容器
     */
    public static Map<Object, Object> getMap() {
        return localStore;
    }

    /**
     * 删除元素
     *
     * @param key key
     */
    public static void remove(Object key) {
        localStore.remove(key);
        write();
    }

    /**
     * 大小
     */
    public static int size() {
        return localStore.size();
    }

    /**
     * 清空
     */
    public static void clear() {
        localStore.clear();
        write();
    }

    /**
     * 持久化
     */
    public static void save() {
        write();
    }

    @Override
    public String toString() {
        return localStore.toString();
    }
}

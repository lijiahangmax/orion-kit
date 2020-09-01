package com.orion.lang.collect;

import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

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
 * @since 2019/8/22 14:41
 */
public class LocalDataStore {

    /**
     * 本地文件
     */
    private File localDataStoreFile = new File(Files1.getPath(System.getProperty("user.dir") + "/data/dataStore.map"));

    /**
     * 数据容器
     */
    private Map<Object, Object> localStore = null;

    /**
     * 默认store
     */
    public static final LocalDataStore STORE = new LocalDataStore();

    public LocalDataStore() {
        init();
    }

    public LocalDataStore(File file) {
        localDataStoreFile = file;
        init();
    }

    /**
     * 初始化
     */
    @SuppressWarnings("unchecked")
    private void init() {
        Files1.touch(localDataStoreFile);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(localDataStoreFile));
            localStore = (Map<Object, Object>) in.readObject();
        } catch (Exception e) {
            localStore = new HashMap<>(4);
        } finally {
            Streams.close(in);
        }
    }

    /**
     * 写入
     */
    private void write() {
        if (localStore != null) {
            forceClean();
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(localDataStoreFile));
                out.writeObject(localStore);
                out.writeObject(null);
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            } finally {
                Streams.close(out);
            }
        }
    }

    /**
     * 清空文件数据
     */
    public void forceClean() {
        BufferedWriter clean = null;
        try {
            clean = new BufferedWriter(new FileWriter(localDataStoreFile));
            clean.write("");
            clean.flush();
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.close(clean);
        }
    }

    /**
     * 删除文件
     */
    public boolean deleteFile() {
        return localDataStoreFile.delete();
    }

    /**
     * 插入元素
     */
    public void put(Object key, Object value) {
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
    public void putAll(Map<Object, Object> map) {
        localStore.putAll(map);
        write();
    }

    /**
     * 获取元素
     *
     * @param key key
     * @return 元素
     */
    public Object get(Object key) {
        return localStore.get(key);
    }

    /**
     * 获取元素容器
     * 不能插入
     *
     * @return 元素容器
     */
    public Map<Object, Object> getMap() {
        return localStore;
    }

    /**
     * 删除元素
     *
     * @param key key
     */
    public void remove(Object key) {
        localStore.remove(key);
        write();
    }

    /**
     * 大小
     */
    public int size() {
        return localStore.size();
    }

    /**
     * 清空
     */
    public void clear() {
        localStore.clear();
        write();
    }

    /**
     * 持久化
     */
    public void save() {
        write();
    }

    @Override
    public String toString() {
        return localStore.toString();
    }

}

package com.orion.utils.ext;

import com.orion.lang.collect.MutableHashMap;
import com.orion.lang.collect.MutableHashSet;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * properties配置文件提取
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/6 12:40
 */
public class PropertiesExt {

    private Properties properties = new Properties();

    public PropertiesExt() {
    }

    public PropertiesExt(Map<?, ?> map) {
        map.forEach((k, v) -> properties.put(k.toString(), v.toString()));
    }

    public PropertiesExt(String path) {
        this(new File(path));
    }

    public PropertiesExt(File file) {
        this(Files1.openInputStreamSafe(file), true);
    }

    public PropertiesExt(InputStream in) {
        this(in, false);
    }

    public PropertiesExt(InputStream in, boolean close) {
        try {
            properties.load(in);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        } finally {
            if (close) {
                Streams.close(in);
            }
        }
    }

    /**
     * 加载系统配置项
     *
     * @return system PropertiesExt
     * @see System#clearProperty delete方法不会删除系统配置, 但是会修改当前properties对象
     * @see System#setProperty put方法不会修改系统配置, 但是会修改当前properties对象
     */
    public static PropertiesExt loadSystem() {
        return new PropertiesExt(System.getProperties());
    }

    /**
     * 获取配置文件的值
     *
     * @param key key
     * @return value
     */
    public String getValue(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取配置文件的值
     *
     * @param key key
     * @param def 默认值
     * @return value
     */
    public String getValue(String key, String def) {
        return properties.getProperty(key, def);
    }

    /**
     * 获取配置文件的所有值
     *
     * @return values
     */
    public MutableHashMap<Object, Object> getValues() {
        return new MutableHashMap<>(properties);
    }

    /**
     * 获取配置文件的所有键
     *
     * @return values
     */
    public MutableHashSet<Object> getKeys() {
        return new MutableHashSet<>(properties.keySet());
    }

    /**
     * 设置配置文件的值
     *
     * @param key   key
     * @param value value
     */
    public PropertiesExt setValue(String key, String value) {
        properties.setProperty(key, value);
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public PropertiesExt setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    /**
     * properties 转 file
     *
     * @param file file
     * @throws IOException IOException
     */
    public void writeToFile(File file) throws IOException {
        Files1.touch(file);
        properties.store(Files1.openOutputStream(file), "#");
    }

    /**
     * properties 转 xml
     *
     * @param file xml
     * @throws IOException IOException
     */
    public void writeToXml(File file) throws IOException {
        Files1.touch(file);
        properties.storeToXML(Files1.openOutputStream(file), "#");
    }

    /**
     * properties 转 xml
     *
     * @param file    xml
     * @param charset 编码格式
     * @throws IOException IOException
     */
    public void writeToXml(File file, String charset) throws IOException {
        Files1.touch(file);
        properties.storeToXML(Files1.openOutputStream(file), "#", charset);
    }

    /**
     * 将当前properties存储到system
     *
     * @return this
     */
    public PropertiesExt storeSystem() {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.setProperty(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return this;
    }

    public void forEach(BiConsumer<Object, Object> action) {
        properties.forEach(action);
    }

}

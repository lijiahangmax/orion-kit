package com.orion.utils.ext;

import com.orion.lang.collect.ConvertHashMap;
import com.orion.lang.collect.ConvertHashSet;
import com.orion.utils.file.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * properties配置文件提取
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/6 12:40
 */
@SuppressWarnings("ALL")
public class PropertiesExt {

    private Properties properties = new Properties();

    public PropertiesExt() {
    }

    public PropertiesExt(Properties properties) {
        this.properties = properties;
    }

    public PropertiesExt(File file) throws IOException {
        this(Files1.openInputStream(file));
    }

    public PropertiesExt(InputStream in) {
        try {
            properties.load(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public PropertiesExt(String path) {
        InputStream inputStream = null;
        try {
            inputStream = PropertiesExt.class.getClassLoader().getResourceAsStream(path);
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载系统配置项
     *
     * @return systemPropertiesExt
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
    public ConvertHashMap getValues() {
        return new ConvertHashMap(properties);
    }

    /**
     * 获取配置文件的所有键
     *
     * @return values
     */
    public ConvertHashSet getKeys() {
        return new ConvertHashSet(properties.keySet());
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
    public void writeToXML(File file) throws IOException {
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
    public void writeToXML(File file, String charset) throws IOException {
        Files1.touch(file);
        properties.storeToXML(Files1.openOutputStream(file), "#", charset);
    }

    /**
     * 将当前properties存储到system
     *
     * @return this
     */
    public PropertiesExt storeSystem() {
        for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            System.setProperty(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return this;
    }

}

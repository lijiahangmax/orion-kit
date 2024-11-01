/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.ext;

import cn.orionsec.kit.lang.define.collect.MutableHashMap;
import cn.orionsec.kit.lang.define.collect.MutableHashSet;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * properties 配置文件提取
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

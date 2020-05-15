package com.orion.csv;

import com.orion.csv.core.CsvReader;
import com.orion.utils.reflect.BeanWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CSV 读取流
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/27 12:21
 */
public class CvsStream {

    private CsvReader reader;

    private List<String[]> lines = new ArrayList<>();

    private String[] headers;

    private String[] line;

    CvsStream(CsvReader reader) {
        this.reader = reader;
    }

    // -------------------- skip --------------------

    /**
     * 跳过头信息 一行
     *
     * @return this
     */
    public CvsStream skipHeaders() {
        try {
            reader.readHeaders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 跳过头信息 多行
     *
     * @param line 行
     * @return this
     */
    public CvsStream skipHeaders(int line) {
        try {
            for (int i = 0; i < line; i++) {
                reader.readHeaders();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 跳过行
     *
     * @return this
     */
    public CvsStream skipLine() {
        try {
            reader.skipRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 跳过多行
     *
     * @param lines 行
     * @return this
     */
    public CvsStream skipLines(int lines) {
        try {
            for (int i = 0; i < lines; i++) {
                if (!reader.skipRecord()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    // -------------------- read --------------------

    /**
     * 读取一行
     *
     * @return this
     */
    public CvsStream readLine() {
        try {
            if (reader.readRecord()) {
                String[] line = reader.getValues();
                this.line = line;
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 读取多行
     *
     * @param line 行
     * @return this
     */
    public CvsStream readLines(int line) {
        try {
            int i = 0;
            while (i++ < line && reader.readRecord()) {
                lines.add(reader.getValues());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 读取剩余行
     *
     * @return this
     */
    public CvsStream readLines() {
        try {
            while (reader.readRecord()) {
                lines.add(reader.getValues());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    // -------------------- result --------------------

    /**
     * 获取列数量 要在读取之前获取
     *
     * @return 列数
     */
    public int columnCount() {
        return reader.getColumnCount();
    }

    /**
     * 获取行记录
     *
     * @return line
     */
    public String[] line() {
        return line;
    }

    /**
     * 获取行记录
     *
     * @return lines
     */
    public List<String[]> lines() {
        return lines;
    }

    /**
     * 获取头 要在读取之前获取
     *
     * @return 头
     */
    public String[] getHeaders() {
        try {
            if (headers == null) {
                if (reader.readHeaders()) {
                    headers = reader.getHeaders();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    /**
     * 获取头 要在读取之前获取
     *
     * @param index 索引
     * @return 头
     */
    public String getHeader(int index) {
        return getHeaders()[index];
    }

    /**
     * 单行转bean
     *
     * @param clazz beanClass
     * @param map   fieldMap
     * @param <T>   T
     * @return bean
     */
    public <T> T toBean(Class<T> clazz, Map<Integer, String> map) {
        return BeanWrapper.toBean(line, map, clazz);
    }

    /**
     * 多行转bean
     *
     * @param clazz beanClass
     * @param map   fieldMap
     * @param <T>   T
     * @return bean
     */
    public <T> List<T> toBeans(Class<T> clazz, Map<Integer, String> map) {
        List<T> list = new ArrayList<>();
        for (String[] line : lines) {
            list.add(BeanWrapper.toBean(line, map, clazz));
        }
        return list;
    }

    /**
     * 单行转Map
     *
     * @param map fieldMap
     * @return bean
     */
    public Map<String, String> toMap(Map<Integer, String> map) {
        return BeanWrapper.toMap(line, map);
    }

    /**
     * 多行转Map
     *
     * @param map fieldMap
     * @return bean
     */
    public List<Map<String, String>> toMaps(Map<Integer, String> map) {
        List<Map<String, String>> list = new ArrayList<>();
        for (String[] line : lines) {
            list.add(BeanWrapper.toMap(line, map));
        }
        return list;
    }

}

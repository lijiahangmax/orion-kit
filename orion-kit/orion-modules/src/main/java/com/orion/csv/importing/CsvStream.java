package com.orion.csv.importing;

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
 * @since 2020/3/27 12:21
 */
public class CsvStream {

    private CsvReader reader;

    private List<String[]> lines = new ArrayList<>();

    private String[] headers;

    public CsvStream(CsvReader reader) {
        this.reader = reader;
    }

    // -------------------- skip --------------------

    /**
     * 跳过头信息 一行
     *
     * @return this
     */
    public CsvStream skipHeaders() {
        try {
            reader.readHeaders();
        } catch (Exception e) {
            // ignore
        }
        return this;
    }

    /**
     * 跳过头信息 多行
     *
     * @param lines 行
     * @return this
     */
    public CsvStream skipHeaders(int lines) {
        try {
            for (int i = 0; i < lines; i++) {
                reader.readHeaders();
            }
        } catch (Exception e) {
            // ignore
        }
        return this;
    }

    /**
     * 跳过行
     *
     * @return this
     */
    public CsvStream skipLine() {
        try {
            reader.skipRecord();
        } catch (Exception e) {
            // ignore
        }
        return this;
    }

    /**
     * 跳过多行
     *
     * @param lines 行
     * @return this
     */
    public CsvStream skipLines(int lines) {
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
    public CsvStream readLine() {
        try {
            if (reader.readRecord()) {
                String[] line = reader.getValues();
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
    public CsvStream readLines(int line) {
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
    public CsvStream readLines() {
        try {
            while (reader.readRecord()) {
                lines.add(reader.getValues());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 清空已读取的行
     *
     * @return this
     */
    public CsvStream clean() {
        lines.clear();
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

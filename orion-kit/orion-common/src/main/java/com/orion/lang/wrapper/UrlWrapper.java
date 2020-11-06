package com.orion.lang.wrapper;

import com.orion.able.JsonAble;
import com.orion.able.Logable;
import com.orion.able.Mapable;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.json.Jsons;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要对url操作的结果集
 * HttpWrapper<UrlWrapper>
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/5/30 22:52
 */
public class UrlWrapper<T> extends CloneSupport<UrlWrapper<T>> implements Wrapper<T>, JsonAble, Logable, Mapable<String, Object> {

    private static final long serialVersionUID = 4250545197688197L;

    /**
     * url
     */
    private String url;

    /**
     * 操作 1: 无操作  2: 刷新  3: 重定向到url
     */
    private Integer type;

    /**
     * 携带的数据
     */
    private T data;

    private UrlWrapper() {
    }

    private UrlWrapper(String url, Integer type, T data) {
        this.url = url;
        this.type = type;
        this.data = data;
    }

    /**
     * 初始化
     */
    public static <T> UrlWrapper<T> get() {
        return new UrlWrapper<>();
    }

    public static <T> UrlWrapper<T> get(T data) {
        return new UrlWrapper<>("", 1, data);
    }

    /**
     * 刷新页面
     */
    public static <T> UrlWrapper<T> refresh() {
        return new UrlWrapper<>("", 2, null);
    }

    public static <T> UrlWrapper<T> refresh(T data) {
        return new UrlWrapper<>("", 2, data);
    }

    /**
     * 重定向页面
     */
    public static <T> UrlWrapper<T> redirect() {
        return new UrlWrapper<>("", 3, null);
    }

    public static <T> UrlWrapper<T> redirect(String url) {
        return new UrlWrapper<>(url, 3, null);
    }

    public static <T> UrlWrapper<T> redirect(String url, T data) {
        return new UrlWrapper<>(url, 3, data);
    }

    public UrlWrapper<T> url(String url) {
        this.url = url;
        return this;
    }

    public UrlWrapper<T> type(Integer type) {
        this.type = type;
        return this;
    }

    public UrlWrapper<T> data(T data) {
        this.data = data;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UrlWrapper<T> setUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public UrlWrapper<T> setType(Integer type) {
        this.type = type;
        return this;
    }

    public T getData() {
        return data;
    }

    public UrlWrapper<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return toLogString();
    }

    @Override
    public String toJsonString() {
        return Jsons.toJsonWriteNull(this);
    }

    @Override
    public String toLogString() {
        return "UrlWrapper:\n\turl ==> " + url + "\n\t" +
                "type ==> " + type + "\n\t" +
                "data ==> " + Jsons.toJsonWriteNull(data);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("url", url);
        map.put("type", type);
        map.put("data", data);
        return map;
    }

}

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
package com.orion.lang.define.wrapper;

import com.orion.lang.KitLangConfiguration;
import com.orion.lang.able.ILogObject;
import com.orion.lang.able.IMapObject;
import com.orion.lang.config.KitConfig;
import com.orion.lang.constant.Const;
import com.orion.lang.define.support.CloneSupport;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.json.Jsons;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要对 url 操作的结果集
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see HttpWrapper<UrlWrapper>
 * @since 2019/5/30 22:52
 */
public class UrlWrapper<T> extends CloneSupport<UrlWrapper<T>> implements Wrapper<T>, ILogObject, IMapObject<String, Object> {

    private static final long serialVersionUID = 4250545197688197L;

    // -------------------- URL --------------------

    public static final Integer URL_NO_OPERATION = KitConfig.get(KitLangConfiguration.CONFIG.URL_NO_OPERATION);

    public static final Integer URL_REFRESH = KitConfig.get(KitLangConfiguration.CONFIG.URL_REFRESH);

    public static final Integer URL_REDIRECT = KitConfig.get(KitLangConfiguration.CONFIG.URL_REDIRECT);

    /**
     * url
     */
    private String url;

    /**
     * 操作 1: 无操作  2: 刷新  3: 重定向到url
     */
    private int type;

    /**
     * 携带的数据
     */
    private T data;

    private UrlWrapper() {
    }

    private UrlWrapper(String url) {
        this.url = url;
    }

    private UrlWrapper(String url, int type) {
        this.url = url;
        this.type = type;
    }

    private UrlWrapper(String url, T data) {
        this.url = url;
        this.data = data;
    }

    private UrlWrapper(String url, int type, T data) {
        this.url = url;
        this.type = type;
        this.data = data;
    }

    /**
     * 初始化
     */
    public static <T> UrlWrapper<T> get() {
        return new UrlWrapper<>(Strings.EMPTY, URL_NO_OPERATION, null);
    }

    public static <T> UrlWrapper<T> get(T data) {
        return new UrlWrapper<>(Strings.EMPTY, URL_NO_OPERATION, data);
    }

    /**
     * 刷新页面
     */
    public static <T> UrlWrapper<T> refresh() {
        return new UrlWrapper<>(Strings.EMPTY, URL_REFRESH, null);
    }

    public static <T> UrlWrapper<T> refresh(T data) {
        return new UrlWrapper<>(Strings.EMPTY, URL_REFRESH, data);
    }

    /**
     * 重定向页面
     */
    public static <T> UrlWrapper<T> redirect() {
        return new UrlWrapper<>(Strings.EMPTY, URL_REDIRECT, null);
    }

    public static <T> UrlWrapper<T> redirect(String url) {
        return new UrlWrapper<>(url, URL_REDIRECT, null);
    }

    public static <T> UrlWrapper<T> redirect(String url, T data) {
        return new UrlWrapper<>(url, URL_REDIRECT, data);
    }

    public UrlWrapper<T> url(String url) {
        this.url = url;
        return this;
    }

    public UrlWrapper<T> type(int type) {
        this.type = type;
        return this;
    }

    public UrlWrapper<T> data(T data) {
        this.data = data;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return this.toJsonString();
    }

    @Override
    public String toLogString() {
        return "UrlWrapper:" +
                "\n   url ==> " + url +
                "\n  type ==> " + type +
                "\n  data ==> " + Jsons.toJsonWriteNull(data);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(Const.CAPACITY_8);
        map.put("url", url);
        map.put("type", type);
        map.put("data", data);
        return map;
    }

}

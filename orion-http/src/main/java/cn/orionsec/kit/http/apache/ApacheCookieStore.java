/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.http.apache;

import cn.orionsec.kit.lang.constant.Const;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Map;

/**
 * Apache Cookie
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/30 18:38
 */
public class ApacheCookieStore extends BasicCookieStore {

    public ApacheCookieStore addCookie(String key, String value, String domain) {
        return this.addCookie(key, value, domain, Const.ROOT_PATH);
    }

    public ApacheCookieStore addCookie(String key, String value, String domain, String path) {
        BasicClientCookie c = new BasicClientCookie(key, value);
        c.setDomain(domain);
        c.setPath(path);
        this.addCookie(c);
        return this;
    }

    public ApacheCookieStore addCookies(Map<String, String> map, String domain) {
        return this.addCookies(map, domain, Const.ROOT_PATH);
    }

    public ApacheCookieStore addCookies(Map<String, String> map, String domain, String path) {
        map.forEach((k, v) -> {
            BasicClientCookie c = new BasicClientCookie(k, v);
            c.setDomain(domain);
            c.setPath(path);
            this.addCookie(c);
        });
        return this;
    }

}

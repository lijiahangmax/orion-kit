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
package cn.orionsec.kit.http.useragent;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * UA 解析器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 18:01
 */
public class UserAgentParses {

    private UserAgentParses() {
    }

    /**
     * 解析UA
     *
     * @param userAgent UA
     * @return UA信息
     */
    public static UserAgentInfo parse(String userAgent) {
        return new UserAgentInfo(UserAgent.parseUserAgentString(userAgent));
    }

    /**
     * 解析UA
     *
     * @param id UA id
     * @return UA信息
     */
    public static UserAgentInfo parseById(int id) {
        return new UserAgentInfo(UserAgent.valueOf(id));
    }

    /**
     * 解析UA
     *
     * @param name UA name
     * @return UA信息
     */
    public static UserAgentInfo parseByName(String name) {
        return new UserAgentInfo(UserAgent.valueOf(name));
    }

}

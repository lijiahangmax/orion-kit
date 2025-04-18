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
package cn.orionsec.kit.net.ftp.client;

import cn.orionsec.kit.lang.utils.regexp.Matches;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * FTP 文件过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/12 9:50
 */
@FunctionalInterface
public interface FtpFileFilter extends Predicate<FtpFile> {

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return filter
     */
    static FtpFileFilter suffix(String suffix) {
        return a -> a.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 包含过滤器
     *
     * @param name 文件名
     * @return filter
     */
    static FtpFileFilter contains(String name) {
        return a -> a.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return filter
     */
    static FtpFileFilter matches(Pattern pattern) {
        return a -> Matches.test(a.getName(), pattern);
    }

}

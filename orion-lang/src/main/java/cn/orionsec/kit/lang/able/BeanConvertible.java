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
package cn.orionsec.kit.lang.able;

import cn.orionsec.kit.lang.utils.reflect.BeanWrapper;

/**
 * bean 转化接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/30 17:11
 */
public interface BeanConvertible {

    /**
     * sourceBean -> targetBean
     *
     * @param clazz targetBean class
     * @param <T>   T
     * @return targetBean
     */
    <T> T convert(Class<T> clazz);

    /**
     * copy bean
     *
     * @param clazz targetBean
     * @param <T>   T
     * @return targetBean
     */
    default <T> T copyProperties(Class<T> clazz) {
        return BeanWrapper.copyProperties(this, clazz);
    }

}
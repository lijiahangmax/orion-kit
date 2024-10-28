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
package cn.orionsec.kit.lang.utils.loader;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * SPI 服务加载工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/25 12:11
 */
public class ServiceLoaders {

    private ServiceLoaders() {
    }

    /**
     * 加载第一个可用服务, 如果定义多个接口实现类, 只获取第一个不报错的服务
     *
     * @param clazz 服务接口
     * @param <T>   接口类型
     * @return 第一个服务接口实现对象
     */
    public static <T> T loadFirstAvailable(Class<T> clazz) {
        Iterator<T> iterator = load(clazz).iterator();
        while (iterator.hasNext()) {
            try {
                return iterator.next();
            } catch (ServiceConfigurationError e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 加载第一个服务, 如果定义了多个接口实现类, 只获取第一个
     *
     * @param clazz 服务接口
     *              * @param <T>   接口类型
     * @return 第一个服务接口实现对象
     */
    public static <T> T loadFirst(Class<T> clazz) {
        Iterator<T> iterator = load(clazz).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * 加载服务
     *
     * @param clazz 服务接口
     * @param <T>   接口类型
     * @return 服务接口实现
     */
    public static <T> ServiceLoader<T> load(Class<T> clazz) {
        return ServiceLoader.load(clazz);
    }

    /**
     * 加载服务
     *
     * @param clazz  服务接口
     * @param loader 类加载器
     * @param <T>    接口类型
     * @return 服务接口实现
     */
    public static <T> ServiceLoader<T> load(Class<T> clazz, ClassLoader loader) {
        return ServiceLoader.load(clazz, loader);
    }

}

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
package cn.orionsec.kit.spring;

import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Proxy;

/**
 * spring 获取源对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/10 17:35
 */
public class ProxyTargets {

    private ProxyTargets() {
    }

    /**
     * 判断是否为spring的代理类
     *
     * @param o 对象
     * @return true springProxy
     */
    public static boolean isSpringProxy(Object o) {
        return o instanceof SpringProxy;
    }

    /**
     * 判断是否为代理类
     *
     * @param o 对象
     * @return true proxy
     */
    public static boolean isProxy(Object o) {
        return o instanceof SpringProxy || (o != null && Proxy.isProxyClass(o.getClass()));
    }

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return 目标对象
     * @throws Exception e
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        return ((Advised) proxy).getTargetSource().getTarget();
    }

}

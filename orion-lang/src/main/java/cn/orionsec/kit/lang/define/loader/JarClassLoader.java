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
package cn.orionsec.kit.lang.define.loader;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.reflect.Classes;
import cn.orionsec.kit.lang.utils.reflect.Methods;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * jar 加载器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/25 14:10
 */
public class JarClassLoader extends URLClassLoader {

    /**
     * 构造
     */
    public JarClassLoader() {
        this(new URL[]{});
    }

    /**
     * 构造
     *
     * @param urls 被加载的URL
     */
    public JarClassLoader(URL[] urls) {
        super(urls, Classes.getCurrentClassLoader());
    }

    /**
     * 加载 jar 到 classPath
     *
     * @param file file
     * @return JarClassLoader
     */
    public static JarClassLoader load(File file) {
        JarClassLoader loader = new JarClassLoader();
        loader.addJar(file);
        return loader;
    }

    /**
     * 加载 jar 到 SystemClassLoader
     *
     * @param file file
     * @return SystemClassLoader
     */
    public static URLClassLoader loadJar(File file) {
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        loadJar(urlClassLoader, file);
        return urlClassLoader;
    }

    /**
     * 加载 jar 到 classLoader
     *
     * @param loader 加载器
     * @param file   file
     */
    public static void loadJar(URLClassLoader loader, File file) {
        try {
            Method method = Methods.getAccessibleMethod(URLClassLoader.class, "addURL", URL.class);
            if (method != null) {
                Methods.invokeMethod(loader, method, file.toURI().toURL());
            }
        } catch (Exception e) {
            throw Exceptions.load(e);
        }
    }

    /**
     * 加载 jar 文件
     *
     * @param file file
     * @return this
     */
    public JarClassLoader addJar(String file) {
        Assert.notBlank(file, "file path is empty");
        return this.addJar(new File(file));
    }

    /**
     * 加载 jar文件
     *
     * @param file file
     * @return this
     */
    public JarClassLoader addJar(File file) {
        Assert.notNull(file, "jar file is null");
        if (isJarFile(file)) {
            this.addURL(Files1.toUrl(file));
        } else {
            throw Exceptions.load("file is not jar file");
        }
        return this;
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    /**
     * 是否为jar文件
     *
     * @param file file
     * @return true jar
     */
    private static boolean isJarFile(File file) {
        if (!Files1.isFile(file)) {
            return false;
        }
        return file.getPath().toLowerCase().endsWith(".jar");
    }

}

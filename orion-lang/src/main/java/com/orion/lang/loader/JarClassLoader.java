package com.orion.lang.loader;

import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.reflect.Classes;
import com.orion.utils.reflect.Methods;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * jar 加载器
 *
 * @author ljh15
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
     * 加载jar 到 classPath
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
     * 加载jar 到 SystemClassLoader
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
     * 加载jar 到 classLoader
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
     * 加载 jar文件
     *
     * @param file file
     * @return this
     */
    public JarClassLoader addJar(String file) {
        Valid.notBlank(file, "file path is empty");
        return addJar(new File(file));
    }

    /**
     * 加载 jar文件
     *
     * @param file file
     * @return this
     */
    public JarClassLoader addJar(File file) {
        Valid.notNull(file, "jar file is null");
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

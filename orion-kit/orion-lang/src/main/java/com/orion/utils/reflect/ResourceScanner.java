package com.orion.utils.reflect;

import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Urls;
import com.orion.utils.collect.Sets;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 资源扫描器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/2 11:47
 */
public class ResourceScanner {

    /**
     * target resource url
     */
    private Set<URL> resourcesUrl = new LinkedHashSet<>();

    /**
     * 扫描到的资源
     */
    private Set<String> resources = new LinkedHashSet<>();

    /**
     * 包含的后缀
     */
    private Set<String> includeSuffix = new HashSet<>();

    /**
     * 排除的后缀 如果配置包含则不生效
     */
    private Set<String> excludeSuffix = new HashSet<>();

    /**
     * 扫描所有资源
     */
    private boolean all;

    public ResourceScanner() {
    }

    /**
     * 加载资源
     *
     * @param resource resource
     * @return InputStream
     */
    public static InputStream getResourceAsStream(String resource) {
        return ResourceScanner.class.getClassLoader().getResourceAsStream(resource);
    }

    /**
     * 扫描所有资源
     * 否则只扫描当前资源
     *
     * @return this
     */
    public ResourceScanner all() {
        this.all = true;
        return this;
    }

    /**
     * 包含的后缀 需要加.
     *
     * @param suffix 后缀
     * @return this
     */
    public ResourceScanner include(String... suffix) {
        if (!Arrays1.isEmpty(suffix)) {
            Arrays1.forEach(includeSuffix::add, suffix);
        }
        return this;
    }

    /**
     * 排除的后缀 需要加. 如果配置包含则不生效
     *
     * @param suffix 后缀
     * @return this
     */
    public ResourceScanner exclude(String... suffix) {
        if (!Arrays1.isEmpty(suffix)) {
            Arrays1.forEach(excludeSuffix::add, suffix);
        }
        return this;
    }

    /**
     * 扫描
     *
     * @return this
     */
    public ResourceScanner scan() {
        this.addResource();
        for (URL resource : resourcesUrl) {
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                this.scanFile(resource);
            } else if ("jar".equals(protocol)) {
                this.scanJar(resource);
            }
        }
        return this;
    }

    /**
     * 添加resource
     */
    private void addResource() {
        resourcesUrl.clear();
        if (all) {
            try {
                resourcesUrl.addAll(Sets.as(ResourceScanner.class.getClassLoader().getResources("")));
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        } else {
            URL r = ResourceScanner.class.getClassLoader().getResource("");
            if (r == null) {
                r = ResourceScanner.class.getResource("");
            }
            if (r != null) {
                resourcesUrl.add(r);
            }
        }
    }

    /**
     * 扫描 file://*
     *
     * @param resource resource
     */
    private void scanFile(URL resource) {
        String path = Files1.getPath(Urls.decode(resource.getPath()));
        List<File> files = Files1.listFilesFilter(path, (f, n) -> !n.endsWith(".class") && !f.isDirectory() && this.isAdd(f.getAbsolutePath()), true);
        resources.addAll(Files1.toPaths(files));
    }

    /**
     * 扫描 jar://*
     *
     * @param resource resource
     */
    private void scanJar(URL resource) {
        try {
            JarURLConnection connection = (JarURLConnection) resource.openConnection();
            if (connection == null) {
                return;
            }
            JarFile jarFile = connection.getJarFile();
            if (jarFile != null) {
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    String jarEntryName = jarEntry.getName();
                    if (!jarEntryName.endsWith(".class") && !jarEntryName.endsWith("/") && this.isAdd(jarEntryName)) {
                        resources.add(jarEntryName);
                    }
                }
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime("scan jar file error", e);
        }
    }

    /**
     * 判断是否添加
     *
     * @param path path
     * @return true添加
     */
    private boolean isAdd(String path) {
        boolean ie = includeSuffix.isEmpty();
        if (ie && excludeSuffix.isEmpty()) {
            return true;
        }
        if (!ie) {
            return includeSuffix.stream().anyMatch(path::endsWith);
        }
        return excludeSuffix.stream().noneMatch(path::endsWith);
    }

    public Set<URL> getResourcesUrl() {
        return resourcesUrl;
    }

    public Set<String> getResources() {
        return resources;
    }

}

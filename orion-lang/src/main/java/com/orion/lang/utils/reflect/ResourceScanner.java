package com.orion.lang.utils.reflect;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.*;
import com.orion.lang.utils.io.Files1;

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
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/2 11:47
 */
public class ResourceScanner {

    /**
     * 加载的资源
     */
    private final Set<URL> resources;

    /**
     * 扫描到的资源
     */
    private final Set<String> scannedResources;

    /**
     * 包含的后缀
     */
    private final Set<String> includeSuffix;

    /**
     * 排除的后缀 如果配置包含则不生效
     */
    private final Set<String> excludeSuffix;

    public ResourceScanner() {
        this.resources = new LinkedHashSet<>();
        this.scannedResources = new LinkedHashSet<>();
        this.includeSuffix = new HashSet<>();
        this.excludeSuffix = new HashSet<>();
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
     * 设置扫描的资源
     *
     * @param resource resource
     * @return this
     */
    public ResourceScanner addResource(URL... resource) {
        if (Arrays1.isEmpty(resource)) {
            return this;
        }
        for (URL r : resource) {
            if (r != null) {
                resources.add(r);
            }
        }
        return this;
    }

    /**
     * 添加资源 class
     *
     * @param resourceClass resourceClass
     * @return this
     */
    public ResourceScanner with(Class<?> resourceClass) {
        Valid.notNull(resourceClass, "resourceClass is null");
        URL r1 = resourceClass.getProtectionDomain().getCodeSource().getLocation();
        URL r2 = resourceClass.getClassLoader().getResource(Strings.EMPTY);
        if (r1 != null) {
            resources.add(r1);
        }
        if (r2 != null) {
            resources.add(r2);
        }
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
        for (URL resource : resources) {
            String protocol = resource.getProtocol();
            if (Const.PROTOCOL_FILE.equals(protocol)) {
                this.scanFile(resource);
            } else if (Const.PROTOCOL_JAR.equals(protocol)) {
                this.scanJar(resource);
            }
        }
        return this;
    }

    /**
     * 扫描 file://*
     *
     * @param resource resource
     */
    private void scanFile(URL resource) {
        String path = Files1.getPath(Urls.decode(resource.getPath()));
        List<File> files = Files1.listFilesFilter(path, (f, n) -> !n.endsWith(".class") && !f.isDirectory() && this.canAdd(f.getAbsolutePath()), true);
        scannedResources.addAll(Files1.toPaths(files));
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
                    if (!jarEntryName.endsWith(".class") && !jarEntryName.endsWith("/") && this.canAdd(jarEntryName)) {
                        scannedResources.add(jarEntryName);
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
    private boolean canAdd(String path) {
        boolean ie = includeSuffix.isEmpty();
        if (ie && excludeSuffix.isEmpty()) {
            return true;
        }
        if (!ie) {
            return includeSuffix.stream().anyMatch(path::endsWith);
        }
        return excludeSuffix.stream().noneMatch(path::endsWith);
    }

    public Set<URL> getResources() {
        return resources;
    }

    public Set<String> getScannedResources() {
        return scannedResources;
    }

}

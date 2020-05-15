package com.orion.utils.reflect;

import com.orion.utils.Valid;
import com.orion.utils.file.Files1;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射 jar工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/15 13:17
 */
@SuppressWarnings("ALL")
public class Jars {

    private Jars() {
    }

    /**
     * 获取target目录URL
     *
     * @return targetUrl / thisJar
     */
    public static URL getTargetURL() {
        URL resource = Methods.class.getClassLoader().getResource("");
        if (resource == null) {
            resource = Methods.class.getResource("");
        }
        return resource;
    }

    /**
     * 获取当前jar文件, 如果不是jar环境返回null
     *
     * @return 当前 jarFile
     */
    public static JarFile getJarFile() {
        URL targetURL = getTargetURL();
        if (targetURL != null && "jar".equals(targetURL.getProtocol())) {
            try {
                return ((JarURLConnection) targetURL.openConnection()).getJarFile();
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 获取jar文件
     *
     * @param file jar文件
     * @return jarFile
     */
    public static JarFile getJarFile(File file) {
        try {
            return new JarFile(file);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取jar文件
     *
     * @param url url 支持jar file
     * @return jarFile
     */
    public static JarFile getJarFile(URL url) {
        String protocol = url.getProtocol();
        try {
            switch (protocol) {
                case "jar":
                    return ((JarURLConnection) url.openConnection()).getJarFile();
                case "file":
                    return new JarFile(url.getFile());
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从jar获取所有类
     *
     * @param jarPath jar
     * @return 类名
     */
    public static List<String> getClassByJar(String jarPath) {
        Valid.notBlank(jarPath, "Jar Path is null");
        List<String> classes = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            return getClassByJar(jarFile);
        } catch (IOException e) {
            // ignore
        }
        return classes;
    }

    /**
     * 从jar获取所有类
     *
     * @param jarFile jar
     * @return 类名
     */
    public static List<String> getClassByJar(JarFile jarFile) {
        Valid.notNull(jarFile, "Jar File is null");
        List<String> classes = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String className = entries.nextElement().getName();
            if (className.endsWith(".class")) {
                classes.add(className.replace("/", ".").substring(0, className.lastIndexOf(".")));
            }
        }
        return classes;
    }

    /**
     * 从jar中获取除class的所有文件
     *
     * @param jarPath jar
     * @return 资源名称
     */
    public static List<String> getSourceByJar(String jarPath) {
        Valid.notBlank(jarPath, "Jar Path is null");
        List<String> sources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String fileName = entries.nextElement().getName();
                if (!fileName.endsWith(".class") && !fileName.endsWith("/")) {
                    sources.add(Files1.getPath(jarPath + "!/" + fileName));
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return sources;
    }

    /**
     * 从jar中获取除class的所有文件
     *
     * @param jarFile jar
     * @return 资源名称
     */
    public static List<String> getSourceByJar(JarFile jarFile) {
        Valid.notNull(jarFile, "jar File is null");
        List<String> sources = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String fileName = entries.nextElement().getName();
            if (!fileName.endsWith(".class") && !fileName.endsWith("/")) {
                sources.add("/" + fileName);
            }
        }
        return sources;
    }

    /**
     * 从jar中获取文件
     *
     * @param jarPath jar
     * @param suffix  后缀
     * @return 资源名称
     */
    public static List<String> getSourceByJar(String jarPath, String suffix) {
        Valid.notBlank(jarPath, "Jar Path is null");
        List<String> sources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String fileName = entries.nextElement().getName();
                if (suffix != null && fileName.endsWith(suffix) && !fileName.endsWith("/")) {
                    sources.add(Files1.getPath(jarPath + "!/" + fileName));
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return sources;
    }

    /**
     * 从jar中获取文件
     *
     * @param jarFile jar
     * @param suffix  后缀
     * @return 资源名称
     */
    public static List<String> getSourceByJar(JarFile jarFile, String suffix) {
        Valid.notNull(jarFile, "Jar File is null");
        List<String> sources = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String fileName = entries.nextElement().getName();
            if (suffix != null && fileName.endsWith(suffix) && !fileName.endsWith("/")) {
                sources.add("/" + fileName);
            }
        }
        return sources;
    }

}

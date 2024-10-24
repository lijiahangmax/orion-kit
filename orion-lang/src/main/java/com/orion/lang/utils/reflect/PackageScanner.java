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
package com.orion.lang.utils.reflect;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.*;
import com.orion.lang.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/23 17:38
 */
@SuppressWarnings("ALL")
public class PackageScanner {

    /**
     * 扫描到的类
     */
    private final Set<Class<?>> classes;

    /**
     * 扫描的包
     */
    private final Set<String> packages;

    /**
     * 加载的资源
     */
    private final Set<URL> resources;

    /**
     * 扫描类加载器
     */
    private ClassLoader classLoader;

    /**
     * 是否扫描所有包
     */
    private boolean scanAll;

    /**
     * 扫描指定包 如果不填扫描当前项目所有的包
     *
     * @param packages 包名 可以用.*结尾 扫描子包
     */
    public PackageScanner(String... packages) {
        this.classes = new LinkedHashSet<>();
        this.packages = new LinkedHashSet<>();
        this.resources = new LinkedHashSet<>();
        this.classLoader = Classes.getCurrentClassLoader();
        if (Arrays1.isEmpty(packages)) {
            this.scanAll = true;
        } else {
            for (String packageName : packages) {
                if (!Strings.isBlank(packageName)) {
                    this.packages.add(packageName);
                }
            }
        }
    }

    /**
     * 添加扫描的包
     *
     * @param packageName 包名 可以用.*结尾 扫描子包
     * @return this
     */
    public PackageScanner addPackage(String... packageName) {
        if (Arrays1.isEmpty(packageName)) {
            return this;
        }
        for (String p : packageName) {
            if (!Strings.isBlank(p)) {
                packages.add(p);
            }
        }
        return this;
    }

    /**
     * 设置扫描的资源
     *
     * @param resource resource
     * @return this
     */
    public PackageScanner addResource(URL... resource) {
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
    public PackageScanner with(Class<?> resourceClass) {
        Valid.notNull(resourceClass, "resourceClass is null");
        URL r1 = resourceClass.getClassLoader().getResource(Strings.EMPTY);
        URL r2 = resourceClass.getProtectionDomain().getCodeSource().getLocation();
        if (r1 != null) {
            resources.add(r1);
        }
        if (r2 != null) {
            resources.add(r2);
        }
        return this;
    }

    /**
     * 扫描所有包
     *
     * @return this
     */
    public PackageScanner all() {
        this.scanAll = true;
        return this;
    }

    /**
     * 设置类加载器
     *
     * @param classLoader classLoader
     * @return this
     */
    public PackageScanner classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    /**
     * 开始扫描
     *
     * @return this
     */
    public PackageScanner scan() {
        if (resources.isEmpty()) {
            throw Exceptions.init("not set scan resources");
        }
        for (URL resource : resources) {
            String protocol = resource.getProtocol();
            Set<String> classNameSet = new LinkedHashSet<>();
            if (scanAll) {
                if (Const.PROTOCOL_FILE.equals(protocol)) {
                    this.scanFile(resource, Strings.EMPTY, classNameSet);
                } else if (Const.PROTOCOL_JAR.equals(protocol)) {
                    this.scanJar(resource, Strings.EMPTY, classNameSet);
                }
            } else {
                for (String p : packages) {
                    if (Const.PROTOCOL_FILE.equals(protocol)) {
                        this.scanFile(resource, p, classNameSet);
                    } else if (Const.PROTOCOL_JAR.equals(protocol)) {
                        this.scanJar(resource, p, classNameSet);
                    }
                }
            }
            this.loadClasses(classNameSet);
        }
        return this;
    }

    // -------------------- SCAN --------------------

    /**
     * 扫描 file://*
     *
     * @param resource     资源
     * @param packageName  包名
     * @param classNameSet 包名容器
     */
    private void scanFile(URL resource, String packageName, Set<String> classNameSet) {
        boolean all = packageName.endsWith(".*");
        if (all) {
            packageName = packageName.substring(0, packageName.length() - 2);
        }
        String packagePath = resource.getPath() + "/" + packageName.replaceAll("\\.", "/");
        this.addFileClass(Files1.getPath(Urls.decode(packagePath)), packageName, all || scanAll, classNameSet);
    }

    /**
     * 扫描 jar://*
     *
     * @param resource     资源
     * @param packageName  包名
     * @param classNameSet 包名容器
     */
    private void scanJar(URL resource, String packageName, Set<String> classNameSet) {
        boolean all = packageName.endsWith(".*");
        if (all) {
            packageName = packageName.substring(0, packageName.length() - 2);
        }
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
                    if (jarEntryName.endsWith(".class") && (scanAll || this.checkJarEntry(packageName, jarEntryName, all))) {
                        classNameSet.add(jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", "."));
                    }
                }
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime("scan jar file error", e);
        }
    }

    /**
     * file://* 添加class
     *
     * @param packagePath 包路径
     * @param basePackage 包名
     * @param all         是否通配本包
     * @param set         classNameSet
     */
    private void addFileClass(String packagePath, String basePackage, boolean all, Set<String> set) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || (all && file.isDirectory()));
        if (Arrays1.isEmpty(files)) {
            return;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (Strings.isNotEmpty(basePackage)) {
                    className = basePackage + "." + className;
                }
                set.add(className);
            } else {
                String subPackagePath = fileName;
                if (Strings.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (Strings.isNotEmpty(basePackage)) {
                    subPackageName = basePackage + "." + subPackageName;
                }
                this.addFileClass(subPackagePath, subPackageName, all, set);
            }
        }
    }

    /**
     * 检查jar内元素是否添加
     *
     * @param packageName 包名
     * @param entryName   类名
     * @param all         是否通配本包
     * @return true添加
     */
    private boolean checkJarEntry(String packageName, String entryName, boolean all) {
        if (Strings.isBlank(packageName)) {
            return true;
        }

        packageName = packageName.replaceAll("\\.", "/");
        if (entryName.startsWith(packageName + "/") && all) {
            return true;
        }
        return entryName.substring(0, entryName.lastIndexOf("/")).equals(packageName);
    }

    /**
     * 加载类 不初始化
     *
     * @param classNames 类名
     */
    private void loadClasses(Set<String> classNames) {
        for (String className : classNames) {
            Class<?> c = null;
            try {
                c = Class.forName(className, false, classLoader);
            } catch (ClassNotFoundException e) {
                // ignore
            }
            if (c != null) {
                classes.add(c);
            }
        }
    }

    // -------------------- GET --------------------

    /**
     * 从扫描到的类中获取给定类的所有实现类
     *
     * @param superClass class
     * @return ImplClasses
     */
    public Set<Class<?>> getImplClass(Class<?> superClass) {
        Valid.notNull(superClass, "super class is null");
        if (superClass.equals(Object.class)) {
            return classes;
        }
        Set<Class<?>> impl = new LinkedHashSet<>();
        for (Class<?> c : classes) {
            if (!c.equals(superClass) && Classes.isImplClass(superClass, c)) {
                impl.add(c);
            }
        }
        return impl;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的类
     *
     * @param annotatedClasses 注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Set<Class<?>> getAnnotatedClass(Class<? extends Annotation>... annotatedClasses) {
        Valid.notEmpty(annotatedClasses, "annotated classes length is 0");
        Set<Class<?>> annotatedClass = new LinkedHashSet<>();
        for (Class<?> c : classes) {
            if (Annotations.present(c, annotatedClasses)) {
                annotatedClass.add(c);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的构造方法
     *
     * @param annotatedClasses 注解class
     * @return AnnotatedConstructor
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Constructor<?>>> getAnnotatedConstructor(Class<? extends Annotation>... annotatedClasses) {
        Valid.notEmpty(annotatedClasses, "annotated classes length is 0");
        Map<Class<?>, Set<Constructor<?>>> annotatedClass = new HashMap<>(Const.CAPACITY_16);
        for (Class<?> c : classes) {
            Set<Constructor<?>> constructors = null;
            for (Constructor<?> constructor : c.getDeclaredConstructors()) {
                if (Annotations.present(constructor, annotatedClasses)) {
                    if (constructors == null) {
                        constructors = new LinkedHashSet<>();
                    }
                    constructors.add(constructor);
                }
            }
            if (constructors != null) {
                annotatedClass.put(c, constructors);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的构造方法
     *
     * @param classAnnotatedClass        类注解class
     * @param constructorAnnotateClasses 构造方法注解class
     * @return AnnotatedConstructor
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Constructor<?>>> getAnnotatedConstructorByAnnotatedClass(Class<? extends Annotation> classAnnotatedClass, Class<? extends Annotation>... constructorAnnotateClasses) {
        Valid.notNull(classAnnotatedClass, "class annotated class is null");
        Valid.notEmpty(constructorAnnotateClasses, "constructor annotate classes length is 0");
        Map<Class<?>, Set<Constructor<?>>> annotatedClass = new HashMap<>(Const.CAPACITY_16);
        for (Class<?> c : classes) {
            boolean hasAnnotated = Annotations.present(c, classAnnotatedClass);
            if (!hasAnnotated) {
                continue;
            }
            Set<Constructor<?>> constructors = null;
            for (Constructor<?> constructor : c.getDeclaredConstructors()) {
                if (Annotations.present(constructor, constructorAnnotateClasses)) {
                    if (constructors == null) {
                        constructors = new LinkedHashSet<>();
                    }
                    constructors.add(constructor);
                }
            }
            if (constructors != null) {
                annotatedClass.put(c, constructors);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的方法
     *
     * @param annotatedClasses 注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Method>> getAnnotatedMethod(Class<? extends Annotation>... annotatedClasses) {
        Valid.notEmpty(annotatedClasses, "annotated classes length is 0");
        Map<Class<?>, Set<Method>> annotatedClass = new HashMap<>(Const.CAPACITY_16);
        for (Class<?> c : classes) {
            Set<Method> methods = null;
            for (Method method : c.getDeclaredMethods()) {
                if (Annotations.present(method, annotatedClasses)) {
                    if (methods == null) {
                        methods = new LinkedHashSet<>();
                    }
                    methods.add(method);
                }
            }
            if (methods != null) {
                annotatedClass.put(c, methods);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的方法
     *
     * @param classAnnotatedClass   类注解class
     * @param methodAnnotateClasses 方法注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Method>> getAnnotatedMethodByAnnotatedClass(Class<? extends Annotation> classAnnotatedClass, Class<? extends Annotation>... methodAnnotateClasses) {
        Valid.notNull(classAnnotatedClass, "class annotated class is null");
        Valid.notEmpty(methodAnnotateClasses, "method annotated classes length is 0");
        Map<Class<?>, Set<Method>> annotatedClass = new HashMap<>(Const.CAPACITY_16);
        for (Class<?> c : classes) {
            boolean hasAnnotated = Annotations.present(c, classAnnotatedClass);
            if (!hasAnnotated) {
                continue;
            }
            Set<Method> methods = null;
            for (Method method : c.getDeclaredMethods()) {
                if (Annotations.present(method, methodAnnotateClasses)) {
                    if (methods == null) {
                        methods = new LinkedHashSet<>();
                    }
                    methods.add(method);
                }
            }
            if (methods != null) {
                annotatedClass.put(c, methods);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的字段
     *
     * @param annotatedClasses 注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Field>> getAnnotatedField(Class<? extends Annotation>... annotatedClasses) {
        Valid.notEmpty(annotatedClasses, "annotated classes length is 0");
        Map<Class<?>, Set<Field>> annotatedClass = new HashMap<>(Const.CAPACITY_16);
        for (Class<?> c : classes) {
            Set<Field> fields = null;
            for (Field field : c.getDeclaredFields()) {
                if (Annotations.present(field, annotatedClasses)) {
                    if (fields == null) {
                        fields = new LinkedHashSet<>();
                    }
                    fields.add(field);
                }
            }
            if (fields != null) {
                annotatedClass.put(c, fields);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的字段
     *
     * @param classAnnotatedClass   类注解class
     * @param fieldAnnotatedClasses 字段注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Field>> getAnnotatedFieldByAnnotatedClass(Class<? extends Annotation> classAnnotatedClass, Class<? extends Annotation>... fieldAnnotatedClasses) {
        Valid.notNull(classAnnotatedClass, "class annotated class is null");
        Valid.notEmpty(fieldAnnotatedClasses, "field annotated classes length is 0");
        Map<Class<?>, Set<Field>> annotatedClass = new HashMap<>(Const.CAPACITY_16);
        for (Class<?> c : classes) {
            boolean hasAnnotated = Annotations.present(c, classAnnotatedClass);
            if (!hasAnnotated) {
                continue;
            }
            Set<Field> fields = null;
            for (Field field : c.getDeclaredFields()) {
                if (Annotations.present(field, fieldAnnotatedClasses)) {
                    if (fields == null) {
                        fields = new LinkedHashSet<>();
                    }
                    fields.add(field);
                }
            }
            if (fields != null) {
                annotatedClass.put(c, fields);
            }
        }
        return annotatedClass;
    }

    /**
     * 获取扫描到的所有类
     *
     * @return class
     */
    public Set<Class<?>> getClasses() {
        return classes;
    }

    public Set<String> getPackages() {
        return packages;
    }

    public Set<URL> getResources() {
        return resources;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

}

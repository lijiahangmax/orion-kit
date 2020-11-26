package com.orion.utils.reflect;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;

import java.io.File;
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
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/23 17:38
 */
@SuppressWarnings("ALL")
public class PackageScanners {

    /**
     * 扫描到的类
     */
    private Set<Class<?>> classes = new HashSet<>();

    /**
     * 是否扫描子包
     */
    private boolean scanChildPackage;

    /**
     * 扫描的包
     */
    private Set<String> packages = new HashSet<>();

    /**
     * target resource url
     */
    private URL resource;

    /**
     * 扫描指定包 如果不填扫描当前项目所有的包
     *
     * @param packageNames 包
     */
    public PackageScanners(String... packageNames) {
        this(false, packageNames);
    }

    /**
     * 扫描指定包 如果不填扫描当前项目所有的包
     *
     * @param scanChildPackage 是否扫描子包
     * @param packageNames     包
     */
    public PackageScanners(boolean scanChildPackage, String... packageNames) {
        URL resource = PackageScanners.class.getClassLoader().getResource("");
        if (resource == null) {
            resource = PackageScanners.class.getResource("");
        }
        this.resource = resource;
        if (packageNames == null || packageNames.length == 0) {
            this.scanChildPackage = true;
            if (resource != null) {
                packages.add("");
            }
        } else {
            this.scanChildPackage = scanChildPackage;
            for (String packageName : packageNames) {
                if (!Strings.isBlank(packageName)) {
                    if (resource != null) {
                        packages.add(packageName);
                    }
                }
            }
        }
        try {
            scanClasses();
        } catch (Exception e) {
            throw Exceptions.parse("Scan Package error: " + Arrays.toString(packageNames), e);
        }
    }

    // --------------- GET ---------------

    /**
     * 从扫描到的类中获取给定类的所有实现类 Object.class 返回空
     *
     * @param superClass class
     * @return ImplClasses
     */
    public Set<Class<?>> getImplClass(Class<?> superClass) {
        Valid.notNull(superClass, "SuperClass is null");
        if (superClass.equals(Object.class)) {
            return new HashSet<>();
        }
        Set<Class<?>> impl = new HashSet<>();
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
     * @param annotateClasses 注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Set<Class<?>> getAnnotatedClass(Class<? extends Annotation>... annotateClasses) {
        Valid.notEmpty(annotateClasses, "AnnotateClasses length is 0");
        Set<Class<?>> annotatedClass = new HashSet<>();
        for (Class<?> c : classes) {
            if (Annotations.present(c, annotateClasses)) {
                annotatedClass.add(c);
            }
        }
        return annotatedClass;
    }

    /**
     * 从扫描到的类中获取所有标注指定注解的构造方法
     *
     * @param annotateClasses 注解class
     * @return AnnotatedConstructor
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Constructor<?>>> getAnnotatedConstructor(Class<? extends Annotation>... annotateClasses) {
        Valid.notEmpty(annotateClasses, "AnnotateClasses length is 0");
        Map<Class<?>, Set<Constructor<?>>> annotatedClass = new HashMap<>(16);
        for (Class<?> c : classes) {
            Set<Constructor<?>> constructors = null;
            for (Constructor<?> constructor : c.getConstructors()) {
                if (Annotations.present(constructor, annotateClasses)) {
                    if (constructors == null) {
                        constructors = new HashSet<>();
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
        Valid.notNull(classAnnotatedClass, "ClassAnnotatedClass is null");
        Valid.notEmpty(constructorAnnotateClasses, "ConstructorAnnotateClasses length is 0");
        Map<Class<?>, Set<Constructor<?>>> annotatedClass = new HashMap<>(16);
        for (Class<?> c : classes) {
            boolean hasAnnotated = Annotations.present(c, classAnnotatedClass);
            if (!hasAnnotated) {
                continue;
            }
            Set<Constructor<?>> constructors = null;
            for (Constructor<?> constructor : c.getConstructors()) {
                if (Annotations.present(constructor, constructorAnnotateClasses)) {
                    if (constructors == null) {
                        constructors = new HashSet<>();
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
     * @param annotateClasses 注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Method>> getAnnotatedMethod(Class<? extends Annotation>... annotateClasses) {
        Valid.notEmpty(annotateClasses, "AnnotateClasses length is 0");
        Map<Class<?>, Set<Method>> annotatedClass = new HashMap<>(16);
        for (Class<?> c : classes) {
            Set<Method> methods = null;
            for (Method method : c.getDeclaredMethods()) {
                if (Annotations.present(method, annotateClasses)) {
                    if (methods == null) {
                        methods = new HashSet<>();
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
        Valid.notNull(classAnnotatedClass, "ClassAnnotatedClass is null");
        Valid.notEmpty(methodAnnotateClasses, "MethodAnnotateClasses length is 0");
        Map<Class<?>, Set<Method>> annotatedClass = new HashMap<>(16);
        for (Class<?> c : classes) {
            boolean hasAnnotated = Annotations.present(c, classAnnotatedClass);
            if (!hasAnnotated) {
                continue;
            }
            Set<Method> methods = null;
            for (Method method : c.getDeclaredMethods()) {
                if (Annotations.present(method, methodAnnotateClasses)) {
                    if (methods == null) {
                        methods = new HashSet<>();
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
     * @param annotateClasses 注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Field>> getAnnotatedField(Class<? extends Annotation>... annotateClasses) {
        Valid.notEmpty(annotateClasses, "AnnotateClasses length is 0");
        Map<Class<?>, Set<Field>> annotatedClass = new HashMap<>(16);
        for (Class<?> c : classes) {
            Set<Field> fields = null;
            for (Field field : c.getDeclaredFields()) {
                if (Annotations.present(field, annotateClasses)) {
                    if (fields == null) {
                        fields = new HashSet<>();
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
     * @param classAnnotatedClass  类注解class
     * @param fieldAnnotateClasses 字段注解class
     * @return AnnotatedClasses
     */
    @SafeVarargs
    public final Map<Class<?>, Set<Field>> getAnnotatedField(Class<? extends Annotation> classAnnotatedClass, Class<? extends Annotation>... fieldAnnotateClasses) {
        Valid.notNull(classAnnotatedClass, "ClassAnnotatedClass is null");
        Valid.notEmpty(fieldAnnotateClasses, "FieldAnnotateClasses length is 0");
        Map<Class<?>, Set<Field>> annotatedClass = new HashMap<>(16);
        for (Class<?> c : classes) {
            boolean hasAnnotated = Annotations.present(c, classAnnotatedClass);
            if (!hasAnnotated) {
                continue;
            }
            Set<Field> fields = null;
            for (Field field : c.getDeclaredFields()) {
                if (Annotations.present(field, fieldAnnotateClasses)) {
                    if (fields == null) {
                        fields = new HashSet<>();
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

    // --------------- SCAN ---------------

    /**
     * 获取某个包下的所有类
     */
    private void scanClasses() throws Exception {
        Set<String> classNameSet = new HashSet<>();
        for (String packageName : packages) {
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                addFileClass(Files1.getPath(Urls.decode(resource.getPath() + "/" + packageName.replaceAll("\\.", "/"))), packageName, classNameSet);
            } else if ("jar".equals(protocol)) {
                JarURLConnection connection = (JarURLConnection) resource.openConnection();
                if (connection != null) {
                    JarFile jarFile = connection.getJarFile();
                    if (jarFile != null) {
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if (jarEntryName.endsWith(".class") && checkJarEntry(packageName, jarEntryName)) {
                                classNameSet.add(jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", "."));
                            }
                        }
                    }
                }
            }
        }
        loadClasses(classNameSet);
    }

    /**
     * 检查jar内元素是否添加
     *
     * @param packageName 包名
     * @param entryName   类名
     * @return true添加
     */
    private boolean checkJarEntry(String packageName, String entryName) {
        if (Strings.isBlank(packageName)) {
            return true;
        }
        String[] packageNameAnalysis = packageName.split("\\.");
        String[] entryAnalysis = entryName.split("/");
        if (entryAnalysis.length <= packageNameAnalysis.length) {
            return false;
        }
        for (int i = 0; i < packageNameAnalysis.length; i++) {
            if (!entryAnalysis[i].equals(packageNameAnalysis[i])) {
                return false;
            }
        }
        if (packageNameAnalysis.length + 1 == entryAnalysis.length) {
            return true;
        }
        return scanChildPackage;
    }

    /**
     * file 添加class
     *
     * @param packagePath 包路径
     * @param packageName 包名
     */
    private void addFileClass(String packagePath, String packageName, Set<String> set) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || (scanChildPackage && file.isDirectory()));
        if (files == null) {
            return;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (Strings.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                set.add(className);
            } else if (scanChildPackage) {
                String subPackagePath = fileName;
                if (Strings.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (Strings.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addFileClass(subPackagePath, subPackageName, set);
            }
        }
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
                c = Class.forName(className, false, Classes.getCurrentClassLoader());
            } catch (ClassNotFoundException e) {
                // ignore
            }
            if (c != null) {
                classes.add(c);
            }
        }
    }

    /**
     * 获取扫描到的所有类
     *
     * @return class
     */
    public Set<Class<?>> getScanClasses() {
        return classes;
    }

    public URL getResource() {
        return resource;
    }

}

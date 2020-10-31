package com.orion.utils.reflect;

import com.orion.utils.Valid;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 反射 泛型工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/31 11:58
 */
public class Generics {

    private Generics() {
    }

    /**
     * 获取父类泛型
     *
     * @param clazz class
     * @return 第一个泛型 默认 Object.class
     */
    public static Class<?> getClassGenericType(Class<?> clazz) {
        return getClassGenericType(clazz, 0);
    }

    /**
     * 获取父类泛型
     *
     * @param clazz class
     * @param index 泛型索引
     * @return 泛型 默认 Object.class
     */
    public static Class<?> getClassGenericType(Class<?> clazz, int index) {
        Valid.notNull(clazz, "target class is null");
        Valid.gte(index, 0, "index must greater than or equal 0");
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] arguments = ((ParameterizedType) genType).getActualTypeArguments();
        int argumentsLength = arguments.length;
        Valid.lt(index, argumentsLength, "type length: {}, index : {}", argumentsLength, index);
        if (!(arguments[index] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) arguments[index];
    }

    /**
     * 获取父类所有泛型
     *
     * @param clazz class
     * @return 泛型  默认 Object.class
     */
    public static Class<?>[] getClassGenericTypes(Class<?> clazz) {
        Valid.notNull(clazz, "target class is null");
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return new Class[0];
        }
        Type[] arguments = ((ParameterizedType) genType).getActualTypeArguments();
        int argumentsLength = arguments.length;
        Class<?>[] classes = new Class[argumentsLength];
        for (int i = 0; i < argumentsLength; i++) {
            if (!(arguments[i] instanceof Class)) {
                classes[i] = Object.class;
            } else {
                classes[i] = (Class<?>) arguments[i];
            }
        }
        return classes;
    }

    /**
     * 获取接口泛型
     *
     * @param clazz class
     * @return 泛型 第1个接口 第1个泛型  默认 Object.class
     */
    public static Class<?> getInterfaceGenericType(Class<?> clazz) {
        return getInterfaceGenericType(clazz, 0, 0);
    }

    /**
     * 获取接口泛型
     *
     * @param clazz          class
     * @param interfaceIndex 接口索引
     * @return 泛型 第1个泛型  默认 Object.class
     */
    public static Class<?> getInterfaceGenericType(Class<?> clazz, int interfaceIndex) {
        return getInterfaceGenericType(clazz, interfaceIndex, 0);
    }

    /**
     * 获取接口泛型
     *
     * @param clazz          class
     * @param interfaceIndex 接口索引
     * @param genericIndex   泛型索引
     * @return 泛型  默认 Object.class
     */
    public static Class<?> getInterfaceGenericType(Class<?> clazz, int interfaceIndex, int genericIndex) {
        Valid.notNull(clazz, "target class is null");
        Valid.gte(interfaceIndex, 0, "class index must greater than or equal 0");
        Valid.gte(genericIndex, 0, "generic class must greater than or equal 0");
        Type[] genTypes = clazz.getGenericInterfaces();
        int genTypesLength = genTypes.length;
        Valid.lt(interfaceIndex, genTypesLength, "interface length: {}, index : {}", interfaceIndex, genTypesLength);
        Type genType = genTypes[interfaceIndex];
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] arguments = ((ParameterizedType) genType).getActualTypeArguments();
        int argumentsLength = arguments.length;
        Valid.lt(genericIndex, argumentsLength, "type length: {}, index : {}", argumentsLength, genericIndex);
        if (!(arguments[genericIndex] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) arguments[genericIndex];
    }

    /**
     * 获取接口泛型
     *
     * @param clazz          class
     * @param interfaceClazz 接口class
     * @return 泛型 第1个 默认 Object.class
     */
    public static Class<?> getInterfaceGenericType(Class<?> clazz, Class<?> interfaceClazz) {
        return getInterfaceGenericType(clazz, interfaceClazz, 0);
    }

    /**
     * 获取接口泛型
     *
     * @param clazz          class
     * @param interfaceClazz 接口class
     * @param genericIndex   泛型索引
     * @return 泛型 默认 Object.class
     */
    public static Class<?> getInterfaceGenericType(Class<?> clazz, Class<?> interfaceClazz, int genericIndex) {
        Valid.notNull(clazz, "target class is null");
        Valid.notNull(interfaceClazz, "interface class is null");
        Valid.gte(genericIndex, 0, "generic class must greater than or equal 0");
        Type[] genTypes = clazz.getGenericInterfaces();
        Type genType = null;
        boolean isNormalClass = false;
        for (Type tmp : genTypes) {
            if (tmp instanceof ParameterizedType) {
                if (interfaceClazz.equals(((ParameterizedType) tmp).getRawType())) {
                    genType = tmp;
                    break;
                }
            } else if (tmp instanceof Class) {
                if (interfaceClazz.getName().equals(tmp.getTypeName())) {
                    isNormalClass = true;
                    break;
                }
            }
        }
        if (isNormalClass) {
            return Object.class;
        }
        Valid.notNull(genType, "interface class not found");
        Type[] arguments = ((ParameterizedType) genType).getActualTypeArguments();
        int argumentsLength = arguments.length;
        Valid.lt(genericIndex, argumentsLength, "type length: {}, index : {}", argumentsLength, genericIndex);
        if (!(arguments[genericIndex] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) arguments[genericIndex];
    }

    /**
     * 获取接口泛型
     *
     * @param clazz          class
     * @param interfaceIndex interfaceIndex
     * @return 泛型 默认 Object.class
     */
    public static Class<?>[] getInterfaceGenericTypes(Class<?> clazz, int interfaceIndex) {
        Valid.notNull(clazz, "target class is null");
        Valid.gte(interfaceIndex, 0, "class index must greater than or equal 0");
        Type[] genTypes = clazz.getGenericInterfaces();
        int genTypesLength = genTypes.length;
        Valid.lt(interfaceIndex, genTypesLength, "interface length: {}, index : {}", interfaceIndex, genTypesLength);
        Type genType = genTypes[interfaceIndex];
        if (!(genType instanceof ParameterizedType)) {
            return new Class<?>[0];
        }
        Type[] arguments = ((ParameterizedType) genType).getActualTypeArguments();
        int argumentsLength = arguments.length;
        Class<?>[] classes = new Class[argumentsLength];
        for (int i = 0; i < argumentsLength; i++) {
            if (!(arguments[i] instanceof Class)) {
                classes[i] = Object.class;
            } else {
                classes[i] = (Class<?>) arguments[i];
            }
        }
        return classes;
    }

    /**
     * 获取接口泛型
     *
     * @param clazz          class
     * @param interfaceClazz interfaceClass
     * @return 泛型 默认 Object.class
     */
    public static Class<?>[] getInterfaceGenericTypes(Class<?> clazz, Class<?> interfaceClazz) {
        Valid.notNull(clazz, "target class is null");
        Valid.notNull(interfaceClazz, "interface class is null");
        Type[] genTypes = clazz.getGenericInterfaces();
        Type genType = null;
        boolean isNormalClass = false;
        for (Type tmp : genTypes) {
            if (tmp instanceof ParameterizedType) {
                if (interfaceClazz.equals(((ParameterizedType) tmp).getRawType())) {
                    genType = tmp;
                    break;
                }
            } else if (tmp instanceof Class) {
                if (interfaceClazz.equals(tmp)) {
                    isNormalClass = true;
                    break;
                }
            }
        }
        if (isNormalClass) {
            return new Class[0];
        }
        Valid.notNull(genType, "interface class not found");
        Type[] arguments = ((ParameterizedType) genType).getActualTypeArguments();
        int argumentsLength = arguments.length;
        Class<?>[] classes = new Class[argumentsLength];
        for (int i = 0; i < argumentsLength; i++) {
            if (!(arguments[i] instanceof Class)) {
                classes[i] = Object.class;
            } else {
                classes[i] = (Class<?>) arguments[i];
            }
        }
        return classes;
    }

    /**
     * 获取接口泛型
     *
     * @param clazz class
     * @return 泛型 默认 Object.class Key: interfaceClass Value: genericClasses
     */
    public static Map<Class<?>, Class<?>[]> getInterfaceGenericTypes(Class<?> clazz) {
        Valid.notNull(clazz, "target class is null");
        Type[] genTypes = clazz.getGenericInterfaces();
        Map<Class<?>, Class<?>[]> map = new LinkedHashMap<>();
        for (Type genType : genTypes) {
            if (genType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genType;
                Type[] arguments = parameterizedType.getActualTypeArguments();
                Class<?>[] classes = new Class[arguments.length];
                int argumentsLength = arguments.length;
                for (int i = 0; i < argumentsLength; i++) {
                    if (!(arguments[i] instanceof Class)) {
                        classes[i] = Object.class;
                    } else {
                        classes[i] = (Class<?>) arguments[i];
                    }
                }
                map.put((Class<?>) parameterizedType.getRawType(), classes);
            } else if (genType instanceof Class) {
                map.put((Class<?>) genType, new Class[0]);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("as");
        System.out.println(getClassGenericType(strings.getClass()));
    }

}

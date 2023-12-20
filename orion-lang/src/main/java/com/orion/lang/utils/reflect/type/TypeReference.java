package com.orion.lang.utils.reflect.type;

import com.orion.lang.utils.reflect.ByteCodes;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类型引用
 * <p>
 * from FastJSON
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/10 13:50
 */
public abstract class TypeReference<T> {

    static ConcurrentMap<Type, Type> classTypeCache = new ConcurrentHashMap<>(16, 0.75F, 1);

    protected final Type type;

    public TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        Type cachedType = classTypeCache.get(type);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(type, type);
            cachedType = classTypeCache.get(type);
        }
        this.type = cachedType;
    }

    public TypeReference(Type... actualTypeArguments) {
        Class<?> thisClass = this.getClass();
        Type superClass = thisClass.getGenericSuperclass();
        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        Type rawType = argType.getRawType();
        Type[] argTypes = argType.getActualTypeArguments();
        int actualIndex = 0;
        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable &&
                    actualIndex < actualTypeArguments.length) {
                argTypes[i] = actualTypeArguments[actualIndex++];
            }
            // fix for openjdk and android env
            if (argTypes[i] instanceof GenericArrayType) {
                argTypes[i] = ByteCodes.checkPrimitiveArray(
                        (GenericArrayType) argTypes[i]);
            }
            // 如果有多层泛型且该泛型已经注明实现的情况下 判断该泛型下一层是否还有泛型
            if (argTypes[i] instanceof ParameterizedType) {
                argTypes[i] = handlerParameterizedType((ParameterizedType) argTypes[i], actualTypeArguments, actualIndex);
            }
        }
        Type key = new ParameterizedTypeImpl(argTypes, thisClass, rawType);
        Type cachedType = classTypeCache.get(key);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(key, key);
            cachedType = classTypeCache.get(key);
        }
        this.type = cachedType;
    }

    /**
     * 处理泛型类型
     *
     * @param type                type
     * @param actualTypeArguments actualTypeArguments
     * @param actualIndex         actualIndex
     * @return index
     */
    private Type handlerParameterizedType(ParameterizedType type, Type[] actualTypeArguments, int actualIndex) {
        Class<?> thisClass = this.getClass();
        Type rawType = type.getRawType();
        Type[] argTypes = type.getActualTypeArguments();
        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable && actualIndex < actualTypeArguments.length) {
                argTypes[i] = actualTypeArguments[actualIndex++];
            }
            if (argTypes[i] instanceof GenericArrayType) {
                argTypes[i] = ByteCodes.checkPrimitiveArray(
                        (GenericArrayType) argTypes[i]);
            }
            if (argTypes[i] instanceof ParameterizedType) {
                return this.handlerParameterizedType((ParameterizedType) argTypes[i], actualTypeArguments, actualIndex);
            }
        }
        return new ParameterizedTypeImpl(argTypes, thisClass, rawType);
    }

    public Type getType() {
        return type;
    }

}

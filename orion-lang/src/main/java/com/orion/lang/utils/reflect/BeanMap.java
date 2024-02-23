package com.orion.lang.utils.reflect;

import com.orion.lang.define.collect.MutableLinkedHashMap;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.VariableStyles;

import java.lang.reflect.Method;
import java.util.List;

/**
 * bean map 根据getter方法获取
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/9 1:57
 */
public class BeanMap extends MutableLinkedHashMap<String, Object> {

    /**
     * 对象
     */
    private final Object o;

    /**
     * 忽略的字段
     */
    private final String[] ignoreFields;

    /**
     * 是否添加空字段
     */
    private final boolean addNull;

    /**
     * keyStyle
     */
    private final VariableStyles variableStyle;

    public BeanMap(Object o, String... ignoreFields) {
        this(o, null, false, ignoreFields);
    }

    public BeanMap(Object o, VariableStyles variableStyle, String... ignoreFields) {
        this(o, variableStyle, false, ignoreFields);
    }

    public BeanMap(Object o, boolean addNull, String... ignoreFields) {
        this(o, null, addNull, ignoreFields);
    }

    public BeanMap(Object o, VariableStyles variableStyle, boolean addNull, String... ignoreFields) {
        Valid.notNull(o, "object is null");
        this.o = o;
        this.variableStyle = variableStyle;
        this.addNull = addNull;
        this.ignoreFields = ignoreFields;
        this.invokeGetter(Methods.getGetterMethodsByCache(o.getClass()));
    }

    public static BeanMap create(Object o, String... ignoreFields) {
        return new BeanMap(o, null, true, ignoreFields);
    }

    public static BeanMap create(Object o, boolean addNull, String... ignoreFields) {
        return new BeanMap(o, null, addNull, ignoreFields);
    }

    public static BeanMap create(Object o, VariableStyles variableStyle, String... ignoreFields) {
        return new BeanMap(o, variableStyle, true, ignoreFields);
    }

    /**
     * 创建  BeanMap
     *
     * @param o             object
     * @param variableStyle key 类型
     * @param addNull       是否添加为null的属性
     * @param ignoreFields  跳过的属性名称
     * @return BeanMap
     */
    public static BeanMap create(Object o, VariableStyles variableStyle, boolean addNull, String... ignoreFields) {
        return new BeanMap(o, variableStyle, addNull, ignoreFields);
    }

    /**
     * 调用 getter
     *
     * @param methods getterMethods
     */
    private void invokeGetter(List<Method> methods) {
        for (Method method : methods) {
            String fieldName = Fields.getFieldNameByMethod(method.getName());
            if (this.isIgnoreField(fieldName)) {
                continue;
            }
            if (variableStyle != null) {
                fieldName = VariableStyles.convert(fieldName, variableStyle);
            }
            Object value = Methods.invokeMethod(this.o, method);
            if (value == null) {
                if (addNull) {
                    put(fieldName, null);
                }
            } else {
                put(fieldName, value);
            }
        }
    }

    /**
     * 是否为忽略的字段
     *
     * @param field field
     * @return true忽略
     */
    private boolean isIgnoreField(String field) {
        if (Arrays1.isEmpty(ignoreFields)) {
            return false;
        }
        for (String ignoreField : ignoreFields) {
            if (field.equals(ignoreField)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) o;
    }

}

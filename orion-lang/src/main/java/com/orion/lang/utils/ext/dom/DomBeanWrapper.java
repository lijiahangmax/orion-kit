package com.orion.lang.utils.ext.dom;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.reflect.Classes;
import com.orion.lang.utils.reflect.Constructors;
import com.orion.lang.utils.reflect.Methods;
import org.dom4j.Document;
import org.dom4j.Element;

import java.lang.reflect.Method;
import java.util.*;

/**
 * xml 转 bean 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/26 18:38
 */
@SuppressWarnings("unchecked")
public class DomBeanWrapper {

    private DomBeanWrapper() {
    }

    public static <T> T toBean(Document document, Class<T> clazz) {
        return toBean(document.getRootElement(), clazz, null);
    }

    public static <T> T toBean(Document document, Class<T> clazz, Map<String, Object> convertMap) {
        return toBean(document.getRootElement(), clazz, convertMap);
    }

    public static <T> T toBean(Element element, Class<T> clazz) {
        return toBean(element, clazz, null);
    }

    /**
     * xml 转 bean
     *
     * @param element       element
     * @param convertMapper 属性转换map key:xml value:bean
     * @param clazz         beanClass
     * @param <T>           beanType
     * @return bean
     */
    public static <T> T toBean(Element element, Class<T> clazz, Map<String, Object> convertMapper) {
        T t = Constructors.newInstance(clazz);
        List<Element> elements = element.elements();
        List<Method> setterMethod = Methods.getSetterMethods(clazz);
        Map<String, Integer> singleMap = new LinkedHashMap<>();
        Map<String, Integer> multiMap = new LinkedHashMap<>();
        for (Element e : elements) {
            List<?> es = e.elements();
            String eName = e.getName();
            if (Lists.isEmpty(es)) {
                singleMap.merge(eName, 1, Integer::sum);
            } else {
                multiMap.merge(eName, 1, Integer::sum);
            }
        }
        for (Map.Entry<String, Integer> singleEntry : singleMap.entrySet()) {
            for (Method method : setterMethod) {
                String elementSet = Methods.getSetterMethodNameByFieldName(singleEntry.getKey());
                if (convertMapper != null) {
                    Object getSetter = convertMapper.get(singleEntry.getKey());
                    if (getSetter instanceof String) {
                        elementSet = Methods.getSetterMethodNameByFieldName(getSetter.toString());
                    }
                }
                if (method.getName().equals(elementSet)) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Class<?> paramsClass = null;
                    if (paramType.equals(Object.class)) {
                        if (singleEntry.getValue() == 1) {
                            paramType = String.class;
                            paramsClass = String.class;
                        } else {
                            paramType = ArrayList.class;
                            paramsClass = List.class;
                        }
                    } else {
                        List<Class<?>> paramInter = Classes.getInterfaces(paramType);
                        for (Class<?> pi : paramInter) {
                            if (Set.class.getName().equals(pi.getName())) {
                                paramsClass = Set.class;
                            } else if (List.class.getName().equals(pi.getName())) {
                                paramsClass = List.class;
                            }
                        }
                        if (paramsClass == null) {
                            paramsClass = paramType;
                        }
                    }
                    Object paramValue = null;
                    if (paramsClass.equals(List.class) || paramsClass.equals(Set.class)) {
                        if (Classes.isInterface(paramType)) {
                            if (paramsClass.equals(List.class)) {
                                paramType = ArrayList.class;
                            } else {
                                paramType = Set.class;
                            }
                        }
                        Collection<Object> c = ((Collection<Object>) Constructors.newInstance(paramType));
                        Collection<Element> list = element.elements(singleEntry.getKey());
                        for (Element e : list) {
                            c.add(e.getStringValue());
                        }
                        paramValue = c;
                    } else if (paramsClass.equals(String.class)) {
                        paramValue = element.element(singleEntry.getKey()).getStringValue();
                    }
                    if (paramValue == null) {
                        try {
                            paramValue = element.element(singleEntry.getKey()).getStringValue();
                        } catch (Exception e) {
                            Exceptions.printStacks(e);
                        }
                    }
                    if (paramValue != null) {
                        Methods.invokeMethodInfer(t, method.getName(), paramValue);
                    }
                }
            }
        }
        for (Map.Entry<String, Integer> multiEntry : multiMap.entrySet()) {
            for (Method method : setterMethod) {
                String elementSet = Methods.getSetterMethodNameByFieldName(multiEntry.getKey());
                if (convertMapper != null) {
                    Object getSetter = convertMapper.get(multiEntry.getKey());
                    if (getSetter instanceof String) {
                        elementSet = Methods.getSetterMethodNameByFieldName(getSetter.toString());
                    }
                }
                if (method.getName().equals(elementSet)) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Class<?> paramsClass = null;
                    if (paramType.equals(Object.class)) {
                        paramType = LinkedHashMap.class;
                        paramsClass = Map.class;
                    } else {
                        List<Class<?>> paramInter = Classes.getInterfaces(paramType);
                        for (Class<?> pi : paramInter) {
                            if (Map.class.getName().equals(pi.getName())) {
                                paramsClass = Map.class;
                            }
                        }
                        if (paramsClass == null) {
                            paramsClass = paramType;
                        }
                    }
                    Object paramValue;
                    if (paramsClass.equals(Map.class)) {
                        paramValue = toMap(element.element(multiEntry.getKey()), paramType);
                    } else {
                        if (convertMapper != null) {
                            Object childConvertMap = convertMapper.get(multiEntry.getKey());
                            if (childConvertMap instanceof Map) {
                                paramValue = toBean(element.element(multiEntry.getKey()), paramType, ((Map<String, Object>) childConvertMap));
                            } else {
                                paramValue = toBean(element.element(multiEntry.getKey()), paramType, null);
                            }
                        } else {
                            paramValue = toBean(element.element(multiEntry.getKey()), paramType, null);
                        }
                    }
                    Methods.invokeMethodInfer(t, method.getName(), paramValue);
                }
            }
        }
        return t;
    }

    public static Map<String, Object> toMap(Document document) {
        return toMap(document.getRootElement(), LinkedHashMap.class);
    }

    public static Map<String, Object> toMap(Element element) {
        return toMap(element, LinkedHashMap.class);
    }

    /**
     * xml 解析为 map 只有标签值, 没有属性值, 不具有跟标签
     *
     * @param element  element
     * @param mapClass mapClass
     * @return map
     */
    public static Map<String, Object> toMap(Element element, Class<?> mapClass) {
        Map<String, Object> map;
        if (mapClass == null || mapClass.equals(Map.class)) {
            map = new LinkedHashMap<>();
        } else {
            map = ((Map<String, Object>) Constructors.newInstance(mapClass));
        }
        Map<String, Integer> countMap = new LinkedHashMap<>();
        List<Element> elements = element.elements();
        for (Element e : elements) {
            countMap.merge(e.getName(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> countEntry : countMap.entrySet()) {
            if (countEntry.getValue() == 1) {
                Element child = element.element(countEntry.getKey());
                if (!Lists.isEmpty(child.elements())) {
                    map.put(countEntry.getKey(), toMap(child));
                } else {
                    map.put(countEntry.getKey(), child.getStringValue());
                }
            } else {
                List<Object> list = new ArrayList<>();
                List<Element> childList = element.elements(countEntry.getKey());
                for (Element child : childList) {
                    if (!Lists.isEmpty(child.elements())) {
                        list.add(toMap(child));
                    } else {
                        list.add(child.getStringValue());
                    }
                }
                map.put(countEntry.getKey(), list);
            }
        }
        return map;
    }

    public static Map<String, DomNode> toDomNode(Document document) {
        return toDomNode(document.getRootElement());
    }

    /**
     * xml 解析为 domNode 有标签值, 有属性值, 不具有跟标签
     *
     * @param element element
     * @return domNode
     */
    public static Map<String, DomNode> toDomNode(Element element) {
        Map<String, DomNode> map = new LinkedHashMap<>();
        Map<String, Integer> countMap = new LinkedHashMap<>();
        List<Element> elements = element.elements();
        for (Element e : elements) {
            countMap.merge(e.getName(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> countEntry : countMap.entrySet()) {
            if (countEntry.getValue() == 1) {
                Element child = element.element(countEntry.getKey());
                if (!Lists.isEmpty(child.elements())) {
                    map.put(countEntry.getKey(), new DomNode(toDomNode(child)).setAttr(DomSupport.getAttributes(child)));
                } else {
                    map.put(countEntry.getKey(), new DomNode(child.getStringValue()).setAttr(DomSupport.getAttributes(child)));
                }
            } else {
                List<DomNode> list = new ArrayList<>();
                List<Element> childList = element.elements(countEntry.getKey());
                for (Element child : childList) {
                    if (!Lists.isEmpty(child.elements())) {
                        list.add(new DomNode(toDomNode(child)).setAttr(DomSupport.getAttributes(child)));
                    } else {
                        list.add(new DomNode(child.getStringValue()).setAttr(DomSupport.getAttributes(child)));
                    }
                }
                map.put(countEntry.getKey(), new DomNode(list));
            }
        }
        return map;
    }

}

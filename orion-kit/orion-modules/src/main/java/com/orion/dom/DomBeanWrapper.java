package com.orion.dom;

import com.orion.utils.Converts;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import com.orion.utils.reflect.Classes;
import com.orion.utils.reflect.Constructors;
import com.orion.utils.reflect.Methods;
import org.dom4j.Document;
import org.dom4j.Element;

import java.lang.reflect.Method;
import java.util.*;

/**
 * XML 转 Bean工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/26 18:38
 */
@SuppressWarnings("unchecked")
class DomBeanWrapper {

    /**
     * XML 转 bean
     *
     * @param document document
     * @param clazz    beanClass
     * @param <T>      beanType
     * @return bean
     */
    static <T> T toBean(Document document, Class<T> clazz) {
        return toBean(document.getRootElement(), null, clazz);
    }

    /**
     * XML 转 bean
     *
     * @param document   document
     * @param convertMap 属性转换map key:xml value:bean
     * @param clazz      beanClass
     * @param <T>        beanType
     * @return bean
     */
    static <T> T toBean(Document document, Map<String, Object> convertMap, Class<T> clazz) {
        return toBean(document.getRootElement(), convertMap, clazz);
    }

    /**
     * XML 转 bean
     *
     * @param element element
     * @param clazz   beanClass
     * @param <T>     beanType
     * @return bean
     */
    static <T> T toBean(Element element, Class<T> clazz) {
        return toBean(element, null, clazz);
    }

    /**
     * XML 转 bean
     *
     * @param element    element
     * @param convertMap 属性转换map key:xml value:bean
     * @param clazz      beanClass
     * @param <T>        beanType
     * @return bean
     */
    static <T> T toBean(Element element, Map<String, Object> convertMap, Class<T> clazz) {
        T t = Constructors.newInstance(clazz);
        List<Element> elements = element.elements();
        List<Method> setterMethod = Methods.getAllSetterMethod(clazz);
        Map<String, Integer> normMap = new LinkedHashMap<>();
        Map<String, Integer> childMap = new LinkedHashMap<>();
        for (Element e : elements) {
            List es = e.elements();
            String eName = e.getName();
            if (Lists.isEmpty(es)) {
                normMap.merge(eName, 1, (a, b) -> a + b);
            } else {
                childMap.merge(eName, 1, (a, b) -> a + b);
            }
        }
        for (Map.Entry<String, Integer> entry : normMap.entrySet()) {
            for (Method method : setterMethod) {
                String elementSet = "set" + Strings.firstUpper(entry.getKey());
                if (convertMap != null) {
                    Object getSetter = convertMap.get(entry.getKey());
                    if (getSetter instanceof String) {
                        String setter = getSetter.toString();
                        elementSet = "set" + Strings.firstUpper(setter);
                    }
                }
                if (method.getName().equals(elementSet)) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Class<?> paramsClass = null;
                    if (paramType.equals(Object.class)) {
                        if (entry.getValue() == 1) {
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
                        Collection c = ((Collection) Constructors.newInstance(paramType));
                        Collection<Element> list = element.elements(entry.getKey());
                        for (Element e : list) {
                            c.add(e.getStringValue());
                        }
                        paramValue = c;
                    } else if (paramsClass.equals(String.class)) {
                        paramValue = element.element(entry.getKey()).getStringValue();
                    }
                    if (paramValue == null) {
                        try {
                            paramValue = Converts.convert(element.element(entry.getKey()).getStringValue(), String.class);
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    if (paramValue != null) {
                        Methods.invokeMethodInfer(t, method.getName(), new Object[]{paramValue});
                    }
                }
            }
        }
        for (Map.Entry<String, Integer> entry : childMap.entrySet()) {
            for (Method method : setterMethod) {
                String elementSet = "set" + Strings.firstUpper(entry.getKey());
                if (convertMap != null) {
                    Object getSetter = convertMap.get(entry.getKey());
                    if (getSetter instanceof String) {
                        String setter = getSetter.toString();
                        elementSet = "set" + Strings.firstUpper(setter);
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
                        paramValue = toMap(paramType, element.element(entry.getKey()));
                    } else {
                        if (convertMap != null) {
                            Object childConvertMap = convertMap.get(entry.getKey());
                            if (childConvertMap instanceof Map) {
                                paramValue = toBean(element.element(entry.getKey()), ((Map) childConvertMap), paramType);
                            } else {
                                paramValue = toBean(element.element(entry.getKey()), null, paramType);
                            }
                        } else {
                            paramValue = toBean(element.element(entry.getKey()), null, paramType);
                        }
                    }
                    if (paramValue != null) {
                        Methods.invokeMethodInfer(t, method.getName(), new Object[]{paramValue});
                    }
                }
            }
        }
        return t;
    }

    /**
     * XML 解析为 Map 只有标签值, 没有属性值, 不具有跟标签
     *
     * @param document document
     * @return Map
     */
    static Map<String, Object> toMap(Document document) {
        return toMap(LinkedHashMap.class, document.getRootElement());
    }

    /**
     * XML 解析为 Map 只有标签值, 没有属性值, 不具有跟标签
     *
     * @param element element
     * @return Map
     */
    static Map<String, Object> toMap(Element element) {
        return toMap(LinkedHashMap.class, element);
    }

    /**
     * XML 解析为 Map 只有标签值, 没有属性值, 不具有跟标签
     *
     * @param element element
     * @return Map
     */
    static Map<String, Object> toMap(Class<?> mapClass, Element element) {
        Map<String, Object> map;
        if (mapClass.equals(Map.class)) {
            map = new LinkedHashMap<>();
        } else {
            map = ((Map<String, Object>) Constructors.newInstance(mapClass));
        }
        Map<String, Integer> countMap = new LinkedHashMap<>();
        List<Element> elements = element.elements();
        for (Element e : elements) {
            countMap.merge(e.getName(), 1, (a, b) -> a + b);
        }
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                Element element1 = element.element(entry.getKey());
                if (!Lists.isEmpty(element1.elements())) {
                    map.put(entry.getKey(), toMap(element1));
                } else {
                    map.put(entry.getKey(), element1.getStringValue());
                }
            } else {
                List<Object> list = new ArrayList<>();
                List<Element> element1 = element.elements(entry.getKey());
                for (Element element2 : element1) {
                    if (!Lists.isEmpty(element2.elements())) {
                        list.add(toMap(element2));
                    } else {
                        list.add(element2.getStringValue());
                    }
                }
                map.put(entry.getKey(), list);
            }
        }
        return map;
    }

    /**
     * XML 解析为 DomNode 有标签值, 有属性值, 不具有跟标签
     *
     * @param document document
     * @return DomNode
     */
    static Map<String, DomNode> toDomNode(Document document) {
        return toDomNode(document.getRootElement());
    }

    /**
     * XML 解析为 DomNode 有标签值, 有属性值, 不具有跟标签
     *
     * @param element element
     * @return DomNode
     */
    static Map<String, DomNode> toDomNode(Element element) {
        Map<String, DomNode> map = new LinkedHashMap<>();
        Map<String, Integer> countMap = new LinkedHashMap<>();
        List<Element> elements = element.elements();
        for (Element e : elements) {
            countMap.merge(e.getName(), 1, (a, b) -> a + b);
        }
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                Element element1 = element.element(entry.getKey());
                if (!Lists.isEmpty(element1.elements())) {
                    map.put(entry.getKey(), new DomNode(toDomNode(element1)).setAttr(DomExt.getAttributes(element1)));
                } else {
                    map.put(entry.getKey(), new DomNode(element1.getStringValue()).setAttr(DomExt.getAttributes(element1)));
                }
            } else {
                List<DomNode> list = new ArrayList<>();
                List<Element> element1 = element.elements(entry.getKey());
                for (Element element2 : element1) {
                    if (!Lists.isEmpty(element2.elements())) {
                        list.add(new DomNode(toDomNode(element2)).setAttr(DomExt.getAttributes(element2)));
                    } else {
                        list.add(new DomNode(element2.getStringValue()).setAttr(DomExt.getAttributes(element2)));
                    }
                }
                map.put(entry.getKey(), new DomNode(list));
            }
        }
        return map;
    }

}

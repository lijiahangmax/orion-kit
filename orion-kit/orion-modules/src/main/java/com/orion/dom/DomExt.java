package com.orion.dom;

import com.orion.utils.ext.StringExt;
import com.orion.utils.Exceptions;
import com.orion.utils.Streams;
import com.orion.utils.collect.Lists;
import com.orion.utils.file.Files1;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultElement;

import java.io.*;
import java.util.*;

/**
 * XML提取器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/23 14:10
 */
@SuppressWarnings("unchecked")
public class DomExt {

    /**
     * XML读取
     */
    private static SAXReader reader = new SAXReader();

    /**
     * document
     */
    private Document document;

    public DomExt(File file) throws IOException {
        this(Files1.openInputStream(file));
    }

    public DomExt(String file) {
        this(DomExt.class.getClassLoader().getResourceAsStream(file));
    }

    public DomExt(InputStream in) {
        try {
            this.document = reader.read(in);
        } catch (Exception e) {
            throw Exceptions.parse(e);
        }
    }

    public DomExt(Document document) {
        this.document = document;
    }

    /**
     * 获取dom流
     *
     * @return dom流
     */
    public DomStream stream() {
        return new DomStream(document.getRootElement());
    }

    /**
     * 获取解析的element
     *
     * @param formula 如: bean[1] > property > list > ref[0]:name=c   [n] 下标  k:v 属性     不需要包含跟元素
     * @return element
     */
    public Element parse(String formula) {
        return new DomParser(document.getRootElement(), formula).getElement();
    }

    /**
     * 获取解析的element的value
     *
     * @param formula 如: bean[1] > property > list > ref[0]:name=c   [n] 下标  k:v 属性     不需要包含跟元素
     * @return element的value
     */
    public String parseValue(String formula) {
        return new DomParser(document.getRootElement(), formula).getElementValue();
    }

    /**
     * 获取 document
     *
     * @return document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * 获取根 element
     *
     * @return element
     */
    public Element getRootElement() {
        return document.getRootElement();
    }

    /**
     * 获取根节点元素
     *
     * @return element
     */
    public Map<String, List<Element>> getRootElements() {
        return getElements(document.getRootElement());
    }

    /**
     * 将xml文本转换为 DomExt
     *
     * @param xml xml
     * @return DomExt
     */
    public static DomExt newDomExt(String xml) {
        return new DomExt(toDocument(xml));
    }

    /**
     * 获取解析的element
     *
     * @param element element
     * @param formula 如: bean[1] > property > list > ref[0]:name=c   [n] 下标  k:v 属性     不需要包含跟元素
     * @return element
     */
    public static Element parse(Element element, String formula) {
        return new DomParser(element, formula).getElement();
    }

    /**
     * 获取解析的element的value
     *
     * @param element element
     * @param formula 如: bean[1] > property > list > ref[0]:name=c   [n] 下标  k:v 属性  不需要包含跟元素
     * @return element的value
     */
    public static String parseValue(Element element, String formula) {
        return new DomParser(element, formula).getElementValue();
    }

    /**
     * 获取节点元素
     *
     * @param element element
     * @return element
     */
    public static Map<String, List<Element>> getElements(Element element) {
        Map<String, List<Element>> map = new LinkedHashMap<>();
        List<Element> elements = element.elements();
        for (Element e : elements) {
            String name = e.getName();
            List<Element> els = map.computeIfAbsent(name, k -> new ArrayList<>());
            els.add(e);
        }
        return map;
    }

    /**
     * 获取属性集
     *
     * @param e element
     * @return values
     */
    public static Map<String, String> getAttributes(Element e) {
        Map<String, String> map = new HashMap<>();
        List<Attribute> attributes = e.attributes();
        for (Attribute attr : attributes) {
            map.put(attr.getName(), attr.getValue());
        }
        return map;
    }

    /**
     * 获取属性集
     *
     * @param e element
     * @return values
     */
    public static Map<String, StringExt> getAttributesExt(Element e) {
        Map<String, StringExt> map = new HashMap<>();
        List<Attribute> attributes = e.attributes();
        for (Attribute attr : attributes) {
            map.put(attr.getName(), new StringExt(attr.getValue()));
        }
        return map;
    }

    /**
     * 获取属性
     *
     * @param e   element
     * @param key key
     * @return value
     */
    public static String getAttribute(Element e, String key) {
        List<Attribute> attributes = e.attributes();
        for (Attribute attr : attributes) {
            if (key.trim().equals(attr.getName().trim())) {
                return attr.getValue();
            }
        }
        return null;
    }

    /**
     * 获取属性
     *
     * @param e   element
     * @param key key
     * @return value
     */
    public static StringExt getAttributeExt(Element e, String key) {
        List<Attribute> attributes = e.attributes();
        for (Attribute attr : attributes) {
            if (key.trim().equals(attr.getName().trim())) {
                return new StringExt(attr.getValue());
            }
        }
        return null;
    }

    /**
     * 格式化xml
     *
     * @param xml xml
     * @return 格式化后的xml
     */
    public static String format(String xml) {
        return format(xml, "    ");
    }

    /**
     * 格式化xml
     *
     * @param xml    xml
     * @param indent 缩进
     * @return 格式化后的xml
     */
    public static String format(String xml, String indent) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            String r = null;
            XMLWriter writer = null;
            if (document != null) {
                try {
                    StringWriter stringWriter = new StringWriter();
                    OutputFormat format = new OutputFormat(indent, true);
                    writer = new XMLWriter(stringWriter, format);
                    writer.write(document);
                    writer.flush();
                    r = stringWriter.getBuffer().toString();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            return r;
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * XML 转 bean
     *
     * @param document document
     * @param clazz    beanClass
     * @param <T>      beanType
     * @return bean
     */
    public static <T> T toBean(Document document, Class<T> clazz) {
        return DomBeanWrapper.toBean(document.getRootElement(), null, clazz);
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
    public static <T> T toBean(Document document, Map<String, Object> convertMap, Class<T> clazz) {
        return DomBeanWrapper.toBean(document.getRootElement(), convertMap, clazz);
    }

    /**
     * XML 转 bean
     *
     * @param element element
     * @param clazz   beanClass
     * @param <T>     beanType
     * @return bean
     */
    public static <T> T toBean(Element element, Class<T> clazz) {
        return DomBeanWrapper.toBean(element, null, clazz);
    }

    /**
     * XML 转 bean
     *
     * @param element    document
     * @param convertMap 属性转换map key:xml value:bean
     * @param clazz      beanClass
     * @param <T>        beanType
     * @return bean
     */
    public static <T> T toBean(Element element, Map<String, Object> convertMap, Class<T> clazz) {
        return DomBeanWrapper.toBean(element, convertMap, clazz);
    }

    /**
     * XML 解析为 Map 只有属性值, 没有属性
     *
     * @param document document
     * @return Map
     */
    public static Map<String, Object> toMap(Document document) {
        return DomBeanWrapper.toMap(document.getRootElement());
    }

    /**
     * XML 解析为 Map 只有属性值, 没有属性
     *
     * @param element element
     * @return Map
     */
    public static Map<String, Object> toMap(Element element) {
        return DomBeanWrapper.toMap(element);
    }

    /**
     * XML 解析为 DomNode  有属性值, 有属性
     *
     * @param document document
     * @return DomNode
     */
    public static Map<String, DomNode> toDomNode(Document document) {
        return DomBeanWrapper.toDomNode(document.getRootElement());
    }

    /**
     * XML 解析为 DomNode 有属性值, 有属性
     *
     * @param element element
     * @return DomNode
     */
    public static Map<String, DomNode> toDomNode(Element element) {
        return DomBeanWrapper.toDomNode(element);
    }

    /**
     * xml转document
     *
     * @param xml xml
     * @return document
     */
    public static Document toDocument(String xml) {
        try {
            return reader.read(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception e) {
            throw Exceptions.argument("XML Parse To Document Error " + e.getMessage());
        }
    }

    /**
     * 将Map转为 Element
     *
     * @param rootName 根节点名称
     * @param childMap Map <String, String>
     *                 Map <String, Map<String,Object>>
     *                 Map <String, List<Map>>
     *                 Map <String, List<String>>
     * @return Element
     */
    public static Element toElement(String rootName, Map<String, ?> childMap) {
        return toElement(rootName, childMap, true);
    }

    /**
     * 将Map转为 Element
     *
     * @param rootName 根节点名称
     * @param childMap Map <String, String>
     *                 Map <String, Map<String,Object>>
     *                 Map <String, List<Map>>
     *                 Map <String, List<String>>
     * @param cdata    string是否使用CDATA修饰
     * @return Element
     */
    public static Element toElement(String rootName, Map<String, ?> childMap, boolean cdata) {
        DefaultElement root = new DefaultElement(rootName);
        toElement(root, childMap, cdata);
        return root;
    }

    /**
     * 将Map转为 Element
     *
     * @param element  根节点
     * @param childMap Map <String, String>
     *                 Map <String, Map<String,Object>>
     *                 Map <String, List<Map>>
     *                 Map <String, List<String>>
     * @param cdata    string是否使用CDATA修饰
     */
    private static void toElement(Element element, Map<String, ?> childMap, boolean cdata) {
        for (Map.Entry<String, ?> entry : childMap.entrySet()) {
            Object value = entry.getValue();
            DefaultElement child = new DefaultElement(entry.getKey());
            if (value != null) {
                if (value instanceof Map) {
                    toElement(child, (Map) value, cdata);
                    element.add(child);
                } else if (value instanceof List) {
                    List<?> children = (List<?>) value;
                    if (Lists.isEmpty(children)) {
                        continue;
                    }
                    int i = 0;
                    Object typeCheck = children.get(0);
                    while (typeCheck == null && ++i < children.size()) {
                        typeCheck = children.get(i);
                    }
                    if (typeCheck == null) {
                        continue;
                    }
                    if (typeCheck instanceof Map) {
                        for (Object o : children) {
                            toElement(child, (Map<String, ?>) o, cdata);
                            element.add(child);
                            child = new DefaultElement(entry.getKey());
                        }
                    } else {
                        for (Object o : children) {
                            if (cdata) {
                                child.add(new DefaultCDATA(o.toString()));
                            } else {
                                child.setText(o.toString());
                            }
                            element.add(child);
                            child = new DefaultElement(entry.getKey());
                        }
                    }
                } else {
                    if (cdata) {
                        child.add(new DefaultCDATA(value.toString()));
                    } else {
                        child.setText(value.toString());
                    }
                    element.add(child);
                }
            }
        }
    }

    /**
     * 将xml保存到文件
     *
     * @param xml  xml
     * @param file 本地文件
     */
    public static void write(String xml, File file) throws IOException {
        Files1.touch(file);
        FileOutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
            out.write(format(xml).getBytes());
        } finally {
            Streams.closeQuietly(out);
        }
    }

    /**
     * 将xml保存到文件
     *
     * @param document document
     * @param file     本地文件
     */
    public static void write(Document document, File file) throws IOException {
        Files1.touch(file);
        FileOutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
            out.write(format(document.asXML()).getBytes());
        } finally {
            Streams.closeQuietly(out);
        }
    }

    /**
     * 将xml保存到文件
     *
     * @param element element
     * @param file    本地文件
     */
    public static void write(Element element, File file) throws IOException {
        Files1.touch(file);
        FileOutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
            out.write(format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + element.asXML()).getBytes());
        } finally {
            Streams.closeQuietly(out);
        }
    }

    @Override
    public String toString() {
        return document.asXML();
    }

}

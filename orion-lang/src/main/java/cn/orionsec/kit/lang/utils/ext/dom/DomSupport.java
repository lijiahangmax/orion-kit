/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.ext.dom;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.io.Files1;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;

import java.io.*;
import java.util.*;

/**
 * XML 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/2 12:34
 */
@SuppressWarnings("unchecked")
public class DomSupport {

    /**
     * 转义对照
     */
    private static final Map<String, String> ESCAPE_MAP = new HashMap<>();

    /**
     * 反转义对照
     */
    private static final Map<String, String> UNESCAPE_MAP = new HashMap<>();

    private static final String DEFAULT_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private DomSupport() {
    }

    static {
        ESCAPE_MAP.put("&", "&amp;");
        ESCAPE_MAP.put(">", "&gt;");
        ESCAPE_MAP.put("<", "&lt;");
        ESCAPE_MAP.put("\"", "&quot;");
        ESCAPE_MAP.put("'", "&apos;");

        UNESCAPE_MAP.put("&amp;", "&");
        UNESCAPE_MAP.put("&gt;", ">");
        UNESCAPE_MAP.put("&lt;", "<");
        UNESCAPE_MAP.put("&quot;", "\"");
        UNESCAPE_MAP.put("&apos;", "'");
    }

    /**
     * 是否需要 xml 转义
     *
     * @param s s
     * @return true 需要转义
     */
    public static boolean isEscape(String s) {
        return s.chars().anyMatch(i -> Objects.nonNull(ESCAPE_MAP.get((char) i + Strings.EMPTY)));
    }

    /**
     * xml 转义
     *
     * @param s s
     * @return 转义字符
     */
    public static String escape(String s) {
        for (Map.Entry<String, String> e : ESCAPE_MAP.entrySet()) {
            s = s.replaceAll(e.getKey(), e.getValue());
        }
        return s;
    }

    /**
     * xml 反转义
     *
     * @param s s
     * @return 原始字符
     */
    public static String unescape(String s) {
        for (Map.Entry<String, String> e : UNESCAPE_MAP.entrySet()) {
            s = s.replaceAll(e.getKey(), e.getValue());
        }
        return s;
    }

    /**
     * 获取解析的element
     *
     * @param element element
     * @param formula 如: bean[1] > property > list > ref[0]:name=c   [n] 下标  k:v 属性  不需要包含跟元素
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
        Map<String, String> map = new LinkedHashMap<>();
        List<Attribute> attributes = e.attributes();
        for (Attribute attr : attributes) {
            map.put(attr.getName(), attr.getValue());
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
     * xml 转 document
     *
     * @param xml xml
     * @return document
     */
    public static Document toDocument(String xml) {
        try {
            return new SAXReader().read(new StringReader(xml));
        } catch (Exception e) {
            throw Exceptions.argument("xml parse to document error " + e.getMessage(), e);
        }
    }

    public static Document toDocument(String rootName, Map<String, ?> childMap) {
        DefaultDocument d = new DefaultDocument();
        d.setRootElement(toElement(rootName, childMap, true));
        return d;
    }

    /**
     * 将 map 转为 document
     *
     * @param rootName 根节点名称
     * @param childMap Map <String, String>
     *                 Map <String, Map<String,Object>>
     *                 Map <String, List<Map>>
     *                 Map <String, List<String>>
     * @param cdata    string是否使用CDATA修饰
     * @return Document
     */
    public static Document toDocument(String rootName, Map<String, ?> childMap, boolean cdata) {
        DefaultDocument d = new DefaultDocument();
        DefaultElement root = new DefaultElement(rootName);
        toElement(root, childMap, cdata);
        d.setRootElement(root);
        return d;
    }

    /**
     * 将 map 转为 element
     *
     * @param xml xml
     * @return element
     */
    public static Element toElement(String xml) {
        return toDocument(xml).getRootElement();
    }

    public static Element toElement(String rootName, Map<String, ?> childMap) {
        return toElement(rootName, childMap, true);
    }

    /**
     * 将 map 转为 element
     *
     * @param rootName 根节点名称
     * @param childMap Map <String, String>
     *                 Map <String, Map<String,Object>>
     *                 Map <String, List<Map>>
     *                 Map <String, List<String>>
     * @param cdata    string是否使用CDATA修饰
     * @return element
     */
    public static Element toElement(String rootName, Map<String, ?> childMap, boolean cdata) {
        DefaultElement root = new DefaultElement(rootName);
        toElement(root, childMap, cdata);
        return root;
    }

    /**
     * 将 map 转为 element
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
            if (value != null) {
                DefaultElement child = new DefaultElement(entry.getKey());
                if (value instanceof Map) {
                    toElement(child, (Map<String, ?>) value, cdata);
                    element.add(child);
                } else if (value instanceof List) {
                    toElementList(element, entry.getKey(), (List<?>) value, cdata);
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

    private static void toElementList(Element element, String key, List<?> list, boolean cdata) {
        if (Lists.isEmpty(list)) {
            return;
        }
        Element child = new DefaultElement(key);
        int i = 0;
        for (Object obj : list) {
            if (i++ > 0) {
                child = new DefaultElement(key);
            }
            if (obj == null) {
                continue;
            }
            if (obj instanceof Map) {
                toElement(child, (Map<String, ?>) obj, cdata);
                element.add(child);
            } else if (obj instanceof List) {
                Map<String, Object> listMap = new HashMap<>();
                listMap.put(key, obj);
                toElement(child, listMap, cdata);
                element.add(child);
            } else {
                if (cdata) {
                    child.add(new DefaultCDATA(obj.toString()));
                } else {
                    child.setText(obj.toString());
                }
                element.add(child);
            }
        }
    }

    public static String formatIndent(String xml) {
        return formatIndent(xml, Const.SPACE_4, true);
    }

    public static String formatIndent(String xml, boolean newLine) {
        return formatIndent(xml, Const.SPACE_4, newLine);
    }

    public static String formatIndent(String xml, String indent) {
        return formatIndent(xml, indent, true);
    }

    /**
     * 格式化 xml 缩进
     *
     * @param xml     xml
     * @param indent  缩进
     * @param newLine 缩进后是否另起一行
     * @return 格式化后的 xml
     */
    public static String formatIndent(String xml, String indent, boolean newLine) {
        return format(xml, new OutputFormat(indent, newLine));
    }

    public static String formatExpandTag(String xml) {
        return formatExpandTag(toDocument(xml));
    }

    /**
     * 格式化 xml 禁止空标签自结束
     *
     * @param document document
     * @return 格式化后的 xml
     */
    public static String formatExpandTag(Document document) {
        OutputFormat format = new OutputFormat();
        format.setExpandEmptyElements(true);
        return format(document, format);
    }

    public static String format(String xml) {
        return format(toDocument(xml));
    }

    /**
     * 格式化 xml 默认格式
     *
     * @param document document
     * @return 格式化后的 xml
     */
    public static String format(Document document) {
        OutputFormat format = new OutputFormat(Const.SPACE_4, true);
        format.setExpandEmptyElements(true);
        return format(document, format);
    }

    public static String format(String xml, OutputFormat format) {
        return format(toDocument(xml), format);
    }

    /**
     * 格式化 xml
     *
     * @param document document
     * @param format   format
     * @return 格式化后的 xml
     */
    public static String format(Document document, OutputFormat format) {
        try {
            XMLWriter writer = null;
            try (StringWriter stringWriter = new StringWriter()) {
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                return stringWriter.getBuffer().toString();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 去除注释
     *
     * @param document document
     */
    public static void cleanComment(Document document) {
        cleanComment(document.getRootElement());
    }

    /**
     * 去除注释
     *
     * @param element element
     */
    public static void cleanComment(Element element) {
        Iterator<Node> nodes = element.nodeIterator();
        List<Node> rmNodes = new ArrayList<>();
        while (nodes.hasNext()) {
            Node subNode = nodes.next();
            if (subNode.getNodeType() == Node.COMMENT_NODE) {
                rmNodes.add(subNode);
                rmNodes.add(nodes.next());
            }
        }
        for (Node node : rmNodes) {
            element.remove(node);
        }
        Iterator<Element> eleIt = element.elementIterator();
        while (eleIt.hasNext()) {
            cleanComment(eleIt.next());
        }
    }

    public static <T> T toBean(String xml, Class<T> clazz) {
        return toBean(xml, clazz, null);
    }

    public static <T> T toBean(String xml, String formula, Class<T> clazz) {
        return toBean(xml, formula, clazz, null);
    }

    public static <T> T toBean(String xml, Class<T> clazz, Map<String, Object> convertMapper) {
        return DomBeanWrapper.toBean(toElement(xml), clazz, convertMapper);
    }

    public static <T> T toBean(String xml, String formula, Class<T> clazz, Map<String, Object> convertMapper) {
        Element match = new DomParser(toElement(xml), formula).getElement();
        return DomBeanWrapper.toBean(match, clazz, convertMapper);
    }

    public static <T> T toBean(Document document, Class<T> clazz) {
        return toBean(document, clazz, null);
    }

    public static <T> T toBean(Document document, String formula, Class<T> clazz) {
        return toBean(document, formula, clazz, null);
    }

    public static <T> T toBean(Document document, Class<T> clazz, Map<String, Object> convertMapper) {
        return DomBeanWrapper.toBean(document.getRootElement(), clazz, convertMapper);
    }

    public static <T> T toBean(Document document, String formula, Class<T> clazz, Map<String, Object> convertMapper) {
        Element root = document.getRootElement();
        Element match = new DomParser(root, formula).getElement();
        return DomBeanWrapper.toBean(match, clazz, convertMapper);
    }

    public static <T> T toBean(Element element, Class<T> clazz) {
        return toBean(element, clazz, null);
    }

    public static <T> T toBean(Element element, String formula, Class<T> clazz) {
        return toBean(element, formula, clazz, null);
    }

    public static <T> T toBean(Element element, Class<T> clazz, Map<String, Object> convertMapper) {
        return DomBeanWrapper.toBean(element, clazz, convertMapper);
    }

    /**
     * xml 转 bean
     *
     * @param element       document
     * @param formula       如: bean[1] > property > list > ref[0]:name=c
     *                      语法: [n] 下标  k:v 属性
     * @param clazz         beanClass
     * @param convertMapper 属性转换map key:xml value:bean
     * @param <T>           beanType
     * @return bean
     */
    public static <T> T toBean(Element element, String formula, Class<T> clazz, Map<String, Object> convertMapper) {
        Element match = new DomParser(element, formula).getElement();
        return DomBeanWrapper.toBean(match, clazz, convertMapper);
    }

    public static Map<String, Object> toMap(String xml) {
        return toMap(xml, (Class<?>) null);
    }

    public static Map<String, Object> toMap(String xml, String formula) {
        return toMap(xml, formula, null);
    }

    public static Map<String, Object> toMap(String xml, Class<?> mapClass) {
        return DomBeanWrapper.toMap(toElement(xml), mapClass);
    }

    public static Map<String, Object> toMap(String xml, String formula, Class<?> mapClass) {
        Element match = new DomParser(toElement(xml), formula).getElement();
        return DomBeanWrapper.toMap(match, mapClass);
    }

    public static Map<String, Object> toMap(Document document) {
        return toMap(document, (Class<?>) null);
    }

    public static Map<String, Object> toMap(Document document, String formula) {
        return toMap(document, formula, null);
    }

    public static Map<String, Object> toMap(Document document, Class<?> mapClass) {
        return DomBeanWrapper.toMap(document.getRootElement(), mapClass);
    }

    public static Map<String, Object> toMap(Document document, String formula, Class<?> mapClass) {
        Element root = document.getRootElement();
        Element match = new DomParser(root, formula).getElement();
        return DomBeanWrapper.toMap(match, mapClass);
    }

    public static Map<String, Object> toMap(Element element) {
        return toMap(element, (Class<?>) null);
    }

    public static Map<String, Object> toMap(Element element, String formula) {
        return toMap(element, formula, null);
    }

    public static Map<String, Object> toMap(Element element, Class<?> mapClass) {
        return DomBeanWrapper.toMap(element, mapClass);
    }

    /**
     * xml 解析为 map 有标签值 有属性值 不具有根标签
     *
     * @param element element
     * @param formula 如: bean[1] > property > list > ref[0]:name=c
     *                语法: [n] 下标  k:v 属性
     * @return map
     */
    public static Map<String, Object> toMap(Element element, String formula, Class<?> mapClass) {
        Element match = new DomParser(element, formula).getElement();
        return DomBeanWrapper.toMap(match, mapClass);
    }

    public static Map<String, DomNode> toDomNode(String xml) {
        return DomBeanWrapper.toDomNode(toElement(xml));
    }

    public static Map<String, DomNode> toDomNode(String xml, String formula) {
        Element match = new DomParser(toElement(xml), formula).getElement();
        return DomBeanWrapper.toDomNode(match);
    }

    public static Map<String, DomNode> toDomNode(Document document) {
        return DomBeanWrapper.toDomNode(document.getRootElement());
    }

    public static Map<String, DomNode> toDomNode(Document document, String formula) {
        return toDomNode(document.getRootElement(), formula);
    }

    public static Map<String, DomNode> toDomNode(Element element) {
        return DomBeanWrapper.toDomNode(element);
    }

    /**
     * xml 解析为 domNode 有标签值 有属性值 不具有根标签
     *
     * @param element element
     * @param formula 如: bean[1] > property > list > ref[0]:name=c
     *                语法: [n] 下标  k:v 属性
     * @return domNode
     */
    public static Map<String, DomNode> toDomNode(Element element, String formula) {
        Element match = new DomParser(element, formula).getElement();
        return DomBeanWrapper.toDomNode(match);
    }

    public static String asXml(Document document) {
        return DomSupport.format(document);
    }

    /**
     * 转 xml
     *
     * @param element element
     * @return xml
     */
    public static String asXml(Element element) {
        return DomSupport.format(DEFAULT_HEADER + "\n" + element.asXML());
    }

    public static void write(String xml, File file) throws IOException {
        Files1.touch(file);
        try (FileOutputStream out = Files1.openOutputStream(file)) {
            out.write(Strings.bytes(DomSupport.format(xml)));
        }
    }

    public static void write(Document document, File file) throws IOException {
        Files1.touch(file);
        try (FileOutputStream out = Files1.openOutputStream(file)) {
            out.write(Strings.bytes(DomSupport.format(document)));
        }
    }

    /**
     * 将 xml 保存到文件
     *
     * @param element element
     * @param file    本地文件
     */
    public static void write(Element element, File file) throws IOException {
        Files1.touch(file);
        try (FileOutputStream out = Files1.openOutputStream(file)) {
            out.write(Strings.bytes(DomSupport.format(asXml(element))));
        }
    }

}

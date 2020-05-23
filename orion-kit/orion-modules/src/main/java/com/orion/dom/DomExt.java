package com.orion.dom;

import com.orion.utils.Exceptions;
import com.orion.utils.Streams;
import com.orion.utils.collect.Lists;
import com.orion.utils.file.Files1;
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

    // --------------- new ---------------

    /**
     * 将xml文本转换为 DomExt
     *
     * @param xml xml
     * @return DomExt
     */
    public static DomExt newDomExt(String xml) {
        return new DomExt(toDocument(xml));
    }

    // --------------- stream ---------------

    /**
     * 获取dom流
     *
     * @return dom流
     */
    public DomStream stream() {
        return new DomStream(document.getRootElement());
    }

    // --------------- parse ---------------

    /**
     * 获取解析的element
     * 不需要包含跟元素
     *
     * @param formula 如: bean[1] > property > list > ref[0]:name=c
     *                语法: [n] 下标  k:v 属性
     * @return element
     */
    public Element parse(String formula) {
        return new DomParser(document.getRootElement(), formula).getElement();
    }

    /**
     * 获取解析的element的value
     * 不需要包含跟元素
     *
     * @param formula 如: bean[1] > property > list > ref[0]:name=c
     *                语法: [n] 下标  k:v 属性
     * @return element的value
     */
    public String parseValue(String formula) {
        return new DomParser(document.getRootElement(), formula).getElementValue();
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

    // --------------- format ---------------

    /**
     * 格式化xml
     *
     * @param xml xml
     * @return 格式化后的xml
     */
    public static String format(String xml) {
        return format(xml, "    ", false);
    }

    /**
     * 格式化xml
     *
     * @param xml     xml
     * @param newLine 缩进后是否另起一行
     * @return 格式化后的xml
     */
    public static String format(String xml, boolean newLine) {
        return format(xml, "    ", newLine);
    }

    /**
     * 格式化xml
     *
     * @param xml    xml
     * @param indent 缩进
     * @return 格式化后的xml
     */
    public static String format(String xml, String indent) {
        return format(xml, indent, false);
    }

    /**
     * 格式化xml
     *
     * @param xml     xml
     * @param indent  缩进
     * @param newLine 缩进后是否另起一行
     * @return 格式化后的xml
     */
    public static String format(String xml, String indent, boolean newLine) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            String r = null;
            XMLWriter writer = null;
            if (document != null) {
                try {
                    StringWriter stringWriter = new StringWriter();
                    OutputFormat format = new OutputFormat(indent, newLine);
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

    // --------------- document ---------------

    /**
     * 获取 document
     *
     * @return document
     */
    public Document getDocument() {
        return document;
    }

    // --------------- element ---------------

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

    // --------------- attribute ---------------

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

    // --------------- toDocument ---------------

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
     * 将Map转为 Document
     *
     * @param rootName 根节点名称
     * @param childMap Map <String, String>
     *                 Map <String, Map<String,Object>>
     *                 Map <String, List<Map>>
     *                 Map <String, List<String>>
     * @return Document
     */
    public static Document toDocument(String rootName, Map<String, ?> childMap) {
        DefaultDocument d = new DefaultDocument();
        d.setRootElement(toElement(rootName, childMap, true));
        return d;
    }

    /**
     * 将Map转为 Document
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

    // --------------- toElement ---------------

    /**
     * 将Map转为 Element
     *
     * @param xml xml
     * @return Element
     */
    public static Element toElement(String xml) {
        return toDocument(xml).getRootElement();
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
            if (value != null) {
                DefaultElement child = new DefaultElement(entry.getKey());
                if (value instanceof Map) {
                    toElement(child, (Map) value, cdata);
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

    // --------------- toMap ---------------

    /**
     * XML 解析为 Map 只有标签值, 没有属性值, 不具有根标签
     *
     * @return Map
     */
    public Map<String, Object> toMap() {
        return DomBeanWrapper.toMap(this.document.getRootElement());
    }

    /**
     * XML 解析为 Map 只有标签值, 没有属性值, 不具有根标签
     *
     * @param document document
     * @return Map
     */
    public static Map<String, Object> toMap(Document document) {
        return DomBeanWrapper.toMap(document.getRootElement());
    }

    /**
     * XML 解析为 Map 只有标签值, 没有属性值, 不具有根标签
     *
     * @param element element
     * @return Map
     */
    public static Map<String, Object> toMap(Element element) {
        return DomBeanWrapper.toMap(element);
    }

    // --------------- toDomNode ---------------

    /**
     * XML 解析为 DomNode 有标签值, 有属性值, 不具有根标签
     *
     * @return DomNode
     */
    public Map<String, DomNode> toDomNode() {
        return DomBeanWrapper.toDomNode(this.document.getRootElement());
    }

    /**
     * XML 解析为 DomNode  有标签值, 有属性值, 不具有根标签
     *
     * @param document document
     * @return DomNode
     */
    public static Map<String, DomNode> toDomNode(Document document) {
        return DomBeanWrapper.toDomNode(document.getRootElement());
    }

    /**
     * XML 解析为 DomNode 有标签值, 有属性值, 不具有根标签
     *
     * @param element element
     * @return DomNode
     */
    public static Map<String, DomNode> toDomNode(Element element) {
        return DomBeanWrapper.toDomNode(element);
    }

    // --------------- toBean ---------------

    /**
     * XML 转 bean
     *
     * @param clazz beanClass
     * @param <T>   beanType
     * @return bean
     */
    public <T> T toBean(Class<T> clazz) {
        return DomBeanWrapper.toBean(document.getRootElement(), null, clazz);
    }

    /**
     * XML 转 bean
     *
     * @param convertMap 属性转换map key:xml value:bean
     * @param clazz      beanClass
     * @param <T>        beanType
     * @return bean
     */
    public <T> T toBean(Map<String, Object> convertMap, Class<T> clazz) {
        return DomBeanWrapper.toBean(document.getRootElement(), convertMap, clazz);
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

    // --------------- write ---------------

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

    // --------------- cleanComment ---------------

    /**
     * 去除注释
     */
    public DomExt cleanComment() {
        cleanComment(this.document.getRootElement());
        return this;
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

    // --------------- result ---------------

    /**
     * 获取当前Document的XML
     *
     * @return xml
     */
    public String toXML() {
        return document.asXML();
    }

    /**
     * 获取当前Document的XML
     *
     * @return 格式化后的xml
     */
    public String formatXML() {
        return format(document.asXML(), "    ", false);
    }

    /**
     * 获取当前Document的XML
     *
     * @param indent 缩进
     * @return 格式化后的xml
     */
    public String formatXML(String indent) {
        return format(document.asXML(), indent, false);
    }

    /**
     * 获取当前Document的XML
     *
     * @param indent  缩进
     * @param newLine 缩进后是否另起一行
     * @return 格式化后的xml
     */
    public String formatXML(String indent, boolean newLine) {
        return format(document.asXML(), indent, newLine);
    }

    @Override
    public String toString() {
        return document.asXML();
    }

}

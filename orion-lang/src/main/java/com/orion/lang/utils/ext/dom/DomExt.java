package com.orion.lang.utils.ext.dom;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Files1;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * XML提取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/23 14:10
 */
@SuppressWarnings("unchecked")
public class DomExt {

    /**
     * XML读取
     */
    private SAXReader reader = new SAXReader();

    /**
     * document
     */
    private Document document;

    public DomExt(File file) {
        try {
            this.document = reader.read(Files1.openInputStreamSafe(file));
        } catch (Exception e) {
            throw Exceptions.parse(e);
        }
    }

    public DomExt(String xml) {
        this(DomSupport.toDocument(xml));
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

    public static DomExt of(File file) {
        return new DomExt(file);
    }

    public static DomExt of(String xml) {
        return new DomExt(xml);
    }

    public static DomExt of(InputStream in) {
        return new DomExt(in);
    }

    public static DomExt of(Document document) {
        return new DomExt(document);
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
        return DomSupport.getElements(document.getRootElement());
    }

    public Map<String, Object> toMap() {
        return this.toMap((Class<?>) null);
    }

    public Map<String, Object> toMap(Class<?> mapClass) {
        return DomBeanWrapper.toMap(document.getRootElement(), mapClass);
    }

    public Map<String, Object> toMap(String formula) {
        return this.toMap(formula, null);
    }

    /**
     * xml 解析为 map 只有标签值, 没有属性值, 不具有根标签
     *
     * @param formula  如: bean[1] > property > list > ref[0]:name=c
     *                 语法: [n] 下标  k:v 属性
     * @param mapClass mapClass
     * @return map
     */
    public Map<String, Object> toMap(String formula, Class<?> mapClass) {
        Element element = new DomParser(document.getRootElement(), formula).getElement();
        return DomBeanWrapper.toMap(element, mapClass);
    }

    public <T> T toBean(Class<T> clazz) {
        return this.toBean(clazz, null);
    }

    public <T> T toBean(Class<T> clazz, Map<String, Object> convertMapper) {
        return DomBeanWrapper.toBean(document.getRootElement(), clazz, convertMapper);
    }

    public <T> T toBean(String formula, Class<T> clazz) {
        return this.toBean(formula, clazz, null);
    }

    /**
     * xml 转 bean
     *
     * @param formula       如: bean[1] > property > list > ref[0]:name=c
     *                      语法: [n] 下标  k:v 属性
     * @param clazz         beanClass
     * @param convertMapper 属性转换map key:xml value:bean
     * @param <T>           beanType
     * @return bean
     */
    public <T> T toBean(String formula, Class<T> clazz, Map<String, Object> convertMapper) {
        Element element = new DomParser(document.getRootElement(), formula).getElement();
        return DomBeanWrapper.toBean(element, clazz, convertMapper);
    }

    public Map<String, DomNode> toDomNode() {
        return DomBeanWrapper.toDomNode(document.getRootElement());
    }

    /**
     * xml 解析为 domNode 有标签值, 有属性值, 不具有根标签
     *
     * @param formula 如: bean[1] > property > list > ref[0]:name=c
     *                语法: [n] 下标  k:v 属性
     * @return DomNode
     */
    public Map<String, DomNode> toDomNode(String formula) {
        Element element = new DomParser(document.getRootElement(), formula).getElement();
        return DomBeanWrapper.toDomNode(element);
    }

    /**
     * 去除注释
     */
    public DomExt cleanComment() {
        DomSupport.cleanComment(document.getRootElement());
        return this;
    }

    /**
     * 获取当前 document 的 xml
     *
     * @return xml
     */
    public String toXml() {
        return document.asXML();
    }

    public String format() {
        return DomSupport.format(document);
    }

    /**
     * 获取当前 document 的 xml
     *
     * @param format format
     * @return 格式化后的xml
     */
    public String format(OutputFormat format) {
        return DomSupport.format(document, format);
    }

    @Override
    public String toString() {
        return document.asXML();
    }

}

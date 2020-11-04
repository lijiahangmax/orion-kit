package com.orion.dom;

import com.orion.able.BuilderAble;
import com.orion.utils.Exceptions;
import com.orion.utils.collect.Lists;
import com.orion.utils.collect.Maps;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML构建器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/24 12:58
 */
public class DomBuilder implements BuilderAble<DomBuilder> {

    /**
     * 构建xml信息
     */
    private DomTag domTag;

    /**
     * document
     */
    private Document document;

    /**
     * 编码格式
     */
    private String charset = "UTF-8";

    public DomBuilder() {
    }

    /**
     * 获取根节点信息
     *
     * @return 节点
     */
    public DomTag getDomTag() {
        return domTag;
    }

    /**
     * 设置节点信息
     *
     * @param domTag domTag
     * @return this
     */
    public DomBuilder setDomTag(DomTag domTag) {
        this.domTag = domTag;
        return this;
    }

    /**
     * 获取根节点信息
     *
     * @param name 标签
     * @return 节点
     */
    public DomTag getDomRootTag(String name) {
        if (domTag == null) {
            domTag = new DomTag(name);
        }
        return domTag;
    }

    /**
     * 设置xml编码格式
     *
     * @param charset 编码格式
     * @return this
     */
    public DomBuilder charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 构建
     *
     * @return this
     */
    @Override
    public DomBuilder build() {
        if (domTag == null) {
            throw Exceptions.argument("DomTag is null");
        }
        this.document = new DefaultDocument();
        this.document.setXMLEncoding(charset);
        Element rootElement = new DefaultElement(domTag.getName());
        this.document.setRootElement(rootElement);
        rootElement.setAttributes(getAttribute(domTag.getAttributes()));
        if (domTag.getValue() != null) {
            if (domTag.getCdata()) {
                rootElement.add(new DefaultCDATA(domTag.getValue()));
            } else {
                rootElement.setText(domTag.getValue());
            }
        } else if (!Lists.isEmpty(domTag.getChildNode())) {
            for (DomTag info : this.domTag.getChildNode()) {
                buildChildElement(rootElement, info);
            }
        }
        return this;
    }

    /**
     * 构建子节点
     *
     * @param element 上级节点
     * @param domTag  dom信息
     */
    private void buildChildElement(Element element, DomTag domTag) {
        DefaultElement e = new DefaultElement(domTag.getName());
        element.add(e);
        e.setAttributes(getAttribute(domTag.getAttributes()));
        if (domTag.getValue() != null) {
            if (domTag.getCdata()) {
                e.add(new DefaultCDATA(domTag.getValue()));
            } else {
                e.setText(domTag.getValue());
            }
        } else if (!Lists.isEmpty(domTag.getChildNode())) {
            for (DomTag info : domTag.getChildNode()) {
                buildChildElement(e, info);
            }
        }
    }

    /**
     * Map 转 List<Attribute>
     *
     * @param map map
     * @return attribute
     */
    private List<Attribute> getAttribute(Map<String, String> map) {
        if (Maps.isEmpty(map)) {
            return new ArrayList<>();
        }
        List<Attribute> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new DefaultAttribute(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    /**
     * 获取 Document
     *
     * @return Document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * 获取 RootElement
     *
     * @return RootElement
     */
    public Element getRootElement() {
        return document.getRootElement();
    }

    /**
     * 获取 XML
     *
     * @return XML
     */
    public String getXml() {
        return document.asXML();
    }

    /**
     * 获取格式化的 XML
     *
     * @return XML
     */
    public String getFormatXml() {
        return DomExt.format(document.asXML());
    }

    /**
     * 将xml保存到文件
     *
     * @param file 本地文件
     */
    public void write(File file) throws IOException {
        DomExt.write(document.asXML(), file);
    }

    @Override
    public String toString() {
        return document.asXML();
    }

}

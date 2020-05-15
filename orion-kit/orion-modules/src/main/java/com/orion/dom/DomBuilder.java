package com.orion.dom;

import com.orion.able.Builderable;
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
 * @date 2020/3/24 12:58
 */
public class DomBuilder implements Builderable<DomBuilder> {

    /**
     * 构建xml信息
     */
    private DomInfo domInfo;

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
    public DomInfo getDomInfo() {
        return domInfo;
    }

    /**
     * 获取根节点信息
     *
     * @param name 标签
     * @return 节点
     */
    public DomInfo getDomInfo(String name) {
        if (domInfo == null) {
            domInfo = new DomInfo(name);
        }
        return domInfo;
    }

    /**
     * 设置节点信息
     *
     * @param domInfo domInfo
     * @return this
     */
    public DomBuilder setDomInfo(DomInfo domInfo) {
        this.domInfo = domInfo;
        return this;
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
        if (domInfo == null) {
            throw Exceptions.argument("domInfo is null");
        }
        this.document = new DefaultDocument();
        this.document.setXMLEncoding(charset);
        Element rootElement = new DefaultElement(domInfo.getName());
        this.document.setRootElement(rootElement);
        rootElement.setAttributes(getAttribute(domInfo.getAttributes()));
        if (domInfo.getValue() != null) {
            if (domInfo.getCdata()) {
                rootElement.add(new DefaultCDATA(domInfo.getValue()));
            } else {
                rootElement.setText(domInfo.getValue());
            }
        } else if (!Lists.isEmpty(domInfo.getChildNode())) {
            for (DomInfo info : this.domInfo.getChildNode()) {
                buildChildElement(rootElement, info);
            }
        }
        return this;
    }

    /**
     * 构建子节点
     *
     * @param element 上级节点
     * @param domInfo dom信息
     */
    private void buildChildElement(Element element, DomInfo domInfo) {
        DefaultElement e = new DefaultElement(domInfo.getName());
        element.add(e);
        e.setAttributes(getAttribute(domInfo.getAttributes()));
        if (domInfo.getValue() != null) {
            if (domInfo.getCdata()) {
                e.add(new DefaultCDATA(domInfo.getValue()));
            } else {
                e.setText(domInfo.getValue());
            }
        } else if (!Lists.isEmpty(domInfo.getChildNode())) {
            for (DomInfo info : domInfo.getChildNode()) {
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
    public String getXML() {
        return document.asXML();
    }

    /**
     * 获取格式化的 XML
     *
     * @return XML
     */
    public String getFormatXML() {
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

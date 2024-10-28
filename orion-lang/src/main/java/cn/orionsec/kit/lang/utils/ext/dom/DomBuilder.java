/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.able.Buildable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
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
 * XML 构建器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/24 12:58
 */
public class DomBuilder implements Buildable<DomBuilder> {

    /**
     * 构建 xml 信息
     */
    private DomElement domElement;

    /**
     * document
     */
    private Document document;

    /**
     * 编码格式
     */
    private String charset;

    public DomBuilder() {
        this.charset = Const.UTF_8;
    }

    /**
     * 创建实例
     *
     * @return DomBuilder
     */
    public static DomBuilder create() {
        return new DomBuilder();
    }

    /**
     * 设置节点信息
     *
     * @param domElement domElement
     * @return this
     */
    public DomBuilder setDomElement(DomElement domElement) {
        this.domElement = domElement;
        return this;
    }

    /**
     * 创建根节点信息
     *
     * @param name 标签
     * @return 节点
     */
    public DomElement createRootElement(String name) {
        return this.domElement = new DomElement(name);
    }

    /**
     * 设置 xml 编码格式
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
        Valid.notNull(domElement, "element is null");
        this.document = new DefaultDocument();
        document.setXMLEncoding(charset);
        Element rootElement = new DefaultElement(domElement.getName());
        document.setRootElement(rootElement);
        rootElement.setAttributes(this.getAttribute(domElement.getAttributes()));
        if (domElement.getValue() != null) {
            if (domElement.isCdata()) {
                rootElement.add(new DefaultCDATA(domElement.getValue()));
            } else {
                rootElement.setText(domElement.getValue());
            }
        } else if (!Lists.isEmpty(domElement.getChildNode())) {
            for (DomElement info : domElement.getChildNode()) {
                this.buildChildElement(rootElement, info);
            }
        }
        return this;
    }

    /**
     * 构建子节点
     *
     * @param element    上级节点
     * @param domElement dom信息
     */
    private void buildChildElement(Element element, DomElement domElement) {
        DefaultElement e = new DefaultElement(domElement.getName());
        element.add(e);
        e.setAttributes(this.getAttribute(domElement.getAttributes()));
        if (domElement.getValue() != null) {
            if (domElement.isCdata()) {
                e.add(new DefaultCDATA(domElement.getValue()));
            } else {
                e.setText(domElement.getValue());
            }
        } else if (!Lists.isEmpty(domElement.getChildNode())) {
            for (DomElement info : domElement.getChildNode()) {
                buildChildElement(e, info);
            }
        }
    }

    /**
     * map 转 list<Attribute>
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
     * 获取 document
     *
     * @return document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * 获取根节点信息
     *
     * @return 节点
     */
    public DomElement getDomElement() {
        return domElement;
    }

    /**
     * 获取 rootElement
     *
     * @return rootElement
     */
    public Element getRootElement() {
        return document.getRootElement();
    }

    /**
     * 获取 xml
     *
     * @return XML
     */
    public String getXml() {
        return document.asXML();
    }

    /**
     * 获取格式化的 xml
     *
     * @return xml
     */
    public String getFormatXml() {
        return DomSupport.format(document);
    }

    /**
     * 获取格式化的 xml
     *
     * @param format format
     * @return xml
     */
    public String getFormatXml(OutputFormat format) {
        return DomSupport.format(document, format);
    }

    /**
     * 将xml保存到文件
     *
     * @param file 本地文件
     */
    public void write(File file) throws IOException {
        DomSupport.write(document.asXML(), file);
    }

    @Override
    public String toString() {
        return document.asXML();
    }

}

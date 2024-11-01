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

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML 解析器
 * <p>
 * 如: bean[1] > property > list > ref[0]:name=c
 * 语法: [n] 下标  k:v 属性
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/24 10:17
 */
@SuppressWarnings("unchecked")
class DomParser {

    private final Element element;

    private final String formula;

    private final List<DomParserParam> domParserParams;

    DomParser(Element element, String formula) {
        this.element = element;
        this.formula = formula;
        this.domParserParams = new ArrayList<>();
        this.toParams();
    }

    /**
     * 解析公式
     */
    private void toParams() {
        try {
            String[] params = formula.split(">");
            for (String param : params) {
                DomParserParam domParserParam = new DomParserParam();
                param = param.trim();
                int is = param.indexOf("[");
                int ie = param.indexOf("]");
                int ak = param.indexOf(":");
                int av = param.indexOf("=");
                if ((is != -1 && ie != -1) && (ak != -1 && av != -1)) {
                    domParserParam.setName(param.substring(0, is));
                    domParserParam.setIndex(Integer.parseInt(param.substring(is + 1, ie)));
                    domParserParam.setKey(param.substring(ak + 1, av));
                    domParserParam.setValue(param.substring(av + 1));
                } else if (is != -1 && ie != -1) {
                    domParserParam.setName(param.substring(0, is));
                    domParserParam.setIndex(Integer.parseInt(param.substring(is + 1, ie)));
                } else if (ak != -1 && av != -1) {
                    domParserParam.setName(param.substring(0, ak));
                    domParserParam.setKey(param.substring(ak + 1, av));
                    domParserParam.setValue(param.substring(av + 1));
                } else {
                    domParserParam.setName(param);
                }
                domParserParams.add(domParserParam);
            }
        } catch (Exception e) {
            throw Exceptions.parse("cannot parse formula " + formula, e);
        }
    }

    /**
     * 获取解析后的element的value
     *
     * @return element的value
     */
    String getElementValue() {
        return this.getElement().getText();
    }

    /**
     * 获取解析后的element
     *
     * @return element
     */
    Element getElement() {
        Element now = element;
        for (DomParserParam domParserParam : domParserParams) {
            if (domParserParam.getIndex() != 0 && domParserParam.getKey() != null) {
                List<Element> elements = now.elements(domParserParam.getName());
                if (elements == null || elements.size() < domParserParam.getIndex() + 1) {
                    throw Exceptions.noSuchElement(Strings.format("not found element {}, index: {}, attr: {}, attrValue: {}", domParserParam.getName(), domParserParam.getIndex(), domParserParam.getKey(), domParserParam.getValue()));
                }
                Element element = elements.get(domParserParam.getIndex());
                String attribute = DomSupport.getAttribute(element, domParserParam.getKey());
                if (attribute != null && attribute.equals(domParserParam.getValue())) {
                    now = element;
                } else {
                    throw Exceptions.noSuchElement(Strings.format("not found element {}, index: {}, attr: {}, attrValue: {}", domParserParam.getName(), domParserParam.getIndex(), domParserParam.getKey(), domParserParam.getValue()));
                }
            } else if (domParserParam.getIndex() != 0) {
                List<Element> elements = now.elements(domParserParam.getName());
                if (elements == null || elements.size() < domParserParam.getIndex() + 1) {
                    throw Exceptions.noSuchElement(Strings.format("not found element {}, index: {}", domParserParam.getName(), domParserParam.getIndex()));
                }
                now = elements.get(domParserParam.getIndex());
            } else if (domParserParam.getKey() != null) {
                Element element = now.element(domParserParam.getName());
                if (element == null) {
                    throw Exceptions.noSuchElement(Strings.format("not found element {}", domParserParam.getName()));
                }
                String attribute = DomSupport.getAttribute(element, domParserParam.getKey());
                if (attribute != null && attribute.equals(domParserParam.getValue())) {
                    now = element;
                } else {
                    throw Exceptions.noSuchElement(Strings.format("not found element {}, attr: {}, attrValue: {}", domParserParam.getName(), domParserParam.getKey(), domParserParam.getValue()));
                }
            } else {
                Element element = now.element(domParserParam.getName());
                if (element == null) {
                    throw Exceptions.noSuchElement(Strings.format("not found element {}", domParserParam.getName()));
                }
                now = element;
            }
        }
        return now;
    }

}

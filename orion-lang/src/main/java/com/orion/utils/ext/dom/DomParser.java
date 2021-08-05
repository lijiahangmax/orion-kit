package com.orion.utils.ext.dom;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML解析器
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

    private Element element;

    private String formula;

    private List<DomParserParam> domParserParams = new ArrayList<>();

    DomParser(Element element, String formula) {
        this.element = element;
        this.formula = formula;
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

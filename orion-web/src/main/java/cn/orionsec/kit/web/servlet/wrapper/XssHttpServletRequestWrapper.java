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
package cn.orionsec.kit.web.servlet.wrapper;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Xsses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * xss请求过滤包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/21 10:03
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 存放 url 中不需要过滤的字段名
     */
    private final Set<String> ignoreFields;

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        this.ignoreFields = Collections.emptySet();
    }

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest, String fields) {
        super(servletRequest);
        this.ignoreFields = new HashSet<>();
        String[] fieldArr = fields.split(Const.COMMA);
        for (String f : fieldArr) {
            ignoreFields.add(f.trim());
        }
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            if (ignoreFields.contains(parameter)) {
                encodedValues[i] = Xsses.clean(values[i]);
            } else {
                encodedValues[i] = values[i];
            }
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        if (ignoreFields.contains(parameter)) {
            return Xsses.clean(value);
        }
        return value;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return Xsses.clean(value);
    }

}

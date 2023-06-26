package com.orion.web.servlet.wrapper;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Xsses;

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

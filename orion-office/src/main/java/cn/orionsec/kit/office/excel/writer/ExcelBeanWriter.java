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
package cn.orionsec.kit.office.excel.writer;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.reflect.Fields;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.office.excel.option.WriteFieldOption;
import cn.orionsec.kit.office.excel.writer.exporting.ExcelExport;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * excel bean 写入器 不支持注解
 * <p>
 * 如果要使用注解需要用 {@link ExcelExport} 导出
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/13 14:22
 */
public class ExcelBeanWriter<T> extends BaseExcelWriter<String, T> {

    private final Class<T> targetClass;

    /**
     * getter
     */
    private final Map<String, Method> getters;

    public ExcelBeanWriter(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        super(workbook, sheet);
        this.targetClass = Assert.notNull(targetClass, "target class is null");
        this.getters = Methods.getGetterMethodsByCache(targetClass).stream()
                .collect(Collectors.toMap(Fields::getFieldNameByMethod, Function.identity()));
    }

    @Override
    protected void addOption(String field, WriteFieldOption option, Object defaultValue) {
        Assert.notNull(getters.get(field), "not found getter method ({}) in {}", field, targetClass);
        super.addOption(field, option, defaultValue);
    }

    @Override
    protected Object getValue(T row, String key) {
        return Methods.invokeMethod(row, getters.get(key));
    }

}

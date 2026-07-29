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
package cn.orionsec.kit.office.csv.annotation;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.office.csv.type.CsvEscapeMode;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link ImportSetting} 注解元数据测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class ImportSettingTest {

    @ImportSetting(delimiter = '\t', useComments = true, caseSensitive = false)
    private static class User {
    }

    @Test
    public void testRetention() {
        Retention retention = ImportSetting.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    public void testTarget() {
        Target target = ImportSetting.class.getAnnotation(Target.class);
        assertNotNull(target);
        List<ElementType> types = Arrays.asList(target.value());
        assertEquals(1, types.size());
        assertTrue(types.contains(ElementType.TYPE));
    }

    @Test
    public void testDocumented() {
        assertTrue(ImportSetting.class.isAnnotationPresent(Documented.class));
    }

    @Test
    public void testDefaultValues() throws Exception {
        assertEquals(Letters.QUOTE, defaultOf("textQualifier"));
        assertEquals(true, defaultOf("useTextQualifier"));
        assertEquals(Letters.COMMA, defaultOf("delimiter"));
        assertEquals(Letters.NULL, defaultOf("lineDelimiter"));
        assertEquals(Letters.POUND, defaultOf("comment"));
        assertEquals(CsvEscapeMode.DOUBLE_QUALIFIER, defaultOf("escapeMode"));
        assertEquals(Const.UTF_8, defaultOf("charset"));
        assertEquals(true, defaultOf("caseSensitive"));
        assertEquals(true, defaultOf("trim"));
        assertEquals(false, defaultOf("useComments"));
        assertEquals(true, defaultOf("safetySwitch"));
        assertEquals(true, defaultOf("skipEmptyRows"));
        assertEquals(true, defaultOf("skipRawRow"));
    }

    @Test
    public void testUsageOnType() {
        ImportSetting setting = User.class.getAnnotation(ImportSetting.class);
        assertNotNull(setting);
        assertEquals('\t', setting.delimiter());
        assertTrue(setting.useComments());
        assertFalse(setting.caseSensitive());
        // 未指定的属性使用默认值
        assertTrue(setting.safetySwitch());
        assertTrue(setting.skipEmptyRows());
    }

    private static Object defaultOf(String attr) throws Exception {
        Method method = ImportSetting.class.getMethod(attr);
        return method.getDefaultValue();
    }

}

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

import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link ExportIgnore} 注解元数据测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class ExportIgnoreTest {

    private static class User {
        @ExportIgnore
        private String password;

        private String name;
    }

    @Test
    public void testRetention() {
        Retention retention = ExportIgnore.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    public void testTarget() {
        Target target = ExportIgnore.class.getAnnotation(Target.class);
        assertNotNull(target);
        List<ElementType> types = Arrays.asList(target.value());
        assertTrue(types.contains(ElementType.FIELD));
        assertTrue(types.contains(ElementType.METHOD));
    }

    @Test
    public void testDocumented() {
        assertTrue(ExportIgnore.class.isAnnotationPresent(Documented.class));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, ExportIgnore.class.getDeclaredMethods().length);
    }

    @Test
    public void testUsageOnField() throws Exception {
        Field password = User.class.getDeclaredField("password");
        assertTrue(password.isAnnotationPresent(ExportIgnore.class));
        Field name = User.class.getDeclaredField("name");
        assertFalse(name.isAnnotationPresent(ExportIgnore.class));
    }

}

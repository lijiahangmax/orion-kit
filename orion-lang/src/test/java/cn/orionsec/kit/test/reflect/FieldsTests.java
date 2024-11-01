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
package cn.orionsec.kit.test.reflect;

import cn.orionsec.kit.lang.define.Console;
import cn.orionsec.kit.lang.utils.reflect.Fields;
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.test.reflect.value.BaseTypeUser;
import cn.orionsec.kit.test.reflect.value.User;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/13 18:39
 */
public class FieldsTests {

    @Test
    public void getTests() {
        System.out.println(Fields.getFieldNameByMethod("getName"));
        System.out.println(Fields.getFieldNameByMethod("setName"));
        System.out.println(Fields.getFieldNameByMethod("isName"));
        System.out.println(Fields.getFieldByMethod(User.class, Methods.getAccessibleMethod(User.class, "getId")));
        System.out.println(Fields.getFieldByMethod(User.class, "getId"));
        System.out.println("------");
        System.out.println((Object) Fields.getFieldValue(new User(1L), "id"));
        System.out.println((Object) Fields.getFieldValue(new User(2L), Fields.getAccessibleField(User.class, "id")));
    }

    @Test
    public void setTests() {
        User user = new User();
        Fields.setFieldValue(user, "id", 1L);
        System.out.println(user.getId());
        Fields.setFieldValue(user, Fields.getAccessibleField(user.getClass(), "id"), 2L);
        System.out.println(user.getId());
        Fields.setFieldValueInfer(user, "id", "2");
        System.out.println(user.getId());
        Fields.setFieldValueInfer(user, Fields.getAccessibleField(user.getClass(), "id"), 3);
        System.out.println(user.getId());
    }

    @Test
    public void getFieldTests() {
        System.out.println("------");
        Fields.getFields(User.class).forEach(Console::trace);
        System.out.println("------");
        Fields.getFieldMap(User.class).forEach(Console::trace);
        System.out.println("------");
        Fields.getStaticFields(User.class).forEach(Console::trace);
    }

    @Test
    public void getBaseFieldTests() {
        BaseTypeUser u = new BaseTypeUser();
        Fields.setFieldValueInfer(u, "id", 1L);
        Fields.setFieldValue(u, "time", 2L);
        Fields.setFieldValue(u, "sex", true);
        Fields.setFieldValue(u, "name", 'n');
        System.out.println(u.toString());
        System.out.println((Object) Fields.getFieldValue(u, "id"));
        System.out.println((Object) Fields.getFieldValue(u, "time"));
        System.out.println((Object) Fields.getFieldValue(u, "sex"));
        System.out.println((Object) Fields.getFieldValue(u, "name"));
    }

}

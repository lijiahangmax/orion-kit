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
package com.orion.test.reflect;

import com.orion.lang.define.Console;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.reflect.Annotations;
import com.orion.lang.utils.reflect.Constructors;
import com.orion.lang.utils.reflect.Fields;
import com.orion.lang.utils.reflect.Methods;
import com.orion.test.reflect.value.*;
import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/23 18:33
 */
public class AnnoTests {

    @Test
    public void getTest() {
        System.out.println((Object) Annotations.getDefaultValue(UserAnno1.class));
        System.out.println((Object) Annotations.getDefaultValue(UserAnno1.class, "name"));
        System.out.println((Object) Annotations.getDefaultValue(UserAnno1.class, "age"));
        System.out.println((Object) Annotations.getDefaultValue(UserAnno1.class, "age1"));

        Annotation annotation = User.class.getAnnotations()[0];
        UserAnno1 a1 = Annotations.cast(annotation);

        System.out.println((Object) Annotations.getDefaultValue(annotation));
        System.out.println((Object) Annotations.getDefaultValue(annotation, "name"));
        System.out.println((Object) Annotations.getDefaultValue(annotation, "age"));
        System.out.println((Object) Annotations.getDefaultValue(annotation, "age1"));

        System.out.println("---");
        System.out.println((Object) Annotations.getValue(a1));
        System.out.println((Object) Annotations.getAttribute(a1, "name"));
        System.out.println("---");
        Annotations.getAttributes(a1).forEach(Console::trace);
    }

    @Test
    public void present() {
        Valid.isTrue(Annotations.present(User.class, UserAnno1.class));

        Valid.isFalse(Annotations.present(User.class, UserAnno2.class));
        Valid.isFalse(Annotations.present(User.class, UserAnno1.class, UserAnno2.class));

        Valid.isTrue(Annotations.present(Constructors.getConstructor(User.class, 1), UserAnno1.class));
        Valid.isTrue(Annotations.present(Constructors.getConstructor(User.class, 2), UserAnno1.class, UserAnno2.class));

        Valid.isFalse(Annotations.present(Constructors.getConstructor(User.class, 0), UserAnno1.class));
        Valid.isFalse(Annotations.present(Constructors.getConstructor(User.class, 1), UserAnno2.class));
        Valid.isFalse(Annotations.present(Constructors.getConstructor(User.class, 1), UserAnno1.class, UserAnno2.class));

        Valid.isTrue(Annotations.present(Fields.getAccessibleField(User.class, "id"), UserAnno1.class));
        Valid.isTrue(Annotations.present(Fields.getAccessibleField(User.class, "age"), UserAnno2.class));
        Valid.isTrue(Annotations.present(Fields.getAccessibleField(User.class, "sex"), UserAnno1.class, UserAnno2.class));

        Valid.isFalse(Annotations.present(Fields.getAccessibleField(User.class, "id"), UserAnno2.class));
        Valid.isFalse(Annotations.present(Fields.getAccessibleField(User.class, "age"), UserAnno1.class));
        Valid.isFalse(Annotations.present(Fields.getAccessibleField(User.class, "type"), UserAnno1.class, UserAnno2.class));

        Valid.isTrue(Annotations.present(Methods.getAccessibleMethod(User.class, "getId"), UserAnno1.class));
        Valid.isTrue(Annotations.present(Methods.getAccessibleMethod(User.class, "setId"), UserAnno2.class));
        Valid.isTrue(Annotations.present(Methods.getAccessibleMethod(User.class, "getAge"), UserAnno1.class, UserAnno2.class));

        Valid.isFalse(Annotations.present(Methods.getAccessibleMethod(User.class, "getId"), UserAnno2.class));
        Valid.isFalse(Annotations.present(Methods.getAccessibleMethod(User.class, "setId"), UserAnno1.class));
        Valid.isFalse(Annotations.present(Methods.getAccessibleMethod(User.class, "isOk"), UserAnno1.class, UserAnno2.class));
    }

    @Test
    public void getTest1() {
        System.out.println(Annotations.getAnnotatedConstructor(Order.class, OrderAnno.class));
        System.out.println();
        System.out.println(Annotations.getAnnotatedFields(Order.class, OrderAnno.class));
        System.out.println();
        System.out.println(Annotations.getAnnotatedGetterMethods(Order.class, OrderAnno.class));
        System.out.println();
        System.out.println(Annotations.getAnnotatedSetterMethods(Order.class, OrderAnno.class));
        System.out.println();
        System.out.println(Annotations.getAnnotatedGetterMethodsMergeField(Order.class, OrderAnno.class));
        System.out.println();
        System.out.println(Annotations.getAnnotatedSetterMethodsMergeField(Order.class, OrderAnno.class));
        System.out.println();
    }

}

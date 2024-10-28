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
import cn.orionsec.kit.lang.utils.reflect.Methods;
import cn.orionsec.kit.test.reflect.value.User;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/19 17:43
 */
public class MethodTests {

    @Test
    public void getGetter() {
        Methods.getGetterMethods(User.class).forEach(Console::trace);
    }

    @Test
    public void getGetter1() {
        Methods.getGetterMethodsByField(User.class).forEach(Console::trace);
    }

    @Test
    public void getSetter() {
        Methods.getSetterMethods(User.class).forEach(Console::trace);
    }

    @Test
    public void getSetter1() {
        Methods.getSetterMethodsByField(User.class).forEach(Console::trace);
    }

    @Test
    public void getMethods() {
        Methods.getAccessibleMethods(User.class).forEach(Console::trace);
        System.out.println("--------");
        Methods.getAccessibleMethodMap(User.class).forEach(Console::trace);
        System.out.println("--------");
        Methods.getStaticMethods(User.class).forEach(Console::trace);

    }

    @Test
    public void getAccessibleMethod() {
        System.out.println(Methods.getAccessibleMethod(User.class, "getType", 0));
        System.out.println(Methods.getAccessibleMethod(User.class, "getType", 1));
        System.out.println(Methods.getAccessibleMethod(User.class, "setType", 0));
        System.out.println(Methods.getAccessibleMethod(User.class, "setType", 1));
        System.out.println(Methods.getAccessibleMethod(User.class, "setType", int.class));
        System.out.println(Methods.getAccessibleMethod(User.class, "setType", Integer.class));
        System.out.println(Methods.getAccessibleMethod(User.class, "isOk"));
        System.out.println(Methods.getAccessibleMethod(User.class, "isOk", 0));
        System.out.println(Methods.getAccessibleMethod(User.class, "getBalance"));
        System.out.println(Methods.getAccessibleMethods(User.class, "getBalance"));
    }

    @Test
    public void invoke() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        System.out.println((Object) Methods.invokeMethod(user, "getName"));
        Methods.invokeMethod(user, "setName", "lsd");
        System.out.println((Object) Methods.invokeMethod(user, "getName"));
        Methods.invokeSetter(user, "name", "q");
        System.out.println(user.getName());
        System.out.println((Object) Methods.invokeGetter(user, "age"));
        Methods.invokeSetterInfer(user, "age", 2);
        System.out.println((Object) Methods.invokeGetter(user, "age"));
        Methods.invokeMethodInfer(user, "setBalance", "2.2");
        System.out.println(user.getBalance());
    }

}

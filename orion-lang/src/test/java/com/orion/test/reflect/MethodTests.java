package com.orion.test.reflect;

import com.orion.lang.define.Console;
import com.orion.lang.utils.reflect.Methods;
import com.orion.test.reflect.value.User;
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

package com.orion.test.reflect;

import com.orion.lang.Console;
import com.orion.test.reflect.value.User;
import com.orion.utils.reflect.Methods;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/19 17:43
 */
public class MethodTests {

    @Test
    public void getGetter() {
        Methods.getAllGetterMethod(User.class).forEach(Console::trace);
    }

    @Test
    public void getGetter1() {
        Methods.getAllGetterMethodByField(User.class).forEach(Console::trace);
    }

    @Test
    public void getSetter() {
        Methods.getAllSetterMethod(User.class).forEach(Console::trace);
    }

    @Test
    public void getSetter1() {
        Methods.getAllSetterMethodByField(User.class).forEach(Console::trace);
    }

    @Test
    public void getMethods() {
        Methods.getMethodList(User.class).forEach(Console::trace);
        System.out.println("--------");
        Methods.getMethodMap(User.class).forEach(Console::trace);
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
        System.out.println((Object) Methods.invokeMethod(user, "setName", new Object[]{"lsd"}));
        System.out.println((Object) Methods.invokeMethod(user, "getName"));
        System.out.println((Object) Methods.invokeSetter(user, "name", "q"));
        System.out.println(user.getName());
        System.out.println((Object) Methods.invokeGetter(user, "age"));
        System.out.println((Object) Methods.invokeSetterInfer(user, "age", 2));
        System.out.println((Object) Methods.invokeGetter(user, "age"));
        System.out.println((Object) Methods.invokeMethodInfer(user, "setBalance", new Object[]{"2.2"}));
        System.out.println(user.getBalance());
    }

}

package com.orion.test.reflect;

import com.orion.lang.Console;
import com.orion.test.reflect.value.Shop;
import com.orion.utils.reflect.Constructors;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/23 18:33
 */
public class ConstructorTests {

    @Test
    public void getConstructorTests() {
        Constructors.getConstructors(Shop.class).forEach(Console::trace);
        System.out.println("------");
        System.out.println(Constructors.getDefaultConstructor(Shop.class));
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 0).forEach(Console::trace);
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 1).forEach(Console::trace);
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 2).forEach(Console::trace);
        System.out.println("------");
        Constructors.getConstructors(Shop.class, 3).forEach(Console::trace);
        System.out.println("------");
        System.out.println(Constructors.getConstructor(Shop.class, 0));
        System.out.println(Constructors.getConstructor(Shop.class, 1));
        System.out.println(Constructors.getConstructor(Shop.class, 2));
        System.out.println(Constructors.getConstructor(Shop.class, 3));
        System.out.println("------");
        System.out.println(Constructors.getConstructor(Shop.class));
        System.out.println(Constructors.getConstructor(Shop.class, Long.class));
        System.out.println(Constructors.getConstructor(Shop.class, Integer.class));
        System.out.println(Constructors.getConstructor(Shop.class, String.class));
        System.out.println(Constructors.getConstructor(Shop.class, String.class, Long.class));
    }

    @Test
    public void newInstanceTest() {
        System.out.println(Constructors.newInstance(Shop.class));
        System.out.println(Constructors.newInstance(Constructors.getDefaultConstructor(Shop.class)));
        System.out.println(Constructors.newInstance(Constructors.getDefaultConstructor(Shop.class)));
        System.out.println("------");
        System.out.println(Constructors.newInstance(Constructors.getConstructor(Shop.class, Long.class), 1L));
        System.out.println(Constructors.newInstance(Constructors.getConstructor(Shop.class, String.class), "ss"));
        System.out.println(Constructors.newInstance(Constructors.getConstructor(Shop.class, String.class, Long.class), "ss1", 2L));
        System.out.println("------");
        System.out.println(Constructors.newInstance(Shop.class, new Class[]{Long.class}, 1L));
        System.out.println(Constructors.newInstance(Shop.class, new Class[]{String.class}, "ss"));
        System.out.println(Constructors.newInstance(Shop.class, new Class[]{String.class, Long.class}, "ss1", 11L));
        System.out.println("------");
        System.out.println(Constructors.newInstanceInfer(Shop.class, 1L));
        System.out.println(Constructors.newInstanceInfer(Shop.class, "1L"));
        System.out.println(Constructors.newInstanceInfer(Shop.class, 123, "1"));
        System.out.println("------");
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, Long.class), "22"));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, Long.class), 22));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, String.class), 22.22));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, String.class), 22));
        System.out.println(Constructors.newInstanceInfer(Constructors.getConstructor(Shop.class, String.class, Long.class), 22.22, 22.22));
    }

}

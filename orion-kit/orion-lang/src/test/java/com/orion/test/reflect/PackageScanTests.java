package com.orion.test.reflect;

import com.orion.lang.Console;
import com.orion.test.reflect.value.BaseUser;
import com.orion.test.reflect.value.UserAnno1;
import com.orion.utils.reflect.PackageScanners;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/23 18:33
 */
public class PackageScanTests {

    @Test
    public void scan() {
        PackageScanners s = new PackageScanners(true, "com");
        s.getScanClasses().forEach(Console::trace);
        System.out.println("-----");
        s.getImplClass(BaseUser.class).forEach(Console::trace);
        System.out.println("-----");
        s.getAnnotatedClass(UserAnno1.class).forEach(Console::trace);
        System.out.println("-----");
        s.getAnnotatedConstructor(UserAnno1.class).forEach(Console::trace);
        System.out.println("-----");
        s.getAnnotatedField(UserAnno1.class).forEach(Console::trace);
        System.out.println("-----");
        s.getAnnotatedMethod(UserAnno1.class).forEach(Console::trace);
        System.out.println("-----");
    }

}

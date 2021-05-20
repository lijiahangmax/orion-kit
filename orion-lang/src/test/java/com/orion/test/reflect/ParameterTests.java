package com.orion.test.reflect;

import com.orion.test.reflect.value.User;
import com.orion.test.reflect.value.UserAnno1;
import com.orion.utils.Valid;
import com.orion.utils.reflect.Constructors;
import com.orion.utils.reflect.Methods;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/4 18:03
 */
public class ParameterTests {

    @Test
    public void testParameter() {
        Method method = Methods.getAccessibleMethod(User.class, "say");
        method = Valid.notNull(method);
        Parameter[] parameters = method.getParameters();
        for (Parameter p : parameters) {
            System.out.println(p.getAnnotation(UserAnno1.class));
            System.out.println(p.getType());
            System.out.println(p.getParameterizedType());
        }
        System.out.println("---");
        Constructor<User> c = Constructors.getConstructor(User.class, 2);
        c = Valid.notNull(c);
        parameters = c.getParameters();
        for (Parameter p : parameters) {
            System.out.println(p.getAnnotation(UserAnno1.class));
            System.out.println(p.getType());
            System.out.println(p.getParameterizedType());
        }
    }

    @Test
    public void testParameter1() {
        Method method = Methods.getAccessibleMethod(User.class, "setId");
        method = Valid.notNull(method);
        for (Annotation[] a : method.getParameterAnnotations()) {
            System.out.println(a.length);
        }
    }

}

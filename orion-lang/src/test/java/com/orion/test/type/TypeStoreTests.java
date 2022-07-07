package com.orion.test.type;

import com.orion.lang.utils.convert.TypeStore;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 12:35
 */
public class TypeStoreTests {

    @Test
    public void test1() {
        System.out.println(TypeStore.STORE.to(new Integer[]{}, int[].class).getClass());
        System.out.println(TypeStore.STORE.to(new Integer[]{}, Integer[].class).getClass());
        System.out.println(TypeStore.STORE.to(new int[]{}, int[].class).getClass());
        System.out.println(TypeStore.STORE.to(new int[]{}, Integer[].class).getClass());
        System.out.println(TypeStore.STORE.to(new int[]{}, Long[].class).getClass());
    }

    @Test
    public void test2() {
        System.out.println(TypeStore.STORE.getAllSuitableClasses(Integer[].class));
        System.out.println(TypeStore.STORE.getAllSuitableClasses(Integer.class));
        System.out.println(TypeStore.STORE.getAllSuitableClasses(Serializable.class));
    }

    @Test
    public void test3() {
        System.out.println(TypeStore.STORE.to(new int[]{1, 2, 3}, Integer[].class).length);
        System.out.println(TypeStore.STORE.to(new int[]{1, 2, 3}, Long[].class).length);
    }

    @Test
    public void test4() {
        TypeStore.STORE.getConversionMapping().forEach((k, v) -> {
            v.forEach((v1, vv) -> {
                System.out.println(k + " " + v1);
            });
        });
    }

}

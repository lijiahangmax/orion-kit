package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

/**
 * Classes 单元测试
 */
public class ClassesTest {

    interface MyInterface {
    }

    interface MyInterface2 {
    }

    static class ParentClass implements MyInterface {
    }

    static class ChildClass extends ParentClass implements MyInterface2, Serializable {
    }

    @Test
    public void testGetCurrentClassLoader() {
        ClassLoader cl = Classes.getCurrentClassLoader();
        Assert.assertNotNull(cl);
    }

    @Test
    public void testLoadClass() {
        Class<?> clazz = Classes.loadClass("java.lang.String");
        Assert.assertNotNull(clazz);
        Assert.assertEquals(String.class, clazz);
    }

    @Test
    public void testLoadClassNotFound() {
        Class<?> clazz = Classes.loadClass("com.nonexist.FakeClass");
        Assert.assertNull(clazz);
    }

    @Test
    public void testLoadClassWithInit() {
        Class<?> clazz = Classes.loadClass("java.lang.Integer", true);
        Assert.assertNotNull(clazz);
    }

    @Test
    public void testIsProxy() {
        Assert.assertFalse(Classes.isProxy(String.class));
        Assert.assertFalse(Classes.isProxy(ChildClass.class));
    }

    @Test
    public void testIsJdkProxy() {
        Assert.assertFalse(Classes.isJdkProxy("hello"));
        Assert.assertFalse(Classes.isJdkProxy(new ChildClass()));
    }

    @Test
    public void testIsCglibProxy() {
        Assert.assertFalse(Classes.isCglibProxy("hello"));
    }

    @Test
    public void testGetSuperClass() {
        Class<?> superClass = Classes.getSuperClass(ChildClass.class);
        Assert.assertEquals(ParentClass.class, superClass);
    }

    @Test
    public void testGetSuperClasses() {
        List<Class<?>> superClasses = Classes.getSuperClasses(ChildClass.class);
        Assert.assertNotNull(superClasses);
        Assert.assertTrue(superClasses.contains(ParentClass.class));
    }

    @Test
    public void testGetInterfaces() {
        List<Class<?>> interfaces = Classes.getInterfaces(ChildClass.class);
        Assert.assertNotNull(interfaces);
        Assert.assertTrue(interfaces.contains(MyInterface.class));
        Assert.assertTrue(interfaces.contains(MyInterface2.class));
        Assert.assertTrue(interfaces.contains(Serializable.class));
    }

    @Test
    public void testGetInterfacesForInterface() {
        List<Class<?>> interfaces = Classes.getInterfaces(MyInterface.class);
        Assert.assertNotNull(interfaces);
        Assert.assertTrue(interfaces.contains(MyInterface.class));
    }

    @Test
    public void testIsInterface() {
        Assert.assertTrue(Classes.isInterface(MyInterface.class));
        Assert.assertFalse(Classes.isInterface(ChildClass.class));
    }

    @Test
    public void testIsImplClass() {
        Assert.assertTrue(Classes.isImplClass(MyInterface.class, ChildClass.class));
        Assert.assertTrue(Classes.isImplClass(ParentClass.class, ChildClass.class));
        Assert.assertTrue(Classes.isImplClass(Object.class, ChildClass.class));
        Assert.assertTrue(Classes.isImplClass(ChildClass.class, ChildClass.class));
        Assert.assertFalse(Classes.isImplClass(ChildClass.class, ParentClass.class));
    }

    @Test
    public void testIsArray() {
        Assert.assertTrue(Classes.isArray(int[].class));
        Assert.assertTrue(Classes.isArray(String[].class));
        Assert.assertFalse(Classes.isArray(String.class));
    }

    @Test
    public void testIsBaseClass() {
        Assert.assertTrue(Classes.isBaseClass(int.class));
        Assert.assertTrue(Classes.isBaseClass(boolean.class));
        Assert.assertTrue(Classes.isBaseClass(double.class));
        Assert.assertFalse(Classes.isBaseClass(Integer.class));
        Assert.assertFalse(Classes.isBaseClass(String.class));
    }

    @Test
    public void testIsWrapClass() {
        Assert.assertTrue(Classes.isWrapClass(Integer.class));
        Assert.assertTrue(Classes.isWrapClass(Boolean.class));
        Assert.assertTrue(Classes.isWrapClass(Double.class));
        Assert.assertFalse(Classes.isWrapClass(int.class));
        Assert.assertFalse(Classes.isWrapClass(String.class));
    }

    @Test
    public void testGetWrapClass() {
        Assert.assertEquals(Integer.class, Classes.getWrapClass(int.class));
        Assert.assertEquals(Boolean.class, Classes.getWrapClass(boolean.class));
        Assert.assertEquals(Long.class, Classes.getWrapClass(long.class));
        Assert.assertEquals(String.class, Classes.getWrapClass(String.class));
    }

    @Test
    public void testGetBaseClass() {
        Assert.assertEquals(int.class, Classes.getBaseClass(Integer.class));
        Assert.assertEquals(boolean.class, Classes.getBaseClass(Boolean.class));
        Assert.assertEquals(String.class, Classes.getBaseClass(String.class));
    }

    @Test
    public void testIsBaseArrayClass() {
        Assert.assertTrue(Classes.isBaseArrayClass(int[].class));
        Assert.assertTrue(Classes.isBaseArrayClass(double[].class));
        Assert.assertFalse(Classes.isBaseArrayClass(Integer[].class));
    }

    @Test
    public void testIsWrapArrayClass() {
        Assert.assertTrue(Classes.isWrapArrayClass(Integer[].class));
        Assert.assertTrue(Classes.isWrapArrayClass(Double[].class));
        Assert.assertFalse(Classes.isWrapArrayClass(int[].class));
    }

    @Test
    public void testGetWrapArrayClass() {
        Assert.assertEquals(Integer[].class, Classes.getWrapArrayClass(int[].class));
        Assert.assertEquals(Boolean[].class, Classes.getWrapArrayClass(boolean[].class));
    }

    @Test
    public void testGetBaseArrayClass() {
        Assert.assertEquals(int[].class, Classes.getBaseArrayClass(Integer[].class));
        Assert.assertEquals(boolean[].class, Classes.getBaseArrayClass(Boolean[].class));
    }

    @Test
    public void testIsNumberClass() {
        Assert.assertTrue(Classes.isNumberClass(int.class));
        Assert.assertTrue(Classes.isNumberClass(Integer.class));
        Assert.assertTrue(Classes.isNumberClass(Long.class));
        Assert.assertTrue(Classes.isNumberClass(double.class));
        Assert.assertFalse(Classes.isNumberClass(String.class));
        Assert.assertFalse(Classes.isNumberClass(boolean.class));
    }

    @Test
    public void testIsNumberClassObject() {
        Assert.assertTrue(Classes.isNumberClass(Integer.valueOf(1)));
        Assert.assertTrue(Classes.isNumberClass(Long.valueOf(1L)));
        Assert.assertFalse(Classes.isNumberClass("hello"));
    }
}

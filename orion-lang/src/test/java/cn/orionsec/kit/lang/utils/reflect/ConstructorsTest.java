package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Constructors 单元测试
 */
public class ConstructorsTest {

    static class SimpleClass {
        private String value;

        public SimpleClass() {
        }

        public SimpleClass(String value) {
            this.value = value;
        }

        public SimpleClass(String value, int num) {
            this.value = value + num;
        }

        public String getValue() {
            return value;
        }
    }

    static class PrivateConstructorClass {
        private String data;

        private PrivateConstructorClass() {
            this.data = "private";
        }

        public String getData() {
            return data;
        }
    }

    @Test
    public void testGetDefaultConstructor() {
        Constructor<SimpleClass> ctor = Constructors.getDefaultConstructor(SimpleClass.class);
        Assert.assertNotNull(ctor);
        Assert.assertEquals(0, ctor.getParameterCount());
    }

    @Test
    public void testGetDefaultConstructorByCache() {
        Constructor<SimpleClass> ctor1 = Constructors.getDefaultConstructorByCache(SimpleClass.class);
        Constructor<SimpleClass> ctor2 = Constructors.getDefaultConstructorByCache(SimpleClass.class);
        Assert.assertNotNull(ctor1);
        Assert.assertSame(ctor1, ctor2);
    }

    @Test
    public void testGetConstructorByTypes() {
        Constructor<SimpleClass> ctor = Constructors.getConstructor(SimpleClass.class, String.class);
        Assert.assertNotNull(ctor);
        Assert.assertEquals(1, ctor.getParameterCount());
    }

    @Test
    public void testGetConstructorByLen() {
        Constructor<SimpleClass> ctor = Constructors.getConstructor(SimpleClass.class, 2);
        Assert.assertNotNull(ctor);
        Assert.assertEquals(2, ctor.getParameterCount());
    }

    @Test
    public void testGetConstructors() {
        List<Constructor<SimpleClass>> ctors = Constructors.getConstructors(SimpleClass.class);
        Assert.assertNotNull(ctors);
        Assert.assertTrue(ctors.size() >= 3);
    }

    @Test
    public void testGetConstructorsByLen() {
        List<Constructor<SimpleClass>> ctors = Constructors.getConstructors(SimpleClass.class, 1);
        Assert.assertNotNull(ctors);
        Assert.assertEquals(1, ctors.size());
    }

    @Test
    public void testNewInstanceDefault() {
        SimpleClass obj = Constructors.newInstance(SimpleClass.class);
        Assert.assertNotNull(obj);
    }

    @Test
    public void testNewInstanceWithConstructor() {
        Constructor<SimpleClass> ctor = Constructors.getConstructor(SimpleClass.class, String.class);
        SimpleClass obj = Constructors.newInstance(ctor, "hello");
        Assert.assertNotNull(obj);
        Assert.assertEquals("hello", obj.getValue());
    }

    @Test
    public void testNewInstanceWithParamTypes() {
        SimpleClass obj = Constructors.newInstance(SimpleClass.class, new Class[]{String.class}, "world");
        Assert.assertNotNull(obj);
        Assert.assertEquals("world", obj.getValue());
    }

    @Test
    public void testNewInstanceInferByClass() {
        SimpleClass obj = Constructors.newInstanceInfer(SimpleClass.class, "test");
        Assert.assertNotNull(obj);
        Assert.assertEquals("test", obj.getValue());
    }

    @Test
    public void testNewInstanceInferByConstructor() {
        Constructor<SimpleClass> ctor = Constructors.getConstructor(SimpleClass.class, String.class);
        SimpleClass obj = Constructors.newInstanceInfer(ctor, "infer");
        Assert.assertNotNull(obj);
        Assert.assertEquals("infer", obj.getValue());
    }

    @Test
    public void testNewInstancePrivateConstructor() {
        Constructor<PrivateConstructorClass> ctor = Constructors.getDefaultConstructor(PrivateConstructorClass.class);
        Assert.assertNotNull(ctor);
        PrivateConstructorClass obj = Constructors.newInstance(ctor);
        Assert.assertNotNull(obj);
        Assert.assertEquals("private", obj.getData());
    }

    @Test
    public void testSetAccessible() {
        Constructor<PrivateConstructorClass> ctor = Constructors.getDefaultConstructor(PrivateConstructorClass.class);
        Assert.assertNotNull(ctor);
        Constructors.setAccessible(ctor);
        Assert.assertTrue(ctor.isAccessible());
    }
}

package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Methods 单元测试
 */
public class MethodsTest {

    static class SampleBean {
        private String name;
        private int age;
        private boolean active;

        public SampleBean() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String hello(String msg) {
            return "hello " + msg;
        }

        public int add(int a, int b) {
            return a + b;
        }

        private void privateMethod() {
        }

        public static void staticMethod() {
        }
    }

    @Test
    public void testGetGetterMethodNameByField() {
        Assert.assertEquals("getName", Methods.getGetterMethodNameByField("name"));
        Assert.assertEquals("isActive", Methods.getGetterMethodNameByField("active", true));
        Assert.assertEquals("getActive", Methods.getGetterMethodNameByField("active", false));
        Assert.assertNull(Methods.getGetterMethodNameByField(""));
        Assert.assertNull(Methods.getGetterMethodNameByField((String) null));
    }

    @Test
    public void testGetSetterMethodNameByField() {
        Assert.assertEquals("setName", Methods.getSetterMethodNameByField("name"));
        Assert.assertEquals("setAge", Methods.getSetterMethodNameByField("age"));
        Assert.assertNull(Methods.getSetterMethodNameByField(""));
        Assert.assertNull(Methods.getSetterMethodNameByField((String) null));
    }

    @Test
    public void testGetGetterMethods() {
        List<Method> getters = Methods.getGetterMethods(SampleBean.class);
        Assert.assertNotNull(getters);
        Assert.assertTrue(getters.size() >= 3);
    }

    @Test
    public void testGetSetterMethods() {
        List<Method> setters = Methods.getSetterMethods(SampleBean.class);
        Assert.assertNotNull(setters);
        Assert.assertTrue(setters.size() >= 3);
    }

    @Test
    public void testGetGetterMethodByField() {
        Method getter = Methods.getGetterMethodByField(SampleBean.class, "name");
        Assert.assertNotNull(getter);
        Assert.assertEquals("getName", getter.getName());

        Method boolGetter = Methods.getGetterMethodByField(SampleBean.class, "active");
        Assert.assertNotNull(boolGetter);
        Assert.assertEquals("isActive", boolGetter.getName());
    }

    @Test
    public void testGetSetterMethodByField() {
        Method setter = Methods.getSetterMethodByField(SampleBean.class, "name");
        Assert.assertNotNull(setter);
        Assert.assertEquals("setName", setter.getName());
    }

    @Test
    public void testGetAccessibleMethodByName() {
        Method method = Methods.getAccessibleMethod(SampleBean.class, "hello");
        Assert.assertNotNull(method);
        Assert.assertEquals("hello", method.getName());
    }

    @Test
    public void testGetAccessibleMethodByNameAndArgsNum() {
        Method method = Methods.getAccessibleMethod(SampleBean.class, "add", 2);
        Assert.assertNotNull(method);
        Assert.assertEquals("add", method.getName());
    }

    @Test
    public void testGetAccessibleMethodByParamTypes() {
        Method method = Methods.getAccessibleMethod(SampleBean.class, "hello", String.class);
        Assert.assertNotNull(method);
        Assert.assertEquals("hello", method.getName());
    }

    @Test
    public void testGetAccessibleMethods() {
        List<Method> methods = Methods.getAccessibleMethods(SampleBean.class);
        Assert.assertNotNull(methods);
        Assert.assertTrue(methods.size() > 0);
    }

    @Test
    public void testGetAccessibleMethodsByName() {
        List<Method> methods = Methods.getAccessibleMethods(SampleBean.class, "hello");
        Assert.assertNotNull(methods);
        Assert.assertEquals(1, methods.size());
    }

    @Test
    public void testGetAccessibleMethodMap() {
        Map<String, Method> map = Methods.getAccessibleMethodMap(SampleBean.class);
        Assert.assertNotNull(map);
        Assert.assertTrue(map.containsKey("hello"));
        Assert.assertTrue(map.containsKey("add"));
    }

    @Test
    public void testGetStaticMethods() {
        List<Method> statics = Methods.getStaticMethods(SampleBean.class);
        Assert.assertNotNull(statics);
        Assert.assertTrue(statics.stream().anyMatch(m -> m.getName().equals("staticMethod")));
    }

    @Test
    public void testInvokeMethod() {
        SampleBean bean = new SampleBean();
        String result = Methods.invokeMethod(bean, "hello", "world");
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void testInvokeMethodWithTypes() {
        SampleBean bean = new SampleBean();
        String result = Methods.invokeMethod(bean, "hello", new Class[]{String.class}, "test");
        Assert.assertEquals("hello test", result);
    }

    @Test
    public void testInvokeGetter() {
        SampleBean bean = new SampleBean();
        bean.setName("testName");
        String name = Methods.invokeGetter(bean, "name");
        Assert.assertEquals("testName", name);
    }

    @Test
    public void testInvokeSetter() {
        SampleBean bean = new SampleBean();
        Methods.invokeSetter(bean, "name", "value");
        Assert.assertEquals("value", bean.getName());
    }

    @Test
    public void testInvokeSetterInfer() {
        SampleBean bean = new SampleBean();
        Methods.invokeSetterInfer(bean, "age", 25);
        Assert.assertEquals(25, bean.getAge());
    }

    @Test
    public void testInvokeMethodInfer() {
        SampleBean bean = new SampleBean();
        int result = Methods.invokeMethodInfer(bean, "add", 3, 5);
        Assert.assertEquals(8, result);
    }

    @Test
    public void testGetGetterMethodByCache() {
        Method method = Methods.getGetterMethodByCache(SampleBean.class, "name");
        Assert.assertNotNull(method);
        Assert.assertEquals("getName", method.getName());
    }

    @Test
    public void testGetSetterMethodByCache() {
        Method method = Methods.getSetterMethodByCache(SampleBean.class, "name");
        Assert.assertNotNull(method);
        Assert.assertEquals("setName", method.getName());
    }

    @Test
    public void testGetGetterMethodsByCache() {
        List<Method> methods1 = Methods.getGetterMethodsByCache(SampleBean.class);
        List<Method> methods2 = Methods.getGetterMethodsByCache(SampleBean.class);
        Assert.assertSame(methods1, methods2);
    }

    @Test
    public void testGetSetterMethodsByCache() {
        List<Method> methods1 = Methods.getSetterMethodsByCache(SampleBean.class);
        List<Method> methods2 = Methods.getSetterMethodsByCache(SampleBean.class);
        Assert.assertSame(methods1, methods2);
    }

    @Test
    public void testInvokeSetterChain() {
        SampleBean bean = new SampleBean();
        Methods.invokeSetterChain(bean, "name.active", "chainName", true);
        Assert.assertEquals("chainName", bean.getName());
        Assert.assertTrue(bean.isActive());
    }
}

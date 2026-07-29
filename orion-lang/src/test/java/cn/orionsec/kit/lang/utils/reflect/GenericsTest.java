package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.utils.reflect.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Generics 单元测试
 */
public class GenericsTest {

    static class GenericBean {
        private List<String> names;
        private Map<String, Integer> scores;
        private String plain;

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }

        public Map<String, Integer> getScores() {
            return scores;
        }

        public void setScores(Map<String, Integer> scores) {
            this.scores = scores;
        }

        public String getPlain() {
            return plain;
        }

        public void setPlain(String plain) {
            this.plain = plain;
        }
    }

    static abstract class GenericParent<T> {
        public abstract T getValue();
    }

    static class StringChild extends GenericParent<String> {
        @Override
        public String getValue() {
            return "hello";
        }
    }

    interface GenericInterface<K, V> {
    }

    static class ImplClass implements GenericInterface<String, Integer> {
    }

    @Test
    public void testGetFieldGenericType() throws Exception {
        Field field = GenericBean.class.getDeclaredField("names");
        Class<?> type = Generics.getFieldGenericType(field, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testGetFieldGenericTypes() throws Exception {
        Field field = GenericBean.class.getDeclaredField("scores");
        Class<?>[] types = Generics.getFieldGenericTypes(field);
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.length);
        Assert.assertEquals(String.class, types[0]);
        Assert.assertEquals(Integer.class, types[1]);
    }

    @Test
    public void testGetFieldGenericTypesPlain() throws Exception {
        Field field = GenericBean.class.getDeclaredField("plain");
        Class<?>[] types = Generics.getFieldGenericTypes(field);
        Assert.assertNull(types);
    }

    @Test
    public void testGetMethodParameterGenericType() throws Exception {
        Method method = GenericBean.class.getDeclaredMethod("setNames", List.class);
        Class<?> type = Generics.getMethodParameterGenericType(method, 0, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testGetMethodParameterGenericTypes() throws Exception {
        Method method = GenericBean.class.getDeclaredMethod("setScores", Map.class);
        Class<?>[] types = Generics.getMethodParameterGenericTypes(method, 0);
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.length);
        Assert.assertEquals(String.class, types[0]);
        Assert.assertEquals(Integer.class, types[1]);
    }

    @Test
    public void testGetMethodReturnGenericType() throws Exception {
        Method method = GenericBean.class.getDeclaredMethod("getNames");
        Class<?> type = Generics.getMethodReturnGenericType(method, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testGetMethodReturnGenericTypes() throws Exception {
        Method method = GenericBean.class.getDeclaredMethod("getScores");
        Class<?>[] types = Generics.getMethodReturnGenericTypes(method);
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.length);
    }

    @Test
    public void testGetSuperClassGenericType() {
        Class<?> type = Generics.getSuperClassGenericType(StringChild.class, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testGetSuperClassGenericTypes() {
        Class<?>[] types = Generics.getSuperClassGenericTypes(StringChild.class);
        Assert.assertNotNull(types);
        Assert.assertEquals(1, types.length);
        Assert.assertEquals(String.class, types[0]);
    }

    @Test
    public void testGetInterfaceGenericType() {
        Class<?> type = Generics.getInterfaceGenericType(ImplClass.class, GenericInterface.class, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testGetInterfaceGenericTypes() {
        Class<?>[] types = Generics.getInterfaceGenericTypes(ImplClass.class, GenericInterface.class);
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.length);
        Assert.assertEquals(String.class, types[0]);
        Assert.assertEquals(Integer.class, types[1]);
    }

    @Test
    public void testGetInterfaceGenericTypesAll() {
        Map<Class<?>, Class<?>[]> map = Generics.getInterfaceGenericTypes(ImplClass.class);
        Assert.assertNotNull(map);
        Assert.assertTrue(map.containsKey(GenericInterface.class));
    }

    @Test
    public void testGetClassGenericType() {
        TypeReference<List<String>> ref = new TypeReference<List<String>>() {
        };
        Class<?> type = Generics.getClassGenericType(ref, 0);
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testGetClassGenericTypes() {
        TypeReference<Map<String, Integer>> ref = new TypeReference<Map<String, Integer>>() {
        };
        Class<?>[] types = Generics.getClassGenericTypes(ref);
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.length);
        Assert.assertEquals(String.class, types[0]);
        Assert.assertEquals(Integer.class, types[1]);
    }
}

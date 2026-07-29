package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.utils.reflect.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Types 单元测试
 */
public class TypesTest {

    static class GenericClass<T> {
    }

    static class StringGenericClass extends GenericClass<String> {
    }

    interface GenericInterface<K, V> {
    }

    static class ImplClass implements GenericInterface<String, Integer> {
    }

    @Test
    public void testGetTypeParameterizedTypes() {
        TypeReference<List<String>> ref = new TypeReference<List<String>>() {
        };
        Class<?>[] types = Types.getTypeParameterizedTypes(ref.getType());
        Assert.assertNotNull(types);
        Assert.assertEquals(1, types.length);
        Assert.assertEquals(String.class, types[0]);
    }

    @Test
    public void testGetTypeParameterizedTypesNull() {
        Class<?>[] types = Types.getTypeParameterizedTypes(String.class);
        Assert.assertNull(types);
    }

    @Test
    public void testGetTypeParameterizedTypesMap() {
        TypeReference<Map<String, Integer>> ref = new TypeReference<Map<String, Integer>>() {
        };
        Class<?>[] types = Types.getTypeParameterizedTypes(ref.getType());
        Assert.assertNotNull(types);
        Assert.assertEquals(2, types.length);
        Assert.assertEquals(String.class, types[0]);
        Assert.assertEquals(Integer.class, types[1]);
    }

    @Test
    public void testToParameterizedType() {
        TypeReference<List<String>> ref = new TypeReference<List<String>>() {
        };
        ParameterizedType pt = Types.toParameterizedType(ref.getType());
        Assert.assertNotNull(pt);
        Assert.assertEquals(List.class, pt.getRawType());
    }

    @Test
    public void testToParameterizedTypeFromClass() {
        // StringGenericClass extends GenericClass<String>
        ParameterizedType pt = Types.toParameterizedType(StringGenericClass.class);
        Assert.assertNotNull(pt);
    }

    @Test
    public void testIsUnknown() {
        Assert.assertTrue(Types.isUnknown(null));
        Assert.assertFalse(Types.isUnknown(String.class));
    }

    @Test
    public void testGetTypeRawClass() {
        Assert.assertEquals(String.class, Types.getTypeRawClass(String.class));

        TypeReference<List<String>> ref = new TypeReference<List<String>>() {
        };
        Assert.assertEquals(List.class, Types.getTypeRawClass(ref.getType()));
    }

    @Test
    public void testGetTypeRawClassNull() {
        Assert.assertNull(Types.getTypeRawClass(null));
    }

    @Test
    public void testGetTypeArgument() {
        TypeReference<List<String>> ref = new TypeReference<List<String>>() {
        };
        Type arg = Types.getTypeArgument(ref.getType());
        Assert.assertNotNull(arg);
        Assert.assertEquals(String.class, arg);
    }

    @Test
    public void testGetTypeArguments() {
        TypeReference<Map<String, Integer>> ref = new TypeReference<Map<String, Integer>>() {
        };
        Type[] args = Types.getTypeArguments(ref.getType());
        Assert.assertNotNull(args);
        Assert.assertEquals(2, args.length);
    }

    @Test
    public void testGetTypeArgumentByIndex() {
        TypeReference<Map<String, Integer>> ref = new TypeReference<Map<String, Integer>>() {
        };
        Type arg0 = Types.getTypeArgument(ref.getType(), 0);
        Type arg1 = Types.getTypeArgument(ref.getType(), 1);
        Assert.assertEquals(String.class, arg0);
        Assert.assertEquals(Integer.class, arg1);
    }
}

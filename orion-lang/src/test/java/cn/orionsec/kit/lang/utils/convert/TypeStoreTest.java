package cn.orionsec.kit.lang.utils.convert;

import cn.orionsec.kit.lang.function.Conversion;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TypeStoreTest {

    @Test
    public void testGetStore() {
        TypeStore store = TypeStore.getStore();
        assertNotNull(store);
        assertSame(TypeStore.STORE, store);
    }

    @Test
    public void testRegisterAndGet() {
        TypeStore store = new TypeStore();
        Conversion<String, Integer> conversion = Integer::valueOf;
        store.register(String.class, Integer.class, conversion);
        Conversion<String, Integer> result = store.get(String.class, Integer.class);
        assertNotNull(result);
        assertEquals(Integer.valueOf(123), result.apply("123"));
    }

    @Test
    public void testToBasicConversion() {
        // Using the global store which has basic type conversions
        Integer result = TypeStore.STORE.to("123", Integer.class);
        assertNotNull(result);
        assertEquals(Integer.valueOf(123), result);
    }

    @Test
    public void testToIntFromString() {
        Integer result = TypeStore.STORE.to("42", Integer.class);
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testToLongFromString() {
        Long result = TypeStore.STORE.to("999", Long.class);
        assertEquals(Long.valueOf(999), result);
    }

    @Test
    public void testToDoubleFromString() {
        Double result = TypeStore.STORE.to("3.14", Double.class);
        assertEquals(3.14, result, 0.001);
    }

    @Test
    public void testToBooleanFromString() {
        Boolean result = TypeStore.STORE.to("true", Boolean.class);
        assertTrue(result);
    }

    @Test
    public void testCanConvert() {
        assertTrue(TypeStore.canConvert(String.class, Integer.class));
        assertTrue(TypeStore.canConvert(String.class, Long.class));
        assertTrue(TypeStore.canConvert(String.class, Boolean.class));
    }

    @Test
    public void testCanDirectConvert() {
        // Same class
        assertTrue(TypeStore.canDirectConvert(String.class, String.class));
        // To Object
        assertTrue(TypeStore.canDirectConvert(String.class, Object.class));
    }

    @Test
    public void testCanDirectConvertPrimitive() {
        // int -> Integer (wrapping)
        assertTrue(TypeStore.canDirectConvert(int.class, Integer.class));
    }

    @Test
    public void testGetSuitableClasses() {
        Set<Class<?>> classes = TypeStore.STORE.getSuitableClasses(String.class);
        assertNotNull(classes);
        assertTrue(classes.size() > 0);
    }

    @Test
    public void testGetSuitableConversion() {
        Map<Class<?>, Conversion<?, ?>> conversions = TypeStore.STORE.getSuitableConversion(String.class);
        assertNotNull(conversions);
        assertTrue(conversions.size() > 0);
    }

    @Test
    public void testGetConversionMapping() {
        TypeStore store = new TypeStore();
        assertNotNull(store.getConversionMapping());
    }
}

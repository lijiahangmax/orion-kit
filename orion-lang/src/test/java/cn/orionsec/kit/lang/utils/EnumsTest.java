package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EnumsTest {

    private enum TestEnum {
        VALUE_A,
        VALUE_B,
        VALUE_C
    }

    @Test
    public void testIsEnumClass() {
        assertTrue(Enums.isEnum(TestEnum.class));
        assertFalse(Enums.isEnum(String.class));
    }

    @Test
    public void testIsEnumObject() {
        assertTrue(Enums.isEnum(TestEnum.VALUE_A));
        assertFalse(Enums.isEnum("hello"));
    }

    @Test
    public void testToString() {
        assertEquals("VALUE_A", Enums.toString(TestEnum.VALUE_A));
        assertNull(Enums.toString(null));
    }

    @Test
    public void testGetEnumByIndex() {
        assertEquals(TestEnum.VALUE_A, Enums.getEnum(TestEnum.class, 0));
        assertEquals(TestEnum.VALUE_B, Enums.getEnum(TestEnum.class, 1));
        assertNull(Enums.getEnum(TestEnum.class, 100));
    }

    @Test
    public void testGetEnumByIndexWithDefault() {
        assertEquals(TestEnum.VALUE_C, Enums.getEnum(TestEnum.class, 100, TestEnum.VALUE_C));
    }

    @Test
    public void testGetEnumByName() {
        assertEquals(TestEnum.VALUE_A, Enums.getEnum(TestEnum.class, "VALUE_A"));
    }

    @Test
    public void testGetEnumByNameWithDefault() {
        assertEquals(TestEnum.VALUE_A, Enums.getEnum(TestEnum.class, "NOT_EXIST", TestEnum.VALUE_A));
        assertEquals(TestEnum.VALUE_A, Enums.getEnum(TestEnum.class, "", TestEnum.VALUE_A));
    }

    @Test
    public void testGetNames() {
        List<String> names = Enums.getNames(TestEnum.class);
        assertEquals(3, names.size());
        assertTrue(names.contains("VALUE_A"));
    }

    @Test
    public void testGetEnumMap() {
        Map<String, TestEnum> map = Enums.getEnumMap(TestEnum.class);
        assertEquals(3, map.size());
        assertEquals(TestEnum.VALUE_A, map.get("VALUE_A"));
    }

    @Test
    public void testContains() {
        assertTrue(Enums.contains(TestEnum.class, "VALUE_A"));
        assertFalse(Enums.contains(TestEnum.class, "NOT_EXIST"));
    }

    @Test
    public void testEqualsMethod() {
        assertTrue(Enums.equals(TestEnum.VALUE_A, "VALUE_A"));
        assertFalse(Enums.equals(TestEnum.VALUE_A, "value_a"));
    }

    @Test
    public void testEqualsIgnoreCase() {
        assertTrue(Enums.equalsIgnoreCase(TestEnum.VALUE_A, "value_a"));
        assertFalse(Enums.equalsIgnoreCase(TestEnum.VALUE_A, "VALUE_B"));
    }
}

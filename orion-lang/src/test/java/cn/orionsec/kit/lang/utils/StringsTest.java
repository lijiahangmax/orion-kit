package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void testIsBlank() {
        assertTrue(Strings.isBlank(null));
        assertTrue(Strings.isBlank(""));
        assertTrue(Strings.isBlank("   "));
        assertFalse(Strings.isBlank("abc"));
    }

    @Test
    public void testIsNotBlank() {
        assertTrue(Strings.isNotBlank("abc"));
        assertFalse(Strings.isNotBlank(null));
        assertFalse(Strings.isNotBlank(""));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Strings.isEmpty((CharSequence) null));
        assertTrue(Strings.isEmpty(""));
        assertFalse(Strings.isEmpty(" "));
        assertFalse(Strings.isEmpty("abc"));
    }

    @Test
    public void testIsNotEmpty() {
        assertTrue(Strings.isNotEmpty("abc"));
        assertFalse(Strings.isNotEmpty(null));
        assertFalse(Strings.isNotEmpty(""));
    }

    @Test
    public void testLength() {
        assertEquals(0, Strings.length(null));
        assertEquals(0, Strings.length(""));
        assertEquals(3, Strings.length("abc"));
    }

    @Test
    public void testDef() {
        assertEquals("abc", Strings.def("abc"));
        assertEquals("", Strings.def(null));
        assertEquals("", Strings.def(""));
    }

    @Test
    public void testDefWithDefault() {
        assertEquals("abc", Strings.def("abc", "default"));
        assertEquals("default", Strings.def(null, "default"));
    }

    @Test
    public void testRepeat() {
        assertEquals("aaa", Strings.repeat("a", 3));
        assertEquals("", Strings.repeat("a", 0));
    }

    @Test
    public void testRepeatChar() {
        assertEquals("***", Strings.repeat('*', 3));
    }

    @Test
    public void testIsAllBlank() {
        assertTrue(Strings.isAllBlank(null, "", "  "));
        assertFalse(Strings.isAllBlank(null, "abc"));
        assertTrue(Strings.isAllBlank((String[]) null));
    }

    @Test
    public void testIsNoneBlank() {
        assertTrue(Strings.isNoneBlank("a", "b"));
        assertFalse(Strings.isNoneBlank("a", null));
        assertFalse(Strings.isNoneBlank((String[]) null));
    }

    @Test
    public void testStr() {
        assertNull(Strings.str(null));
        assertEquals("hello", Strings.str("hello"));
        assertEquals("abc", Strings.str(new char[]{'a', 'b', 'c'}));
    }

    @Test
    public void testSplit() {
        assertArrayEquals(new String[]{"a", "b", "c"}, Strings.split("a,b,c"));
        assertArrayEquals(new String[]{"a", "b", "c"}, Strings.split("a-b-c", "-"));
        // 保留空串与顺序
        assertArrayEquals(new String[]{"a", "", "b"}, Strings.split("a,,b"));
        assertArrayEquals(new String[]{"", "a", ""}, Strings.split(",a,"));
        // 多字符字面量分隔符 (非正则)
        assertArrayEquals(new String[]{"a", "b"}, Strings.split("a::b", "::"));
        // "." 作为字面量而非正则元字符
        assertArrayEquals(new String[]{"a", "b", "c"}, Strings.split("a.b.c", "."));
        // 无分隔符命中
        assertArrayEquals(new String[]{"abc"}, Strings.split("abc", ","));
    }

    @Test
    public void testSplitEdgeCases() {
        // 空输入返回空数组
        assertArrayEquals(new String[0], Strings.split(null));
        assertArrayEquals(new String[0], Strings.split(""));
        assertArrayEquals(new String[0], Strings.split(null, ","));
        // 分隔符为空返回单元素数组
        assertArrayEquals(new String[]{"abc"}, Strings.split("abc", null));
        assertArrayEquals(new String[]{"abc"}, Strings.split("abc", ""));
    }
}

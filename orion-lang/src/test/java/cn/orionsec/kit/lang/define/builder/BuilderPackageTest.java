package cn.orionsec.kit.lang.define.builder;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * builder 包单元测试
 */
public class BuilderPackageTest {

    // ==================== Builder ====================

    @Test
    public void testBuilderBasic() {
        Map<String, Object> result = Builder.of(HashMap<String, Object>::new)
                .with(Map::put, "name", (Object) "test")
                .with(Map::put, "age", (Object) 18)
                .build();
        assertEquals("test", result.get("name"));
        assertEquals(18, result.get("age"));
    }

    @Test
    public void testBuilderEmpty() {
        Map<String, String> result = Builder.of(HashMap<String, String>::new)
                .build();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testBuilderOf() {
        StringBuilder sb = Builder.of(StringBuilder::new)
                .with(StringBuilder::append, "hello")
                .with(StringBuilder::append, " world")
                .build();
        assertEquals("hello world", sb.toString());
    }

    // ==================== StringJoiner ====================

    @Test
    public void testStringJoinerBasic() {
        String result = StringJoiner.of(",")
                .with("a")
                .with("b")
                .with("c")
                .build();
        assertEquals("a,b,c", result);
    }

    @Test
    public void testStringJoinerWithPrefixSuffix() {
        String result = StringJoiner.of(",", "[", "]")
                .with("1")
                .with("2")
                .with("3")
                .build();
        assertEquals("[1,2,3]", result);
    }

    @Test
    public void testStringJoinerSkipNull() {
        String result = StringJoiner.of(",")
                .skipNull()
                .with("a")
                .with((String) null)
                .with("c")
                .build();
        assertEquals("a,c", result);
    }

    @Test
    public void testStringJoinerSkipEmpty() {
        String result = StringJoiner.of(",")
                .skipEmpty()
                .with("a")
                .with("")
                .with("c")
                .build();
        assertEquals("a,c", result);
    }

    @Test
    public void testStringJoinerSkipBlank() {
        String result = StringJoiner.of(",")
                .skipBlank()
                .with("a")
                .with("  ")
                .with("c")
                .build();
        assertEquals("a,c", result);
    }

    @Test
    public void testStringJoinerNoDelimiter() {
        String result = StringJoiner.of()
                .with("a")
                .with("b")
                .with("c")
                .build();
        assertEquals("abc", result);
    }

    @Test
    public void testStringJoinerWrapper() {
        String result = StringJoiner.of(",")
                .wrapper(s -> "'" + s + "'")
                .with("a")
                .with("b")
                .build();
        assertEquals("'a','b'", result);
    }

    @Test
    public void testStringJoinerWithObject() {
        String result = StringJoiner.of("-")
                .with((Object) 1)
                .with((Object) 2)
                .with((Object) 3)
                .build();
        assertEquals("1-2-3", result);
    }
}

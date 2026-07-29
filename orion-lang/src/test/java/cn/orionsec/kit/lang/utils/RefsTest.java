package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class RefsTest {

    @Test
    public void testJsonAndUnref() {
        String json = Refs.json("hello");
        assertNotNull(json);
        assertTrue(json.contains("hello"));
    }

    @Test
    public void testUnrefNull() {
        assertNull(Refs.unref(null));
    }

    @Test
    public void testUnrefWithType() {
        String json = Refs.json(123);
        assertNotNull(json);
        Object value = Refs.unref(json);
        assertNotNull(value);
    }

    @Test
    public void testUnrefToString() {
        String json = Refs.json("world");
        String result = Refs.unrefToString(json);
        assertEquals("world", result);
    }
}

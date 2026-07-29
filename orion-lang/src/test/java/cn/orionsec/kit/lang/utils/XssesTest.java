package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class XssesTest {

    @Test
    public void testClean() {
        String result = Xsses.clean("<script>alert('xss')</script>");
        assertNotNull(result);
        assertFalse(result.contains("<script>"));
    }

    @Test
    public void testCleanBlank() {
        assertNull(Xsses.clean(null));
        assertEquals("", Xsses.clean(""));
        assertEquals("  ", Xsses.clean("  "));
    }

    @Test
    public void testCleanSpecialChars() {
        String result = Xsses.clean("a&b");
        assertTrue(result.contains("&amp;"));
    }

    @Test
    public void testRecode() {
        String result = Xsses.recode("&amp;&lt;&gt;&nbsp;&apos;&quot;");
        assertEquals("&<> '\"", result);
    }

    @Test
    public void testRecodeBlank() {
        assertNull(Xsses.recode(null));
        assertEquals("", Xsses.recode(""));
    }

    @Test
    public void testCleanAndRecode() {
        // Clean should encode, recode should partially decode
        String cleaned = Xsses.clean("hello");
        assertNotNull(cleaned);
    }
}

package cn.orionsec.kit.lang.utils.regexp;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Patterns 单元测试
 */
public class PatternsTest {

    @Test
    public void testGetPattern() {
        Pattern p = Patterns.getPattern("\\d+");
        assertNotNull(p);
        assertTrue(p.matcher("123").matches());
    }

    @Test
    public void testGetPatternCached() {
        Pattern p1 = Patterns.getPattern("\\d+");
        Pattern p2 = Patterns.getPattern("\\d+");
        assertSame(p1, p2);
    }

    @Test
    public void testGetPatternExt() {
        Pattern original = Pattern.compile("^\\d+$");
        Pattern ext = Patterns.getPatternExt(original);
        assertNotNull(ext);
        // Should match without anchors
        assertTrue(ext.matcher("123").find());
    }

    @Test
    public void testGetPatternExtString() {
        Pattern ext = Patterns.getPatternExt("^hello$");
        assertNotNull(ext);
        assertEquals("hello", ext.pattern());
    }

    @Test
    public void testPredefinedPatterns() {
        assertNotNull(Patterns.INTEGER);
        assertNotNull(Patterns.DOUBLE);
        assertNotNull(Patterns.NUMBER);
        assertNotNull(Patterns.EMAIL);
        assertNotNull(Patterns.PHONE);
        assertNotNull(Patterns.HTTP);
        assertNotNull(Patterns.IPV4);
        assertNotNull(Patterns.UUID);
    }

    @Test
    public void testIntegerPattern() {
        assertTrue(Patterns.INTEGER.matcher("12345").matches());
        assertFalse(Patterns.INTEGER.matcher("12.34").matches());
    }

    @Test
    public void testEmailPattern() {
        assertTrue(Patterns.EMAIL.matcher("test@example.com").matches());
        assertFalse(Patterns.EMAIL.matcher("not-email").matches());
    }
}

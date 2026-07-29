package cn.orionsec.kit.lang.utils.regexp;

import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Matches 和 Patterns 单元测试
 */
public class MatchesTest {

    @Test
    public void testFindNum() {
        int count = Matches.findNum("hello world hello", "hello");
        assertEquals(2, count);
    }

    @Test
    public void testFindNumNoMatch() {
        int count = Matches.findNum("hello world", "xyz");
        assertEquals(0, count);
    }

    @Test
    public void testTestString() {
        assertTrue(Matches.test("12345", "\\d+"));
        assertFalse(Matches.test("abc", "\\d+"));
    }

    @Test
    public void testTestPattern() {
        Pattern p = Pattern.compile("\\d+");
        assertTrue(Matches.test("12345", p));
        assertFalse(Matches.test("abc", p));
    }

    @Test
    public void testExtGroup() {
        String result = Matches.extGroup("hello 123 world", "\\d+");
        assertEquals("123", result);
    }

    @Test
    public void testExtGroupNoMatch() {
        String result = Matches.extGroup("hello world", "\\d+");
        assertNull(result);
    }

    @Test
    public void testExtGroups() {
        List<String> results = Matches.extGroups("a1b2c3", "\\d");
        assertEquals(3, results.size());
        assertEquals("1", results.get(0));
        assertEquals("2", results.get(1));
        assertEquals("3", results.get(2));
    }

    @Test
    public void testExtGroupsPattern() {
        Pattern p = Pattern.compile("\\d+");
        List<String> results = Matches.extGroups("hello 123 world 456", p);
        assertEquals(2, results.size());
        assertEquals("123", results.get(0));
        assertEquals("456", results.get(1));
    }

    @Test
    public void testIsInteger() {
        assertTrue(Matches.isInteger("12345"));
        assertTrue(Matches.isInteger("0"));
        assertFalse(Matches.isInteger("12.34"));
        assertFalse(Matches.isInteger("abc"));
    }

    @Test
    public void testIsDouble() {
        assertTrue(Matches.isDouble("12.34"));
        assertFalse(Matches.isDouble("abc"));
    }

    @Test
    public void testIsNumber() {
        assertTrue(Matches.isNumber("12345"));
        assertTrue(Matches.isNumber("12.34"));
        assertFalse(Matches.isNumber("abc"));
    }

    @Test
    public void testIsIpv4() {
        assertTrue(Matches.isIpv4("192.168.1.1"));
        assertTrue(Matches.isIpv4("0.0.0.0"));
        assertFalse(Matches.isIpv4("999.999.999.999"));
        assertFalse(Matches.isIpv4("abc"));
    }

    @Test
    public void testIsEmail() {
        assertTrue(Matches.isEmail("test@example.com"));
        assertFalse(Matches.isEmail("invalid-email"));
    }

    @Test
    public void testIsPhone() {
        assertTrue(Matches.isPhone("13800138000"));
        assertFalse(Matches.isPhone("12345"));
    }

    @Test
    public void testIsHttp() {
        assertTrue(Matches.isHttp("http://example.com"));
        assertTrue(Matches.isHttp("https://example.com/path"));
        assertFalse(Matches.isHttp("ftp://example.com"));
    }

    @Test
    public void testIsCreditCode() {
        // 合法的统一社会信用代码格式
        assertTrue(Matches.isCreditCode("91350100M000100Y43"));
    }
}

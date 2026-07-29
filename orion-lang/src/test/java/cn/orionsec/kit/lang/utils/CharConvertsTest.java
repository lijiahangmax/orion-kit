package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharConvertsTest {

    @Test
    public void testToHtmlEntity() {
        String result = CharConverts.toHtmlEntity("AB");
        assertEquals("&#65;&#66;", result);
    }

    @Test
    public void testFromHtmlEntity() {
        String result = CharConverts.fromHtmlEntity("&#65;&#66;");
        assertEquals("AB", result);
    }

    @Test
    public void testToUnicode() {
        String result = CharConverts.toUnicode("A", true);
        assertEquals("\\u0041", result);
    }

    @Test
    public void testToUnicodeNoConvertNumber() {
        String result = CharConverts.toUnicode("AB", false);
        assertEquals("AB", result);
    }

    @Test
    public void testToUnicodeChar() {
        String result = CharConverts.toUnicodeChar('A');
        assertEquals("\\u0041", result);
    }

    @Test
    public void testFromUnicode() {
        String result = CharConverts.fromUnicode("\\u0041\\u0042");
        assertEquals("AB", result);
    }

    @Test
    public void testFromUnicodeChar() {
        char result = CharConverts.fromUnicodeChar("\\u0041");
        assertEquals('A', result);
    }

    @Test
    public void testToHex() {
        String result = CharConverts.toHex("A");
        assertEquals("%41", result);
    }

    @Test
    public void testFromHex() {
        String result = CharConverts.fromHex("%41%42");
        assertEquals("AB", result);
    }

    @Test
    public void testConvertControlCodeToUnicode() {
        // tab is control char (code 9)
        String result = CharConverts.convertControlCodeToUnicode("\t");
        assertEquals("\\u0009", result);
        // Normal char should not be converted
        String normal = CharConverts.convertControlCodeToUnicode("A");
        assertEquals("A", normal);
    }
}

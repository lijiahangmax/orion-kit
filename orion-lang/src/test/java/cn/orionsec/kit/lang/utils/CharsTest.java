package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharsTest {

    @Test
    public void testIsAscii() {
        assertTrue(Chars.isAscii('A'));
        assertTrue(Chars.isAscii('\0'));
        assertTrue(Chars.isAscii((char) 127));
        assertFalse(Chars.isAscii((char) 128));
    }

    @Test
    public void testIsAsciiPrintable() {
        assertTrue(Chars.isAsciiPrintable(' '));
        assertTrue(Chars.isAsciiPrintable('~'));
        assertFalse(Chars.isAsciiPrintable((char) 31));
        assertFalse(Chars.isAsciiPrintable((char) 127));
    }

    @Test
    public void testIsAsciiControl() {
        assertTrue(Chars.isAsciiControl((char) 0));
        assertTrue(Chars.isAsciiControl((char) 31));
        assertTrue(Chars.isAsciiControl((char) 127));
        assertFalse(Chars.isAsciiControl(' '));
    }

    @Test
    public void testIsLetter() {
        assertTrue(Chars.isLetter('a'));
        assertTrue(Chars.isLetter('Z'));
        assertFalse(Chars.isLetter('0'));
        assertFalse(Chars.isLetter(' '));
    }

    @Test
    public void testIsLetterUpper() {
        assertTrue(Chars.isLetterUpper('A'));
        assertTrue(Chars.isLetterUpper('Z'));
        assertFalse(Chars.isLetterUpper('a'));
    }

    @Test
    public void testIsLetterLower() {
        assertTrue(Chars.isLetterLower('a'));
        assertTrue(Chars.isLetterLower('z'));
        assertFalse(Chars.isLetterLower('A'));
    }

    @Test
    public void testIsNumber() {
        assertTrue(Chars.isNumber('0'));
        assertTrue(Chars.isNumber('9'));
        assertFalse(Chars.isNumber('a'));
    }

    @Test
    public void testIsHexChar() {
        assertTrue(Chars.isHexChar('0'));
        assertTrue(Chars.isHexChar('a'));
        assertTrue(Chars.isHexChar('F'));
        assertFalse(Chars.isHexChar('g'));
    }

    @Test
    public void testEquals() {
        assertTrue(Chars.equals('a', 'a', false));
        assertFalse(Chars.equals('a', 'A', false));
        assertTrue(Chars.equals('a', 'A', true));
    }

    @Test
    public void testIsBlankChar() {
        assertTrue(Chars.isBlankChar(' '));
        assertTrue(Chars.isBlankChar('\t'));
        assertFalse(Chars.isBlankChar('a'));
    }

    @Test
    public void testDigit16() {
        assertEquals(10, Chars.digit16('a'));
        assertEquals(15, Chars.digit16('f'));
        assertEquals(0, Chars.digit16('0'));
    }
}

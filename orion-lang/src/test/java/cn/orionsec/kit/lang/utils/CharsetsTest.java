package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class CharsetsTest {

    @Test
    public void testConstants() {
        assertNotNull(Charsets.UTF_8);
        assertNotNull(Charsets.GBK);
        assertNotNull(Charsets.GB_2312);
        assertNotNull(Charsets.ISO_8859_1);
        assertEquals("UTF-8", Charsets.UTF_8.name());
    }

    @Test
    public void testOf() {
        Charset charset = Charsets.of("UTF-8");
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testIsSupported() {
        assertTrue(Charsets.isSupported("UTF-8"));
        assertTrue(Charsets.isSupported("GBK"));
        assertFalse(Charsets.isSupported("NOT_EXIST"));
        assertFalse(Charsets.isSupported(null));
        assertFalse(Charsets.isSupported(""));
        assertFalse(Charsets.isSupported("  "));
    }
}

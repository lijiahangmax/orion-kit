package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorsTest {

    @Test
    public void testToHex() {
        Color red = new Color(255, 0, 0);
        assertEquals("#FF0000", Colors.toHex(red));
        Color white = new Color(255, 255, 255);
        assertEquals("#FFFFFF", Colors.toHex(white));
    }

    @Test
    public void testToRgbColor() {
        int[] rgb = Colors.toRgbColor("#FF0000");
        assertNotNull(rgb);
        assertEquals(255, rgb[0]);
        assertEquals(0, rgb[1]);
        assertEquals(0, rgb[2]);
    }

    @Test
    public void testToRgbColorShort() {
        int[] rgb = Colors.toRgbColor("#F00");
        assertNotNull(rgb);
        assertEquals(255, rgb[0]);
        assertEquals(0, rgb[1]);
        assertEquals(0, rgb[2]);
    }

    @Test
    public void testToRgbColorNull() {
        assertNull(Colors.toRgbColor(null));
    }

    @Test
    public void testToColor() {
        Color c = Colors.toColor("#FF0000");
        assertNotNull(c);
        assertEquals(255, c.getRed());
        assertEquals(0, c.getGreen());
        assertEquals(0, c.getBlue());
    }

    @Test
    public void testIsDarkColor() {
        assertTrue(Colors.isDarkColor("#000000"));
        assertFalse(Colors.isDarkColor("#FFFFFF"));
    }

    @Test
    public void testAdjustColor() {
        String result = Colors.adjustColor("#808080", 10);
        assertNotNull(result);
        assertTrue(result.startsWith("#"));
    }
}

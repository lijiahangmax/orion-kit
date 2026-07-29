package cn.orionsec.kit.lang.utils.awt;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class FontsTest {

    @Before
    public void checkHeadless() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
    }

    @Test
    public void testGetMetrics() {
        Font font = new Font("Dialog", Font.PLAIN, 12);
        FontMetrics metrics = Fonts.getMetrics(font);
        assertNotNull(metrics);
        assertTrue(metrics.getHeight() > 0);
    }

    @Test
    public void testGetStringWidthChar() {
        Font font = new Font("Dialog", Font.PLAIN, 12);
        int width = Fonts.getStringWidth('A', font);
        assertTrue(width > 0);
    }

    @Test
    public void testGetStringWidthString() {
        Font font = new Font("Dialog", Font.PLAIN, 12);
        int width = Fonts.getStringWidth("Hello", font);
        assertTrue(width > 0);
    }

    @Test
    public void testGetHeight() {
        Font font = new Font("Dialog", Font.PLAIN, 14);
        int height = Fonts.getHeight(font);
        assertTrue(height > 0);
    }

    @Test
    public void testGetSupportedFonts() {
        String[] fonts = Fonts.getSupportedFonts();
        assertNotNull(fonts);
        assertTrue(fonts.length > 0);
    }

    @Test
    public void testGetWidthHeightPixel() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font("Dialog", Font.PLAIN, 12);
        try {
            int[] wh = Fonts.getWidthHeightPixel(g2d, font, "Test");
            assertNotNull(wh);
            assertEquals(2, wh.length);
            assertTrue(wh[0] > 0);
            assertTrue(wh[1] > 0);
        } finally {
            g2d.dispose();
        }
    }

    @Test
    public void testGetWidthHeightPixelWithPosition() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font("Dialog", Font.PLAIN, 12);
        try {
            int[] wh = Fonts.getWidthHeightPixel(g2d, font, "Test", 10, 10);
            assertNotNull(wh);
            assertEquals(2, wh.length);
            assertTrue(wh[0] > 0);
            assertTrue(wh[1] > 0);
        } finally {
            g2d.dispose();
        }
    }
}

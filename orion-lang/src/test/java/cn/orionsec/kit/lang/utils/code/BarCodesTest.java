package cn.orionsec.kit.lang.utils.code;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * BarCodes 单元测试
 */
public class BarCodesTest {

    private boolean headless;

    @Before
    public void setUp() {
        headless = GraphicsEnvironment.isHeadless();
    }

    @Test
    public void testEncodeToBytes() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        BarCodes barCodes = new BarCodes();
        byte[] result = barCodes.encode("123456789");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testEncodeToStream() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        BarCodes barCodes = new BarCodes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        barCodes.encode("123456789", out);
        assertTrue(out.size() > 0);
    }

    @Test
    public void testEncodeWithWords() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        BarCodes barCodes = new BarCodes();
        byte[] result = barCodes.encode("123456789", "test-label");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testDefaultSettings() {
        BarCodes barCodes = new BarCodes();
        assertEquals(200, barCodes.getWidth());
        assertEquals(30, barCodes.getHeight());
        assertEquals("png", barCodes.getSuffix());
    }

    @Test
    public void testSetters() {
        BarCodes barCodes = new BarCodes();
        barCodes.width(300);
        barCodes.height(50);
        barCodes.suffix("jpg");
        assertEquals(300, barCodes.getWidth());
        assertEquals(50, barCodes.getHeight());
        assertEquals("jpg", barCodes.getSuffix());
    }
}

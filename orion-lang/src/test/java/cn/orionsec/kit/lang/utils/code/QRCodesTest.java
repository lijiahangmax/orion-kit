package cn.orionsec.kit.lang.utils.code;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * QRCodes 单元测试
 */
public class QRCodesTest {

    private boolean headless;

    @Before
    public void setUp() {
        headless = GraphicsEnvironment.isHeadless();
    }

    @Test
    public void testEncodeToBytes() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        QRCodes qrCodes = new QRCodes();
        byte[] result = qrCodes.encode("https://example.com");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testEncodeToStream() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        QRCodes qrCodes = new QRCodes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        qrCodes.encode("https://example.com", out);
        assertTrue(out.size() > 0);
    }

    @Test
    public void testEncodeWithWords() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        QRCodes qrCodes = new QRCodes();
        byte[] result = qrCodes.encode("hello world", "label");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testEncodeDecode() {
        Assume.assumeFalse("Skipping AWT test in headless mode", headless);
        QRCodes qrCodes = new QRCodes();
        String content = "test-decode-content";
        byte[] encoded = qrCodes.encode(content);
        String decoded = qrCodes.decode(encoded);
        assertEquals(content, decoded);
    }

    @Test
    public void testDefaultSettings() {
        QRCodes qrCodes = new QRCodes();
        assertEquals(300, qrCodes.getWidth());
        assertEquals(300, qrCodes.getHeight());
        assertEquals(60, qrCodes.getLogoWidth());
        assertEquals(60, qrCodes.getLogoHeight());
    }

    @Test
    public void testSize() {
        QRCodes qrCodes = new QRCodes();
        qrCodes.size(500);
        assertEquals(500, qrCodes.getWidth());
        assertEquals(500, qrCodes.getHeight());
    }

    @Test
    public void testLogoSize() {
        QRCodes qrCodes = new QRCodes();
        qrCodes.logoSize(100);
        assertEquals(100, qrCodes.getLogoWidth());
        assertEquals(100, qrCodes.getLogoHeight());
    }
}

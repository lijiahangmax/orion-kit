package cn.orionsec.kit.lang.utils.awt;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ImagesTest {

    @Before
    public void checkHeadless() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
    }

    @Test
    public void testCopy() {
        BufferedImage original = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = original.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 100, 50);
        g.dispose();

        BufferedImage copy = Images.copy(original);
        assertNotNull(copy);
        assertEquals(100, copy.getWidth());
        assertEquals(50, copy.getHeight());
        assertEquals(original.getType(), copy.getType());
        // Verify pixel was copied
        assertEquals(original.getRGB(50, 25), copy.getRGB(50, 25));
    }

    @Test
    public void testCopyWithType() {
        BufferedImage original = new BufferedImage(80, 60, BufferedImage.TYPE_INT_ARGB);
        BufferedImage copy = Images.copy(original, BufferedImage.TYPE_INT_RGB);
        assertNotNull(copy);
        assertEquals(80, copy.getWidth());
        assertEquals(60, copy.getHeight());
        assertEquals(BufferedImage.TYPE_INT_RGB, copy.getType());
    }
}

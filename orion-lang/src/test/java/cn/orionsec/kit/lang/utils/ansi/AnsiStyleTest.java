package cn.orionsec.kit.lang.utils.ansi;

import cn.orionsec.kit.lang.utils.ansi.style.AnsiFont;
import cn.orionsec.kit.lang.utils.ansi.style.AnsiStyle;
import cn.orionsec.kit.lang.utils.ansi.style.AnsiStyleChain;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiBackground;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiBit24Color;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiBit8Color;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiForeground;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnsiStyleTest {

    private static final String CSI = "\033[";
    private static final String SGR = "m";

    @Test
    public void testAnsiFontBold() {
        assertEquals(CSI + "1" + SGR, AnsiFont.BOLD.toString());
    }

    @Test
    public void testAnsiFontItalic() {
        assertEquals(CSI + "3" + SGR, AnsiFont.ITALIC.toString());
    }

    @Test
    public void testAnsiFontUnderline() {
        assertEquals(CSI + "4" + SGR, AnsiFont.UNDERLINE.toString());
    }

    @Test
    public void testAnsiFontReset() {
        assertEquals(CSI + "0" + SGR, AnsiFont.RESET.toString());
    }

    @Test
    public void testAnsiFontGetCode() {
        assertEquals("1", AnsiFont.BOLD.getCode());
        assertEquals("4", AnsiFont.UNDERLINE.getCode());
    }

    @Test
    public void testForegroundBlack() {
        assertEquals(CSI + "30" + SGR, AnsiForeground.BLACK.toString());
    }

    @Test
    public void testForegroundRed() {
        assertEquals(CSI + "31" + SGR, AnsiForeground.RED.toString());
    }

    @Test
    public void testForegroundGreen() {
        assertEquals(CSI + "32" + SGR, AnsiForeground.GREEN.toString());
    }

    @Test
    public void testForegroundBrightWhite() {
        assertEquals(CSI + "97" + SGR, AnsiForeground.BRIGHT_WHITE.toString());
    }

    @Test
    public void testForegroundGetCode() {
        assertEquals("31", AnsiForeground.RED.getCode());
    }

    @Test
    public void testBackgroundBlack() {
        assertEquals(CSI + "40" + SGR, AnsiBackground.BLACK.toString());
    }

    @Test
    public void testBackgroundRed() {
        assertEquals(CSI + "41" + SGR, AnsiBackground.RED.toString());
    }

    @Test
    public void testBackgroundDefault() {
        assertEquals(CSI + "49" + SGR, AnsiBackground.DEFAULT.toString());
    }

    @Test
    public void testBackgroundGetCode() {
        assertEquals("40", AnsiBackground.BLACK.getCode());
    }

    @Test
    public void testBit8Foreground() {
        String result = AnsiBit8Color.foreground(196).toString();
        assertEquals(CSI + "38;5;196" + SGR, result);
    }

    @Test
    public void testBit8Background() {
        String result = AnsiBit8Color.background(82).toString();
        assertEquals(CSI + "48;5;82" + SGR, result);
    }

    @Test
    public void testBit24Foreground() {
        String result = AnsiBit24Color.foreground(255, 128, 0).toString();
        assertEquals(CSI + "38;2;255;128;0" + SGR, result);
    }

    @Test
    public void testBit24Background() {
        String result = AnsiBit24Color.background(0, 128, 255).toString();
        assertEquals(CSI + "48;2;0;128;255" + SGR, result);
    }

    @Test
    public void testBit24ForegroundRgbArray() {
        String result = AnsiBit24Color.foreground(new int[]{100, 200, 50}).toString();
        assertEquals(CSI + "38;2;100;200;50" + SGR, result);
    }

    @Test
    public void testBit24ForegroundHex() {
        String result = AnsiBit24Color.foreground("#FF0000").toString();
        assertEquals(CSI + "38;2;255;0;0" + SGR, result);
    }

    @Test
    public void testBit24BackgroundHex() {
        String result = AnsiBit24Color.background("#00FF00").toString();
        assertEquals(CSI + "48;2;0;255;0" + SGR, result);
    }

    @Test
    public void testStyleChain() {
        AnsiStyleChain chain = AnsiStyleChain.create(AnsiFont.BOLD);
        chain.and(AnsiForeground.RED);
        String result = chain.toString();
        assertEquals(CSI + "1;31" + SGR, result);
    }

    @Test
    public void testStyleChainGetCode() {
        AnsiStyleChain chain = AnsiStyleChain.create(AnsiFont.BOLD);
        chain.and(AnsiForeground.GREEN);
        assertEquals("1;32", chain.getCode());
    }

    @Test
    public void testStyleAndMethod() {
        AnsiStyle style = AnsiFont.BOLD.and(AnsiForeground.RED);
        assertTrue(style instanceof AnsiStyleChain);
        assertEquals(CSI + "1;31" + SGR, style.toString());
    }
}

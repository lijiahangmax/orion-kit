package cn.orionsec.kit.lang.utils.ansi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnsiScrollTest {

    private static final String CSI = "\033[";

    @Test
    public void testUp() {
        assertEquals(CSI + "S", AnsiScroll.up().toString());
    }

    @Test
    public void testUpN() {
        assertEquals(CSI + "3S", AnsiScroll.up(3).toString());
    }

    @Test
    public void testDown() {
        assertEquals(CSI + "T", AnsiScroll.down().toString());
    }

    @Test
    public void testDownN() {
        assertEquals(CSI + "5T", AnsiScroll.down(5).toString());
    }

    @Test
    public void testRight() {
        assertEquals(CSI + " A", AnsiScroll.right().toString());
    }

    @Test
    public void testRightN() {
        assertEquals(CSI + "2 A", AnsiScroll.right(2).toString());
    }

    @Test
    public void testLeft() {
        assertEquals(CSI + " @", AnsiScroll.left().toString());
    }

    @Test
    public void testLeftN() {
        assertEquals(CSI + "4 @", AnsiScroll.left(4).toString());
    }
}

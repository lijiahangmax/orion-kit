package cn.orionsec.kit.lang.utils.ansi;

import cn.orionsec.kit.lang.utils.ansi.cursor.AnsiCursor;
import cn.orionsec.kit.lang.utils.ansi.cursor.AnsiCursorStyle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnsiCursorTest {

    private static final String CSI = "\033[";

    @Test
    public void testUp() {
        assertEquals(CSI + "A", AnsiCursor.up().toString());
    }

    @Test
    public void testUpN() {
        assertEquals(CSI + "5A", AnsiCursor.up(5).toString());
    }

    @Test
    public void testDown() {
        assertEquals(CSI + "B", AnsiCursor.down().toString());
    }

    @Test
    public void testDownN() {
        assertEquals(CSI + "3B", AnsiCursor.down(3).toString());
    }

    @Test
    public void testRight() {
        assertEquals(CSI + "C", AnsiCursor.right().toString());
    }

    @Test
    public void testRightN() {
        assertEquals(CSI + "4C", AnsiCursor.right(4).toString());
    }

    @Test
    public void testLeft() {
        assertEquals(CSI + "D", AnsiCursor.left().toString());
    }

    @Test
    public void testLeftN() {
        assertEquals(CSI + "2D", AnsiCursor.left(2).toString());
    }

    @Test
    public void testNext() {
        assertEquals(CSI + "E", AnsiCursor.next().toString());
    }

    @Test
    public void testNextN() {
        assertEquals(CSI + "3E", AnsiCursor.next(3).toString());
    }

    @Test
    public void testPrev() {
        assertEquals(CSI + "F", AnsiCursor.prev().toString());
    }

    @Test
    public void testPrevN() {
        assertEquals(CSI + "2F", AnsiCursor.prev(2).toString());
    }

    @Test
    public void testColumn() {
        assertEquals(CSI + "G", AnsiCursor.column().toString());
    }

    @Test
    public void testColumnN() {
        assertEquals(CSI + "10G", AnsiCursor.column(10).toString());
    }

    @Test
    public void testForwardTab() {
        assertEquals(CSI + "I", AnsiCursor.forwardTab().toString());
    }

    @Test
    public void testForwardTabN() {
        assertEquals(CSI + "3I", AnsiCursor.forwardTab(3).toString());
    }

    @Test
    public void testBackwardTab() {
        assertEquals(CSI + "Z", AnsiCursor.backwardTab().toString());
    }

    @Test
    public void testBackwardTabN() {
        assertEquals(CSI + "2Z", AnsiCursor.backwardTab(2).toString());
    }

    @Test
    public void testLine() {
        assertEquals(CSI + "5;H", AnsiCursor.line(5).toString());
    }

    @Test
    public void testReset() {
        assertEquals(CSI + ";H", AnsiCursor.reset().toString());
    }

    @Test
    public void testMove() {
        assertEquals(CSI + "3;5H", AnsiCursor.move(3, 5).toString());
    }

    @Test
    public void testMark() {
        assertEquals(CSI + "s", AnsiCursor.mark().toString());
    }

    @Test
    public void testResume() {
        assertEquals(CSI + "u", AnsiCursor.resume().toString());
    }

    @Test
    public void testReport() {
        assertEquals(CSI + "6n", AnsiCursor.report().toString());
    }

    @Test
    public void testCursorStyleSteadyBlock() {
        assertEquals(CSI + "1 q", AnsiCursorStyle.STEADY_BLOCK.toString());
    }

    @Test
    public void testCursorStyleBlinkBlock() {
        assertEquals(CSI + "2 q", AnsiCursorStyle.BLINK_BLOCK.toString());
    }

    @Test
    public void testCursorStyleSteadyUnderline() {
        assertEquals(CSI + "3 q", AnsiCursorStyle.STEADY_UNDERLINE.toString());
    }

    @Test
    public void testCursorStyleBlinkBar() {
        assertEquals(CSI + "6 q", AnsiCursorStyle.BLINK_BAR.toString());
    }
}

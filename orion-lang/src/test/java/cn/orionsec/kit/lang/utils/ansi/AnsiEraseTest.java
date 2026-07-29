package cn.orionsec.kit.lang.utils.ansi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnsiEraseTest {

    private static final String CSI = "\033[";

    @Test
    public void testDisplayEnd() {
        assertEquals(CSI + "0J", AnsiErase.displayEnd().toString());
    }

    @Test
    public void testDisplayStart() {
        assertEquals(CSI + "1J", AnsiErase.displayStart().toString());
    }

    @Test
    public void testDisplay() {
        assertEquals(CSI + "2J", AnsiErase.display().toString());
    }

    @Test
    public void testLineEnd() {
        assertEquals(CSI + "0K", AnsiErase.lineEnd().toString());
    }

    @Test
    public void testLineStart() {
        assertEquals(CSI + "1K", AnsiErase.lineStart().toString());
    }

    @Test
    public void testLine() {
        assertEquals(CSI + "2K", AnsiErase.line().toString());
    }

    @Test
    public void testCharacter() {
        assertEquals(CSI + "5X", AnsiErase.character(5).toString());
    }
}

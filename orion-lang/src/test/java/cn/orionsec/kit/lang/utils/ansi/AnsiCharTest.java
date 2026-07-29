package cn.orionsec.kit.lang.utils.ansi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnsiCharTest {

    private static final String CSI = "\033[";

    @Test
    public void testInsertLine() {
        assertEquals(CSI + "L", AnsiChar.insertLine().toString());
    }

    @Test
    public void testInsertLineN() {
        assertEquals(CSI + "5L", AnsiChar.insertLine(5).toString());
    }

    @Test
    public void testDeleteLine() {
        assertEquals(CSI + "M", AnsiChar.deleteLine().toString());
    }

    @Test
    public void testDeleteLineN() {
        assertEquals(CSI + "3M", AnsiChar.deleteLine(3).toString());
    }

    @Test
    public void testInsertColumns() {
        assertEquals(CSI + "'}", AnsiChar.insertColumns().toString());
    }

    @Test
    public void testInsertColumnsN() {
        assertEquals(CSI + "4'}", AnsiChar.insertColumns(4).toString());
    }

    @Test
    public void testDeleteColumns() {
        assertEquals(CSI + "'~", AnsiChar.deleteColumns().toString());
    }

    @Test
    public void testDeleteColumnsN() {
        assertEquals(CSI + "2'~", AnsiChar.deleteColumns(2).toString());
    }

    @Test
    public void testInsertBlankChars() {
        assertEquals(CSI + "@", AnsiChar.insertBlankChars().toString());
    }

    @Test
    public void testInsertBlankCharsN() {
        assertEquals(CSI + "6@", AnsiChar.insertBlankChars(6).toString());
    }

    @Test
    public void testDeleteChars() {
        assertEquals(CSI + "P", AnsiChar.deleteChars().toString());
    }

    @Test
    public void testDeleteCharsN() {
        assertEquals(CSI + "3P", AnsiChar.deleteChars(3).toString());
    }

    @Test
    public void testRepeatChar() {
        assertEquals(CSI + "b", AnsiChar.repeatChar().toString());
    }

    @Test
    public void testRepeatCharN() {
        assertEquals(CSI + "7b", AnsiChar.repeatChar(7).toString());
    }
}

package cn.orionsec.kit.lang.utils.ansi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnsiCtrlTest {

    @Test
    public void testBS() {
        assertEquals("\b", AnsiCtrl.BS.toString());
    }

    @Test
    public void testHT() {
        assertEquals("\t", AnsiCtrl.HT.toString());
    }

    @Test
    public void testCR() {
        assertEquals("\r", AnsiCtrl.CR.toString());
    }

    @Test
    public void testLF() {
        assertEquals("\n", AnsiCtrl.LF.toString());
    }

    @Test
    public void testStartPrivacyMessage() {
        assertEquals("\033^", AnsiCtrl.START_PRIVACY_MESSAGE.toString());
    }

    @Test
    public void testInsertMode() {
        assertEquals("\033[4h", AnsiCtrl.INSERT_MODE.toString());
    }

    @Test
    public void testReplaceMode() {
        assertEquals("\033[4l", AnsiCtrl.REPLACE_MODE.toString());
    }
}

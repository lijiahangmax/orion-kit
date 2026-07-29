package cn.orionsec.kit.lang.utils.ansi;

import cn.orionsec.kit.lang.utils.ansi.style.AnsiFont;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnsiAppenderTest {

    @Test
    public void testCreate() {
        AnsiAppender appender = AnsiAppender.create();
        assertNotNull(appender);
        assertEquals("", appender.get());
    }

    @Test
    public void testAppendText() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append("hello");
        assertEquals("hello", appender.get());
    }

    @Test
    public void testAppendWithStyle() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append(AnsiFont.BOLD, "text");
        String result = appender.get();
        assertTrue(result.startsWith("\033["));
        assertTrue(result.contains("text"));
        assertTrue(result.endsWith("\033[0m"));
    }

    @Test
    public void testAppendElement() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append(AnsiCtrl.LF);
        assertEquals("\n", appender.get());
    }

    @Test
    public void testAppendMultipleObjects() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append((Object[]) new Object[]{"a", "b", "c"});
        assertEquals("abc", appender.get());
    }

    @Test
    public void testReset() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append("hello");
        appender.reset();
        assertEquals("hello\033[0m", appender.get());
    }

    @Test
    public void testNewLine() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append("line1").newLine().append("line2");
        String result = appender.get();
        assertTrue(result.contains("\r"));
        assertTrue(result.contains("\n"));
    }

    @Test
    public void testClear() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append("hello");
        appender.clear();
        assertEquals("", appender.get());
    }

    @Test
    public void testGetAndClear() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append("hello");
        String result = appender.getAndClear();
        assertEquals("hello", result);
        assertEquals("", appender.get());
    }

    @Test
    public void testToString() {
        AnsiAppender appender = AnsiAppender.create();
        appender.append("test");
        assertEquals("test", appender.toString());
    }
}

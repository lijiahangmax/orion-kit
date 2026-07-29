package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.handler.LineHandler;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * LineHandler 接口测试
 */
public class LineHandlerTest {

    @Test
    public void testLambdaAssignment() {
        LineHandler handler = (read, line, tracker) -> {
        };
        assertNotNull(handler);
    }

    @Test
    public void testReadLineInvocation() {
        AtomicBoolean called = new AtomicBoolean(false);
        AtomicReference<String> receivedRead = new AtomicReference<>();
        AtomicInteger receivedLine = new AtomicInteger(-1);

        LineHandler handler = (read, line, tracker) -> {
            called.set(true);
            receivedRead.set(read);
            receivedLine.set(line);
        };

        handler.readLine("test line", 0, null);

        assertTrue(called.get());
        assertEquals("test line", receivedRead.get());
        assertEquals(0, receivedLine.get());
    }

    @Test
    public void testMultipleLines() {
        AtomicInteger lineCount = new AtomicInteger(0);

        LineHandler handler = (read, line, tracker) -> lineCount.incrementAndGet();

        handler.readLine("line1", 0, null);
        handler.readLine("line2", 1, null);
        handler.readLine("line3", 2, null);

        assertEquals(3, lineCount.get());
    }

}

package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.handler.DataHandler;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * DataHandler 接口测试
 */
public class DataHandlerTest {

    @Test
    public void testLambdaAssignment() {
        DataHandler handler = (bytes, len, tracker) -> {
        };
        assertNotNull(handler);
    }

    @Test
    public void testReadInvocation() {
        AtomicBoolean called = new AtomicBoolean(false);
        AtomicInteger receivedLen = new AtomicInteger(0);

        DataHandler handler = (bytes, len, tracker) -> {
            called.set(true);
            receivedLen.set(len);
        };

        byte[] testData = "hello".getBytes();
        handler.read(testData, testData.length, null);

        assertTrue(called.get());
        assertEquals(5, receivedLen.get());
    }

    @Test
    public void testReadWithPartialLen() {
        AtomicInteger receivedLen = new AtomicInteger(0);

        DataHandler handler = (bytes, len, tracker) -> receivedLen.set(len);

        byte[] testData = new byte[1024];
        handler.read(testData, 100, null);

        assertEquals(100, receivedLen.get());
    }

}

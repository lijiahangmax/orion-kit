package cn.orionsec.kit.lang.define.io;

import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * io 包单元测试
 */
public class IoPackageTest {

    // ==================== IgnoreOutputStream ====================

    @Test
    public void testIgnoreOutputStreamWriteInt() throws Exception {
        IgnoreOutputStream out = IgnoreOutputStream.OUT;
        out.write(1); // should not throw
        out.write(new byte[]{1, 2, 3});
        out.write(new byte[]{1, 2, 3}, 0, 2);
    }

    @Test
    public void testIgnoreOutputStreamSingleton() {
        assertNotNull(IgnoreOutputStream.OUT);
        assertSame(IgnoreOutputStream.OUT, IgnoreOutputStream.OUT);
    }

    // ==================== OutputAppender ====================

    @Test
    public void testOutputAppenderWriteToMultiple() throws Exception {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();

        OutputAppender appender = OutputAppender.create(out1).then(out2);
        appender.write("hello".getBytes());
        appender.flush();

        assertEquals("hello", out1.toString());
        assertEquals("hello", out2.toString());
    }

    @Test
    public void testOutputAppenderWriteInt() throws Exception {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();

        OutputAppender appender = OutputAppender.create(out1).then(out2);
        appender.write(65); // 'A'
        appender.flush();

        assertEquals("A", out1.toString());
        assertEquals("A", out2.toString());
    }

    @Test
    public void testOutputAppenderGetRoot() {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();

        OutputAppender a1 = OutputAppender.create(out1);
        OutputAppender a2 = a1.then(out2);
        OutputAppender a3 = a2.then(out3);

        assertSame(a1, a3.getRoot());
    }

    @Test
    public void testOutputAppenderIterator() {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();

        OutputAppender appender = OutputAppender.create(out1).then(out2);
        int count = 0;
        for (OutputAppender a : appender) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testOutputAppenderClose() throws Exception {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();

        OutputAppender appender = OutputAppender.create(out1).then(out2);
        appender.close(); // should not throw
    }

    // ==================== WriterAppender ====================

    @Test
    public void testWriterAppenderWriteInt() throws Exception {
        StringWriter w1 = new StringWriter();
        StringWriter w2 = new StringWriter();

        WriterAppender appender = WriterAppender.create(w1).then(w2);
        appender.write(65); // 'A'
        appender.flush();

        assertEquals("A", w1.toString());
        assertEquals("A", w2.toString());
    }

    @Test
    public void testWriterAppenderGetRoot() {
        StringWriter w1 = new StringWriter();
        StringWriter w2 = new StringWriter();

        WriterAppender a1 = WriterAppender.create(w1);
        WriterAppender a2 = a1.then(w2);

        assertSame(a1, a2.getRoot());
    }

    @Test
    public void testWriterAppenderIterator() {
        StringWriter w1 = new StringWriter();
        StringWriter w2 = new StringWriter();
        StringWriter w3 = new StringWriter();

        WriterAppender appender = WriterAppender.create(w1).then(w2).then(w3);
        int count = 0;
        Iterator<WriterAppender> it = appender.iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(3, count);
    }

    // ==================== ProgressInputStream ====================

    @Test
    public void testProgressInputStreamRead() throws Exception {
        byte[] data = "hello world".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ProgressInputStream pis = new ProgressInputStream(bais, data.length);
        pis.start();

        byte[] buf = new byte[5];
        int read = pis.read(buf);
        assertEquals(5, read);
        assertEquals("hello", new String(buf));
        pis.close();
    }

    @Test
    public void testProgressInputStreamReadSingle() throws Exception {
        byte[] data = {65, 66, 67};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ProgressInputStream pis = new ProgressInputStream(bais, data.length);
        assertEquals(65, pis.read());
        assertEquals(66, pis.read());
        pis.close();
    }

    @Test
    public void testProgressInputStreamAvailable() throws Exception {
        byte[] data = "test".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ProgressInputStream pis = new ProgressInputStream(bais);
        assertEquals(4, pis.available());
        pis.close();
    }

    // ==================== ProgressOutputStream ====================

    @Test
    public void testProgressOutputStreamWrite() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProgressOutputStream pos = new ProgressOutputStream(baos, 100);
        pos.start();
        pos.write("hello".getBytes());
        pos.flush();
        assertEquals("hello", baos.toString());
        pos.close();
    }

    @Test
    public void testProgressOutputStreamWriteSingle() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProgressOutputStream pos = new ProgressOutputStream(baos);
        pos.write(65);
        pos.flush();
        assertEquals("A", baos.toString());
        pos.close();
    }

    @Test
    public void testProgressOutputStreamFinish() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProgressOutputStream pos = new ProgressOutputStream(baos);
        pos.start();
        pos.finish();
        assertNotNull(pos.getProgress());
    }

    // ==================== ProgressReader ====================

    @Test
    public void testProgressReaderRead() throws Exception {
        StringReader sr = new StringReader("hello");
        ProgressReader pr = new ProgressReader(sr, 5);
        pr.start();
        char[] buf = new char[5];
        int read = pr.read(buf);
        assertEquals(5, read);
        assertEquals("hello", new String(buf));
        pr.close();
    }

    @Test
    public void testProgressReaderReadSingle() throws Exception {
        StringReader sr = new StringReader("AB");
        ProgressReader pr = new ProgressReader(sr);
        assertEquals('A', pr.read());
        assertEquals('B', pr.read());
        pr.close();
    }

    // ==================== ProgressWriter ====================

    @Test
    public void testProgressWriterWrite() throws Exception {
        StringWriter sw = new StringWriter();
        ProgressWriter pw = new ProgressWriter(sw, 100);
        pw.start();
        pw.write('A');
        pw.write(new char[]{'B', 'C'});
        pw.flush();
        assertEquals("ABC", sw.toString());
        pw.close();
    }

    @Test
    public void testProgressWriterFinish() throws Exception {
        StringWriter sw = new StringWriter();
        ProgressWriter pw = new ProgressWriter(sw);
        pw.start();
        pw.finish(false);
        assertNotNull(pw.getProgress());
    }

    // ==================== LimitOutputStream ====================

    @Test
    public void testLimitOutputStreamWriteInt() {
        LimitOutputStream out = new LimitOutputStream(3);
        out.write('A');
        out.write('B');
        out.write('C');
        out.write('D');
        assertEquals("ABC", new String(out.toByteArray()));
    }

    @Test
    public void testLimitOutputStreamWriteBytes() {
        LimitOutputStream out = new LimitOutputStream(4);
        out.write("hello".getBytes(), 0, 5);
        assertEquals("hell", new String(out.toByteArray()));
    }

    @Test
    public void testLimitOutputStreamZeroLimit() {
        LimitOutputStream out = new LimitOutputStream(0);
        out.write('A');
        out.write(new byte[]{'B', 'C'});
        assertEquals(0, out.toByteArray().length);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testLimitOutputStreamNegativeLimit() {
        new LimitOutputStream(-1);
    }

    // ==================== LimitInputStream ====================

    @Test
    public void testLimitInputStreamReadSingle() throws Exception {
        LimitInputStream in = new LimitInputStream(new ByteArrayInputStream("hello".getBytes()), 3);
        assertEquals('h', in.read());
        assertEquals('e', in.read());
        assertEquals('l', in.read());
        assertEquals(-1, in.read());
    }

    @Test
    public void testLimitInputStreamReadBytes() throws Exception {
        LimitInputStream in = new LimitInputStream(new ByteArrayInputStream("hello".getBytes()), 4);
        byte[] buf = new byte[10];
        assertEquals(4, in.read(buf));
        assertEquals("hell", new String(buf, 0, 4));
        assertEquals(-1, in.read(buf, 0, 10));
    }

    @Test
    public void testLimitInputStreamSkip() throws Exception {
        LimitInputStream in = new LimitInputStream(new ByteArrayInputStream("hello".getBytes()), 3);
        assertEquals(3, in.skip(10));
        assertEquals(-1, in.read());
    }

    @Test
    public void testLimitInputStreamAvailable() throws Exception {
        LimitInputStream in = new LimitInputStream(new ByteArrayInputStream("hello".getBytes()), 2);
        assertEquals(2, in.available());
        in.read();
        assertEquals(1, in.available());
        in.read();
        assertEquals(0, in.available());
    }

    @Test
    public void testLimitInputStreamZeroLimit() throws Exception {
        LimitInputStream in = new LimitInputStream(new ByteArrayInputStream("hello".getBytes()), 0);
        assertEquals(-1, in.read());
        assertEquals(-1, in.read(new byte[5], 0, 5));
        assertEquals(0, in.available());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testLimitInputStreamNegativeLimit() {
        new LimitInputStream(new ByteArrayInputStream(new byte[0]), -1);
    }
}

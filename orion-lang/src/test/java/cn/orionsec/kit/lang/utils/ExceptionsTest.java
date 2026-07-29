package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionsTest {

    @Test
    public void testImpossible() {
        RuntimeException e = Exceptions.impossible();
        assertNotNull(e);
        assertTrue(e.getMessage().contains("impossible"));
    }

    @Test
    public void testGetMessage() {
        assertNull(Exceptions.getMessage(null));
        assertEquals("test", Exceptions.getMessage(new RuntimeException("test")));
    }

    @Test
    public void testGetDigest() {
        assertNull(Exceptions.getDigest(null));
        String digest = Exceptions.getDigest(new RuntimeException("test"));
        assertTrue(digest.contains("RuntimeException"));
        assertTrue(digest.contains("test"));
    }

    @Test
    public void testGetDigestNullMessage() {
        String digest = Exceptions.getDigest(new RuntimeException());
        assertEquals("java.lang.RuntimeException", digest);
    }

    @Test
    public void testUnchecked() {
        RuntimeException re = Exceptions.unchecked(new Exception("wrapped"));
        assertNotNull(re);
    }

    @Test
    public void testRuntime() {
        RuntimeException re = Exceptions.runtime("msg");
        assertEquals("msg", re.getMessage());
    }

    @Test
    public void testArgument() {
        Exception e = Exceptions.argument("bad arg");
        assertNotNull(e);
        assertEquals("bad arg", e.getMessage());
    }

    @Test
    public void testArrayIndex() {
        Exception e = Exceptions.arrayIndex("index error");
        assertNotNull(e);
    }

    @Test
    public void testIoRuntime() {
        RuntimeException re = Exceptions.ioRuntime(new java.io.IOException("io"));
        assertNotNull(re);
    }
}

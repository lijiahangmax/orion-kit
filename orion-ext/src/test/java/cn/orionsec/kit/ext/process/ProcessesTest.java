package cn.orionsec.kit.ext.process;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * {@link Processes} 单元测试
 */
public class ProcessesTest {

    @Test
    public void testGetOutputResultString() {
        String result = Processes.getOutputResultString("cmd", "/c", "echo", "hello");
        assertNotNull(result);
        assertTrue(result.trim().contains("hello"));
    }

    @Test
    public void testGetOutputResultStringWithRedirectError() {
        String result = Processes.getOutputResultString(true, "cmd", "/c", "echo", "hello");
        assertNotNull(result);
        assertTrue(result.trim().contains("hello"));
    }

    @Test
    public void testGetOutputResult() {
        byte[] result = Processes.getOutputResult("cmd", "/c", "echo", "test123");
        assertNotNull(result);
        assertTrue(result.length > 0);
        String str = new String(result);
        assertTrue(str.contains("test123"));
    }

    @Test
    public void testGetOutputResultWithRedirectError() {
        byte[] result = Processes.getOutputResult(true, "cmd", "/c", "echo", "test456");
        assertNotNull(result);
        assertTrue(result.length > 0);
        String str = new String(result);
        assertTrue(str.contains("test456"));
    }

    @Test
    public void testGetOutputResultWithDirString() {
        String dir = System.getProperty("java.io.tmpdir");
        String result = Processes.getOutputResultWithDirString(dir, "cmd", "/c", "echo", "dirtest");
        assertNotNull(result);
        assertTrue(result.trim().contains("dirtest"));
    }

    @Test
    public void testGetOutputResultWithDirStringAndRedirectError() {
        String dir = System.getProperty("java.io.tmpdir");
        String result = Processes.getOutputResultWithDirString(true, dir, "cmd", "/c", "echo", "dirtest2");
        assertNotNull(result);
        assertTrue(result.trim().contains("dirtest2"));
    }

    @Test
    public void testGetOutputResultWithDir() {
        String dir = System.getProperty("java.io.tmpdir");
        byte[] result = Processes.getOutputResultWithDir(dir, "cmd", "/c", "echo", "bytes_dir");
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertTrue(new String(result).contains("bytes_dir"));
    }

    @Test
    public void testGetOutputResultWithDirAndRedirectError() {
        String dir = System.getProperty("java.io.tmpdir");
        byte[] result = Processes.getOutputResultWithDir(true, dir, "cmd", "/c", "echo", "bytes_dir2");
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertTrue(new String(result).contains("bytes_dir2"));
    }

}

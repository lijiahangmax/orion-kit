package cn.orionsec.kit.ext.process;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * {@link ProcessAwaitExecutor} 单元测试
 */
public class ProcessAwaitExecutorTest {

    @Test
    public void testConstructorWithString() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo hello");
        assertNotNull(executor);
        assertArrayEquals(new String[]{"echo hello"}, executor.getCommand());
        executor.close();
    }

    @Test
    public void testConstructorWithStringArray() {
        String[] cmd = {"cmd", "/c", "echo", "hello"};
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(cmd);
        assertNotNull(executor);
        assertArrayEquals(cmd, executor.getCommand());
        executor.close();
    }

    @Test
    public void testConstructorWithDir() {
        String dir = System.getProperty("java.io.tmpdir");
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test", dir);
        assertNotNull(executor);
        assertEquals(dir, executor.getDir());
        executor.close();
    }

    @Test
    public void testConstructorWithArrayAndDir() {
        String dir = System.getProperty("java.io.tmpdir");
        String[] cmd = {"cmd", "/c", "echo", "test"};
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(cmd, dir);
        assertNotNull(executor);
        assertEquals(dir, executor.getDir());
        assertArrayEquals(cmd, executor.getCommand());
        executor.close();
    }

    @Test
    public void testInherit() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        assertFalse(executor.isInherit());
        executor.inherit();
        assertTrue(executor.isInherit());
        executor.close();
    }

    @Test
    public void testWaitFor() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        assertEquals(-1L, executor.getWaitFor());
        executor.waitFor();
        assertEquals(0L, executor.getWaitFor());
        executor.close();
    }

    @Test
    public void testWaitForWithTimeout() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.waitFor(5000L);
        assertEquals(5000L, executor.getWaitFor());
        executor.close();
    }

    @Test
    public void testSync() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.sync();
        assertEquals(0L, executor.getWaitFor());
        executor.close();
    }

    @Test
    public void testDir() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        String dir = System.getProperty("java.io.tmpdir");
        executor.dir(dir);
        assertEquals(dir, executor.getDir());
        executor.close();
    }

    @Test
    public void testRedirectError() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.redirectError();
        // redirectError is protected, test via exec behavior
        executor.close();
    }

    @Test
    public void testAddEnvSingle() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.addEnv("MY_VAR", "my_value");
        Map<String, String> addEnv = executor.getAddEnv();
        assertNotNull(addEnv);
        assertEquals("my_value", addEnv.get("MY_VAR"));
        executor.close();
    }

    @Test
    public void testAddEnvMap() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        Map<String, String> envMap = new HashMap<>();
        envMap.put("KEY1", "VAL1");
        envMap.put("KEY2", "VAL2");
        executor.addEnv(envMap);
        Map<String, String> addEnv = executor.getAddEnv();
        assertNotNull(addEnv);
        assertEquals("VAL1", addEnv.get("KEY1"));
        assertEquals("VAL2", addEnv.get("KEY2"));
        executor.close();
    }

    @Test
    public void testRemoveEnvVarargs() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.removeEnv("PATH", "HOME");
        executor.close();
    }

    @Test
    public void testTerminal() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo hello");
        executor.terminal();
        String[] cmd = executor.getCommand();
        // On Windows, terminal should prepend cmd /c
        assertTrue(cmd.length >= 3);
        assertEquals("cmd", cmd[0]);
        assertEquals("/c", cmd[1]);
        executor.close();
    }

    @Test
    public void testExecSyncEcho() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "sync_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.streamHandler(handler)
                .sync()
                .exec();
        String output = baos.toString();
        assertTrue(output.contains("sync_test"));
        assertFalse(executor.isAlive());
        assertEquals(0, executor.getExitCode());
        executor.close();
        assertTrue(executor.isClose());
    }

    @Test
    public void testExecWithWaitFor() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "wait_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.streamHandler(handler)
                .waitFor()
                .exec();
        // After waitFor, the process should be finished
        assertNotNull(executor.getProcess());
        assertNotNull(executor.getProcessBuilder());
        executor.close();
    }

    @Test
    public void testExecWithWaitForTimeout() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "timeout_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.streamHandler(handler)
                .waitFor(5000)
                .exec();
        assertNotNull(executor.getProcess());
        executor.close();
    }

    @Test
    public void testExecWithCallback() throws Exception {
        final boolean[] callbackCalled = {false};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "callback_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.streamHandler(handler)
                .callback(() -> callbackCalled[0] = true)
                .sync()
                .exec();
        assertTrue(callbackCalled[0]);
        assertTrue(executor.isDone());
        executor.close();
    }

    @Test
    public void testExecWithDir() {
        String dir = System.getProperty("java.io.tmpdir");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "dir_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.sync().streamHandler(handler);
        executor.dir(dir);
        executor.exec();
        String output = baos.toString();
        assertTrue(output.contains("dir_test"));
        executor.close();
    }

    @Test
    public void testExecWithAddEnv() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "%MY_CUSTOM_VAR%"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.sync().streamHandler(handler);
        executor.addEnv("MY_CUSTOM_VAR", "custom_value_123");
        executor.exec();
        String output = baos.toString();
        assertTrue(output.contains("custom_value_123"));
        executor.close();
    }

    @Test
    public void testExecWithRedirectError() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "redirect_err"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.sync().streamHandler(handler);
        executor.redirectError();
        executor.exec();
        String output = baos.toString();
        assertTrue(output.contains("redirect_err"));
        executor.close();
    }

    @Test(expected = RuntimeException.class)
    public void testExecWithoutStreamHandler() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "test"});
        executor.sync().exec();
    }

    @Test
    public void testToString() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "test"});
        String str = executor.toString();
        assertNotNull(str);
        assertTrue(str.contains("cmd"));
        assertTrue(str.contains("echo"));
        executor.close();
    }

    @Test
    public void testGetStreams() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "stream_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.streamHandler(handler)
                .sync()
                .exec();
        // After exec with sync, streams should have been created
        assertNotNull(executor.getInputStream());
        assertNotNull(executor.getOutputStream());
        assertNotNull(executor.getErrorStream());
        executor.close();
    }

    @Test
    public void testCloseIdempotent() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "close_test"});
        Consumer<InputStream> handler = inputStream -> {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // ignore
            }
        };
        executor.streamHandler(handler)
                .sync()
                .exec();
        executor.close();
        assertTrue(executor.isClose());
        // calling close again should not throw
        executor.close();
        assertTrue(executor.isClose());
    }

}

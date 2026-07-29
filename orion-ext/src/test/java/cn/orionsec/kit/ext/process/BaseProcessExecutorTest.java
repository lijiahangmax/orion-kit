package cn.orionsec.kit.ext.process;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * {@link BaseProcessExecutor} 单元测试
 * 通过具体子类 ProcessAwaitExecutor 来测试基类方法
 */
public class BaseProcessExecutorTest {

    @Test
    public void testGetCommand() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        assertArrayEquals(new String[]{"echo test"}, executor.getCommand());
        executor.close();
    }

    @Test
    public void testGetCommandArray() {
        String[] cmd = {"cmd", "/c", "echo", "hello"};
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(cmd);
        assertArrayEquals(cmd, executor.getCommand());
        executor.close();
    }

    @Test
    public void testGetDir() {
        String dir = System.getProperty("java.io.tmpdir");
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test", dir);
        assertEquals(dir, executor.getDir());
        executor.close();
    }

    @Test
    public void testGetDirNull() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        assertNull(executor.getDir());
        executor.close();
    }

    @Test
    public void testDirSetter() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        String dir = System.getProperty("java.io.tmpdir");
        BaseProcessExecutor result = executor.dir(dir);
        assertEquals(dir, executor.getDir());
        assertSame(executor, result);
        executor.close();
    }

    @Test
    public void testRedirectError() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        BaseProcessExecutor result = executor.redirectError();
        assertSame(executor, result);
        executor.close();
    }

    @Test
    public void testAddEnvSingle() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        assertNull(executor.getAddEnv());
        BaseProcessExecutor result = executor.addEnv("KEY1", "VAL1");
        assertSame(executor, result);
        assertNotNull(executor.getAddEnv());
        assertEquals("VAL1", executor.getAddEnv().get("KEY1"));
        executor.close();
    }

    @Test
    public void testAddEnvMultiple() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.addEnv("K1", "V1");
        executor.addEnv("K2", "V2");
        assertEquals(2, executor.getAddEnv().size());
        assertEquals("V1", executor.getAddEnv().get("K1"));
        assertEquals("V2", executor.getAddEnv().get("K2"));
        executor.close();
    }

    @Test
    public void testAddEnvMap() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        Map<String, String> envMap = new HashMap<>();
        envMap.put("A", "1");
        envMap.put("B", "2");
        envMap.put("C", "3");
        BaseProcessExecutor result = executor.addEnv(envMap);
        assertSame(executor, result);
        assertEquals(3, executor.getAddEnv().size());
        assertEquals("1", executor.getAddEnv().get("A"));
        assertEquals("2", executor.getAddEnv().get("B"));
        assertEquals("3", executor.getAddEnv().get("C"));
        executor.close();
    }

    @Test
    public void testAddEnvMapMerge() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.addEnv("EXISTING", "existing_val");
        Map<String, String> envMap = new HashMap<>();
        envMap.put("NEW_KEY", "new_val");
        executor.addEnv(envMap);
        assertEquals(2, executor.getAddEnv().size());
        assertEquals("existing_val", executor.getAddEnv().get("EXISTING"));
        assertEquals("new_val", executor.getAddEnv().get("NEW_KEY"));
        executor.close();
    }

    @Test
    public void testRemoveEnvVarargs() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        BaseProcessExecutor result = executor.removeEnv("PATH", "HOME");
        assertSame(executor, result);
        executor.close();
    }

    @Test
    public void testRemoveEnvNull() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.removeEnv((String[]) null);
        // should not throw
        executor.close();
    }

    @Test
    public void testRemoveEnvList() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        List<String> keys = Arrays.asList("PATH", "HOME", "USER");
        BaseProcessExecutor result = executor.removeEnv(keys);
        assertSame(executor, result);
        executor.close();
    }

    @Test
    public void testRemoveEnvListNull() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        executor.removeEnv((List<String>) null);
        // should not throw
        executor.close();
    }

    @Test
    public void testTerminal() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo hello world");
        BaseProcessExecutor result = executor.terminal();
        assertSame(executor, result);
        String[] cmd = executor.getCommand();
        // Windows: cmd, /c, echo hello world
        assertEquals("cmd", cmd[0]);
        assertEquals("/c", cmd[1]);
        assertTrue(cmd.length >= 3);
        executor.close();
    }

    @Test
    public void testTerminalWithNewlines() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo\nhello");
        executor.terminal();
        String[] cmd = executor.getCommand();
        // Newlines should be replaced with spaces
        assertEquals("cmd", cmd[0]);
        assertEquals("/c", cmd[1]);
        // The command should have newlines replaced
        assertFalse(cmd[2].contains("\n"));
        assertFalse(cmd[2].contains("\r"));
        executor.close();
    }

    @Test
    public void testTerminalWithMultipleCommands() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"echo", "hello"});
        executor.terminal();
        String[] cmd = executor.getCommand();
        // cmd, /c, echo, hello
        assertEquals("cmd", cmd[0]);
        assertEquals("/c", cmd[1]);
        assertEquals("echo", cmd[2]);
        assertEquals("hello", cmd[3]);
        executor.close();
    }

    @Test
    public void testToString() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(new String[]{"cmd", "/c", "echo", "test"});
        String str = executor.toString();
        assertNotNull(str);
        assertTrue(str.contains("cmd"));
        assertTrue(str.contains("/c"));
        assertTrue(str.contains("echo"));
        assertTrue(str.contains("test"));
        executor.close();
    }

    @Test
    public void testGetEnvBeforeExec() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo test");
        // env is null before exec
        assertNull(executor.getEnv());
        executor.close();
    }

}

package cn.orionsec.kit.ext.process;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * {@link ProcessAsyncExecutor} 单元测试
 */
public class ProcessAsyncExecutorTest {

    private ProcessAsyncExecutor executor;

    @After
    public void tearDown() {
        if (executor != null) {
            try {
                executor.close();
            } catch (Exception e) {
                // ignore if process not started
            }
        }
    }

    @Test
    public void testConstructorWithString() {
        executor = new ProcessAsyncExecutor("echo hello");
        assertNotNull(executor);
        assertArrayEquals(new String[]{"echo hello"}, executor.getCommand());
    }

    @Test
    public void testConstructorWithStringArray() {
        String[] cmd = {"cmd", "/c", "echo", "hello"};
        executor = new ProcessAsyncExecutor(cmd);
        assertNotNull(executor);
        assertArrayEquals(cmd, executor.getCommand());
    }

    @Test
    public void testConstructorWithDir() {
        String dir = System.getProperty("java.io.tmpdir");
        executor = new ProcessAsyncExecutor("echo test", dir);
        assertEquals(dir, executor.getDir());
    }

    @Test
    public void testConstructorWithArrayAndDir() {
        String dir = System.getProperty("java.io.tmpdir");
        String[] cmd = {"cmd", "/c", "echo", "test"};
        executor = new ProcessAsyncExecutor(cmd, dir);
        assertEquals(dir, executor.getDir());
        assertArrayEquals(cmd, executor.getCommand());
    }

    @Test
    public void testInputFileString() {
        executor = new ProcessAsyncExecutor("echo test");
        String path = System.getProperty("java.io.tmpdir") + File.separator + "test_input.txt";
        executor.inputFile(path);
        assertEquals(new File(path), executor.getInputFile());
    }

    @Test
    public void testInputFileObject() {
        executor = new ProcessAsyncExecutor("echo test");
        File file = new File(System.getProperty("java.io.tmpdir"), "test_input.txt");
        executor.inputFile(file);
        assertEquals(file, executor.getInputFile());
    }

    @Test
    public void testOutputFileString() {
        executor = new ProcessAsyncExecutor("echo test");
        String path = System.getProperty("java.io.tmpdir") + File.separator + "test_output.txt";
        executor.outputFile(path);
        assertEquals(new File(path), executor.getOutputFile());
    }

    @Test
    public void testOutputFileObject() {
        executor = new ProcessAsyncExecutor("echo test");
        File file = new File(System.getProperty("java.io.tmpdir"), "test_output.txt");
        executor.outputFile(file);
        assertEquals(file, executor.getOutputFile());
    }

    @Test
    public void testOutputFileWithAppend() {
        executor = new ProcessAsyncExecutor("echo test");
        String path = System.getProperty("java.io.tmpdir") + File.separator + "test_output_append.txt";
        executor.outputFile(path, true);
        assertEquals(new File(path), executor.getOutputFile());
    }

    @Test
    public void testOutputFileObjectWithAppend() {
        executor = new ProcessAsyncExecutor("echo test");
        File file = new File(System.getProperty("java.io.tmpdir"), "test_output_append.txt");
        executor.outputFile(file, true);
        assertEquals(file, executor.getOutputFile());
    }

    @Test
    public void testErrorFileString() {
        executor = new ProcessAsyncExecutor("echo test");
        String path = System.getProperty("java.io.tmpdir") + File.separator + "test_error.txt";
        executor.errorFile(path);
        assertEquals(new File(path), executor.getErrorFile());
    }

    @Test
    public void testErrorFileObject() {
        executor = new ProcessAsyncExecutor("echo test");
        File file = new File(System.getProperty("java.io.tmpdir"), "test_error.txt");
        executor.errorFile(file);
        assertEquals(file, executor.getErrorFile());
    }

    @Test
    public void testErrorFileWithAppend() {
        executor = new ProcessAsyncExecutor("echo test");
        String path = System.getProperty("java.io.tmpdir") + File.separator + "test_error_append.txt";
        executor.errorFile(path, true);
        assertEquals(new File(path), executor.getErrorFile());
    }

    @Test
    public void testErrorFileObjectWithAppend() {
        executor = new ProcessAsyncExecutor("echo test");
        File file = new File(System.getProperty("java.io.tmpdir"), "test_error_append.txt");
        executor.errorFile(file, true);
        assertEquals(file, executor.getErrorFile());
    }

    @Test
    public void testTerminal() {
        executor = new ProcessAsyncExecutor("echo hello");
        executor.terminal();
        String[] cmd = executor.getCommand();
        assertTrue(cmd.length >= 3);
        assertEquals("cmd", cmd[0]);
        assertEquals("/c", cmd[1]);
    }

    @Test
    public void testDir() {
        executor = new ProcessAsyncExecutor("echo test");
        String dir = System.getProperty("java.io.tmpdir");
        executor.dir(dir);
        assertEquals(dir, executor.getDir());
    }

    @Test
    public void testAddEnvSingle() {
        executor = new ProcessAsyncExecutor("echo test");
        executor.addEnv("TEST_KEY", "TEST_VAL");
        Map<String, String> addEnv = executor.getAddEnv();
        assertNotNull(addEnv);
        assertEquals("TEST_VAL", addEnv.get("TEST_KEY"));
    }

    @Test
    public void testAddEnvMap() {
        executor = new ProcessAsyncExecutor("echo test");
        Map<String, String> envMap = new HashMap<>();
        envMap.put("K1", "V1");
        envMap.put("K2", "V2");
        executor.addEnv(envMap);
        Map<String, String> addEnv = executor.getAddEnv();
        assertNotNull(addEnv);
        assertEquals("V1", addEnv.get("K1"));
        assertEquals("V2", addEnv.get("K2"));
    }

    @Test
    public void testRemoveEnv() {
        executor = new ProcessAsyncExecutor("echo test");
        executor.removeEnv("PATH", "HOME");
        // no exception means pass
    }

    @Test
    public void testExecWithOutputFile() throws Exception {
        Path outputPath = Files.createTempFile("process_async_test_", ".txt");
        File outputFile = outputPath.toFile();
        outputFile.deleteOnExit();

        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "async_output"});
        executor.outputFile(outputFile);
        executor.exec();

        // Wait for process to finish
        executor.getProcess().waitFor();

        assertFalse(executor.isAlive());
        assertEquals(0, executor.getExitCode());
        assertNotNull(executor.getProcessBuilder());

        // Verify file has content
        String content = new String(Files.readAllBytes(outputPath));
        assertTrue(content.contains("async_output"));
    }

    @Test
    public void testExecWithOutputFileAppend() throws Exception {
        Path outputPath = Files.createTempFile("process_async_append_", ".txt");
        File outputFile = outputPath.toFile();
        outputFile.deleteOnExit();

        // Write first
        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "line1"});
        executor.outputFile(outputFile);
        executor.exec();
        executor.getProcess().waitFor();
        executor.close();

        // Append second
        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "line2"});
        executor.outputFile(outputFile, true);
        executor.exec();
        executor.getProcess().waitFor();

        String content = new String(Files.readAllBytes(outputPath));
        assertTrue(content.contains("line1"));
        assertTrue(content.contains("line2"));
    }

    @Test
    public void testExecWithRedirectError() throws Exception {
        Path outputPath = Files.createTempFile("process_async_redir_", ".txt");
        File outputFile = outputPath.toFile();
        outputFile.deleteOnExit();

        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "redir_test"});
        executor.outputFile(outputFile);
        executor.redirectError();
        executor.exec();
        executor.getProcess().waitFor();

        assertFalse(executor.isAlive());
        assertEquals(0, executor.getExitCode());
    }

    @Test
    public void testExecWithErrorFile() throws Exception {
        Path errorPath = Files.createTempFile("process_async_err_", ".txt");
        File errorFile = errorPath.toFile();
        errorFile.deleteOnExit();

        Path outputPath = Files.createTempFile("process_async_out_", ".txt");
        File outputFile = outputPath.toFile();
        outputFile.deleteOnExit();

        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "normal_output"});
        executor.outputFile(outputFile);
        executor.errorFile(errorFile);
        executor.exec();
        executor.getProcess().waitFor();

        assertEquals(0, executor.getExitCode());
    }

    @Test
    public void testExecWithDir() throws Exception {
        String dir = System.getProperty("java.io.tmpdir");
        Path outputPath = Files.createTempFile("process_async_dir_", ".txt");
        File outputFile = outputPath.toFile();
        outputFile.deleteOnExit();

        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "dir_exec"});
        executor.outputFile(outputFile);
        executor.dir(dir);
        executor.exec();
        executor.getProcess().waitFor();

        assertEquals(0, executor.getExitCode());
        String content = new String(Files.readAllBytes(outputPath));
        assertTrue(content.contains("dir_exec"));
    }

    @Test
    public void testExitCodeWhileRunning() throws Exception {
        // Use a command that takes a tiny bit of time
        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "ping", "127.0.0.1", "-n", "2"});
        Path outputPath = Files.createTempFile("process_async_alive_", ".txt");
        File outputFile = outputPath.toFile();
        outputFile.deleteOnExit();
        executor.outputFile(outputFile);
        executor.exec();

        // Immediately check - might still be alive
        if (executor.isAlive()) {
            assertEquals(-1, executor.getExitCode());
        }
        executor.getProcess().waitFor();
        assertFalse(executor.isAlive());
    }

    @Test
    public void testToString() {
        executor = new ProcessAsyncExecutor(new String[]{"cmd", "/c", "echo", "to_string_test"});
        String str = executor.toString();
        assertNotNull(str);
        assertTrue(str.contains("cmd"));
        assertTrue(str.contains("echo"));
    }

}

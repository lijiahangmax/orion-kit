package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public class SystemsTest {

    @Test
    public void testConstants() {
        assertNotNull(Systems.LINE_SEPARATOR);
        assertNotNull(Systems.FILE_SEPARATOR);
        assertNotNull(Systems.USER_NAME);
        assertNotNull(Systems.FILE_ENCODING);
        assertNotNull(Systems.HOME_DIR);
        assertNotNull(Systems.USER_DIR);
        assertNotNull(Systems.TEMP_DIR);
        assertNotNull(Systems.OS_NAME);
        assertNotNull(Systems.OS_VERSION);
        assertNotNull(Systems.JAVA_HOME);
        assertNotNull(Systems.JAVA_SPEC_VERSION);
        assertTrue(Systems.PID > 0);
        assertTrue(Systems.PROCESS_NUM > 0);
    }

    @Test
    public void testBeWindowsOrUnix() {
        // At least one should be true
        assertTrue(Systems.BE_WINDOWS || Systems.BE_UNIX);
    }

    @Test
    public void testGetProperty() {
        String javaVersion = Systems.getProperty("java.version");
        assertNotNull(javaVersion);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String value = Systems.getProperty("nonexistent.property.xyz", "default");
        assertEquals("default", value);
    }

    @Test
    public void testSetAndClearProperty() {
        String key = "test.systems.unit.key";
        Systems.setProperty(key, "test_value");
        assertEquals("test_value", Systems.getProperty(key));
        System.clearProperty(key);
    }

    @Test
    public void testGetEnv() {
        Map<String, String> env = Systems.getEnv();
        assertNotNull(env);
        assertFalse(env.isEmpty());
    }

    @Test
    public void testGetProperties() {
        Properties props = Systems.getProperties();
        assertNotNull(props);
        assertFalse(props.isEmpty());
    }

    @Test
    public void testGetMachineCode() {
        int code = Systems.getMachineCode();
        // just verify it doesn't throw
        assertNotNull(code);
    }

    @Test
    public void testGetProcessCode() {
        int code = Systems.getProcessCode();
        assertTrue(code >= 0);
    }
}

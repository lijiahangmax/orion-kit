package cn.orionsec.kit.lang.utils.ext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * PropertiesExt 单元测试
 */
public class PropertiesExtTest {

    private File tempFile;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test-props", ".properties");
        tempFile.deleteOnExit();
    }

    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testConstructorEmpty() {
        PropertiesExt ext = new PropertiesExt();
        assertNotNull(ext.getProperties());
        assertTrue(ext.getProperties().isEmpty());
    }

    @Test
    public void testConstructorWithMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        PropertiesExt ext = new PropertiesExt(map);
        assertEquals("value1", ext.getValue("key1"));
        assertEquals("value2", ext.getValue("key2"));
    }

    @Test
    public void testConstructorWithInputStream() {
        String content = "name=test\nversion=1.0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        PropertiesExt ext = new PropertiesExt(in);
        assertEquals("test", ext.getValue("name"));
        assertEquals("1.0", ext.getValue("version"));
    }

    @Test
    public void testGetValueWithDefault() {
        PropertiesExt ext = new PropertiesExt();
        assertNull(ext.getValue("missing"));
        assertEquals("default", ext.getValue("missing", "default"));
    }

    @Test
    public void testSetValue() {
        PropertiesExt ext = new PropertiesExt();
        ext.setValue("k", "v");
        assertEquals("v", ext.getValue("k"));
    }

    @Test
    public void testGetValues() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        PropertiesExt ext = new PropertiesExt(map);
        assertEquals(2, ext.getValues().size());
    }

    @Test
    public void testGetKeys() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        PropertiesExt ext = new PropertiesExt(map);
        assertEquals(2, ext.getKeys().size());
        assertTrue(ext.getKeys().contains("a"));
        assertTrue(ext.getKeys().contains("b"));
    }

    @Test
    public void testWriteToFile() throws IOException {
        PropertiesExt ext = new PropertiesExt();
        ext.setValue("file.key", "file.value");
        ext.writeToFile(tempFile);
        // Read back
        PropertiesExt read = new PropertiesExt(tempFile);
        assertEquals("file.value", read.getValue("file.key"));
    }

    @Test
    public void testWriteToXml() throws IOException {
        File xmlFile = File.createTempFile("test-props", ".xml");
        xmlFile.deleteOnExit();
        PropertiesExt ext = new PropertiesExt();
        ext.setValue("xml.key", "xml.value");
        ext.writeToXml(xmlFile);
        assertTrue(xmlFile.length() > 0);
        xmlFile.delete();
    }

    @Test
    public void testLoadSystem() {
        PropertiesExt sys = PropertiesExt.loadSystem();
        assertNotNull(sys.getValue("java.version"));
    }

    @Test
    public void testForEach() {
        Map<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        PropertiesExt ext = new PropertiesExt(map);
        int[] count = {0};
        ext.forEach((k, v) -> count[0]++);
        assertEquals(1, count[0]);
    }
}

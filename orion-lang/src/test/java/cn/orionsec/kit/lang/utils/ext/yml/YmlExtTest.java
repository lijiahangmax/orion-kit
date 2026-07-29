package cn.orionsec.kit.lang.utils.ext.yml;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * YmlExt 单元测试 - 基于内存中的字符串解析
 */
public class YmlExtTest {

    private static final String SIMPLE_YML = "server:\n" +
            "  port: 8080\n" +
            "  host: localhost\n" +
            "app:\n" +
            "  name: test-app\n" +
            "  version: 1.0\n";

    @Test
    public void testLoadString() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        assertNotNull(yml);
    }

    @Test
    public void testGetKeys() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        Set<String> keys = yml.getKeys();
        assertNotNull(keys);
        assertTrue(keys.contains("server"));
        assertTrue(keys.contains("app"));
    }

    @Test
    public void testGetValue() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        String port = yml.getValue("server.port");
        assertEquals("8080", port);
    }

    @Test
    public void testGetValueNested() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        String host = yml.getValue("server.host");
        assertEquals("localhost", host);
    }

    @Test
    public void testGetValueSingleKey() {
        String simpleYml = "name: hello\n";
        YmlExt yml = YmlExt.load(simpleYml);
        String name = yml.getValue("name");
        assertEquals("hello", name);
    }

    @Test
    public void testGetValues() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        Map<String, Object> values = yml.getValues();
        assertNotNull(values);
        assertEquals(2, values.size());
    }

    @Test
    public void testGetValuesWithKey() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        Map<String, Object> server = yml.getValues("server");
        assertNotNull(server);
        assertEquals("8080", String.valueOf(server.get("port")));
    }

    @Test
    public void testGetKeysWithKey() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        Set<String> keys = yml.getKeys("server");
        assertNotNull(keys);
        assertTrue(keys.contains("port"));
        assertTrue(keys.contains("host"));
    }

    @Test
    public void testGetMap() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        assertNotNull(yml.getMap());
        assertFalse(yml.getMap().isEmpty());
    }

    @Test
    public void testGetYaml() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        assertNotNull(yml.getYaml());
    }

    @Test
    public void testForEach() {
        YmlExt yml = YmlExt.load(SIMPLE_YML);
        int[] count = {0};
        yml.forEach((k, v) -> count[0]++);
        assertEquals(2, count[0]);
    }
}

package cn.orionsec.kit.lang.utils.json;

import cn.orionsec.kit.lang.utils.json.matcher.ReplacementFormatters;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Jsons 工具类测试
 */
public class JsonsTest {

    @Test
    public void testToJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "test");
        map.put("age", 25);
        String json = Jsons.toJson(map);
        assertNotNull(json);
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"test\""));
        assertTrue(json.contains("25"));
    }

    @Test
    public void testToJsonNull() {
        String json = Jsons.toJson(null);
        assertEquals("null", json);
    }

    @Test
    public void testParse() {
        String json = "{\"name\":\"test\",\"age\":25}";
        Map result = Jsons.parse(json, Map.class);
        assertNotNull(result);
        assertEquals("test", result.get("name"));
    }

    @Test
    public void testParseBlank() {
        assertNull(Jsons.parse("", Map.class));
        assertNull(Jsons.parse(null, Map.class));
    }

    @Test
    public void testToList() {
        String json = "[1,2,3]";
        List<Integer> list = Jsons.toList(json, Integer.class);
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(1), list.get(0));
    }

    @Test
    public void testToListBlank() {
        List<String> list = Jsons.toList("", String.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testToMap() {
        String json = "{\"key1\":\"value1\",\"key2\":123}";
        Map<String, Object> map = Jsons.toMap(json);
        assertEquals("value1", map.get("key1"));
        assertEquals(123, map.get("key2"));
    }

    @Test
    public void testToMapBlank() {
        Map<String, Object> map = Jsons.toMap("");
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testReadPath() {
        String json = "{\"user\":{\"name\":\"test\"}}";
        Object result = Jsons.readPath(json, "$.user.name");
        assertEquals("test", result);
    }

    @Test
    public void testReadPathInvalid() {
        Object result = Jsons.readPath("invalid", "$.x");
        assertNull(result);
    }

    @Test
    public void testReplacementFormatters() {
        String json = "{\"name\":\"hello\",\"msg\":\"world\"}";
        String template = "Name: ${$.name}, Msg: ${$.msg}";
        String result = ReplacementFormatters.format(template, json);
        assertEquals("Name: hello, Msg: world", result);
    }
}

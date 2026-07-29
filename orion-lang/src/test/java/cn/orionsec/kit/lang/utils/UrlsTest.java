package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UrlsTest {

    @Test
    public void testGetUrlSource() {
        assertEquals("file.jpg", Urls.getUrlSource("http://example.com/path/file.jpg?a=1"));
        assertEquals("page.html", Urls.getUrlSource("http://example.com/page.html"));
    }

    @Test
    public void testBuildQueryString() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("a", "1");
        params.put("b", "2");
        String result = Urls.buildQueryString(params);
        assertEquals("a=1&b=2", result);
    }

    @Test
    public void testBuildQueryStringEmpty() {
        assertEquals("", Urls.buildQueryString(null));
        assertEquals("", Urls.buildQueryString(new LinkedHashMap<>()));
    }

    @Test
    public void testGetQueryString() {
        Map<String, String> result = Urls.getQueryString("http://example.com?a=1&b=2");
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
    }

    @Test
    public void testGetQueryStringBlank() {
        Map<String, String> result = Urls.getQueryString("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testQuery() {
        String value = Urls.query("http://example.com?name=test&age=20", "name");
        assertEquals("test", value);
    }

    @Test
    public void testEncode() {
        String result = Urls.encode("hello world");
        assertNotNull(result);
        assertTrue(result.contains("+") || result.contains("%"));
    }

    @Test
    public void testDecode() {
        String encoded = Urls.encode("hello world");
        String decoded = Urls.decode(encoded);
        assertEquals("hello world", decoded);
    }

    @Test
    public void testEncodeDecodeRoundTrip() {
        String original = "key=value&foo=bar baz";
        String encoded = Urls.encode(original);
        String decoded = Urls.decode(encoded);
        assertEquals(original, decoded);
    }
}

package cn.orionsec.kit.lang.utils.ext.dom;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * DomExt 单元测试 - 基于内存中的XML字符串解析
 */
public class DomExtTest {

    private static final String SIMPLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root>\n" +
            "  <name>test</name>\n" +
            "  <version>1.0</version>\n" +
            "  <items>\n" +
            "    <item id=\"1\">first</item>\n" +
            "    <item id=\"2\">second</item>\n" +
            "  </items>\n" +
            "</root>";

    @Test
    public void testOfString() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        assertNotNull(ext);
        assertNotNull(ext.getDocument());
    }

    @Test
    public void testGetRootElement() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        assertNotNull(ext.getRootElement());
        assertEquals("root", ext.getRootElement().getName());
    }

    @Test
    public void testParseValue() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        String value = ext.parseValue("name");
        assertEquals("test", value);
    }

    @Test
    public void testParseElement() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        org.dom4j.Element el = ext.parse("name");
        assertNotNull(el);
        assertEquals("name", el.getName());
        assertEquals("test", el.getText());
    }

    @Test
    public void testToXml() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        String xml = ext.toXml();
        assertNotNull(xml);
        assertTrue(xml.contains("<root>") || xml.contains("<root"));
    }

    @Test
    public void testToMap() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        Map<String, Object> map = ext.toMap();
        assertNotNull(map);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testToDomNode() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        Map<String, DomNode> nodes = ext.toDomNode();
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }

    @Test
    public void testStream() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        DomStream stream = ext.stream();
        assertNotNull(stream);
    }

    @Test
    public void testFormat() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        String formatted = ext.format();
        assertNotNull(formatted);
        assertTrue(formatted.length() > 0);
    }

    @Test
    public void testCleanComment() {
        String xmlWithComment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root><!-- comment --><name>test</name></root>";
        DomExt ext = DomExt.of(xmlWithComment);
        ext.cleanComment();
        String result = ext.toXml();
        assertFalse(result.contains("<!-- comment -->"));
    }

    @Test
    public void testGetRootElements() {
        DomExt ext = DomExt.of(SIMPLE_XML);
        Map<String, ?> elements = ext.getRootElements();
        assertNotNull(elements);
    }
}

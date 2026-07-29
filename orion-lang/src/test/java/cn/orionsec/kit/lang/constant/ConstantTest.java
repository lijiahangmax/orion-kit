package cn.orionsec.kit.lang.constant;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 常量类单元测试
 */
public class ConstantTest {

    // ==================== Const ====================

    @Test
    public void testConstLettersNotNull() {
        assertNotNull(Const.LETTERS);
        assertEquals(26, Const.LETTERS.length);
        assertEquals("A", Const.LETTERS[0]);
        assertEquals("Z", Const.LETTERS[25]);
    }

    @Test
    public void testConstStringConstants() {
        assertEquals("\r", Const.CR);
        assertEquals("\n", Const.LF);
        assertEquals("\r\n", Const.CR_LF);
        assertEquals("\t", Const.TAB);
        assertEquals("#", Const.POUND);
        assertEquals("$", Const.DOLLAR);
        assertEquals("-", Const.DASHED);
        assertEquals("/", Const.SLASH);
        assertEquals("\\", Const.BACKSLASH);
        assertEquals("\"", Const.QUOTE);
        assertEquals("'", Const.SINGLE_QUOTE);
        assertEquals("_", Const.UNDERLINE);
        assertEquals(":", Const.COLON);
        assertEquals(",", Const.COMMA);
        assertEquals("+", Const.PLUS);
        assertEquals("=", Const.EQUALS);
        assertEquals("|", Const.PIPE);
        assertEquals(";", Const.SEMICOLON);
        assertEquals("!", Const.EXCLAMATION);
        assertEquals("%", Const.PERCENT);
        assertEquals("^", Const.CARET);
        assertEquals("?", Const.QUESTION);
        assertEquals("&", Const.AMP);
        assertEquals("@", Const.AT);
        assertEquals("*", Const.ASTERISK);
        assertEquals("", Const.EMPTY);
        assertEquals(" ", Const.SPACE);
        assertEquals(".", Const.DOT);
        assertEquals("..", Const.DOT_2);
        assertEquals("...", Const.OMIT);
    }

    @Test
    public void testConstCharsetConstants() {
        assertEquals("US-ASCII", Const.ASCII);
        assertEquals("GBK", Const.GBK);
        assertEquals("GB2312", Const.GB_2312);
        assertEquals("UTF-8", Const.UTF_8);
        assertEquals("UTF-16BE", Const.UTF_16BE);
        assertEquals("UTF-16LE", Const.UTF_16LE);
        assertEquals("ISO-8859-1", Const.ISO_8859_1);
    }

    @Test
    public void testConstBufferSizes() {
        assertEquals(1024, Const.BUFFER_KB_1);
        assertEquals(2048, Const.BUFFER_KB_2);
        assertEquals(4096, Const.BUFFER_KB_4);
        assertEquals(8192, Const.BUFFER_KB_8);
        assertEquals(16384, Const.BUFFER_KB_16);
        assertEquals(32768, Const.BUFFER_KB_32);
        assertEquals(65536, Const.BUFFER_KB_64);
    }

    @Test
    public void testConstNumbers() {
        assertEquals(Integer.valueOf(-1), Const.N_N_1);
        assertEquals(Integer.valueOf(0), Const.N_0);
        assertEquals(Integer.valueOf(1), Const.N_1);
        assertEquals(Integer.valueOf(10), Const.N_10);
        assertEquals(Integer.valueOf(100), Const.N_100);
        assertEquals(Long.valueOf(-1L), Const.L_N_1);
        assertEquals(Long.valueOf(0L), Const.L_0);
        assertEquals(Long.valueOf(1L), Const.L_1);
    }

    @Test
    public void testConstSuffixes() {
        assertEquals("csv", Const.SUFFIX_CSV);
        assertEquals("xlsx", Const.SUFFIX_XLSX);
        assertEquals("pdf", Const.SUFFIX_PDF);
        assertEquals("java", Const.SUFFIX_JAVA);
        assertEquals("json", Const.SUFFIX_JSON);
        assertEquals("zip", Const.SUFFIX_ZIP);
    }

    @Test
    public void testConstProtocols() {
        assertEquals("http", Const.PROTOCOL_HTTP);
        assertEquals("https", Const.PROTOCOL_HTTPS);
        assertEquals("ftp", Const.PROTOCOL_FTP);
        assertEquals("file", Const.PROTOCOL_FILE);
    }

    @Test
    public void testConstHttpCodes() {
        assertEquals(Integer.valueOf(200), Const.HTTP_OK_CODE);
        assertEquals(Integer.valueOf(400), Const.HTTP_BAD_REQUEST_CODE);
        assertEquals(Integer.valueOf(403), Const.HTTP_FORBIDDEN_CODE);
        assertEquals(Integer.valueOf(404), Const.HTTP_NOT_FOUND_CODE);
        assertEquals(Integer.valueOf(500), Const.HTTP_ERROR_CODE);
    }

    // ==================== OrionConst ====================

    @Test
    public void testOrionConstValues() {
        assertEquals("orion", OrionConst.ORION);
        assertEquals("orion-kit", OrionConst.ORION_KIT);
        assertEquals(".orion", OrionConst.ORION_DISPLAY);
        assertNotNull(OrionConst.ORION_KIT_VERSION);
        assertFalse(OrionConst.ORION_KIT_VERSION.isEmpty());
        assertEquals("Jiahang Li", OrionConst.ORION_AUTHOR);
        assertNotNull(OrionConst.ORION_EMAIL);
        assertNotNull(OrionConst.ORION_GITHUB);
        assertNotNull(OrionConst.ORION_GITEE);
    }

    // ==================== Letters ====================

    @Test
    public void testLettersValues() {
        assertEquals('\n', Letters.LF);
        assertEquals('\r', Letters.CR);
        assertEquals('"', Letters.QUOTE);
        assertEquals('\'', Letters.SINGLE_QUOTE);
        assertEquals('_', Letters.UNDERLINE);
        assertEquals('-', Letters.DASHED);
        assertEquals(':', Letters.COLON);
        assertEquals(',', Letters.COMMA);
        assertEquals(' ', Letters.SPACE);
        assertEquals('.', Letters.DOT);
        assertEquals('\t', Letters.TAB);
        assertEquals('#', Letters.POUND);
        assertEquals('$', Letters.DOLLAR);
        assertEquals('/', Letters.SLASH);
        assertEquals('\\', Letters.BACKSLASH);
        assertEquals('&', Letters.AMP);
        assertEquals('@', Letters.AT);
        assertEquals('*', Letters.ASTERISK);
        assertEquals('\0', Letters.NULL);
    }

    // ==================== StandardContentType ====================

    @Test
    public void testStandardContentType() {
        assertEquals("Content-Type", StandardContentType.CONTENT_TYPE);
        assertEquals("*/*", StandardContentType.ALL);
        assertEquals("text/html", StandardContentType.TEXT_HTML);
        assertEquals("text/plain", StandardContentType.TEXT_PLAIN);
        assertEquals("application/json", StandardContentType.APPLICATION_JSON);
        assertEquals("application/xml", StandardContentType.APPLICATION_XML);
        assertEquals("application/octet-stream", StandardContentType.APPLICATION_STREAM);
        assertEquals("multipart/form-data", StandardContentType.MULTIPART_FORM);
    }

    // ==================== StandardHttpHeader ====================

    @Test
    public void testStandardHttpHeader() {
        assertEquals("Host", StandardHttpHeader.HOST);
        assertEquals("User-Agent", StandardHttpHeader.USER_AGENT);
        assertEquals("Content-Type", StandardHttpHeader.CONTENT_TYPE);
        assertEquals("Content-Length", StandardHttpHeader.CONTENT_LENGTH);
        assertEquals("Accept", StandardHttpHeader.ACCEPT);
        assertEquals("Authorization", StandardHttpHeader.AUTHORIZATION);
        assertEquals("Cookie", StandardHttpHeader.COOKIE);
        assertEquals("Set-Cookie", StandardHttpHeader.SET_COOKIE);
        assertEquals("Cache-Control", StandardHttpHeader.CACHE_CONTROL);
        assertEquals("Connection", StandardHttpHeader.CONNECTION);
    }

    // ==================== StandardTlsVersion ====================

    @Test
    public void testStandardTlsVersion() {
        assertEquals("TLS", StandardTlsVersion.TLS);
        assertEquals("SSLv3", StandardTlsVersion.SSL_3);
        assertEquals("TLSv1", StandardTlsVersion.TLS_1);
        assertEquals("TLSv1.1", StandardTlsVersion.TLS_1_1);
        assertEquals("TLSv1.2", StandardTlsVersion.TLS_1_2);
        assertEquals("TLSv1.3", StandardTlsVersion.TLS_1_3);
    }

    // ==================== 验证常量非 null ====================

    @Test
    public void testConstantsNotNull() {
        // Const string constants
        assertNotNull(Const.CR);
        assertNotNull(Const.LF);
        assertNotNull(Const.EMPTY);
        assertNotNull(Const.SPACE);
        assertNotNull(Const.LOCALHOST);
        assertNotNull(Const.LOCALHOST_IP_V4);
        assertNotNull(Const.LOCALHOST_IP_V6);
    }

}

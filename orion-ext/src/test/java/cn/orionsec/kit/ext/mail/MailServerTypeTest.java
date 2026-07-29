package cn.orionsec.kit.ext.mail;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MailServerType 单元测试
 */
public class MailServerTypeTest {

    @Test
    public void testWy163HostNotNull() {
        assertNotNull(MailServerType.WY163.getHost());
        assertFalse(MailServerType.WY163.getHost().isEmpty());
    }

    @Test
    public void testWy163Port() {
        assertEquals(465, MailServerType.WY163.getPort());
    }

    @Test
    public void testQqHostNotNull() {
        assertNotNull(MailServerType.QQ.getHost());
        assertFalse(MailServerType.QQ.getHost().isEmpty());
    }

    @Test
    public void testQqPort() {
        assertEquals(465, MailServerType.QQ.getPort());
    }

    @Test
    public void testYd139HostNotNull() {
        assertNotNull(MailServerType.YD139.getHost());
        assertFalse(MailServerType.YD139.getHost().isEmpty());
    }

    @Test
    public void testYd139Port() {
        assertEquals(465, MailServerType.YD139.getPort());
    }

    @Test
    public void testCustomerPort() {
        // CUSTOMER 的 port 配置默认值为 465
        assertEquals(465, MailServerType.CUSTOMER.getPort());
    }

    @Test
    public void testCustomerHostNotNull() {
        // CUSTOMER 的 host 配置默认值为空字符串, 不为 null
        assertNotNull(MailServerType.CUSTOMER.getHost());
    }

    @Test
    public void testEnumValues() {
        MailServerType[] values = MailServerType.values();
        assertEquals(4, values.length);
    }

    @Test
    public void testEnumValueOf() {
        assertEquals(MailServerType.QQ, MailServerType.valueOf("QQ"));
        assertEquals(MailServerType.WY163, MailServerType.valueOf("WY163"));
        assertEquals(MailServerType.YD139, MailServerType.valueOf("YD139"));
        assertEquals(MailServerType.CUSTOMER, MailServerType.valueOf("CUSTOMER"));
    }

    @Test
    public void testImplementsMailServerProvider() {
        for (MailServerType type : MailServerType.values()) {
            assertTrue(type instanceof MailServerProvider);
        }
    }

    @Test
    public void testHostValues() {
        assertEquals("smtp.163.com", MailServerType.WY163.getHost());
        assertEquals("smtp.qq.com", MailServerType.QQ.getHost());
        assertEquals("smtp.139.com", MailServerType.YD139.getHost());
    }
}

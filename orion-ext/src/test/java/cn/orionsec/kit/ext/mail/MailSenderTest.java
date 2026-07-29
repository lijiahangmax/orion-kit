package cn.orionsec.kit.ext.mail;

import cn.orionsec.kit.lang.exception.argument.NullArgumentException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * MailSender 单元测试
 * 注意: 不调用 send() 方法, 避免实际发送邮件
 */
public class MailSenderTest {

    @Test
    public void testConstructorWithServerProvider() {
        MailSender sender = new MailSender(MailServerType.QQ);
        Properties props = sender.getProps();
        assertNotNull(props);
        assertEquals("smtp", props.get("mail.transport.protocol"));
        assertEquals("true", props.get("mail.smtp.auth"));
        assertEquals("smtp.qq.com", props.get("mail.smtp.host"));
        assertEquals("465", props.get("mail.smtp.port"));
    }

    @Test
    public void testConstructorWithHostAndPort() {
        MailSender sender = new MailSender("smtp.example.com", 587);
        Properties props = sender.getProps();
        assertEquals("smtp.example.com", props.get("mail.smtp.host"));
        assertEquals("587", props.get("mail.smtp.port"));
        assertEquals("587", props.get("mail.smtp.socketFactory.port"));
    }

    @Test
    public void testSsl() {
        MailSender sender = new MailSender("smtp.test.com", 465);
        MailSender result = sender.ssl();
        assertSame(sender, result);
        Properties props = sender.getProps();
        assertEquals(true, props.get("mail.smtp.ssl.enable"));
        assertEquals("TLSv1.2", props.get("mail.smtp.ssl.protocols"));
    }

    @Test
    public void testSslWithPort() {
        MailSender sender = new MailSender("smtp.test.com", 25);
        sender.ssl(993);
        Properties props = sender.getProps();
        assertEquals(true, props.get("mail.smtp.ssl.enable"));
        assertEquals(993, props.get("mail.smtp.port"));
        assertEquals(993, props.get("mail.smtp.socketFactory.port"));
        assertEquals("TLSv1.2", props.get("mail.smtp.ssl.protocols"));
    }

    @Test
    public void testDebug() {
        MailSender sender = new MailSender("smtp.test.com", 465);
        MailSender result = sender.debug();
        assertSame(sender, result);
    }

    @Test
    public void testAuth() {
        MailSender sender = new MailSender("smtp.test.com", 465);
        MailSender result = sender.auth("user@test.com", "password123");
        assertSame(sender, result);
    }

    @Test
    public void testPropsKeyValue() {
        MailSender sender = new MailSender("smtp.test.com", 465);
        MailSender result = sender.props("custom.key", "custom.value");
        assertSame(sender, result);
        assertEquals("custom.value", sender.getProps().get("custom.key"));
    }

    @Test
    public void testPropsMap() {
        MailSender sender = new MailSender("smtp.test.com", 465);
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        MailSender result = sender.props(map);
        assertSame(sender, result);
        assertEquals("val1", sender.getProps().get("key1"));
        assertEquals("val2", sender.getProps().get("key2"));
    }

    @Test
    public void testDefaultPropertiesStructure() {
        MailSender sender = new MailSender(MailServerType.WY163);
        Properties props = sender.getProps();
        assertEquals("smtp", props.get("mail.transport.protocol"));
        assertEquals("true", props.get("mail.smtp.auth"));
        assertEquals("smtp.163.com", props.get("mail.smtp.host"));
        assertEquals("465", props.get("mail.smtp.port"));
        assertEquals("465", props.get("mail.smtp.socketFactory.port"));
        assertEquals("javax.net.ssl.SSLSocketFactory", props.get("mail.smtp.socketFactory.class"));
        assertEquals("false", props.get("mail.smtp.socketFactory.fallback"));
        assertEquals(false, props.get("mail.smtp.ssl.enable"));
    }

    @Test(expected = NullArgumentException.class)
    public void testSendWithoutAuthThrowsException() {
        MailSender sender = new MailSender("smtp.test.com", 465);
        MailMessage msg = new MailMessage();
        msg.from("test@test.com").to("to@test.com").title("Test").content("Body");
        // 未设置 auth, 应抛出异常
        sender.send(msg);
    }

    @Test
    public void testConstructorWithCustomProvider() {
        MailServerProvider customProvider = new MailServerProvider() {
            @Override
            public String getHost() {
                return "custom.smtp.server";
            }

            @Override
            public int getPort() {
                return 2525;
            }
        };
        MailSender sender = new MailSender(customProvider);
        Properties props = sender.getProps();
        assertEquals("custom.smtp.server", props.get("mail.smtp.host"));
        assertEquals("2525", props.get("mail.smtp.port"));
    }

    @Test
    public void testFluentConfiguration() {
        MailSender sender = new MailSender(MailServerType.QQ)
                .ssl()
                .debug()
                .auth("user@qq.com", "pass")
                .props("mail.smtp.timeout", "5000");
        Properties props = sender.getProps();
        assertEquals(true, props.get("mail.smtp.ssl.enable"));
        assertEquals("5000", props.get("mail.smtp.timeout"));
    }
}

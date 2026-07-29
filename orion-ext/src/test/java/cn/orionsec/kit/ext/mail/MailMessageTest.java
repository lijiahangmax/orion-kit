package cn.orionsec.kit.ext.mail;

import cn.orionsec.kit.lang.constant.StandardContentType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * MailMessage 单元测试
 */
public class MailMessageTest {

    @Test
    public void testDefaultConstructor() {
        MailMessage msg = new MailMessage();
        assertEquals(StandardContentType.TEXT_PLAIN, msg.getMimeType());
        assertEquals("UTF-8", msg.getContentCharset());
        assertNull(msg.getFrom());
        assertNull(msg.getTo());
        assertNull(msg.getCc());
        assertNull(msg.getBcc());
        assertNull(msg.getTitle());
        assertNull(msg.getContent());
        assertNull(msg.getAttachments());
    }

    @Test
    public void testFrom() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.from("test@example.com");
        assertSame(msg, result);
        assertEquals("test@example.com", msg.getFrom());
    }

    @Test
    public void testToVarargs() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.to("a@test.com", "b@test.com");
        assertSame(msg, result);
        assertEquals(2, msg.getTo().size());
        assertEquals("a@test.com", msg.getTo().get(0));
        assertEquals("b@test.com", msg.getTo().get(1));
    }

    @Test
    public void testToCollection() {
        MailMessage msg = new MailMessage();
        List<String> to = Arrays.asList("a@test.com", "b@test.com");
        msg.to(to);
        assertEquals(2, msg.getTo().size());
        assertTrue(msg.getTo().contains("a@test.com"));
    }

    @Test
    public void testToAccumulates() {
        MailMessage msg = new MailMessage();
        msg.to("a@test.com");
        msg.to("b@test.com");
        assertEquals(2, msg.getTo().size());
    }

    @Test
    public void testCcVarargs() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.cc("cc1@test.com", "cc2@test.com");
        assertSame(msg, result);
        assertEquals(2, msg.getCc().size());
        assertEquals("cc1@test.com", msg.getCc().get(0));
    }

    @Test
    public void testCcCollection() {
        MailMessage msg = new MailMessage();
        List<String> cc = Arrays.asList("cc1@test.com", "cc2@test.com");
        msg.cc(cc);
        assertEquals(2, msg.getCc().size());
    }

    @Test
    public void testBccVarargs() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.bcc("bcc1@test.com", "bcc2@test.com");
        assertSame(msg, result);
        assertEquals(2, msg.getBcc().size());
        assertEquals("bcc1@test.com", msg.getBcc().get(0));
    }

    @Test
    public void testBccCollection() {
        MailMessage msg = new MailMessage();
        List<String> bcc = Arrays.asList("bcc1@test.com", "bcc2@test.com");
        msg.bcc(bcc);
        assertEquals(2, msg.getBcc().size());
    }

    @Test
    public void testTitle() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.title("Test Title");
        assertSame(msg, result);
        assertEquals("Test Title", msg.getTitle());
    }

    @Test
    public void testContent() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.content("Test Content");
        assertSame(msg, result);
        assertEquals("Test Content", msg.getContent());
    }

    @Test
    public void testMimeType() {
        MailMessage msg = new MailMessage();
        msg.mimeType("application/json");
        assertEquals("application/json", msg.getMimeType());
    }

    @Test
    public void testHtml() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.html();
        assertSame(msg, result);
        assertEquals(StandardContentType.TEXT_HTML, msg.getMimeType());
    }

    @Test
    public void testText() {
        MailMessage msg = new MailMessage();
        msg.html();
        msg.text();
        assertEquals(StandardContentType.TEXT_PLAIN, msg.getMimeType());
    }

    @Test
    public void testContentCharset() {
        MailMessage msg = new MailMessage();
        msg.contentCharset("GBK");
        assertEquals("GBK", msg.getContentCharset());
    }

    @Test
    public void testAddLineWhenContentNull() {
        MailMessage msg = new MailMessage();
        msg.addLine("first line");
        assertEquals("first line", msg.getContent());
    }

    @Test
    public void testAddLineWhenContentExists() {
        MailMessage msg = new MailMessage();
        msg.content("hello");
        msg.addLine("world");
        // text/plain 模式下 newLine 是 \n
        assertTrue(msg.getContent().contains("world"));
        assertTrue(msg.getContent().contains("\n"));
    }

    @Test
    public void testAddLineHtmlMode() {
        MailMessage msg = new MailMessage();
        msg.html();
        msg.content("hello");
        msg.addLine("world");
        // html 模式下 newLine 是 <br/>
        assertTrue(msg.getContent().contains("world"));
    }

    @Test
    public void testAddLinesVarargs() {
        MailMessage msg = new MailMessage();
        msg.addLines("line1", "line2", "line3");
        assertNotNull(msg.getContent());
        assertTrue(msg.getContent().contains("line1"));
        assertTrue(msg.getContent().contains("line2"));
        assertTrue(msg.getContent().contains("line3"));
    }

    @Test
    public void testAddLinesCollection() {
        MailMessage msg = new MailMessage();
        List<String> lines = Arrays.asList("lineA", "lineB");
        msg.addLines(lines);
        assertNotNull(msg.getContent());
        assertTrue(msg.getContent().contains("lineA"));
        assertTrue(msg.getContent().contains("lineB"));
    }

    @Test
    public void testAddLinesWhenContentNull() {
        MailMessage msg = new MailMessage();
        msg.addLines("line1");
        assertNotNull(msg.getContent());
        assertTrue(msg.getContent().contains("line1"));
    }

    @Test
    public void testAttachment() {
        MailMessage msg = new MailMessage();
        MailAttachment attachment = new MailAttachment("data".getBytes(), "file.txt");
        MailMessage result = msg.attachment(attachment);
        assertSame(msg, result);
        assertNotNull(msg.getAttachments());
        assertEquals(1, msg.getAttachments().size());
        assertSame(attachment, msg.getAttachments().get(0));
    }

    @Test
    public void testAttachments() {
        MailMessage msg = new MailMessage();
        MailAttachment a1 = new MailAttachment("data1".getBytes(), "f1.txt");
        MailAttachment a2 = new MailAttachment("data2".getBytes(), "f2.txt");
        List<MailAttachment> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        msg.attachments(list);
        assertEquals(2, msg.getAttachments().size());
    }

    @Test
    public void testMessageThreeArgs() {
        MailMessage msg = new MailMessage();
        MailMessage result = msg.message("from@test.com", "Title", "Content");
        assertSame(msg, result);
        assertEquals("from@test.com", msg.getFrom());
        assertEquals("Title", msg.getTitle());
        assertEquals("Content", msg.getContent());
    }

    @Test
    public void testMessageFourArgs() {
        MailMessage msg = new MailMessage();
        msg.message("from@test.com", "to@test.com", "Title", "Content");
        assertEquals("from@test.com", msg.getFrom());
        assertEquals("Title", msg.getTitle());
        assertEquals("Content", msg.getContent());
        assertNotNull(msg.getTo());
        assertTrue(msg.getTo().contains("to@test.com"));
    }

    @Test
    public void testMessageFiveArgs() {
        MailMessage msg = new MailMessage();
        msg.message("from@test.com", "to@test.com", "Title", "Content", StandardContentType.TEXT_HTML);
        assertEquals("from@test.com", msg.getFrom());
        assertEquals("Title", msg.getTitle());
        assertEquals("Content", msg.getContent());
        assertEquals(StandardContentType.TEXT_HTML, msg.getMimeType());
        assertTrue(msg.getTo().contains("to@test.com"));
    }

    @Test
    public void testToString() {
        MailMessage msg = new MailMessage();
        msg.from("sender@test.com").to("receiver@test.com").title("Hello");
        String str = msg.toString();
        assertNotNull(str);
        assertTrue(str.contains("sender@test.com"));
        assertTrue(str.contains("receiver@test.com"));
        assertTrue(str.contains("Hello"));
    }

    @Test
    public void testToStringWithCcAndBcc() {
        MailMessage msg = new MailMessage();
        msg.from("sender@test.com")
                .to("receiver@test.com")
                .cc("cc@test.com")
                .bcc("bcc@test.com")
                .title("Hello");
        String str = msg.toString();
        assertTrue(str.contains("cc@test.com"));
        assertTrue(str.contains("bcc@test.com"));
    }

    @Test
    public void testFluentChaining() {
        MailMessage msg = new MailMessage()
                .from("from@test.com")
                .to("to@test.com")
                .cc("cc@test.com")
                .bcc("bcc@test.com")
                .title("Title")
                .content("Body")
                .html()
                .contentCharset("UTF-8");
        assertEquals("from@test.com", msg.getFrom());
        assertEquals(StandardContentType.TEXT_HTML, msg.getMimeType());
    }
}

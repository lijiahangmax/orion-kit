package cn.orionsec.kit.ext.mail;

import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.exception.argument.NullArgumentException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * MailAttachment 单元测试
 */
public class MailAttachmentTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testDefaultConstructor() {
        MailAttachment attachment = new MailAttachment();
        // 默认 contentType 和 charset
        assertNotNull(attachment);
    }

    @Test
    public void testByteArrayConstructor() {
        byte[] data = "hello world".getBytes();
        MailAttachment attachment = new MailAttachment(data, "test.txt");
        assertNotNull(attachment);
    }

    @Test
    public void testInputStreamConstructor() {
        InputStream is = new ByteArrayInputStream("data".getBytes());
        MailAttachment attachment = new MailAttachment(is, "file.txt");
        assertNotNull(attachment);
    }

    @Test
    public void testInputStreamConstructorWithAutoClose() {
        InputStream is = new ByteArrayInputStream("data".getBytes());
        MailAttachment attachment = new MailAttachment(is, "file.txt", true);
        assertNotNull(attachment);
    }

    @Test
    public void testFileConstructor() throws Exception {
        File tempFile = tempFolder.newFile("testfile.txt");
        MailAttachment attachment = new MailAttachment(tempFile);
        assertNotNull(attachment);
    }

    @Test
    public void testStringPathConstructor() throws Exception {
        File tempFile = tempFolder.newFile("testpath.txt");
        MailAttachment attachment = new MailAttachment(tempFile.getAbsolutePath());
        assertNotNull(attachment);
    }

    @Test
    public void testNameChainMethod() {
        byte[] data = "hello".getBytes();
        MailAttachment attachment = new MailAttachment(data, "original.txt");
        MailAttachment result = attachment.name("renamed.txt");
        assertSame(attachment, result);
    }

    @Test
    public void testBodyChainMethod() {
        MailAttachment attachment = new MailAttachment();
        InputStream is = new ByteArrayInputStream("content".getBytes());
        MailAttachment result = attachment.body(is);
        assertSame(attachment, result);
    }

    @Test
    public void testContentTypeChainMethod() {
        byte[] data = "content".getBytes();
        MailAttachment attachment = new MailAttachment(data, "test.html");
        MailAttachment result = attachment.contentType(StandardContentType.TEXT_HTML);
        assertSame(attachment, result);
    }

    @Test
    public void testCharsetChainMethod() {
        byte[] data = "content".getBytes();
        MailAttachment attachment = new MailAttachment(data, "test.txt");
        MailAttachment result = attachment.charset("GBK");
        assertSame(attachment, result);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullBodyThrowsException() {
        new MailAttachment(null, "test.txt", false);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testBlankNameThrowsException() {
        InputStream is = new ByteArrayInputStream("data".getBytes());
        new MailAttachment(is, "  ", false);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testEmptyNameThrowsException() {
        InputStream is = new ByteArrayInputStream("data".getBytes());
        new MailAttachment(is, "", false);
    }

    @Test(expected = NullArgumentException.class)
    public void testBodyMethodNullThrowsException() {
        MailAttachment attachment = new MailAttachment();
        attachment.body(null);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testNameMethodBlankThrowsException() {
        byte[] data = "hello".getBytes();
        MailAttachment attachment = new MailAttachment(data, "test.txt");
        attachment.name("  ");
    }

    @Test
    public void testGetMimeBodyPart() throws Exception {
        byte[] data = "test attachment content".getBytes();
        MailAttachment attachment = new MailAttachment(data, "document.pdf");
        attachment.contentType("application/pdf");
        // getMimeBodyPart should not throw
        assertNotNull(attachment.getMimeBodyPart());
    }

    @Test
    public void testGetMimeBodyPartWithCharset() throws Exception {
        byte[] data = "中文内容".getBytes("UTF-8");
        MailAttachment attachment = new MailAttachment(data, "中文文件.txt");
        attachment.charset("UTF-8");
        assertNotNull(attachment.getMimeBodyPart());
    }
}

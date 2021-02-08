package com.orion.mail;

import com.orion.constant.Const;
import com.orion.constant.StandardContentType;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 邮件附件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/28 10:51
 */
public class MailAttachment implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private InputStream body;

    /**
     * 类型
     */
    private String contentType = StandardContentType.APPLICATION_STREAM;

    /**
     * 编码
     */
    private String charset = Const.UTF_8;

    public MailAttachment() {
    }

    public MailAttachment(String file) {
        Valid.notBlank(file, "attachment file path is blank");
        this.body = Files1.openInputStreamSafeFast(file);
        this.name = Files1.getFileName(file);
    }

    public MailAttachment(File file) {
        Valid.notNull(file, "attachment file is null");
        this.body = Files1.openInputStreamSafeFast(file);
        this.name = file.getName();
    }

    public MailAttachment(InputStream body, String name) {
        Valid.notNull(body, "attachment body is null");
        Valid.notBlank(name, "attachment file name is blank");
        this.body = body;
        this.name = name;
    }

    public MailAttachment(byte[] body, String name) {
        Valid.notNull(body, "attachment body is null");
        Valid.notBlank(name, "attachment file name is blank");
        this.body = Streams.toInputStream(body);
        this.name = name;
    }

    public MailAttachment name(String name) {
        Valid.notBlank(name, "attachment file name is blank");
        this.name = name;
        return this;
    }

    public MailAttachment body(InputStream body) {
        Valid.notNull(body, "attachment body is null");
        this.body = body;
        return this;
    }

    public MailAttachment contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public MailAttachment charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 获取 MimeBodyPart
     *
     * @return MimeBodyPart
     * @throws Exception on convert error
     */
    public MimeBodyPart getMimeBodyPart() throws Exception {
        MimeBodyPart attache = new MimeBodyPart();
        attache.setDataHandler(new DataHandler(new ByteArrayDataSource(body, contentType)));
        attache.setFileName(MimeUtility.encodeText(name, charset, null));
        return attache;
    }

}

package com.orion.ext.mail;

import com.orion.lang.constant.Const;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;

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
 * @author Jiahang Li
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
    private String contentType;

    /**
     * 编码
     */
    private String charset;

    private boolean autoClose;

    public MailAttachment() {
        this.contentType = StandardContentType.APPLICATION_STREAM;
        this.charset = Const.UTF_8;
    }

    public MailAttachment(String file) {
        this(Files1.openInputStreamFastSafe(file), Files1.getFileName(file), true);
    }

    public MailAttachment(File file) {
        this(Files1.openInputStreamFastSafe(file), file.getName(), true);
    }

    public MailAttachment(byte[] body, String name) {
        this(Streams.toInputStream(body), name, true);
    }

    public MailAttachment(InputStream body, String name) {
        this(body, name, false);
    }

    public MailAttachment(InputStream body, String name, boolean autoClose) {
        this();
        Valid.notNull(body, "attachment body is null");
        Valid.notBlank(name, "attachment file name is blank");
        this.body = body;
        this.name = name;
        this.autoClose = autoClose;
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
        try {
            MimeBodyPart attach = new MimeBodyPart();
            attach.setDataHandler(new DataHandler(new ByteArrayDataSource(body, contentType)));
            attach.setFileName(MimeUtility.encodeText(name, charset, null));
            return attach;
        } finally {
            if (autoClose) {
                Streams.close(body);
            }
        }
    }

}

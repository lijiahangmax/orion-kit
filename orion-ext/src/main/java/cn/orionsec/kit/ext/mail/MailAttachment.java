/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.ext.mail;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

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

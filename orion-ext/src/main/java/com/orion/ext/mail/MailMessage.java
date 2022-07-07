package com.orion.ext.mail;

import com.orion.lang.constant.Const;
import com.orion.lang.constant.Letters;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 邮件配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/15 18:27
 */
public class MailMessage implements Serializable {

    /**
     * 发件人账号
     */
    private String from;

    /**
     * 收件人邮箱
     */
    private List<String> to;

    /**
     * 抄送人邮箱
     */
    private List<String> cc;

    /**
     * 抄秘人邮箱
     */
    private List<String> bcc;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件类型
     */
    private String mimeType;

    /**
     * 内容编码格式
     */
    private String contentCharset;

    /**
     * 附件
     */
    private List<MailAttachment> attachments;

    public MailMessage() {
        this.mimeType = StandardContentType.TEXT_PLAIN;
        this.contentCharset = Const.UTF_8;
    }

    /**
     * 消息
     */
    public MailMessage message(String from, String title, String content) {
        this.from = from;
        this.title = title;
        this.content = content;
        return this;
    }

    /**
     * 消息
     */
    public MailMessage message(String from, String to, String title, String content) {
        this.from = from;
        this.title = title;
        this.content = content;
        return to(to);
    }

    /**
     * 消息
     */
    public MailMessage message(String from, String to, String title, String content, String mimeType) {
        this.from = from;
        this.title = title;
        this.content = content;
        this.mimeType = mimeType;
        return to(to);
    }

    /**
     * 发件人
     *
     * @param from 发件人
     * @return this
     */
    public MailMessage from(String from) {
        this.from = from;
        return this;
    }

    /**
     * 收件人
     *
     * @param to 收件人
     * @return this
     */
    public MailMessage to(String... to) {
        if (this.to == null) {
            this.to = new ArrayList<>();
        }
        this.to.addAll(Arrays.asList(to));
        return this;
    }

    /**
     * 收件人
     *
     * @param to 收件人
     * @return this
     */
    public MailMessage to(Collection<String> to) {
        if (this.to == null) {
            this.to = new ArrayList<>();
        }
        this.to.addAll(to);
        return this;
    }

    /**
     * 抄送人
     *
     * @param cc 抄送人
     * @return this
     */
    public MailMessage cc(String... cc) {
        if (this.cc == null) {
            this.cc = new ArrayList<>();
        }
        this.cc.addAll(Arrays.asList(cc));
        return this;
    }

    /**
     * 抄送人
     *
     * @param cc 抄送人
     * @return this
     */
    public MailMessage cc(Collection<String> cc) {
        if (this.cc == null) {
            this.cc = new ArrayList<>();
        }
        this.cc.addAll(cc);
        return this;
    }

    /**
     * 密抄人
     *
     * @param bcc 密抄人
     * @return this
     */
    public MailMessage bcc(String... bcc) {
        if (this.bcc == null) {
            this.bcc = new ArrayList<>();
        }
        this.bcc.addAll(Arrays.asList(bcc));
        return this;
    }

    /**
     * 密抄人
     *
     * @param bcc 密抄人
     * @return this
     */
    public MailMessage bcc(Collection<String> bcc) {
        if (this.bcc == null) {
            this.bcc = new ArrayList<>();
        }
        this.bcc.addAll(bcc);
        return this;
    }

    /**
     * 标题
     *
     * @param title 标题
     * @return this
     */
    public MailMessage title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 内容
     *
     * @param content 内容
     * @return this
     */
    public MailMessage content(String content) {
        this.content = content;
        return this;
    }

    /**
     * 追加内容
     *
     * @param line 行
     * @return this
     */
    public MailMessage addLine(String line) {
        if (content == null) {
            this.content = line;
        } else {
            this.content += (line + this.newLine());
        }
        return this;
    }

    /**
     * 追加内容
     *
     * @param lines 行
     * @return this
     */
    public MailMessage addLines(String... lines) {
        StringBuilder sb = new StringBuilder(content == null ? Strings.EMPTY : content);
        for (String line : lines) {
            sb.append(line).append(this.newLine());
        }
        this.content = sb.toString();
        return this;
    }

    /**
     * 追加内容
     *
     * @param lines 行
     * @return this
     */
    public MailMessage addLines(Collection<String> lines) {
        StringBuilder sb = new StringBuilder(content == null ? Strings.EMPTY : content);
        for (String line : lines) {
            sb.append(line).append(this.newLine());
        }
        this.content = sb.toString();
        return this;
    }

    /**
     * 邮件类型
     *
     * @param mimeType {@link StandardContentType#TEXT_PLAIN} {@link StandardContentType#TEXT_HTML}
     * @return this
     */
    public MailMessage mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    /**
     * 内容为html
     */
    public MailMessage html() {
        this.mimeType = StandardContentType.TEXT_HTML;
        return this;
    }

    /**
     * 内容为文本
     */
    public MailMessage text() {
        this.mimeType = StandardContentType.TEXT_PLAIN;
        return this;
    }

    /**
     * 内容编码
     *
     * @param contentCharset charset
     * @return this
     */
    public MailMessage contentCharset(String contentCharset) {
        this.contentCharset = contentCharset;
        return this;
    }

    /**
     * 附件
     *
     * @param attachment 附件
     * @return this
     */
    public MailMessage attachment(MailAttachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        return this;
    }

    /**
     * 附件
     *
     * @param attachments 附件
     * @return this
     */
    public MailMessage attachments(Collection<MailAttachment> attachments) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.addAll(attachments);
        return this;
    }

    /**
     * 新起一行
     *
     * @return newLine
     */
    private String newLine() {
        return StandardContentType.TEXT_HTML.equals(mimeType) ? Const.BR : Const.LF;
    }

    // -------------------- getter --------------------

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getContentCharset() {
        return contentCharset;
    }

    public List<MailAttachment> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EML-form: [");
        sb.append(from).append("]").append(Letters.LF);
        sb.append("  ==> to: ").append(to == null ? "[]" : to.toString()).append(Strings.SPACE).append(Letters.LF);
        if (cc != null) {
            sb.append("  ==> cc: ").append(cc.toString()).append(Strings.SPACE).append(Letters.LF);
        }
        if (bcc != null) {
            sb.append("  ==> bcc: ").append(bcc.toString()).append(Strings.SPACE).append(Letters.LF);
        }
        sb.append("  ==> title: [").append(title).append("] ").append(Letters.LF);
        return sb.toString();
    }

}

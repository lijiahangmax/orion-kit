package com.orion.mail;

import com.orion.able.Jsonable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/15 18:27
 */
@SuppressWarnings("ALL")
public class MailMessage implements Serializable, Jsonable {

    private static final String CHARSET = "UTF-8";
    private static final String HTML = "text/html";
    private static final String TEXT = "text/plain";

    /**
     * 发件人账号
     */
    private String from;

    /**
     * 发件服务器用户名
     */
    private String username;

    /**
     * 发件服务器密码
     */
    private String password;

    /**
     * 是否启用ssl
     */
    private Boolean ssl = false;

    /**
     * 使用使用debug模式
     */
    private Boolean debug = false;

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
    private String mimeType = TEXT;

    /**
     * 内容编码格式
     */
    private String contentCharset = CHARSET;

    /**
     * 邮件协议配置
     */
    private MailServer mailServer;

    /**
     * 附件
     */
    private List<File> attachments;

    /**
     * 附件编码格式
     */
    private String attachmentsCharset = CHARSET;

    /**
     * 初始化
     */
    public MailMessage(MailServer mailServer) {
        this.mailServer = mailServer;
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
        return addTo(to);
    }

    /**
     * 消息
     */
    public MailMessage message(String from, String to, String title, String content, String mimeType) {
        this.from = from;
        this.title = title;
        this.content = content;
        this.mimeType = mimeType;
        return addTo(to);
    }

    /**
     * ssl发件
     */
    public MailMessage ssl() {
        this.ssl = true;
        return this;
    }

    /**
     * debug
     */
    public MailMessage debug() {
        this.debug = true;
        return this;
    }

    /**
     * 内容为html
     */
    public MailMessage html() {
        this.mimeType = HTML;
        return this;
    }

    /**
     * 认证
     */
    public MailMessage auth(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * 追加内容
     */
    public MailMessage addLine(String line) {
        if (content == null) {
            content = line;
        } else {
            content += (line + "\n");
        }
        return this;
    }

    /**
     * 追加内容
     */
    public MailMessage addLines(List<String> lines) {
        StringBuilder sb = new StringBuilder(content == null ? "" : content);
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        content = sb.toString();
        return this;
    }

    public String getFrom() {
        return from;
    }

    public MailMessage setFrom(String from) {
        this.from = from;
        return this;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public MailMessage setSsl(Boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public Boolean getDebug() {
        return debug;
    }

    public MailMessage setDebug(Boolean debug) {
        this.debug = debug;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MailMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailMessage setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<String> getTo() {
        return to;
    }

    public MailMessage addTo(String to) {
        if (this.to == null) {
            this.to = new ArrayList<>();
        }
        this.to.add(to);
        return this;
    }

    public MailMessage setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public List<String> getCc() {
        return cc;
    }

    public MailMessage addCc(String cc) {
        if (this.cc == null) {
            this.cc = new ArrayList<>();
        }
        this.cc.add(cc);
        return this;
    }

    public MailMessage setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public MailMessage addBcc(String bcc) {
        if (this.bcc == null) {
            this.bcc = new ArrayList<>();
        }
        this.bcc.add(bcc);
        return this;
    }

    public MailMessage setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MailMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public MailMessage setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public MailServer getMailServer() {
        return mailServer;
    }

    public MailMessage setMailServer(MailServer mailServer) {
        this.mailServer = mailServer;
        return this;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public MailMessage addAttachments(File attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        return this;
    }

    public MailMessage setAttachments(List<File> attachments) {
        this.attachments = attachments;
        return this;
    }

    public String getContentCharset() {
        return contentCharset;
    }

    public MailMessage setContentCharset(String contentCharset) {
        this.contentCharset = contentCharset;
        return this;
    }

    public String getAttachmentsCharset() {
        return attachmentsCharset;
    }

    public MailMessage setAttachmentsCharset(String attachmentsCharset) {
        this.attachmentsCharset = attachmentsCharset;
        return this;
    }

    @Override
    public String toJsonString() {
        return toJSON();
    }

    @Override
    public String toString() {
        return toJSON();
    }

}

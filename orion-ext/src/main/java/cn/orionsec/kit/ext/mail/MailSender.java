/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.able.ISendEvent;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/15 18:25
 */
public class MailSender implements ISendEvent<MailMessage> {

    private final Properties props;

    private boolean debug;

    private PasswordAuthentication authentication;

    public MailSender(MailServerProvider type) {
        this(type.getHost(), type.getPort());
    }

    public MailSender(String serverHost, int serverPort) {
        this.props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", serverHost);
        props.put("mail.smtp.port", serverPort + Strings.EMPTY);
        props.put("mail.smtp.socketFactory.port", serverPort + Strings.EMPTY);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.enable", false);
    }

    /**
     * ssl发送
     *
     * @return this
     */
    public MailSender ssl() {
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return this;
    }

    /**
     * ssl发送
     *
     * @param sslPort ssl端口
     * @return this
     */
    public MailSender ssl(int sslPort) {
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.port", sslPort);
        props.put("mail.smtp.socketFactory.port", sslPort);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return this;
    }

    /**
     * 开启 debug
     *
     * @return this
     */
    public MailSender debug() {
        this.debug = true;
        return this;
    }

    /**
     * 服务器认证
     *
     * @param serverUsername 服务器用户名
     * @param serverPassword 服务器密码
     * @return this
     */
    public MailSender auth(String serverUsername, String serverPassword) {
        this.authentication = new PasswordAuthentication(serverUsername, serverPassword);
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public MailSender props(Object key, Object value) {
        props.put(key, value);
        return this;
    }

    /**
     * 添加参数
     *
     * @param map map
     * @return this
     */
    public MailSender props(Map<? super Object, ? super Object> map) {
        props.putAll(map);
        return this;
    }

    /**
     * 发件
     */
    @Override
    public void send(MailMessage msg) {
        Valid.notNull(authentication, "sender service unauthorized");
        // 会话
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return authentication;
            }
        });
        session.setDebug(debug);
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人
            mimeMessage.setFrom(new InternetAddress(msg.getFrom()));
            // 收件人
            List<String> to = msg.getTo();
            if (to != null && !to.isEmpty()) {
                InternetAddress[] ccAddress = new InternetAddress[to.size()];
                for (int i = 0, size = to.size(); i < size; i++) {
                    ccAddress[i] = new InternetAddress(to.get(i));
                }
                mimeMessage.setRecipients(Message.RecipientType.TO, ccAddress);
            }
            // 抄送者
            List<String> cc = msg.getCc();
            if (cc != null && !cc.isEmpty()) {
                InternetAddress[] ccAddress = new InternetAddress[cc.size()];
                for (int i = 0, size = cc.size(); i < size; i++) {
                    ccAddress[i] = new InternetAddress(cc.get(i));
                }
                mimeMessage.setRecipients(Message.RecipientType.CC, ccAddress);
            }
            // 秘送者
            List<String> bcc = msg.getBcc();
            if (bcc != null && !bcc.isEmpty()) {
                InternetAddress[] bccAddress = new InternetAddress[bcc.size()];
                for (int i = 0, size = bcc.size(); i < size; i++) {
                    bccAddress[i] = new InternetAddress(bcc.get(i));
                }
                mimeMessage.setRecipients(Message.RecipientType.BCC, bccAddress);
            }
            // 主题
            mimeMessage.setSubject(msg.getTitle(), msg.getContentCharset());
            // 内容
            Multipart multipart = new MimeMultipart();
            MimeBodyPart body = new MimeBodyPart();
            body.setContent(msg.getContent(), msg.getMimeType() + ";charset=" + msg.getContentCharset());
            multipart.addBodyPart(body);
            // 附件
            if (msg.getAttachments() != null && !msg.getAttachments().isEmpty()) {
                for (MailAttachment attachment : msg.getAttachments()) {
                    multipart.addBodyPart(attachment.getMimeBodyPart());
                }
            }
            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
            // 发件
            Transport.send(mimeMessage);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Properties getProps() {
        return props;
    }

}

package com.orion.mail;

import com.orion.able.Sendable;
import com.orion.utils.Strings;
import com.orion.utils.Valid;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/15 18:25
 */
public class MailSender implements Sendable<MailMessage> {

    private Properties props;

    private boolean debug;

    private PasswordAuthentication authentication;

    public MailSender(MailServerType type) {
        this(type.getHost(), type.getPort());
    }

    public MailSender(String serverHost, int serverPort) {
        props = new Properties();
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
        props.put("mail.smtp.port", sslPort + Strings.EMPTY);
        props.put("mail.smtp.socketFactory.port", sslPort + Strings.EMPTY);
        return this;
    }

    /**
     * 开启debug
     *
     * @return this
     */
    public MailSender debug() {
        debug = true;
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
        authentication = new PasswordAuthentication(serverUsername, serverPassword);
        return this;
    }

    /**
     * 发件
     */
    @Override
    public boolean send(MailMessage msg) {
        Valid.notNull(authentication, "unauthorized");
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
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

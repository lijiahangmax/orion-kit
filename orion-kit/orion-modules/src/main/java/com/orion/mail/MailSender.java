package com.orion.mail;

import com.orion.able.SendAble;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
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
public class MailSender implements SendAble<MailMessage> {

    public static MailSender getInstance() {
        return SenderInstance.MAIL_SENDER;
    }

    /**
     * 发件
     */
    @Override
    public boolean send(MailMessage msg) {
        MailServer mailServer = msg.getMailServer();
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", mailServer.getSmtpHost());
        props.put("mail.smtp.port", mailServer.getSmtpPort().toString());
        props.put("mail.smtp.socketFactory.port", mailServer.getSmtpPort().toString());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.enable", msg.getSsl());

        // 会话
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(msg.getUsername(), msg.getPassword());
            }
        });
        session.setDebug(msg.getDebug());
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
                for (File file : msg.getAttachments()) {
                    MimeBodyPart attache = new MimeBodyPart();
                    attache.setDataHandler(new DataHandler(new FileDataSource(file)));
                    attache.setFileName(MimeUtility.encodeText(file.getName(), msg.getAttachmentsCharset(), null));
                    multipart.addBodyPart(attache);
                }
            }
            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
            // 发件
            Transport.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    /**
     * 单例
     */
    private static class SenderInstance {
        private static final MailSender MAIL_SENDER = new MailSender();

        private SenderInstance() {
        }
    }

}

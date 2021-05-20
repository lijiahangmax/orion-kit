package com.orion.test.mail;

import com.orion.mail.MailAttachment;
import com.orion.mail.MailMessage;
import com.orion.mail.MailSender;
import com.orion.mail.MailServerType;
import com.orion.utils.io.Files1;
import org.junit.Test;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/28 9:56
 */
public class MailTests {

    @Test
    public void sendQQ() {
        MailSender sender = new MailSender(MailServerType.QQ).debug();
        sender.auth("ljh1553488six@qq.com", "");
        MailMessage msg = new MailMessage();
        msg.from("ljh1553488six@qq.com")
                .to("")
                .cc("")
                .bcc("")
                .title("标题")
                .html()
                .content("<hr/><br/><p>text</p><br/><hr/>")
                .addLine("-------------------")
                .addLine("--------end--------")
                .addLine("-------------------")
                .attachment(new MailAttachment("C:\\Users\\ljh15\\Desktop\\tmp.txt"))
                .attachment(new MailAttachment(new File("C:\\Users\\ljh15\\Desktop\\新增&列表.txt")))
                .attachment(new MailAttachment(Files1.openInputStreamSafe(new File("C:\\Users\\ljh15\\Desktop\\sql.txt")), "sql.md"))
                .attachment(new MailAttachment("fq".getBytes(), "1.file"));
        System.out.println(msg);
        sender.send(msg);
    }

}

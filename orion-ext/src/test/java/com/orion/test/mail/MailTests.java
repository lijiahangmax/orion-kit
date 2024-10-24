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
package com.orion.test.mail;

import com.orion.ext.mail.MailAttachment;
import com.orion.ext.mail.MailMessage;
import com.orion.ext.mail.MailSender;
import com.orion.ext.mail.MailServerType;
import com.orion.lang.utils.io.Files1;
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

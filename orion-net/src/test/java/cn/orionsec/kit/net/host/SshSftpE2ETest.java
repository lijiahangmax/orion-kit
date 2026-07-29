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
package cn.orionsec.kit.net.host;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutors;
import org.junit.*;

import java.io.IOException;

/**
 * SSH / SFTP 端到端测试
 * <p>
 * 通过环境变量提供连接信息, 未配置时自动跳过 (CI 安全):
 * ORION_SSH_HOST / ORION_SSH_PORT / ORION_SSH_USER / ORION_SSH_PASSWORD
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/30 0:00
 */
public class SshSftpE2ETest {

    private SessionStore store;

    @Before
    public void init() {
        String host = System.getenv("ORION_SSH_HOST");
        String user = System.getenv("ORION_SSH_USER");
        String password = System.getenv("ORION_SSH_PASSWORD");
        String portEnv = System.getenv("ORION_SSH_PORT");
        Assume.assumeTrue("ORION_SSH_* 环境变量未配置, 跳过 SSH/SFTP 端到端测试",
                host != null && !host.isEmpty()
                        && user != null && !user.isEmpty()
                        && password != null && !password.isEmpty());
        int port = (portEnv == null || portEnv.isEmpty()) ? 22 : Integer.parseInt(portEnv);
        SessionHolder holder = SessionHolder.create();
        holder.setLogger(SessionLogger.ERROR);
        this.store = holder.getSession(host, port, user)
                .password(password)
                .timeout(20000)
                .connect(20000);
    }

    @After
    public void destroy() {
        if (store != null) {
            store.close();
        }
    }

    @Test
    public void testSshCommand() throws IOException {
        CommandExecutor executor = store.getCommandExecutor("echo orion-ssh-ok");
        executor.merge();
        executor.connect();
        String output = CommandExecutors.getCommandOutputResultString(executor);
        Assert.assertTrue("ssh command output: " + output, output.contains("orion-ssh-ok"));
        Assert.assertEquals(0, executor.getExitCode());
        executor.close();
    }

    @Test
    public void testSftpOperations() throws IOException {
        SftpExecutor sftp = store.getSftpExecutor();
        sftp.connect(20000);
        sftp.charset(Const.UTF_8);
        String dir = "/tmp/orion-e2e-" + System.currentTimeMillis();
        String path = dir + "/hello.txt";
        try {
            sftp.makeDirectories(dir);
            sftp.write(path, "orion-sftp-ok");
            Assert.assertTrue(sftp.isExist(path));
            byte[] buffer = new byte[64];
            int read = sftp.read(path, buffer);
            Assert.assertEquals("orion-sftp-ok", new String(buffer, 0, read));
        } finally {
            sftp.remove(dir);
            sftp.disconnect();
        }
    }

}

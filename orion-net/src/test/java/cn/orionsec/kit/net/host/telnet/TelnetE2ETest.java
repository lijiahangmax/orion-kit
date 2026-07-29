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
package cn.orionsec.kit.net.host.telnet;

import cn.orionsec.kit.net.host.ssh.command.CommandExecutors;
import org.junit.*;

import java.io.IOException;

/**
 * Telnet 端到端测试
 * <p>
 * 通过环境变量提供连接信息, 未配置时自动跳过 (CI 安全):
 * ORION_SSH_HOST / ORION_SSH_PORT / ORION_SSH_USER / ORION_SSH_PASSWORD / ORION_TELNET_PORT
 * <p>
 * 初始化时通过 SSH 在目标 alpine 服务器安装并启动 telnetd (幂等), 再经 telnet 端口验证执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public class TelnetE2ETest {

    /**
     * 通过 SSH 安装并启动 telnetd 的幂等命令
     */
    private static final String SETUP_TELNETD_COMMAND = "which telnetd || apk add --no-cache busybox-extras; " +
            "grep -q 'pts/0' /etc/securetty 2>/dev/null || for i in 0 1 2 3 4 5 6 7 8 9; do echo pts/$i >> /etc/securetty; done; " +
            "pgrep telnetd >/dev/null || telnetd -p 23; " +
            "echo orion-telnetd-ready";

    private TelnetSession session;

    @Before
    public void init() throws IOException {
        String host = System.getenv("ORION_SSH_HOST");
        String user = System.getenv("ORION_SSH_USER");
        String password = System.getenv("ORION_SSH_PASSWORD");
        String sshPortEnv = System.getenv("ORION_SSH_PORT");
        String telnetPortEnv = System.getenv("ORION_TELNET_PORT");
        Assume.assumeTrue("ORION_SSH_* 环境变量未配置, 跳过 telnet 端到端测试",
                host != null && !host.isEmpty()
                        && user != null && !user.isEmpty()
                        && password != null && !password.isEmpty());
        int sshPort = (sshPortEnv == null || sshPortEnv.isEmpty()) ? 22 : Integer.parseInt(sshPortEnv);
        int telnetPort = (telnetPortEnv == null || telnetPortEnv.isEmpty()) ? TelnetSession.DEFAULT_TELNET_PORT : Integer.parseInt(telnetPortEnv);
        // 通过 ssh 安装并启动 telnetd
        String setupResult = CommandExecutors.getCommandOutputResult(host, sshPort, user, password, SETUP_TELNETD_COMMAND);
        Assert.assertTrue("telnetd setup output: " + setupResult, setupResult.contains("orion-telnetd-ready"));
        // 建立 telnet 会话 (root 提示符为 #)
        this.session = TelnetSession.create(host, telnetPort)
                .username(user)
                .password(password)
                .prompt("#")
                .timeout(20000)
                .readTimeout(20000)
                .connect();
    }

    @After
    public void destroy() {
        if (session != null) {
            session.close();
        }
    }

    @Test
    public void testTelnetCommand() throws IOException {
        TelnetCommandExecutor executor = session.getCommandExecutor("echo orion-telnet-ok");
        String output = TelnetExecutors.getCommandOutputResultString(executor);
        Assert.assertTrue("telnet command output: " + output, output.contains("orion-telnet-ok"));
        Assert.assertTrue(executor.isDone());
    }

    @Test
    public void testTelnetUname() throws IOException {
        TelnetCommandExecutor executor = session.getCommandExecutor("uname -s");
        String output = TelnetExecutors.getCommandOutputResultString(executor);
        Assert.assertTrue("telnet uname output: " + output, output.contains("Linux"));
    }

    @Test
    public void testTelnetShell() throws IOException {
        TelnetShellExecutor executor = session.getShellExecutor();
        Assert.assertTrue(executor.isConnected());
        // 交互式写入命令并读取输出
        executor.writeLine("echo orion-shell-ok");
        String output = executor.readUntilPrompt();
        Assert.assertTrue("telnet shell output: " + output, output.contains("orion-shell-ok"));
    }

}

package com.orion.test.remote.channel;

import com.orion.remote.channel.SessionHolder;
import com.orion.remote.channel.SessionLogger;
import com.orion.remote.channel.sftp.FileAttribute;
import com.orion.remote.channel.sftp.SftpExecutor;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class SftpExecutorTests {

    private SftpExecutor e;

    @Before
    public void before() {
        SessionHolder.setLogger(SessionLogger.INFO);
        e = SessionHolder.getSession("root", "192.168.146.230")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getSftpExecutor();
        e.connect(20000);
    }

    @After
    public void after() {
        e.disconnect();
    }

    @Test
    public void list() {
        for (FileAttribute a : e.listFiles("/root", true)) {
            System.out.println(a);
        }
        System.out.println("---------");
        for (FileAttribute a : e.listFilesSuffix("/root", "g", true, true)) {
            System.out.println(a);
        }
        System.out.println("---------");
        for (FileAttribute a : e.listDirs("/root", true)) {
            System.out.println(a);
        }
        System.out.println("---------");
    }

    @Test
    public void read() throws IOException {
        byte[] bs = new byte[20];
        int read = e.read("/root/file1", 0, bs);
        System.out.println(new String(bs, 0, read));
        read = e.read("/root/file1", 2, bs);
        System.out.println(new String(bs, 0, read));
    }

    @Test
    public void transfer() throws IOException {
        long transfer = e.transfer("/root/file1", System.out);
        System.out.println(transfer);
        transfer = e.transfer("/root/file1", System.out, 3, 5);
        System.out.println(transfer);
        transfer = e.transfer("/root/file1", System.out, 3);
        System.out.println(transfer);
    }

    @Test
    public void write() throws IOException {
        e.write("/root/file2", Strings.bytes("a"));
        e.writeLine("/root/file3", "123");
        e.writeLines("/root/file4", Lists.of("123", "345"));
    }

    @Test
    public void append() throws IOException {
        e.append("/root/file2", Strings.bytes("a"));
        e.appendLine("/root/file3", "123");
        e.appendLines("/root/file4", Lists.of("123", "345"));
    }

}

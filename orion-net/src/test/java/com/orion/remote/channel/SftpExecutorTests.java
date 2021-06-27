package com.orion.remote.channel;

import com.alibaba.fastjson.JSON;
import com.orion.constant.Const;
import com.orion.remote.channel.sftp.SftpExecutor;
import com.orion.remote.channel.sftp.SftpFile;
import com.orion.remote.channel.sftp.bigfile.SftpDownload;
import com.orion.remote.channel.sftp.bigfile.SftpUpload;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class SftpExecutorTests {

    private SftpExecutor e;

    @Before
    public void before() {
        SessionHolder.setLogger(SessionLogger.ERROR);
        e = SessionHolder.getSession("192.168.146.230", "root")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getSftpExecutor();
        e.connect(20000)
                .charset(Const.UTF_8);
    }

    @Test
    public void create() {
        System.out.println(e.mkdirs("/root/1/2/3/4/5"));
        System.out.println(e.touch("/root/a/b/c.txt"));
        System.out.println(e.touchLink("/root/a/b/c.txt", "/root/a/b/d.txt"));
        System.out.println(e.touch("/root/a/b/e.txt", true));
        System.out.println(e.touch("/root/a/b/f.txt", true));
        System.out.println(e.touch("/root/a/b/1/2/g1.txt", true));
        System.out.println(e.touch("/root/a/b/1/2/g2.txt", true));
        System.out.println(e.touch("/root/a/b/1/2/g3.txt", true));
        System.out.println(e.mv("/root/a/b/1/2/g1.txt", "g11.txt"));
        System.out.println(e.mv("/root/a/b/1/2/g2.txt", "../../g22.txt"));
        System.out.println(e.mv("/root/a/b/1/2/g3.txt", "/root/g33.txt"));
    }

    @Test
    public void rm() {
        System.out.println(e.rmFile("/root/a/b/1/2/g1.txt"));
        System.out.println(e.rmdir("/root/a/b/1/2"));
        System.out.println(e.rm("/root/a/b/1"));
    }

    @Test
    public void update() {
        String path = "/root/1";
        SftpFile file = e.getFile(path);
        System.out.println(e.setModifyTime(path, new Date()));
        System.out.println(e.chmod(path, 777));
        System.out.println(e.chown(path, 1));
        System.out.println(e.chgrp(path, 1));
        file.setPermission(444);
        System.out.println(e.setFileAttribute(file));
    }

    @Test
    public void get() {
        System.out.println(e.getHome());
        System.out.println(e.getPath("/root/文本.txt"));
        System.out.println(e.getPath("/etc/yum.conf"));
        System.out.println(e.isExist("/root/文本.txt"));
        System.out.println(e.getFile("/root/文本.txt"));
        System.out.println(JSON.toJSONString(e.getFile("/root/a/b/d.txt", false)));
        System.out.println(JSON.toJSONString(e.getFile("/root/a/b/d.txt", true)));
        System.out.println(e.getLinkPath("/root/a/b/d.txt"));
        System.out.println(e.getLinkPath("/root/文本1.txt"));
        System.out.println(e.getSize("/root/文本.txt"));
        System.out.println(e.getSize("/root/文本1.txt"));
        System.out.println(e.getSize("/root/文本2.txt"));
        System.out.println(e.getSize("/root/文本3.txt"));
        System.out.println(e.truncate("/root/文本.txt"));
        System.out.println(e.truncate("/root/文本2.txt"));
    }

    @Test
    public void list() {
        System.out.println(e.listFiles("/root", true));
        System.out.println("---------");
        System.out.println(e.listFilesSuffix("/root", ".sql", true));
        System.out.println("---------");
        System.out.println(e.listFilesMatch("/root", "f", true));
        System.out.println("---------");
        System.out.println(e.listFilesPattern("/root", Pattern.compile(".*\\.sql"), true));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", s -> s.getSize() > 100000, true));
        System.out.println("---------");
        System.out.println(e.listDirs("/root", true));
        System.out.println("---------");
    }

    @Test
    public void read() throws IOException {
        byte[] bs = new byte[20];
        int read = e.read("/root/file1", 0, bs);
        System.out.println(new String(bs, 0, read));
        read = e.read("/root/file1", 2, bs);
        System.out.println(new String(bs, 0, read));
        read = e.read("/root/file1", 2, bs);
        System.out.println(new String(bs, 0, read));
        System.out.println(e.readLine("/root/x/x/file4"));
        System.out.println(e.readLine("/root/x/x/file4", 2));
        System.out.println(e.readLines("/root/x/x/file4"));
        System.out.println(e.readLines("/root/x/x/file4", 2L));
        System.out.println(e.readLines("/root/x/x/file4", 2));
        System.out.println(e.readLines("/root/x/x/file4", 2L, 2));
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
        e.write("/root/x/x/file2", Strings.bytes("爱是觉得\n"));
        e.writeLine("/root/x/x/file3", "撒是的的去");
        e.writeLines("/root/x/x/file4", Lists.of("123试试", "345", "kkkk", "集合的四海"));
    }

    @Test
    public void append() throws IOException {
        e.append("/root/x/x/file2", Strings.bytes("爱是觉得\n"));
        e.appendLine("/root/x/x/file3", "撒是的的去");
        e.appendLines("/root/x/x/file4", Lists.of("123试试", "345"));
    }

    @Test
    public void upload() throws IOException {
        e.uploadFile("/root/data/sql.txt", "C:\\Users\\ljh15\\Desktop\\sql.txt");
        e.uploadDir("/root/data/ftp/", "C:\\Users\\ljh15\\Desktop\\ftp");
    }

    @Test
    public void download() throws IOException {
        e.downloadFile("/root/data/sql.txt", "C:\\Users\\ljh15\\Desktop\\download\\sql.txt");
        e.downloadDir("/root/data/ftp/", "C:\\Users\\ljh15\\Desktop\\download\\ftp");
    }

    @Test
    public void bigUpload() {
        SftpUpload u = e.upload("/root/a/b/c/a3.rar", new File("C:\\Users\\ljh15\\Desktop\\a.rar"));
        u.getProgress()
                .computeRate()
                .rateAcceptor(pr -> {
                    System.out.println(pr.getProgress() * 100 + "% " + pr.getNowRate() / 1024 + "kb/s");
                })
                .callback(pr -> {
                    System.out.println("done");
                    System.exit(0);
                });
        new Thread(u).start();
        Threads.sleep(30000000L);
    }

    @Test
    public void bigDownload() {
        SftpDownload u = e.download("/root/a/b/c/a.rar", new File("C:\\Users\\ljh15\\Desktop\\a\\b\\c\\a1.rar"));
        u.getProgress()
                .computeRate()
                .rateAcceptor(pr -> {
                    System.out.println(pr.getProgress() * 100 + "% " + pr.getNowRate() / 1024 + "kb/s");
                })
                .callback(pr -> {
                    System.out.println("done");
                    System.exit(0);
                });
        new Thread(u).start();
        Threads.sleep(30000000L);
    }

    @After
    public void after() {
        e.disconnect();
    }

}

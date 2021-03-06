package com.orion.remote.connection;

import com.alibaba.fastjson.JSON;
import com.orion.remote.connection.sftp.SftpExecutor;
import com.orion.remote.connection.sftp.SftpFile;
import com.orion.remote.connection.sftp.bigfile.SftpDownload;
import com.orion.remote.connection.sftp.bigfile.SftpUpload;
import com.orion.utils.Threads;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Streams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/20 12:39
 */
public class SftpTests {

    private ConnectionStore s;
    private SftpExecutor e;

    @Before
    public void init() {
        s = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        e = s.getSftpExecutor();
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
    public void get() {
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
    public void modify() {
        String path = "/root/1";
        SftpFile file = e.getFile(path);
        System.out.println(JSON.toJSONString(file));
        file.setPermission(777);
        e.setFileAttribute(path, file);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chmod(path, 644);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chown(path, 10, 10);
        System.out.println(JSON.toJSONString(e.getFile(path)));
    }

    @Test
    public void list() {
        System.out.println(e.ll("/root"));
        System.out.println("--------");
        System.out.println(e.listFiles("/root"));
        System.out.println("--------");
        System.out.println(e.listFiles("/root", true, true));
        System.out.println("--------");
        System.out.println(e.listDirs("/root"));
        System.out.println("--------");
        System.out.println(e.listDirs("/root", true));
        System.out.println("--------");
        System.out.println(e.listFilesSuffix("/root", ".txt", true));
        System.out.println("--------");
        System.out.println(e.listFilesMatch("/root", "g", true));
        System.out.println("--------");
        System.out.println(e.listFilesPattern("/root", Pattern.compile("g.*"), true));
        System.out.println("--------");
        System.out.println(e.listFilesFilter("/root", (f) -> f.getSize() > 10, true));
        System.out.println("--------");
    }

    @Test
    public void read() throws IOException {
        byte[] bs = new byte[10];
        int read;
        read = e.read("/root/1.txt", bs);
        System.out.println(new String(bs, 0, read));
        System.out.println("-----");
        read = e.read("/root/1.txt", 2, bs);
        System.out.println(new String(bs, 0, read));
        System.out.println("-----");
        read = e.read("/root/1.txt", 2, bs);
        System.out.println(new String(bs, 0, read));
    }

    @Test
    public void transfer() throws IOException {
        e.transfer("/root/1.txt", System.out);
        System.out.println("-----");
        e.transfer("/root/1.txt", System.out, 2);
        System.out.println("-----");
        e.transfer("/root/1.txt", System.out, 2, 4);
    }

    @Test
    public void write() throws IOException {
        e.write("/root/2.txt", "123全文\n".getBytes());
        Threads.sleep(1500);
        e.write("/root/s/s/2.txt", Streams.toInputStream("123让他受到\n"));
        Threads.sleep(1500);
        e.writeLines("/root/s/s/2.txt", Lists.of("123", "456"));
        Threads.sleep(1500);
        e.writeLine("/root/s/s/2.txt", "1234567890");
        Threads.sleep(1500);
    }

    @Test
    public void replace() throws IOException {
        e.writeLine("/root/2.txt", "1234567890");
        Threads.sleep(1500);
        e.replace("/root/2.txt", "45".getBytes());
        Threads.sleep(1500);
        e.replace("/root/2.txt", 7, "45".getBytes());
        e.replaceLine("/root/2.txt", "空");
        Threads.sleep(1500);
        e.replaceLine("/root/2.txt", 7, "管");
    }

    @Test
    public void append() throws IOException {
        e.append("/root/2.txt", "123全文\n".getBytes());
        Threads.sleep(1500);
        e.append("/root/2.txt", Streams.toInputStream("123让他受到\n"));
        Threads.sleep(1500);
        e.appendLines("/root/2.txt", Lists.of("123", "456"));
        Threads.sleep(1500);
        e.appendLine("/root/2.txt", "1234567890");
        Threads.sleep(1500);
    }

    @Test
    public void upload() throws IOException {
        e.uploadFile("/root/1.jks", "C:\\Users\\ljh15\\Desktop\\1.jks");
        e.uploadDir("/root/data/ftp/", "C:\\Users\\ljh15\\Desktop\\ftp");
    }

    @Test
    public void download() throws IOException {
        e.downloadFile("/root/1.jks", "C:\\Users\\ljh15\\Desktop\\2.jks");
        e.downloadDir("/root/data/ftp/", "C:\\Users\\ljh15\\Desktop\\ms\\data");
    }

    @Test
    public void bigUpload() {
        SftpUpload u = e.upload("/root/a/b/c/a5.rar", new File("C:\\Users\\ljh15\\Desktop\\a.rar"));
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
        SftpDownload u = e.download("/root/ffmpeg", new File("C:\\Users\\ljh15\\Desktop\\ffmpeg.flv"));
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
    public void close() {
        e.close();
        s.close();
    }

}

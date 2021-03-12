package com.orion.ftp.clint;

import com.orion.ftp.client.FtpInstance;
import com.orion.ftp.client.bigfile.FtpDownload;
import com.orion.ftp.client.bigfile.FtpUpload;
import com.orion.ftp.client.config.FtpConfig;
import com.orion.ftp.client.config.FtpsConfig;
import com.orion.ftp.client.pool.FtpClientFactory;
import com.orion.support.progress.ByteTransferProgress;
import com.orion.utils.Threads;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Streams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/11 11:36
 */
public class FtpClintTests {

    private FtpInstance i;

    @Before
    public void init() {
        FtpConfig config = new FtpsConfig("127.0.0.1").auth("user", "123");
        FtpClientFactory f = new FtpClientFactory(config);
        i = f.createInstance();
    }

    @Test
    public void list1() {
        System.out.println(i.listFiles("/"));
        System.out.println("-------");
        System.out.println(i.listFiles("/", true));
        System.out.println("-------");
        System.out.println(i.listDirs("/"));
        System.out.println("-------");
        System.out.println(i.listDirs("/", true));
    }

    @Test
    public void list2() {
        System.out.println(i.listFilesSuffix(".docx", true));
        System.out.println("-------");
        System.out.println(i.listFilesMatch("consumer", true));
        System.out.println("-------");
        System.out.println(i.listFilesPattern(Pattern.compile(".*\\.sql"), true));
        System.out.println("-------");
        System.out.println(i.listFilesFilter(f -> f.getSize() < 1024, true));
    }

    @Test
    public void file1() {
        System.out.println(i.exist("/普通文本.txt"));
        System.out.println(i.exist("/普通文本1.txt"));
        System.out.println("-------");
        System.out.println(i.getFile("/"));
        System.out.println(i.getFile("/普通文本.txt"));
        System.out.println(i.getFile("/普通文本1.txt"));
    }

    @Test
    public void delete1() {
        i.delete("/普通文本1.txt");
        i.deleteDir("/7000-实施1/7300-运维情况");
        i.rm("/7000-实施1");
    }

    @Test
    public void create() {
        i.mkdirs("/1/2/3/4/5");
        i.touch("/a/b/c/d1");
        i.touch("/a/b/c/d2");
        i.touch("/a/b/c/d3");
        i.mv("/a/b/c/d2", "d22");
        i.mv("/a/b/c/d3", "/a/b/c1/d2");
    }

    @Test
    public void read() throws IOException {
        byte[] bs = new byte[1024];
        int read = i.read("/普通文本.txt", bs);
        System.out.println(new String(bs, 0, read));
        System.out.println("---------------------");
        read = i.read("/普通文本.txt", 20, bs);
        System.out.println(new String(bs, 0, read));
        System.out.println("---------------------");
        read = i.read("/普通文本.txt", bs, 0, 20);
        System.out.println(new String(bs, 0, read));
        System.out.println("---------------------");
        read = i.read("/普通文本.txt", 20, bs, 0, 20);
        System.out.println(new String(bs, 0, read));
        System.out.println("---------------------");
        InputStream in = this.i.getInputStream("/普通文本.txt");
        Streams.transfer(in, System.out);
        in.close();
        i.pending();
        System.out.println("---------------------");
        in = this.i.getInputStream("/普通文本.txt", 10);
        Streams.transfer(in, System.out);
        in.close();
        i.pending();
    }

    @Test
    public void write() throws IOException {
        i.write("/1.txt", "123哈哈\n".getBytes());
        i.write("/2.txt", "123哈哈\n".getBytes(), 2, 4);
        i.writeLine("/3.txt", "第一行");
        i.writeLines("/4.txt", Lists.of("第1行", "第2行", "第3行"));
        OutputStream out = i.getOutputStreamWriter("/5.txt");
        out.write("爱世代".getBytes());
        out.flush();
        out.close();
        i.pending();
        i.writeStream("/6.txt", Streams.toInputStream("123哈哈\n".getBytes()));
    }

    @Test
    public void append() throws IOException {
        i.append("/1/1.txt", "123哈哈\n".getBytes());
        i.append("/1/1.txt", "123哈哈\n".getBytes(), 2, 4);
        i.appendLine("/1/1.txt", "第一行");
        i.appendLines("/1/1.txt", Lists.of("第1行", "第2行", "第3行"));
        OutputStream out = i.getOutputStreamAppend("/1/1.txt");
        out.write("爱世代".getBytes());
        out.flush();
        out.close();
        i.pending();
        i.appendStream("/1/1.txt", Streams.toInputStream("123哈哈\n".getBytes()));
    }

    @Test
    public void upload() throws IOException {
        i.uploadFile("/aka1.txt", new File("C:\\Users\\ljh15\\Desktop\\sql.txt"));
        i.uploadFile("/aka2.txt", Streams.toInputStream("哈哈哈\n"));
        i.uploadDir("/new/f1", "C:\\Users\\ljh15\\Desktop\\f1");
    }

    @Test
    public void download() throws IOException {
        i.downloadFile("/aka1.txt", "C:\\Users\\ljh15\\Desktop\\11.txt");
        i.downloadDir("/new", "C:\\Users\\ljh15\\Desktop\\aa");
    }

    public static void main(String[] args) {
        FtpClintTests t = new FtpClintTests();
        t.init();
        t.bigUpload();
        t.destroy();
    }

    @Test
    public void bigUpload() {
        FtpUpload u = i.upload("/big/big.rar", "C:\\Users\\ljh15\\Desktop\\16.5.rar").computeRate(true);
        new Thread(u).start();
        ByteTransferProgress p = u.getProgress();
        while (!p.isDone()) {
            System.out.println(p.getProgress() + "% " + p.getNowRate() / 1024 + "kb/s");
            Threads.sleep(500);
        }
        System.out.println(p.getProgress() + "% " + p.getNowRate() / 1024 + "kb/s");
        System.out.println("done");
    }

    @Test
    public void bigDownload() {
        FtpDownload u = i.download("/big/big.rar", "C:\\Users\\ljh15\\Desktop\\16.7.rar").computeRate(true);
        new Thread(u).start();
        ByteTransferProgress p = u.getProgress();
        while (!p.isDone()) {
            System.out.println(p.getProgress() + "% " + p.getNowRate() / 1024 + "kb/s");
            Threads.sleep(500);
        }
        System.out.println(p.getProgress() + "% " + p.getNowRate() / 1024 + "kb/s");
        System.out.println("done");
    }

    @After
    public void destroy() {
        i.close();
    }

}

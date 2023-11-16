package com.orion.net.ftp.clint;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.time.Dates;
import com.orion.net.ftp.client.FtpFileFilter;
import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.instance.IFtpInstance;
import com.orion.net.ftp.client.pool.FtpClientFactory;
import com.orion.net.specification.transfer.IFileDownloader;
import com.orion.net.specification.transfer.IFileUploader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/11 11:36
 */
public class FtpClintTests {

    private IFtpInstance e;

    @Before
    public void init() {
        FtpConfig config = new FtpConfig("127.0.0.1").auth("user", "123");
        FtpClientFactory f = new FtpClientFactory(config);
        this.e = f.createInstance();
    }

    @Test
    public void testGetFile() {
        e.touch("/root/bbb/ccc/cct.txt");
        System.out.println(JSON.toJSONString(e.getFile("/root/bbb/ccc/cct.txt")));
        System.out.println(JSON.toJSONString(e.listFiles("/root")));
    }

    @Test
    public void testSize() {
        System.out.println(e.getSize("/root"));
        System.out.println(e.getSize("/root/bbb/ccc/cct.txt"));
    }

    @Test
    public void testExist() {
        e.change("/a/a");
        System.out.println(e.isExist("/"));
        System.out.println(e.isExist("/root"));
        System.out.println(e.isExist("/root/bug.txt"));
        System.out.println(e.isExist("/root/bbb/ccc/cct.txt"));
    }

    @Test
    public void testModify() {
        String path = "/root/test/1.txt";
        e.touch(path);
        System.out.println(e.getWorkDirectory());
        System.out.println(Dates.format(new Date(e.getModifyTime(path))));
        e.setModifyTime(path, Dates.build(2020, 2, 2, 2, 2, 2));
        System.out.println(Dates.format(new Date(e.getModifyTime(path))));
    }

    @Test
    public void testTouch() {
        e.touch("/root/test24/1.txt");
        e.touch("/root/test24/2.txt");
        e.touch("/root/test24/3.txt");
        e.touch("/root/test24/3.txt");
    }

    @Test
    public void testMakeDir() {
        e.makeDirectories("/root/test22/t/4/5");
        e.makeDirectories("/root/test22/t/4/5");
        e.makeDirectories("/root/test22/t/4/6");
        e.makeDirectories("/root/test22/t/4/7");
        e.makeDirectories("/root/test22/t/4/8");
    }

    @Test
    public void testRm() {
        e.touch("/root/test1x/1.txt");
        e.touch("/root/test2x/1.txt");
        e.touch("/root/test2x/2.txt");
        e.touch("/root/test2x/d/1.txt");
        e.removeFile("/root/test2x/1.txt");
        e.removeDir("/root/test1x");
        e.remove("/root/test2x");
    }

    @Test
    public void testMove() {
        String path1 = "/root/test1x/1/1.txt";
        String path2 = "/root/test2x/1/2.txt";
        String path3 = "/root/test2x/1/3.txt";
        String path4 = "/root/4.txt";
        e.touch(path1);
        System.out.println(JSON.toJSONString(e.getFile(path1)));
        e.move(path1, path2);
        System.out.println(JSON.toJSONString(e.getFile(path2)));
        e.move(path2, "3.txt");
        System.out.println(JSON.toJSONString(e.getFile(path3)));
        e.move(path3, "../../4.txt");
        System.out.println(JSON.toJSONString(e.getFile(path4)));
    }

    @Test
    public void testList() {
        System.out.println(e.listFiles("/root", false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", FtpFileFilter.suffix(".txt"), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", FtpFileFilter.contains("4."), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", FtpFileFilter.matches(Pattern.compile(".*\\.txt")), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", s -> s.getSize() > 20, true));
        System.out.println("---------");
        System.out.println(e.listDirs("/root", false));
    }

    @Test
    public void testStream() throws IOException {
        try {
            System.out.println(e.openInputStream("/root/1/2/3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Threads.sleep(500);

        e.touch("/root/2/2/3");
        try (InputStream in = e.openInputStream("/root/2/2/3")) {
            System.out.println(in);
        }
        System.out.println(e.pending());
        Threads.sleep(500);

        try (OutputStream o1 = e.openOutputStream("/root/3/2/3")) {
            System.out.println(o1);
        }
        System.out.println(e.pending());
        Threads.sleep(500);

        try (OutputStream o2 = e.openOutputStream("/root/4/2/3", true)) {
            System.out.println(o2);
        }
        System.out.println(e.pending());
    }

    @Test
    public void testRead() throws IOException {
        String path = "/root/1.txt";
        byte[] bs;
        int read;

        bs = new byte[20];
        read = e.read(path, bs);
        System.out.println(read);
        System.out.println(new String(bs, 0, read));
        System.out.println("------");

        bs = new byte[20];
        read = e.read(path, bs, 0, 2);
        System.out.println(read);
        System.out.println(new String(bs, 0, read));
        System.out.println("------");

        bs = new byte[20];
        read = e.read(path, 5, bs);
        System.out.println(read);
        System.out.println(new String(bs, 0, read));
        System.out.println("------");

        bs = new byte[20];
        read = e.read(path, 5, bs, 0, 2);
        System.out.println(read);
        System.out.println(new String(bs, 0, read));
        System.out.println("------");
    }

    @Test
    public void testTransfer() throws IOException {
        String path = "/root/1.txt";

        e.transfer(path, System.out);
        System.out.println("-----");

        e.transfer(path, System.out, 3, 5);
        System.out.println("-----");

        e.transfer(path, System.out, 3);
    }

    @Test
    public void testWrite() throws IOException {
        String path = "/root/write/1.txt";

        e.write(path, "123\n".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);
        System.out.println("----");

        e.write(path, Streams.toInputStream("123123123123\n"));
        e.transfer(path, System.out);
        Threads.sleep(500);

        e.write(path, "111");
        e.transfer(path, System.out);
        Threads.sleep(500);
        System.out.println("----");

        e.write(path, String.join("\n", Lists.of("111", "我是", "333", "444")));
        e.transfer(path, System.out);
        Threads.sleep(500);
    }

    @Test
    public void testAppend() throws IOException {
        String path = "/root/write1/12.txt";
        e.truncate(path);

        e.append(path, "123\n".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);
        System.out.println("----");

        e.append(path, Streams.toInputStream("123123123123\n"));
        e.transfer(path, System.out);
        Threads.sleep(500);
        System.out.println("----");

        e.append(path, "111");
        e.transfer(path, System.out);
        Threads.sleep(500);
        System.out.println("----");

        e.append(path, String.join("\n", Lists.of("111", "我是", "333", "444")));
        System.out.println("----");
        Threads.sleep(500);

        e.transfer(path, System.out);
    }

    @Test
    public void testUpload() throws IOException {
        e.uploadFile("/root/test/bug.txt", "C:\\Users\\Administrator\\Desktop\\bug.txt");
        e.uploadDir("/root/test/dir", "C:\\Users\\Administrator\\Desktop\\FastStoneCapture7.3", true);
    }

    @Test
    public void testDownload() throws IOException {
        e.downloadFile("/root/test/bug.txt", "C:\\Users\\Administrator\\Desktop\\bug1.txt");
        e.downloadDir("/root/test/dir", "C:\\Users\\Administrator\\Desktop\\_FastStoneCapture7.3", true);
    }

    @Test
    public void testTransferUpload() {
        String local = "C:\\Users\\Administrator\\Desktop\\cp.zip";
        System.out.println(Files1.md5(local));

        IFileUploader u = e.upload("/root/test/cp.zip", local);
        u.getProgress()
                .computeRate()
                .rateAcceptor(pr -> {
                    System.out.println(pr.getProgress() * 100 + "% " + pr.getNowRate() / 1024 + "kb/s");
                })
                .callback(() -> {
                    System.out.println("done");
                    System.exit(0);
                });
        new Thread(u).start();
        // Threads.sleep(2000L);
        // u.abort();
        Threads.sleep(30000000L);
    }

    @Test
    public void testTransferDownload() {
        String local = "C:\\Users\\Administrator\\Desktop\\cp1.zip";

        IFileDownloader u = e.download("/root/test/cp.zip", local);
        u.getProgress()
                .computeRate()
                .rateAcceptor(pr -> {
                    System.out.println(pr.getProgress() * 100 + "% " + pr.getNowRate() / 1024 + "kb/s");
                })
                .callback(() -> {
                    System.out.println("done");
                    System.out.println(Files1.md5(local));
                    System.exit(1000);
                });
        new Thread(u).start();
        // Threads.sleep(2000L);
        // u.abort();
        Threads.sleep(30000000L);
    }

    @After
    public void destroy() {
        e.close();
    }

}

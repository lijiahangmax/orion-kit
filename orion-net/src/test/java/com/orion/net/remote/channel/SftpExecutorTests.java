package com.orion.net.remote.channel;

import com.alibaba.fastjson.JSON;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.lang.utils.time.Dates;
import com.orion.net.base.file.sftp.SftpFile;
import com.orion.net.base.file.transfer.IFileTransfer;
import com.orion.net.remote.channel.sftp.SftpExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
        SessionHolder.HOLDER.setLogger(SessionLogger.ERROR);
        this.e = SessionHolder.HOLDER.getSession("192.168.146.230", "root")
                .password("admin123")
                .timeout(20000)
                .connect(20000)
                .getSftpExecutor();
        e.connect(20000);
        e.charset(Const.UTF_8);
    }

    @After
    public void after() {
        e.disconnect();
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
        System.out.println(e.getSize("/root/bug.txt"));
    }

    @Test
    public void testExist() {
        System.out.println(e.isExist("/root"));
        System.out.println(e.isExist("/root/bug.txt"));
        System.out.println(e.isExist("/root/aaa/bug.txt"));
        System.out.println(e.isExist("/bug.txt"));
    }

    @Test
    public void testGetPath() {
        System.out.println(e.getPath("/root/bug.txt"));
        System.out.println(e.getPath("/root/aaa/bug.txt"));
        System.out.println(e.getPath("/bug.txt"));
    }

    @Test
    public void testModifyPermission() {
        String path = "/root/test/1.txt";
        e.touch(path);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chmod(path, 111);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chgrp(path, 1);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chgrp(path, 2);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chown(path, 1);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.chown(path, 2);
        System.out.println(JSON.toJSONString(e.getFile(path)));
    }

    @Test
    public void testModifyAttr() {
        String path = "/root/test/21.txt";
        e.touch(path);
        SftpFile file = e.getFile(path);
        System.out.println(JSON.toJSONStringWithDateFormat(file, Dates.YMD_HMS));
        file.setSize(200);
        file.setGid(2);
        file.setUid(2);
        file.setPermission(777);
        file.setAccessTime(Dates.build(2020, 1, 1, 1, 1, 1));
        file.setModifyTime(Dates.build(2020, 1, 1, 1, 1, 1));
        e.setFileAttribute(file);
        System.out.println(JSON.toJSONStringWithDateFormat(e.getFile(path), Dates.YMD_HMS));
        e.setModifyTime(path, Dates.build(2020, 2, 2, 2, 2, 2));
        System.out.println(JSON.toJSONStringWithDateFormat(e.getFile(path), Dates.YMD_HMS));
    }

    @Test
    public void testTouch() {
        e.touch("/root/test1/t/4/1.txt");
        e.touch("/root/test1/t/4/2.txt");
        e.touch("/root/test1/t/4/3.txt");
        e.touch("/root/test1/t/4/3.txt");
        // err
        e.touch("/root/test1/t/4");
    }

    @Test
    public void testTruncate() {
        e.truncate("/root/test/x1.txt");
        e.truncate("/root/test/xx/xx/x1.txt");
        // err
        e.truncate("/root/test/xx/xx");
    }

    @Test
    public void testMkdir() {
        e.mkdirs("/root/test22/t/4/5");
        e.mkdirs("/root/test22/t/4/5");
        e.mkdir("/root/test22/t/4/6");
    }

    @Test
    public void testRm() {
        e.touch("/root/test1x/1.txt");
        e.touch("/root/test2x/1.txt");
        e.touch("/root/test2x/2.txt");
        e.removeFile("/root/test1x/1.txt");
        e.removeDir("/root/test1x");
        e.rm("/root/test2x");
    }

    @Test
    public void testMove() {
        String path1 = "/root/test1x/1/2/3/4/5/1.txt";
        String path2 = "/root/test2x/1/2/3/4/5/2.txt";
        String path3 = "/root/test2x/1/2/3/4/5/3.txt";
        e.touch(path1);
        System.out.println(JSON.toJSONString(e.getFile(path1)));
        e.mv(path1, path2);
        System.out.println(JSON.toJSONString(e.getFile(path2)));
        e.mv(path2, "3.txt");
        System.out.println(JSON.toJSONString(e.getFile(path3)));
    }

    @Test
    public void testLink() {
        String path1 = "/root/test1/1.txt";
        String path2 = "/root/test1/x/2.txt";
        String path3 = "/root/test1/x/3.txt";
        e.touch(path1);
        e.touchHardLink(path1, path2);
        e.touchSymLink(path1, path3);
        System.out.println(JSON.toJSONString(e.getFile(path1, true)));
        System.out.println(JSON.toJSONString(e.getFile(path1, false)));
        System.out.println(JSON.toJSONString(e.getFile(path2, true)));
        System.out.println(JSON.toJSONString(e.getFile(path2, false)));
        System.out.println(JSON.toJSONString(e.getFile(path3, true)));
        System.out.println(JSON.toJSONString(e.getFile(path3, false)));
        System.out.println(e.getLinkPath(path1));
        System.out.println(e.getLinkPath(path2));
        System.out.println(e.getLinkPath(path3));
    }

    @Test
    public void testList() {
        System.out.println(e.ll("/root"));
        System.out.println("---------");
        System.out.println(e.listFiles("/root", false));
        System.out.println("---------");
        System.out.println(e.listFilesSuffix("/root", ".txt", false));
        System.out.println("---------");
        System.out.println(e.listFilesMatch("/root", "tmp", false));
        System.out.println("---------");
        System.out.println(e.listFilesPattern("/root", Pattern.compile(".*\\.txt"), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", s -> s.getSize() > 100000, false));
        System.out.println("---------");
        System.out.println(e.listDirs("/root", false));
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
    public void testReadLine() throws IOException {
        String path = "/root/1.txt";
        System.out.println(e.readLine(path));
        System.out.println("-----");

        System.out.println(e.readLine(path, 9));
        System.out.println("-----");

        System.out.println(e.readLine(path, 10));
        System.out.println("-----");

        System.out.println(e.readLine(path, 11));
        System.out.println("-----");

        System.out.println(e.readLines(path));
        System.out.println("-----");

        System.out.println(e.readLines(path, 2));
        System.out.println("-----");

        System.out.println(e.readLines(path, 3));
        System.out.println("-----");

        System.out.println(e.readLines(path, 2L));
        System.out.println("-----");

        System.out.println(e.readLines(path, 3L));
        System.out.println("-----");

        System.out.println(e.readLines(path, 2L, 2));
        System.out.println("-----");

        System.out.println(e.readLines(path, 3L, 2));
        System.out.println("-----");

        System.out.println(e.readLines(path, 10L, 2));
        System.out.println("-----");
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
        e.truncate(path);

        e.write(path, "111".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.write(path, "123\n".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.write(path, Streams.toInputStream("123123123123\n"));
        e.transfer(path, System.out);
        Threads.sleep(500);
    }

    @Test
    public void testWriteLine() throws IOException {
        String path = "/root/write/1.txt";
        e.truncate(path);

        e.writeLine(path, "111");
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.writeLines(path, Lists.of("111", "我是", "333", "444"));
        e.transfer(path, System.out);
        Threads.sleep(500);
    }

    @Test
    public void testAppend() throws IOException {
        String path = "/root/write/1.txt";
        e.truncate(path);

        e.append(path, "111".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.append(path, "123\n".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.append(path, Streams.toInputStream("123123123123\n"));
        e.transfer(path, System.out);
        Threads.sleep(500);
    }

    @Test
    public void testAppendLine() throws IOException {
        String path = "/root/write/1.txt";
        e.truncate(path);

        e.appendLine(path, "111");
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.appendLines(path, Lists.of("111", "我是", "333", "444"));
        e.transfer(path, System.out);
        Threads.sleep(500);
    }

    @Test
    public void testUpload() throws IOException {
        e.uploadFile("/root/test/bug.txt", "C:\\Users\\ljh15\\Desktop\\bug.txt");
        e.uploadDir("/root/test/dir", "C:\\Users\\ljh15\\Desktop\\data\\_FastStoneCapture7.3", true);
    }

    @Test
    public void testDownload() throws IOException {
        e.downloadFile("/root/test/bug.txt", "C:\\Users\\ljh15\\Desktop\\bug1.txt");
        e.downloadDir("/root/test/dir", "C:\\Users\\ljh15\\Desktop\\_FastStoneCapture7.3", true);
    }

    @Test
    public void testBigUpload() {
        String local = "C:\\Users\\ljh15\\Desktop\\cp.zip";
        System.out.println(Files1.md5(local));

        IFileTransfer u = e.upload("/root/test/cp.zip", local);
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
        Threads.sleep(30000000L);
    }

    @Test
    public void testBigDownload() {
        String local = "C:\\Users\\ljh15\\Desktop\\cp1.zip";

        IFileTransfer u = e.download("/root/test/cp.zip", local);
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
        Threads.sleep(30000000L);
    }

}

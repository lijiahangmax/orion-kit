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
package cn.orionsec.kit.net.host;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.sftp.SftpFile;
import cn.orionsec.kit.net.host.sftp.SftpFileFilter;
import cn.orionsec.kit.net.specification.transfer.IFileDownloader;
import cn.orionsec.kit.net.specification.transfer.IFileUploader;
import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class SftpExecutorTests {

    private SessionHolder h;

    private SftpExecutor e;

    @Before
    public void before() {
        this.h = SessionHolder.create();
        h.setLogger(SessionLogger.ERROR);
        this.e = h.getSession("192.168.146.230", "root")
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
        e.changeMode(path, 111);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.changeGroup(path, 1);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.changeGroup(path, 2);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.changeOwner(path, 1);
        System.out.println(JSON.toJSONString(e.getFile(path)));
        e.changeOwner(path, 2);
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
        e.makeDirectories("/root/test22/t/4/5");
        e.makeDirectories("/root/test22/t/4/5");
        e.makeDirectory("/root/test22/t/4/6");
    }

    @Test
    public void testRm() {
        e.touch("/root/test1x/1.txt");
        e.touch("/root/test2x/1.txt");
        e.touch("/root/test2x/2.txt");
        e.removeFile("/root/test1x/1.txt");
        e.removeDir("/root/test1x");
        e.remove("/root/test2x");
    }

    @Test
    public void testMove() {
        String path1 = "/root/test1x/1/2/3/4/5/1.txt";
        String path2 = "/root/test2x/1/2/3/4/5/2.txt";
        String path3 = "/root/test2x/1/2/3/4/5/3.txt";
        e.touch(path1);
        System.out.println(JSON.toJSONString(e.getFile(path1)));
        e.move(path1, path2);
        System.out.println(JSON.toJSONString(e.getFile(path2)));
        e.move(path2, "3.txt");
        System.out.println(JSON.toJSONString(e.getFile(path3)));
    }

    @Test
    public void testLink() {
        String path1 = "/root/test1/1.txt";
        String path2 = "/root/test1/x/2.txt";
        String path3 = "/root/test1/x/3.txt";
        e.touch(path1);
        e.hardLink(path1, path2);
        e.symLink(path1, path3);
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
        System.out.println(e.list("/root"));
        System.out.println("---------");
        System.out.println(e.listFiles("/root", false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", SftpFileFilter.suffix(".txt"), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", SftpFileFilter.contains("tmp"), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", SftpFileFilter.matches(Pattern.compile(".*\\.txt")), false));
        System.out.println("---------");
        System.out.println(e.listFilesFilter("/root", s -> s.getSize() > 100000, false));
        System.out.println("---------");
        System.out.println(e.listDirs("/root", false));
    }

    @Test
    public void testReadInit() throws IOException {
        String path = "/root/1.txt";
        e.write(path, "0123456789\n0123456789\n0123456789\n0123456789\n");
    }

    @Test
    public void testStream() throws IOException {
        Streams.lineConsumer(e.openInputStream("/root/1.txt"), System.out::println);
        System.out.println("------");

        Streams.lineConsumer(e.openInputStream("/root/1.txt", 2), System.out::println);
        System.out.println("------");

        OutputStream write = e.openOutputStream("/root/12.txt");
        write.write("123".getBytes());
        Streams.close(write);

        OutputStream append = e.openOutputStream("/root/12.txt", true);
        append.write("456\n".getBytes());
        Streams.close(append);

        System.out.println(Streams.toString(e.openInputStream("/root/12.txt")));
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
        e.truncate(path);

        System.out.println("----");
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
    }

    @Test
    public void testAppend() throws IOException {
        String path = "/root/write/1.txt";
        e.truncate(path);

        System.out.println("----");
        e.append(path, "123\n".getBytes());
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.append(path, Streams.toInputStream("123123123123\n"));
        e.transfer(path, System.out);
        Threads.sleep(500);

        e.append(path, "111");
        e.transfer(path, System.out);
        Threads.sleep(500);

        System.out.println("----");
        e.append(path, String.join("\n", Lists.of("111", "我是", "333", "444")));
        e.transfer(path, System.out);
    }

    @Test
    public void testUpload() throws IOException {
        e.uploadFile("/root/test/bug.txt", "C:\\Users\\lijiahang\\Desktop\\bug.txt");
        e.uploadDir("/root/test/dir", "C:\\Users\\lijiahang\\Desktop\\terminal", true);
    }

    @Test
    public void testDownload() throws IOException {
        e.downloadFile("/root/test/bug.txt", "C:\\Users\\lijiahang\\Desktop\\bug1.txt");
        e.downloadDir("/root/test/dir", "C:\\Users\\lijiahang\\Desktop\\terminal1", true);
    }

    @Test
    public void testTransferUpload() {
        String local = "C:\\Users\\lijiahang\\Desktop\\cp.zip";
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
        Threads.sleep(30000000L);
    }

    @Test
    public void testTransferDownload() {
        String local = "C:\\Users\\lijiahang\\Desktop\\cp1.zip";

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
        Threads.sleep(30000000L);
    }

}

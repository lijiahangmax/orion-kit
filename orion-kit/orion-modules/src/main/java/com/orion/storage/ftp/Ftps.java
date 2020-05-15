package com.orion.storage.ftp;

import com.orion.storage.ftp.pool.FtpClientFactory;
import com.orion.storage.ftp.pool.FtpClientPool;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * FTP工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/17 16:10
 */
public class Ftps {

    private Ftps() {
    }

    /**
     * FTP 配置
     */
    private static FtpConfig config = new FtpConfig("129.204.94.66").auth("ftpuser", "admin123");

    /**
     * FTP 工厂
     */
    private static FtpClientFactory factory = new FtpClientFactory(config);

    /**
     * 连接池对象
     */
    private static FtpClientPool pool = new FtpClientPool(factory).keepAliveListener();

    /**
     * 重新加载配置
     *
     * @param config 配置项
     * @throws InterruptedException 释放连接超时
     */
    public static void reloadConfig(FtpConfig config) throws InterruptedException {
        Ftps.pool.close();
        Ftps.config = config;
        Ftps.factory = new FtpClientFactory(config);
        Ftps.pool = new FtpClientPool(factory);
    }

    /**
     * 归还连接
     *
     * @param client client
     */
    private static void returnClient(FTPClient client) {
        if (pool != null) {
            pool.returnClient(client);
        } else {
            destroy(client);
        }
    }

    /**
     * 归还连接
     *
     * @param instance ftp实例
     */
    private static void returnClient(FtpInstance instance) {
        if (pool != null) {
            pool.returnClient(instance.client());
        } else {
            destroy(instance.client());
        }
    }

    /**
     * 从连接池中获取连接
     * 需要调用 returnClient 归还连接
     *
     * @return FTP 连接
     */
    private static FTPClient getClient() {
        if (pool == null) {
            if (factory == null) {
                return null;
            }
            return factory.getClient();
        }
        return pool.getClient();
    }

    /**
     * 从连接池中获取连接
     *
     * @param byPool 是否从连接池中获取
     * @return FTP 连接
     */
    private static FTPClient getClient(boolean byPool) {
        if (byPool) {
            if (pool == null) {
                return null;
            }
            return pool.getClient();
        } else {
            if (factory == null) {
                return null;
            }
            return factory.getClient();
        }
    }

    /**
     * 获取链接工厂
     *
     * @return 工厂
     */
    private static FtpClientFactory getFactory() {
        return factory;
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @return 实例
     */
    public static FtpInstance getInstance() {
        return getInstance(true, null);
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @param byPool 是否从连接池中获取
     * @return 实例
     */
    public static FtpInstance getInstance(boolean byPool) {
        return getInstance(byPool, null);
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @param config 配置信息
     * @return 实例
     */
    public FtpInstance getInstance(FtpConfig config) {
        return getInstance(true, config);
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @param byPool 是否从连接池中获取
     * @return 实例
     */
    public static FtpInstance getInstance(boolean byPool, FtpConfig config) {
        if (byPool) {
            if (pool == null) {
                return null;
            }
            return new FtpInstance(config != null ? config : Ftps.config, pool.getClient());
        }
        if (factory == null) {
            return null;
        }
        return new FtpInstance(config != null ? config : Ftps.config, pool.getClient());
    }

    /**
     * 销毁
     */
    private static void destroy(FTPClient client) {
        if (client != null && client.isConnected()) {
            try {
                client.logout();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    client.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        // FTPClient c = pool.getClient();
        // String dir = "/11x/1x/nginx-1.15.0/conf/";
        // FtpInstance instance = new FtpInstance(conf, c);


        // 文件列表
        // System.out.println(instance.listFiles(dir));
        // System.out.println(instance.listFilesAndDir(dir));
        // System.out.println(instance.listDirs("/11x/1x/nginx-1.15.0/"));
        // System.out.println(instance.listFilesFilter(dir, (f) -> f.getSize() > 10));
        // System.out.println(instance.listFilesMatch(dir, "-"));
        // System.out.println(instance.listFilesSuffix(dir, "conf"));
        // System.out.println(instance.listFilesPattern(dir, ".*s"));
        // System.out.println(instance.listFilesAttr(dir));
        // System.out.println(instance.getFileAttr(dir + "我.txt"));
        // System.out.println(instance.exist(dir + "我.txt"));
        // System.out.println(instance.replyMsg());

        // 下载 上传
        // String localDir = "C:\\Users\\ljh15\\Desktop\\ftp\\";
        // instance.download(dir + "我.txt", localDir + "我.txt");
        // instance.downloadDir(dir, localDir);
        // instance.upload(localDir + "大猩猩.txt", dir + "大猩猩.txt");
        // instance.uploadDir(localDir, dir + "local");
        // System.out.println(instance.replyMsg());

        // append
        // String f = dir + "大猩猩.txt";
        // instance.append(f, "沃特法克1".getBytes());
        // instance.append(f, "沃特法克2".getBytes());
        // instance.appendLine(f, "色谱如爱思1");
        // instance.appendLine(f, "色谱如爱思2");
        // instance.appendLines(f, Collections1.asList("独有王特死淡定1", "噎死诶特诶子1"));
        // instance.appendLines(f, Collections1.asList("独有王特死淡定2", "噎死诶特诶子2"));
        // instance.appendStream(f, Streams.toInputStream("独有王特斯但丁1"));
        // instance.appendStream(f, Streams.toInputStream("独有王特斯但丁2"));
        // OutputStream o = instance.getOutputStreamAppend(f);
        // o.write("趴窝1".getBytes());
        // o.close();
        // instance.pending();
        // o = instance.getOutputStreamAppend(f);
        // o.write("趴窝2".getBytes());
        // o.close();
        // instance.pending();
        // System.out.println(instance.replyMsg());

        // read
        // String f1 = dir + "大猩猩.txt";
        // byte[] bs = new byte[50];
        // System.out.println(new String(bs, 0, instance.read(f1, bs)));
        // bs = new byte[50];
        // System.out.println(new String(bs, 0, instance.read(f1, 1, bs)));
        // System.out.println(instance.readLine(f1));
        // System.out.println(instance.readLine(f1, 10));
        // System.out.println(instance.readLines(f1, 10, 4));
        // System.out.println(instance.readLines(f1, 4));
        // System.out.println(instance.readLines(f1, 10L));
        // bs = new byte[50];
        // InputStream in = instance.getInputStreamRead(f1);
        // System.out.println(new String(bs, 0, in.read(bs)));
        // in.close();
        // instance.pending();
        // bs = new byte[50];
        // in = instance.getInputStreamRead(f1, 10);
        // System.out.println(new String(bs, 0, in.read(bs)));
        // in.close();
        // instance.pending();
        // System.out.println(instance.replyMsg());

        // write
        // String f2 = dir + "大猩猩.txt";
        // instance.write(f2, "清除打发".getBytes());
        // instance.writeLine(f2, "写入行");
        // instance.writeLines(f2, Collections1.asList("写入行1", "写入行2", "写入行3"));
        // instance.writeStream(f2, Streams.toInputStream("希尔3"));
        // OutputStream o1 = instance.getOutputStreamWrite(f2);
        // o1.write("青创城1".getBytes());
        // o1.close();
        // instance.pending();
        // o1 = instance.getOutputStreamWrite(f2);
        // o1.write("青创城2".getBytes());
        // o1.close();
        // instance.pending();
        // System.out.println(instance.replyMsg());

        // file
        // instance.delete(dir + "win-utf");
        // System.out.println(instance.replyMsg());
        // instance.deleteDir(dir + "local");
        // System.out.println(instance.replyMsg());
        // instance.mkdirs(dir + "l/a");
        // System.out.println(instance.replyMsg());
        // instance.touch(dir + "l/a/x.x");
        // System.out.println(instance.replyMsg());
        // instance.mv(dir + "l", dir + "a");
        // System.out.println(instance.replyMsg());
        // instance.mv(dir + "a/a/x.x", dir + "x.x");
        // System.out.println(instance.replyMsg());

        //  bigfile
        // DecimalFormat format = new DecimalFormat("##.##");
        // FtpUpload upload = instance.getUploadBigFileRunnable("/11x/1x/nginx-1.15.0/conf/access3.log", "C:\\Users\\ljh15\\Desktop\\access.log");
        // new Thread(upload.openNowRate()).start();
        // while (!upload.isDone()) {
        //     Threads.sleep(1000);
        //     System.out.println("当前进度: " + format.format(upload.getStep() * 100) + "%");
        //     System.out.println("当前速度: " + format.format(upload.getTimeRate() / 1024) + " KB");
        //     System.out.println("平均速度: " + format.format(upload.getAvgRate() / 1024) + " KB");
        // }
        // System.out.println("____________________");
        // System.out.println("____________________");
        // System.out.println("____________________");
        //
        // FtpUpload upload1 = instance.getUploadBigFileRunnable("/11x/1x/nginx-1.15.0/conf/access4.log", "C:\\Users\\ljh15\\Desktop\\access.log");
        // new Thread(upload1.openNowRate()).start();
        // while (!upload1.isDone()) {
        //     Threads.sleep(1000);
        //     System.out.println("当前进度: " + format.format(upload1.getStep() * 100) + "%");
        //     System.out.println("当前速度: " + format.format(upload1.getTimeRate() / 1024) + " KB");
        //     System.out.println("平均速度: " + format.format(upload1.getAvgRate() / 1024) + " KB");
        // }
        //
        // DecimalFormat format = new DecimalFormat("##.##");
        // FtpDownload download = instance.getDownloadBigFileRunnable(dir + "fastcgi.conf", "C:\\Users\\ljh15\\Desktop\\ftp\\fastcgi1.conf");
        // new Thread(download.openNowRate()).start();
        // while (!download.isDone()) {
        //     Threads.sleep(1000);
        //     System.out.println("当前进度: " + format.format(download.getStep() * 100) + "%");
        //     System.out.println("当前速度: " + format.format(download.getTimeRate() / 1024) + " KB");
        //     System.out.println("平均速度: " + format.format(download.getAvgRate() / 1024) + " KB");
        // }
        // System.out.println("____________________");
        // System.out.println("____________________");
        // System.out.println("____________________");
        //
        // FtpDownload download1 = instance.getDownloadBigFileRunnable(dir + "fastcgi.conf", "C:\\Users\\ljh15\\Desktop\\ftp\\fastcgi2.conf");
        // new Thread(download1.openNowRate()).start();
        // while (!download1.isDone()) {
        //     Threads.sleep(1000);
        //     System.out.println("当前进度: " + format.format(download1.getStep() * 100) + "%");
        //     System.out.println("当前速度: " + format.format(download1.getTimeRate() / 1024) + " KB");
        //     System.out.println("平均速度: " + format.format(download1.getAvgRate() / 1024) + " KB");
        // }
        //
        // System.out.println("end");
    }
}

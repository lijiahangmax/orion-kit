package com.orion.test.remote.channel;

import com.orion.remote.channel.SessionFactory;
import com.orion.remote.channel.SessionLogger;
import com.orion.remote.channel.executor.sftp.bigfile.SftpDownload;
import com.orion.utils.Threads;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/20 12:58
 */
public class DownloadTests {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTests.class);

    private static SftpDownload s;

    static {
        SessionFactory.setLogger(SessionLogger.INFO);
        s = SessionFactory.getSession("root", "192.168.146.230")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getSftpDownload("/root/ffmpeg", "C:\\Users\\ljh15\\Desktop\\ffmpeg.flv");
        s.connect();
    }

    public static void main(String[] args) {
        download();
    }

    private static void download() {
        s.openNowRate();
        Threads.start(s);
        while (!s.isDone()) {
            Threads.sleep(1000);
            LOGGER.info("{}%   now: {}kb/s  avg: {}kb/s", s.getProgress() * 100, s.getNowRate() / 1024, s.getAvgRate() / 1024);
        }
        LOGGER.info("s.getUseTime() = " + s.getUseTime());
        s.close();
        Valid.eq(Files1.md5("C:\\Users\\ljh15\\Desktop\\ffmpeg.flv"), Files1.md5("C:\\Users\\ljh15\\Desktop\\ffmpeg1.flv"));
    }

}

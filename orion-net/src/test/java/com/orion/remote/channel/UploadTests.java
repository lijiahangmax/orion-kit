package com.orion.remote.channel;

import com.orion.remote.channel.sftp.bigfile.SftpUpload;
import com.orion.utils.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/20 13:28
 */
public class UploadTests {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadTests.class);

    private static SftpUpload s;

    static {
        SessionHolder.setLogger(SessionLogger.INFO);
        s = SessionHolder.getSession("root", "192.168.146.230")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getSftpUpload("/root/ffmpeg1.flv", "C:\\Users\\ljh15\\Desktop\\ffmpeg.flv");
        s.connect();
    }

    public static void main(String[] args) {
        upload();
    }

    private static void upload() {
        s.openNowRate();
        Threads.start(s);
        while (!s.isDone()) {
            Threads.sleep(1000);
            LOGGER.info("{}%   now: {}kb/s  avg: {}kb/s", s.getProgress() * 100, s.getNowRate() / 1024, s.getAvgRate() / 1024);
        }
        LOGGER.info("s.getUseTime() = " + s.getUseTime());
        s.close();
    }

}

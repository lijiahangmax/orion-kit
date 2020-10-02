package com.orion.test.remote;

import ch.ethz.ssh2.SFTPv3Client;
import com.orion.exception.AuthenticationException;
import com.orion.remote.connection.RemoteConnection;
import com.orion.remote.connection.sftp.FileAttribute;
import com.orion.remote.connection.sftp.SftpExecutor;
import com.orion.remote.connection.sftp.bigfile.SftpDownload;
import com.orion.remote.connection.sftp.bigfile.SftpUpload;
import com.orion.utils.Threads;
import org.junit.Test;

import java.io.File;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/14 14:30
 */
public class SFTPTests {

    private SFTPv3Client c = RemoteConnection.getConnection("129.204.94.66").auth("root", "lijiahang001..").getSFTPClient();
    private SftpExecutor e = new SftpExecutor(c);

    public SFTPTests() throws AuthenticationException {
    }

    @Test
    public void t() {
        for (FileAttribute l : e.ll("/root/sftp")) {
            System.out.println(l);
        }
    }

    public static void main(String[] args) throws AuthenticationException {
        new SFTPTests().bigUp();
    }

    public void bigDown() {
        SftpDownload d = new SftpDownload(c, "/root/sysmin-0.0.1-SNAPSHOT.jar", new File("C:\\Users\\ljh15\\Desktop\\tmp\\sysmin.jar"));
        new Thread(d.openNowRate()).start();
        while (!d.isDone()) {
            Threads.sleep(1000);
            System.out.println(d.getAvgRate());
            System.out.println(d.getNowRate());
            System.out.println(d.getProgress());
        }
    }

    public void bigUp() {
        SftpUpload d = new SftpUpload(c, "/root/sysmin-0.0.3-SNAPSHOT.jar", new File("C:\\Users\\ljh15\\Desktop\\tmp\\sysmin.jar"));
        new Thread(d.openNowRate()).start();
        while (!d.isDone()) {
            Threads.sleep(1000);
            System.out.println(d.getProgress());
            System.out.println(d.getAvgRate());
            System.out.println(d.getNowRate());
        }
    }

}

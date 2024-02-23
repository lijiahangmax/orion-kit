package com.orion.net.ftp.server;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/10 18:26
 */
public class FtpServerTests {

    public static void main(String[] args) {
        FtpServerInstance instance = new FtpServerInstance();
        FtpUser user = new FtpUser();
        user.setUsername("user");
        user.setPassword("123");
        user.setHomePath("C:\\Users\\Administrator\\Desktop\\sftp");
        user.setWritePermission(true);
        instance.addUser(user)
                // .sslConfig(new FtpServerSslConfig("C:\Users\Administrator\Desktop\sftp\\1.jks", "123456"))
                .listener()
                .start();
    }

}

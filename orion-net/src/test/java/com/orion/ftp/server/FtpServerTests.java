package com.orion.ftp.server;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/10 18:26
 */
public class FtpServerTests {

    public static void main(String[] args) {
        FtpServerInstance instance = new FtpServerInstance();
        FtpUser user = new FtpUser()
                .username("user")
                .password("123")
                .homePath("C:\\Users\\ljh15\\Desktop\\ftp")
                .writePermission();
        instance.addUser(user)
                // .sslConfig(new FtpServerSslConfig("C:\\Users\\ljh15\\Desktop\\1.jks", "123456"))
                .listener()
                .start();
    }

}

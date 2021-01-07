package com.orion.test.remote.connection;

import com.orion.remote.connection.ConnectionStore;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;

import java.io.IOException;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/20 12:39
 */
public class SftpTests {

    public static void main(String[] args) throws IOException {
        writeLines();
    }

    public static void append() throws IOException {
        new ConnectionStore("192.168.146.230")
                .auth("root", "admin123")
                .getSftpExecutor()
                .append("/root/a", Strings.bytes("23"));
    }

    public static void appendLines() throws IOException {
        new ConnectionStore("192.168.146.230")
                .auth("root", "admin123")
                .getSftpExecutor()
                .appendLines("/root/a", Lists.of("123", "xxx", "sdwd"));
    }

    public static void write() throws IOException {
        new ConnectionStore("192.168.146.230")
                .auth("root", "admin123")
                .getSftpExecutor()
                .write("/root/a", Strings.bytes("99999999999"));
    }

    public static void writeLines() throws IOException {
        new ConnectionStore("192.168.146.230")
                .auth("root", "admin123")
                .getSftpExecutor()
                .writeLines("/root/a", 13, Lists.of("123222", "xxxxxx", "sdwdddd"));
    }

}

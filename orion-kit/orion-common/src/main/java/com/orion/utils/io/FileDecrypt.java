package com.orion.utils.io;

import com.orion.utils.Encrypts;
import com.orion.utils.Exceptions;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.concurrent.Callable;

/**
 * 文件解密器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/7/15 18:03
 */
public class FileDecrypt implements Callable<Boolean> {

    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    private BufferedReader reader;

    private OutputStream out;

    private String password;

    private boolean autoClose = true;

    public FileDecrypt(File file, File dest, String password) {
        this(file, dest, password, DEFAULT_BUFFER_SIZE);
    }

    public FileDecrypt(String file, String dest, String password) {
        this(file, dest, password, DEFAULT_BUFFER_SIZE);
    }

    public FileDecrypt(InputStream in, OutputStream out, String password) {
        this(in, out, password, DEFAULT_BUFFER_SIZE);
    }

    public FileDecrypt(Reader reader, OutputStream out, String password) {
        this(reader, out, password, DEFAULT_BUFFER_SIZE);
    }

    public FileDecrypt(File file, File dest, String password, int bufferSize) {
        this.password = password;
        try {
            Files1.touch(dest);
            this.reader = new BufferedReader(new FileReader(file), bufferSize);
            this.out = Files1.openOutputStream(dest);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public FileDecrypt(String file, String dest, String password, int bufferSize) {
        this.password = password;
        try {
            Files1.touch(dest);
            this.reader = new BufferedReader(new FileReader(file), bufferSize);
            this.out = Files1.openOutputStream(dest);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public FileDecrypt(InputStream in, OutputStream out, String password, int bufferSize) {
        this.password = password;
        this.autoClose = false;
        this.reader = new BufferedReader(new InputStreamReader(in), bufferSize);
        this.out = out;
    }

    public FileDecrypt(Reader reader, OutputStream out, String password, int bufferSize) {
        this.password = password;
        this.autoClose = false;
        this.reader = new BufferedReader(reader, bufferSize);
        this.out = out;
    }

    @Override
    public Boolean call() {
        SecretKey secretKey = Encrypts.generatorAESKey(password);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] d = Encrypts.aesDecrypt(line.getBytes(), secretKey);
                out.write(d);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (autoClose) {
                Streams.closeQuietly(reader);
                Streams.closeQuietly(out);
            }
        }
    }

}

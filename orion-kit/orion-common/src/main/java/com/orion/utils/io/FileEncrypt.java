package com.orion.utils.io;

import com.orion.utils.Arrays1;
import com.orion.utils.Encrypts;
import com.orion.utils.Exceptions;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * 文件加密器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/7/15 18:02
 */
public class FileEncrypt implements Callable<Boolean> {

    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    private InputStream in;

    private OutputStream out;

    private String password;

    private int bufferSize = DEFAULT_BUFFER_SIZE;

    private boolean autoClose = true;

    public FileEncrypt(File file, File dest, String password) {
        this(file, dest, password, DEFAULT_BUFFER_SIZE);
    }

    public FileEncrypt(String file, String dest, String password) {
        this(file, dest, password, DEFAULT_BUFFER_SIZE);
    }

    public FileEncrypt(InputStream in, OutputStream out, String password) {
        this(in, out, password, DEFAULT_BUFFER_SIZE);
    }

    public FileEncrypt(File file, File dest, String password, int bufferSize) {
        this.password = password;
        this.bufferSize = bufferSize;
        try {
            Files1.touch(dest);
            this.in = Files1.openInputStream(file);
            this.out = Files1.openOutputStream(dest);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public FileEncrypt(String file, String dest, String password, int bufferSize) {
        this.password = password;
        this.bufferSize = bufferSize;
        try {
            Files1.touch(dest);
            this.in = Files1.openInputStream(file);
            this.out = Files1.openOutputStream(dest);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public FileEncrypt(InputStream in, OutputStream out, String password, int bufferSize) {
        this.password = password;
        this.autoClose = false;
        this.in = in;
        this.out = out;
        this.bufferSize = bufferSize;
    }

    @Override
    public Boolean call() {
        SecretKey secretKey = Encrypts.generatorAESKey(password);
        try {
            byte[] bs = new byte[bufferSize];
            int read;
            while ((read = in.read(bs)) != -1) {
                byte[] ebs = Encrypts.aesEncrypt(Arrays1.resize(bs, read), secretKey);
                out.write(ebs);
                out.write('\n');
            }
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (autoClose) {
                Streams.closeQuietly(in);
                Streams.closeQuietly(out);
            }
        }
    }

}

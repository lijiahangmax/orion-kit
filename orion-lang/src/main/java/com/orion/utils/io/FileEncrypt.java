package com.orion.utils.io;

import com.orion.constant.Const;
import com.orion.constant.Letters;
import com.orion.utils.Arrays1;
import com.orion.utils.crypto.AES;
import com.orion.utils.crypto.Keys;
import com.orion.utils.crypto.enums.CipherAlgorithm;

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
 * @since 2020/7/15 18:02
 */
public class FileEncrypt implements Callable<Boolean> {

    private InputStream in;

    private OutputStream out;

    private String password;

    private int bufferSize;

    private boolean autoClose = true;

    public FileEncrypt(File file, File dest, String password) {
        this(file, dest, password, Const.BUFFER_KB_8);
    }

    public FileEncrypt(String file, String dest, String password) {
        this(file, dest, password, Const.BUFFER_KB_8);
    }

    public FileEncrypt(InputStream in, OutputStream out, String password) {
        this(in, out, password, Const.BUFFER_KB_8);
    }

    public FileEncrypt(File file, File dest, String password, int bufferSize) {
        this.password = password;
        this.bufferSize = bufferSize;
        Files1.touch(dest);
        this.in = Files1.openInputStreamSafe(file);
        this.out = Files1.openOutputStreamSafe(dest);
    }

    public FileEncrypt(String file, String dest, String password, int bufferSize) {
        this.password = password;
        this.bufferSize = bufferSize;
        Files1.touch(dest);
        this.in = Files1.openInputStreamSafe(file);
        this.out = Files1.openOutputStreamSafe(dest);
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
        SecretKey secretKey = Keys.generatorKey(password, CipherAlgorithm.AES);
        try {
            byte[] bs = new byte[bufferSize];
            int read;
            while ((read = in.read(bs)) != -1) {
                byte[] ebs = AES.encrypt(Arrays1.resize(bs, read), secretKey);
                out.write(ebs);
                out.write(Letters.LF);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (autoClose) {
                Streams.close(in);
                Streams.close(out);
            }
        }
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.io.crypto;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.crypto.AES;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * 文件加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/15 18:02
 */
public class FileEncrypt implements Callable<Boolean> {

    private final InputStream in;

    private final OutputStream out;

    private final String password;

    private final int bufferSize;

    private final boolean autoClose;

    public FileEncrypt(File file, File dest, String password) {
        this(file, dest, password, Const.BUFFER_KB_8);
    }

    public FileEncrypt(String file, String dest, String password) {
        this(file, dest, password, Const.BUFFER_KB_8);
    }

    public FileEncrypt(InputStream in, OutputStream out, String password) {
        this(in, out, password, Const.BUFFER_KB_8);
    }

    public FileEncrypt(String file, String dest, String password, int bufferSize) {
        this(new File(file), new File(dest), password, bufferSize);
    }

    public FileEncrypt(File file, File dest, String password, int bufferSize) {
        this.autoClose = true;
        this.password = password;
        this.bufferSize = bufferSize;
        Files1.touch(dest);
        this.in = Files1.openInputStreamSafe(file);
        this.out = Files1.openOutputStreamSafe(dest);
    }

    public FileEncrypt(InputStream in, OutputStream out, String password, int bufferSize) {
        this.autoClose = false;
        this.password = password;
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

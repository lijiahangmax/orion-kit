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
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.AES;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.concurrent.Callable;

/**
 * 文件解密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/15 18:03
 */
public class FileDecrypt implements Callable<Boolean> {

    private final BufferedReader reader;

    private final OutputStream out;

    private final String password;

    private final boolean autoClose;

    public FileDecrypt(File file, File dest, String password) {
        this(file, dest, password, Const.BUFFER_KB_8);
    }

    public FileDecrypt(String file, String dest, String password) {
        this(file, dest, password, Const.BUFFER_KB_8);
    }

    public FileDecrypt(InputStream in, OutputStream out, String password) {
        this(in, out, password, Const.BUFFER_KB_8);
    }

    public FileDecrypt(Reader reader, OutputStream out, String password) {
        this(reader, out, password, Const.BUFFER_KB_8);
    }

    public FileDecrypt(String file, String dest, String password, int bufferSize) {
        this(new File(file), new File(dest), password, bufferSize);
    }

    public FileDecrypt(File file, File dest, String password, int bufferSize) {
        this.autoClose = true;
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
        this.autoClose = false;
        this.password = password;
        this.reader = new BufferedReader(new InputStreamReader(in), bufferSize);
        this.out = out;
    }

    public FileDecrypt(Reader reader, OutputStream out, String password, int bufferSize) {
        this.autoClose = false;
        this.password = password;
        this.reader = new BufferedReader(reader, bufferSize);
        this.out = out;
    }

    @Override
    public Boolean call() {
        SecretKey secretKey = Keys.generatorKey(password, CipherAlgorithm.AES);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] d = AES.decrypt(Strings.bytes(line), secretKey);
                out.write(d);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (autoClose) {
                Streams.close(reader);
                Streams.close(out);
            }
        }
    }

}

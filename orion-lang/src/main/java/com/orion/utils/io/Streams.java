package com.orion.utils.io;

import com.orion.lang.iterator.LineIterator;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.crypto.enums.HashMessageDigest;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * io 操作
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/6 21:45
 */
@SuppressWarnings("ALL")
public class Streams {

    /**
     * 默认缓冲区大小
     */
    public static final int BUFFER_SIZE = 1024 * 8;

    /**
     * 编码格式
     */
    public static final String UFT_8 = "UTF-8";

    private Streams() {
    }

    // -------------------------------- 随机读写 --------------------------------

    /**
     * 从偏移量开始读取,读取到结尾
     *
     * @param reader 流
     * @param offset 偏移量
     * @return bytes
     * @throws IOException IO
     */
    public static byte[] read(RandomAccessFile reader, long offset) throws IOException {
        return read(reader, offset, reader.length());
    }

    /**
     * 从偏移量开始读取,读取到指定位置
     *
     * @param reader 流
     * @param offset 偏移量
     * @param end    结束位置
     * @return bytes
     * @throws IOException IO
     */
    public static byte[] read(RandomAccessFile reader, long offset, long end) throws IOException {
        reader.seek(offset);
        long e = end, len = reader.length();
        if (end > len) {
            e = end;
        }
        byte[] bs = new byte[((int) (end - offset))];
        reader.read(bs, 0, ((int) (e - offset)));
        return bs;
    }

    /**
     * 从当前偏移量读取一行
     *
     * @param reader reader
     * @return 行
     * @throws IOException IO
     */
    public static String readLine(RandomAccessFile reader) throws IOException {
        return readLine(reader, UFT_8);
    }

    /**
     * 从当前偏移量读取一行
     *
     * @param reader  reader
     * @param charset 编码
     * @return 行
     * @throws IOException IO
     */
    public static String readLine(RandomAccessFile reader, String charset) throws IOException {
        long def = reader.getFilePointer();
        byte[] bytes = new byte[BUFFER_SIZE];
        byte[] line = new byte[BUFFER_SIZE];
        int linePos = 0;
        int seek = 0;
        int read;
        while (-1 != (read = reader.read(bytes))) {
            seek += read;
            int bi = -1;
            for (int i = 0; i < read; i++) {
                byte b = bytes[i];
                if (b == 13) {
                    // \r
                    if (i + 1 < read) {
                        if (bytes[i + 1] == 10) {
                            seek++;
                        }
                        bi = i;
                        break;
                    } else {
                        byte[] bs1 = new byte[1];
                        int tmpRead = reader.read(bs1);
                        if (tmpRead != -1 && bs1[0] == 10) {
                            seek++;
                        }
                        bi = i;
                        break;
                    }
                } else if (b == 10) {
                    // \n
                    bi = i;
                    break;
                }
            }
            if (bi != -1) {
                line = Arrays1.arraycopy(bytes, 0, line, linePos, bi);
                linePos += bi;
                seek -= read - bi - 1;
                break;
            } else {
                line = Arrays1.arraycopy(bytes, 0, line, linePos, read);
                linePos += read;
            }
        }
        reader.seek(def + seek);
        if (seek == 0) {
            return null;
        }
        if (linePos == 0) {
            return Strings.EMPTY;
        }
        return new String(line, 0, linePos, charset);
    }

    /**
     * 读取所有行
     *
     * @param reader reader
     * @return lines
     * @throws IOException IOException
     */
    public static String readLines(RandomAccessFile reader) throws IOException {
        return readLines(reader, UFT_8);
    }

    /**
     * 从当前偏移量读取到最后一行
     *
     * @param reader 输入流
     * @return lines
     * @throws IOException I/O异常
     */
    public static String readLines(RandomAccessFile reader, String charset) throws IOException {
        long pos = reader.getFilePointer();
        int num;
        List<byte[]> lineBuffer = new ArrayList<>();
        byte[] buffer = new byte[BUFFER_SIZE];
        int allSize = 0;
        while ((num = reader.read(buffer)) != -1) {
            byte[] bs = new byte[num];
            System.arraycopy(buffer, 0, bs, 0, num);
            lineBuffer.add(bs);
            allSize += num;
        }
        byte[] abs = new byte[allSize];
        int tp = 0;
        for (int i = 0, len = lineBuffer.size(); i < len; i++) {
            byte[] bytes = lineBuffer.get(i);
            int ul = bytes.length;
            if (i == len - 1) {
                int p = ul < 2 ? 0 : ul - 2;
                for (int j = p; j < bytes.length; j++) {
                    if (bytes[j] == 10 || bytes[j] == 13) {
                        ul--;
                    }
                }
            }
            System.arraycopy(bytes, 0, abs, tp, ul);
            tp += bytes.length;
        }
        reader.seek(pos + allSize);
        if (allSize == 0) {
            return null;
        }
        return new String(abs, charset);
    }

    // -------------------------------- 关闭流 --------------------------------

    public static void close(Reader input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void close(Writer output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void close(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void close(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void close(AutoCloseable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception ioe) {
            // ignore
        }
    }

    // -------------------------------- 复制流 --------------------------------

    public static int transfer(RandomAccessFile access, OutputStream output) throws IOException {
        long count = transferLarge(access, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long transferLarge(RandomAccessFile access, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = access.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static int transfer(InputStream input, OutputStream output) throws IOException {
        long count = transferLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long transferLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void transfer(InputStream input, Writer output) throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        transfer(in, output);
    }

    public static void transfer(InputStream input, Writer output, String charset) throws IOException {
        if (charset == null) {
            transfer(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, charset);
            transfer(in, output);
        }
    }

    public static int transfer(Reader input, Writer output) throws IOException {
        long count = transferLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long transferLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void transfer(Reader input, OutputStream output) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        transfer(input, out);
        out.flush();
    }

    public static void transfer(Reader input, OutputStream output, String charset) throws IOException {
        if (charset == null) {
            transfer(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, charset);
            transfer(input, out);
            out.flush();
        }
    }

    // -------------------------------- 读取所有行 --------------------------------

    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(new InputStreamReader(input));
    }

    public static List readLines(InputStream input, String charset) throws IOException {
        if (charset == null) {
            return readLines(input);
        } else {
            return readLines(new InputStreamReader(input, charset));
        }
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List<String> list = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    // -------------------------------- 写入 --------------------------------

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(byte[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(new String(data));
        }
    }

    public static void write(byte[] data, Writer output, String charset) throws IOException {
        if (data != null) {
            if (charset == null) {
                output.write(new String(data));
            } else {
                output.write(new String(data, charset));
            }
        }
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(char[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(Strings.bytes(new String(data)));
        }
    }

    public static void write(char[] data, OutputStream output, String charset) throws IOException {
        if (data != null) {
            if (charset == null) {
                output.write(Strings.bytes(new String(data)));
            } else {
                output.write(Strings.bytes(new String(data)));
            }
        }
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(String data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(Strings.bytes(data));
        }
    }

    public static void write(String data, OutputStream output, String charset) throws IOException {
        if (data != null) {
            if (charset == null) {
                output.write(Strings.bytes(data));
            } else {
                output.write(Strings.bytes(data, charset));
            }
        }
    }

    public static void write(StringBuffer data, Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    public static void writeLines(Collection lines, String eof, OutputStream output) throws IOException {
        if (lines == null) {
            return;
        }
        if (eof == null) {
            eof = Systems.LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                output.write(Strings.bytes(line.toString()));
            }
            output.write(Strings.bytes(eof));
        }
    }

    public static void writeLines(Collection lines, String eof, OutputStream output, String charset) throws IOException {
        if (charset == null) {
            writeLines(lines, charset, output);
        } else {
            if (lines == null) {
                return;
            }
            if (eof == null) {
                eof = Systems.LINE_SEPARATOR;
            }
            for (Object line : lines) {
                if (line != null) {
                    output.write(Strings.bytes(line.toString(), charset));
                }
                output.write(Strings.bytes(eof, charset));
            }
        }
    }

    public static void writeLines(Collection lines, String eof, Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (eof == null) {
            eof = Systems.LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(eof);
        }
    }

    // -------------------------------- 流转字符 --------------------------------

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transfer(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transfer(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String charset) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transfer(input, output, charset);
        return output.toByteArray();
    }

    public static char[] toCharArray(InputStream is) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        transfer(is, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream is, String charset) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        transfer(is, output, charset);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        transfer(input, sw);
        return sw.toCharArray();
    }

    public static String toString(InputStream input) throws IOException {
        StringWriter sw = new StringWriter();
        transfer(input, sw);
        return sw.toString();
    }

    public static String toString(InputStream input, String charset) throws IOException {
        StringWriter sw = new StringWriter();
        transfer(input, sw, charset);
        return sw.toString();
    }

    public static String toString(Reader input) throws IOException {
        StringWriter sw = new StringWriter();
        transfer(input, sw);
        return sw.toString();
    }

    public static String toString(byte[] input) throws IOException {
        return new String(input);
    }

    public static String toString(byte[] input, String charset) throws IOException {
        if (charset == null) {
            return new String(input);
        } else {
            return new String(input, charset);
        }
    }

    // -------------------------------- 转流 --------------------------------

    public static InputStream toInputStream(byte[] bs) {
        return new ByteArrayInputStream(bs);
    }

    public static InputStream toInputStream(byte[] bs, int off, int len) {
        return new ByteArrayInputStream(bs, off, len);
    }

    public static InputStream toInputStream(String input) {
        return new ByteArrayInputStream(Strings.bytes(input));
    }

    public static InputStream toInputStream(String input, String charset) {
        byte[] bytes = charset != null ? Strings.bytes(input, charset) : Strings.bytes(input);
        return new ByteArrayInputStream(bytes);
    }

    public static OutputStream toOutputStream(byte[] bs) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(bs);
        } catch (Exception e) {
            // ignore
        }
        return out;
    }

    public static OutputStream toOutputStream(byte[] bs, int off, int len) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(bs, off, len);
        } catch (Exception e) {
            // ignore
        }
        return out;
    }

    public static OutputStream toOutputStream(String input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(Strings.bytes(input));
        } catch (Exception e) {
            // ignore
        }
        return out;
    }

    public static OutputStream toOutputStream(String input, String charset) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(charset != null ? Strings.bytes(input, charset) : Strings.bytes(input));
        } catch (Exception e) {
            // ignore
        }
        return out;
    }

    // -------------------------------- 签名 --------------------------------

    /**
     * 流 MD5 签名
     *
     * @param in 流
     * @return 签名
     */
    public static String md5(InputStream in) {
        return sign(in, HashMessageDigest.MD5);
    }

    /**
     * 流 SHA1 签名
     *
     * @param in 流
     * @return 签名
     */
    public static String sha1(InputStream in) {
        return sign(in, HashMessageDigest.SHA1);
    }

    /**
     * 流 SHA224 签名
     *
     * @param in 流
     * @return 签名
     */
    public static String sha224(InputStream in) {
        return sign(in, HashMessageDigest.SHA224);
    }

    /**
     * 流 SHA256 签名
     *
     * @param in 流
     * @return 签名
     */
    public static String sha256(InputStream in) {
        return sign(in, HashMessageDigest.SHA256);
    }

    /**
     * 流 SHA384 签名
     *
     * @param in 流
     * @return 签名
     */
    public static String sha384(InputStream in) {
        return sign(in, HashMessageDigest.SHA256);
    }

    /**
     * 流 SHA512 签名
     *
     * @param in 流
     * @return 签名
     */
    public static String sha512(InputStream in) {
        return sign(in, HashMessageDigest.SHA512);
    }

    /**
     * 散列签名
     *
     * @param in   流
     * @param type 加密类型 MD5 SHA-1 SHA-224 SHA-256 SHA-384 SHA-512
     * @return 签名
     */
    public static String sign(InputStream in, HashMessageDigest type) {
        try {
            MessageDigest m = type.getMessageDigest();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) != -1) {
                m.update(buffer, 0, length);
            }
            return new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    // -------------------------------- 行迭代器 --------------------------------

    public static void lineConsumer(InputStream in, Consumer<String> c) throws IOException {
        try {
            lineConsumer(new InputStreamReader(in), c);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding();
        }
    }

    public static void lineConsumer(InputStream in, String charset, Consumer<String> c) throws IOException {
        if (charset == null) {
            lineConsumer(in, c);
            return;
        }
        try {
            lineConsumer(new InputStreamReader(in, charset), c);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unCoding();
        }
    }

    public static void lineConsumer(Reader reader, Consumer<String> c) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            c.accept(line);
        }
    }

    public static void lineConsumer(Reader reader, int bufferSize, Consumer<String> c) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader, bufferSize);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            c.accept(line);
        }
    }

    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    public static LineIterator lineIterator(InputStream input) throws IOException {
        return new LineIterator(new InputStreamReader(input));
    }

    public static LineIterator lineIterator(InputStream input, String charset) throws IOException {
        return new LineIterator(new InputStreamReader(input, charset));
    }

}

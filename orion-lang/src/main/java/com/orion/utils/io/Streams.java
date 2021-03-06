package com.orion.utils.io;

import com.orion.constant.Const;
import com.orion.lang.iterator.ByteArrayIterator;
import com.orion.lang.iterator.LineIterator;
import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.Valid;
import com.orion.utils.crypto.Signatures;
import com.orion.utils.crypto.enums.HashMessageDigest;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * io 操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/6 21:45
 */
@SuppressWarnings("ALL")
public class Streams {

    private Streams() {
    }

    // -------------------- close --------------------

    public static void close(AutoCloseable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception ioe) {
            // ignore
        }
    }

    // -------------------- flush --------------------

    public static void flush(Flushable f) {
        try {
            if (f != null) {
                f.flush();
            }
        } catch (Exception ioe) {
            // ignore
        }
    }

    // -------------------- transfer --------------------

    public static int transfer(RandomAccessFile access, OutputStream output) throws IOException {
        long count = transferLarge(access, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long transferLarge(RandomAccessFile access, OutputStream output) throws IOException {
        byte[] buffer = new byte[Const.BUFFER_KB_8];
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
        byte[] buffer = new byte[Const.BUFFER_KB_8];
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
        char[] buffer = new char[Const.BUFFER_KB_8];
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

    // -------------------- read lines --------------------

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

    // -------------------- write --------------------

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

    // -------------------- convert byte array --------------------

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

    // -------------------- convert stream --------------------

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

    // -------------------- sign --------------------

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
            byte[] buffer = new byte[Const.BUFFER_KB_8];
            int length;
            while ((length = in.read(buffer)) != -1) {
                m.update(buffer, 0, length);
            }
            return Signatures.toHex(m.digest());
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- line consumer --------------------

    public static void lineConsumer(InputStream in, Consumer<String> c) throws IOException {
        lineConsumer(new InputStreamReader(in), Const.BUFFER_KB_4, c);
    }

    public static void lineConsumer(InputStream in, String charset, Consumer<String> c) throws IOException {
        if (charset == null) {
            lineConsumer(in, c);
            return;
        }
        lineConsumer(new InputStreamReader(in, charset), Const.BUFFER_KB_4, c);
    }

    public static void lineConsumer(Reader reader, Consumer<String> c) throws IOException {
        lineConsumer(reader, Const.BUFFER_KB_4, c);
    }

    public static void lineConsumer(Reader reader, int bufferSize, Consumer<String> c) throws IOException {
        Valid.notNull(reader, "reader is null");
        BufferedReader bufferedReader;
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader, bufferSize);
        }
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            c.accept(line);
        }
    }

    // -------------------- line iterator --------------------

    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    public static LineIterator lineIterator(InputStream input) throws IOException {
        return new LineIterator(new InputStreamReader(input));
    }

    public static LineIterator lineIterator(InputStream input, String charset) throws IOException {
        return new LineIterator(new InputStreamReader(input, charset));
    }

    // -------------------- byte array consumer --------------------

    public static void byteArrayConsumer(InputStream input, byte[] buffer, IntConsumer consumer) throws IOException {
        int read;
        while ((read = input.read(buffer)) != -1) {
            consumer.accept(read);
        }
    }

    // -------------------- byte array iterator --------------------

    public static ByteArrayIterator byteArrayIterator(InputStream input, byte[] buffer) throws IOException {
        return new ByteArrayIterator(input, buffer);
    }

}

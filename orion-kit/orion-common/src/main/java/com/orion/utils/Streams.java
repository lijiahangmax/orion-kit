package com.orion.utils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

/**
 * io 操作
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/6 21:45
 */
@SuppressWarnings("ALL")
public class Streams {

    /**
     * 默认缓冲区大小
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * 头文件
     */
    private static final Map<String, String> FILE_HEAD_MAP = new LinkedHashMap<>();

    static {
        FILE_HEAD_MAP.put("doc", "D0CF11E0A1B11AE10000000000000000");
        FILE_HEAD_MAP.put("xls", "D0CF11E0A1B11AE10000000000000000");
        FILE_HEAD_MAP.put("exe", "4D5A90000300000004000000FFFF0000");
        FILE_HEAD_MAP.put("dll", "4D5A90000300000004000000FFFF0000");
        FILE_HEAD_MAP.put("png", "89504E470D0A1A0A0000000D49484452");
        FILE_HEAD_MAP.put("lnk", "4C0000000114020000000000C0000000");
        FILE_HEAD_MAP.put("xsd", "3C3F786D6C2076657273696F6E3D2231");
        FILE_HEAD_MAP.put("rmvb", "2E524D46000000120001000000000000");
        FILE_HEAD_MAP.put("avi", "5249464616BD5301415649204C495354");
        FILE_HEAD_MAP.put("mkv", "1A45DFA3A34286810142F7810142F281");
        FILE_HEAD_MAP.put("gif", "474946383961E0010E01E70000000000");
        FILE_HEAD_MAP.put("wmv", "3026B2758E66CF11A6D900AA0062CE6C");
        FILE_HEAD_MAP.put("eml", "52656365697665643A2066726F6D2073");
        FILE_HEAD_MAP.put("psd", "38425053000100000000000000030000");
        FILE_HEAD_MAP.put("xmind", "504B0304140008080800");
        FILE_HEAD_MAP.put("mdb", "5374616E64617264204A");
        FILE_HEAD_MAP.put("eps", "252150532D41646F6265");
        FILE_HEAD_MAP.put("ps", "252150532D41646F6265");
        FILE_HEAD_MAP.put("asf", "3026B2758E66CF11");
        FILE_HEAD_MAP.put("dbx", "CFAD12FEC5FD746F");
        FILE_HEAD_MAP.put("7z", "377ABCAF271C0004");
        FILE_HEAD_MAP.put("pdf", "255044462D312E3");
        FILE_HEAD_MAP.put("zip", "504B03040A00000");
        FILE_HEAD_MAP.put("rar", "526172211A070");
        FILE_HEAD_MAP.put("rtf", "7B5C727466");
        FILE_HEAD_MAP.put("tif", "49492A00");
        FILE_HEAD_MAP.put("dwg", "41433130");
        FILE_HEAD_MAP.put("pst", "2142444E");
        FILE_HEAD_MAP.put("docx", "504B0304");
        FILE_HEAD_MAP.put("xlsx", "504B0304");
        FILE_HEAD_MAP.put("jar", "504B0304");
        FILE_HEAD_MAP.put("war", "504B0304");
        FILE_HEAD_MAP.put("wpd", "FF575043");
        FILE_HEAD_MAP.put("class", "CAFEBABE");
        FILE_HEAD_MAP.put("ram", "2E7261FD");
        FILE_HEAD_MAP.put("qdf", "AC9EBD8F");
        FILE_HEAD_MAP.put("pwl", "E3828596");
        FILE_HEAD_MAP.put("rm", "2E524D46");
        FILE_HEAD_MAP.put("html", "3C21444F");
        FILE_HEAD_MAP.put("mpg", "000001BA");
        FILE_HEAD_MAP.put("mov", "6D6F6F76");
        FILE_HEAD_MAP.put("mid", "4D546864");
        FILE_HEAD_MAP.put("jpg", "FFD8FFE");
        FILE_HEAD_MAP.put("mp4", "000000");
        FILE_HEAD_MAP.put("bmp", "424D");
        FILE_HEAD_MAP.put("xml", "3C");
    }

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
        return readLine(reader, "UTF-8");
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
        byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
        byte[] line = new byte[DEFAULT_BUFFER_SIZE];
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
            return "";
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
        return readLines(reader, "UTF-8");
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
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
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

    public static void closeQuietly(Reader input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(Writer output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    // -------------------------------- 复制流 --------------------------------

    public static int copy(RandomAccessFile access, OutputStream output) throws IOException {
        long count = copyLarge(access, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(RandomAccessFile access, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = access.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void copy(InputStream input, Writer output) throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    public static void copy(InputStream input, Writer output, String encode) throws IOException {
        if (encode == null) {
            copy(input, output);
        } else {
            InputStreamReader in = new InputStreamReader(input, encode);
            copy(in, output);
        }
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void copy(Reader input, OutputStream output) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, out);
        out.flush();
    }

    public static void copy(Reader input, OutputStream output, String encode) throws IOException {
        if (encode == null) {
            copy(input, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encode);
            copy(input, out);
            out.flush();
        }
    }

    // -------------------------------- 读取所有行 --------------------------------

    public static List readLines(InputStream input) throws IOException {
        return readLines(new InputStreamReader(input));
    }

    public static List readLines(InputStream input, String encode) throws IOException {
        if (encode == null) {
            return readLines(input);
        } else {
            return readLines(new InputStreamReader(input, encode));
        }
    }

    public static List readLines(Reader input) throws IOException {
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

    public static void write(byte[] data, Writer output, String encode) throws IOException {
        if (data != null) {
            if (encode == null) {
                write(data, output);
            } else {
                output.write(new String(data, encode));
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
            output.write(new String(data).getBytes());
        }
    }

    public static void write(char[] data, OutputStream output, String encode) throws IOException {
        if (data != null) {
            if (encode == null) {
                write(data, output);
            } else {
                output.write(new String(data).getBytes(encode));
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
            output.write(data.getBytes());
        }
    }

    public static void write(String data, OutputStream output, String encode) throws IOException {
        if (data != null) {
            if (encode == null) {
                write(data, output);
            } else {
                output.write(data.getBytes(encode));
            }
        }
    }

    public static void write(StringBuffer data, Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    public static void write(StringBuffer data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes());
        }
    }

    public static void write(StringBuffer data, OutputStream output, String encode) throws IOException {
        if (data != null) {
            if (encode == null) {
                write(data, output);
            } else {
                output.write(data.toString().getBytes(encode));
            }
        }
    }

    public static void writeLines(Collection lines, String lineEnding, OutputStream output) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = Systems.LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes());
            }
            output.write(lineEnding.getBytes());
        }
    }

    public static void writeLines(Collection lines, String lineEnding, OutputStream output, String encode) throws IOException {
        if (encode == null) {
            writeLines(lines, lineEnding, output);
        } else {
            if (lines == null) {
                return;
            }
            if (lineEnding == null) {
                lineEnding = Systems.LINE_SEPARATOR;
            }
            for (Object line : lines) {
                if (line != null) {
                    output.write(line.toString().getBytes(encode));
                }
                output.write(lineEnding.getBytes(encode));
            }
        }
    }

    public static void writeLines(Collection lines, String lineEnding, Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = Systems.LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    // -------------------------------- 流转字符 --------------------------------

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encode) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, encode);
        return output.toByteArray();
    }

    public static char[] toCharArray(InputStream is) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream is, String encode) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        copy(is, output, encode);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(input, sw);
        return sw.toCharArray();
    }

    public static String toString(InputStream input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static String toString(InputStream input, String encode) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw, encode);
        return sw.toString();
    }

    public static String toString(Reader input) throws IOException {
        StringWriter sw = new StringWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static String toString(byte[] input, String encode) throws IOException {
        if (encode == null) {
            return new String(input);
        } else {
            return new String(input, encode);
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
        return new ByteArrayInputStream(input.getBytes());
    }

    public static InputStream toInputStream(String input, String encode) throws IOException {
        byte[] bytes = encode != null ? input.getBytes(encode) : input.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    // -------------------------------- 签名 --------------------------------

    /**
     * 流 MD5签名
     *
     * @param in 流
     * @return 签名
     */
    public static String md5(InputStream in) {
        return sign(in, "MD5");
    }

    /**
     * 流 SHA1签名
     *
     * @param in 流
     * @return 签名
     */
    public static String sha1(InputStream in) {
        return sign(in, "SHA-1");
    }

    /**
     * 散列签名
     *
     * @param in   流
     * @param type 加密类型 MD5 SHA-1 SHA-256 SHA-384 SHA-512
     * @return 签名
     */
    public static String sign(InputStream in, String type) {
        try {
            MessageDigest m = MessageDigest.getInstance(type);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) != -1) {
                m.update(buffer, 0, length);
            }
            return new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------------------- 文件流类型 --------------------------------

    /**
     * 通过文件头推算文件类型 不准确
     *
     * @param in 文件
     * @return 类型
     */
    public static String getFileType(InputStream in) {
        String type = null;
        byte[] b = new byte[20];
        try {
            if (in.read(b) != -1) {
                type = getFileType(b);
            }
        } catch (IOException e) {
            // ignore
        }
        return type;
    }

    /**
     * 通过文件头推算文件类型 不准确
     *
     * @param b 文件头
     * @return 类型
     */
    public static String getFileType(byte[] b) {
        for (Map.Entry<String, String> entry : FILE_HEAD_MAP.entrySet()) {
            String hexValue = entry.getValue();
            if (String.valueOf(getHexString(b)).toUpperCase().startsWith(hexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 数组转16进制无符号字符
     *
     * @param b 数组
     * @return 16进制
     */
    private static String getHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (byte aB : b) {
            int v = aB & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    // -------------------------------- 行迭代器 --------------------------------

    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    public static LineIterator lineIterator(InputStream input, String encode) throws IOException {
        Reader reader;
        if (encode == null) {
            reader = new InputStreamReader(input);
        } else {
            reader = new InputStreamReader(input, encode);
        }
        return new LineIterator(reader);
    }

    public static class LineIterator implements Iterator {

        private final BufferedReader bufferedReader;
        private String cachedLine;
        private boolean finished = false;

        private LineIterator(final Reader reader) throws IllegalArgumentException {
            if (reader == null) {
                throw new IllegalArgumentException("Reader must not be null");
            }
            if (reader instanceof BufferedReader) {
                bufferedReader = (BufferedReader) reader;
            } else {
                bufferedReader = new BufferedReader(reader);
            }
        }

        @Override
        public boolean hasNext() {
            if (cachedLine != null) {
                return true;
            } else if (finished) {
                return false;
            } else {
                try {
                    for (; ; ) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            finished = true;
                            return false;
                        } else {
                            cachedLine = line;
                            return true;
                        }
                    }
                } catch (IOException ioe) {
                    close();
                    throw new IllegalStateException(ioe.toString());
                }
            }
        }

        @Override
        public Object next() {
            return nextLine();
        }

        public String nextLine() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more lines");
            }
            String currentLine = cachedLine;
            cachedLine = null;
            return currentLine;
        }

        public void close() {
            finished = true;
            Streams.closeQuietly(bufferedReader);
            cachedLine = null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove unsupported on LineIterator");
        }

        public static void closeQuietly(LineIterator iterator) {
            if (iterator != null) {
                iterator.close();
            }
        }
    }

}

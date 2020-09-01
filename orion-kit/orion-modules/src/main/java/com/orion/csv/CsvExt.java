package com.orion.csv;

import com.orion.csv.core.CsvReader;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * CSV 提取
 * 也支持 TXT
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/27 11:07
 */
public class CsvExt {

    /**
     * 默认分隔符
     */
    private static final char DEFAULT_SPLIT_CHAR = ',';

    /**
     * 默认编码格式
     */
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private CsvReader reader;

    public CsvExt(File file) throws IOException {
        this(Files1.openInputStream(file), DEFAULT_SPLIT_CHAR, DEFAULT_CHARSET);
    }

    public CsvExt(File file, String charset) throws IOException {
        this(Files1.openInputStream(file), DEFAULT_SPLIT_CHAR, Charset.forName(charset));
    }

    public CsvExt(File file, char splitChar) throws IOException {
        this(Files1.openInputStream(file), splitChar, DEFAULT_CHARSET);
    }

    public CsvExt(File file, char splitChar, String charset) throws IOException {
        this(Files1.openInputStream(file), splitChar, Charset.forName(charset));
    }

    public CsvExt(String file) {
        this(CsvExt.class.getClassLoader().getResourceAsStream(file), DEFAULT_SPLIT_CHAR, DEFAULT_CHARSET);
    }

    public CsvExt(String file, String charset) {
        this(CsvExt.class.getClassLoader().getResourceAsStream(file), DEFAULT_SPLIT_CHAR, Charset.forName(charset));
    }

    public CsvExt(String file, char splitChar) {
        this(CsvExt.class.getClassLoader().getResourceAsStream(file), splitChar, DEFAULT_CHARSET);
    }

    public CsvExt(String file, char splitChar, String charset) {
        this(CsvExt.class.getClassLoader().getResourceAsStream(file), splitChar, Charset.forName(charset));
    }

    public CsvExt(InputStream in) {
        this(in, DEFAULT_SPLIT_CHAR, DEFAULT_CHARSET);
    }

    public CsvExt(InputStream in, String charset) {
        this(in, DEFAULT_SPLIT_CHAR, Charset.forName(charset));
    }

    public CsvExt(InputStream in, char splitChar) {
        this(in, splitChar, DEFAULT_CHARSET);
    }

    public CsvExt(InputStream in, char splitChar, String charset) {
        this(in, splitChar, Charset.forName(charset));
    }

    private CsvExt(InputStream in, char splitChar, Charset charset) {
        this.reader = new CsvReader(in, splitChar, charset);
    }

    private CsvExt(CsvReader reader) {
        this.reader = reader;
    }

    /**
     * CSV 读取流
     *
     * @return 流
     */
    public CvsStream stream() {
        return new CvsStream(reader);
    }

    /**
     * 关闭读取流
     */
    public void close() {
        reader.close();
    }

    /**
     * 将文本转化为 CsvExt
     *
     * @param s s
     * @return CsvExt
     */
    public static CsvExt parse(String s) {
        return new CsvExt(CsvReader.parse(s));
    }

}

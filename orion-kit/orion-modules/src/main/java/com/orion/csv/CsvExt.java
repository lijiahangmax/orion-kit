package com.orion.csv;

import com.orion.csv.core.CsvReader;
import com.orion.csv.core.CsvSymbol;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * CSV 提取
 * 也支持其他格式的文本文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/27 11:07
 */
public class CsvExt {

    private CsvReader reader;

    public CsvExt(File file) throws IOException {
        this(Files1.openInputStream(file), CsvSymbol.DEFAULT_SYMBOL, CsvSymbol.DEFAULT_CHARSET);
    }

    public CsvExt(File file, CsvSymbol s) throws IOException {
        this(Files1.openInputStream(file), s.getSymbol(), s.getCharset());
    }

    public CsvExt(String file) {
        this(CsvExt.class.getClassLoader().getResourceAsStream(file), CsvSymbol.DEFAULT_SYMBOL, CsvSymbol.DEFAULT_CHARSET);
    }

    public CsvExt(String file, CsvSymbol s) {
        this(CsvExt.class.getClassLoader().getResourceAsStream(file), s.getSymbol(), s.getCharset());
    }

    public CsvExt(InputStream in) {
        this(in, CsvSymbol.DEFAULT_SYMBOL, CsvSymbol.DEFAULT_CHARSET);
    }

    public CsvExt(InputStream in, CsvSymbol s) {
        this(in, s.getSymbol(), s.getCharset());
    }

    private CsvExt(InputStream in, char symbol, Charset charset) {
        this.reader = new CsvReader(in, symbol, charset);
    }

    private CsvExt(CsvReader reader) {
        this.reader = reader;
    }

    /**
     * CSV 读取流
     *
     * @return 流
     */
    public CsvStream stream() {
        return new CsvStream(reader);
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

package com.orion.csv.core;

import com.orion.utils.Objects1;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * CSV symbol
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 11:33
 */
public class CsvSymbol implements Serializable {

    /**
     * 默认分隔符
     */
    public static final char DEFAULT_SYMBOL = ',';

    /**
     * 默认编码格式
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 默认文件后缀
     */
    public static final String DEFAULT_SUFFIX = "csv";

    private char symbol = DEFAULT_SYMBOL;

    private Charset charset = DEFAULT_CHARSET;

    private String suffix = DEFAULT_SUFFIX;

    public CsvSymbol() {
    }

    public CsvSymbol(char symbol) {
        this.symbol = symbol;
    }

    public CsvSymbol(char symbol, String suffix) {
        this.symbol = symbol;
        this.suffix = Objects1.def(suffix, DEFAULT_SUFFIX);
    }

    public CsvSymbol(char symbol, Charset charset) {
        this.symbol = symbol;
        this.charset = Objects1.def(charset, DEFAULT_CHARSET);
    }

    public CsvSymbol(char symbol, Charset charset, String suffix) {
        this.symbol = symbol;
        this.charset = Objects1.def(charset, DEFAULT_CHARSET);
        this.suffix = Objects1.def(suffix, DEFAULT_SUFFIX);
    }

    private static CsvSymbol of(char symbol) {
        return new CsvSymbol(symbol);
    }

    private static CsvSymbol of(char symbol, String suffix) {
        return new CsvSymbol(symbol, suffix);
    }

    private static CsvSymbol of(char symbol, Charset charset) {
        return new CsvSymbol(symbol, Objects1.def(charset, DEFAULT_CHARSET));
    }

    private static CsvSymbol of(char symbol, Charset charset, String suffix) {
        return new CsvSymbol(symbol, Objects1.def(charset, DEFAULT_CHARSET), Objects1.def(suffix, DEFAULT_SUFFIX));
    }

    public char getSymbol() {
        return symbol;
    }

    public CsvSymbol setSymbol(char symbol) {
        this.symbol = symbol;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public CsvSymbol setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public CsvSymbol setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public String toString() {
        return "'" + symbol + "'\t" + DEFAULT_CHARSET + "\t" + DEFAULT_SUFFIX;
    }

}

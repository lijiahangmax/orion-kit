/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.office.csv.reader;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.office.csv.core.CsvReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * csv raw 读取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/7 16:15
 */
public class CsvRawReader extends BaseCsvReader<String> {

    /**
     * 行默认值
     */
    private String defaultRaw;

    public CsvRawReader(CsvReader reader) {
        this(reader, new ArrayList<>(), null);
    }

    public CsvRawReader(CsvReader reader, Collection<String> rows) {
        this(reader, rows, null);
    }

    public CsvRawReader(CsvReader reader, Consumer<String> consumer) {
        this(reader, null, consumer);
    }

    protected CsvRawReader(CsvReader reader, Collection<String> rows, Consumer<String> consumer) {
        super(reader, rows, consumer);
        reader.getOption().setSkipRawRow(false);
    }

    /**
     * 空行默认行 为 ""
     *
     * @return this
     */
    public CsvRawReader defaultRawOfEmpty() {
        this.defaultRaw = Strings.EMPTY;
        return this;
    }

    /**
     * 空行默认行
     *
     * @param defaultRaw 默认行
     * @return this
     */
    public CsvRawReader defaultRaw(String defaultRaw) {
        this.defaultRaw = defaultRaw;
        return this;
    }

    @Override
    protected String parserRow(String[] row) {
        return Strings.def(super.getRaw(), defaultRaw);
    }

}

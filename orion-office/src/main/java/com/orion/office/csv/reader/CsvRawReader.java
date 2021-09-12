package com.orion.office.csv.reader;

import com.orion.office.csv.core.CsvReader;
import com.orion.utils.Strings;

import java.util.ArrayList;
import java.util.List;
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

    public CsvRawReader(CsvReader reader, List<String> rows) {
        this(reader, rows, null);
    }

    public CsvRawReader(CsvReader reader, Consumer<String> consumer) {
        this(reader, null, consumer);
    }

    protected CsvRawReader(CsvReader reader, List<String> rows, Consumer<String> consumer) {
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

package com.orion.csv.core;

import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.constant.Letters;
import com.orion.csv.option.CsvOption;
import com.orion.csv.option.CsvReaderOption;
import com.orion.exception.IORuntimeException;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * CSV 读取类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/1 23:21
 */
public class CsvReader implements SafeCloseable {

    private Reader reader;

    private DataBuffer dataBuffer = new DataBuffer();

    private ColumnBuffer columnBuffer = new ColumnBuffer();

    private RawRecordBuffer rawBuffer = new RawRecordBuffer();

    private HeadersHolder headersHolder = new HeadersHolder();

    /**
     * 行的列数据是否合格
     * index: column
     */
    private boolean[] isQualified;

    /**
     * 行数据
     */
    private String rawRecord = Strings.EMPTY;

    private boolean startedColumn;

    private boolean startedWithQualifier;

    private boolean hasMoreData = true;

    private char lastLetter = '\0';

    private boolean hasReadNextLine;

    private int currentColumnCount;

    /**
     * 当前记录行
     */
    private long currentRecord;

    /**
     * 配置
     */
    private CsvReaderOption option;

    /**
     * 是否已关闭
     */
    private boolean closed;

    /**
     * 当前行记录 复用
     */
    private String[] values = new String[CsvOption.INITIAL_COLUMN_COUNT];

    public CsvReader(String file) {
        this(file, Letters.COMMA, StandardCharsets.UTF_8);
    }

    public CsvReader(String file, char delimiter) {
        this(file, delimiter, StandardCharsets.UTF_8);
    }

    public CsvReader(String file, char delimiter, Charset charset) {
        this(file, new CsvReaderOption(delimiter, charset));
    }

    public CsvReader(String file, CsvReaderOption option) {
        this(Files1.openInputStreamSafe(file), option);
    }

    public CsvReader(File file) {
        this(file, Letters.COMMA, StandardCharsets.UTF_8);
    }

    public CsvReader(File file, char delimiter) {
        this(file, delimiter, StandardCharsets.UTF_8);
    }

    public CsvReader(File file, char delimiter, Charset charset) {
        this(file, new CsvReaderOption(delimiter, charset));
    }

    public CsvReader(File file, CsvReaderOption option) {
        this(Files1.openInputStreamSafe(file), option);
    }

    public CsvReader(InputStream in) {
        this(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    public CsvReader(InputStream in, Charset charset) {
        this(new InputStreamReader(in, charset));
    }

    public CsvReader(InputStream in, char delimiter, Charset charset) {
        this(new InputStreamReader(in, charset), delimiter);
    }

    public CsvReader(InputStream in, CsvReaderOption option) {
        Valid.notNull(in, "inputStream can not be null");
        this.option = Objects1.def(option, CsvReaderOption::new);
        this.reader = new InputStreamReader(in, this.option.getCharset());
        this.isQualified = new boolean[values.length];
    }

    public CsvReader(Reader reader) {
        this(reader, new CsvReaderOption(Letters.COMMA));
    }

    public CsvReader(Reader reader, char delimiter) {
        this(reader, new CsvReaderOption(delimiter));
    }

    public CsvReader(Reader reader, CsvReaderOption option) {
        Valid.notNull(reader, "reader can not be null");
        this.reader = reader;
        this.option = Objects1.def(option, CsvReaderOption::new);
        this.isQualified = new boolean[values.length];
    }

    /**
     * 解析
     *
     * @param data text
     * @return CsvReader
     */
    public static CsvReader parse(String data) {
        Valid.notBlank(data, "data is blank");
        return new CsvReader(new StringReader(data));
    }

    /**
     * 解析
     *
     * @param data      text
     * @param delimiter 分隔符
     * @return CsvReader
     */
    public static CsvReader parse(String data, char delimiter) {
        Valid.notBlank(data, "data is blank");
        return new CsvReader(new StringReader(data), new CsvReaderOption(delimiter));
    }

    /**
     * 解析
     *
     * @param data   text
     * @param option option
     * @return CsvReader
     */
    public static CsvReader parse(String data, CsvReaderOption option) {
        Valid.notBlank(data, "data is blank");
        return new CsvReader(new StringReader(data), option);
    }

    /**
     * 获取当前读取列的值
     *
     * @return 当前值
     */
    public String[] getRow() {
        String[] clone = new String[currentColumnCount];
        System.arraycopy(values, 0, clone, 0, currentColumnCount);
        return clone;
    }

    /**
     * 获取当前读取列的值
     *
     * @return 当前读取列的值
     */
    public String getRawRow() {
        return rawRecord;
    }

    /**
     * 获取当前读取列的值
     *
     * @param columnIndex 列
     * @return 值
     */
    public String get(int columnIndex) {
        if (columnIndex > -1 && columnIndex < currentColumnCount) {
            return values[columnIndex];
        } else {
            return Strings.EMPTY;
        }
    }

    /**
     * 获取当前列标题对应的值
     *
     * @param headerName 标题
     * @return 值
     */
    public String get(String headerName) {
        return this.get(this.getHeaderIndex(headerName));
    }

    /**
     * 设置标题
     *
     * @param headers 标题
     */
    public void setHeaders(String[] headers) {
        headersHolder.headers = headers;
        headersHolder.indexByName.clear();
        if (headers != null) {
            headersHolder.length = headers.length;
        } else {
            headersHolder.length = 0;
        }
        for (int i = 0; i < headersHolder.length; i++) {
            headersHolder.indexByName.put(headers[i], i);
        }
    }

    /**
     * 读取数据的第一条记录作为列标题
     *
     * @return 是否成功
     * @throws IOException IOException
     */
    public boolean readHeaders() throws IOException {
        boolean result = this.readRow();
        headersHolder.length = currentColumnCount;
        headersHolder.headers = new String[currentColumnCount];
        for (int i = 0; i < headersHolder.length; i++) {
            String columnValue = get(i);
            headersHolder.headers[i] = columnValue;
            headersHolder.indexByName.put(columnValue, i);
        }
        if (result) {
            currentRecord--;
        }
        currentColumnCount = 0;
        return result;
    }

    /**
     * 读取一行数据
     *
     * @return 是否成功
     * @throws IOException IOException
     */
    public boolean readRow() throws IOException {
        this.checkClosed();
        currentColumnCount = 0;
        rawBuffer.position = 0;
        dataBuffer.lineStart = dataBuffer.position;
        hasReadNextLine = false;
        if (hasMoreData) {
            do {
                if (dataBuffer.position == dataBuffer.count) {
                    this.checkDataLength();
                } else {
                    startedWithQualifier = false;
                    char currentLetter = dataBuffer.buffer[dataBuffer.position];
                    if (option.isUseTextQualifier() && currentLetter == option.getTextQualifier()) {
                        lastLetter = currentLetter;
                        startedColumn = true;
                        dataBuffer.columnStart = dataBuffer.position + 1;
                        startedWithQualifier = true;
                        boolean lastLetterWasQualifier = false;
                        char escapeChar = option.getTextQualifier();
                        if (option.getEscapeMode() == CsvOption.ESCAPE_MODE_BACKSLASH) {
                            escapeChar = Letters.BACKSLASH;
                        }
                        boolean eatingTrailingJunk = false;
                        boolean lastLetterWasEscape = false;
                        boolean readingComplexEscape = false;
                        int escape = CsvOption.UNICODE;
                        int escapeLength = 0;
                        char escapeValue = (char) 0;
                        dataBuffer.position++;
                        do {
                            if (dataBuffer.position == dataBuffer.count) {
                                this.checkDataLength();
                            } else {
                                currentLetter = dataBuffer.buffer[dataBuffer.position];
                                if (eatingTrailingJunk) {
                                    dataBuffer.columnStart = dataBuffer.position + 1;
                                    if (currentLetter == option.getDelimiter()) {
                                        this.endColumn();
                                    } else if ((!option.isUseCustomLineDelimiter() && (currentLetter == Letters.CR || currentLetter == Letters.LF))
                                            || (option.isUseCustomLineDelimiter() && currentLetter == option.getLineDelimiter())) {
                                        this.endColumn();
                                        this.endRecord();
                                    }
                                } else if (readingComplexEscape) {
                                    escapeLength++;
                                    switch (escape) {
                                        case CsvOption.UNICODE:
                                            escapeValue *= (char) 16;
                                            escapeValue += hexToDec(currentLetter);
                                            if (escapeLength == 4) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                        case CsvOption.OCTAL:
                                            escapeValue *= (char) 8;
                                            escapeValue += (char) (currentLetter - '0');
                                            if (escapeLength == 3) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                        case CsvOption.DECIMAL:
                                            escapeValue *= (char) 10;
                                            escapeValue += (char) (currentLetter - '0');
                                            if (escapeLength == 3) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                        case CsvOption.HEX:
                                            escapeValue *= (char) 16;
                                            escapeValue += hexToDec(currentLetter);

                                            if (escapeLength == 2) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                    }
                                    if (!readingComplexEscape) {
                                        this.appendLetter(escapeValue);
                                    } else {
                                        dataBuffer.columnStart = dataBuffer.position + 1;
                                    }
                                } else if (currentLetter == option.getTextQualifier()) {
                                    if (lastLetterWasEscape) {
                                        lastLetterWasEscape = false;
                                        lastLetterWasQualifier = false;
                                    } else {
                                        this.updateCurrentValue();
                                        if (option.getEscapeMode() == CsvOption.ESCAPE_MODE_DOUBLED) {
                                            lastLetterWasEscape = true;
                                        }
                                        lastLetterWasQualifier = true;
                                    }
                                } else if (option.getEscapeMode() == CsvOption.ESCAPE_MODE_BACKSLASH && lastLetterWasEscape) {
                                    switch (currentLetter) {
                                        case 'n':
                                            appendLetter(Letters.LF);
                                            break;
                                        case 'r':
                                            appendLetter(Letters.CR);
                                            break;
                                        case 't':
                                            appendLetter(Letters.TAB);
                                            break;
                                        case 'b':
                                            appendLetter(Letters.BACKSPACE);
                                            break;
                                        case 'f':
                                            appendLetter(Letters.FORM_FEED);
                                            break;
                                        case 'e':
                                            appendLetter(Letters.ESCAPE);
                                            break;
                                        case 'v':
                                            appendLetter(Letters.VERTICAL_TAB);
                                            break;
                                        case 'a':
                                            appendLetter(Letters.ALERT);
                                            break;
                                        case '0':
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                            escape = CsvOption.OCTAL;
                                            readingComplexEscape = true;
                                            escapeLength = 1;
                                            escapeValue = (char) (currentLetter - '0');
                                            dataBuffer.columnStart = dataBuffer.position + 1;
                                            break;
                                        case 'u':
                                        case 'x':
                                        case 'o':
                                        case 'd':
                                        case 'U':
                                        case 'X':
                                        case 'O':
                                        case 'D':
                                            switch (currentLetter) {
                                                case 'u':
                                                case 'U':
                                                    escape = CsvOption.UNICODE;
                                                    break;
                                                case 'x':
                                                case 'X':
                                                    escape = CsvOption.HEX;
                                                    break;
                                                case 'o':
                                                case 'O':
                                                    escape = CsvOption.OCTAL;
                                                    break;
                                                case 'd':
                                                case 'D':
                                                    escape = CsvOption.DECIMAL;
                                                    break;
                                            }
                                            readingComplexEscape = true;
                                            escapeLength = 0;
                                            escapeValue = (char) 0;
                                            dataBuffer.columnStart = dataBuffer.position + 1;
                                            break;
                                        default:
                                            break;
                                    }
                                    lastLetterWasEscape = false;
                                } else if (currentLetter == escapeChar) {
                                    this.updateCurrentValue();
                                    lastLetterWasEscape = true;
                                } else {
                                    if (lastLetterWasQualifier) {
                                        if (currentLetter == option.getDelimiter()) {
                                            this.endColumn();
                                        } else if ((!option.isUseCustomLineDelimiter() && (currentLetter == Letters.CR || currentLetter == Letters.LF))
                                                || (option.isUseCustomLineDelimiter() && currentLetter == option.getLineDelimiter())) {
                                            this.endColumn();
                                            this.endRecord();
                                        } else {
                                            dataBuffer.columnStart = dataBuffer.position + 1;
                                            eatingTrailingJunk = true;
                                        }
                                        lastLetterWasQualifier = false;
                                    }
                                }
                                lastLetter = currentLetter;
                                if (startedColumn) {
                                    dataBuffer.position++;
                                    if (option.isSafetySwitch() && dataBuffer.position - dataBuffer.columnStart + columnBuffer.position > 100000) {
                                        this.close();
                                        throw new IOException("maximum column length of 100,000 exceeded in column "
                                                + NumberFormat.getIntegerInstance().format(currentColumnCount)
                                                + " in record "
                                                + NumberFormat.getIntegerInstance().format(currentRecord)
                                                + ". Set the SafetySwitch property to false"
                                                + " if you're expecting column lengths greater than 100,000 characters to"
                                                + " avoid this error.");
                                    }
                                }
                            }
                        } while (hasMoreData && startedColumn);
                    } else if (currentLetter == option.getDelimiter()) {
                        lastLetter = currentLetter;
                        this.endColumn();
                    } else if (option.isUseCustomLineDelimiter() && currentLetter == option.getLineDelimiter()) {
                        if (startedColumn || currentColumnCount > 0 || !option.isSkipEmptyRows()) {
                            this.endColumn();
                            this.endRecord();
                        } else {
                            dataBuffer.lineStart = dataBuffer.position + 1;
                        }
                        lastLetter = currentLetter;
                    } else if (!option.isUseCustomLineDelimiter() && (currentLetter == Letters.CR || currentLetter == Letters.LF)) {
                        if (startedColumn || currentColumnCount > 0 || (!option.isSkipEmptyRows() && (currentLetter == Letters.CR || lastLetter != Letters.CR))) {
                            this.endColumn();
                            this.endRecord();
                        } else {
                            dataBuffer.lineStart = dataBuffer.position + 1;
                        }
                        lastLetter = currentLetter;
                    } else if (option.isUseComments() && currentColumnCount == 0 && currentLetter == option.getComment()) {
                        lastLetter = currentLetter;
                        this.clear();
                    } else if (option.isTrim() && (currentLetter == Letters.SPACE || currentLetter == Letters.TAB)) {
                        startedColumn = true;
                        dataBuffer.columnStart = dataBuffer.position + 1;
                    } else {
                        startedColumn = true;
                        dataBuffer.columnStart = dataBuffer.position;
                        boolean lastLetterWasBackslash = false;
                        boolean readingComplexEscape = false;
                        int escape = CsvOption.UNICODE;
                        int escapeLength = 0;
                        char escapeValue = (char) 0;
                        boolean firstLoop = true;
                        do {
                            if (!firstLoop && dataBuffer.position == dataBuffer.count) {
                                this.checkDataLength();
                            } else {
                                if (!firstLoop) {
                                    currentLetter = dataBuffer.buffer[dataBuffer.position];
                                }
                                if (!option.isUseTextQualifier() && option.getEscapeMode() == CsvOption.ESCAPE_MODE_BACKSLASH && currentLetter == Letters.BACKSLASH) {
                                    if (lastLetterWasBackslash) {
                                        lastLetterWasBackslash = false;
                                    } else {
                                        this.updateCurrentValue();
                                        lastLetterWasBackslash = true;
                                    }
                                } else if (readingComplexEscape) {
                                    escapeLength++;
                                    switch (escape) {
                                        case CsvOption.UNICODE:
                                            escapeValue *= (char) 16;
                                            escapeValue += hexToDec(currentLetter);
                                            if (escapeLength == 4) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                        case CsvOption.OCTAL:
                                            escapeValue *= (char) 8;
                                            escapeValue += (char) (currentLetter - '0');
                                            if (escapeLength == 3) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                        case CsvOption.DECIMAL:
                                            escapeValue *= (char) 10;
                                            escapeValue += (char) (currentLetter - '0');
                                            if (escapeLength == 3) {
                                                readingComplexEscape = false;
                                            }

                                            break;
                                        case CsvOption.HEX:
                                            escapeValue *= (char) 16;
                                            escapeValue += hexToDec(currentLetter);
                                            if (escapeLength == 2) {
                                                readingComplexEscape = false;
                                            }
                                            break;
                                    }
                                    if (!readingComplexEscape) {
                                        appendLetter(escapeValue);
                                    } else {
                                        dataBuffer.columnStart = dataBuffer.position + 1;
                                    }
                                } else if (option.getEscapeMode() == CsvOption.ESCAPE_MODE_BACKSLASH && lastLetterWasBackslash) {
                                    switch (currentLetter) {
                                        case 'n':
                                            appendLetter(Letters.LF);
                                            break;
                                        case 'r':
                                            appendLetter(Letters.CR);
                                            break;
                                        case 't':
                                            appendLetter(Letters.TAB);
                                            break;
                                        case 'b':
                                            appendLetter(Letters.BACKSPACE);
                                            break;
                                        case 'f':
                                            appendLetter(Letters.FORM_FEED);
                                            break;
                                        case 'e':
                                            appendLetter(Letters.ESCAPE);
                                            break;
                                        case 'v':
                                            appendLetter(Letters.VERTICAL_TAB);
                                            break;
                                        case 'a':
                                            appendLetter(Letters.ALERT);
                                            break;
                                        case '0':
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                            escape = CsvOption.OCTAL;
                                            readingComplexEscape = true;
                                            escapeLength = 1;
                                            escapeValue = (char) (currentLetter - '0');
                                            dataBuffer.columnStart = dataBuffer.position + 1;
                                            break;
                                        case 'u':
                                        case 'x':
                                        case 'o':
                                        case 'd':
                                        case 'U':
                                        case 'X':
                                        case 'O':
                                        case 'D':
                                            switch (currentLetter) {
                                                case 'u':
                                                case 'U':
                                                    escape = CsvOption.UNICODE;
                                                    break;
                                                case 'x':
                                                case 'X':
                                                    escape = CsvOption.HEX;
                                                    break;
                                                case 'o':
                                                case 'O':
                                                    escape = CsvOption.OCTAL;
                                                    break;
                                                case 'd':
                                                case 'D':
                                                    escape = CsvOption.DECIMAL;
                                                    break;
                                            }
                                            readingComplexEscape = true;
                                            escapeLength = 0;
                                            escapeValue = (char) 0;
                                            dataBuffer.columnStart = dataBuffer.position + 1;
                                            break;
                                        default:
                                            break;
                                    }
                                    lastLetterWasBackslash = false;
                                } else {
                                    if (currentLetter == option.getDelimiter()) {
                                        this.endColumn();
                                    } else if ((!option.isUseCustomLineDelimiter() && (currentLetter == Letters.CR || currentLetter == Letters.LF))
                                            || (option.isUseCustomLineDelimiter() && currentLetter == option.getLineDelimiter())) {
                                        this.endColumn();
                                        this.endRecord();
                                    }
                                }
                                lastLetter = currentLetter;
                                firstLoop = false;
                                if (startedColumn) {
                                    dataBuffer.position++;
                                    if (option.isSafetySwitch() && dataBuffer.position - dataBuffer.columnStart + columnBuffer.position > 100000) {
                                        this.close();
                                        throw new IOException("maximum column length of 100,000 exceeded in column "
                                                + NumberFormat.getIntegerInstance().format(currentColumnCount)
                                                + " in record "
                                                + NumberFormat.getIntegerInstance().format(currentRecord)
                                                + ". Set the SafetySwitch property to false"
                                                + " if you're expecting column lengths greater than 100,000 characters to"
                                                + " avoid this error.");
                                    }
                                }
                            }
                        } while (hasMoreData && startedColumn);
                    }
                    if (hasMoreData) {
                        dataBuffer.position++;
                    }
                }
            } while (hasMoreData && !hasReadNextLine);
            if (startedColumn || lastLetter == option.getDelimiter()) {
                this.endColumn();
                this.endRecord();
            }
        }
        if (!option.isSkipRawRow()) {
            if (hasMoreData) {
                if (rawBuffer.position == 0) {
                    rawRecord = new String(dataBuffer.buffer, dataBuffer.lineStart, dataBuffer.position - dataBuffer.lineStart - 1);
                } else {
                    rawRecord = new String(rawBuffer.buffer, 0, rawBuffer.position)
                            + new String(dataBuffer.buffer, dataBuffer.lineStart, dataBuffer.position - dataBuffer.lineStart - 1);
                }
            } else {
                rawRecord = new String(rawBuffer.buffer, 0, rawBuffer.position);
            }
        } else {
            rawRecord = Strings.EMPTY;
        }
        return hasReadNextLine;
    }

    /**
     * 跳过一行记录
     *
     * @return 是否成功
     * @throws IOException IOException
     */
    public boolean skipRecord() throws IOException {
        this.checkClosed();
        boolean recordRead = false;
        if (hasMoreData) {
            recordRead = this.readRow();
            if (recordRead) {
                currentRecord--;
            }
        }
        return recordRead;
    }

    /**
     * 清空读取的当前行
     *
     * @return 是否成功
     * @throws IOException IOException
     */
    public boolean clear() throws IOException {
        this.checkClosed();
        currentColumnCount = 0;
        boolean skippedLine = false;
        if (hasMoreData) {
            boolean foundEol = false;
            do {
                if (dataBuffer.position == dataBuffer.count) {
                    checkDataLength();
                } else {
                    skippedLine = true;
                    char currentLetter = dataBuffer.buffer[dataBuffer.position];
                    if (currentLetter == Letters.CR || currentLetter == Letters.LF) {
                        foundEol = true;
                    }
                    lastLetter = currentLetter;
                    if (!foundEol) {
                        dataBuffer.position++;
                    }
                }
            } while (hasMoreData && !foundEol);
            columnBuffer.position = 0;
            dataBuffer.lineStart = dataBuffer.position + 1;
        }
        rawBuffer.position = 0;
        rawRecord = Strings.EMPTY;
        return skippedLine;
    }

    private void checkDataLength() throws IOException {
        this.updateCurrentValue();
        if (!option.isSkipRawRow() && dataBuffer.count > 0) {
            if (rawBuffer.buffer.length - rawBuffer.position < dataBuffer.count - dataBuffer.lineStart) {
                int newLength = rawBuffer.buffer.length + Math.max(dataBuffer.count - dataBuffer.lineStart, rawBuffer.buffer.length);
                char[] holder = new char[newLength];
                System.arraycopy(rawBuffer.buffer, 0, holder, 0, rawBuffer.position);
                rawBuffer.buffer = holder;
            }
            System.arraycopy(dataBuffer.buffer, dataBuffer.lineStart, rawBuffer.buffer, rawBuffer.position, dataBuffer.count - dataBuffer.lineStart);
            rawBuffer.position += dataBuffer.count - dataBuffer.lineStart;
        }
        try {
            dataBuffer.count = reader.read(dataBuffer.buffer, 0, dataBuffer.buffer.length);
        } catch (IOException ex) {
            this.close();
            throw ex;
        }
        if (dataBuffer.count == -1) {
            hasMoreData = false;
        }
        dataBuffer.position = 0;
        dataBuffer.lineStart = 0;
        dataBuffer.columnStart = 0;
    }

    private void endColumn() {
        String currentValue = Strings.EMPTY;
        if (startedColumn) {
            if (columnBuffer.position == 0) {
                if (dataBuffer.columnStart < dataBuffer.position) {
                    int lastLetter = dataBuffer.position - 1;
                    if (option.isTrim() && !startedWithQualifier) {
                        while (lastLetter >= dataBuffer.columnStart
                                && (dataBuffer.buffer[lastLetter] == Letters.SPACE || dataBuffer.buffer[lastLetter] == Letters.TAB)) {
                            lastLetter--;
                        }
                    }
                    currentValue = new String(dataBuffer.buffer, dataBuffer.columnStart, lastLetter - dataBuffer.columnStart + 1);
                }
            } else {
                this.updateCurrentValue();
                int lastLetter = columnBuffer.position - 1;
                if (option.isTrim() && !startedWithQualifier) {
                    while (lastLetter >= 0
                            && (columnBuffer.buffer[lastLetter] == Letters.SPACE || columnBuffer.buffer[lastLetter] == Letters.SPACE)) {
                        lastLetter--;
                    }
                }
                currentValue = new String(columnBuffer.buffer, 0, lastLetter + 1);
            }
        }
        columnBuffer.position = 0;
        startedColumn = false;
        if (currentColumnCount >= 100000 && option.isSafetySwitch()) {
            this.close();
            throw new RuntimeException("maximum column count of 100,000 exceeded in record "
                    + NumberFormat.getIntegerInstance().format(currentRecord)
                    + ". Set the SafetySwitch property to false"
                    + " if you're expecting more than 100,000 columns per record to"
                    + " avoid this error.");
        }
        if (currentColumnCount == values.length) {
            int newLength = values.length * 2;
            String[] holder = new String[newLength];
            System.arraycopy(values, 0, holder, 0, values.length);
            values = holder;
            boolean[] qualifiedHolder = new boolean[newLength];
            System.arraycopy(isQualified, 0, qualifiedHolder, 0, isQualified.length);
            isQualified = qualifiedHolder;
        }
        values[currentColumnCount] = currentValue;
        isQualified[currentColumnCount] = startedWithQualifier;
        currentValue = Strings.EMPTY;
        currentColumnCount++;
    }

    private void appendLetter(char letter) {
        if (columnBuffer.position == columnBuffer.buffer.length) {
            int newLength = columnBuffer.buffer.length * 2;
            char[] holder = new char[newLength];
            System.arraycopy(columnBuffer.buffer, 0, holder, 0, columnBuffer.position);
            columnBuffer.buffer = holder;
        }
        columnBuffer.buffer[columnBuffer.position++] = letter;
        dataBuffer.columnStart = dataBuffer.position + 1;
    }

    private void updateCurrentValue() {
        if (startedColumn && dataBuffer.columnStart < dataBuffer.position) {
            if (columnBuffer.buffer.length - columnBuffer.position < dataBuffer.position - dataBuffer.columnStart) {
                int newLength = columnBuffer.buffer.length + Math.max(dataBuffer.position - dataBuffer.columnStart, columnBuffer.buffer.length);
                char[] holder = new char[newLength];
                System.arraycopy(columnBuffer.buffer, 0, holder, 0, columnBuffer.position);
                columnBuffer.buffer = holder;
            }
            System.arraycopy(dataBuffer.buffer, dataBuffer.columnStart, columnBuffer.buffer, columnBuffer.position, dataBuffer.position - dataBuffer.columnStart);
            columnBuffer.position += dataBuffer.position - dataBuffer.columnStart;
        }
        dataBuffer.columnStart = dataBuffer.position + 1;
    }

    private void endRecord() {
        hasReadNextLine = true;
        currentRecord++;
    }

    /**
     * 检查是否已关闭
     */
    private void checkClosed() {
        if (closed) {
            throw new IORuntimeException("this instance already been closed");
        }
    }

    @Override
    public void close() {
        if (!closed) {
            headersHolder.headers = null;
            headersHolder.indexByName = null;
            dataBuffer.buffer = null;
            columnBuffer.buffer = null;
            rawBuffer.buffer = null;
            Streams.close(reader);
            reader = null;
            closed = true;
        }
    }

    public CsvReaderOption getOption() {
        return option;
    }

    public CsvReader setOption(CsvReaderOption option) {
        this.option = option;
        return this;
    }

    /**
     * 数据是否合法
     *
     * @param columnIndex index
     * @return qualified
     */
    public boolean isQualified(int columnIndex) {
        if (columnIndex < currentColumnCount && columnIndex > -1) {
            return isQualified[columnIndex];
        } else {
            return false;
        }
    }

    /**
     * 获取列数
     *
     * @return 列数
     */
    public int getCurrentColumnCount() {
        return currentColumnCount;
    }

    /**
     * 获取当前记录索引
     *
     * @return 当前记录索引
     */
    public long getCurrentIndex() {
        return currentRecord - 1;
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    public String[] getHeaders() {
        this.checkClosed();
        if (headersHolder.headers == null) {
            return null;
        } else {
            String[] clone = new String[headersHolder.length];
            System.arraycopy(headersHolder.headers, 0, clone, 0, headersHolder.length);
            return clone;
        }
    }

    /**
     * 获取列的标题
     *
     * @param columnIndex 列
     * @return 标题
     */
    public String getHeader(int columnIndex) {
        if (columnIndex > -1 && columnIndex < headersHolder.length) {
            return headersHolder.headers[columnIndex];
        } else {
            return Strings.EMPTY;
        }
    }

    /**
     * 获取标题列的索引
     *
     * @param headerName 标题
     * @return index 没找到返回-1
     */
    public int getHeaderIndex(String headerName) {
        this.checkClosed();
        Integer indexValue = headersHolder.indexByName.get(headerName);
        if (indexValue != null) {
            return indexValue;
        } else {
            return -1;
        }
    }

    /**
     * 获取标题数量
     *
     * @return 数量
     */
    public int getHeaderCount() {
        return headersHolder.length;
    }

    private static class DataBuffer {

        public char[] buffer;

        public int position;

        public int count;

        public int columnStart;

        public int lineStart;

        public DataBuffer() {
            buffer = new char[Const.BUFFER_KB_1];
            position = 0;
            count = 0;
            columnStart = 0;
            lineStart = 0;
        }

    }

    private static class ColumnBuffer {

        public char[] buffer;

        public int position;

        public ColumnBuffer() {
            buffer = new char[CsvOption.INITIAL_COLUMN_BUFFER_SIZE];
            position = 0;
        }
    }

    private static class RawRecordBuffer {

        public char[] buffer;

        public int position;

        public RawRecordBuffer() {
            buffer = new char[CsvOption.INITIAL_COLUMN_BUFFER_SIZE * CsvOption.INITIAL_COLUMN_COUNT];
            position = 0;
        }
    }

    private static class HeadersHolder {

        public String[] headers;

        public int length;

        public Map<String, Integer> indexByName;

        public HeadersHolder() {
            headers = null;
            length = 0;
            indexByName = new HashMap<>();
        }
    }

    /**
     * 16进制 -> 10进制
     *
     * @param hex 16进制
     * @return 10进制
     */
    private static char hexToDec(char hex) {
        char result;
        if (hex >= 'a') {
            result = (char) (hex - 'a' + 10);
        } else if (hex >= 'A') {
            result = (char) (hex - 'A' + 10);
        } else {
            result = (char) (hex - '0');
        }
        return result;
    }

}
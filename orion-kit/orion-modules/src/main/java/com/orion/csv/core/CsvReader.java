package com.orion.csv.core;

import java.io.*;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.HashMap;

/**
 * CSV 读取类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/1 23:21
 */
@SuppressWarnings(value = {"unchecked", "all"})
public class CsvReader {

    private Reader inputStream;
    private String fileName;
    private CsvReader.UserSettings userSettings;
    private Charset charset;
    private boolean useCustomRecordDelimiter;
    private CsvReader.DataBuffer dataBuffer;
    private CsvReader.ColumnBuffer columnBuffer;
    private CsvReader.RawRecordBuffer rawBuffer;
    private boolean[] isQualified;
    private String rawRecord;
    private CsvReader.HeadersHolder headersHolder;
    private boolean startedColumn;
    private boolean startedWithQualifier;
    private boolean hasMoreData;
    private char lastLetter;
    private boolean hasReadNextLine;
    private int columnsCount;
    private long currentRecord;
    private String[] values;
    private boolean initialized;
    private boolean closed;

    public CsvReader(String fileName, char delimiter, Charset charset) throws FileNotFoundException {
        this.inputStream = null;
        this.fileName = null;
        this.userSettings = new CsvReader.UserSettings();
        this.charset = null;
        this.useCustomRecordDelimiter = false;
        this.dataBuffer = new CsvReader.DataBuffer();
        this.columnBuffer = new CsvReader.ColumnBuffer();
        this.rawBuffer = new CsvReader.RawRecordBuffer();
        this.isQualified = null;
        this.rawRecord = "";
        this.headersHolder = new CsvReader.HeadersHolder();
        this.startedColumn = false;
        this.startedWithQualifier = false;
        this.hasMoreData = true;
        this.lastLetter = 0;
        this.hasReadNextLine = false;
        this.columnsCount = 0;
        this.currentRecord = 0L;
        this.values = new String[10];
        this.initialized = false;
        this.closed = false;
        if (fileName == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else if (!(new File(fileName)).exists()) {
            throw new FileNotFoundException("File " + fileName + " does not exist.");
        } else {
            this.fileName = fileName;
            this.userSettings.delimiter = delimiter;
            this.charset = charset;
            this.isQualified = new boolean[this.values.length];
        }
    }

    public CsvReader(String fileName, char delimiter) throws FileNotFoundException {
        this(fileName, delimiter, Charset.forName("ISO-8859-1"));
    }

    public CsvReader(String fileName) throws FileNotFoundException {
        this(fileName, ',', Charset.forName("ISO-8859-1"));
    }

    public CsvReader(Reader reader, char delimiter) {
        this.inputStream = null;
        this.fileName = null;
        this.userSettings = new CsvReader.UserSettings();
        this.charset = null;
        this.useCustomRecordDelimiter = false;
        this.dataBuffer = new CsvReader.DataBuffer();
        this.columnBuffer = new CsvReader.ColumnBuffer();
        this.rawBuffer = new CsvReader.RawRecordBuffer();
        this.isQualified = null;
        this.rawRecord = "";
        this.headersHolder = new CsvReader.HeadersHolder();
        this.startedColumn = false;
        this.startedWithQualifier = false;
        this.hasMoreData = true;
        this.lastLetter = 0;
        this.hasReadNextLine = false;
        this.columnsCount = 0;
        this.currentRecord = 0L;
        this.values = new String[10];
        this.initialized = false;
        this.closed = false;
        if (reader == null) {
            throw new IllegalArgumentException("Parameter inputStream can not be null.");
        } else {
            this.inputStream = reader;
            this.userSettings.delimiter = delimiter;
            this.initialized = true;
            this.isQualified = new boolean[this.values.length];
        }
    }

    public CsvReader(Reader reader) {
        this(reader, ',');
    }

    public CsvReader(InputStream in, char delimiter, Charset charset) {
        this((Reader) (new InputStreamReader(in, charset)), delimiter);
    }

    public CsvReader(InputStream in, Charset charset) {
        this((Reader) (new InputStreamReader(in, charset)));
    }

    public boolean getCaptureRawRecord() {
        return this.userSettings.captureRawRecord;
    }

    public void setCaptureRawRecord(boolean b) {
        this.userSettings.captureRawRecord = b;
    }

    public String getRawRecord() {
        return this.rawRecord;
    }

    public boolean getTrimWhitespace() {
        return this.userSettings.trimWhitespace;
    }

    public void setTrimWhitespace(boolean b) {
        this.userSettings.trimWhitespace = b;
    }

    public char getDelimiter() {
        return this.userSettings.delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.userSettings.delimiter = delimiter;
    }

    public char getRecordDelimiter() {
        return this.userSettings.recordDelimiter;
    }

    public void setRecordDelimiter(char recordDelimiter) {
        this.useCustomRecordDelimiter = true;
        this.userSettings.recordDelimiter = recordDelimiter;
    }

    public char getTextQualifier() {
        return this.userSettings.textQualifier;
    }

    public void setTextQualifier(char textQualifier) {
        this.userSettings.textQualifier = textQualifier;
    }

    public boolean getUseTextQualifier() {
        return this.userSettings.useTextQualifier;
    }

    public void setUseTextQualifier(boolean textQualifier) {
        this.userSettings.useTextQualifier = textQualifier;
    }

    public char getComment() {
        return this.userSettings.comment;
    }

    public void setComment(char comment) {
        this.userSettings.comment = comment;
    }

    public boolean getUseComments() {
        return this.userSettings.useComments;
    }

    public void setUseComments(boolean useComments) {
        this.userSettings.useComments = useComments;
    }

    public int getEscapeMode() {
        return this.userSettings.escapeMode;
    }

    public void setEscapeMode(int escapeMode) throws IllegalArgumentException {
        if (escapeMode != 1 && escapeMode != 2) {
            throw new IllegalArgumentException("Parameter escapeMode must be a valid value.");
        } else {
            this.userSettings.escapeMode = escapeMode;
        }
    }

    public boolean getSkipEmptyRecords() {
        return this.userSettings.skipEmptyRecords;
    }

    public void setSkipEmptyRecords(boolean skipEmptyRecords) {
        this.userSettings.skipEmptyRecords = skipEmptyRecords;
    }

    public boolean getSafetySwitch() {
        return this.userSettings.safetySwitch;
    }

    public void setSafetySwitch(boolean safetySwitch) {
        this.userSettings.safetySwitch = safetySwitch;
    }

    public int getColumnCount() {
        return this.columnsCount;
    }

    public long getCurrentRecord() {
        return this.currentRecord - 1L;
    }

    public int getHeaderCount() {
        return this.headersHolder.length;
    }

    public String[] getHeaders() throws IOException {
        this.checkClosed();
        if (this.headersHolder.headers == null) {
            return null;
        } else {
            String[] var1 = new String[this.headersHolder.length];
            System.arraycopy(this.headersHolder.headers, 0, var1, 0, this.headersHolder.length);
            return var1;
        }
    }

    public void setHeaders(String[] headers) {
        this.headersHolder.headers = headers;
        this.headersHolder.indexByName.clear();
        if (headers != null) {
            this.headersHolder.length = headers.length;
        } else {
            this.headersHolder.length = 0;
        }
        for (int var2 = 0; var2 < this.headersHolder.length; ++var2) {
            this.headersHolder.indexByName.put(headers[var2], new Integer(var2));
        }
    }

    public String[] getValues() throws IOException {
        this.checkClosed();
        String[] var1 = new String[this.columnsCount];
        System.arraycopy(this.values, 0, var1, 0, this.columnsCount);
        return var1;
    }

    public String get(int i) throws IOException {
        this.checkClosed();
        return i > -1 && i < this.columnsCount ? this.values[i] : "";
    }

    public String get(String s) throws IOException {
        this.checkClosed();
        return this.get(this.getIndex(s));
    }

    public static CsvReader parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Parameter data can not be null.");
        } else {
            return new CsvReader(new StringReader(s));
        }
    }

    public boolean readRecord() throws IOException {
        this.checkClosed();
        this.columnsCount = 0;
        this.rawBuffer.position = 0;
        this.dataBuffer.lineStart = this.dataBuffer.position;
        this.hasReadNextLine = false;
        if (this.hasMoreData) {
            while (true) {
                if (this.dataBuffer.position == this.dataBuffer.count) {
                    this.checkDataLength();
                } else {
                    this.startedWithQualifier = false;
                    char var1 = this.dataBuffer.buffer[this.dataBuffer.position];
                    boolean var2;
                    if (this.userSettings.useTextQualifier && var1 == this.userSettings.textQualifier) {
                        this.lastLetter = var1;
                        this.startedColumn = true;
                        this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                        this.startedWithQualifier = true;
                        var2 = false;
                        char var10 = this.userSettings.textQualifier;
                        if (this.userSettings.escapeMode == 2) {
                            var10 = '\\';
                        }
                        boolean var11 = false;
                        boolean var12 = false;
                        boolean var14 = false;
                        byte var13 = 1;
                        int var8 = 0;
                        char var9 = 0;
                        ++this.dataBuffer.position;
                        do {
                            if (this.dataBuffer.position == this.dataBuffer.count) {
                                this.checkDataLength();
                            } else {
                                var1 = this.dataBuffer.buffer[this.dataBuffer.position];
                                if (var11) {
                                    this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                    if (var1 == this.userSettings.delimiter) {
                                        this.endColumn();
                                    } else if (!this.useCustomRecordDelimiter && (var1 == '\r' || var1 == '\n') || this.useCustomRecordDelimiter && var1 == this.userSettings.recordDelimiter) {
                                        this.endColumn();
                                        this.endRecord();
                                    }
                                } else if (var14) {
                                    ++var8;
                                    switch (var13) {
                                        case 1:
                                            var9 = (char) (var9 * 16);
                                            var9 += hexToDec(var1);
                                            if (var8 == 4) {
                                                var14 = false;
                                            }
                                            break;
                                        case 2:
                                            var9 = (char) (var9 * 8);
                                            var9 += (char) (var1 - 48);
                                            if (var8 == 3) {
                                                var14 = false;
                                            }
                                            break;
                                        case 3:
                                            var9 = (char) (var9 * 10);
                                            var9 += (char) (var1 - 48);
                                            if (var8 == 3) {
                                                var14 = false;
                                            }
                                            break;
                                        case 4:
                                            var9 = (char) (var9 * 16);
                                            var9 += hexToDec(var1);
                                            if (var8 == 2) {
                                                var14 = false;
                                            }
                                    }
                                    if (!var14) {
                                        this.appendLetter(var9);
                                    } else {
                                        this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                    }
                                } else if (var1 == this.userSettings.textQualifier) {
                                    if (var12) {
                                        var12 = false;
                                        var2 = false;
                                    } else {
                                        this.updateCurrentValue();
                                        if (this.userSettings.escapeMode == 1) {
                                            var12 = true;
                                        }
                                        var2 = true;
                                    }
                                } else if (this.userSettings.escapeMode == 2 && var12) {
                                    switch (var1) {
                                        case '0':
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                            var13 = 2;
                                            var14 = true;
                                            var8 = 1;
                                            var9 = (char) (var1 - 48);
                                            this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                        case '8':
                                        case '9':
                                        case ':':
                                        case ';':
                                        case '<':
                                        case '=':
                                        case '>':
                                        case '?':
                                        case '@':
                                        case 'A':
                                        case 'B':
                                        case 'C':
                                        case 'E':
                                        case 'F':
                                        case 'G':
                                        case 'H':
                                        case 'I':
                                        case 'J':
                                        case 'K':
                                        case 'L':
                                        case 'M':
                                        case 'N':
                                        case 'P':
                                        case 'Q':
                                        case 'R':
                                        case 'S':
                                        case 'T':
                                        case 'V':
                                        case 'W':
                                        case 'Y':
                                        case 'Z':
                                        case '[':
                                        case '\\':
                                        case ']':
                                        case '^':
                                        case '_':
                                        case '`':
                                        case 'c':
                                        case 'g':
                                        case 'h':
                                        case 'i':
                                        case 'j':
                                        case 'k':
                                        case 'l':
                                        case 'm':
                                        case 'p':
                                        case 'q':
                                        case 's':
                                        case 'w':
                                        default:
                                            break;
                                        case 'D':
                                        case 'O':
                                        case 'U':
                                        case 'X':
                                        case 'd':
                                        case 'o':
                                        case 'u':
                                        case 'x':
                                            switch (var1) {
                                                case 'D':
                                                case 'd':
                                                    var13 = 3;
                                                    break;
                                                case 'O':
                                                case 'o':
                                                    var13 = 2;
                                                    break;
                                                case 'U':
                                                case 'u':
                                                    var13 = 1;
                                                    break;
                                                case 'X':
                                                case 'x':
                                                    var13 = 4;
                                            }

                                            var14 = true;
                                            var8 = 0;
                                            var9 = 0;
                                            this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                            break;
                                        case 'a':
                                            this.appendLetter('\u0007');
                                            break;
                                        case 'b':
                                            this.appendLetter('\b');
                                            break;
                                        case 'e':
                                            this.appendLetter('\u001b');
                                            break;
                                        case 'f':
                                            this.appendLetter('\f');
                                            break;
                                        case 'n':
                                            this.appendLetter('\n');
                                            break;
                                        case 'r':
                                            this.appendLetter('\r');
                                            break;
                                        case 't':
                                            this.appendLetter('\t');
                                            break;
                                        case 'v':
                                            this.appendLetter('\u000b');
                                    }
                                    var12 = false;
                                } else if (var1 == var10) {
                                    this.updateCurrentValue();
                                    var12 = true;
                                } else if (var2) {
                                    if (var1 == this.userSettings.delimiter) {
                                        this.endColumn();
                                    } else if ((this.useCustomRecordDelimiter || var1 != '\r' && var1 != '\n') && (!this.useCustomRecordDelimiter || var1 != this.userSettings.recordDelimiter)) {
                                        this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                        var11 = true;
                                    } else {
                                        this.endColumn();
                                        this.endRecord();
                                    }
                                    var2 = false;
                                }

                                this.lastLetter = var1;
                                if (this.startedColumn) {
                                    ++this.dataBuffer.position;
                                    if (this.userSettings.safetySwitch && this.dataBuffer.position - this.dataBuffer.columnStart + this.columnBuffer.position > 100000) {
                                        this.close();
                                        throw new IOException("Maximum column length of 100,000 exceeded in column " + NumberFormat.getIntegerInstance().format((long) this.columnsCount) + " in record " + NumberFormat.getIntegerInstance().format(this.currentRecord) + ". Set the safetySwitch property to false" + " if you're expecting column lengths greater than 100,000 characters to" + " avoid this error.");
                                    }
                                }
                            }
                        } while (this.hasMoreData && this.startedColumn);
                    } else if (var1 == this.userSettings.delimiter) {
                        this.lastLetter = var1;
                        this.endColumn();
                    } else if (this.useCustomRecordDelimiter && var1 == this.userSettings.recordDelimiter) {
                        if (!this.startedColumn && this.columnsCount <= 0 && this.userSettings.skipEmptyRecords) {
                            this.dataBuffer.lineStart = this.dataBuffer.position + 1;
                        } else {
                            this.endColumn();
                            this.endRecord();
                        }
                        this.lastLetter = var1;
                    } else if (this.useCustomRecordDelimiter || var1 != '\r' && var1 != '\n') {
                        if (this.userSettings.useComments && this.columnsCount == 0 && var1 == this.userSettings.comment) {
                            this.lastLetter = var1;
                            this.skipLine();
                        } else if (this.userSettings.trimWhitespace && (var1 == ' ' || var1 == '\t')) {
                            this.startedColumn = true;
                            this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                        } else {
                            this.startedColumn = true;
                            this.dataBuffer.columnStart = this.dataBuffer.position;
                            var2 = false;
                            boolean var3 = false;
                            byte var4 = 1;
                            int var5 = 0;
                            char var6 = 0;
                            boolean var7 = true;
                            do {
                                if (!var7 && this.dataBuffer.position == this.dataBuffer.count) {
                                    this.checkDataLength();
                                } else {
                                    if (!var7) {
                                        var1 = this.dataBuffer.buffer[this.dataBuffer.position];
                                    }

                                    if (!this.userSettings.useTextQualifier && this.userSettings.escapeMode == 2 && var1 == '\\') {
                                        if (var2) {
                                            var2 = false;
                                        } else {
                                            this.updateCurrentValue();
                                            var2 = true;
                                        }
                                    } else if (var3) {
                                        ++var5;
                                        switch (var4) {
                                            case 1:
                                                var6 = (char) (var6 * 16);
                                                var6 += hexToDec(var1);
                                                if (var5 == 4) {
                                                    var3 = false;
                                                }
                                                break;
                                            case 2:
                                                var6 = (char) (var6 * 8);
                                                var6 += (char) (var1 - 48);
                                                if (var5 == 3) {
                                                    var3 = false;
                                                }
                                                break;
                                            case 3:
                                                var6 = (char) (var6 * 10);
                                                var6 += (char) (var1 - 48);
                                                if (var5 == 3) {
                                                    var3 = false;
                                                }
                                                break;
                                            case 4:
                                                var6 = (char) (var6 * 16);
                                                var6 += hexToDec(var1);
                                                if (var5 == 2) {
                                                    var3 = false;
                                                }
                                        }
                                        if (!var3) {
                                            this.appendLetter(var6);
                                        } else {
                                            this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                        }
                                    } else if (this.userSettings.escapeMode == 2 && var2) {
                                        switch (var1) {
                                            case '0':
                                            case '1':
                                            case '2':
                                            case '3':
                                            case '4':
                                            case '5':
                                            case '6':
                                            case '7':
                                                var4 = 2;
                                                var3 = true;
                                                var5 = 1;
                                                var6 = (char) (var1 - 48);
                                                this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                            case '8':
                                            case '9':
                                            case ':':
                                            case ';':
                                            case '<':
                                            case '=':
                                            case '>':
                                            case '?':
                                            case '@':
                                            case 'A':
                                            case 'B':
                                            case 'C':
                                            case 'E':
                                            case 'F':
                                            case 'G':
                                            case 'H':
                                            case 'I':
                                            case 'J':
                                            case 'K':
                                            case 'L':
                                            case 'M':
                                            case 'N':
                                            case 'P':
                                            case 'Q':
                                            case 'R':
                                            case 'S':
                                            case 'T':
                                            case 'V':
                                            case 'W':
                                            case 'Y':
                                            case 'Z':
                                            case '[':
                                            case '\\':
                                            case ']':
                                            case '^':
                                            case '_':
                                            case '`':
                                            case 'c':
                                            case 'g':
                                            case 'h':
                                            case 'i':
                                            case 'j':
                                            case 'k':
                                            case 'l':
                                            case 'm':
                                            case 'p':
                                            case 'q':
                                            case 's':
                                            case 'w':
                                            default:
                                                break;
                                            case 'D':
                                            case 'O':
                                            case 'U':
                                            case 'X':
                                            case 'd':
                                            case 'o':
                                            case 'u':
                                            case 'x':
                                                switch (var1) {
                                                    case 'D':
                                                    case 'd':
                                                        var4 = 3;
                                                        break;
                                                    case 'O':
                                                    case 'o':
                                                        var4 = 2;
                                                        break;
                                                    case 'U':
                                                    case 'u':
                                                        var4 = 1;
                                                        break;
                                                    case 'X':
                                                    case 'x':
                                                        var4 = 4;
                                                }

                                                var3 = true;
                                                var5 = 0;
                                                var6 = 0;
                                                this.dataBuffer.columnStart = this.dataBuffer.position + 1;
                                                break;
                                            case 'a':
                                                this.appendLetter('\u0007');
                                                break;
                                            case 'b':
                                                this.appendLetter('\b');
                                                break;
                                            case 'e':
                                                this.appendLetter('\u001b');
                                                break;
                                            case 'f':
                                                this.appendLetter('\f');
                                                break;
                                            case 'n':
                                                this.appendLetter('\n');
                                                break;
                                            case 'r':
                                                this.appendLetter('\r');
                                                break;
                                            case 't':
                                                this.appendLetter('\t');
                                                break;
                                            case 'v':
                                                this.appendLetter('\u000b');
                                        }
                                        var2 = false;
                                    } else if (var1 == this.userSettings.delimiter) {
                                        this.endColumn();
                                    } else if (!this.useCustomRecordDelimiter && (var1 == '\r' || var1 == '\n') || this.useCustomRecordDelimiter && var1 == this.userSettings.recordDelimiter) {
                                        this.endColumn();
                                        this.endRecord();
                                    }
                                    this.lastLetter = var1;
                                    var7 = false;
                                    if (this.startedColumn) {
                                        ++this.dataBuffer.position;
                                        if (this.userSettings.safetySwitch && this.dataBuffer.position - this.dataBuffer.columnStart + this.columnBuffer.position > 100000) {
                                            this.close();
                                            throw new IOException("Maximum column length of 100,000 exceeded in column " + NumberFormat.getIntegerInstance().format((long) this.columnsCount) + " in record " + NumberFormat.getIntegerInstance().format(this.currentRecord) + ". Set the safetySwitch property to false" + " if you're expecting column lengths greater than 100,000 characters to" + " avoid this error.");
                                        }
                                    }
                                }
                            } while (this.hasMoreData && this.startedColumn);
                        }
                    } else {
                        if (!this.startedColumn && this.columnsCount <= 0 && (this.userSettings.skipEmptyRecords || var1 != '\r' && this.lastLetter == '\r')) {
                            this.dataBuffer.lineStart = this.dataBuffer.position + 1;
                        } else {
                            this.endColumn();
                            this.endRecord();
                        }
                        this.lastLetter = var1;
                    }
                    if (this.hasMoreData) {
                        ++this.dataBuffer.position;
                    }
                }
                if (!this.hasMoreData || this.hasReadNextLine) {
                    if (this.startedColumn || this.lastLetter == this.userSettings.delimiter) {
                        this.endColumn();
                        this.endRecord();
                    }
                    break;
                }
            }
        }
        if (this.userSettings.captureRawRecord) {
            if (this.hasMoreData) {
                if (this.rawBuffer.position == 0) {
                    this.rawRecord = new String(this.dataBuffer.buffer, this.dataBuffer.lineStart, this.dataBuffer.position - this.dataBuffer.lineStart - 1);
                } else {
                    this.rawRecord = new String(this.rawBuffer.buffer, 0, this.rawBuffer.position) + new String(this.dataBuffer.buffer, this.dataBuffer.lineStart, this.dataBuffer.position - this.dataBuffer.lineStart - 1);
                }
            } else {
                this.rawRecord = new String(this.rawBuffer.buffer, 0, this.rawBuffer.position);
            }
        } else {
            this.rawRecord = "";
        }
        return this.hasReadNextLine;
    }

    private void checkDataLength() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), this.charset), 4096);
            }
            this.charset = null;
            this.initialized = true;
        }
        this.updateCurrentValue();
        if (this.userSettings.captureRawRecord && this.dataBuffer.count > 0) {
            if (this.rawBuffer.buffer.length - this.rawBuffer.position < this.dataBuffer.count - this.dataBuffer.lineStart) {
                int var1 = this.rawBuffer.buffer.length + Math.max(this.dataBuffer.count - this.dataBuffer.lineStart, this.rawBuffer.buffer.length);
                char[] var2 = new char[var1];
                System.arraycopy(this.rawBuffer.buffer, 0, var2, 0, this.rawBuffer.position);
                this.rawBuffer.buffer = var2;
            }
            System.arraycopy(this.dataBuffer.buffer, this.dataBuffer.lineStart, this.rawBuffer.buffer, this.rawBuffer.position, this.dataBuffer.count - this.dataBuffer.lineStart);
            this.rawBuffer.position += this.dataBuffer.count - this.dataBuffer.lineStart;
        }
        try {
            this.dataBuffer.count = this.inputStream.read(this.dataBuffer.buffer, 0, this.dataBuffer.buffer.length);
        } catch (IOException var3) {
            this.close();
            throw var3;
        }
        if (this.dataBuffer.count == -1) {
            this.hasMoreData = false;
        }
        this.dataBuffer.position = 0;
        this.dataBuffer.lineStart = 0;
        this.dataBuffer.columnStart = 0;
    }

    public boolean readHeaders() throws IOException {
        boolean var1 = this.readRecord();
        this.headersHolder.length = this.columnsCount;
        this.headersHolder.headers = new String[this.columnsCount];
        for (int var2 = 0; var2 < this.headersHolder.length; ++var2) {
            String var3 = this.get(var2);
            this.headersHolder.headers[var2] = var3;
            this.headersHolder.indexByName.put(var3, new Integer(var2));
        }
        if (var1) {
            --this.currentRecord;
        }
        this.columnsCount = 0;
        return var1;
    }

    public String getHeader(int var1) throws IOException {
        this.checkClosed();
        return var1 > -1 && var1 < this.headersHolder.length ? this.headersHolder.headers[var1] : "";
    }

    public boolean isQualified(int var1) throws IOException {
        this.checkClosed();
        return var1 < this.columnsCount && var1 > -1 ? this.isQualified[var1] : false;
    }

    private void endColumn() throws IOException {
        String var1 = "";
        int var2;
        if (this.startedColumn) {
            if (this.columnBuffer.position == 0) {
                if (this.dataBuffer.columnStart < this.dataBuffer.position) {
                    var2 = this.dataBuffer.position - 1;
                    if (this.userSettings.trimWhitespace && !this.startedWithQualifier) {
                        while (var2 >= this.dataBuffer.columnStart && (this.dataBuffer.buffer[var2] == ' ' || this.dataBuffer.buffer[var2] == '\t')) {
                            --var2;
                        }
                    }

                    var1 = new String(this.dataBuffer.buffer, this.dataBuffer.columnStart, var2 - this.dataBuffer.columnStart + 1);
                }
            } else {
                this.updateCurrentValue();
                var2 = this.columnBuffer.position - 1;
                if (this.userSettings.trimWhitespace && !this.startedWithQualifier) {
                    while (var2 >= 0 && (this.columnBuffer.buffer[var2] == ' ' || this.columnBuffer.buffer[var2] == ' ')) {
                        --var2;
                    }
                }

                var1 = new String(this.columnBuffer.buffer, 0, var2 + 1);
            }
        }

        this.columnBuffer.position = 0;
        this.startedColumn = false;
        if (this.columnsCount >= 100000 && this.userSettings.safetySwitch) {
            this.close();
            throw new IOException("Maximum column count of 100,000 exceeded in record " + NumberFormat.getIntegerInstance().format(this.currentRecord) + ". Set the safetySwitch property to false" + " if you're expecting more than 100,000 columns per record to" + " avoid this error.");
        } else {
            if (this.columnsCount == this.values.length) {
                var2 = this.values.length * 2;
                String[] var3 = new String[var2];
                System.arraycopy(this.values, 0, var3, 0, this.values.length);
                this.values = var3;
                boolean[] var4 = new boolean[var2];
                System.arraycopy(this.isQualified, 0, var4, 0, this.isQualified.length);
                this.isQualified = var4;
            }

            this.values[this.columnsCount] = var1;
            this.isQualified[this.columnsCount] = this.startedWithQualifier;
            var1 = "";
            ++this.columnsCount;
        }
    }

    private void appendLetter(char var1) {
        if (this.columnBuffer.position == this.columnBuffer.buffer.length) {
            int var2 = this.columnBuffer.buffer.length * 2;
            char[] var3 = new char[var2];
            System.arraycopy(this.columnBuffer.buffer, 0, var3, 0, this.columnBuffer.position);
            this.columnBuffer.buffer = var3;
        }

        this.columnBuffer.buffer[this.columnBuffer.position++] = var1;
        this.dataBuffer.columnStart = this.dataBuffer.position + 1;
    }

    private void updateCurrentValue() {
        if (this.startedColumn && this.dataBuffer.columnStart < this.dataBuffer.position) {
            if (this.columnBuffer.buffer.length - this.columnBuffer.position < this.dataBuffer.position - this.dataBuffer.columnStart) {
                int var1 = this.columnBuffer.buffer.length + Math.max(this.dataBuffer.position - this.dataBuffer.columnStart, this.columnBuffer.buffer.length);
                char[] var2 = new char[var1];
                System.arraycopy(this.columnBuffer.buffer, 0, var2, 0, this.columnBuffer.position);
                this.columnBuffer.buffer = var2;
            }

            System.arraycopy(this.dataBuffer.buffer, this.dataBuffer.columnStart, this.columnBuffer.buffer, this.columnBuffer.position, this.dataBuffer.position - this.dataBuffer.columnStart);
            this.columnBuffer.position += this.dataBuffer.position - this.dataBuffer.columnStart;
        }

        this.dataBuffer.columnStart = this.dataBuffer.position + 1;
    }

    private void endRecord() throws IOException {
        this.hasReadNextLine = true;
        ++this.currentRecord;
    }

    public int getIndex(String var1) throws IOException {
        this.checkClosed();
        Object var2 = this.headersHolder.indexByName.get(var1);
        return var2 != null ? (Integer) var2 : -1;
    }

    public boolean skipRecord() throws IOException {
        this.checkClosed();
        boolean var1 = false;
        if (this.hasMoreData) {
            var1 = this.readRecord();
            if (var1) {
                --this.currentRecord;
            }
        }
        return var1;
    }

    public boolean skipLine() throws IOException {
        this.checkClosed();
        this.columnsCount = 0;
        boolean var1 = false;
        if (this.hasMoreData) {
            boolean var2 = false;

            do {
                if (this.dataBuffer.position == this.dataBuffer.count) {
                    this.checkDataLength();
                } else {
                    var1 = true;
                    char var3 = this.dataBuffer.buffer[this.dataBuffer.position];
                    if (var3 == '\r' || var3 == '\n') {
                        var2 = true;
                    }

                    this.lastLetter = var3;
                    if (!var2) {
                        ++this.dataBuffer.position;
                    }
                }
            } while (this.hasMoreData && !var2);

            this.columnBuffer.position = 0;
            this.dataBuffer.lineStart = this.dataBuffer.position + 1;
        }

        this.rawBuffer.position = 0;
        this.rawRecord = "";
        return var1;
    }

    public void close() {
        if (!this.closed) {
            this.close(true);
            this.closed = true;
        }
    }

    private void close(boolean var1) {
        if (!this.closed) {
            if (var1) {
                this.charset = null;
                this.headersHolder.headers = null;
                this.headersHolder.indexByName = null;
                this.dataBuffer.buffer = null;
                this.columnBuffer.buffer = null;
                this.rawBuffer.buffer = null;
            }
            try {
                if (this.initialized) {
                    this.inputStream.close();
                }
            } catch (Exception var3) {
                // ignore
            }
            this.inputStream = null;
            this.closed = true;
        }
    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvReader class has already been closed.");
        }
    }

    @Override
    protected void finalize() {
        this.close(false);
    }

    private static char hexToDec(char var0) {
        char var1;
        if (var0 >= 'a') {
            var1 = (char) (var0 - 97 + 10);
        } else if (var0 >= 'A') {
            var1 = (char) (var0 - 65 + 10);
        } else {
            var1 = (char) (var0 - 48);
        }

        return var1;
    }

    private class StaticSettings {
        public static final int MAX_BUFFER_SIZE = 1024;
        public static final int MAX_FILE_BUFFER_SIZE = 4096;
        public static final int INITIAL_COLUMN_COUNT = 10;
        public static final int INITIAL_COLUMN_BUFFER_SIZE = 50;

        private StaticSettings() {
        }
    }

    private class HeadersHolder {
        public String[] headers = null;
        public int length = 0;
        public HashMap indexByName = new HashMap();

        public HeadersHolder() {
        }
    }

    private class UserSettings {
        public boolean caseSensitive = true;
        public char textQualifier = '"';
        public boolean trimWhitespace = true;
        public boolean useTextQualifier = true;
        public char delimiter = ',';
        public char recordDelimiter = 0;
        public char comment = '#';
        public boolean useComments = false;
        public int escapeMode = 1;
        public boolean safetySwitch = true;
        public boolean skipEmptyRecords = true;
        public boolean captureRawRecord = true;

        public UserSettings() {
        }
    }

    private class Letters {
        public static final char LF = '\n';
        public static final char CR = '\r';
        public static final char QUOTE = '"';
        public static final char COMMA = ',';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';
        public static final char POUND = '#';
        public static final char BACKSLASH = '\\';
        public static final char NULL = '\u0000';
        public static final char BACKSPACE = '\b';
        public static final char FORM_FEED = '\f';
        public static final char ESCAPE = '\u001b';
        public static final char VERTICAL_TAB = '\u000b';
        public static final char ALERT = '\u0007';

        private Letters() {
        }
    }

    private class RawRecordBuffer {
        public char[] buffer = new char[500];
        public int position = 0;

        public RawRecordBuffer() {
        }
    }

    private class ColumnBuffer {
        public char[] buffer = new char[50];
        public int position = 0;

        public ColumnBuffer() {
        }
    }

    private class DataBuffer {
        public char[] buffer = new char[1024];
        public int position = 0;
        public int count = 0;
        public int columnStart = 0;
        public int lineStart = 0;

        public DataBuffer() {
        }
    }

    private class ComplexEscape {
        private static final int UNICODE = 1;
        private static final int OCTAL = 2;
        private static final int DECIMAL = 3;
        private static final int HEX = 4;

        private ComplexEscape() {
        }
    }

}

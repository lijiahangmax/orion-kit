package com.orion.csv.core;

import com.orion.able.SafeCloseable;

import java.io.*;
import java.nio.charset.Charset;

/**
 * CSV 写入类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/1 23:23
 */
@SuppressWarnings("ALL")
public class CsvWriter implements SafeCloseable {

    private PrintWriter outputStream;
    private String fileName;
    private boolean firstColumn;
    private boolean useCustomRecordDelimiter;
    private Charset charset;
    private CsvWriter.UserSettings userSettings;
    private boolean initialized;
    private boolean closed;

    public CsvWriter(String fileName, char delimiter, Charset charset) {
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new CsvWriter.UserSettings();
        this.initialized = false;
        this.closed = false;
        if (fileName == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else {
            this.fileName = fileName;
            this.userSettings.delimiter = delimiter;
            this.charset = charset;
        }
    }

    public CsvWriter(String fileName) {
        this(fileName, ',', Charset.forName("ISO-8859-1"));
    }

    public CsvWriter(Writer writer, char delimiter) {
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new CsvWriter.UserSettings();
        this.initialized = false;
        this.closed = false;
        if (writer == null) {
            throw new IllegalArgumentException("Parameter outputStream can not be null.");
        } else {
            this.outputStream = new PrintWriter(writer);
            this.userSettings.delimiter = delimiter;
            this.initialized = true;
        }
    }

    public CsvWriter(OutputStream out, char delimiter, Charset charset) {
        this(new OutputStreamWriter(out, charset), delimiter);
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

    public void setUseTextQualifier(boolean useTextQualifier) {
        this.userSettings.useTextQualifier = useTextQualifier;
    }

    public int getEscapeMode() {
        return this.userSettings.escapeMode;
    }

    public void setEscapeMode(int escapeMode) {
        this.userSettings.escapeMode = escapeMode;
    }

    public void setComment(char comment) {
        this.userSettings.comment = comment;
    }

    public char getComment() {
        return this.userSettings.comment;
    }

    public boolean getForceQualifier() {
        return this.userSettings.forceQualifier;
    }

    public void setForceQualifier(boolean forceQualifier) {
        this.userSettings.forceQualifier = forceQualifier;
    }

    public void write(String s, boolean newLine) throws IOException {
        this.checkClosed();
        this.checkInit();
        if (s == null) {
            s = "";
        }
        if (!this.firstColumn) {
            this.outputStream.write(this.userSettings.delimiter);
        }
        boolean var3 = this.userSettings.forceQualifier;
        if (!newLine && s.length() > 0) {
            s = s.trim();
        }
        if (!var3 && this.userSettings.useTextQualifier && (s.indexOf(this.userSettings.textQualifier) > -1 ||
                s.indexOf(this.userSettings.delimiter) > -1 || !this.useCustomRecordDelimiter && (s.indexOf(10) > -1 ||
                s.indexOf(13) > -1) || this.useCustomRecordDelimiter && s.indexOf(this.userSettings.recordDelimiter) > -1 ||
                this.firstColumn && s.length() > 0 && s.charAt(0) == this.userSettings.comment || this.firstColumn && s.length() == 0)) {
            var3 = true;
        }
        if (this.userSettings.useTextQualifier && !var3 && s.length() > 0 && newLine) {
            char var4 = s.charAt(0);
            if (var4 == ' ' || var4 == '\t') {
                var3 = true;
            }
            if (!var3 && s.length() > 1) {
                char var5 = s.charAt(s.length() - 1);
                if (var5 == ' ' || var5 == '\t') {
                    var3 = true;
                }
            }
        }
        if (var3) {
            this.outputStream.write(this.userSettings.textQualifier);
            if (this.userSettings.escapeMode == 2) {
                s = replace(s, "\\", "\\\\");
                s = replace(s, "" + this.userSettings.textQualifier, "\\" + this.userSettings.textQualifier);
            } else {
                s = replace(s, "" + this.userSettings.textQualifier, "" + this.userSettings.textQualifier + this.userSettings.textQualifier);
            }
        } else if (this.userSettings.escapeMode == 2) {
            s = replace(s, "\\", "\\\\");
            s = replace(s, "" + this.userSettings.delimiter, "\\" + this.userSettings.delimiter);
            if (this.useCustomRecordDelimiter) {
                s = replace(s, "" + this.userSettings.recordDelimiter, "\\" + this.userSettings.recordDelimiter);
            } else {
                s = replace(s, "\r", "\\\r");
                s = replace(s, "\n", "\\\n");
            }
            if (this.firstColumn && s.length() > 0 && s.charAt(0) == this.userSettings.comment) {
                if (s.length() > 1) {
                    s = "\\" + this.userSettings.comment + s.substring(1);
                } else {
                    s = "\\" + this.userSettings.comment;
                }
            }
        }
        this.outputStream.write(s);
        if (var3) {
            this.outputStream.write(this.userSettings.textQualifier);
        }
        this.firstColumn = false;
    }

    public void write(String s) throws IOException {
        this.write(s, false);
    }

    public void writeComment(String s) throws IOException {
        this.checkClosed();
        this.checkInit();
        this.outputStream.write(this.userSettings.comment);
        this.outputStream.write(s);
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.recordDelimiter);
        } else {
            this.outputStream.println();
        }
        this.firstColumn = true;
    }

    public void writeRecord(String[] record, boolean newLine) throws IOException {
        if (record != null && record.length > 0) {
            for (int i = 0; i < record.length; ++i) {
                this.write(record[i], newLine);
            }
            this.endRecord();
        }
    }

    public void writeRecord(String[] record) throws IOException {
        this.writeRecord(record, false);
    }

    public void endRecord() throws IOException {
        this.checkClosed();
        this.checkInit();
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.recordDelimiter);
        } else {
            this.outputStream.println();
        }

        this.firstColumn = true;
    }

    private void checkInit() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.outputStream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.fileName), this.charset));
            }
            this.initialized = true;
        }

    }

    public void flush() {
        this.outputStream.flush();
    }

    @Override
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
            }
            try {
                if (this.initialized) {
                    this.outputStream.close();
                }
            } catch (Exception var3) {
                // ignore
            }
            this.outputStream = null;
            this.closed = true;
        }
    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvWriter class has already been closed.");
        }
    }

    public static String replace(String var0, String var1, String var2) {
        int var3 = var1.length();
        int var4 = var0.indexOf(var1);
        if (var4 <= -1) {
            return var0;
        } else {
            StringBuffer var5 = new StringBuffer();
            int var6;
            for (var6 = 0; var4 != -1; var4 = var0.indexOf(var1, var6)) {
                var5.append(var0.substring(var6, var4));
                var5.append(var2);
                var6 = var4 + var3;
            }
            var5.append(var0.substring(var6));
            return var5.toString();
        }
    }

    private static class UserSettings {
        public char textQualifier = '"';
        public boolean useTextQualifier = true;
        public char delimiter = ',';
        public char recordDelimiter = 0;
        public char comment = '#';
        public int escapeMode = 1;
        public boolean forceQualifier = false;

        private UserSettings() {
        }
    }

    private static class Letters {
        public static final char LF = '\n';
        public static final char CR = '\r';
        public static final char QUOTE = '"';
        public static final char COMMA = ',';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';
        public static final char POUND = '#';
        public static final char BACKSLASH = '\\';
        public static final char NULL = '\u0000';

        private Letters() {
        }
    }

}

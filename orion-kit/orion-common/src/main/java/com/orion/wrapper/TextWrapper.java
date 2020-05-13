package com.orion.wrapper;

import com.orion.utils.Xsses;

/**
 * restful 文本包装
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/2 18:00
 */
public class TextWrapper implements Wrapper<String> {

    private static final long serialVersionUID = 5693256198048111L;

    private StringBuilder sb;

    private TextWrapper() {
        this.sb = new StringBuilder();
    }

    private TextWrapper(String s) {
        this.sb = new StringBuilder(s);
    }

    private TextWrapper(StringBuilder sb) {
        if (sb == null) {
            this.sb = new StringBuilder();
        } else {
            this.sb = sb;
        }
    }

    public static TextWrapper get() {
        return new TextWrapper();
    }

    public static TextWrapper get(String s) {
        return new TextWrapper(s);
    }

    public static TextWrapper get(StringBuilder sb) {
        return new TextWrapper(sb);
    }

    public TextWrapper append(String s) {
        sb.append(s);
        return this;
    }

    public TextWrapper appendLine(String s) {
        sb.append(s).append("\n");
        return this;
    }

    public TextWrapper newLine() {
        sb.append("\n");
        return this;
    }

    public TextWrapper newLine(String eof) {
        sb.append(eof);
        return this;
    }

    public String encodeText() {
        return Xsses.clean(sb.toString());
    }

    public String decodeText() {
        return Xsses.recode(sb.toString());
    }

    public String text() {
        return sb.toString();
    }

}

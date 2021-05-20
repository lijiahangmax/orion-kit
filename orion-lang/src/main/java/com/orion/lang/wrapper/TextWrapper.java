package com.orion.lang.wrapper;

import com.orion.constant.Const;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.Urls;
import com.orion.utils.Xsses;
import com.orion.utils.codec.Base64s;

/**
 * restful 文本包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/2 18:00
 */
public class TextWrapper extends CloneSupport<TextWrapper> implements Wrapper<String> {

    private static final long serialVersionUID = 5693256198048111L;

    private StringBuilder sb;

    public TextWrapper() {
        this(new StringBuilder());
    }

    public TextWrapper(String s) {
        this(new StringBuilder(s));
    }

    public TextWrapper(StringBuilder sb) {
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
        sb.append(s).append(Const.LF);
        return this;
    }

    public TextWrapper newLine() {
        sb.append(Const.LF);
        return this;
    }

    public TextWrapper newLine(String eof) {
        sb.append(eof);
        return this;
    }

    public String encodeXss() {
        return Xsses.clean(sb.toString());
    }

    public String decodeXss() {
        return Xsses.recode(sb.toString());
    }

    public String encodeUrl() {
        return Urls.encode(sb.toString());
    }

    public String decodeUrl() {
        return Urls.decode(sb.toString());
    }

    public String encodeBase64() {
        return Base64s.encode(sb.toString());
    }

    public String decodeBase64() {
        return Base64s.decode(sb.toString());
    }

    public String text() {
        return sb.toString();
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}

package com.orion.lang.utils.ansi.style.color;

import com.orion.lang.utils.Valid;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI bit 颜色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 16:40
 */
public abstract class AnsiBitColor implements AnsiColor {

    private final String code;

    public AnsiBitColor(String code) {
        this.code = code;
    }

    /**
     * 生成颜色码
     *
     * @param elem elem
     * @return color
     */
    protected static String color(byte type, byte bit, int... elem) {
        StringBuilder sb = new StringBuilder()
                .append(type)
                .append(JOIN)
                .append(bit)
                .append(JOIN);
        for (int i = 0, len = elem.length; i < len; i++) {
            int color = elem[i];
            Valid.isTrue(color >= 0 && color <= 255, "color must be between 0 and 255");
            sb.append(color);
            if (i < len - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code + SGR_SUFFIX;
    }

}

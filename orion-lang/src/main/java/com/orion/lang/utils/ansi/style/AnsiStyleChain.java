package com.orion.lang.utils.ansi.style;

import com.orion.lang.utils.collect.Lists;

import java.util.List;

import static com.orion.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 样式链
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/28 17:00
 */
public class AnsiStyleChain implements AnsiStyle {

    /**
     * 样式
     */
    private final List<AnsiStyle> styles;

    public AnsiStyleChain(AnsiStyle originStyle) {
        this.styles = Lists.of(originStyle);
    }

    /**
     * 创建样式链
     *
     * @param originStyle originStyle
     * @return 样式链
     */
    public static AnsiStyleChain create(AnsiStyle originStyle) {
        return new AnsiStyleChain(originStyle);
    }

    @Override
    public AnsiStyleChain and(AnsiStyle style) {
        styles.add(style);
        return this;
    }

    @Override
    public String getCode() {
        return Lists.join(Lists.map(styles, AnsiStyle::getCode), JOIN);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + this.getCode() + SGR_SUFFIX;
    }

}

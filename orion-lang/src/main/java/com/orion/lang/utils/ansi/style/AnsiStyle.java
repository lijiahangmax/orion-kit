package com.orion.lang.utils.ansi.style;

import com.orion.lang.utils.ansi.AnsiElement;

/**
 * ANSI 样式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/28 15:15
 */
public interface AnsiStyle extends AnsiElement {

    /**
     * 添加样式
     *
     * @param next next
     * @return style
     */
    default AnsiStyle and(AnsiStyle next) {
        if (this instanceof AnsiStyleChain) {
            return this.and(next);
        } else {
            return AnsiStyleChain.create(this).and(next);
        }
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.ext;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 表情工具类 https://github.com/vdurmont/emoji-java/blob/master/EMOJIS.md
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/10/30 15:59
 */
public class EmojiExt {

    /**
     * 判断是否是表情的 unicode 符
     *
     * @param str s
     * @return true 是
     */
    public static boolean isEmoji(String str) {
        return EmojiManager.isEmoji(str);
    }

    /**
     * 通过标签获取表情
     *
     * @param tag 标签 dog...
     * @return 表情
     */
    public static Set<Emoji> getByTag(String tag) {
        return EmojiManager.getForTag(tag);
    }

    /**
     * 获取所有表情标签
     *
     * @return 表情标签
     */
    public static Collection<String> listTags() {
        return EmojiManager.getAllTags();
    }

    /**
     * 通过 unicode 获取表情
     *
     * @param unicode 字符
     * @return 表情
     */
    public static Emoji getByUnicode(String unicode) {
        return EmojiManager.getByUnicode(unicode);
    }

    /**
     * 通过别名获取 emoji
     *
     * @param alias 别名 dog...
     * @return 表情, 如果找不到返回null
     */
    public static Emoji get(String alias) {
        return EmojiManager.getForAlias(alias);
    }

    /**
     * 获取所有表情
     *
     * @return 表情集合
     */
    public static Collection<Emoji> list() {
        return EmojiManager.getAll();
    }

    /**
     * 判断是否包含表情
     *
     * @param str 字符串
     * @return true 包含
     */
    public static boolean someEmoji(String str) {
        return EmojiManager.containsEmoji(str);
    }

    /**
     * 判断是否只有表情
     *
     * @param str 字符串
     * @return true 只有字符串
     */
    public static boolean justEmoji(String str) {
        return EmojiManager.isOnlyEmojis(str);
    }

    /**
     * 将别名 用 ":" 拼接到字符首尾 转化为 Unicode 表情
     * e.g.  :dog: -> 🐶
     * <p>
     * 将HTML字符(或16进制) 转化为 Unicode 表情
     * e.g.  &amp;#128054; -> 🐶
     * <p>
     * 将别名 用 ":" 拼接到字符首尾 转化为 Unicode 表情, 用 "|" 拼接类型
     * e.g. :boy|type_6: -> 👦🏿
     *
     * @param str 包含表情的字符串
     * @return 替换后的字符串
     */
    public static String toUnicode(String str) {
        return EmojiParser.parseToUnicode(str);
    }

    /**
     * 将字符串中的 unicode 表情转换为别名表现形式 用 ":" 拼接到首尾
     * e.g. 😄 -> :smile:
     * <p>
     * 如果有FitzpatrickAction设置为FitzpatrickAction#PARSE 则别名后会拼接 "|" 并拼接fitzpatrick类型
     * <p>
     * e.g. 👦🏿 -> :boy|type_6:
     * <p>
     * 如果FitzpatrickAction设置为FitzpatrickAction#REMOVE, 则不拼接 "|" 和fitzpatrick类型
     * e.g. 👦🏿 -> :boy:
     * <p>
     * 如果FitzpatrickAction设置为FitzpatrickAction#IGNORE, 则别名后的类型将被忽略
     * e.g. 👦🏿 -> :boy:                         🏿
     *
     * @param str 包含表情的字符串
     * @return 替换后的字符串
     */
    public static String toAlias(String str) {
        return toAlias(str, EmojiParser.FitzpatrickAction.PARSE);
    }

    /**
     * 别名
     *
     * @param str 包含表情的字符串
     * @return 替换后的字符串
     */
    public static String toAlias(String str, EmojiParser.FitzpatrickAction fitzpatrickAction) {
        return EmojiParser.parseToAliases(str, fitzpatrickAction);
    }

    /**
     * 转义 unicode 为 html 16进制
     * e.g. 👦🏿 -> &amp;#x1f466;
     *
     * @param str 包含表情的字符串
     * @return 替换后的字符串
     */
    public static String toHtmlHex(String str) {
        return EmojiParser.parseToHtmlHexadecimal(str);
    }

    /**
     * 转义 unicode 为 html 16进制
     *
     * @param str    包含表情的字符串
     * @param action 类型
     * @return 替换后的字符串
     */
    public static String toHtmlHex(String str, EmojiParser.FitzpatrickAction action) {
        return EmojiParser.parseToHtmlHexadecimal(str, action);
    }

    /**
     * 转义 unicode 为 html 10进制
     * e.g. 👦🏿 -> &amp;#128102;
     *
     * @param str 包含表情的字符串
     * @return 替换后的字符串
     */
    public static String toHtml(String str) {
        return EmojiParser.parseToHtmlDecimal(str);
    }

    /**
     * 转义 unicode 为 html 10进制
     *
     * @param str    包含表情的字符串
     * @param action 类型
     * @return 替换后的字符串
     */
    public static String toHtml(String str, EmojiParser.FitzpatrickAction action) {
        return EmojiParser.parseToHtmlDecimal(str, action);
    }

    /**
     * 去除字符串中所有的表情 unicode 字符
     *
     * @param str 去除替换的字符串
     * @return 去除后的字符串
     */
    public static String clearAllEmoji(String str) {
        return EmojiParser.removeAllEmojis(str);
    }

    /**
     * 去除字符串中的表情 unicode 字符, 跳过包含的表情
     *
     * @param str  需要去除的字符串
     * @param skip 需要跳过的表情
     * @return 去除后的字符串
     */
    public static String clearEmojiSkip(String str, Collection<Emoji> skip) {
        return EmojiParser.removeAllEmojisExcept(str, skip);
    }

    /**
     * 去除字符串中的表情 unicode 字符
     *
     * @param str    需要去除的字符串
     * @param remove 需要去除的表情
     * @return 去除后的字符串
     */
    public static String clearEmoji(String str, Collection<Emoji> remove) {
        return EmojiParser.removeEmojis(str, remove);
    }

    /**
     * 提取字符串中所有的表情 unicode
     *
     * @param str 包含表情的字符串
     * @return 表情列表
     */
    public static List<String> extractEmoji(String str) {
        return EmojiParser.extractEmojis(str);
    }

}

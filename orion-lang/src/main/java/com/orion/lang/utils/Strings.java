package com.orion.lang.utils;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.collect.Collections;
import com.orion.lang.utils.regexp.Matches;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/3 10:21
 */
@SuppressWarnings("ALL")
public class Strings {

    public static final String EMPTY = Const.EMPTY;

    public static final String SPACE = Const.SPACE;

    private Strings() {
    }

    /**
     * 创建字符串(UTF-8)
     *
     * @param obj 字符对象
     * @return 字符串
     */
    public static String str(Object obj) {
        return str(obj, Const.UTF_8);
    }

    /**
     * 创建字符串
     *
     * @param obj     字符对象
     * @param charset 编码格式
     * @return 字符串
     */
    public static String str(Object obj, String charset) {
        if (obj == null) {
            return null;
        }
        Charset cs;
        if (charset == null) {
            cs = Charset.defaultCharset();
        } else {
            cs = Charset.forName(charset);
        }
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof char[]) {
            return new String((char[]) obj);
        } else if (obj instanceof byte[]) {
            return new String((byte[]) obj, cs);
        } else if (obj instanceof ByteBuffer) {
            return cs.decode((ByteBuffer) obj).toString();
        }
        return obj.toString();
    }

    /**
     * 字符串长度
     *
     * @param s 字符串
     * @return 长度
     */
    public static int length(String s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 字符串长度
     *
     * @param s    字符串
     * @param trim 是否去除头尾空格
     * @return 长度
     */
    public static int length(String s, boolean trim) {
        if (trim) {
            return s == null ? 0 : s.trim().length();
        } else {
            return s == null ? 0 : s.length();
        }
    }

    public static String def(String str) {
        if (isBlank(str)) {
            return Strings.EMPTY;
        }
        return str;
    }

    public static String def(String str, String def) {
        if (isBlank(str)) {
            return def;
        }
        return str;
    }

    /**
     * 默认值
     *
     * @param str 字符串
     * @param def 默认值
     * @return 如果字符串为空返回默认值
     */
    public static String def(String str, Supplier<String> def) {
        if (isBlank(str)) {
            return def.get();
        }
        return str;
    }

    public static String ifBlank(String s) {
        return isBlank(s) ? EMPTY : s;
    }

    public static String ifBlank(String s, String def) {
        return isBlank(s) ? def : s;
    }

    /**
     * 如果为空串返回默认值
     *
     * @param s   s
     * @param def def
     * @return s or def
     */
    public static String ifBlank(String s, Supplier<String> def) {
        return isBlank(s) ? def.get() : s;
    }

    public static String ifEmpty(String s) {
        return isEmpty(s) ? EMPTY : s;

    }

    public static String ifEmpty(String s, String def) {
        return isEmpty(s) ? def : s;
    }

    /**
     * 如果为空返回默认值
     *
     * @param s   s
     * @param def def
     * @return s or def
     */
    public static String ifEmpty(String s, Supplier<String> def) {
        return isEmpty(s) ? def.get() : s;
    }

    /**
     * 非空执行
     *
     * @param s        s
     * @param acceptor acceptor
     */
    public static void ifNotBlank(String s, Consumer<String> acceptor) {
        Valid.notNull(acceptor, "acceptor is null");
        if (isBlank(s)) {
            return;
        }
        acceptor.accept(s);
    }

    /**
     * 非空执行
     *
     * @param s        s
     * @param acceptor acceptor
     */
    public static void ifNotEmpty(String s, Consumer<String> acceptor) {
        Valid.notNull(acceptor, "acceptor is null");
        if (isEmpty(s)) {
            return;
        }
        acceptor.accept(s);
    }

    /**
     * 检查是否为空
     *
     * @param str 待验证的字符串
     * @return 空 true
     */
    public static boolean isBlank(String str) {
        return str == null || Strings.EMPTY.equals(str) || Strings.EMPTY.equals(str.trim()) || str.replaceAll("\\s", Strings.EMPTY).length() == 0;
    }

    /**
     * 检查是否不为空
     *
     * @param str 待验证的字符串
     * @return 空 false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 是否全部为空
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部为空true
     */
    public static boolean isAllBlank(String... strs) {
        if (strs == null) {
            return true;
        } else if (strs.length == 0) {
            return true;
        }
        for (String str : strs) {
            if (!isBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否没有为空
     *
     * @param strs 待验证的一组字符串, 参数为空返回为false
     * @return 全部不为空true
     */
    public static boolean isNoneBlank(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否为空
     *
     * @param str 待验证的字符串
     * @return 空 true
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 检查是否不为空
     *
     * @param str 待验证的字符串
     * @return 空 false
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 是否全部为空
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部为空true
     */
    public static boolean isAllEmpty(CharSequence... strs) {
        if (strs == null) {
            return true;
        } else if (strs.length == 0) {
            return true;
        }
        for (CharSequence str : strs) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否没有为空
     *
     * @param strs 待验证的一组字符串, 参数为空返回为false
     * @return 全部不为空true
     */
    public static boolean isNoneEmpty(CharSequence... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为整数(正则)
     *
     * @param str 待验证的字符串
     * @return true整数
     */
    public static boolean isInteger(String str) {
        if (isBlank(str)) {
            return false;
        }
        return Matches.isInteger(str);
    }

    /**
     * 是否不为整数
     *
     * @param str 待验证的字符串
     * @return true不为整数
     */
    public static boolean isNotInteger(String str) {
        return !isInteger(str);
    }

    /**
     * 是否全部为整数数
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部为整数true
     */
    public static boolean isAllInteger(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isBlank(str) || !Matches.isInteger(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全部不为浮点数
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部不为浮点数true
     */
    public static boolean isNoneInteger(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (!isBlank(str) && Matches.isInteger(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为整数(ASCII)
     *
     * @param str 待验证的字符串
     * @return true整数
     */
    public static boolean isIntegerASCII(String str) {
        if (isBlank(str)) {
            return false;
        }
        for (int i = str.length(); --i > 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        int chr = str.charAt(0);
        if ((chr < 48 || chr > 57) && chr != 45) {
            return false;
        }
        return true;
    }

    /**
     * 是否为浮点数数(正则)
     *
     * @param str 待验证的字符串
     * @return true浮点数
     */
    public static boolean isDouble(String str) {
        if (isBlank(str)) {
            return false;
        }
        return Matches.isDouble(str);
    }

    /**
     * 是否不为浮点数数(正则)
     *
     * @param str 待验证的字符串
     * @return true不为浮点数
     */
    public static boolean isNotDouble(String str) {
        return !isDouble(str);
    }

    /**
     * 是否全部为浮点数
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部为浮点数true
     */
    public static boolean isAllDouble(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isBlank(str) || !Matches.isDouble(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全部不为浮点数
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部不为浮点数true
     */
    public static boolean isNoneDouble(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (!isBlank(str) && Matches.isDouble(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为数字
     *
     * @param str 待验证的字符串
     * @return true数字
     */
    public static boolean isNumber(String str) {
        if (isBlank(str)) {
            return false;
        }
        return Matches.isNumber(str);
    }

    /**
     * 是否不为数字
     *
     * @param str 待验证的字符串
     * @return true不为数字
     */
    public static boolean isNotNumber(String str) {
        return !isNumber(str);
    }

    /**
     * 是否全部为数字
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部为数字true
     */
    public static boolean isAllNumber(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isBlank(str) || !(matcherNumber(str))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全部不为数字
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部不为数字true
     */
    public static boolean isNoneNumber(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (!isBlank(str) && matcherNumber(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 匹配是否为整数或浮点数
     *
     * @param str 待匹配的字符
     * @return true整数或浮点数
     */
    private static boolean matcherNumber(String str) {
        return Matches.isInteger(str) || Matches.isDouble(str);
    }

    /**
     * 是否为start开头
     *
     * @param s     ignore
     * @param start ignore
     * @return true 开头
     */
    public static boolean startWith(String s, String start) {
        return s.startsWith(start);
    }

    /**
     * 是否为end结尾
     *
     * @param s   ignore
     * @param end ignore
     * @return true 结尾
     */
    public static boolean endWith(String s, String end) {
        return s.endsWith(end);
    }

    /**
     * 截取第一个分隔符之前字符串
     *
     * @param s         ignore
     * @param separator 分隔符
     * @return ignore
     */
    public static String substringBefore(String s, String separator) {
        if (isEmpty(s) || isEmpty(separator)) {
            return s;
        }
        int pos = s.indexOf(separator);
        if (pos == -1) {
            return s;
        }
        return s.substring(0, pos);
    }

    /**
     * 截取最后一个分隔符之前字符串
     *
     * @param s         ignore
     * @param separator 分隔符
     * @return ignore
     */
    public static String substringBeforeLast(String s, String separator) {
        if (isEmpty(s) || isEmpty(separator)) {
            return s;
        }
        int pos = s.lastIndexOf(separator);
        if (pos == -1) {
            return s;
        }
        return s.substring(0, pos);
    }

    /**
     * 截取第一个分隔符之后字符串
     *
     * @param s         ignore
     * @param separator 分隔符
     * @return ignore
     */
    public static String substringAfter(String s, String separator) {
        if (isEmpty(s) || isEmpty(separator)) {
            return s;
        }
        int pos = s.indexOf(separator);
        if (pos == -1) {
            return s;
        }
        return s.substring(pos + separator.length());
    }

    /**
     * 截取最后一个分隔符之后字符串
     *
     * @param s         ignore
     * @param separator 分隔符
     * @return ignore
     */
    public static String substringAfterLast(String s, String separator) {
        if (isEmpty(s) || isEmpty(separator)) {
            return s;
        }
        int pos = s.indexOf(separator);
        if (pos == -1) {
            return s;
        }
        return s.substring(pos + separator.length());
    }

    /**
     * 切分
     *
     * @param str       待切分的字符串
     * @param tokenizer 分隔符
     * @param consumer  消费
     */
    public static void split(String str, String tokenizer, Consumer<String> consumer) {
        if (isBlank(str) || isBlank(tokenizer)) {
            return;
        }
        String[] arr = str.split(tokenizer);
        for (String s : arr) {
            consumer.accept(s);
        }
    }

    public static String join(String... strs) {
        if (Arrays1.length(strs) == 0) {
            return Strings.EMPTY;
        }
        StringBuilder sb = newBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 连接字符串
     *
     * @param strs      连接字符串
     * @param delimiter delimiter
     * @return ignore
     */
    public static String joinWith(String delimiter, String... strs) {
        if (Arrays1.length(strs) == 0) {
            return Strings.EMPTY;
        }
        if (isBlank(delimiter)) {
            delimiter = Strings.EMPTY;
        }
        StringBuilder sb = newBuilder();
        for (String str : strs) {
            sb.append(str).append(delimiter);
        }
        sb.deleteCharAt(sb.length() - delimiter.length());
        return sb.toString();
    }

    public static String join(Collection<String> list, String delimiter) {
        return join(list, delimiter, Strings.EMPTY, Strings.EMPTY);
    }

    /**
     * 连接字符串
     *
     * @param list      连接字符串
     * @param delimiter delimiter
     * @param prefix    前缀
     * @param suffix    后缀
     * @return 连接后的字符串
     */
    public static String join(Collection<String> list, String delimiter, String prefix, String suffix) {
        prefix = isBlank(prefix) ? Strings.EMPTY : prefix;
        suffix = isBlank(suffix) ? Strings.EMPTY : suffix;
        StringJoiner sj = new StringJoiner(delimiter, prefix, suffix);
        list.forEach(sj::add);
        return sj.toString();
    }

    /**
     * 拼接字符串
     *
     * @param s      拼接字符串到前后
     * @param prefix 前缀
     * @param suffix 后缀
     * @return prefix + s + suffix
     */
    public static String appendAround(String s, String prefix, String suffix) {
        prefix = isBlank(prefix) ? Strings.EMPTY : prefix;
        suffix = isBlank(suffix) ? Strings.EMPTY : suffix;
        if (isBlank(s)) {
            return prefix + suffix;
        }
        return prefix + s + suffix;
    }

    /**
     * 替换字符串
     *
     * @param s 字符串
     * @param o 原字符
     * @param t 新字符串
     * @return 替换后的字符串
     */
    public static String replace(String s, String o, String t) {
        if (s.isEmpty()) {
            return s;
        }
        return s.replace(o, t);
    }

    /**
     * 替换字符串
     *
     * @param s   字符串
     * @param reg 正则
     * @param t   新字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(String s, String reg, String t) {
        if (s.isEmpty()) {
            return s;
        }
        return s.replaceAll(reg, t);
    }

    /**
     * 随机一个中文汉字
     *
     * @return 汉字
     */
    public static String randomChar() {
        return randomChars(1);
    }

    /**
     * 随机几个个中文汉字
     *
     * @param count 汉字数量
     * @return 汉字
     */
    public static String randomChars(int count) {
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int highCode;
            int lowCode;
            Random random = new Random();
            highCode = (176 + Math.abs(random.nextInt(39)));
            lowCode = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(highCode)).byteValue();
            b[1] = (Integer.valueOf(lowCode)).byteValue();
            build.append(new String(b, Charsets.GBK));
        }
        return build.toString();
    }

    /**
     * 模板字符串
     *
     * @param str  模板
     * @param args 参数
     * @return 字符串
     */
    public static String format(String str, Object... args) {
        if (isBlank(str) || args == null) {
            return str;
        }
        int strLength = str.length();
        StringBuilder sb = new StringBuilder(strLength + 50);
        int handledPosition = 0;
        int index;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            index = str.indexOf("{}", handledPosition);
            if (index == -1) {
                if (handledPosition == 0) {
                    return str;
                } else {
                    sb.append(str, handledPosition, strLength);
                    return sb.toString();
                }
            } else {
                if (index > 0 && str.charAt(index - 1) == '\\') {
                    if (index > 1 && str.charAt(index - 2) == '\\') {
                        sb.append(str, handledPosition, index - 1);
                        sb.append(str(args[argIndex]));
                        handledPosition = index + 2;
                    } else {
                        argIndex--;
                        sb.append(str, handledPosition, index - 1);
                        sb.append('{');
                        handledPosition = index + 1;
                    }
                } else {
                    sb.append(str, handledPosition, index);
                    sb.append(str(args[argIndex]));
                    handledPosition = index + 2;
                }
            }
        }
        sb.append(str, handledPosition, str.length());
        return sb.toString();
    }

    /**
     * 格式化字符串 ${}
     *
     * @param str 字符串
     * @param map ${key} = value
     * @return str
     */
    public static String format(String str, Map<?, ?> map) {
        return format(str, "\\$", map);
    }

    /**
     * 格式化字符串 comment{}
     *
     * @param str 字符串
     * @param map comment{key} = value
     * @return str
     */
    public static String format(String str, String comment, Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Pattern p = Pattern.compile(comment + "\\{" + entry.getKey() + "}");
            Matcher matcher = p.matcher(str);
            if (matcher.find()) {
                str = matcher.replaceAll(Objects1.toString(entry.getValue()));
            }
        }
        return str;
    }

    /**
     * 构建字符串 (StringBuild)
     *
     * @param obj 字符对象
     * @return 构建后的字符串
     */
    public static String build(Object... obj) {
        return build(null, obj);
    }

    /**
     * 构建字符串
     *
     * @param build 构建对象
     * @param obj   字符对象
     * @return 构建后的字符串
     */
    public static String build(Appendable build, Object... obj) {
        if (obj == null || obj.length == 0) {
            return Strings.EMPTY;
        }
        if (build == null) {
            build = new StringBuilder();
        }
        try {
            for (Object o : obj) {
                build.append(str(o));
            }
        } catch (Exception e) {
            return Strings.EMPTY;
        }
        return build.toString();
    }

    public static StringBuilder newBuilder() {
        return new StringBuilder();
    }

    public static StringBuilder newBuilder(String s) {
        return new StringBuilder(s);
    }

    /**
     * 创建StringBuilder对象
     *
     * @param capacity 初始化容量
     * @return StringBuilder
     */
    public static StringBuilder newBuilder(int capacity) {
        return new StringBuilder(capacity);
    }

    public static StringBuffer newBuffer() {
        return new StringBuffer();
    }

    public static StringBuffer newBuffer(String s) {
        return new StringBuffer(s);
    }

    public static StringBuffer newBuffer(int capacity) {
        return new StringBuffer(capacity);
    }

    /**
     * 比较字符串是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 相同true
     */
    public static boolean eq(Object o1, Object o2) {
        String str1 = str(o1);
        String str2 = str(o2);
        return eq(str1, str2);
    }

    /**
     * 比较字符串是否相等 (去除首尾空格)
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 相同true
     */
    public static boolean trimEq(Object o1, Object o2) {
        String str1 = str(o1);
        String str2 = str(o2);
        if (str1 != null) {
            str1 = str1.trim();
        }
        if (str2 != null) {
            str2 = str2.trim();
        }
        return eq(str1, str2);
    }

    /**
     * 比较字符串是否相等(不区分大小写)
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 相同true
     */
    public static boolean ignoreEq(Object o1, Object o2) {
        String str1 = str(o1);
        String str2 = str(o2);
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null) {
            return str1.equalsIgnoreCase(str2);
        }
        return false;
    }

    /**
     * 比较字符串是否相等
     *
     * @param str1 字符1
     * @param str2 字符2
     * @return 相同true
     */
    public static boolean eq(String str1, String str2) {
        return (str1 == str2) || (str1 != null && str1.equals(str2));
    }

    /**
     * 判断至少包含一个相同的值
     *
     * @param str  判断的值
     * @param strs 给定的一组值
     * @return true包含至少一个
     */
    public static boolean some(String str, String... strs) {
        if (isEmpty(str)) {
            return false;
        } else if (Arrays1.length(strs) == 0) {
            return false;
        }
        for (String t : strs) {
            if (eq(str, t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 小写首字母
     *
     * @param obj 字符对象
     * @return 新字符串对象
     */
    public static String firstLower(Object obj) {
        String str = str(obj);
        if (isBlank(str)) {
            return str;
        }
        char[] cs = str.toCharArray();
        if ((cs[0] >= 'A') && (cs[0] <= 'Z')) {
            cs[0] += (char) 0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 大写首字母
     *
     * @param obj 字符对象
     * @return 新字符串对象
     */
    public static String firstUpper(Object obj) {
        String str = str(obj);
        if (isBlank(str)) {
            return str;
        }
        char[] cs = str.toCharArray();
        if ((cs[0] >= 'a') && (cs[0] <= 'z')) {
            cs[0] -= (char) 0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 全角字符变半角字符
     *
     * @param s 需要处理的字符串
     * @return 处理后的字符串
     */
    public static String fullToHalf(String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 半角字符变全角字符
     *
     * @param s 需要处理的字符串
     * @return 处理后的字符串
     */
    public static String halfToFull(String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 重复字符串
     *
     * @param str 字符串
     * @param num 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int num) {
        if (isBlank(str)) {
            return str;
        }
        if (num <= 0) {
            return Strings.EMPTY;
        }
        if (num == 1) {
            return str;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < num; i++) {
            s.append(str);
        }
        return s.toString();
    }

    /**
     * 重复字符串
     *
     * @param c   字符
     * @param num 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(char c, int num) {
        if (num <= 0) {
            return Strings.EMPTY;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < num; i++) {
            s.append(c);
        }
        return s.toString();
    }

    /**
     * 重复字符串
     *
     * @param str 字符串
     * @param num 重复次数
     * @return 重复后的字符串数组
     */
    public static char[] repeatArr(String str, int num) {
        int length = Strings.length(str);
        if (num <= 0) {
            return new char[0];
        }
        char[] chars = str.toCharArray();
        if (num == 1) {
            return chars;
        }
        char[] arr = new char[length * num];
        for (int i = 0; i < num; i++) {
            System.arraycopy(chars, 0, arr, i * length, length);
        }
        return arr;
    }

    /**
     * 删除所有的标点符号
     *
     * @param s 处理的字符串
     */
    public static String trimPunct(String s) {
        if (isBlank(s)) {
            return s;
        }
        return s.replaceAll("[\\pP\\p{Punct}]", Strings.EMPTY).replaceAll("￥", Strings.EMPTY);
    }

    /**
     * 匹配字符出现次数
     *
     * @param s    源数据
     * @param find 匹配数据
     * @return 次数
     */
    public static int findNum(String s, String find) {
        return Matches.findNum(s, find);
    }

    /**
     * 跳过字符串开头
     *
     * @param str  字符串
     * @param skip 跳过几位
     * @return 跳过后的字符串
     */
    public static String skip(String str, int skip) {
        if (isEmpty(str)) {
            return str;
        }
        int length = length(str);
        if (skip >= length) {
            return Strings.EMPTY;
        }
        char[] cs = new char[length - skip];
        System.arraycopy(str.toCharArray(), skip, cs, 0, length - skip);
        return str(cs);
    }

    public static String omit(String str, int length) {
        return centerOmit(str, length, length, Const.OMIT);
    }

    /**
     * 省略字符串结尾
     * <p>
     * [3] 1234567 123...
     * [4] 1234567 1234567
     *
     * @param str    字符串
     * @param length 保留几位
     * @param omit   省略符
     * @return 省略后的字符串
     */
    public static String omit(String str, int length, String omit) {
        return centerOmit(str, length, length, omit);
    }

    public static String centerOmit(String str, int leftLength, int length) {
        return centerOmit(str, leftLength, length, Const.OMIT);
    }

    /**
     * 省略字符串中间
     * <p>
     * [1, 3] 1234567 1...67
     * [2, 3] 1234567 12...7
     * [2, 4] 1234567 1234567
     *
     * @param str    字符串
     * @param length 左侧保留几位
     * @param length 总共保留几位
     * @param omit   省略符
     * @return 省略后的字符串
     */
    public static String centerOmit(String str, int leftLength, int length, String omit) {
        Valid.gte(length, leftLength, "length must >= left length");
        if (isEmpty(str)) {
            return str;
        }
        int len = length(str);
        if (length >= len) {
            return str;
        }
        String left = str.substring(0, leftLength);
        int rightLen = len - leftLength;
        if (rightLen > length - leftLength + omit.length()) {
            return left + omit + str.substring(len - (length - leftLength));
        } else {
            return str;
        }
    }

    /**
     * 转码
     *
     * @param s          ignore
     * @param oldCharset 原编码格式
     * @param newCharset 新编码格式
     * @return ignore
     */
    public static String charset(String s, String oldCharset, String newCharset) {
        if (!isBlank(s)) {
            try {
                byte[] bytes = Strings.bytes(s, oldCharset);
                return new String(bytes, newCharset);
            } catch (Exception e) {
                return s;
            }
        }
        return s;
    }

    /**
     * 转码
     *
     * @param s       ignore
     * @param charset 原编码格式
     * @return ignore
     */
    public static String charset(String s, String charset) {
        if (!isBlank(s)) {
            try {
                return new String(Strings.bytes(s), charset);
            } catch (Exception e) {
                return s;
            }
        }
        return s;
    }

    /**
     * GBK编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String toGBK(String s) {
        return charset(s, Const.GBK);
    }

    /**
     * UTF8编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String toUTF8(String s) {
        return charset(s, Const.UTF_8);
    }

    /**
     * ASCII编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String toASCII(String s) {
        return charset(s, Const.ASCII);
    }

    /**
     * 右侧用空格补齐到规定长度
     * rightPad(null, *)   = null
     * rightPad(Strings.EMPTY, 3)     = "   "
     * rightPad("abc", 3)  = "abc"
     * rightPad("abc", 5)  = "abc  "
     * rightPad("abc", 1)  = "abc"
     * rightPad("abc", -1) = "abc"
     *
     * @param str  字符串
     * @param size 长度
     * @return ignore
     */
    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = Strings.SPACE;
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return rightPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * 左侧用空格补齐到规定长度
     * leftPad(null, *)   = null
     * leftPad(Strings.EMPTY, 3)     = "   "
     * leftPad("abc", 3)  = "abc"
     * leftPad("abc", 5)  = "  abc"
     * leftPad("abc", 1)  = "abc"
     * leftPad("abc", -1) = "abc"
     *
     * @param str  字符串
     * @param size 长度
     * @return ignore
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = Strings.SPACE;
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * 中间用空格补齐到规定长度
     * .center(null, *)   = null
     * .center(Strings.EMPTY, 4)     = "    "
     * .center("ab", -1)  = "ab"
     * .center("ab", 4)   = " ab "
     * .center("abc", 2) = "abc"
     * .center("a", 4)    = " a  "
     *
     * @param str  字符串
     * @param size 长度
     * @return ignore
     */
    public static String centerPad(String str, int size) {
        return centerPad(str, size, ' ');
    }

    public static String centerPad(String str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }

    public static String centerPad(String str, int size, String padStr) {
        if (str == null || size <= 0) {
            return str;
        }
        if (isEmpty(padStr)) {
            padStr = Strings.SPACE;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padStr);
        str = rightPad(str, size, padStr);
        return str;
    }

    /**
     * 获取字符串码点
     *
     * @param s s
     * @return ignore
     */
    public static int[] getCodePoints(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0) {
            return new int[0];
        }

        int[] result = new int[s.codePointCount(0, s.length())];
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = s.codePointAt(index);
            index += Character.charCount(result[i]);
        }
        return result;
    }

    /**
     * 获取byte[]
     *
     * @param s s
     * @return byte[]
     */
    public static byte[] bytes(String s) {
        return bytes(s, (Charset) null);
    }

    /**
     * 获取byte[]
     *
     * @param s       s
     * @param charset charset
     * @return byte[]
     */
    public static byte[] bytes(String s, Charset charset) {
        if (charset != null) {
            return s.getBytes(charset);
        }
        return s.getBytes(Charsets.of(Systems.FILE_ENCODING));
    }

    /**
     * 获取byte[]
     *
     * @param s       s
     * @param charset charset
     * @return byte[]
     */
    public static byte[] bytes(String s, String charset) {
        if (charset != null) {
            return s.getBytes(Charsets.of(charset));
        }
        return s.getBytes(Charsets.of(Systems.FILE_ENCODING));
    }

    /**
     * 替换 \r \r\n -> \n
     *
     * @param s s
     * @return replace
     */
    public static String replaceCRLF(String s) {
        return s.replaceAll(Const.CR_LF, Const.LF).replaceAll(Const.CR, Const.LF);
    }

    /**
     * 检查是否包含
     *
     * @param s        s
     * @param contains contains
     * @return 是否包含
     */
    public static boolean contains(String s, String contains) {
        if (s == null || contains == null) {
            return false;
        }
        return s.contains(contains);
    }

    /**
     * 检查是否包含任意项
     *
     * @param s        s
     * @param contains 包含项
     * @return 包含
     */
    public static boolean containsAny(String s, Collection<String> contains) {
        if (s == null || Collections.isEmpty(contains)) {
            return false;
        }
        for (String contain : contains) {
            if (s.contains(contain)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否包含所有项
     *
     * @param s        s
     * @param contains contains
     * @return 包含所有
     */
    public static boolean containsAll(String s, Collection<String> contains) {
        if (s == null) {
            return false;
        }
        if (Collections.isEmpty(contains)) {
            return true;
        }
        for (String contain : contains) {
            if (!s.contains(contain)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否不包含
     *
     * @param s        s
     * @param contains contains
     * @return 是否不包含
     */
    public static boolean containsNone(String s, String contains) {
        if (s == null || contains == null) {
            return true;
        }
        return !s.contains(contains);
    }

    /**
     * 检查是否不包含
     *
     * @param s        s
     * @param contains contains
     * @return 不包含
     */
    public static boolean containsNone(String s, Collection<String> contains) {
        if (s == null || Collections.isEmpty(contains)) {
            return true;
        }
        for (String contain : contains) {
            if (s.contains(contain)) {
                return false;
            }
        }
        return true;
    }

}

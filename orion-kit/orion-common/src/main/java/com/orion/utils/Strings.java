package com.orion.utils;

import com.orion.enums.VariableStyleEnum;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/3 10:21
 */
@SuppressWarnings("ALL")
public class Strings {

    /**
     * 大驼峰
     */
    private static final Pattern BIG_HUMP = Pattern.compile("[A-Z]([a-z\\d]+)?");

    private static final String UTF8 = "UTF-8";

    private Strings() {
    }

    /**
     * 创建字符串(UTF-8)
     *
     * @param obj 字符对象
     * @return 字符串
     */
    public static String str(Object obj) {
        return str(obj, UTF8);
    }

    /**
     * 创建字符串
     *
     * @param obj     字符对象
     * @param charset 编码格式
     * @return 字符串
     */
    public static String str(Object obj, String charset) {
        if (null == obj) {
            return null;
        }
        Charset cs;
        if (charset == obj) {
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

    /**
     * 如果为空串返回默认值
     *
     * @param s   s
     * @param def def
     * @return s or def
     */
    public static String ifBlank(String s, String def) {
        if (isBlank(s)) {
            return def;
        }
        return s;
    }

    /**
     * 如果为空返回默认值
     *
     * @param s   s
     * @param def def
     * @return s or def
     */
    public static String ifEmpty(String s, String def) {
        if (isEmpty(s)) {
            return def;
        }
        return s;
    }

    /**
     * 检查是否为空
     *
     * @param str 待验证的字符串
     * @return 空 true
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str) || "".equals(str.trim()) || 0 == str.replaceAll("\\s", "").length();
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
    public static boolean isAllblank(String... strs) {
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
    public static boolean isEmpty(String str) {
        return str == null || 0 == str.length();
    }

    /**
     * 检查是否不为空
     *
     * @param str 待验证的字符串
     * @return 空 false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 是否全部为空
     *
     * @param strs 待验证的一组字符串, 参数为空返回为true
     * @return 全部为空true
     */
    public static boolean isAllEmpty(String... strs) {
        if (strs == null) {
            return true;
        } else if (strs.length == 0) {
            return true;
        }
        for (String str : strs) {
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
    public static boolean isNoneEmpty(String... strs) {
        if (strs == null) {
            return false;
        } else if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
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
        return Matches.isInteger(str) || Matches.isDouble(str);
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
    public static String subStringBefore(String s, String separator) {
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
    public static String subStringBeforeLast(String s, String separator) {
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
    public static String subStringAfter(String s, String separator) {
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
    public static String subStringAfterLast(String s, String separator) {
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

    /**
     * 连接字符串
     *
     * @param strs 连接字符串
     * @return ignore
     */
    public static String join(String... strs) {
        System.out.println(Arrays1.length(strs));
        if (Arrays1.length(strs) == 0) {
            return "";
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
     * @param strs 自串支付付出
     * @return ignore
     */
    public static String joinSymbol(String symbol, String... strs) {
        System.out.println(Arrays1.length(strs));
        if (Arrays1.length(strs) == 0) {
            return "";
        }
        if (isBlank(symbol)) {
            symbol = "";
        }
        StringBuilder sb = newBuilder();
        for (String str : strs) {
            sb.append(str).append(symbol);
        }
        sb.deleteCharAt(sb.length() - symbol.length());
        return sb.toString();
    }

    /**
     * 连接字符串
     *
     * @param list   需要处理的列表
     * @param symbol symbol
     * @return 连接后的字符串
     */
    public static String join(List<String> list, String symbol) {
        return join(list, symbol, "", "");
    }

    /**
     * 连接字符串
     *
     * @param list   需要处理的列表
     * @param symbol symbol
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 连接后的字符串
     */
    public static String join(List<String> list, String symbol, String prefix, String suffix) {
        StringJoiner sj = new StringJoiner(symbol, prefix, suffix);
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
        prefix = isBlank(prefix) ? "" : prefix;
        suffix = isBlank(suffix) ? "" : suffix;
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
        try {
            for (int i = 0; i < count; i++) {
                int highCode;
                int lowCode;
                Random random = new Random();
                highCode = (176 + Math.abs(random.nextInt(39)));
                lowCode = (161 + Math.abs(random.nextInt(93)));
                byte[] b = new byte[2];
                b[0] = (Integer.valueOf(highCode)).byteValue();
                b[1] = (Integer.valueOf(lowCode)).byteValue();
                build.append(new String(b, "GBK"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            str = str.replaceAll("\\$\\{" + entry.getKey() + "}", Objects1.toString(entry.getValue()));
        }
        return str;
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
            str = str.replaceAll(comment + "\\{" + entry.getKey() + "}", Objects1.toString(entry.getValue()));
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
            return "";
        }
        if (build == null) {
            build = new StringBuilder();
        }
        try {
            for (Object o : obj) {
                build.append(str(o));
            }
        } catch (Exception e) {
            return "";
        }
        return build.toString();
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder
     */
    public static StringBuilder newBuilder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     *
     * @param s 初始化字符串
     * @return StringBuilder
     */
    public static StringBuilder newBuilder(CharSequence s) {
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

    /**
     * 创建StringBuffer对象
     *
     * @return StringBuffer
     */
    public static StringBuffer newBuffer() {
        return new StringBuffer();
    }

    /**
     * 创建StringBuffer对象
     *
     * @param s 初始化字符串
     * @return StringBuffer
     */
    public static StringBuffer newBuffer(CharSequence s) {
        return new StringBuffer(s);
    }

    /**
     * 创建StringBuffer对象
     *
     * @param capacity 初始化容量
     * @return StringBuffer
     */
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
    @SuppressWarnings("all")
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
            return "";
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
            return "";
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
            num = 1;
        }
        char[] chars = str.toCharArray();
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
        return s.replaceAll("[\\pP\\p{Punct}]", "");
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
     * 默认值
     *
     * @param str 字符串
     * @return 如果字符串为空返回默认值
     */
    public static String def(String str) {
        if (str == null) {
            return "";
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
    public static String def(String str, String def) {
        if (str == null) {
            return def;
        }
        return str;
    }

    /**
     * 生成一串字符
     *
     * @return 字符
     */
    public static String lorem() {
        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque aut doloremque ea eveniet sint sit voluptas! Ab accusantium aperiam, earum eos id impedit, laboriosam necessitatibus nobis non sint vel voluptates?";
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
            return "";
        }
        char[] cs = new char[length - skip];
        System.arraycopy(str.toCharArray(), skip, cs, 0, length - skip);
        return str(cs);
    }

    /**
     * 省略字符串结尾
     *
     * @param str  字符串
     * @param omit 省略几位
     * @return 省略后的字符串
     */
    public static String omit(String str, int omit) {
        if (isEmpty(str)) {
            return str;
        }
        int length = length(str);
        if (omit >= length) {
            return "";
        }
        char[] cs = new char[length - omit];
        System.arraycopy(str.toCharArray(), 0, cs, 0, length - omit);
        return str(cs);
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
                byte[] bytes = s.getBytes(oldCharset);
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
                byte[] bytes = s.getBytes();
                return new String(bytes, charset);
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
        return charset(s, "GBK");
    }

    /**
     * UTF8编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String toUTF8(String s) {
        return charset(s, "UTF-8");
    }

    /**
     * ASCII编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String toASCII(String s) {
        return charset(s, "US-ASCII");
    }

    /**
     * 右侧用空格补齐到规定长度
     * rightPad(null, *)   = null
     * rightPad("", 3)     = "   "
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
            padStr = " ";
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
     * leftPad("", 3)     = "   "
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
            padStr = " ";
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
     * .center("", 4)     = "    "
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
            padStr = " ";
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
     * 转化变量命名风格
     *
     * @param variable 变量
     * @param e        命名风格
     * @return 变量
     */
    public static String convertVariableStyle(String variable, VariableStyleEnum e) {
        if (isBlank(variable)) {
            return variable;
        }
        int styleType = VariableStyleEnum.getType(variable).getStyleType();
        if (e.getStyleType() == styleType) {
            return variable;
        }
        switch (styleType) {
            case 1:
                switch (e.getStyleType()) {
                    case 2:
                        return styleConvert(variable, 0);
                    case 3:
                        return styleConvert(variable, "_");
                    case 4:
                        return styleConvert(variable, "-");
                    default:
                        return variable;
                }
            case 2:
                switch (e.getStyleType()) {
                    case 1:
                        return styleConvert(variable, 1);
                    case 3:
                        return styleConvert(variable, "_");
                    case 4:
                        return styleConvert(variable, "-");
                    default:
                        return variable;
                }
            case 3:
                switch (e.getStyleType()) {
                    case 1:
                        return styleConvert(variable, true);
                    case 2:
                        return styleConvert(variable, false);
                    case 4:
                        return styleConvert(variable, "_", "-");
                    default:
                        return variable;
                }
            case 4:
                switch (e.getStyleType()) {
                    case 1:
                        return styleConvert(variable, true);
                    case 2:
                        return styleConvert(variable, false);
                    case 3:
                        return styleConvert(variable, "-", "_");
                    default:
                        return variable;
                }
            default:
                return variable;
        }
    }

    // --------------- 转化格式私有方法 ---------------

    private static String styleConvert(String variable, String before, String after) {
        return variable.toLowerCase().replaceAll(before, after);
    }

    private static String styleConvert(String variable, String tokenizer) {
        variable = String.valueOf(variable.charAt(0)).toUpperCase().concat(variable.substring(1));
        StringBuilder sb = new StringBuilder();
        Matcher matcher = BIG_HUMP.matcher(variable);
        while (matcher.find()) {
            sb.append(matcher.group().toLowerCase())
                    .append(matcher.end() == variable.length() ? "" : tokenizer);
        }
        return sb.toString();
    }

    private static String styleConvert(String variable, boolean small) {
        String tokenizer;
        if (variable.contains("-")) {
            tokenizer = "-";
        } else {
            tokenizer = "_";
        }
        String patternRegex = "-".equals(tokenizer) ? "([A-Za-z\\d]+)(-)?" : "([A-Za-z\\d]+)(_)?";
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(patternRegex);
        Matcher matcher = pattern.matcher(variable);
        int i = 0;
        while (matcher.find()) {
            String word = matcher.group();
            if (++i == 1 && small) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }
            int index = word.lastIndexOf(tokenizer);
            if (index > 0) {
                sb.append(word.substring(1, index));
            } else {
                sb.append(word.substring(1));
            }
        }
        return sb.toString();
    }

    private static String styleConvert(String variable, int t) {
        if (t == 0) {
            return Character.toUpperCase(variable.charAt(0)) + variable.substring(1);
        } else {
            return Character.toLowerCase(variable.charAt(0)) + variable.substring(1);
        }
    }

}

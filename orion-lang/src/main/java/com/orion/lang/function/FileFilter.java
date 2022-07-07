package com.orion.lang.function;

import com.orion.lang.utils.regexp.Matches;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/10 10:07
 */
@FunctionalInterface
public interface FileFilter {

    /**
     * 过滤
     *
     * @param file 文件对象
     * @param name 文件名称
     * @return 是否保留
     */
    boolean accept(File file, String name);

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return FileFilter
     */
    static FileFilter suffix(String suffix) {
        return (f, n) -> n.toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 文件名过滤器
     *
     * @param name 文件名
     * @return FileFilter
     */
    static FileFilter match(String name) {
        return (f, n) -> n.toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return FileFilter
     */
    static FileFilter pattern(Pattern pattern) {
        return (f, n) -> Matches.test(n, pattern);
    }

}

package com.orion.lang.function;

import com.orion.lang.utils.regexp.Matches;

import java.io.File;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 文件过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/10 10:07
 */
@FunctionalInterface
public interface FileFilter extends Predicate<File> {

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return filter
     */
    static FileFilter suffix(String suffix) {
        return f -> f.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 包含过滤器
     *
     * @param name 文件名
     * @return filter
     */
    static FileFilter contains(String name) {
        return f -> f.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return filter
     */
    static FileFilter matches(Pattern pattern) {
        return f -> Matches.test(f.getName(), pattern);
    }

}

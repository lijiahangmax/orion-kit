package com.orion.net.ftp.client;

import com.orion.lang.utils.regexp.Matches;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * FTP 文件过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/12 9:50
 */
@FunctionalInterface
public interface FileFilter extends Predicate<FtpFile> {

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return filter
     */
    static FileFilter suffix(String suffix) {
        return a -> a.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 包含过滤器
     *
     * @param name 文件名
     * @return filter
     */
    static FileFilter contains(String name) {
        return a -> a.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return filter
     */
    static FileFilter matches(Pattern pattern) {
        return a -> Matches.test(a.getName(), pattern);
    }

}

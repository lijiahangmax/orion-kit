package com.orion.net.host.sftp;

import com.orion.lang.utils.regexp.Matches;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * sftp 文件属性过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/11 22:35
 */
@FunctionalInterface
public interface SftpFileFilter extends Predicate<SftpFile> {

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return filter
     */
    static SftpFileFilter suffix(String suffix) {
        return a -> a.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 包含过滤器
     *
     * @param name 文件名
     * @return filter
     */
    static SftpFileFilter contains(String name) {
        return a -> a.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return filter
     */
    static SftpFileFilter matches(Pattern pattern) {
        return a -> Matches.test(a.getName(), pattern);
    }

}

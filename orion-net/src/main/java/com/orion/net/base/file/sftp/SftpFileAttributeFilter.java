package com.orion.net.base.file.sftp;

import com.orion.utils.regexp.Matches;

import java.util.regex.Pattern;

/**
 * sftp 文件属性过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/11 22:35
 */
@FunctionalInterface
public interface SftpFileAttributeFilter {

    /**
     * accept
     *
     * @param attr 文件属性
     * @return 是否可用
     */
    boolean accept(SftpFile attr);

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return SftpFileAttributeFilter
     */
    static SftpFileAttributeFilter suffix(String suffix) {
        return a -> a.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 文件名过滤器
     *
     * @param name 文件名
     * @return SftpFileAttributeFilter
     */
    static SftpFileAttributeFilter match(String name) {
        return a -> a.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return SftpFileAttributeFilter
     */
    static SftpFileAttributeFilter pattern(Pattern pattern) {
        return a -> Matches.test(a.getName(), pattern);
    }

}

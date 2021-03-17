package com.orion.remote.channel.sftp;

import com.orion.utils.Matches;

import java.util.regex.Pattern;

/**
 * SFTP 文件属性过滤器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/11 22:35
 */
@FunctionalInterface
public interface FileAttributeFilter {

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
     * @return FileAttributeFilter
     */
    static FileAttributeFilter suffix(String suffix) {
        return a -> a.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 文件名过滤器
     *
     * @param name 文件名
     * @return FileAttributeFilter
     */
    static FileAttributeFilter match(String name) {
        return a -> a.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return FileAttributeFilter
     */
    static FileAttributeFilter pattern(Pattern pattern) {
        return a -> Matches.test(a.getName(), pattern);
    }

}

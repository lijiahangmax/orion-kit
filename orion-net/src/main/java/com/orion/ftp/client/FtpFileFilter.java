package com.orion.ftp.client;

import com.orion.utils.Matches;

import java.util.regex.Pattern;

/**
 * FTP文件过滤器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/12 9:50
 */
@FunctionalInterface
public interface FtpFileFilter {

    /**
     * accept
     *
     * @param ftpFile file
     * @return 是否通过
     */
    boolean accept(FtpFile ftpFile);

    /**
     * 后缀过滤器
     *
     * @param suffix 后缀
     * @return FtpFileFilter
     */
    static FtpFileFilter suffix(String suffix) {
        return a -> a.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * 文件名过滤器
     *
     * @param name 文件名
     * @return FtpFileFilter
     */
    static FtpFileFilter match(String name) {
        return a -> a.getName().toLowerCase().contains(name.toLowerCase());
    }

    /**
     * 正则过滤器
     *
     * @param pattern pattern
     * @return FtpFileFilter
     */
    static FtpFileFilter pattern(Pattern pattern) {
        return a -> Matches.test(a.getName(), pattern);
    }

}

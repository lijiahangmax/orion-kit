package com.orion.http.useragent;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * UA 解析器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 18:01
 */
public class UserAgentParses {

    private UserAgentParses() {
    }

    /**
     * 解析UA
     *
     * @param userAgent UA
     * @return UA信息
     */
    public static UserAgentInfo parse(String userAgent) {
        return new UserAgentInfo(UserAgent.parseUserAgentString(userAgent));
    }

    /**
     * 解析UA
     *
     * @param id UA id
     * @return UA信息
     */
    public static UserAgentInfo parseById(int id) {
        return new UserAgentInfo(UserAgent.valueOf(id));
    }

    /**
     * 解析UA
     *
     * @param name UA name
     * @return UA信息
     */
    public static UserAgentInfo parseByName(String name) {
        return new UserAgentInfo(UserAgent.valueOf(name));
    }

}

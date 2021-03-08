package com.orion.constant;

/**
 * 标准 TLS 版本
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/8 10:36
 */
public class StandardTlsVersion {

    private StandardTlsVersion() {
    }

    /**
     * since 1996
     */
    public static final String SSL_3 = "SSLv3";

    /**
     * since 1999
     */
    public static final String TLS_1 = "TLSv1";

    /**
     * since 2006
     */
    public static final String TLS_1_1 = "TLSv1.1";

    /**
     * since 2008
     */
    public static final String TLS_1_2 = "TLSv1.2";

    /**
     * since 2016
     */
    public static final String TLS_1_3 = "TLSv1.3";

}

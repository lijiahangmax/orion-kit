package com.orion.lang.constant;

/**
 * 标准 TLS 版本
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/8 10:36
 */
public interface StandardTlsVersion {

    String TLS = "TLS";

    /**
     * since 1996
     */
    String SSL_3 = "SSLv3";

    /**
     * since 1999
     */
    String TLS_1 = "TLSv1";

    /**
     * since 2006
     */
    String TLS_1_1 = "TLSv1.1";

    /**
     * since 2008
     */
    String TLS_1_2 = "TLSv1.2";

    /**
     * since 2016
     */
    String TLS_1_3 = "TLSv1.3";

}

package com.orion.lang.define;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * 默认 X509 凭证管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/11 9:56
 */
public class DefaultX509TrustManager implements X509TrustManager {

    public static final X509TrustManager DEFAULT_X509_TRUST_MANAGER = new DefaultX509TrustManager();

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}

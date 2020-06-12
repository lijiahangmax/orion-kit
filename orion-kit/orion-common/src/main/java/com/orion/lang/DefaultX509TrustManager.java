package com.orion.lang;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 默认X509凭证管理器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/11 9:56
 */
public class DefaultX509TrustManager implements X509TrustManager {

    public static final X509TrustManager DEFAULT_TRUST_MANAGER = new DefaultX509TrustManager();

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}

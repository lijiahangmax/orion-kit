package com.orion.http.useragent;

/**
 * 标准 UserAgent
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/5 23:30
 */
public class StandardUserAgent {

    private StandardUserAgent() {
    }

    public static final String USER_AGENT = "User-Agent";

    public static final String OPERA_1 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60";
    public static final String OPERA_2 = "Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50";
    public static final String OPERA_3 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50";
    public static final String FIREFOX_1 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0";
    public static final String FIREFOX_2 = "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10";
    public static final String SAFARI_1 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2";
    public static final String SAFARI_2 = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1";
    public static final String CHROME_1 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
    public static final String CHROME_2 = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11";
    public static final String CHROME_3 = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16";
    public static final String IE_6 = " Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
    public static final String IE_7 = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)";
    public static final String IE_8 = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)";
    public static final String IE_9 = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0";
    public static final String IE_10 = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)";

    // -------------------- mobile --------------------

    public static final String IPHONE_1 = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 10_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5";
    public static final String IPAD_1 = "Mozilla/5.0 (iPad; U; CPU OS 13_1_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5";
    public static final String IPAD_2 = "Mozilla/5.0 (iPad; U; CPU OS 9_3_6 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5";
    public static final String ANDROID_1 = "Mozilla/5.0 (Linux; U; Android 9.0.1; zh-cn; HTC_Wildfire_A3333 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    public static final String ANDROID_2 = "Mozilla/5.0 (Linux; U; Android 10.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    public static final String ANDROID_3 = "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10";
    public static final String ANDROID_4 = "Mozilla/5.0 (Linux; U; Android 10.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13";

    // -------------------- array --------------------

    public static final String[] PC = {OPERA_1, OPERA_2, OPERA_3,
            FIREFOX_1, FIREFOX_2, SAFARI_1, SAFARI_2,
            CHROME_1, CHROME_2, CHROME_3,
            IE_6, IE_7, IE_8, IE_9, IE_10};

    public static final String[] PC_NO_IE = {OPERA_1, OPERA_2, OPERA_3,
            FIREFOX_1, FIREFOX_2, SAFARI_1, SAFARI_2,
            CHROME_1, CHROME_2, CHROME_3};

    public static final String[] MOBILE = {IPHONE_1, IPAD_1, IPAD_2,
            ANDROID_1, ANDROID_2, ANDROID_3, ANDROID_4};

    public static final String[] ALL = {OPERA_1, OPERA_2, OPERA_3,
            FIREFOX_1, FIREFOX_2, SAFARI_1, SAFARI_2,
            CHROME_1, CHROME_2, CHROME_3,
            IE_6, IE_7, IE_8, IE_9, IE_10,
            IPHONE_1, IPAD_1, IPAD_2,
            ANDROID_1, ANDROID_2, ANDROID_3, ANDROID_4};

    public static final String[] ALL_NO_IE = {OPERA_1, OPERA_2, OPERA_3,
            FIREFOX_1, FIREFOX_2, SAFARI_1, SAFARI_2,
            CHROME_1, CHROME_2, CHROME_3,
            IPHONE_1, IPAD_1, IPAD_2,
            ANDROID_1, ANDROID_2, ANDROID_3, ANDROID_4};

}

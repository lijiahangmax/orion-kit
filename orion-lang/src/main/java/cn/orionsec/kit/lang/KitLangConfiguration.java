/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang;

import cn.orionsec.kit.lang.config.KitConfig;

import java.util.regex.Pattern;

/**
 * orion-lang 配置初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/6 18:26
 */
public final class KitLangConfiguration {

    public static final KitLangConfiguration CONFIG = new KitLangConfiguration();

    public final String LIMIT_LIST_DEFAULT_LIMIT = "limit.list.default.limit";

    public final String HTTP_OK_CODE = "http.wrapper.ok.code";

    public final String HTTP_OK_MESSAGE = "http.wrapper.ok.message";

    public final String HTTP_ERROR_CODE = "http.wrapper.error.code";

    public final String HTTP_ERROR_MESSAGE = "http.wrapper.error.message";

    public final String RPC_SUCCESS_CODE = "rpc.wrapper.ok.code";

    public final String RPC_SUCCESS_MESSAGE = "rpc.wrapper.ok.message";

    public final String RPC_ERROR_CODE = "rpc.wrapper.error.code";

    public final String RPC_ERROR_MESSAGE = "rpc.wrapper.error.message";

    public final String PRC_TRACE_PREFIX = "rpc.wrapper.trace.prefix";

    public final String URL_NO_OPERATION = "url.wrapper.no.opt";

    public final String URL_REFRESH = "url.wrapper.refresh";

    public final String URL_REDIRECT = "url.wrapper.redirect";

    public final String PAGE_REQUEST_DEFAULT_LIMIT = "page.request.default.limit";

    public final String PAGER_DEFAULT_LIMIT = "pager.default.limit";

    public final String DATA_GRID_DEFAULT_LIMIT = "data.grid.default.limit";

    public final String XSS_SCRIPT_PATTERN = "xss.script.pattern";

    public final String XSS_STYLE_PATTERN = "xss.style.pattern";

    public final String XSS_HTML_TAG_PATTERN = "xss.html.pattern";

    public final String PATTERN_SPACE_LINE = "pattern.space.line";

    public final String PATTERN_SPACE_POINT = "pattern.space.point";

    public final String PATTERN_PHONE = "pattern.phone";

    public final String PATTERN_EMAIL = "pattern.email";

    public final String PATTERN_HTTP = "pattern.http";

    public final String PATTERN_URI = "pattern.uri";

    public final String PATTERN_INTEGER = "pattern.integer";

    public final String PATTERN_DOUBLE = "pattern.double";

    public final String PATTERN_NUMBER = "pattern.number";

    public final String PATTERN_NUMBER_EXT = "pattern.number.ext";

    public final String PATTERN_IPV4 = "pattern.ipv4";

    public final String PATTERN_IPV6 = "pattern.ipv6";

    public final String PATTERN_MD5 = "pattern.md5";

    public final String PATTERN_WINDOWS_PATH = "pattern.windows.path";

    public final String PATTERN_LINUX_PATH = "pattern.linux.path";

    public final String PATTERN_ZIP_CODE = "pattern.zip.code";

    public final String PATTERN_UTF = "pattern.utf";

    public final String PATTERN_UUID = "pattern.uuid";

    public final String PATTERN_MAC = "pattern.mac";

    public final String PATTERN_HEX = "pattern.hex";

    public final String PATTERN_CREDIT_CODE = "pattern.credit.code";

    public final String PATTERN_ID_CARD = "pattern.id.card";

    public final String PATTERN_PLATE_NUMBER = "pattern.plate.number";

    public final String PATTERN_HEX_COLOR = "pattern.hex.color";

    public final String PATTERN_DATE = "pattern.date";

    public final String PATTERN_WE_CHAT = "pattern.we.chat";

    public final String PATTERN_QQ = "pattern.qq";

    public final String PATTERN_CHINESE = "pattern.chinese";

    public final String PATTERN_USERNAME_1 = "pattern.username.1";

    public final String PATTERN_USERNAME_2 = "pattern.username.2";

    public final String PATTERN_PASSWORD_1 = "pattern.password.1";

    public final String PATTERN_PASSWORD_2 = "pattern.password.2";

    public final String PATTERN_PASSWORD_3 = "pattern.password.3";

    private KitLangConfiguration() {
    }

    static {
        KitConfig.init(CONFIG.LIMIT_LIST_DEFAULT_LIMIT, 10);
    }

    // http wrapper
    static {
        KitConfig.init(CONFIG.HTTP_OK_CODE, 200);
        KitConfig.init(CONFIG.HTTP_OK_MESSAGE, "success");
        KitConfig.init(CONFIG.HTTP_ERROR_CODE, 500);
        KitConfig.init(CONFIG.HTTP_ERROR_MESSAGE, "error");
    }

    // rpc wrapper
    static {
        KitConfig.init(CONFIG.RPC_SUCCESS_CODE, 2000);
        KitConfig.init(CONFIG.RPC_SUCCESS_MESSAGE, "success");
        KitConfig.init(CONFIG.RPC_ERROR_CODE, 5000);
        KitConfig.init(CONFIG.RPC_ERROR_MESSAGE, "error");
        KitConfig.init(CONFIG.PRC_TRACE_PREFIX, "TRACE-");
    }

    // url wrapper
    static {
        KitConfig.init(CONFIG.URL_NO_OPERATION, 1);
        KitConfig.init(CONFIG.URL_REFRESH, 2);
        KitConfig.init(CONFIG.URL_REDIRECT, 3);
    }

    // pager
    static {
        KitConfig.init(CONFIG.PAGE_REQUEST_DEFAULT_LIMIT, 10);
        KitConfig.init(CONFIG.PAGER_DEFAULT_LIMIT, 10);
        KitConfig.init(CONFIG.DATA_GRID_DEFAULT_LIMIT, 10);
    }

    // xss
    static {
        KitConfig.init(CONFIG.XSS_SCRIPT_PATTERN, Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE));
        KitConfig.init(CONFIG.XSS_STYLE_PATTERN, Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE));
        KitConfig.init(CONFIG.XSS_HTML_TAG_PATTERN, Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE));
    }

    // regexp
    static {
        KitConfig.init(CONFIG.PATTERN_SPACE_LINE, Pattern.compile("\\n\\s*\\r"));
        KitConfig.init(CONFIG.PATTERN_SPACE_POINT, Pattern.compile("^\\s*|\\s*$"));
        KitConfig.init(CONFIG.PATTERN_PHONE, Pattern.compile("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$"));
        KitConfig.init(CONFIG.PATTERN_EMAIL, Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Pattern.CASE_INSENSITIVE));
        KitConfig.init(CONFIG.PATTERN_HTTP, Pattern.compile("^(http|https)://([\\w.]+/?)\\S*$"));
        KitConfig.init(CONFIG.PATTERN_URI, Pattern.compile("^[a-zA-z]+://([\\w.]+/?)\\S*$"));
        KitConfig.init(CONFIG.PATTERN_INTEGER, Pattern.compile("^[-+]?[\\d]+$"));
        KitConfig.init(CONFIG.PATTERN_DOUBLE, Pattern.compile("^[-+]?\\d*[.]\\d+$"));
        KitConfig.init(CONFIG.PATTERN_NUMBER, Pattern.compile("^([-+]?\\d*[.]\\d+)$|^([-+]?[\\d]+)$"));
        KitConfig.init(CONFIG.PATTERN_NUMBER_EXT, Pattern.compile("([-+]?\\d*[.]\\d+)|([-+]?[\\d]+)"));
        KitConfig.init(CONFIG.PATTERN_IPV4, Pattern.compile("^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$"));
        KitConfig.init(CONFIG.PATTERN_IPV6, Pattern.compile("^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))$"));
        KitConfig.init(CONFIG.PATTERN_MD5, Pattern.compile("^[a-f0-9]{32}|[A-F0-9]{32}$"));
        KitConfig.init(CONFIG.PATTERN_WINDOWS_PATH, Pattern.compile("^[A-z]:\\\\([^|><?*\":/]*\\\\)*([^|><?*\":/]*)?$|^[A-z]:/([^|><?*\":/]*/)*([^|><?*\":/]*)?$"));
        KitConfig.init(CONFIG.PATTERN_LINUX_PATH, Pattern.compile("^/([^><\"]*/)*([^><\"]*)?$"));
        KitConfig.init(CONFIG.PATTERN_ZIP_CODE, Pattern.compile("[1-9]\\d{5}(?!\\d)"));
        KitConfig.init(CONFIG.PATTERN_UTF, Pattern.compile("^[\u4E00-\u9FFF\\w]+$"));
        KitConfig.init(CONFIG.PATTERN_UUID, Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$"));
        KitConfig.init(CONFIG.PATTERN_MAC, Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE));
        KitConfig.init(CONFIG.PATTERN_HEX, Pattern.compile("^[a-f0-9]+$", Pattern.CASE_INSENSITIVE));
        KitConfig.init(CONFIG.PATTERN_CREDIT_CODE, Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$"));
        KitConfig.init(CONFIG.PATTERN_ID_CARD, Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)"));
        KitConfig.init(CONFIG.PATTERN_PLATE_NUMBER, Pattern.compile(
                "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
                        "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
                        "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$"));
        KitConfig.init(CONFIG.PATTERN_HEX_COLOR, Pattern.compile("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$"));
        KitConfig.init(CONFIG.PATTERN_DATE, Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"));
        KitConfig.init(CONFIG.PATTERN_WE_CHAT, Pattern.compile("^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$"));
        KitConfig.init(CONFIG.PATTERN_QQ, Pattern.compile("^[1-9][0-9]{4,10}$"));
        KitConfig.init(CONFIG.PATTERN_CHINESE, Pattern.compile("[\\u4E00-\\u9FA5]*"));
        KitConfig.init(CONFIG.PATTERN_USERNAME_1, Pattern.compile("^[a-zA-Z0-9]{6,16}$"));
        KitConfig.init(CONFIG.PATTERN_USERNAME_2, Pattern.compile("^[a-zA-Z0-9_]{6,16}$"));
        KitConfig.init(CONFIG.PATTERN_PASSWORD_1, Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$"));
        KitConfig.init(CONFIG.PATTERN_PASSWORD_2, Pattern.compile("^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,20}$"));
        KitConfig.init(CONFIG.PATTERN_PASSWORD_3, Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_=+/\\-?]).{8,20}$"));
    }

}

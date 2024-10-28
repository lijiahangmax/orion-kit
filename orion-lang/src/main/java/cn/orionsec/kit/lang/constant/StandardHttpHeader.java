/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.constant;

/**
 * 标准 http 头
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/9 9:57
 */
public interface StandardHttpHeader {

    // -------------------- request context --------------------

    String HOST = "Host";

    String FROM = "From";

    String REFERER = "Referer";

    String USER_AGENT = "User-Agent";

    String DATE = "Date";

    String LINK = "Link";

    String UPGRADE = "Upgrade";

    String REFERRER_POLICY = "Referrer-Policy";

    // -------------------- response context --------------------

    String ALLOW = "Allow";

    String SERVER = "Server";

    // -------------------- connection management --------------------

    String CONNECTION = "Connection";

    String KEEP_ALIVE = "Keep-Alive";

    // -------------------- content --------------------

    String CONTENT_TYPE = "Content-Type";

    String CONTENT_LENGTH = "Content-Length";

    String CONTENT_LANGUAGE = "Content-Language";

    String CONTENT_ENCODING = "Content-Encoding";

    String CONTENT_LOCATION = "Content-Location";

    String CONTENT_DISPOSITION = "Content-Disposition";

    // -------------------- content negotiation --------------------

    String ACCEPT = "Accept";

    String ACCEPT_ENCODING = "Accept-Encoding";

    String ACCEPT_LANGUAGE = "Accept-Language";

    String ACCEPT_CHARSET = "Accept-Charset";

    String ACCEPT_PATCH = "Accept-Patch";

    // -------------------- control --------------------

    String EXPECT = "Expect";

    String MAX_FORWARDS = "Max-Forwards";

    // -------------------- cookie --------------------

    String COOKIE = "Cookie";

    String SET_COOKIE = "Set-Cookie";

    String SET_COOKIE2 = "Set-Cookie2";

    // -------------------- cache --------------------

    String AGE = "Age";

    String EXPIRES = "Expires";

    String PRAGMA = "Pragma";

    String CACHE_CONTROL = "Cache-Control";

    String CLEAR_SITE_DATA = "Clear-Site-Data";

    String WARNING = "Warning";

    String X_CACHE_LOOKUP = "X-Cache-Lookup";

    // -------------------- authentication --------------------

    String AUTHORIZATION = "Authorization";

    String WWW_AUTHENTICATE = "WWW-Authenticate";

    String PROXY_AUTHENTICATE = "Proxy-Authenticate";

    String PROXY_AUTHORIZATION = "Proxy-Authorization";

    // -------------------- redirect --------------------

    String LOCATION = "Location";

    String REFRESH = "Refresh";

    // -------------------- cors --------------------

    String ORIGIN = "Origin";

    String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    String TIMING_ALLOW_ORIGIN = "Timing-Allow-Origin";

    // -------------------- proxy --------------------

    String VIA = "Via";

    String FORWARDED = "Forwarded";

    String X_FORWARDED_FOR = "X-Forwarded-For";

    String X_FORWARDED_HOST = "X-Forwarded-Host";

    String X_FORWARDED_PROTO = "X-Forwarded-Proto";

    String X_REAL_IP = "X-Real-IP";

    String PROXY_CLIENT_IP = "Proxy-Client-IP";

    String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    // -------------------- conditional --------------------

    String IF_MATCH = "If-Match";

    String IF_NONE_MATCH = "If-None-Match";

    String IF_MODIFIED_SINCE = "If-Modified-Since";

    String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    String ETAG = "ETag";

    String LAST_MODIFIED = "Last-Modified";

    String VARY = "Vary";

    // -------------------- request range --------------------

    String ACCEPT_RANGES = "Accept-Ranges";

    String RANGE = "Range";

    String IF_RANGE = "If-Range";

    String CONTENT_RANGE = "Content-Range";

    // -------------------- transfer encoding --------------------

    String TRANSFER_ENCODING = "Transfer-Encoding";

    String TE = "TE";

    String TRAILER = "Trailer";

    // -------------------- security --------------------

    String EXPECT_CT = "Expect-CT";

    String CROSS_ORIGIN_EMBEDDER_POLICY = "Cross-Origin-Embedder-Policy";

    String CROSS_ORIGIN_OPENER_POLICY = "Cross-Origin-Opener-Policy";

    String CROSS_ORIGIN_RESOURCE_POLICY = "Cross-Origin-Resource-Policy";

    String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

    String CONTENT_SECURITY_POLICY_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    String ORIGIN_ISOLATION = "Origin-Isolation";

    String PERMISSIONS_POLICY = "Permissions-Policy";

    String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";

    String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

    String X_DOWNLOAD_OPTIONS = "X-Download-Options";

    String X_FRAME_OPTIONS = "X-Frame-Options";

    String X_PERMITTED_CROSS_DOMAIN_POLICIES = "X-Permitted-Cross-Domain-Policies";

    String X_POWERED_BY = "X-Powered-By";

    String X_XSS_PROTECTION = "X-XSS-Protection";

    // -------------------- fetch meta --------------------

    String SEC_FETCH_SITE = "Sec-Fetch-Site";

    String SEC_FETCH_MODE = "Sec-Fetch-Mode";

    String SEC_FETCH_USER = "Sec-Fetch-User";

    String SEC_FETCH_DEST = "Sec-Fetch-Dest";

    String SERVICE_WORKER_NAVIGATION_PRELOAD = "Service-Worker-Navigation-Preload";

    // -------------------- server event --------------------

    String LAST_EVENT_ID = "Last-Event-ID";

    String NEL = "NEL";

    String PING_FROM = "Ping-From";

    String PING_TO = "Ping-To";

    String REPORT_TO = "Report-To";

    String PURPOSE = "Purpose";

    // -------------------- client hint --------------------

    String ACCEPT_CH = "Accept-CH";

    String CRITICAL_CH = "Critical-CH";

    String SEC_CH_UA = "Sec-CH-UA";

    String SEC_CH_UA_ARCH = "Sec-CH-UA-Arch";

    String SEC_CH_UA_BITNESS = "Sec-CH-UA-Bitness";

    String SEC_CH_UA_FULL_VERSION_LIST = "Sec-CH-UA-Full-Version-List";

    String SEC_CH_UA_MOBILE = "Sec-CH-UA-Mobile";

    String SEC_CH_UA_MODEL = "Sec-CH-UA-Model";

    String SEC_CH_UA_PLATFORM = "Sec-CH-UA-Platform";

    String SEC_CH_UA_PLATFORM_VERSION = "Sec-CH-UA-Platform-Version";

    String SEC_CH_PREFERS_REDUCED_MOTION = "Sec-CH-Prefers-Reduced-Motion";

    String DOWNLINK = "Downlink";

    String ECT = "ECT";

    String RTT = "RTT";

    String SAVE_DATA = "Save-Data";

    // -------------------- web socket --------------------

    String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";

    String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";

    String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";

    String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";

    // -------------------- others --------------------

    String ACCEPT_PUSH_POLICY = "Accept-Push-Policy";

    String ACCEPT_SIGNATURE = "Accept-Signature";

    String ALT_SVC = "Alt-Svc";

    String EARLY_DATA = "Early-Data";

    String LARGE_ALLOCATION = "Large-Allocation";

    String PUSH_POLICY = "Push-Policy";

    String RETRY_AFTER = "Retry-After";

    String SIGNATURE = "Signature";

    String SIGNED_HEADERS = "Signed-Headers";

    String SERVER_TIMING = "Server-Timing";

    String SERVICE_WORKER_ALLOWED = "Service-Worker-Allowed";

    String SOURCE_MAP = "SourceMap";

    String X_DNS_PREFETCH_CONTROL = "X-DNS-Prefetch-Control";

    String X_FIREFOX_SPDY = "X-Firefox-Spdy";

    String X_PINGBACK = "X-Pingback";

    String X_REQUESTED_WITH = "X-Requested-With";

    String X_REQ_ID = "X-Req-ID";

    String X_ROBOTS_TAG = "X-Robots-Tag";

    String X_UA_COMPATIBLE = "X-UA-Compatible";

}

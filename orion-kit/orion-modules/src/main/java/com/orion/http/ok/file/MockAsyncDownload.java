package com.orion.http.ok.file;

import com.orion.http.common.HttpContent;
import com.orion.http.common.HttpCookie;
import com.orion.http.common.HttpMethod;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.Urls;
import com.orion.utils.io.FileLocks;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Mock 异步下载文件
 * 支持断点下载和进度 前提是服务器支持
 * 支持实时下载速率以及平均下载速率
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/9 14:13
 */
@SuppressWarnings("ALL")
public class MockAsyncDownload {

    /**
     * url
     */
    private String url;

    /**
     * client
     */
    private OkHttpClient client;

    /**
     * 缓冲区大小
     */
    private int buffSize = 1024 * 4;

    /**
     * 下载状态 0未开始 1下载中 2下载成功 3下载失败
     */
    private volatile int downloadStatus;

    /**
     * 开始时间
     */
    private long startDate;

    /**
     * 结束时间
     */
    private long endDate;

    /**
     * body长度
     */
    private long bodyLength;

    /**
     * 当前下载量
     */
    private long nowLength;

    /**
     * 起始位置
     */
    private long startLength;

    /**
     * 实时速率
     */
    private long nowRate;

    /**
     * 开启实时速率
     */
    private boolean openNowRate = false;

    /**
     * 实时速率线程
     */
    private Thread nowRateThread;

    /**
     * 文件锁
     */
    private FileLocks.SuffixFileLock lock;

    /**
     * 跳过的字节数
     */
    private long skipBytes;

    /**
     * 下载文件总大小的头
     */
    private String allSizeHeader = "Attachment-Size";

    /**
     * 断点下载跳过的头
     */
    private String skipSizeHeader = "Attachment-Skip";

    /**
     * method
     */
    private String method = "GET";

    /**
     * 编码格式 GET HEAD 无效
     */
    private String charset;

    /**
     * Content-Type GET HEAD 无效
     */
    private String contentType;

    /**
     * 请求参数
     */
    private Map<String, String> queryParams;

    /**
     * 表单参数
     */
    private Map<String, String> formParts;

    /**
     * 请求参数
     */
    private String queryString;

    /**
     * 请求参数是否编码
     */
    private boolean queryStringEncode;

    /**
     * 请求体
     */
    private byte[] body;

    /**
     * 偏移量
     */
    private int bodyOffset;

    /**
     * 长度
     */
    private int bodyLen;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * cookies
     */
    private List<HttpCookie> cookies;

    /**
     * 忽略的请求头
     */
    private List<String> ignoreHeaders;

    /**
     * tag
     */
    private Object tag;

    /**
     * callback
     */
    private Callback callback;

    /**
     * call
     */
    private Call call;

    /**
     * request
     */
    private Request request;

    /**
     * response
     */
    private Response response;

    /**
     * exception
     */
    private Exception exception;

    public MockAsyncDownload(String url) {
        this.url = url;
    }

    public MockAsyncDownload(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
    }

    public MockAsyncDownload client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public MockAsyncDownload format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public MockAsyncDownload format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * method
     *
     * @param method method GET POST PUT DELETE PATCH
     * @return this
     */
    public MockAsyncDownload method(String method) {
        this.method = method;
        return this;
    }

    /**
     * method
     *
     * @param method method GET POST PUT DELETE PATCH
     * @return this
     */
    public MockAsyncDownload method(HttpMethod method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * charset
     *
     * @param charset charset
     * @return this
     */
    public MockAsyncDownload charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public MockAsyncDownload contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public MockAsyncDownload contentType(HttpContent contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    /**
     * queryParams
     *
     * @param queryParams queryParams
     * @return this
     */
    public MockAsyncDownload queryParams(Map<String, String> queryParams) {
        if (this.queryParams == null) {
            this.queryParams = queryParams;
        } else {
            this.queryParams.putAll(queryParams);
        }
        return this;
    }

    /**
     * queryParams
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public MockAsyncDownload queryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new LinkedHashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * formParts
     *
     * @param formParts formParts
     * @return this
     */
    public MockAsyncDownload formParts(Map<String, String> formParts) {
        if (this.formParts == null) {
            this.formParts = formParts;
        } else {
            this.formParts.putAll(formParts);
        }
        return this;
    }

    /**
     * formParts
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public MockAsyncDownload formPart(String key, String value) {
        if (this.formParts == null) {
            this.formParts = new LinkedHashMap<>();
        }
        this.formParts.put(key, value);
        return this;
    }

    /**
     * queryStringEncode
     *
     * @return this
     */
    public MockAsyncDownload queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * queryStringEncode
     *
     * @param encode ignore
     * @return this
     */
    public MockAsyncDownload queryStringEncode(boolean encode) {
        this.queryStringEncode = encode;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public MockAsyncDownload body(String body) {
        return body(body, false);
    }

    /**
     * body
     *
     * @param body       body
     * @param useCharset 使用charset
     * @return this
     */
    public MockAsyncDownload body(String body, boolean useCharset) {
        if (body == null) {
            return this;
        }
        if (useCharset) {
            try {
                this.body = body.getBytes(this.charset);
            } catch (Exception e) {
                throw Exceptions.unCoding(e);
            }
        } else {
            this.body = body.getBytes();
        }
        this.bodyOffset = 0;
        this.bodyLen = this.body.length;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public MockAsyncDownload body(byte[] body) {
        this.body = body;
        this.bodyOffset = 0;
        this.bodyLen = body.length;
        return this;
    }

    /**
     * body
     *
     * @param body   body
     * @param offset offset
     * @param len    len
     * @return this
     */
    public MockAsyncDownload body(byte[] body, int offset, int len) {
        this.body = body;
        this.bodyOffset = offset;
        this.bodyLen = len;
        return this;
    }

    /**
     * headers
     *
     * @param headers headers
     * @return this
     */
    public MockAsyncDownload headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * header
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public MockAsyncDownload header(String key, String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * UserAgent
     *
     * @param value value
     * @return this
     */
    public MockAsyncDownload userAgent(String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put("User-Agent", value);
        return this;
    }

    /**
     * cookie
     *
     * @param cookie cookie
     * @return this
     */
    public MockAsyncDownload cookie(HttpCookie cookie) {
        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
        return this;
    }

    /**
     * cookies
     *
     * @param cookies cookies
     * @return this
     */
    public MockAsyncDownload cookies(List<HttpCookie> cookies) {
        if (this.cookies == null) {
            this.cookies = cookies;
        } else {
            this.cookies.addAll(cookies);
        }
        return this;
    }

    /**
     * ignoreHeader
     *
     * @param ignoreHeader ignoreHeader
     * @return this
     */
    public MockAsyncDownload ignoreHeader(String ignoreHeader) {
        if (ignoreHeader == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.add(ignoreHeader);
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @param ignoreHeaders ignoreHeaders
     * @return this
     */
    public MockAsyncDownload ignoreHeaders(String... ignoreHeaders) {
        if (ignoreHeaders == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.addAll(Arrays.asList(ignoreHeaders));
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @param ignoreHeaders ignoreHeaders
     * @return this
     */
    public MockAsyncDownload ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public MockAsyncDownload cancel() {
        this.call.cancel();
        return this;
    }

    /**
     * tag
     *
     * @param tag tag
     * @return this
     */
    public MockAsyncDownload tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 设置缓冲区大小
     *
     * @param buffSize 缓冲区大小
     * @return this
     */
    public MockAsyncDownload buffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    /**
     * 设置服务器返回文件大小的头信息
     *
     * @param allSizeHeader 头
     * @return this
     */
    public MockAsyncDownload allSizeHeader(String allSizeHeader) {
        this.allSizeHeader = allSizeHeader;
        return this;
    }

    /**
     * 设置断点下载跳过的头
     *
     * @param skipSizeHeader 头
     * @return this
     */
    public MockAsyncDownload skipSizeHeader(String skipSizeHeader) {
        this.skipSizeHeader = skipSizeHeader;
        return this;
    }

    /**
     * 开启计算实时速率
     *
     * @return this
     */
    public MockAsyncDownload openNowRate() {
        this.openNowRate = true;
        return this;
    }

    /**
     * 下载文件
     *
     * @param file 文件存放路径
     */
    public MockAsyncDownload download(File file) throws IOException {
        Files1.touch(file);
        this.request = this.buildHandler(new Request.Builder()).build();
        return this.download(Files1.openOutputStream(file), true);
    }

    /**
     * 下载文件
     *
     * @param file       文件存放路径
     * @param breakpoint 是否断点续传 前提是服务器支持
     */
    public MockAsyncDownload download(File file, boolean breakpoint) throws IOException {
        Files1.touch(file);
        if (breakpoint) {
            lock = FileLocks.getSuffixFileLock("httpdownload", file);
            if (lock.checkLock()) {
                long size = Files1.getSize(file);
                this.request = this.buildHandler(new Request.Builder()).addHeader(skipSizeHeader, size + "").build();
                this.skipBytes = size;
                return this.download(Files1.openOutputStream(file, true), true);
            } else {
                lock.tryLock();
                this.request = this.buildHandler(new Request.Builder()).build();
                return this.download(Files1.openOutputStream(file), true);
            }
        } else {
            this.request = this.buildHandler(new Request.Builder()).build();
            return this.download(Files1.openOutputStream(file), true);
        }
    }

    /**
     * 下载文件
     *
     * @param out 输出流
     */
    public MockAsyncDownload download(OutputStream out) {
        this.request = this.buildHandler(new Request.Builder()).build();
        return this.download(out, false);
    }

    /**
     * 下载文件
     *
     * @param out       输出流
     * @param skipBytes 跳过字节数, 前提是服务器支持
     */
    public MockAsyncDownload download(OutputStream out, long skipBytes) {
        if (skipBytes <= 0) {
            this.request = this.buildHandler(new Request.Builder()).build();
        } else {
            this.skipBytes = skipBytes;
            this.request = this.buildHandler(new Request.Builder()).addHeader(this.skipSizeHeader, this.skipSizeHeader + "").build();
        }
        return this.download(out, false);
    }

    /**
     * 下载文件 会抛出异常
     *
     * @param out out
     */
    private MockAsyncDownload download(OutputStream out, boolean autoClose) {
        this.callback = new Callback() {

            @Override
            public void onFailure(Call c, IOException e) {
                call = c;
                exception = e;
                downloadStatus = 3;
            }

            @Override
            public void onResponse(Call c, Response r) throws IOException {
                call = c;
                response = r;
                downloadStatus = 1;
                String allSize = r.header(allSizeHeader);
                if (allSize != null && Strings.isNumber(allSize)) {
                    bodyLength = Long.parseLong(allSize);
                }
                InputStream in = null;
                byte[] buf = new byte[buffSize];
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        return;
                    }
                    startDate = System.currentTimeMillis();
                    if (openNowRate) {
                        nowRateThread = new Thread(() -> {
                            while (getProgress() != 1) {
                                long size = nowLength;
                                Threads.sleep(950);
                                nowRate = nowLength - size;
                            }
                        });
                        nowRateThread.setDaemon(true);
                        nowRateThread.start();
                    }
                    in = body.byteStream();
                    int len;
                    nowLength = skipBytes;
                    startLength = skipBytes;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                        nowLength += len;
                    }
                    downloadStatus = 2;
                    if (lock != null) {
                        lock.unLock();
                    }
                } catch (Exception e) {
                    exception = e;
                    downloadStatus = 3;
                    throw e;
                } finally {
                    endDate = System.currentTimeMillis();
                    Streams.close(in);
                    if (autoClose) {
                        Streams.close(out);
                    }
                }
            }

        };
        client.newCall(this.request).enqueue(this.callback);
        return this;
    }

    /**
     * 添加以及忽略头
     *
     * @param builder builder
     * @return builder
     */
    private Request.Builder buildHandler(Request.Builder builder) {
        this.method = this.method.toUpperCase();
        HttpMethod.validMethod(this.method, true);
        if (this.tag != null) {
            builder.tag(tag);
        }
        if (this.headers != null) {
            this.headers.forEach(builder::addHeader);
        }
        if (this.cookies != null) {
            this.cookies.forEach(c -> builder.addHeader("Cookie", c.toString()));
        }
        if (this.ignoreHeaders != null) {
            this.ignoreHeaders.forEach(builder::removeHeader);
        }
        boolean show = false;
        if (HttpMethod.GET.getMethod().equals(this.method) || HttpMethod.HEAD.getMethod().equals(this.method)) {
            show = true;
            if (this.formParts != null) {
                if (this.queryParams == null) {
                    this.queryParams = new LinkedHashMap<>();
                }
                this.queryParams.putAll(this.formParts);
            }
        }
        if (this.queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
        builder.url(this.url);
        if (HttpMethod.GET.getMethod().equals(this.method)) {
            builder.get();
        } else if (HttpMethod.HEAD.getMethod().equals(this.method)) {
            builder.head();
        } else {
            if (this.contentType == null) {
                this.contentType = HttpContent.TEXT_PLAIN.getType();
            }
            if (this.charset == null) {
                if (this.body != null) {
                    builder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), this.body, this.bodyOffset, this.bodyLen));
                } else if (this.formParts != null && !show) {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    this.formParts.forEach(formBuilder::add);
                    builder.method(this.method, formBuilder.build());
                } else {
                    builder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), ""));
                }
            } else {
                if (this.body != null) {
                    builder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), this.body, this.bodyOffset, this.bodyLen));
                } else if (this.formParts != null && !show) {
                    FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName(this.charset));
                    this.formParts.forEach(formBuilder::addEncoded);
                    builder.method(this.method, formBuilder.build());
                } else {
                    builder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), ""));
                }
            }
        }
        return builder;
    }

    public String getUrl() {
        return url;
    }

    public int getBuffSize() {
        return buffSize;
    }

    /**
     * 获取当前下载进度
     *
     * @return 当前进度 0 ~ 1
     */
    public double getProgress() {
        if (nowLength == 0 || bodyLength == 0) {
            return 0;
        }
        return (double) nowLength / (double) bodyLength;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public Callback getCallback() {
        return callback;
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    /**
     * 获取平均速度
     *
     * @return 平均速度
     */
    public double getAvgRate() {
        if (nowLength - startLength == 0) {
            return 0;
        }
        long useDate = endDate;
        if (endDate == 0) {
            useDate = System.currentTimeMillis();
        }
        double used = useDate - startDate;
        return ((nowLength - startLength) / used) * 1000;
    }

    /**
     * 获取当前速度
     *
     * @return 当前速度
     */
    public long getNowRate() {
        return nowRate;
    }

    /**
     * 是否下载完毕
     *
     * @return true完毕
     */
    public boolean isDone() {
        return this.downloadStatus == 2 || this.downloadStatus == 3;
    }

    /**
     * 是否下载失败
     *
     * @return true失败
     */
    public boolean isError() {
        return this.downloadStatus == 3;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getBodyLength() {
        return bodyLength;
    }

    public long getNowLength() {
        return nowLength;
    }

    public long getStartLength() {
        return startLength;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getIgnoreHeaders() {
        return ignoreHeaders;
    }

    public Object getTag() {
        return tag;
    }

    public long getSkipBytes() {
        return skipBytes;
    }

    public String getAllSizeHeader() {
        return allSizeHeader;
    }

    public String getSkipSizeHeader() {
        return skipSizeHeader;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getFormParts() {
        return formParts;
    }

    public String getQueryString() {
        return queryString;
    }

    public boolean isQueryStringEncode() {
        return queryStringEncode;
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyOffset() {
        return bodyOffset;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public String getMethod() {
        return method;
    }

    public String getCharset() {
        return charset;
    }

    public String getContentType() {
        return contentType;
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return url;
    }

}

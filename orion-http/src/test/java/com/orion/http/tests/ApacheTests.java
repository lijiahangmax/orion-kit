package com.orion.http.tests;

import com.orion.http.apache.ApacheRequest;
import com.orion.http.apache.file.ApacheDownload;
import com.orion.http.apache.file.ApacheUpload;
import com.orion.http.support.HttpCookie;
import com.orion.http.support.HttpMethod;
import com.orion.http.support.HttpUploadPart;
import com.orion.http.useragent.UserAgentGenerators;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/4 18:42
 */
public class ApacheTests {

    private static final String REQ = "http://localhost:8080/http/req";
    private static final String REQ1 = "http://localhost:8080/http/req1";
    private static final String SLEEP = "http://localhost:8080/http/sleep";
    private static final String TEXT = "http://localhost:8080/http/text";
    private static final String HTML = "http://localhost:8080/http/html";
    private static final String UP = "http://localhost:8080/http/upload";
    private static final String DOWN = "http://localhost:8080/http/download";
    private static final String NULL = "http://localhost:8080/http/null";
    private static final String TIMEOUT = "http://localhost:8081/http/download";

    @Test
    public void testReq1() {
        System.out.println(new ApacheRequest(REQ).await().getBodyString());
    }

    @Test
    public void testReq2() {
        ApacheRequest req = new ApacheRequest(REQ);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("身体哦");
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq3() {
        ApacheRequest req = new ApacheRequest(REQ1);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.OPTIONS);
        req.queryParam("name", "12");
        req.header("A", "B");
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq4() {
        ApacheRequest req = new ApacheRequest(REQ1);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.POST);
        req.formPart("name", "whh");
        req.formPart("age", "18");
        req.formPart("sex", "女");
        req.cookie(new HttpCookie().addValue("uid", "1"));
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq5() {
        ApacheRequest req = new ApacheRequest(SLEEP);
        req.method(HttpMethod.POST);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq6() {
        ApacheRequest req = new ApacheRequest(TEXT);
        req.method(HttpMethod.DELETE);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq7() {
        ApacheRequest req = new ApacheRequest(HTML);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq8() {
        ApacheUpload req = new ApacheUpload(UP);
        req.method(HttpMethod.PATCH);
        req.part(new HttpUploadPart("file", "文件内容".getBytes(), ".txt"));
        System.out.println(req.await());
        System.out.println(req.getUseDate());
    }

    @Test
    public void testReq9() throws IOException {
        ApacheRequest req = new ApacheRequest(DOWN + "?name={}");
        req.queryStringEncode(false);
        req.method("POST");
        req.format("文本");
        ApacheDownload d = new ApacheDownload(req);
        d.download(System.out);
    }

    @Test
    public void testReq10() {
        ApacheRequest req = new ApacheRequest(NULL);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq11() {
        ApacheRequest req = new ApacheRequest(TIMEOUT);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

}

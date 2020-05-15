package com.orion.mock.file;

import com.orion.mock.MockClient;
import com.orion.utils.collect.Lists;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock 上传文件
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 14:09
 */
public class MockUpload {

    /**
     * url
     */
    private String url;

    /**
     * client
     */
    private OkHttpClient client = MockClient.getClient();

    /**
     * 是否执行完毕
     */
    private boolean done;

    /**
     * 开始时间
     */
    private long startDate;

    /**
     * 结束时间
     */
    private long endDate;

    /**
     * 请求参数
     */
    private Map<String, String> queryParams;

    private Request request;

    private Call call;

    private Response response;

    private Exception exception;

    public MockUpload(String url) {
        this.url = url;
    }

    /**
     * 添加表单参数
     *
     * @param queryParams 参数
     * @return this
     */
    public MockUpload queryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    /**
     * 添加表单参数
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public MockUpload queryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new HashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * 上传单文件
     *
     * @param part ignore
     */
    public void upload(MockUploadPart part) {
        upload(Lists.of(part));
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     */
    public void upload(List<MockUploadPart> parts) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (this.queryParams != null) {
            for (Map.Entry<String, String> entry : this.queryParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        for (MockUploadPart part : parts) {
            if (part == null) {
                continue;
            }
            RequestBody body = null;
            if (part.getFile() != null) {
                body = RequestBody.create(part.getContentType() == null ? null : MediaType.parse(part.getContentType()), part.getFile());
            } else if (part.getBytes() != null) {
                body = RequestBody.create(part.getContentType() == null ? null : MediaType.parse(part.getContentType()), part.getBytes(), part.getOff(), part.getLen());
            }
            if (body != null) {
                builder.addFormDataPart(part.getKey(), part.getFileName(), body);
            }
        }
        this.request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        try {
            this.startDate = System.currentTimeMillis();
            this.call = this.client.newCall(this.request);
            this.response = this.call.execute();
        } catch (IOException e) {
            this.exception = e;
        } finally {
            this.endDate = System.currentTimeMillis();
            this.done = true;
        }
    }

    public String getUrl() {
        return url;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isError() {
        return exception != null;
    }

    public boolean isSuccess() {
        if (this.response == null) {
            return false;
        }
        return this.response.isSuccessful();
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getUseDate() {
        if (startDate == 0) {
            return 0;
        }
        if (endDate == 0) {
            return System.currentTimeMillis() - startDate;
        } else {
            return endDate - startDate;
        }
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

}

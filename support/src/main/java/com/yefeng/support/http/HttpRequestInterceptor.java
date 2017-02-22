package com.yefeng.support.http;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yefeng on 20/01/2017.
 * add request header and common params
 */

public class HttpRequestInterceptor implements Interceptor {

    private HttpRequestInterceptor() {
    }

    private static class SingletonHolder {
        private static final HttpRequestInterceptor INSTANCE = new HttpRequestInterceptor();
    }

    public static HttpRequestInterceptor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //add some common parameters
        Request originalRequest = chain.request();
        HttpUrl.Builder requestBuilder = originalRequest.url().newBuilder();
        if (HttpCommonParams.getInstance().getParams() != null) {
            for (Map.Entry<String, String> entry : HttpCommonParams.getInstance().getParams().entrySet()) {
                requestBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        //add headers
        Request.Builder builder = chain.request().newBuilder();
        if (HttpCommonParams.getInstance().getHeaders() != null) {
            for (Map.Entry<String, String> entry : HttpCommonParams.getInstance().getHeaders().entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        builder.url(requestBuilder.build());
        return chain.proceed(builder.build());
    }
}

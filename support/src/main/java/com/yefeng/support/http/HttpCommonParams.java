package com.yefeng.support.http;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yefeng on 20/01/2017.
 * the common params which will set to all request
 */

public class HttpCommonParams {
    private static Map<String, String> PARAMS;
    private static Map<String, String> HEADERS;

    private HttpCommonParams() {
    }

    private static class SingletonHolder {
        private static final HttpCommonParams INSTANCE = new HttpCommonParams();
    }

    public static HttpCommonParams getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * get params
     *
     * @return common params
     */
    public Map<String, String> getParams() {
        return PARAMS;
    }

    /**
     * addParam params to common params
     *
     * @param key   params key
     * @param value params value
     * @return common params
     */
    public Map<String, String> addParam(@NonNull String key, @NonNull String value) {
        if (null == PARAMS) {
            PARAMS = new HashMap<>();
        }
        PARAMS.put(key, value);
        return PARAMS;
    }

    /**
     * clearParam params
     */
    public void clearParam() {
        if (null != PARAMS) {
            PARAMS.clear();
        }
    }

    /**
     * put header
     */
    public Map<String, String> addHeader(@NonNull String key, @NonNull String value) {
        if (null == HEADERS) {
            HEADERS = new HashMap<>();
        }
        HEADERS.put(key, value);
        return HEADERS;
    }

    /**
     * get headers
     */
    public Map<String, String> getHeaders() {
        return HEADERS;
    }

    /**
     * clear headers
     */
    public void clearHeaders() {
        if (null != HEADERS) {
            HEADERS.clear();
        }
    }

    public static class Builder {

        public Builder addParam(String key, String value) {
            HttpCommonParams.getInstance().addParam(key, value);
            return this;
        }

        public Builder addHeader(String key, String value) {
            HttpCommonParams.getInstance().addHeader(key, value);
            return this;
        }

        public HttpCommonParams build() {
            return HttpCommonParams.getInstance();
        }
    }
}

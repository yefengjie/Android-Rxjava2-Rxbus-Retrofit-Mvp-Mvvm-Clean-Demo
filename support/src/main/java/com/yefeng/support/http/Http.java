package com.yefeng.support.http;

import android.text.TextUtils;

import timber.log.Timber;

/**
 * Created by yefeng on 19/01/2017.
 * http manager
 */

public class Http {
    /**
     * local host for test
     */
    public static final String DEFAULT_HOST = "http://192.168.20.105:8000";
    public static final String DEFAULT_API_VERSION = "/api/v1/";
    private static final int DEFAULT_TIME_OUT = 30;
    private static String CURRENT_HOST;
    /**
     * connection time out
     */
    private static int TIME_OUT = DEFAULT_TIME_OUT;

    private static String localHost() {
        return DEFAULT_HOST + DEFAULT_API_VERSION;
    }

    public static void setHost(String host, String apiVersion) {
        CURRENT_HOST = host + apiVersion;
        Timber.d("set host:%s", CURRENT_HOST);
    }

    public static String host() {
        if (TextUtils.isEmpty(CURRENT_HOST)) {
            CURRENT_HOST = localHost();
        }
        Timber.d("host: %s", CURRENT_HOST);
        return CURRENT_HOST;
    }

    public static void setTimeOut(int timeOut) {
        TIME_OUT = timeOut;
        Timber.d("set timeout: %s", TIME_OUT);
    }

    public static int getTimeOut() {
        return TIME_OUT;
    }
}

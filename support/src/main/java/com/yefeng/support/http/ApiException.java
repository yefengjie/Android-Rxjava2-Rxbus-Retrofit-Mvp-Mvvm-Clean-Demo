package com.yefeng.support.http;

import timber.log.Timber;

/**
 * Created by yefeng on 20/01/2017.
 * api exception from server, handle error code
 */

public class ApiException extends RuntimeException {

    private int code;

    public ApiException(int code, String msg) {
        super(msg + "");
        this.code = code;
        Timber.e("ApiException: " + code + "_" + msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

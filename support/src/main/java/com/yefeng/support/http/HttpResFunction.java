package com.yefeng.support.http;

import com.yefeng.support.DebugLog;

import io.reactivex.functions.Function;

/**
 * Created by yefeng on 06/02/2017.
 */

public class HttpResFunction<T> implements Function<HttpRes<T>, T> {
    @Override
    public T apply(HttpRes<T> tHttpRes) throws Exception {
        DebugLog.logThread("HttpResFunc.call()", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (tHttpRes.getCode() != 0) {
            throw new ApiException(tHttpRes.getCode(), tHttpRes.getMsg());
        }
        return tHttpRes.getData();
    }
}

package com.yefeng.support.http;

import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by yefeng on 06/02/2017.
 * handle http res
 */

public class HttpResFunction<T> implements Function<HttpRes<T>, T> {
    @Override
    public T apply(HttpRes<T> tHttpRes) throws Exception {
        Timber.d("method: %s, thread: %s_%s", "apply()", Thread.currentThread().getName(), Thread.currentThread().getId());
        Timber.d("http res: %s", tHttpRes.toString());
        if (tHttpRes.getCode() != 0) {
            throw new ApiException(tHttpRes.getCode(), tHttpRes.getMsg());
        }
        return tHttpRes.getData();
    }
}

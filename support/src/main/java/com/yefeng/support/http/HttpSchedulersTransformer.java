package com.yefeng.support.http;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yefeng on 07/02/2017.
 */

/**
 * 后台线程执行同步，主线程执行异步操作
 * 并且拦截所有错误，不让app崩溃
 */
public class HttpSchedulersTransformer<T> implements FlowableTransformer<T, T> {
    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(Flowable.<T>empty());
    }
}

package com.yefeng.support.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by yefeng on 16/02/2017.
 * rx bus
 */

public class RxBus {

    private RxBus() {
    }

    private static class SingletonHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    public static RxBus getBus() {
        return RxBus.SingletonHolder.INSTANCE;
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        Timber.d("send: " + o.toString());
        bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}

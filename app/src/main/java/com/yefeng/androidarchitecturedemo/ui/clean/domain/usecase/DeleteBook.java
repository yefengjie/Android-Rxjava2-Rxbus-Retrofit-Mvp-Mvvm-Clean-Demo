package com.yefeng.androidarchitecturedemo.ui.clean.domain.usecase;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.support.base.UseCase;
import com.yefeng.support.http.HttpSchedulersTransformer;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by yefeng on 21/02/2017.
 */

public class DeleteBook extends UseCase<DeleteBook.RequestValues, DeleteBook.ResponseValue> {

    private final BookRepository mBookRepository;

    public DeleteBook(@NonNull BookRepository bookRepository) {
        mBookRepository = bookRepository;
    }

    @Override
    protected Disposable executeUseCase(RequestValues requestValues) {
        return mBookRepository.deleteBookRx(requestValues.getId())
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        Timber.d("doOnSubscribe()");
                        getUseCaseCallback().onStart();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Timber.d("onNext()");
                        Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        getUseCaseCallback().onSuccess(null);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        Timber.e(throwable);
                        getUseCaseCallback().onError(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        Timber.d("onComplete()");
                        getUseCaseCallback().onComplete();
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String mId;

        public RequestValues(@NonNull String id) {
            this.mId = id;
        }

        public String getId() {
            return mId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
    }
}

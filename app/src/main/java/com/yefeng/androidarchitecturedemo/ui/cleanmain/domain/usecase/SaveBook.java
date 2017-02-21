package com.yefeng.androidarchitecturedemo.ui.cleanmain.domain.usecase;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
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

public class SaveBook extends UseCase<SaveBook.RequestValues, SaveBook.ResponseValue> {

    private final BookRepository mBookRepository;

    public SaveBook(@NonNull BookRepository bookRepository) {
        mBookRepository = bookRepository;
    }


    @Override
    protected Disposable executeUseCase(RequestValues requestValues) {
        return mBookRepository.saveBookRx(requestValues.getBook())
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
                }, throwable -> {
                    Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                    Timber.e(throwable);
                    getUseCaseCallback().onError(throwable.getMessage());
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
        private final Book mBook;

        public RequestValues(Book book) {
            mBook = book;
        }

        public Book getBook() {
            return mBook;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
    }
}

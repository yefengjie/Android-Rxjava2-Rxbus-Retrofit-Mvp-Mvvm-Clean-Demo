package com.yefeng.androidarchitecturedemo.ui.clean.domain.usecase;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.support.base.UseCase;
import com.yefeng.support.http.HttpSchedulersTransformer;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by yefeng on 21/02/2017.
 */

public class GetBooks extends UseCase<GetBooks.RequestValues, GetBooks.ResponseValue> {

    private final BookRepository mBookRepository;

    public GetBooks(@NonNull BookRepository bookRepository) {
        mBookRepository = bookRepository;
    }

    @Override
    protected Disposable executeUseCase(RequestValues requestValues) {
        return mBookRepository.getBooks(requestValues.isForceUpdate())
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        Timber.d("doOnSubscribe()");
                        getUseCaseCallback().onStart();
                    }
                })
                .subscribe(new Consumer<List<Book>>() {
                    @Override
                    public void accept(List<Book> books) throws Exception {
                        Timber.d("onNext()");
                        Timber.e(books.toString());
                        Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        getUseCaseCallback().onSuccess(new ResponseValue(books));
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
        private final boolean mForceUpdate;

        public RequestValues(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final List<Book> mBooks;

        ResponseValue(@NonNull List<Book> books) {
            mBooks = books;
        }

        public List<Book> getBooks() {
            return mBooks;
        }
    }
}

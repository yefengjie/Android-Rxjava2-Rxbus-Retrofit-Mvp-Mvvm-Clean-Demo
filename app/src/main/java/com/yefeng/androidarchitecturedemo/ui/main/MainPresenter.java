package com.yefeng.androidarchitecturedemo.ui.main;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.support.http.HttpSchedulersTransformer;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by yefeng on 14/02/2017.
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates
 * the UI as required.
 */

public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private final BookRepository mBookRepository;

    @NonNull
    private final MainContract.View mMainView;


    public MainPresenter(@NonNull BookRepository bookRepository, @NonNull MainContract.View mainView) {
        mBookRepository = bookRepository;
        mMainView = mainView;
        mMainView.setPresenter(this);
    }


    @Override
    public void saveBook(@NonNull Book book) {

    }

    @Override
    public void deleteBook(@NonNull String id) {

    }

    @Override
    public void loadBooks(boolean forceUpdate) {
        mBookRepository.getBooks()
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(subscription -> {
                    Timber.d("doOnSubscribe()");
                    mMainView.onLoading();
                })
                .subscribe(books -> {
                    Timber.d("onNext()");
                    Timber.e(books.toString());
                    mMainView.onLoadOk(new ArrayList<>(books));
                    Timber.d("method: %s, thread: %s_%s", "test()", Thread.currentThread().getName(), Thread.currentThread().getId());
                }, throwable -> {
                    Timber.e(throwable);
                    mMainView.onLoadError(throwable.getMessage());
                }, () -> {
                    Timber.d("onComplete()");
                    mMainView.onLoadFinish();
                });
    }

    @Override
    public void subscribe() {
        loadBooks(true);
    }

    @Override
    public void unsubscribe() {

    }
}

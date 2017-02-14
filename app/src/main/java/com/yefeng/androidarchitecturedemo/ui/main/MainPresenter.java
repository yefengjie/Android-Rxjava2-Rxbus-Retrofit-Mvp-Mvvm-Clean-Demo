package com.yefeng.androidarchitecturedemo.ui.main;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.support.http.HttpSchedulersTransformer;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
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

    private CompositeDisposable mCompositeDisposable;


    public MainPresenter(@NonNull BookRepository bookRepository, @NonNull MainContract.View mainView) {
        mBookRepository = bookRepository;
        mMainView = mainView;
        mMainView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }


    @Override
    public void saveBook(@NonNull Book book) {

    }

    @Override
    public void deleteBook(@NonNull String id) {

    }

    /**
     * load books
     *
     * @param forceUpdate force update data.
     *                    if true, we need to load from net.
     *                    if false, load from memory,
     *                    if memory is empty, load from db,
     *                    if db is empty, load from net
     */
    @Override
    public void loadBooks(boolean forceUpdate) {
        mCompositeDisposable.add(
                mBookRepository.getBooks(forceUpdate)
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
                        }));
    }

    @Override
    public void subscribe() {
        loadBooks(false);
    }

    @Override
    public void unSubscribe() {
        mMainView.onLoadFinish();
        mCompositeDisposable.clear();
    }
}

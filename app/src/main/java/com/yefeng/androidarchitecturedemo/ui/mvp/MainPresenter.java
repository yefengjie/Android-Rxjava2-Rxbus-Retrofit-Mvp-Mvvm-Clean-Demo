package com.yefeng.androidarchitecturedemo.ui.mvp;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.support.http.HttpSchedulersTransformer;
import com.yefeng.support.rxbus.RxBus;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
        mCompositeDisposable.add(
                mBookRepository.saveBookRx(book)
                        .compose(new HttpSchedulersTransformer<>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mMainView.onAction();
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Timber.d("onNext()");
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                mMainView.onActionOk();
                            }
                        }, throwable -> {
                            Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            Timber.e(throwable);
                            mMainView.onActionError(throwable.getMessage());
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mMainView.onActionFinish();
                            }
                        }));
    }

    @Override
    public void deleteBook(@NonNull String id) {
        mCompositeDisposable.add(
                mBookRepository.deleteBookRx(id)
                        .compose(new HttpSchedulersTransformer<>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mMainView.onAction();
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Timber.d("onNext()");
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                mMainView.onActionOk();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.e(throwable);
                                mMainView.onActionError(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mMainView.onActionFinish();
                            }
                        }));
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
        Timber.d("loadBooks: %s", forceUpdate);
        mCompositeDisposable.add(
                mBookRepository.getBooks(forceUpdate)
                        .compose(new HttpSchedulersTransformer<>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mMainView.onLoading();
                            }
                        })
                        .subscribe(new Consumer<List<Book>>() {
                            @Override
                            public void accept(List<Book> books) throws Exception {
                                Timber.d("onNext()");
                                Timber.e(books.toString());
                                mMainView.onLoadOk(new ArrayList<>(books));
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            }
                        }, throwable -> {
                            Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                            Timber.e(throwable);
                            mMainView.onLoadError(throwable.getMessage());
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mMainView.onLoadFinish();
                            }
                        }));
    }

    @Override
    public void subscribe() {
        initRxBus();
        loadBooks(false);
    }

    private void initRxBus() {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(RxBus.getBus()
                .toObserverable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Events.ReloadEvent) {
                        loadBooks(((Events.ReloadEvent) o).mForceUpdate);
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        mMainView.onLoadFinish();
        mCompositeDisposable.clear();
        mMainView.onActionFinish();
    }
}

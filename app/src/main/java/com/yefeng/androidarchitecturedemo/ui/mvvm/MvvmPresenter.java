package com.yefeng.androidarchitecturedemo.ui.mvvm;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.ui.mvp.Events;
import com.yefeng.androidarchitecturedemo.ui.mvp.MainContract;
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
 * Created by yefeng on 22/02/2017.
 */

public class MvvmPresenter implements MainContract.Presenter {

    private final BookRepository mBookRepositoty;
    private final MainContract.View mMvvnView;
    private CompositeDisposable mCompositeDisposable;

    public MvvmPresenter(BookRepository bookRepository, MainContract.View mvvmActivity) {
        mBookRepositoty = bookRepository;
        mMvvnView = mvvmActivity;
    }

    @Override
    public void saveBook(@NonNull Book book) {

    }

    @Override
    public void deleteBook(@NonNull String id) {
        mCompositeDisposable.add(
                mBookRepositoty.deleteBookRx(id)
                        .compose(new HttpSchedulersTransformer<>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mMvvnView.onAction();
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Timber.d("onNext()");
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                mMvvnView.onActionOk();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.e(throwable);
                                mMvvnView.onActionError(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mMvvnView.onActionFinish();
                            }
                        }));
    }

    @Override
    public void loadBooks(boolean forceUpdate) {
        mCompositeDisposable.add(
                mBookRepositoty.getBooks(forceUpdate)
                        .compose(new HttpSchedulersTransformer<>())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("doOnSubscribe()");
                                mMvvnView.onLoading();
                            }
                        })
                        .subscribe(new Consumer<List<Book>>() {
                            @Override
                            public void accept(List<Book> books) throws Exception {
                                Timber.d("onNext()");
                                Timber.e(books.toString());
                                Timber.d("method: %s, thread: %s_%s", "onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                mMvvnView.onLoadOk(new ArrayList<>(books));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.e(throwable);
                                mMvvnView.onLoadError(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("method: %s, thread: %s_%s", "onComplete()", Thread.currentThread().getName(), Thread.currentThread().getId());
                                Timber.d("onComplete()");
                                mMvvnView.onLoadFinish();
                            }
                        }));
    }

    @Override
    public void subscribe() {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        initRxBus();
        loadBooks(false);
    }

    private void initRxBus() {
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
        mMvvnView.onActionOk();
        mMvvnView.onLoadFinish();
        mCompositeDisposable.clear();
    }
}

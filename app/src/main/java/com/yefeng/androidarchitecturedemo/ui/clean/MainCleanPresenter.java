package com.yefeng.androidarchitecturedemo.ui.clean;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.ui.clean.domain.usecase.DeleteBook;
import com.yefeng.androidarchitecturedemo.ui.clean.domain.usecase.GetBooks;
import com.yefeng.androidarchitecturedemo.ui.clean.domain.usecase.SaveBook;
import com.yefeng.androidarchitecturedemo.ui.mvp.Events;
import com.yefeng.androidarchitecturedemo.ui.mvp.MainContract;
import com.yefeng.support.base.UseCase;
import com.yefeng.support.rxbus.RxBus;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yefeng on 21/02/2017.
 */

public class MainCleanPresenter implements MainContract.Presenter {

    private final GetBooks mGetBooks;
    private final SaveBook mSaveBook;
    private final DeleteBook mDeleteBook;
    private final MainContract.View mMainView;
    private CompositeDisposable mCompositeDisposable;

    public MainCleanPresenter(@NonNull GetBooks getBooks, @NonNull SaveBook saveBook, @NonNull DeleteBook deleteBook, @NonNull MainContract.View mainView) {
        this.mGetBooks = getBooks;
        this.mSaveBook = saveBook;
        this.mDeleteBook = deleteBook;
        this.mMainView = mainView;
        mMainView.setPresenter(this);
    }

    @Override
    public void saveBook(@NonNull Book book) {
        Timber.d("saveBook: %s", book.toString());
        mSaveBook.setRequestValues(new SaveBook.RequestValues(book));
        mSaveBook.setUseCaseCallback(new UseCase.UseCaseCallback<SaveBook.ResponseValue>() {
            @Override
            public void onStart() {
                mMainView.onAction();
            }

            @Override
            public void onSuccess(SaveBook.ResponseValue response) {
                mMainView.onActionOk();
            }

            @Override
            public void onError(String errorMsg) {
                mMainView.onActionError(errorMsg);
            }

            @Override
            public void onComplete() {
                mMainView.onActionFinish();
            }
        });
        mCompositeDisposable.add(mSaveBook.run());
    }

    @Override
    public void deleteBook(@NonNull String id) {
        Timber.d("deleteBook: %s", id);
        mDeleteBook.setRequestValues(new DeleteBook.RequestValues(id));
        mDeleteBook.setUseCaseCallback(new UseCase.UseCaseCallback<DeleteBook.ResponseValue>() {
            @Override
            public void onStart() {
                mMainView.onAction();
            }

            @Override
            public void onSuccess(DeleteBook.ResponseValue response) {
                mMainView.onActionOk();
            }

            @Override
            public void onError(String errorMsg) {
                mMainView.onActionError(errorMsg);
            }

            @Override
            public void onComplete() {
                mMainView.onActionFinish();
            }
        });
        mCompositeDisposable.add(mDeleteBook.run());
    }

    @Override
    public void loadBooks(boolean forceUpdate) {
        Timber.d("loadBooks: %s", forceUpdate);
        mGetBooks.setRequestValues(new GetBooks.RequestValues(forceUpdate));
        mGetBooks.setUseCaseCallback(new UseCase.UseCaseCallback<GetBooks.ResponseValue>() {
            @Override
            public void onStart() {
                mMainView.onLoading();
            }

            @Override
            public void onSuccess(GetBooks.ResponseValue response) {
                mMainView.onLoadOk(new ArrayList<>(response.getBooks()));
            }

            @Override
            public void onError(String errorMsg) {
                mMainView.onLoadError(errorMsg);
            }

            @Override
            public void onComplete() {
                mMainView.onLoadFinish();
            }
        });
        mCompositeDisposable.add(mGetBooks.run());
    }

    @Override
    public void subscribe() {
        loadBooks(false);
        initRxBus();
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
        mCompositeDisposable.clear();
        mMainView.onLoadFinish();
        mMainView.onActionFinish();
    }
}

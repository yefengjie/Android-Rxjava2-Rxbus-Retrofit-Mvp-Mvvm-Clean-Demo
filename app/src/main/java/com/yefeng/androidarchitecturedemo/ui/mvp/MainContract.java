package com.yefeng.androidarchitecturedemo.ui.mvp;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.support.base.BasePresenter;
import com.yefeng.support.base.BaseView;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public class MainContract {
    public interface Presenter extends BasePresenter {
        void saveBook(@NonNull Book book);

        void deleteBook(@NonNull String id);

        void loadBooks(boolean forceUpdate);
    }

    public interface View extends BaseView<Presenter> {
        void onLoading();

        void onLoadOk(ArrayList<Book> books);

        void onLoadError(String msg);

        void onLoadFinish();

        void addBook(android.view.View view);

        void deleteBook(@NonNull String id);

        void onAction();

        void onActionOk();

        void onActionError(String msg);

        void onActionFinish();
    }
}

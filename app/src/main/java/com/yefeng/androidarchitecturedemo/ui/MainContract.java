package com.yefeng.androidarchitecturedemo.ui;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.support.base.BasePresenter;
import com.yefeng.support.base.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public class MainContract {
    interface Presenter extends BasePresenter {
        List<Book> getBooks();

        Book getBook(@NonNull String id);

        Void saveBook(@NonNull Book book);

        Void deleteBooks();

        Void deleteBook(@NonNull String id);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();
    }
}

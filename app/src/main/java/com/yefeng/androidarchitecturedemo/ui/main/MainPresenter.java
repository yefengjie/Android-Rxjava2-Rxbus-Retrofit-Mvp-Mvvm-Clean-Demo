package com.yefeng.androidarchitecturedemo.ui.main;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;

import java.util.List;

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
    public List<Book> getBooks() {
        return null;
    }

    @Override
    public Book getBook(@NonNull String id) {
        return null;
    }

    @Override
    public Void saveBook(@NonNull Book book) {
        return null;
    }

    @Override
    public Void deleteBooks() {
        return null;
    }

    @Override
    public Void deleteBook(@NonNull String id) {
        return null;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}

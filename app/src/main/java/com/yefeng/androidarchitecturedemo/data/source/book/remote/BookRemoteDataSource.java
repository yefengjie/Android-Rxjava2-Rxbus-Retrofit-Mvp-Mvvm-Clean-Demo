package com.yefeng.androidarchitecturedemo.data.source.book.remote;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookDataSource;
import com.yefeng.support.http.HttpResFunction;
import com.yefeng.support.http.HttpRetrofit;

import java.util.List;

import io.reactivex.Flowable;
import timber.log.Timber;

/**
 * Created by yefeng on 06/02/2017.
 */

public class BookRemoteDataSource implements BookDataSource {

    public BookApi getApi() {
        return HttpRetrofit.getInstance().getService(BookApi.class);
    }

    @Override
    public Flowable<List<Book>> getBooks() {
        Timber.d("method: %s, thread: %s_%s", "getBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return getApi().getBooks().map(new HttpResFunction<>());
    }

    @Override
    public Flowable<Book> getBook(@NonNull String id) {
        return getApi().getbook(id).map(new HttpResFunction<>());
    }

    @Override
    public Flowable saveBook(@NonNull Book book) {
        return getApi().saveBook(book).map(new HttpResFunction<>());
    }

    @Override
    public Flowable deleteBooks() {
        return getApi().deleteBooks().map(new HttpResFunction<>());
    }

    @Override
    public Flowable deleteBook(@NonNull String id) {
        return getApi().deleteBook(id).map(new HttpResFunction<>());
    }
}

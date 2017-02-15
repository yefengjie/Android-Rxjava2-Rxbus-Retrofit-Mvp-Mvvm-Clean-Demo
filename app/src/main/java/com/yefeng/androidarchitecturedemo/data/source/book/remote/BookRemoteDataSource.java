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
 * remote data source, retrofit
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
    public void saveBook(@NonNull Book book) {
        Timber.d("method: %s, thread: %s_%s", "saveBook()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getApi().saveBook(book).map(new HttpResFunction<>());
    }

    public Flowable<String> saveBookRx(@NonNull Book book) {
        Timber.d("method: %s, thread: %s_%s", "saveBookRx()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return getApi().saveBook(book).map(new HttpResFunction<>());
    }

    @Override
    public void deleteBook(@NonNull String id) {
        Timber.d("method: %s, thread: %s_%s", "deleteBook()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getApi().deleteBook(id).map(new HttpResFunction<>());
    }

    public Flowable<String> deleteBookRx(@NonNull String id) {
        Timber.d("method: %s, thread: %s_%s", "deleteBookRx()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return getApi().deleteBook(id).map(new HttpResFunction<>());
    }
}

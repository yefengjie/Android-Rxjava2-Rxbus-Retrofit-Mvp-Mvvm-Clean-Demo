package com.yefeng.androidarchitecturedemo.data.source.book;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by yefeng on 06/02/2017.
 * book data source interface
 */

public interface BookDataSource {

    Flowable<List<Book>> getBooks();

    Flowable<Book> getBook(@NonNull String id);

    Flowable saveBook(@NonNull Book book);

    Flowable deleteBooks();

    Flowable deleteBook(@NonNull String id);
}

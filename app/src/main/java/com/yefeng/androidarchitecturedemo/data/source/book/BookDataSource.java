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

    void saveBook(@NonNull Book book);

    void deleteBook(@NonNull String id);
}

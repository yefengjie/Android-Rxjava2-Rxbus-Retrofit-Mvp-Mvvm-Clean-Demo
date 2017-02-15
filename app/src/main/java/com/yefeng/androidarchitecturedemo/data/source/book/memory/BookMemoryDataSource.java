package com.yefeng.androidarchitecturedemo.data.source.book.memory;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookDataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by yefeng on 19/01/2017.
 * memory data source
 */

public class BookMemoryDataSource implements BookDataSource {

    private Map<Long, Book> mBooks;

    @Override
    public Flowable<List<Book>> getBooks() {
        Timber.d("method: %s, thread: %s_%s", "getBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return Flowable.just(getMemoryBooks().values()).map((Function<Collection<Book>, List<Book>>) books -> {
            List list;
            if (books instanceof List) {
                list = (List) books;
            } else {
                list = new ArrayList(books);
            }
            Timber.d("getBooks: " + list.size());
            return list;
        });
    }


    @Override
    public Flowable<Void> saveBook(@NonNull Book book) {
        Timber.d("method: %s, thread: %s_%s", "saveBook()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getMemoryBooks().put(book.getId(), book);
        return Flowable.empty();
    }

    public void saveBooks(List<Book> books) {
        Timber.d("method: %s, thread: %s_%s", "saveBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (null == books || books.isEmpty()) {
            return;
        }
        for (Book book : books) {
            getMemoryBooks().put(book.getId(), book);
        }
    }

    @Override
    public Flowable<Void> deleteBook(@NonNull String id) {
        Timber.d("method: %s, thread: %s_%s", "deleteBook()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getMemoryBooks().remove(Long.valueOf(id));
        return Flowable.empty();
    }

    private Map<Long, Book> getMemoryBooks() {
        if (null == mBooks) {
            mBooks = new HashMap<>();
        }
        return mBooks;
    }

    public void clear() {
        Timber.d("method: %s, thread: %s_%s", "clear()", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (null != mBooks) {
            mBooks.clear();
        }
    }

}

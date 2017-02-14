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
        return Flowable.just(getMemoryBooks().values()).map((Function<Collection<Book>, List<Book>>) books -> {
            List list;
            if (books instanceof List) {
                list = (List) books;
            } else {
                list = new ArrayList(books);
            }
            return list;
        });
    }


    @Override
    public Flowable<Void> saveBook(@NonNull Book book) {
        getMemoryBooks().put(book.getId(), book);
        return Flowable.empty();
    }

    public void saveBooks(List<Book> books) {
        if (null == books || books.isEmpty()) {
            return;
        }
        for (Book book : books) {
            getMemoryBooks().put(book.getId(), book);
        }
    }

    @Override
    public Flowable<Void> deleteBook(@NonNull String id) {
        getMemoryBooks().remove(Long.valueOf(id));
        return Flowable.empty();
    }

    private Map<Long, Book> getMemoryBooks() {
        Timber.d("method: %s, thread: %s_%s", "getMemoryBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (null == mBooks) {
            mBooks = new HashMap<>();
        }
        return mBooks;
    }

    public Flowable clear() {
        if (null != mBooks) {
            mBooks.clear();
        }
        return Flowable.empty();
    }

}

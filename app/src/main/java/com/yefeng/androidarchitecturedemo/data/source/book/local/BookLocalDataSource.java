package com.yefeng.androidarchitecturedemo.data.source.book.local;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.model.book.BookDao;
import com.yefeng.androidarchitecturedemo.data.source.book.BookDataSource;
import com.yefeng.androidarchitecturedemo.data.source.db.DbGreen;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import timber.log.Timber;

/**
 * Created by yefeng on 07/02/2017.
 * local data source, green db
 */

public class BookLocalDataSource implements BookDataSource {

    private BookDao getBookDao() {
        Timber.d("method: %s, thread: %s_%s", "getBookDao()", Thread.currentThread().getName(), Thread.currentThread().getId());
        return DbGreen.getInstance().getDaoSession().getBookDao();
    }

    @Override
    public Flowable<List<Book>> getBooks() {
        return Flowable.fromCallable(new Callable<List<Book>>() {
            @Override
            public List<Book> call() throws Exception {
                Timber.d("method: %s, thread: %s_%s", "getBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
                List<Book> list = getBookDao().loadAll();
                Timber.d("getBooks: " + list.size());
                return list;
            }
        });
    }

    @Override
    public void saveBook(@NonNull Book book) {
        Timber.d("method: %s, thread: %s_%s", "saveBook()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getBookDao().insertOrReplace(book);
    }

    public Flowable saveBooks(@NonNull List<Book> books) {
        Timber.d("method: %s, thread: %s_%s", "saveBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getBookDao().insertOrReplaceInTx(books);
        return Flowable.empty();
    }

    @Override
    public void deleteBook(@NonNull String id) {
        Timber.d("method: %s, thread: %s_%s", "deleteBook()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getBookDao().deleteByKey(Long.valueOf(id));
    }

    public void deleteBooks() {
        Timber.d("method: %s, thread: %s_%s", "deleteBooks()", Thread.currentThread().getName(), Thread.currentThread().getId());
        getBookDao().deleteAll();
    }
}

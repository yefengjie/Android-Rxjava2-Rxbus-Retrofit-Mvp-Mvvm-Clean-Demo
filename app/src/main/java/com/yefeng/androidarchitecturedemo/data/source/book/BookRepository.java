package com.yefeng.androidarchitecturedemo.data.source.book;

import android.support.annotation.NonNull;

import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by yefeng on 19/01/2017.
 * book repository
 */

public class BookRepository implements BookDataSource {

    private static BookRepository INSTANCE = null;

    @NonNull
    private final BookRemoteDataSource mBookRemoteDataSource;

    @NonNull
    private final BookLocalDataSource mBookLocalDataSource;

    @NonNull
    private final BookMemoryDataSource mBookMemoryDataSource;

    public BookRepository(@NonNull BookRemoteDataSource bookRemoteDataSource,
                          @NonNull BookLocalDataSource bookLocalDataSource,
                          @NonNull BookMemoryDataSource bookMemoryDataSource) {
        mBookLocalDataSource = checkNotNull(bookLocalDataSource);
        mBookRemoteDataSource = checkNotNull(bookRemoteDataSource);
        mBookMemoryDataSource = checkNotNull(bookMemoryDataSource);
    }

    public static BookRepository getInstance(@NonNull BookRemoteDataSource bookRemoteDataSource,
                                             @NonNull BookLocalDataSource bookLocalDataSource,
                                             @NonNull BookMemoryDataSource bookMemoryDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository(bookRemoteDataSource, bookLocalDataSource, bookMemoryDataSource);
        }
        return INSTANCE;
    }


    /**
     * Gets books from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     */
    @Override
    public Flowable<List<Book>> getBooks() {
        return Flowable.concat(mBookMemoryDataSource.getBooks(), getAndCacheLocalBooks(), getAndSaveRemoteBooks())
                .map(new Function<List<Book>, List<Book>>() {
                    @Override
                    public List<Book> apply(List<Book> books) throws Exception {
                        Timber.d("method: %s, thread: %s_%s", "sort book list", Thread.currentThread().getName(), Thread.currentThread().getId());
                        Collections.sort(books, new Comparator<Book>() {
                            @Override
                            public int compare(Book o1, Book o2) {
                                return Integer.valueOf(o1.getTitle().split(" ")[1]) - Integer.valueOf(o2.getTitle().split(" ")[1]);
                            }
                        });
                        return books;
                    }
                });
    }

    public Flowable<List<Book>> getBooks(boolean forceUpdate) {
        if (forceUpdate) {
            return getBooks();
        }
        return getBooks()
                .filter(books -> !books.isEmpty())
                .take(1);
    }

    private Flowable<List<Book>> getAndCacheLocalBooks() {
        return mBookLocalDataSource.getBooks().doOnNext(books -> {
            mBookMemoryDataSource.clear();
            mBookMemoryDataSource.saveBooks(books);
        });
    }

    private Flowable<List<Book>> getAndSaveRemoteBooks() {
        return mBookRemoteDataSource.getBooks()
                .doOnNext(books -> {
                    mBookMemoryDataSource.clear();
                    mBookLocalDataSource.deleteBooks();
                    mBookMemoryDataSource.saveBooks(books);
                    mBookLocalDataSource.saveBooks(books);
                });
    }


    @Override
    public void saveBook(@NonNull Book book) {

    }

    public Flowable<String> saveBookRx(@NonNull Book book) {
        return mBookRemoteDataSource.saveBookRx(book)
                .doOnNext(s -> {
                    Timber.d("method: %s, thread: %s_%s", "saveBook:doOnNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                    mBookMemoryDataSource.saveBook(book);
                    mBookLocalDataSource.saveBook(book);
                });
    }

    @Override
    public void deleteBook(@NonNull String id) {

    }

    public Flowable<String> deleteBookRx(@NonNull String id) {
        return mBookRemoteDataSource.deleteBookRx(id)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Timber.d("method: %s, thread: %s_%s", "deleteBook:doOnNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        mBookMemoryDataSource.deleteBook(id);
                        mBookLocalDataSource.deleteBook(id);
                    }
                });
    }
}

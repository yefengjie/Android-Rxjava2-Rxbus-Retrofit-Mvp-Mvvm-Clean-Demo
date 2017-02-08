package com.yefeng.androidarchitecturedemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;
import com.yefeng.support.http.HttpSchedulersTransformer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mPd;
    private BookRepository mBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> test());
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中");

        mBr = BookRepository.getInstance(new BookRemoteDataSource(), new BookLocalDataSource(), new BookMemoryDataSource());
    }

    private void test() {
        mBr.getBooks()
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(subscription -> {
                    Timber.d("doOnSubscribe()");
                    mPd.show();
                })
                .subscribe(books -> {
                    Timber.d("onNext()");
                    Timber.e(books.toString());
                    Timber.d("method: %s, thread: %s_%s", "test()", Thread.currentThread().getName(), Thread.currentThread().getId());
                }, t -> Timber.e(t), () -> {
                    Timber.d("onComplete()");
                    mPd.dismiss();
                });
    }

    private void testLocalData() {
        new BookLocalDataSource().getBooks()
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(subscription -> mPd.show())
                .subscribe(books -> {
                    Timber.e(books.toString());
                    Timber.d("method: %s, thread: %s_%s", "testLocalData()", Thread.currentThread().getName(), Thread.currentThread().getId());
                }, Timber::e, () -> mPd.dismiss());
    }

    private void testMemoryData() {
        new BookMemoryDataSource().getBooks()
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(subscription -> mPd.show())
                .subscribe(books -> {
                    Timber.e(books.toString());
                    Timber.d("method: %s, thread: %s_%s", "testMemoryData()", Thread.currentThread().getName(), Thread.currentThread().getId());
                }, Timber::e, () -> mPd.dismiss());
    }

    private void testRemoteData() {
        new BookRemoteDataSource().getBooks()
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(subscription -> mPd.show())
                .subscribe(new Subscriber<List<Book>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);//设置请求数
                    }

                    @Override
                    public void onNext(List<Book> books) {
                        Timber.e(books.toString());
                        Timber.d("method: %s, thread: %s_%s", "testRemoteData()", Thread.currentThread().getName(), Thread.currentThread().getId());
                    }

                    @Override
                    public void onError(Throwable t) {
                        mPd.dismiss();
                        Timber.e(t);
                    }

                    @Override
                    public void onComplete() {
                        mPd.dismiss();
                    }
                });
    }
}

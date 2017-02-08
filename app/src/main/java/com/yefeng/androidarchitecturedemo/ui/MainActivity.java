package com.yefeng.androidarchitecturedemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;
import com.yefeng.support.http.HttpSchedulersTransformer;

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
}

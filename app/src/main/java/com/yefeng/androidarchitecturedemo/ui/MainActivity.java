package com.yefeng.androidarchitecturedemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;
import com.yefeng.support.DebugLog;
import com.yefeng.support.http.HttpSchedulersTransformer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中");
    }

    private void test() {
        BookRepository br = BookRepository.getInstance(new BookRemoteDataSource(), new BookLocalDataSource(), new BookMemoryDataSource());
        br.getBooks()
                .compose(new HttpSchedulersTransformer<>())
                .doOnSubscribe(subscription -> {
                    DebugLog.logThread("doOnSubscribe()", Thread.currentThread().getName(), Thread.currentThread().getId());
                    mPd.show();
                })
                .subscribe(new Subscriber<List<Book>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);//设置请求数
                    }

                    @Override
                    public void onNext(List<Book> books) {
                        Timber.e(books.toString());
                        DebugLog.logThread("onNext()", Thread.currentThread().getName(), Thread.currentThread().getId());
                    }

                    @Override
                    public void onError(Throwable t) {
                        DebugLog.logThread("onError()", Thread.currentThread().getName(), Thread.currentThread().getId());
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.logThread("onCompleted()", Thread.currentThread().getName(), Thread.currentThread().getId());
                        mPd.dismiss();
                    }
                });
    }
}

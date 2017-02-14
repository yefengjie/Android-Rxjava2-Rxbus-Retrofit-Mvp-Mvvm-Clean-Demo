package com.yefeng.androidarchitecturedemo.ui.main;

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

/**
 * ui main view
 */
public class MainActivity extends AppCompatActivity implements MainContract.View {

    private ProgressDialog mPd;
    private BookRepository mBr;
    private MainContract.Presenter mPresenter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mPd = new ProgressDialog(this);
        mPd.setMessage("加载中");
        BookRepository bookRepository = BookRepository.getInstance(
                new BookRemoteDataSource(),
                new BookLocalDataSource(),
                new BookMemoryDataSource()
        );
        mPresenter = new MainPresenter(bookRepository, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}

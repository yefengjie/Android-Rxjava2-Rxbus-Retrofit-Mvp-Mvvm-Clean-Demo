package com.yefeng.androidarchitecturedemo.ui.mvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;
import com.yefeng.androidarchitecturedemo.databinding.ActivityMvvmBinding;
import com.yefeng.androidarchitecturedemo.ui.mvp.MainContract;

import java.util.ArrayList;

/**
 * Created by yefeng on 21/02/2017.
 */

public class MvvmActivity extends AppCompatActivity implements MainContract.View {

    private SwipeRefreshLayout mSwipeLayout;
    private MainContract.Presenter mPresenter;
    private ActivityMvvmBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        init();
    }

    private void init() {

        mPresenter = new MvvmPresenter(new BookRepository(new BookRemoteDataSource(), new BookLocalDataSource(), new BookMemoryDataSource()), this);
        mBinding.setActionHandler(mPresenter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBinding.swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Book book = new Book(1l, "book 1", "");
        mBinding.setBook(book);
    }

    private void showToast(String msg) {
        Snackbar.make(mSwipeLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unSubscribe();
    }

    @Override
    public void onLoading() {
        if (!mBinding.swipeContainer.isRefreshing()) {
            mBinding.swipeContainer.setRefreshing(true);
        }
    }

    @Override
    public void onLoadOk(ArrayList<Book> books) {

    }

    @Override
    public void onLoadError(String msg) {
        mBinding.swipeContainer.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onLoadFinish() {
        mBinding.swipeContainer.setRefreshing(false);
    }

    @Override
    public void addBook(View view) {

    }

    @Override
    public void deleteBook(@NonNull String id) {

    }

    @Override
    public void onAction() {

    }

    @Override
    public void onActionOk() {

    }

    @Override
    public void onActionError(String msg) {

    }

    @Override
    public void onActionFinish() {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}

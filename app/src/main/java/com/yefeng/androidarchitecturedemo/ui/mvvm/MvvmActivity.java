package com.yefeng.androidarchitecturedemo.ui.mvvm;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.freedom.yefeng.yfrecyclerview.YfListMode;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;
import com.yefeng.androidarchitecturedemo.databinding.ActivityMvvmBinding;
import com.yefeng.androidarchitecturedemo.ui.mvp.Events;
import com.yefeng.androidarchitecturedemo.ui.mvp.MainContract;
import com.yefeng.support.rxbus.RxBus;

import java.util.ArrayList;

/**
 * Created by yefeng on 21/02/2017.
 */

public class MvvmActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private ActivityMvvmBinding mBinding;
    private MvvmAdapter mAdapter;
    private ProgressDialog mPd;

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
        mBinding.setMvvmView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBinding.swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        initAdapter();

        //init progress
        mPd = new ProgressDialog(this);
        mPd.setMessage("please wait...");
    }

    private void initAdapter() {
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new MvvmAdapter(null, new OnBookItemClickListener() {
            @Override
            public void onBookClicked(Book book) {
                new AlertDialog.Builder(MvvmActivity.this)
                        .setTitle("delete book: " + book.getTitle() + " ?")
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("delete", (dialog, which) -> deleteBook(String.valueOf(book.getId())))
                        .create()
                        .show();
            }
        });
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setDivider(R.mipmap.divider);
    }

    private void showToast(String msg) {
        Snackbar.make(mBinding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
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
        mAdapter.setData(books);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadError(String msg) {
        mBinding.swipeContainer.setRefreshing(false);
        showToast(msg);
        mAdapter.changeMode(YfListMode.MODE_ERROR);
    }

    @Override
    public void onLoadFinish() {
        mBinding.swipeContainer.setRefreshing(false);
    }

    @Override
    public void addBook(View view) {
        long lastBookId = 0;
        if (mAdapter.getData().size() > 0) {
            lastBookId = mAdapter.getData().get(mAdapter.getData().size() - 1).getId();
        }
        Book newBook = new Book();
        newBook.setId(lastBookId + 1);
        newBook.setTitle("book " + lastBookId);
        mPresenter.saveBook(newBook);
    }

    @Override
    public void deleteBook(@NonNull String id) {
        mPresenter.deleteBook(id);
    }

    @Override
    public void onAction() {
        mPd.show();
    }

    @Override
    public void onActionOk() {
        RxBus.getBus().send(new Events.ReloadEvent(false));
    }

    @Override
    public void onActionError(String msg) {
        mPd.dismiss();
        showToast(msg);
    }

    @Override
    public void onActionFinish() {
        mPd.dismiss();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}

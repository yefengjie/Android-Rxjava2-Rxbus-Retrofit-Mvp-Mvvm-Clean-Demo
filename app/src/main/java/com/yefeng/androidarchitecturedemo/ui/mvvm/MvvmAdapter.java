package com.yefeng.androidarchitecturedemo.ui.mvvm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.freedom.yefeng.yfrecyclerview.YfListAdapter;
import com.freedom.yefeng.yfrecyclerview.YfSimpleViewHolder;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;

import java.util.ArrayList;

/**
 * Created by yefeng on 22/02/2017.
 */

public class MvvmAdapter extends YfListAdapter<Book> {
    public MvvmAdapter(ArrayList<Book> data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent) {
        return new YfSimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_material, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateErrorViewHolder(ViewGroup parent) {
        return new YfSimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_net_error_material, parent, false));
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}

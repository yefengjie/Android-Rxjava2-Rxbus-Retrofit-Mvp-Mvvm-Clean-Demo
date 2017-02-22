package com.yefeng.androidarchitecturedemo.ui.mvvm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.annotations.NonNull;
import com.freedom.yefeng.yfrecyclerview.YfListAdapter;
import com.freedom.yefeng.yfrecyclerview.YfListInterface;
import com.freedom.yefeng.yfrecyclerview.YfSimpleViewHolder;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.databinding.AdapterMvvmBinding;

import java.util.ArrayList;

/**
 * Created by yefeng on 22/02/2017.
 */

public class MvvmAdapter extends YfListAdapter<Book> {

    private OnBookItemClickListener mListener;

    public MvvmAdapter(ArrayList<Book> data, OnBookItemClickListener bookItemClickListener) {
        super(data);
        this.mListener = bookItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        BindingHolder holder = new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mvvm, parent, false));
        holder.setBookItemActionHandler(mListener);
        return holder;
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
        ((BindingHolder) holder).bind(mData.get(position));
    }

    @Override
    public void setOnItemClickListener(YfListInterface.OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private AdapterMvvmBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setBookItemActionHandler(OnBookItemClickListener bookItemActionHandler) {
            binding.setActionHandler(bookItemActionHandler);
        }

        public void bind(@NonNull Book book) {
            binding.setBook(book);
        }
    }
}

package com.yefeng.androidarchitecturedemo.ui.mvvm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private BookItemActionHandler mActionListener;

    public MvvmAdapter(ArrayList<Book> data, BookItemActionHandler bookItemActionHandler) {
        super(data);
        this.mActionListener = bookItemActionHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        AdapterMvvmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_mvvm, parent, false);
        binding.setActionHandler(mActionListener);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
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
        Book book = getData().get(position);
        ((BindingHolder) holder).getBinding().setBook(book);
        ((BindingHolder) holder).getBinding().executePendingBindings();
    }

    @Override
    public void setOnItemClickListener(YfListInterface.OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private AdapterMvvmBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
        }

        public AdapterMvvmBinding getBinding() {
            return binding;
        }

        public void setBinding(AdapterMvvmBinding binding) {
            this.binding = binding;
        }
    }
}

package com.yefeng.androidarchitecturedemo.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freedom.yefeng.yfrecyclerview.YfListAdapter;
import com.freedom.yefeng.yfrecyclerview.YfSimpleViewHolder;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;

import java.util.ArrayList;

/**
 * Created by yefeng on 8/5/15.
 * github:yefengfreedom
 */
public class MainAdapter extends YfListAdapter<Book> {

    public MainAdapter(ArrayList<Book> data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).mText.setText(mData.get(i).getTitle());
        viewHolder.itemView.setTag(mData.get(i));
    }

    @Override
    public RecyclerView.ViewHolder onCreateErrorViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_net_error_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.txt_adapter_item);
        }
    }
}

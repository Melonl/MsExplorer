package com.melonl.msexplorer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.melonl.msexplorer.fragment.MainPageFragment;

import java.io.File;
import java.util.List;

public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.viewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<File> mMemories;
    private FileListAdapter.itemListener mListener;
    private MainPageFragment mFragment;

    public MemoryListAdapter(Context context, MainPageFragment fragment, List<File> list) {
        mContext = context;
        mMemories = list;
        mFragment = fragment;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryListAdapter.viewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    class viewHolder extends RecyclerView.ViewHolder {


        public viewHolder(View v) {
            super(v);
        }

    }
}



package com.melonl.msexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.melonl.msexplorer.R;
import com.melonl.msexplorer.adapter.MemoryListAdapter;

/**
 * Created by root on 17-5-6.
 */

public class MainPageFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private TextView mTextView;


    private MemoryListAdapter mAdapter;

    public MainPageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgress = (ProgressBar) view.findViewById(R.id.progressBar1);
        mTextView = (TextView) view.findViewById(R.id.tv_memory_usage);


    }

    private void calcMemory() {


    }
}

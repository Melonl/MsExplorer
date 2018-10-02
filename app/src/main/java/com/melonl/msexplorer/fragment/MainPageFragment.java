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
import com.melonl.msexplorer.model.StorageSizeUtil;

/**
 * Created by root on 17-5-6.
 */

public class MainPageFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgress;
    private TextView mTextView;


    private int colorGreen;
    private int colorOrange;
    private int colorRed;


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

        colorGreen = getResources().getColor(R.color.blue);
        colorOrange = getResources().getColor(R.color.orange);
        colorRed = getResources().getColor(R.color.red);


        mProgress = (ProgressBar) view.findViewById(R.id.progressBar1);
        mTextView = (TextView) view.findViewById(R.id.tv_memory_usage);

        calcMemory();
    }

    private void calcMemory() {
        double total = StorageSizeUtil.getTotalExternalMemorySize();
        double remain = StorageSizeUtil.getAvailableExternalMemorySize();
        double percent = (total - remain) / total * 100;

        mTextView.setText(String.format("%.2f", percent) + "% used");
        mProgress.setProgress((int) percent);

        /*
        int color;
        if(percent>=90){
            color = colorRed;
        }
        else if(percent >= 70 && percent <90){
            color = colorOrange;
        }
        else{
            color = colorGreen;
        }
        */

        /*
        ClipDrawable drawable = new ClipDrawable(new ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mProgress.setProgressDrawable(drawable);
        drawable.setLevel((int)percent * 100);
        mProgress.setProgressDrawable(drawable);
        */
    }
}

package com.melonl.msexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melonl.msexplorer.R;

/**
 * Created by root on 17-5-6.
 */

public class MainPageFragment extends BaseFragment {

    public MainPageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_page, container);


        return v;
    }
}

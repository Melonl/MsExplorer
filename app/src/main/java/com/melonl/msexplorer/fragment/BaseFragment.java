package com.melonl.msexplorer.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by root on 17-4-30.
 */

public class BaseFragment extends Fragment {

    private String title;

    public String getTitle() {

        if(title == null){
            throw new RuntimeException("Fragment's title is null !");
        }

        return title;
    }

    public BaseFragment setTitle(String title) {
        this.title = title;
        return this;
    }




}

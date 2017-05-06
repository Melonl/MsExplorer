package com.melonl.msexplorer.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.melonl.msexplorer.fragment.BaseFragment;

import java.util.List;

/**
 * Created by root on 17-4-30.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> mFragments;

    public PagerAdapter(FragmentManager fm,List<BaseFragment> frags){
        super(fm);
        this.mFragments = frags;

    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getTitle();
    }
}

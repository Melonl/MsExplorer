package com.melonl.msexplorer;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melonl.msexplorer.fragment.BaseFragment;

public class MainActivity extends BaseActivity {


    private BaseFragment mCurrentfragment;

    private CoordinatorLayout mCoordinator;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        checkPermission();
        initView();
        setTitle(getResources().getString(R.string.app_name));
        setSubText("Main page");
    }

    public void initView(){

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        mViewPager = (ViewPager) findViewById(R.id.main_view_pagwer);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Create new file")
                        .items(new String[]{"New file", "New folder"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch(position){
                                    case 0:

                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    public void initData(){



    }

    public void checkPermission(){
        if (checkUpPermission() == 0)
        {
            initData();
        }
        else
        {
            requestStoragePermission();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_STORAGE_CODE)
        {
            int grantResult = grantResults[0];

            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;

            if (granted)
            {
                initData();
            }
            else
            {
                finish();
            }

        }


    }

    public void Snackbar(String text)
    {
        Snackbar sb = Snackbar.make(mCoordinator, text, Snackbar.LENGTH_SHORT);
        //sb.getView().setBackgroundColor(getResources().getColor(R.color.black_semi_transparent));
        sb.setAction("OK", null);
        sb.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean useToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.melonl.msexplorer;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends BaseActivity {


    private CoordinatorLayout coordinator;
    private ViewPager viewPager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        setTitle(getResources().getString(R.string.app_name));
        setSubText("Sub Title");
    }

    public void initView(){
        coordinator = (CoordinatorLayout)findViewById(R.id.main_coordinator);
        viewPager = (ViewPager)findViewById(R.id.main_view_pagwer);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Create new file")
                        .items(new String[]{"New file","New folder"})
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

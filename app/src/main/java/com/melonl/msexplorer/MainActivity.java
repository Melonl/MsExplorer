package com.melonl.msexplorer;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.melonl.msexplorer.adapter.PagerAdapter;
import com.melonl.msexplorer.fragment.BaseFragment;
import com.melonl.msexplorer.fragment.FileListFragment;
import com.melonl.msexplorer.fragment.MainPageFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    private BaseFragment mCurrentfragment;

    private CoordinatorLayout mCoordinator;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private FloatingActionButton mFab;
    private FloatingToolbar mFloatingbar;

    private PagerAdapter mPagerAdapter;

    private boolean isCreatingFile; //used to mark creating file or folder


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();
        setTitle(getResources().getString(R.string.app_name));
        setSubText("Main page");
    }

    public void addNewPage(String title, String path) {
        FileListFragment newFragment = new FileListFragment();
        newFragment.setTitle(title);
        mPagerAdapter.addFragment(newFragment);
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText(title);
        mTabLayout.addTab(tab);
        mPagerAdapter.notifyDataSetChanged();
    }

    public void removePage(BaseFragment fragment) {
        int position = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position - 1);
        //mTabLayout.removeTabAt(position);
        mPagerAdapter.removeFragment(position);
        mPagerAdapter.notifyDataSetChanged();
    }

    private void findViews() {

        mDrawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.abc_toolbar_collapse_description, R.string.abc_action_bar_home_description);
        mNavigationView = (NavigationView) findViewById(R.id.nav);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        mViewPager = (ViewPager) findViewById(R.id.main_view_pagwer);
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mFloatingbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

    }

    private void setUpViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToggle.syncState();
        mDrawer.addDrawerListener(mToggle);

        List<BaseFragment> pages = new ArrayList<>();
        pages.add(new MainPageFragment().setTitle("Main"));
        pages.add(new FileListFragment().setTitle("File"));

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), pages);
        mViewPager.setAdapter(mPagerAdapter);
        //mViewPager.setOffscreenPageLimit(4);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);

        mFloatingbar.attachFab(mFab);


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
                                    case 0://new file case
                                        isCreatingFile = true;
                                        break;
                                    case 1://new folder case
                                        isCreatingFile = false;
                                        break;
                                }
                                creatingFileOrFolder();
                            }
                        })

                        .show();
            }
        });

    }

    private void creatingFileOrFolder() {
        final String creatingType;
        if (isCreatingFile) {
            creatingType = "File";
        } else {
            creatingType = "Folder";
        }
        new MaterialDialog.Builder(this)
                .title("Create new " + creatingType)
                .input("New" + creatingType, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (TextUtils.isEmpty(input)) {
                            return;
                        }
                        FileListFragment f = (FileListFragment) mCurrentfragment;
                        String currentPath = f.getCurrentPath();
                        if (!new File(currentPath).canWrite()) {
                            Snackbar("Permission denied");
                            return;
                        }
                        File newFile = new File(currentPath + File.separator + input);
                        boolean createSucc;
                        if (isCreatingFile) {
                            try {
                                createSucc = newFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                createSucc = false;
                            }
                        } else {
                            createSucc = newFile.mkdir();
                        }
                        if (createSucc) {
                            Snackbar("Created successfully");
                            f.refreshList();
                        } else {
                            Snackbar("Failed to create!");
                        }
                        return;
                    }
                })
                .show();
    }

    public void checkPermission(){
        if (checkUpPermission() == 0)
        {
            findViews();
            setUpViews();
        }
        else
        {
            requestStoragePermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_STORAGE_CODE)
        {
            int grantResult = grantResults[0];

            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;

            if (granted)
            {
                findViews();
                setUpViews();
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
        sb.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        });
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
            case R.id.action_about:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //Snackbar(position + "");
        mCurrentfragment = mPagerAdapter.getItem(position);
        if (mCurrentfragment instanceof FileListFragment) {
            RecyclerView rv = ((FileListFragment) mCurrentfragment).getRecyclerView();
            mFloatingbar.attachRecyclerView(rv);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mFloatingbar.detachRecyclerView();
    }
}

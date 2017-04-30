package com.melonl.msexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by root on 17-4-30.
 */

public class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_STORAGE_CODE = 1;

    Toolbar toolbar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        if (useToolbar()) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if(useBackBtn()){
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    public int getLayoutId(){

        //pls override this method to set content view.
        return 0;
    }

    public boolean useToolbar() {
        //pls override this method if you want to use toolbar.
        return false;
    }

    public void setSubText(String text){
        getToolbar().setSubtitle(text);
    }

    public Toolbar getToolbar() {
        if (toolbar == null) {
            throw new RuntimeException("Toolbar is null!");
        }

        return toolbar;
    }

    public boolean useBackBtn() {
        //pls override this method if you want to use back buttton in toolbar.
        return false;
    }



    public int checkUpPermission()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return 0;
        }

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) || !(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        {
            return -1;
        }
        else
        {
            return 0;
        }

    }

    public void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_CODE);
    }
}

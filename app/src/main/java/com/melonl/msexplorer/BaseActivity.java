package com.melonl.msexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by root on 17-4-30.
 */

public class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_STORAGE_CODE = 1;

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        if (useToolbar()) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (useBackBtn()) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (useTinted()) {
                setStatusBarBackground();
            }

        }


    }

    public int getLayoutId() {

        //pls override this method to set content view.
        return 0;
    }

    public boolean useToolbar() {
        //pls override this method if you want to use toolbar.
        return false;
    }

    public void setSubText(String text) {
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

    public boolean useTinted() {
        return true;
    }

    private void setStatusBarBackground() {
        // 4.4 translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 5.0 transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void toast(String string) {
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        //toast.getView().setBackgroundColor(0x50000000);
        toast.show();

    }

    public int checkUpPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return 0;
        }

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) || !(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return -1;
        } else {
            return 0;
        }

    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_CODE);
    }
}

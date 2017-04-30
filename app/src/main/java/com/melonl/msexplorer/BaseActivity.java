package com.melonl.msexplorer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by root on 17-4-30.
 */

public class BaseActivity extends AppCompatActivity {

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
}

package com.melonl.msexplorer.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BottomBar extends LinearLayoutCompat implements View.OnClickListener {

    private Context mContext;

    private List<AppCompatImageButton> mButtons = new ArrayList<AppCompatImageButton>();

    public BottomBar(Context context) {
        super(context, (AttributeSet) null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;

        //LayoutInflater.from(mContext).inflate(R.layout.view_bottom_bar,this);
        setOrientation(HORIZONTAL);


        /*
        mButtons.add((AppCompatImageButton)findViewById(R.id.item_copy));
        mButtons.add((AppCompatImageButton)findViewById(R.id.item_cut));
        mButtons.add((AppCompatImageButton)findViewById(R.id.item_delete));
        mButtons.add((AppCompatImageButton)findViewById(R.id.item_info));
        mButtons.add((AppCompatImageButton)findViewById(R.id.item_more));
        */

        for (AppCompatImageButton aib : mButtons) {
            aib.setOnClickListener(this);
        }


    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}

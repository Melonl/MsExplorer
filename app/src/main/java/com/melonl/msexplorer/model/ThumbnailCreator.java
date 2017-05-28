package com.melonl.msexplorer.model;

import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Melonl on 17-5-28.
 */

public class ThumbnailCreator {

    private static ThumbnailCreator mCreator;

    private LruCache<String, Drawable> drawableCache;
    private ExecutorService fixedThreadPool;

    private ThumbnailCreator() {
        //signal instance
        int size = (int) Runtime.getRuntime().maxMemory() / 1024;
        fixedThreadPool = Executors.newFixedThreadPool(5);
        drawableCache = new LruCache<String, Drawable>(size / 8);
    }

    public ThumbnailCreator getInstance() {
        if (mCreator == null) {
            mCreator = new ThumbnailCreator();
        }
        return mCreator;

    }


}

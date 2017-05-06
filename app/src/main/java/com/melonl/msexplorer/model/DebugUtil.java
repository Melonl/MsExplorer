package com.melonl.msexplorer.model;

import android.content.Context;
import android.util.Log;

public class DebugUtil {
    private static final Boolean debugMode = true;

    public static void printLog(Context c, String str) {
        if (debugMode) {
            Log.v(Config.APP_TAG, c.getClass().getSimpleName() + ":" + str);
        }
    }

    public static void printLog(String str) {
        if (debugMode) {
            Log.v(Config.APP_TAG, str);
        }
    }
}

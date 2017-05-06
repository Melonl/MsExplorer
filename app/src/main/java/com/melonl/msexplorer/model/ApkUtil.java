package com.melonl.msexplorer.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ApkUtil {

    public static boolean isApkFile(Context c, String path) {
        ApplicationInfo ali = getAppInfo(c, path);
        return ali != null;
    }

    public static ApplicationInfo getAppInfo(Context ctx, String apkFilePath) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkFilePath, 0);
            ApplicationInfo appInfo = pi.applicationInfo;
            appInfo.sourceDir = apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
            return appInfo;
        } catch (Exception e) {

            return null;
        }

    }

    public static String getApkPackageName(Context ctx, String apkFilePath) {
        ApplicationInfo appInfo = getAppInfo(ctx, apkFilePath);
        return appInfo.packageName;
    }

    public static Drawable getApkIcon(Context ctx, String apkFilePath) {

        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        pi.applicationInfo.sourceDir = apkFilePath;
        pi.applicationInfo.publicSourceDir = apkFilePath;
        return pi.applicationInfo.loadIcon(pm);
    }

    public static String getApkLabel(Context ctx, String apkFilePath) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(apkFilePath, 0);
        ApplicationInfo appInfo = pi.applicationInfo;
        appInfo.sourceDir = apkFilePath;
        appInfo.publicSourceDir = apkFilePath;

        return appInfo.loadLabel(pm).toString();
    }

    public static String getApkVersionName(Context ctx, String apkFilePath) {

        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        pi.applicationInfo.sourceDir = apkFilePath;
        pi.applicationInfo.publicSourceDir = apkFilePath;
        return pi.versionName;
    }


}

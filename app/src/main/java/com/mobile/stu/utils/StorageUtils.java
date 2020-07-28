package com.mobile.stu.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * author: dourl
 * created on: 2018/7/30 下午1:57
 * description: 存储工具类：获取各种文件路径
 */
public class StorageUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    public static File getFilesDirectory(Context context) {
        return getFilesDirectory(context, true);
    }
    private static File getFilesDirectory(Context context, boolean preferExternal) {
        File appFilesDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
           // appFilesDir = getExternalFilesDir(context);
        }
        if (appFilesDir == null) {
            appFilesDir = context.getFilesDir();
        }
        if (appFilesDir == null) {
            appFilesDir = context.getFilesDir();
            //NHLog.d("Can't define system cache directory! '%s' will be used.", "context.getFilesDir()");
        }
        return appFilesDir;
    }
    private static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
           // NHLog.d("Can't define system cache directory! '%s' will be used.", "context.getCacheDir()");
        }
        return appCacheDir;
    }
    protected static File getExternalCacheDir(Context context) {
        File appCacheDir = context.getExternalCacheDir();
        if (appCacheDir == null) {
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            File cacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
            if (makeNoMedia(cacheDir)) return null;
            return cacheDir;
        } else {
            if (makeNoMedia(appCacheDir)) return null;
            return appCacheDir;
        }
    }
    protected static boolean makeNoMedia(File cacheDir) {
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
               // NHLog.d("Unable to create external cache directory");
                return true;
            }
            try {
                new File(cacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                //NHLog.d("Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return false;
    }
    protected static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }


    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(getCacheDirectory(context), cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }
}

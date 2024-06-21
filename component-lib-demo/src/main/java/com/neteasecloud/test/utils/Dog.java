package com.neteasecloud.test.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.neteasecloud.test.BuildConfig;

/**
 * Logging tools
 */
public final class Dog {

    private static String ROOT_TAG = "ComponentLib";

    private static boolean isDebug = true;

    public static void initLog(Context context) {
        String  packageName = context.getPackageName();
        long    versionCode;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                versionCode =
                        context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).getLongVersionCode();
            } else {
                versionCode =
                        context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = 1;
        }
        //测试再验证时总是无法区分编译版本，自研人员也无法知道版本号，所以此处添加编译时间的逻辑
        ROOT_TAG = String.format("%s-%s-%s", ROOT_TAG, versionCode, BuildConfig.APPLICATION_ID);
    }


    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(ROOT_TAG, tag + " ->>> " + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(ROOT_TAG, tag + " ->>> " + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(ROOT_TAG, tag + " ->>> " + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(ROOT_TAG, tag + " ->>> " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(ROOT_TAG, tag + " ->>> " + msg);
        }
    }
}

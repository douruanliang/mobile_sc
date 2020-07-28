package com.mobile.stu.base;

import android.app.Application;

/**
 * author: dourl
 * created on: 2019/2/13 5:42 PM
 * description: 借一个 Application
 */
public class AppConstant {

    private static Application sApp;

    public static Application getApp() {
        return sApp;
    }

    public static void init(Application application) {
        sApp = application;
    }
}

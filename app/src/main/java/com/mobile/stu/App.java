package com.mobile.stu;

import android.app.Application;

import com.mobile.stu.base.AppConstant;
import com.mobile.stu.base.net.HttpApiBase;

/**
 * @author: dourl
 * @date: 2020/7/28
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppConstant.init(this);
        HttpApiBase.init(this);

    }
}

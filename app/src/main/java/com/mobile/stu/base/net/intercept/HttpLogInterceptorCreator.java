package com.mobile.stu.base.net.intercept;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author: dourl
 * created on: 2018/10/24 4:21 PM
 * description:
 */
public class HttpLogInterceptorCreator {
    public static HttpLoggingInterceptor create(){
        HttpLoggingInterceptor interceptorCreator = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("OkHttp", message);
            }
        });
        interceptorCreator.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptorCreator;
    }
}

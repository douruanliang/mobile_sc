package com.mobile.stu.base.net;

import android.app.Application;


import com.liulishuo.filedownloader.BuildConfig;
import com.mobile.stu.base.Constants;
import com.mobile.stu.base.net.calladapter.BaseCallAdapterFactory;
import com.mobile.stu.base.net.intercept.HttpLogInterceptorCreator;
import com.mobile.stu.base.net.retrofit.RetrofitManager;
import com.mobile.stu.utils.StorageUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: dourl
 * created on: 2018/7/30 下午1:44
 * description:
 */
public class HttpApiBase {

    static int HOST = 0;

    //需要后期修改
    static String APP_ID;
    static String DEBUG_APP_ID;

    public static String getSecureBaseUrl() {
        //配置正式环境还是测试环境
        //return HOST == 0 ? "https://" + IPS[HOST] : "http://" + IPS[HOST];
        return "http://192.168.1.14:8080";
    }

    public static final int DEFAULT_MAX_CONNECTIONS = 5;
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

    public static OkHttpClient mClient;

    public static void init(Application context) {
        Cache cache = new Cache(StorageUtils.getOwnCacheDirectory(context, "net"), 50 * 1024 * 1024);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(DEFAULT_MAX_CONNECTIONS);
        dispatcher.setMaxRequestsPerHost(DEFAULT_MAX_CONNECTIONS);
        //
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .dispatcher(dispatcher)
                .dns(Dns.SYSTEM)
                .cache(cache)
                .addInterceptor(new RequestInterceptor());

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(HttpLogInterceptorCreator.create());
            //TODO other Interceptor
        }


        // builder.hostnameVerifier(new ReleaseHostnameVerifier());
        //https 证书
        //builder.sslSocketFactory()

       /* try {
            char[] chars = null;
          *//*  char[] chars = Cng.ta().toCharArray();
            chars[8] -= 2;*//*
            X509TrustManager trustManager = SSLContextFactory.loadTrustManagers(AppConstant.getApp().getAssets().open("CA证书"));
            builder.sslSocketFactory(SSLContextFactory
                    .makeContext(AppConstant.getApp().getAssets().open("Client"),new String(chars),trustManager)
                            .getSocketFactory()
                    ,trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //------------认证--end
        if (BuildConfig.DEBUG) {
            RetrofitManager.enableDebug();
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(getSecureBaseUrl());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        //更具不同业务进行替换
        retrofitBuilder.addCallAdapterFactory(new BaseCallAdapterFactory());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        mClient = builder.build();
        //组装
        RetrofitManager.get().initRetrofit(retrofitBuilder, mClient);
    }

    public static void cancelAll() {
        if (mClient != null) {
            mClient.dispatcher().cancelAll();
        }
    }

    private static class RequestInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl httpUrl = request.url();
            final HttpUrl requestUrl = getRequestUrl(httpUrl);
            if (httpUrl != null && requestUrl != null) {
                //根据服务端来动态添加参数
                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.url(requestUrl);
              /*  long timeStamp = System.currentTimeMillis() / 1000;
                //String nonceStr = DigestUtils.md5Hex(UUID.randomUUID().toString());
                String nonceStr ="";
                requestBuilder.addHeader("nonce", nonceStr);
                requestBuilder.addHeader("timestamp", String.valueOf(timeStamp));
                TreeMap<String, String> map = new TreeMap<>();
                for (int i = 0; i < requestUrl.querySize(); i++) {
                    map.put(requestUrl.queryParameterName(i), requestUrl.queryParameterValue(i));
                }
                map.put("nonce", nonceStr);
                map.put("timestamp", String.valueOf(timeStamp));
                StringBuilder builder = new StringBuilder();
                for (String key : map.keySet()) {
                    builder.append(key).append("=").append(map.get(key)).append("&");
                }
                builder.deleteCharAt(builder.length() - 1);
                String baseStr = request.method() + "&" + request.url().toString().split("\\?")[0]+ "&" + builder.toString();
                //这些需要后期自己定义
                *//*String signStr = DigestUtils.md5Hex(baseStr + (HttpApiBase.HOST != 1 ? BuildConfig.APP_SECRET : BuildConfig.DEBUG_APP_SECRET)
                        + LoginManager.getInstance().getSecret());
*//*
                String signStr ="";
                requestBuilder.addHeader("sign", signStr);*/
                if (BuildConfig.DEBUG) {
                    requestBuilder.removeHeader("If-None-Match");
                }
                return chain.proceed(requestBuilder.build());
            } else {
                return chain.proceed(request);
            }
        }
    }

    private static class DebugNetworkInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.removeHeader("If-None-Match");
            return chain.proceed(builder.build());
        }
    }

    static HttpUrl getRequestUrl(@NonNull HttpUrl httpUrl) {
        if (httpUrl == null) {
            return null;
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        urlBuilder.addQueryParameter("nbid", Constants.DEVICEID + "@");
        //  urlBuilder.addQueryParameter("lang", LocalHelper.getLanguage());
        urlBuilder.addQueryParameter("app_id", HttpApiBase.HOST != 1 ? APP_ID : DEBUG_APP_ID);
        urlBuilder.addQueryParameter("app_version", Constants.VERSION_NAME);
        urlBuilder.addQueryParameter("app_version_code", String.valueOf(Constants.VERSION_CODE));
        urlBuilder.addQueryParameter("app_channel", Constants.CHANNEL);
        urlBuilder.addQueryParameter("os_version", Constants.OS_VERSION);
        urlBuilder.addQueryParameter("os_name", "Android");
        urlBuilder.addQueryParameter("network_country_iso", Constants.NETWORKCOUNTRYISO);
        urlBuilder.addQueryParameter("network_operator", Constants.NETWORKOPERATOR);
        urlBuilder.addQueryParameter("sim_operator", Constants.SIMOPERATOR);
        urlBuilder.addQueryParameter("sim_country_iso", Constants.SIMCOUNTRYISO);
        urlBuilder.addQueryParameter("package_name", Constants.PACKAGE_NAME);

        /*if (LoginManager.getInstance().isLogin()) {
            urlBuilder.addQueryParameter("access_token", LoginManager.getInstance().getToken());
        }*/

        return urlBuilder.build();
    }

    public static @Nullable
    HttpUrl getRequestUrl(@Nullable String url) {
        if (url == null) {
            return null;
        }
        return getRequestUrl(HttpUrl.parse(url));
    }
}

package com.mobile.stu.base.net.retrofit;
import com.mobile.stu.base.net.calladapter.BaseCallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: dourl
 * created on: 2018/8/6 下午4:06
 * description:
 */
public class RetrofitManager {

    //静态的成员
    private static RetrofitManager mInstance = new RetrofitManager();

    public static RetrofitManager get(){
        return  mInstance;
    }
    //私有的构造方法
    private RetrofitManager(){
        mBuilder = new Retrofit.Builder();
    }


    private Retrofit.Builder mBuilder;

    private static boolean Debug =false;

    private Retrofit mRetrofit;


    /**
     * 1
     * @param baseUrl
     * @param client
     */
    public void initRetrofit(String baseUrl, OkHttpClient client){
        mBuilder.baseUrl(baseUrl);
        mBuilder.addConverterFactory(GsonConverterFactory.create());
        mBuilder.addCallAdapterFactory(new BaseCallAdapterFactory());
        initRetrofit(mBuilder,client);
    }

    /**
     *  2初始化方法必须在各种设置完成后最后调用
     * @param builder
     * @param client
     */
    public void initRetrofit(Retrofit.Builder builder,OkHttpClient client){
        mRetrofit = builder.client(client).build();
    }


    public <T> T create(final Class<T> service){
        if (Debug) {
            long start = System.nanoTime();
            T t =  mRetrofit.create(service);
            return t;
        } else {
            return mRetrofit.create(service);
        }
    }

    public final void create(final Class...services) {
        for (Class service : services) {
            if (Debug) {
                long start = System.nanoTime();
                mRetrofit.create(service);
              /* // NHLog.d("create %s cost: %sms", service.getSimpleName(),
                        String.valueOf((System.nanoTime() - start)/ 1000000.0));*/
            } else {
                mRetrofit.create(service);
            }
        }
    }


    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static void enableDebug() {
        Debug = true;
    }
}

package com.mobile.stu.base.net.response;
import com.google.gson.Gson;
import com.mobile.stu.base.net.callback.ICallback;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * author: dourl
 * created on: 2018/8/6 下午5:04
 * description:基础网络请求回调
 */
public abstract  class ResponseCallback <GSON_TYPE> implements ICallback<GSON_TYPE> {
    public static final int NO_NETWORK_STATUS_CODE = -1;
    Request mRequest;
    private boolean mSync;
    private boolean mCacheResponse = false;
    public ResponseCallback() {
    }

    public ResponseCallback(boolean sync) {
        mSync = sync;
    }

    public void onStart() {
    }

    public abstract void onProgress(long bytesWritten, long totalSize, boolean download);

    public abstract GSON_TYPE parseResponse(Response response) throws IOException;

    public Request updateRequestHeaders(Request request) {
        return request;
    }
    public void onCacheLoaded(GSON_TYPE responseJsonType, long time) {
    }

    /**
     * 需要提供Gson对象
     * 用于返回数据解析
     * @return Gson对象
     */
    public abstract Gson getGson();

    /**
     * 返回需要解析的数据对象Class
     * @return 需要解析的数据对象Class
     */
    public abstract Type getClazz();

    @Deprecated
    public Request getRequest() {
        return mRequest;
    }

    @Deprecated
    public void setRequest(Request request) {
        this.mRequest = request;
    }

    public boolean isSync() {
        return mSync;
    }

    public void setSync(boolean sync) {
        mSync = sync;
    }

    @Deprecated
    public boolean isCacheResponse() {
        return mCacheResponse;
    }

    @Deprecated
    public void setCacheResponse(boolean cacheResponse) {
        mCacheResponse = cacheResponse;
    }

}

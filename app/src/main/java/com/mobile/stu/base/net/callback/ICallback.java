package com.mobile.stu.base.net.callback;

import androidx.annotation.Nullable;

/**
 * author: dourl
 * created on: 2018/8/6 下午4:49
 * description: 泛型（GSON_TYPE）接口
 */
public interface ICallback <GSON_TYPE>{
    int NO_NETWORK_STATUS_CODE = -1;

    int OTHER_STATUS_CODE = -2;

    /**
     * 请求成功
     * @param baseData
     */
    void onSuccess(GSON_TYPE baseData);

    /**
     * 请求失败
     * @param statusCode
     * @param failDate   失败的信息
     * @param error      具体的错误
     * @return
     */
    boolean onFail(int statusCode, @Nullable GSON_TYPE failDate, @Nullable Throwable error);

    /**
     * 请求完成，无论失败或者成功，都会调用该方法
     */
    void onFinish();
}

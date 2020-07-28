package com.mobile.stu.base.net.response;

import com.mobile.stu.base.net.model.BaseObject;
import com.mobile.stu.base.net.model.IResponse;

/**
 * author: dourl
 * created on: 2018/10/24 5:01 PM
 * description:
 */
public class BaseResponse implements BaseObject, IResponse {
    public String message;
    public int code = 0;

    @Override
    public boolean isSucceeded() {
        return code == 0;
    }

    @Override
    public int getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}

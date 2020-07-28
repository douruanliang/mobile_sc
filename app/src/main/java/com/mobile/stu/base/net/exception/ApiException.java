package com.mobile.stu.base.net.exception;

/**
 * author: dourl
 * created on: 2018/7/30 下午1:31
 * description:
 */
public class ApiException extends Throwable {
    protected int mCode;
    public ApiException(int code, String message) {
        super(message);
        mCode = code;
    }
    @Override
    public String toString() {
        return super.toString();
    }
}

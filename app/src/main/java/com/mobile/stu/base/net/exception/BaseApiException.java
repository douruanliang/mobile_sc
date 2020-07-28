package com.mobile.stu.base.net.exception;

/**
 * author: dourl
 * created on: 2018/7/30 下午1:33
 * description:
 */
public class BaseApiException extends ApiException {
    //无效的TOKEN
    private static final int INVALIDATE_TOKEN = 401;
    //失效的TOKEN
    private static final int EXPIRE_TOKEN = 402;
    //登录错误
    private static final int SIGN_ERROR = 403;
    //强制更新
    private static final int FORCE_UPGRADE = 10011;

    public BaseApiException(int code, String message) {
        super(code,message);
    }


    public boolean needLogout() {
        return mCode == INVALIDATE_TOKEN || mCode == EXPIRE_TOKEN || mCode == FORCE_UPGRADE || mCode == SIGN_ERROR;
    }

    public boolean forceUpgrade() {
        return mCode == FORCE_UPGRADE;
    }
}

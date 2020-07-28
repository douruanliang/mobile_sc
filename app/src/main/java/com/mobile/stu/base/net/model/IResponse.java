package com.mobile.stu.base.net.model;

/**
 * author: dourl
 * created on: 2018/8/6 下午5:21
 * description: 自定义业务方法
 */
public interface IResponse extends BaseObject {

    /**
     * 业务逻辑上是否成功
     * @return true代表成功
     */
    boolean isSucceeded();

    /**
     * 获取业务逻辑出错代码
     * @return
     */
    int getErrorCode();

    /**
     * 获取出错信息
     * @return
     */
    String getErrorMessage();
}

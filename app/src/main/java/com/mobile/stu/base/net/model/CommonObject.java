package com.mobile.stu.base.net.model;

/**
 * author: dourl
 * created on: 2018/10/24 3:01 PM
 * description: 基础数据内容 可以和具体的业务相关联
 */
public class CommonObject implements BaseObject {

    public int invite_id;
    public int invite_mark;
    public int force_upgrade;
    public String timestamp;
    public boolean force_auth;

    public boolean forceUpdate() {
        return force_upgrade == 1;
    }
}

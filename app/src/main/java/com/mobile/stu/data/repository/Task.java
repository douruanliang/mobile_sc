package com.mobile.stu.data.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * @author: dourl
 * @date: 2020/4/16
 */
@Entity(indices = {@Index(value = {"beVersion", "bePackageName"})})
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "tid")
    private int tid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "path")
    private String path;
    @ColumnInfo(name = "packageName")
    private String packageName;
    @ColumnInfo(name = "versionName")
    private String versionName;
    @ColumnInfo(name = "profileId")
    private int profileId;  // 分别 local \ discover \ pro
    private String beVersion;
    private String bePackageName;
    @ColumnInfo(name = "status")
    private int status; // 状态
    private int env;  // 环境
    private String flavor; //渠道
    private String buildType; //构建类型o
    private String created_time;
    private String created_by;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getBeVersion() {
        return beVersion;
    }

    public void setBeVersion(String beVersion) {
        this.beVersion = beVersion;
    }

    public String getBePackageName() {
        return bePackageName;
    }

    public void setBePackageName(String bePackageName) {
        this.bePackageName = bePackageName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getEnv() {
        return env;
    }

    public void setEnv(int env) {
        this.env = env;
    }

    public Task() {
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}

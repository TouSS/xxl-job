package com.xxl.job.core.biz.model;

public class GroupParam {
    private static final long serialVersionUID = 42L;

    private String appName;
    private String appDesc;

    public GroupParam() {
    }

    public GroupParam(String appName, String appDesc) {
        this.appName = appName;
        this.appDesc = appDesc;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }
}

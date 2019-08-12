package com.hongchuang.ysblibrary.photovideo.bean;

/**
 * 功能描述:
 * author:caitiangui
 * time: 2017/6/16.
 */

public class ConfigBean {

    //token值 ,
    private String upToken;

    // url前缀
    private String url;

    //空间名
    private String bucket;

    private String fieldName;

    public String getToken() {
        return upToken;
    }

    public void setToken(String token) {
        this.upToken = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

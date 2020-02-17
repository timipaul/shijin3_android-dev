package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Copyright (C)
 * FileName: UserSiteBean
 * Date: 2019/5/21 11:28
 * Description: 用户地址表
 */
public class UserSiteBean {

    private String _id;
    //名字
    private String name;
    //电话
    private String phone;
    //地址
    private String address;
    //状态
    private boolean use;
    //地址类型 1家 2公司 3学校
    private int siteType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSiteType() {
        return siteType;
    }

    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}

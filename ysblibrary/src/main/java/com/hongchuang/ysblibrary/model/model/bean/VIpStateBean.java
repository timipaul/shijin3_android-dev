package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Copyright (C)
 * FileName: VIpStateBean
 * Author: m1342
 * Date: 2019/5/17 11:23
 * Description: VIP状态
 */
public class VIpStateBean {

    private String opening_at;//会员开始时间
    private String expiration_at;//会员结束时间
    private String try_member;//是否领取过免费会员

    public String getOpening_at() {
        return opening_at;
    }

    public void setOpening_at(String opening_at) {
        this.opening_at = opening_at;
    }

    public String getExpiration_at() {
        return expiration_at;
    }

    public void setExpiration_at(String expiration_at) {
        this.expiration_at = expiration_at;
    }

    public String getTry_member() {
        return try_member;
    }

    public void setTry_member(String try_member) {
        this.try_member = try_member;
    }

    @Override
    public String toString() {
        return "VIpStateBean{" +
                "opening_at='" + opening_at + '\'' +
                ", expiration_at='" + expiration_at + '\'' +
                ", try_member='" + try_member + '\'' +
                '}';
    }
}

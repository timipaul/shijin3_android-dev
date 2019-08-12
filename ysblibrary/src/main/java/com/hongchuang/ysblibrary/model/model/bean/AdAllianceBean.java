package com.hongchuang.ysblibrary.model.model.bean;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

/**
 * Copyright (C)
 * FileName: AdAllianceBean
 * Author: m1342
 * Date: 2019/7/11 11:21
 * Description: 广告联盟推广
 */
public class AdAllianceBean {

    private int id;
    //广告图片
    private String img_url;
    //广告标题
    private String name;
    //广告详情
    private String details;
    //广告价格
    private String price;
    //折扣前价格
    private String coupon_price;
    //优惠劵领取地址
    private String coupon_url;
    //购买地址
    private String buy_url;

    AdAllianceBean(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBuy_url() {
        return buy_url;
    }

    public void setBuy_url(String buy_url) {
        this.buy_url = buy_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getCoupon_url() {
        return coupon_url;
    }

    public void setCoupon_url(String coupon_url) {
        this.coupon_url = coupon_url;
    }

    @Override
    public String toString() {
        return "AdAllianceBean{" +
                "id=" + id +
                ", img_url='" + img_url + '\'' +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", price='" + price + '\'' +
                ", coupon_price='" + coupon_price + '\'' +
                ", coupon_url='" + coupon_url + '\'' +
                ", buy_url='" + buy_url + '\'' +
                '}';
    }
}

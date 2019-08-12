package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Copyright (C)
 * FileName: VipRechangeBean
 * Author: m1342
 * Date: 2019/5/12 15:18
 * Description: Vip充值实体
 */
public class VipRechangeBean {

    private int id;
    private String title;
    private int price;
    private int old_price;
    //0首次 1非首次
    private int first_vip;
    private double red_package_number;
    private String get_time;
    //会员金币获取数量
    private String member_give_points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOld_price() {
        return old_price;
    }

    public void setOld_price(int old_price) {
        this.old_price = old_price;
    }

    public int getFirst_vip() {
        return first_vip;
    }

    public void setFirst_vip(int first_vip) {
        this.first_vip = first_vip;
    }

    public double getRed_package_number() {
        return red_package_number;
    }

    public void setRed_package_number(double red_package_number) {
        this.red_package_number = red_package_number;
    }

    public String getGet_time() {
        return get_time;
    }

    public void setGet_time(String get_time) {
        this.get_time = get_time;
    }

    public String getMember_give_points() {
        return member_give_points;
    }

    public void setMember_give_points(String member_give_points) {
        this.member_give_points = member_give_points;
    }

    @Override
    public String toString() {
        return "VipRechangeBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", old_price=" + old_price +
                ", first_vip=" + first_vip +
                '}';
    }
}

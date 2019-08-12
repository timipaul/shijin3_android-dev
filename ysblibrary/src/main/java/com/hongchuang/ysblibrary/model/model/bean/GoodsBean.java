package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/29.
 * 商品表
 */

public class GoodsBean {
    public String id;
    public String goods_title;
    public String goods_imgurl;
    public String allowable_exchange_number;
    public String exchange_points;
    public String exchange_number;
    public String status;
    public String created_at;
    public String able_exchange_number;
    public String over_time;
    public String zero_shop_number;//抢购数量

    public String goods_describe_one;
    public String goods_describe_two;
    public String goods_describe_three;

    public String getGoods_describe_one() {
        return goods_describe_one;
    }

    public void setGoods_describe_one(String goods_describe_one) {
        this.goods_describe_one = goods_describe_one;
    }

    public String getGoods_describe_two() {
        return goods_describe_two;
    }

    public void setGoods_describe_two(String goods_describe_two) {
        this.goods_describe_two = goods_describe_two;
    }

    public String getGoods_describe_three() {
        return goods_describe_three;
    }

    public void setGoods_describe_three(String goods_describe_three) {
        this.goods_describe_three = goods_describe_three;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_imgurl() {
        return goods_imgurl;
    }

    public void setGoods_imgurl(String goods_imgurl) {
        this.goods_imgurl = goods_imgurl;
    }

    public String getAllowable_exchange_number() {
        return allowable_exchange_number;
    }

    public void setAllowable_exchange_number(String allowable_exchange_number) {
        this.allowable_exchange_number = allowable_exchange_number;
    }

    public String getExchange_point() {
        return exchange_points;
    }

    public void setExchange_point(String exchange_point) {
        this.exchange_points = exchange_point;
    }

    public String getExchange_number() {
        return exchange_number;
    }

    public void setExchange_number(String exchange_number) {
        this.exchange_number = exchange_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String modified_at;

    public String getAble_exchange_number() {
        return able_exchange_number;
    }

    public void setAble_exchange_number(String able_exchange_number) {
        this.able_exchange_number = able_exchange_number;
    }

    public void setExchange_points(String exchange_points) {
        this.exchange_points = exchange_points;
    }

    public String getExchange_points() {
        return exchange_points;
    }

    public String getOver_time() {
        return over_time;
    }

    public void setOver_time(String over_time) {
        this.over_time = over_time;
    }

    public String getZero_shop_number() {
        return zero_shop_number;
    }

    public void setZero_shop_number(String zero_shop_number) {
        this.zero_shop_number = zero_shop_number;
    }
}

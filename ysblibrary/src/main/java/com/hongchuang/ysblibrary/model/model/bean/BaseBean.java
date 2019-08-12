package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class BaseBean<T> {

    public List<T> comments;
    public List<T> goods;
    public String lottery_activity_time;
    public List<T> vip_setting;
    public List<T> result;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public List<T> getVip_setting() {
        return vip_setting;
    }

    public void setVip_setting(List<T> vip_setting) {
        this.vip_setting = vip_setting;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public List<T> records;

    public List<T> getLotteries() {
        return lotteries;
    }

    public void setLotteries(List<T> lotteries) {
        this.lotteries = lotteries;
    }

    public List<T> lotteries;

    public List<T> getUsers() {
        return users;
    }

    public void setUsers(List<T> users) {
        this.users = users;
    }

    public List<T> users;

    public T getAd() {
        return ad;
    }

    public void setAd(T ad) {
        this.ad = ad;
    }

    public T ad;
    public List<T> getComments() {
        return comments;
    }

    public void setComments(List<T> comments) {
        this.comments = comments;
    }



    public String getLottery_activity_time() {
        return lottery_activity_time;
    }

    public void setLottery_activity_time(String lottery_activity_time) {
        this.lottery_activity_time = lottery_activity_time;
    }

    public List<T> getDraw_lottery_list() {
        return draw_lottery_list;
    }

    public void setDraw_lottery_list(List<T> draw_lottery_list) {
        this.draw_lottery_list = draw_lottery_list;
    }

    public List<T> draw_lottery_list;

    public T getReward_lottery() {
        return reward_lottery;
    }

    public void setReward_lottery(T reward_lottery) {
        this.reward_lottery = reward_lottery;
    }

    public T reward_lottery;

    public List<T> getFans() {
        return fans;
    }

    public void setFans(List<T> fans) {
        this.fans = fans;
    }

    public List<T> fans;

    public List<T> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<T> invitations) {
        this.invitations = invitations;
    }

    public List<T> invitations;

    public List<T> getGoods() {
        return goods;
    }

    public void setGoods(List<T> goods) {
        this.goods = goods;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String cursor;

    public String getTotal_change() {
        return total_change;
    }

    public void setTotal_change(String total_change) {
        this.total_change = total_change;
    }

    public String total_change;

    public String getTotal_size() {
        return total_size;
    }

    public void setTotal_size(String total_size) {
        this.total_size = total_size;
    }

    public String total_size;

    public List<CategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesBean> categories) {
        this.categories = categories;
    }

    public List<CategoriesBean> categories;
}

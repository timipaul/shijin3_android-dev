package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/13.
 */

public class TodayFollowBean {
    private String today_follow_number;

    public String getToday_follow_number() {
        return today_follow_number;
    }

    public void setToday_follow_number(String today_follow_number) {
        this.today_follow_number = today_follow_number;
    }

    public String getToday_unfollow_number() {
        return today_unfollow_number;
    }

    public void setToday_unfollow_number(String today_unfollow_number) {
        this.today_unfollow_number = today_unfollow_number;
    }

    private String today_unfollow_number;
}

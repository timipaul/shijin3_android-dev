package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/27.
 */

public class AttachBean {
    public String mode;
    public String ad_id;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }


    public Object getReward_plan() {
        return reward_plan;
    }

    public void setReward_plan(Object reward_plan) {
        this.reward_plan = reward_plan;
    }

    public Object reward_plan;
}

package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/26.
 */

public class RewardPlan{
    public String total_number;
    public String people_number;

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }

    public String getPeople_number() {
        return people_number;
    }

    public void setPeople_number(String people_number) {
        this.people_number = people_number;
    }

    public String getReward_mode() {
        return reward_mode;
    }

    public void setReward_mode(String reward_mode) {
        this.reward_mode = reward_mode;
    }

    public String reward_mode;
}
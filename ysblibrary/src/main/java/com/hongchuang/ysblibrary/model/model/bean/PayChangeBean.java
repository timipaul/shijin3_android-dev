package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/26.
 */

public class PayChangeBean {
    public String mode;
    public String change;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public RewardPlan getReward_plan() {
        return reward_plan;
    }

    public void setReward_plan(RewardPlan reward_plan) {
        this.reward_plan = reward_plan;
    }

    public RewardPlan reward_plan;


}

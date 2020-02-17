package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/8/27.
 */

public class AnswersBean {
    public String is_right;



    public String getIs_reward() {
        return is_reward;
    }

    public void setIs_reward(String is_reward) {
        this.is_reward = is_reward;
    }

    public String is_reward;

    public String getIs_right() {
        return is_right;
    }

    public void setIs_right(String is_right) {
        this.is_right = is_right;
    }


    public List<String> getAnswers_right() {
        return answers_right;
    }

    public void setAnswers_right(List<String> answers_right) {
        this.answers_right = answers_right;
    }

    public List<String> answers_right;

    public String getReward_number() {
        return reward_number;
    }

    public void setReward_number(String reward_number) {
        this.reward_number = reward_number;
    }

    public String reward_number;

    public String reward_number_add;

    public String getReward_number_add() {
        return reward_number_add;
    }

    public void setReward_number_add(String reward_number_add) {
        this.reward_number_add = reward_number_add;
    }
}

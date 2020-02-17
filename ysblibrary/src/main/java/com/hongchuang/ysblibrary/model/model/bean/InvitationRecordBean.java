package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InvitationRecordBean {
    public String nickname;
    public String reward_mode;
    public String inviter_reward;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReward_mode() {
        return reward_mode;
    }

    public void setReward_mode(String reward_mode) {
        this.reward_mode = reward_mode;
    }

    public String getInviter_reward() {
        return inviter_reward;
    }

    public void setInviter_reward(String inviter_reward) {
        this.inviter_reward = inviter_reward;
    }

    public String getInvite_people_number() {
        return invite_people_number;
    }

    public void setInvite_people_number(String invite_people_number) {
        this.invite_people_number = invite_people_number;
    }

    public String invite_people_number;
}

package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InvitationInfoBean {
    public String invitation_code;

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getInvite_change_number() {
        return invite_change_number;
    }

    public void setInvite_change_number(String invite_change_number) {
        this.invite_change_number = invite_change_number;
    }

    public String getInvite_people_number() {
        return invite_people_number;
    }

    public void setInvite_people_number(String invite_people_number) {
        this.invite_people_number = invite_people_number;
    }

    public String getSlogan_pic() {
        return slogan_pic;
    }

    public void setSlogan_pic(String slogan_pic) {
        this.slogan_pic = slogan_pic;
    }

    public RewardRule getReward_rule() {
        return reward_rule;
    }

    public void setReward_rule(RewardRule reward_rule) {
        this.reward_rule = reward_rule;
    }

    public String invite_change_number;
    public String invite_people_number;
    public String slogan_pic;
    public RewardRule reward_rule;
    public class RewardRule{
        public String getRule1() {
            return rule1;
        }

        public void setRule1(String rule1) {
            this.rule1 = rule1;
        }

        public String getRole2() {
            return rule2;
        }

        public void setRole2(String role2) {
            this.rule2 = role2;
        }

        public String rule1;
        public String rule2;
    }
}

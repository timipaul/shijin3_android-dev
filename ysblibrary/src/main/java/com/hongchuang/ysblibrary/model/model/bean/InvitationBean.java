package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InvitationBean {
    public String id;
    public String invitation_channel;
    public String reward_mode;
    public String inviter_reward;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvitation_channel() {
        return invitation_channel;
    }

    public void setInvitation_channel(String invitation_channel) {
        this.invitation_channel = invitation_channel;
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

    public String getInvitee_reward() {
        return invitee_reward;
    }

    public void setInvitee_reward(String invitee_reward) {
        this.invitee_reward = invitee_reward;
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

    public Inviter getInviter() {
        return inviter;
    }

    public void setInviter(Inviter inviter) {
        this.inviter = inviter;
    }

    public Invitee getInvitee() {
        return invitee;
    }

    public void setInvitee(Invitee invitee) {
        this.invitee = invitee;
    }

    public String status;
    public String created_at;
    public String modified_at;
    public String invitee_username;
    public String user_img;
    public String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getInvitee_username() {
        return invitee_username;
    }

    public void setInvitee_username(String invitee_username) {
        this.invitee_username = invitee_username;
    }

    public String getInvitation_status() {
        return invitation_status;
    }

    public void setInvitation_status(String invitation_status) {
        this.invitation_status = invitation_status;
    }

    public String invitation_status;
    public String invitee_reward;
    public Inviter inviter;
    public class Inviter{
        public String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String nickname;
    }
    public Invitee invitee;
    public class Invitee{
        public String id;
        public String nickname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String username;
    }
}

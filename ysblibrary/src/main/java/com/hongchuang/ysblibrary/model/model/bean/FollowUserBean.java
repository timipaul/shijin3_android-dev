package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/21.
 */

public class FollowUserBean {
    public String id;
    public String nickname;
    public String imgurl;
    public String fan_number;
    public String is_follow;

    public String getIs_advertiser() {
        return is_advertiser;
    }

    public void setIs_advertiser(String is_advertiser) {
        this.is_advertiser = is_advertiser;
    }

    public String is_advertiser;
    public String created_at;

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

    public String getImageurl() {
        return imgurl;
    }

    public void setImageurl(String imageurl) {
        this.imgurl = imageurl;
    }

    public String getFan_number() {
        return fan_number;
    }

    public void setFan_number(String fan_number) {
        this.fan_number = fan_number;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
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
}

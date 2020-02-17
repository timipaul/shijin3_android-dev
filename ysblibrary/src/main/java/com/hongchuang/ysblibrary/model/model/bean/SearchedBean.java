package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/9/5.
 */

public class SearchedBean {
    public List<Ads> getAds() {
        return ads;
    }

    public void setAds(List<Ads> ads) {
        this.ads = ads;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<Ads> ads;
    public List<Users> users;
    public class Users{
        public String id;
        public String username;
        public String nickname;
        public String imgurl;
        public String fan_number;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getIs_advertiser() {
            return is_advertiser;
        }

        public void setIs_advertiser(String is_advertiser) {
            this.is_advertiser = is_advertiser;
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

        public String is_follow;
        public String is_advertiser;
        public String created_at;
        public String modified_at;
    }
}

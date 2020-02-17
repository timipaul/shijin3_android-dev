package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/9/5.
 */

public class UserDetailBean {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAnswer_cursor() {
        return answer_cursor;
    }

    public void setAnswer_cursor(String answer_cursor) {
        this.answer_cursor = answer_cursor;
    }

    public String getRelease_cursor() {
        return release_cursor;
    }

    public void setRelease_cursor(String release_cursor) {
        this.release_cursor = release_cursor;
    }

    public List<Ads> getAnswer_ads() {
        return answer_ads;
    }

    public void setAnswer_ads(List<Ads> answer_ads) {
        this.answer_ads = answer_ads;
    }

    public List<Ads> getRelease_ads() {
        return release_ads;
    }

    public void setRelease_ads(List<Ads> release_ads) {
        this.release_ads = release_ads;
    }

    public User user;
    public class User{
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

        public String getIs_advertiser() {
            return is_advertiser;
        }

        public void setIs_advertiser(String is_advertiser) {
            this.is_advertiser = is_advertiser;
        }

        public String getAd_release_number() {
            return ad_release_number;
        }

        public void setAd_release_number(String ad_release_number) {
            this.ad_release_number = ad_release_number;
        }

        public String getFan_number() {
            return fan_number;
        }

        public void setFan_number(String fan_number) {
            this.fan_number = fan_number;
        }

        public String getFollow_people_number() {
            return follow_people_number;
        }

        public void setFollow_people_number(String follow_people_number) {
            this.follow_people_number = follow_people_number;
        }

        public String getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(String is_follow) {
            this.is_follow = is_follow;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String is_advertiser;
        public String ad_release_number;
        public String fan_number;
        public String follow_people_number;
        public String is_follow;
        public String imgurl;

        public String getIs_black() {
            return is_black;
        }

        public void setIs_black(String is_black) {
            this.is_black = is_black;
        }

        public String is_black;
    }
    public String answer_cursor;
    public String release_cursor;
    public List<Ads> answer_ads;
    public List<Ads> release_ads;

}

package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/8/26.
 */

public class AdBean  {
//            "interests": [
//            "game",
//            "sports"
//            ],
//            "reward_mode|+1": [
//            "change",
//            "point"
//            ],
//            "reward": 0.15,
//            "status": "on",
//            "adshow|1": [
//            "on",
//            "off"
//            ],
//            "answer_number": "@natural(10000,99499)",
//            "ad_like_number": "@natural(10040,99599)",
//            "ad_share_number": "@natural(10000,99499)",
//            "ad_comment_number": "@natural(10000,99959)",
//            "ad_collection_number": "@natural(10000,94999)",
//            "ad_content": "@cparagraph()",
//            "question": "@csentence()",
//            "options": {
//        "option1": "@csentence()",
//                "option2": "@csentence()",
//                "option3": "@csentence()",
//                "option4": "@csentence()"
//    },
//            "url": "@url('http')",
//            "is_follow|1": [
//            "on",
//            "off"
//            ],
//            "is_like|1": [
//            "on",
//            "off"
//            ],
//            "is_collection|1": [
//            "on",
//            "off"
//            ],
//            "is_answer|1": [
//            "off",
//            "first_answer",
//            "second_answer"
//            ],
//            "created_at": "1532848156",
//            "modified_at": "1532848156"
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAd_mode() {
        return ad_mode;
    }

    public void setAd_mode(String ad_mode) {
        this.ad_mode = ad_mode;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public List<String> getAd_title_pics() {
        return ad_title_pics;
    }

    public void setAd_title_pics(List<String> ad_title_pics) {
        this.ad_title_pics = ad_title_pics;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getReward_mode() {
        return reward_mode;
    }

    public void setReward_mode(String reward_mode) {
        this.reward_mode = reward_mode;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdshow() {
        return adshow;
    }

    public void setAdshow(String adshow) {
        this.adshow = adshow;
    }

    public String getAnswer_number() {
        return answer_number;
    }

    public void setAnswer_number(String answer_number) {
        this.answer_number = answer_number;
    }

    public String getAd_like_number() {
        return ad_like_number;
    }

    public void setAd_like_number(String ad_like_number) {
        this.ad_like_number = ad_like_number;
    }

    public String getAd_share_number() {
        return ad_share_number;
    }

    public void setAd_share_number(String ad_share_number) {
        this.ad_share_number = ad_share_number;
    }

    public String getAd_comment_number() {
        return ad_comment_number;
    }

    public void setAd_comment_number(String ad_comment_number) {
        this.ad_comment_number = ad_comment_number;
    }

    public String getAd_collection_number() {
        return ad_collection_number;
    }

    public void setAd_collection_number(String ad_collection_number) {
        this.ad_collection_number = ad_collection_number;
    }

    public String getAd_content() {
        return ad_content;
    }

    public void setAd_content(String ad_content) {
        this.ad_content = ad_content;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(String is_collection) {
        this.is_collection = is_collection;
    }

    public String getIs_answer() {
        return is_answer;
    }

    public void setIs_answer(String is_answer) {
        this.is_answer = is_answer;
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

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getIs_advertiser() {
            return is_advertiser;
        }

        public void setIs_advertiser(String is_advertiser) {
            this.is_advertiser = is_advertiser;
        }

        public String imgurl;
        public String is_advertiser;

        @Override
        public String toString() {
            return "User{" +
                    "id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", imgurl='" + imgurl + '\'' +
                    ", is_advertiser='" + is_advertiser + '\'' +
                    '}';
        }
    }

    public String ad_mode;
    public String ad_type;
    public List<String> ad_title_pics;
    public String ad_title;
    public List<String> interests;
    public String reward_mode;
    public String reward;
    public String status;
    public String adshow;
    public String answer_number;
    public String ad_like_number;
    public String ad_share_number;
    public String ad_comment_number;
    public String ad_collection_number;
    public String ad_content;
    public String question;
    public Options options;
    public class Options{
        public String option1;

        public String getOption1() {
            return option1;
        }

        public void setOption1(String option1) {
            this.option1 = option1;
        }

        public String getOption2() {
            return option2;
        }

        public void setOption2(String option2) {
            this.option2 = option2;
        }

        public String getOption3() {
            return option3;
        }

        public void setOption3(String option3) {
            this.option3 = option3;
        }

        public String getOption4() {
            return option4;
        }

        public void setOption4(String option4) {
            this.option4 = option4;
        }

        public String option2;
        public String option3;
        public String option4;
    }
    public String url;
    public String is_follow;
    public String is_like;
    public String is_collection;
    public String is_answer;
    public String created_at;
    public String modified_at;


    @Override
    public String toString() {
        return "AdBean{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", ad_mode='" + ad_mode + '\'' +
                ", ad_type='" + ad_type + '\'' +
                ", ad_title_pics=" + ad_title_pics +
                ", ad_title='" + ad_title + '\'' +
                ", interests=" + interests +
                ", reward_mode='" + reward_mode + '\'' +
                ", reward='" + reward + '\'' +
                ", status='" + status + '\'' +
                ", adshow='" + adshow + '\'' +
                ", answer_number='" + answer_number + '\'' +
                ", ad_like_number='" + ad_like_number + '\'' +
                ", ad_share_number='" + ad_share_number + '\'' +
                ", ad_comment_number='" + ad_comment_number + '\'' +
                ", ad_collection_number='" + ad_collection_number + '\'' +
                ", ad_content='" + ad_content + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", url='" + url + '\'' +
                ", is_follow='" + is_follow + '\'' +
                ", is_like='" + is_like + '\'' +
                ", is_collection='" + is_collection + '\'' +
                ", is_answer='" + is_answer + '\'' +
                ", created_at='" + created_at + '\'' +
                ", modified_at='" + modified_at + '\'' +
                '}';
    }
}

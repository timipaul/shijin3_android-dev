package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Copyright (C)
 * FileName: VoteBean
 * Author: paul
 * Date: 2019/6/14 14:40
 * Description: 用户投票
 */
public class VoteBean {

    private int id;
    private String url;//跳转地址
    private String img_url;//图片地址
    private String rewards_number;//投票奖励数量
    private String end_time;    //截止时间
    private String created_at;  //投票时间
    private String voted;  //投票状态
    private String count;      //投票数量
    private Content content;      //投票选择数据
    private String title;

    public VoteBean(){}

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Content{
        private List<Info> info;
        private String count;
        private String update;
        private String title;   //投票名称
        private String voteType;  //投票类型 //1文字 2图片

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVoteType() {
            return voteType;
        }



        public void setVoteType(String voteType) {
            this.voteType = voteType;
        }

        public List<Info> getInfo() {
            return info;
        }

        public void setInfo(List<Info> info) {
            this.info = info;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "info=" + info +
                    ", count='" + count + '\'' +
                    ", update='" + update + '\'' +
                    '}';
        }
    }

    public static class Info{
        private List<Option> option;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Option> getOption() {
            return option;
        }

        public void setOption(List<Option> option) {
            this.option = option;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "option=" + option +
                    '}';
        }
    }

    public static class Option{
        private String title;
        private String count;
        private String index;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRewards_number() {
        return rewards_number;
    }

    public void setRewards_number(String rewards_number) {
        this.rewards_number = rewards_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "VoteBean{" +
                "id=" + id +
                ", end_time='" + end_time + '\'' +
                ", voted='" + voted + '\'' +
                ", count='" + count + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

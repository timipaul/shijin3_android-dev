package com.hongchuang.ysblibrary.model.model.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C)
 * FileName: StoreBean
 * Author: Administrator
 * Date: 2019/9/9 0009 上午 10:44
 * Description: 商城信息
 */
public class StoreBean {

    private String _id;
    private List<Val> val;
    private String like;
    private String comment;
    private String play;
    private String title;
    private String url;
    private String homeImg;

    public class Val{
        private String url;
        private String color;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Val> getVal() {
        return val;
    }

    public void setVal(List<Val> val) {
        this.val = val;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    @Override
    public String toString() {
        return "StoreBean{" +
                "val=" + val +
                ", like='" + like + '\'' +
                ", comment='" + comment + '\'' +
                ", play='" + play + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

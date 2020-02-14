package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/29.
 */

public class PointDetailBean {
    public String id;
    public String user_id;
    public String channel;
    public String created_at;
    public String modified_at;
    public ChannelInfo channel_info;
    public String points;
    public String change;
    public String status;

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


    public ChannelInfo getChannel_info() {
        return channel_info;
    }

    public void setChannel_info(ChannelInfo channel_info) {
        this.channel_info = channel_info;
    }

    public class ChannelInfo{
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String title;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

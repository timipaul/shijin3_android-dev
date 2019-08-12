package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/13.
 */

public class FollowNumberBean {
    public String id;
    public String follow_number;
    public String time;
    public String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollow_number() {
        return follow_number;
    }

    public void setFollow_number(String follow_number) {
        this.follow_number = follow_number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

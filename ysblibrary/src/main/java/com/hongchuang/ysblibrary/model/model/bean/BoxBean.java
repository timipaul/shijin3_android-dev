package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/30.
 */

public class BoxBean {
    public String id;
    public String user_id;
    public String lottery_id;
    public String lottery_name;
    public String lottery_use_description;
    public String lottery_mode;
    public String status;
    public String created_at;

    public String getLottery_code() {
        return lottery_code;
    }

    public void setLottery_code(String lottery_code) {
        this.lottery_code = lottery_code;
    }

    public String lottery_code;

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String expire_time;

    public String getLottery_status() {
        return lottery_status;
    }

    public void setLottery_status(String lottery_status) {
        this.lottery_status = lottery_status;
    }

    public String lottery_status;

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

    public String getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(String lottery_id) {
        this.lottery_id = lottery_id;
    }

    public String getLottery_name() {
        return lottery_name;
    }

    public void setLottery_name(String lottery_name) {
        this.lottery_name = lottery_name;
    }

    public String getLottery_use_description() {
        return lottery_use_description;
    }

    public void setLottery_use_description(String lottery_use_description) {
        this.lottery_use_description = lottery_use_description;
    }

    public String getLottery_mode() {
        return lottery_mode;
    }

    public void setLottery_mode(String lottery_mode) {
        this.lottery_mode = lottery_mode;
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

    public String modified_at;
}

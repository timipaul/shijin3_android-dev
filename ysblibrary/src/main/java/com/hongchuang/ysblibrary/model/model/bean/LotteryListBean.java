package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/3.
 */

public class LotteryListBean {
    public String id;
    public String lottery_name;
    public String lottery_imgurl;
    public String lottery_big_imgurl;
    public String lottery_mode;

    public String getLottery_value() {
        return lottery_value;
    }

    public void setLottery_value(String lottery_value) {
        this.lottery_value = lottery_value;
    }

    public String lottery_value;

    public String getLottery_code() {
        return lottery_code;
    }

    public void setLottery_code(String lottery_code) {
        this.lottery_code = lottery_code;
    }

    public String lottery_code;

    public String getLotteryId() {
        return id;
    }

    public void setLotteryId(String lottery_id) {
        this.id = lottery_id;
    }

    public String getLottery_name() {
        return lottery_name;
    }

    public void setLottery_name(String lottery_name) {
        this.lottery_name = lottery_name;
    }

    public String getLottery_imgurl() {
        return lottery_imgurl;
    }

    public void setLottery_imgurl(String lottery_imgurl) {
        this.lottery_imgurl = lottery_imgurl;
    }

    public String getLottery_big_imgurl() {
        return lottery_big_imgurl;
    }

    public void setLottery_big_imgurl(String lottery_big_imgurl) {
        this.lottery_big_imgurl = lottery_big_imgurl;
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

    public String status;
    public String created_at;
    public String modified_at;
}

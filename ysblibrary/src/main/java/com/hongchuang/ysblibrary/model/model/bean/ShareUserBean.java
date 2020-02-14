package com.hongchuang.ysblibrary.model.model.bean;

/**
 * 爱分享用户信息
 */
public class ShareUserBean {

    private String _id;
    private String username;
    //当前分享次数
    private int now_num;
    //总次数
    private int sum_num;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNow_num() {
        return now_num;
    }

    public void setNow_num(int now_num) {
        this.now_num = now_num;
    }

    public int getSum_num() {
        return sum_num;
    }

    public void setSum_num(int sum_num) {
        this.sum_num = sum_num;
    }
}

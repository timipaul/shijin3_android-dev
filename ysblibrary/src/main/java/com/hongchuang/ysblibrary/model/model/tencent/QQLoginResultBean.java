package com.hongchuang.ysblibrary.model.model.tencent;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/6/26
 ***/

public class QQLoginResultBean {
    private String nickName;
    private String avatar;
    private String openId;

    public QQLoginResultBean() {
    }

    public QQLoginResultBean(String nickName, String avatar, String openId) {
        this.nickName = nickName;
        this.avatar = avatar;
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}

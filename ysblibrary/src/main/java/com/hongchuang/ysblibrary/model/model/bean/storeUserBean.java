package com.hongchuang.ysblibrary.model.model.bean;


/** 商城用户信息 */
public class storeUserBean {

    //余额
    private String cash;
    //会员状态 会员状态（'ORDINARY', 'MEMBER', 'PARTNER'）
    private String privilege;
    //粉丝
    private String fans;
    //关注数量
    private String follow;
    //点赞数量
    private String like;


    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}

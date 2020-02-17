package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/9/5.
 */

public class FollowDetailBean {
    public List<FollowUserBean> users;

    public List<FollowUserBean> getUsers() {
        return users;
    }

    public void setUsers(List<FollowUserBean> users) {
        this.users = users;
    }

    public List<FollowUserBean> getFans() {
        return fans;
    }

    public void setFans(List<FollowUserBean> fans) {
        this.fans = fans;
    }

    public List<FollowUserBean> fans;
}

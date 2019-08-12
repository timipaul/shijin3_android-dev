package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 */

public class ShieidsBean {
    public boolean see;
    public boolean interested;
    public boolean content_level;
    public String black_user_id;

    public ShieidsBean(boolean see,boolean interested,boolean content_level,String black_user_id,List<String> black_interests){
        this.see = see;
        this.interested = interested;
        this.content_level = content_level;
        this.black_user_id = black_user_id;
        this.black_interests = black_interests;

    }
    public boolean isSee() {
        return see;
    }

    public void setSee(boolean see) {
        this.see = see;
    }

    public boolean isInterested() {
        return interested;
    }

    public void setInterested(boolean interested) {
        this.interested = interested;
    }

    public boolean isContent_level() {
        return content_level;
    }

    public void setContent_level(boolean content_level) {
        this.content_level = content_level;
    }

    public String getBlack_user_id() {
        return black_user_id;
    }

    public void setBlack_user_id(String black_user_id) {
        this.black_user_id = black_user_id;
    }

    public List<String> getBlack_interests() {
        return black_interests;
    }

    public void setBlack_interests(List<String> black_interests) {
        this.black_interests = black_interests;
    }

    public List<String> black_interests;
}

package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/27.
 */

public class WXPayBean {
    public String mode;
    public String channel;
    public String change;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public Object getAttach() {
        return attach;
    }

    public void setAttach(Object attach) {
        this.attach = attach;
    }

    public Object attach;
}

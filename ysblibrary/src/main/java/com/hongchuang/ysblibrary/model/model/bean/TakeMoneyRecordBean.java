package com.hongchuang.ysblibrary.model.model.bean;


/** 提现记录 */
public class TakeMoneyRecordBean {

    //零钱
    private String change;
    //来源
    private String cause;
    //状态
    private String state;

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/19.
 */

public class InterestBean {
    public String interest;

    public InterestBean(String interest, String name, boolean isSelect) {
        this.interest = interest;
        this.name = name;
        this.isSelect = isSelect;
    }

    public String name;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect;
}

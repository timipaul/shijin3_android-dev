package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/18.
 */

public class ReportBean {
    private String name;

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

    private boolean isSelect=false;

    public ReportBean(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }
}

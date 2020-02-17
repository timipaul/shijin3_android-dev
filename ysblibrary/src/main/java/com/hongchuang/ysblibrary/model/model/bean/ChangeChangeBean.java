package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/11/26.
 */

public class ChangeChangeBean {
    public String change;
    public String point;

    public ChangeChangeBean(String change, String point, boolean select) {
        this.change = change;
        this.point = point;
        this.select = select;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean select=false;
}

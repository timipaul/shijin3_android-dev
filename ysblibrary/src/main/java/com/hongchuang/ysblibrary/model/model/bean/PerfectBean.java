package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by Administrator on 2018/7/27.
 */

public class PerfectBean {

    public PerfectBean(int res,String name,String interest){
        this.res=res;
        this.name = name;
        this.interest = interest;
    }
    public int res;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String interest;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean check=false;
}

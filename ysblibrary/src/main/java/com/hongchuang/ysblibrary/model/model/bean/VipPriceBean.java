package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Copyright (C)
 * FileName: VipPriceBean
 * Author: m1342
 * Date: 2019/5/16 16:03
 * Description: vip价格信息
 */
public class VipPriceBean {

    private String sum;
    private String type;        //  类型
    private String tooltip;     // 标题
    private String gold_add;    // 金币加成比例
    private String original_price;// 原价




    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getGold_add() {
        return gold_add;
    }

    public void setGold_add(String gold_add) {
        this.gold_add = gold_add;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    @Override
    public String toString() {
        return "VipPriceBean{" +
                "sum='" + sum + '\'' +
                ", type='" + type + '\'' +
                ", tooltip='" + tooltip + '\'' +
                ", gold_add='" + gold_add + '\'' +
                ", original_price='" + original_price + '\'' +
                '}';
    }
}

package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Copyright (C)
 * FileName: StoreRushBean
 * Author: Administrator
 * Date: 2019/9/9 0009 下午 17:08
 * Description: 商城抢购信息
 */
public class StoreRushBean {

    //抢购
    private GoodsData now;
    //即将抢购
    private GoodsData next;

    public GoodsData getNow() {
        return now;
    }

    public void setNow(GoodsData now) {
        this.now = now;
    }

    public GoodsData getNext() {
        return next;
    }

    public void setNext(GoodsData next) {
        this.next = next;
    }

    public class GoodsData{
        private String _id;
        private List<StoreGoodsBean> goods;
        private long beginTime;
        private long endTime;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public List<StoreGoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<StoreGoodsBean> goods) {
            this.goods = goods;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }



}

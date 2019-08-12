package com.hongchuang.hclibrary.rxbus;

/***
 * 功能描述:RxBus消息订阅
 * 作者:qiujialiu
 * 时间:2016/12/15
 * 版本:1.0
 ***/

public class SubscriptionBean {
    public static <T> RxBusSendBean createSendBean(int type, T t) {
        RxBusSendBean<T> busSendBean = new RxBusSendBean();
        busSendBean.type = type;
        busSendBean.content = t;
        return busSendBean;
    }

    public static class RxBusSendBean<T> {
        public int type;
        public T content;
    }

}

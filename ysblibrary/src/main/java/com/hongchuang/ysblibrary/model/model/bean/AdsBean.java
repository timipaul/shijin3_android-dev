package com.hongchuang.ysblibrary.model.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class AdsBean implements Serializable{
    public String cursor;

    public AdsBean(String cursor, List<Ads> ads, String total_size) {
        this.cursor = cursor;
        this.ads = ads;
        this.total_size = total_size;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<Ads> getAds() {
        return ads;
    }

    public void setAds(List<Ads> ads) {
        this.ads = ads;
    }

    public List<Ads> ads;

    public List<Ads> getRelease_ads() {
        return release_ads;
    }

    public void setRelease_ads(List<Ads> release_ads) {
        this.release_ads = release_ads;
    }

    public List<Ads> getAnswer_ads() {
        return answer_ads;
    }

    public void setAnswer_ads(List<Ads> answer_ads) {
        this.answer_ads = answer_ads;
    }

    public List<Ads> release_ads;
    public List<Ads> answer_ads;

    public String getTotal_size() {
        return total_size;
    }

    public void setTotal_size(String total_size) {
        this.total_size = total_size;
    }

    public String total_size;


}

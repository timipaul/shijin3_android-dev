package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class BannerBean {
    public List<Banners> getBanners() {
        return banners;
    }

    public void setBanners(List<Banners> banners) {
        this.banners = banners;
    }

    public List<Banners> banners;

    public class Banners{
        public String banner_pic;
        public String aid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String id;

        public String getBanner_pic() {
            return banner_pic;
        }

        public void setBanner_pic(String banner_pic) {
            this.banner_pic = banner_pic;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getBanner_url() {
            return banner_url;
        }

        public void setBanner_url(String banner_url) {
            this.banner_url = banner_url;
        }

        public String getJump_mode() {
            return jump_mode;
        }

        public void setJump_mode(String jump_mode) {
            this.jump_mode = jump_mode;
        }

        public String banner_url;
        public String jump_mode;
    }
}

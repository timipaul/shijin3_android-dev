package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/11/19.
 */

public class ShenmiBean {
    /**
     * code : 0
     * msg : SUCCESS
     * count : 1
     * ads : [{"adtype":"0","restype":null,"title":"","desc":"","src":" ","icon":"","link":"","clickreport":null,"displayreport":null,"page":"","ish":1,"tc":null,"width":"-1","height":"-1","extra":null,"action":0,"em":0,"apn":"","aps":0,"showtime":0,"admode":null,"vast":null,"trackingevents":null,"logourl":null,"gp":0}]
     * adcode : ad001
     * pvid : 54e1b1bf-6877-4e97-85c9-5f04cf189a0d
     */

    private int code;
    private String msg;
    private int count;
    private String adcode;
    private String pvid;
    private List<AdsBean> ads;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getPvid() {
        return pvid;
    }

    public void setPvid(String pvid) {
        this.pvid = pvid;
    }

    public List<AdsBean> getAds() {
        return ads;
    }

    public void setAds(List<AdsBean> ads) {
        this.ads = ads;
    }

    public static class AdsBean {
        /**
         * adtype : 0
         * restype : null
         * title :
         * desc :
         * src :
         * icon :
         * link :
         * clickreport : null
         * displayreport : null
         * page :
         * ish : 1
         * tc : null
         * width : -1
         * height : -1
         * extra : null
         * action : 0
         * em : 0
         * apn :
         * aps : 0
         * showtime : 0
         * admode : null
         * vast : null
         * trackingevents : null
         * logourl : null
         * gp : 0
         */

        private String adtype;
        private Object restype;
        private String title;
        private String desc;
        private String src;
        private String icon;
        private String link;
        private Object clickreport;
        private Object displayreport;
        private String page;
        private int ish;
        private Object tc;
        private String width;
        private String height;
        private Object extra;
        private int action;
        private int em;
        private String apn;
        private int aps;
        private int showtime;
        private Object admode;
        private Object vast;
        private List<Trackingevents> trackingevents;
        private Object logourl;
        private int gp;

        public String getDplink() {
            return dplink;
        }

        public void setDplink(String dplink) {
            this.dplink = dplink;
        }

        private String dplink;
        public class Trackingevents{
            public String eventtype;

            public String getEventtype() {
                return eventtype;
            }

            public void setEventtype(String eventtype) {
                this.eventtype = eventtype;
            }

            public List<String> getTracking() {
                return tracking;
            }

            public void setTracking(List<String> tracking) {
                this.tracking = tracking;
            }

            public List<String> tracking;
        }
        public String getAdtype() {
            return adtype;
        }

        public void setAdtype(String adtype) {
            this.adtype = adtype;
        }

        public Object getRestype() {
            return restype;
        }

        public void setRestype(Object restype) {
            this.restype = restype;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Object getClickreport() {
            return clickreport;
        }

        public void setClickreport(Object clickreport) {
            this.clickreport = clickreport;
        }

        public Object getDisplayreport() {
            return displayreport;
        }

        public void setDisplayreport(Object displayreport) {
            this.displayreport = displayreport;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public int getIsh() {
            return ish;
        }

        public void setIsh(int ish) {
            this.ish = ish;
        }

        public Object getTc() {
            return tc;
        }

        public void setTc(Object tc) {
            this.tc = tc;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public Object getExtra() {
            return extra;
        }

        public void setExtra(Object extra) {
            this.extra = extra;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public int getEm() {
            return em;
        }

        public void setEm(int em) {
            this.em = em;
        }

        public String getApn() {
            return apn;
        }

        public void setApn(String apn) {
            this.apn = apn;
        }

        public int getAps() {
            return aps;
        }

        public void setAps(int aps) {
            this.aps = aps;
        }

        public int getShowtime() {
            return showtime;
        }

        public void setShowtime(int showtime) {
            this.showtime = showtime;
        }

        public Object getAdmode() {
            return admode;
        }

        public void setAdmode(Object admode) {
            this.admode = admode;
        }

        public Object getVast() {
            return vast;
        }

        public void setVast(Object vast) {
            this.vast = vast;
        }

        public List<Trackingevents> getTrackingevents() {
            return trackingevents;
        }

        public void setTrackingevents(List<Trackingevents> trackingevents) {
            this.trackingevents = trackingevents;
        }

        public Object getLogourl() {
            return logourl;
        }

        public void setLogourl(Object logourl) {
            this.logourl = logourl;
        }

        public int getGp() {
            return gp;
        }

        public void setGp(int gp) {
            this.gp = gp;
        }
    }
}

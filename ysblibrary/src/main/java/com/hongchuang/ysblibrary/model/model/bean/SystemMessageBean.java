package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/8/8.
 */

public class SystemMessageBean {
    public String cursor;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<Circulars> getCirculars() {
        return circulars;
    }

    public void setCirculars(List<Circulars> circulars) {
        this.circulars = circulars;
    }

    public List<Circulars> circulars;
    public class Circulars{
        public String id;
        public String circular_title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCircular_title() {
            return circular_title;
        }

        public void setCircular_title(String circular_title) {
            this.circular_title = circular_title;
        }

        public String getCircular_imgurl() {
            return circular_imgurl;
        }

        public void setCircular_imgurl(String circular_imgurl) {
            this.circular_imgurl = circular_imgurl;
        }

        public String getCircular_brief() {
            return circular_brief;
        }

        public void setCircular_brief(String circular_brief) {
            this.circular_brief = circular_brief;
        }

        public String getCircular_content() {
            return circular_content;
        }

        public void setCircular_content(String circular_content) {
            this.circular_content = circular_content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getModified_at() {
            return modified_at;
        }

        public void setModified_at(String modified_at) {
            this.modified_at = modified_at;
        }

        public String circular_imgurl;
        public String circular_brief;
        public String circular_content;
        public String created_at;
        public String modified_at;
    }
}

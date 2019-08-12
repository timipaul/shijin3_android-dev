package com.hongchuang.ysblibrary.dao;

import java.util.List;

/**
 * Created by yrdan on 2018/8/7.
 */

public class BlackListBean {
    public String cursor;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public List<Records> records;

    public class Records{
        public String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Black getBlack() {
            return black;
        }

        public void setBlack(Black black) {
            this.black = black;
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

        public User user;

        public class User{
            public String id;
            public String username;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String nickname;
        }

        public Black black;

        public class Black{
            public String id;
            public String username;
            public String nickname;
            public String imgurl;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getIs_advertiser() {
                return is_advertiser;
            }

            public void setIs_advertiser(String is_advertiser) {
                this.is_advertiser = is_advertiser;
            }

            public String is_advertiser;

        }

        public String created_at;
        public String modified_at;
    }
}

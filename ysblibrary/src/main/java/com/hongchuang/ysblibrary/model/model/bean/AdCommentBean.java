package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/8/9.
 */

public class AdCommentBean {
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public List<MessagesBean> messages;
    public class MessagesBean{
        public String id;
        public boolean isShow=false;

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean isCheck=false;

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

        public Ad getAd() {
            return ad;
        }

        public void setAd(Ad ad) {
            this.ad = ad;
        }

        public Father getFather_comment() {
            return father_comment;
        }

        public void setFather_comment(Father father_comment) {
            this.father_comment = father_comment;
        }

        public Child getChild_comment() {
            return child_comment;
        }

        public void setChild_comment(Child child_comment) {
            this.child_comment = child_comment;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String imgurl;
            public String nickname;

            public String getIs_advertiser() {
                return is_advertiser;
            }

            public void setIs_advertiser(String is_advertiser) {
                this.is_advertiser = is_advertiser;
            }

            public String is_advertiser;
        }
        public Ad ad;
        public class Ad{
            public String id;
            public String user_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAd_title() {
                return ad_title;
            }

            public void setAd_title(String ad_title) {
                this.ad_title = ad_title;
            }

            public String nickname;
            public String ad_title;
        }
        public Father father_comment;
        public class Father{
            public String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String content;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String nickname;
        }
        public Child child_comment;
        public class Child{
            public String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String content;
        }
        public String created_at;
        public String modified_at;

    }
}

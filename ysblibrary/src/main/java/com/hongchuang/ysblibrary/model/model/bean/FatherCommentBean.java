package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/8/24.
 */

public class FatherCommentBean {
    public String id;
    public String ad_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_number() {
        return comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public String user_id;
    public User user;
    public class User{
        public String imgurl;

        public String getImageurl() {
            return imgurl;
        }

        public void setImageurl(String imageurl) {
            this.imgurl = imageurl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String nickname;

        public String getIs_advertiser() {
            return is_advertiser;
        }

        public void setIs_advertiser(String is_advertiser) {
            this.is_advertiser = is_advertiser;
        }

        public String is_advertiser;
    }
    public String created_at;
    public String is_like;
    public String like_number;
    public String content;
    public String comment_number;
    public List<Comments> comments;
    public class Comments{
        public String id;
        public String father_comment_id;
        public String user_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFather_comment_id() {
            return father_comment_id;
        }

        public void setFather_comment_id(String father_comment_id) {
            this.father_comment_id = father_comment_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getLike_number() {
            return like_number;
        }

        public void setLike_number(String like_number) {
            this.like_number = like_number;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getComment_number() {
            return comment_number;
        }

        public void setComment_number(String comment_number) {
            this.comment_number = comment_number;
        }

        public String getFather_id() {
            return father_id;
        }

        public void setFather_id(String father_id) {
            this.father_id = father_id;
        }

        public String getFather_nickname() {
            return father_nickname;
        }

        public void setFather_nickname(String father_nickname) {
            this.father_nickname = father_nickname;
        }

        public User user;
        public class User{
            public String imgurl;

            public String getImageurl() {
                return imgurl;
            }

            public void setImageurl(String imageurl) {
                this.imgurl = imageurl;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String nickname;

            public String getIs_advertiser() {
                return is_advertiser;
            }

            public void setIs_advertiser(String is_advertiser) {
                this.is_advertiser = is_advertiser;
            }

            public String is_advertiser;
        }
        public String created_at;
        public String is_like;
        public String like_number;
        public String content;
        public String comment_number;
        public String father_id;
        public String father_nickname;
    }

}

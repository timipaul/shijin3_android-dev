package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/** 用户排行表 */
public class UserRankingBean {

    private Ranking user;
    private List<Ranking> list;

    public class Ranking{
        private int id;
        //用户ID
        private String userId;
        private String username;
        private String nickname;
        private String imgUrl;
        //金币
        private int goldCoin;
        //排行
        private int index;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(int goldCoin) {
            this.goldCoin = goldCoin;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Override
        public String toString() {
            return "Ranking{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", goldCoin=" + goldCoin +
                    ", index=" + index +
                    '}';
        }
    }

    public Ranking getUser() {
        return user;
    }

    public void setUser(Ranking user) {
        this.user = user;
    }

    public List<Ranking> getList() {
        return list;
    }

    public void setList(List<Ranking> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "UserRankingBean{" +
                "user=" + user +
                ", list=" + list +
                '}';
    }
}

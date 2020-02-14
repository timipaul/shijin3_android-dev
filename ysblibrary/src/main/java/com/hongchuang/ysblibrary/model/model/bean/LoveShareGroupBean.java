package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/** 爱分享 团队实体类 */
public class LoveShareGroupBean {

    private int peopleNum;
    private String achievement;
    private Subordinate mySubordinate;
    private int sv;

    public class Subordinate{
        private String _id;
        private String achievement;
        private int peoleNum;
        private List<Child> list;

        public class Child{
            private String _id;
            private String nickname;
            private String avatarUrl;
            private int peopleNum;
            //个人业绩
            private int sv;
            //团队业绩
            private String groupAchievement;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public int getSv() {
                return sv;
            }

            public void setSv(int sv) {
                this.sv = sv;
            }

            public int getPeopleNum() {
                return peopleNum;
            }

            public void setPeopleNum(int peopleNum) {
                this.peopleNum = peopleNum;
            }

            public String getGroupAchievement() {
                return groupAchievement;
            }

            public void setGroupAchievement(String groupAchievement) {
                this.groupAchievement = groupAchievement;
            }
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        public int getPeoleNum() {
            return peoleNum;
        }

        public void setPeoleNum(int peoleNum) {
            this.peoleNum = peoleNum;
        }

        public List<Child> getList() {
            return list;
        }

        public void setList(List<Child> list) {
            this.list = list;
        }
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public Subordinate getMySubordinate() {
        return mySubordinate;
    }

    public void setMySubordinate(Subordinate mySubordinate) {
        this.mySubordinate = mySubordinate;
    }

    public int getSv() {
        return sv;
    }

    public void setSv(int sv) {
        this.sv = sv;
    }
}

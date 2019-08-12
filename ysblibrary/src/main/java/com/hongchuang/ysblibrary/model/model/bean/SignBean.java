package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/9/6.
 */

public class SignBean {
    public SmallLottery getSmall_lottery() {
        return small_lottery;
    }

    public void setSmall_lottery(SmallLottery small_lottery) {
        this.small_lottery = small_lottery;
    }

    public String getAble_patch_number() {
        return able_patch_number;
    }

    public void setAble_patch_number(String able_patch_number) {
        this.able_patch_number = able_patch_number;
    }

    public SignIn getSign_in() {
        return sign_in;
    }

    public void setSign_in(SignIn sign_in) {
        this.sign_in = sign_in;
    }

    public SmallLottery small_lottery;
    public class SmallLottery{
        public String id;
        public String lottery_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLottery_name() {
            return lottery_name;
        }

        public void setLottery_name(String lottery_name) {
            this.lottery_name = lottery_name;
        }

        public String getLottery_imgurl() {
            return lottery_imgurl;
        }

        public void setLottery_imgurl(String lottery_imgurl) {
            this.lottery_imgurl = lottery_imgurl;
        }

        public String lottery_imgurl;
    }
    public String able_patch_number;
    public SignIn sign_in;
    public class SignIn{
        public String id;

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

        public List<Marks> getMarks() {
            return marks;
        }

        public void setMarks(List<Marks> marks) {
            this.marks = marks;
        }

        public String getContinuous_number() {
            return continuous_number;
        }

        public void setContinuous_number(String continuous_number) {
            this.continuous_number = continuous_number;
        }

        public String getLast_sign_in_time() {
            return last_sign_in_time;
        }

        public void setLast_sign_in_time(String last_sign_in_time) {
            this.last_sign_in_time = last_sign_in_time;
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

        public String user_id;
        public List<Marks> marks;
        public class Marks{
            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getContinuous_number() {
                return continuous_number;
            }

            public void setContinuous_number(String continuous_number) {
                this.continuous_number = continuous_number;
            }

            public String status;
            public String continuous_number;
        }
        public String continuous_number;
        public String last_sign_in_time;
        public String created_at;
        public String modified_at;
    }
}

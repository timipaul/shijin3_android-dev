package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/8/13.
 */

public class AdReleaseBean {
    public String sursor;
    public String total_change;

    public String getTotal_change() {
        return total_change;
    }

    public void setTotal_change(String total_change) {
        this.total_change = total_change;
    }

    public String getTotal_history_change() {
        return total_history_change;
    }

    public void setTotal_history_change(String total_history_change) {
        this.total_history_change = total_history_change;
    }

    public String getTotal_history_points() {
        return total_history_points;
    }

    public void setTotal_history_points(String total_history_points) {
        this.total_history_points = total_history_points;
    }

    public String getTotal_points() {
        return total_points;
    }

    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }

    public String total_history_change;
    public String total_history_points;
    public String total_points;
    public String total_size;

    public String getSursor() {
        return sursor;
    }

    public void setSursor(String sursor) {
        this.sursor = sursor;
    }

    public String getTotal_size() {
        return total_size;
    }

    public void setTotal_size(String total_size) {
        this.total_size = total_size;
    }

    public String getTotal_history_reward() {
        return total_history_reward;
    }

    public void setTotal_history_reward(String total_history_reward) {
        this.total_history_reward = total_history_reward;
    }

    public String getTotal_reward() {
        return total_reward;
    }

    public void setTotal_reward(String total_reward) {
        this.total_reward = total_reward;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public String total_history_reward;
    public String total_reward;
    public List<Records> records;
    public class Records{
        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public boolean isShow=false;
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

        public Ad getAd() {
            return ad;
        }

        public void setAd(Ad ad) {
            this.ad = ad;
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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNikename() {
                return nikename;
            }

            public void setNikename(String nikename) {
                this.nikename = nikename;
            }

            public String username;
            public String nikename;
        }
        public Ad ad;
        public class Ad{
            public String id;
            public String ad_title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAd_title() {
                return ad_title;
            }

            public void setAd_title(String ad_title) {
                this.ad_title = ad_title;
            }

            public String getReward_mode() {
                return reward_mode;
            }

            public void setReward_mode(String reward_mode) {
                this.reward_mode = reward_mode;
            }

            public List<String> getAd_title_pics() {
                return ad_title_pics;
            }

            public void setAd_title_pics(List<String> ad_title_pics) {
                this.ad_title_pics = ad_title_pics;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getChange() {
                return change;
            }

            public void setChange(String change) {
                this.change = change;
            }

            public String getHistory_change() {
                return history_change;
            }

            public void setHistory_change(String history_change) {
                this.history_change = history_change;
            }

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public String getHistory_points() {
                return history_points;
            }

            public void setHistory_points(String history_points) {
                this.history_points = history_points;
            }

            public String getAd_exposure_number() {
                return ad_exposure_number;
            }

            public void setAd_exposure_number(String ad_exposure_number) {
                this.ad_exposure_number = ad_exposure_number;
            }

            public String getAd_click_number() {
                return ad_click_number;
            }

            public void setAd_click_number(String ad_click_number) {
                this.ad_click_number = ad_click_number;
            }

            public String getAnswer_number() {
                return answer_number;
            }

            public void setAnswer_number(String answer_number) {
                this.answer_number = answer_number;
            }

            public String getAnswer_right_number() {
                return answer_right_number;
            }

            public void setAnswer_right_number(String answer_right_number) {
                this.answer_right_number = answer_right_number;
            }

            public String getAd_like_number() {
                return ad_like_number;
            }

            public void setAd_like_number(String ad_like_number) {
                this.ad_like_number = ad_like_number;
            }

            public String getAd_share_number() {
                return ad_share_number;
            }

            public void setAd_share_number(String ad_share_number) {
                this.ad_share_number = ad_share_number;
            }

            public String getAd_comment_number() {
                return ad_comment_number;
            }

            public void setAd_comment_number(String ad_comment_number) {
                this.ad_comment_number = ad_comment_number;
            }

            public String getAd_collection_number() {
                return ad_collection_number;
            }

            public void setAd_collection_number(String ad_collection_number) {
                this.ad_collection_number = ad_collection_number;
            }

            public String reward_mode;
            public List<String> ad_title_pics;
            public String status;
            public String created_at;
            public String change;
            public String history_change;
            public String points;
            public String history_points;
            public String ad_exposure_number;
            public String ad_click_number;
            public String answer_number;
            public String answer_right_number;
            public String ad_like_number;
            public String ad_share_number;
            public String ad_comment_number;
            public String ad_collection_number;
        }
        public String created_at;
        public String modified_at;
    }
}

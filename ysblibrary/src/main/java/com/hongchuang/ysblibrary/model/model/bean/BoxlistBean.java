package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/9/6.
 */

public class BoxlistBean {
    public String last_box_id;
    public String today_share_number;

    public DailyShareNumber getDaily_task_setting() {
        return daily_task_setting;
    }

    public void setDaily_task_setting(DailyShareNumber daily_task_setting) {
        this.daily_task_setting = daily_task_setting;
    }

    public DailyShareNumber daily_task_setting;
    public class DailyShareNumber{
        public String sign_in_lottery_day_number;
        public String invitation_reward_number;
        public String invitation_reward_mode;
        public String invitation_reward;

        public String getSign_in_lottery_day_number() {
            return sign_in_lottery_day_number;
        }

        public void setSign_in_lottery_day_number(String sign_in_lottery_day_number) {
            this.sign_in_lottery_day_number = sign_in_lottery_day_number;
        }

        public String getInvitation_reward_number() {
            return invitation_reward_number;
        }

        public void setInvitation_reward_number(String invitation_reward_number) {
            this.invitation_reward_number = invitation_reward_number;
        }

        public String getInvitation_reward_mode() {
            return invitation_reward_mode;
        }

        public void setInvitation_reward_mode(String invitation_reward_mode) {
            this.invitation_reward_mode = invitation_reward_mode;
        }

        public String getInvitation_reward() {
            return invitation_reward;
        }

        public void setInvitation_reward(String invitation_reward) {
            this.invitation_reward = invitation_reward;
        }

        public String getIncome_bask_reward_mode() {
            return income_bask_reward_mode;
        }

        public void setIncome_bask_reward_mode(String income_bask_reward_mode) {
            this.income_bask_reward_mode = income_bask_reward_mode;
        }

        public String getIncome_bask_reward_number() {
            return income_bask_reward_number;
        }

        public void setIncome_bask_reward_number(String income_bask_reward_number) {
            this.income_bask_reward_number = income_bask_reward_number;
        }

        public String getIncome_bask_reward() {
            return income_bask_reward;
        }

        public void setIncome_bask_reward(String income_bask_reward) {
            this.income_bask_reward = income_bask_reward;
        }

        public String getInfo_complete_reward_mode() {
            return info_complete_reward_mode;
        }

        public void setInfo_complete_reward_mode(String info_complete_reward_mode) {
            this.info_complete_reward_mode = info_complete_reward_mode;
        }

        public String getInfo_complete_reward_number() {
            return info_complete_reward_number;
        }

        public void setInfo_complete_reward_number(String info_complete_reward_number) {
            this.info_complete_reward_number = info_complete_reward_number;
        }

        public String getInfo_complete_reward() {
            return info_complete_reward;
        }

        public void setInfo_complete_reward(String info_complete_reward) {
            this.info_complete_reward = info_complete_reward;
        }

        public String getGood_feedback_reward_mode() {
            return good_feedback_reward_mode;
        }

        public void setGood_feedback_reward_mode(String good_feedback_reward_mode) {
            this.good_feedback_reward_mode = good_feedback_reward_mode;
        }

        public String getGood_feedback_reward_number() {
            return good_feedback_reward_number;
        }

        public void setGood_feedback_reward_number(String good_feedback_reward_number) {
            this.good_feedback_reward_number = good_feedback_reward_number;
        }

        public String getGood_feedback_reward() {
            return good_feedback_reward;
        }

        public void setGood_feedback_reward(String good_feedback_reward) {
            this.good_feedback_reward = good_feedback_reward;
        }

        public String getPage_share_reward_mode() {
            return page_share_reward_mode;
        }

        public void setPage_share_reward_mode(String page_share_reward_mode) {
            this.page_share_reward_mode = page_share_reward_mode;
        }

        public String getPage_share_reward_number() {
            return page_share_reward_number;
        }

        public void setPage_share_reward_number(String page_share_reward_number) {
            this.page_share_reward_number = page_share_reward_number;
        }

        public String getPage_share_reward() {
            return page_share_reward;
        }

        public void setPage_share_reward(String page_share_reward) {
            this.page_share_reward = page_share_reward;
        }

        public String getAllow_page_share_reward_number() {
            return allow_page_share_reward_number;
        }

        public void setAllow_page_share_reward_number(String allow_page_share_reward_number) {
            this.allow_page_share_reward_number = allow_page_share_reward_number;
        }

        public String income_bask_reward_mode;
        public String income_bask_reward_number;
        public String income_bask_reward;
        public String info_complete_reward_mode;
        public String info_complete_reward_number;
        public String info_complete_reward;
        public String good_feedback_reward_mode;
        public String good_feedback_reward_number;
        public String good_feedback_reward;
        public String page_share_reward_mode;
        public String page_share_reward_number;
        public String page_share_reward;
        public String allow_page_share_reward_number;
        public String good_comment_reward_mode;

        public String getGood_comment_reward_mode() {
            return good_comment_reward_mode;
        }

        public void setGood_comment_reward_mode(String good_comment_reward_mode) {
            this.good_comment_reward_mode = good_comment_reward_mode;
        }

        public String getGood_comment_reward_number() {
            return good_comment_reward_number;
        }

        public void setGood_comment_reward_number(String good_comment_reward_number) {
            this.good_comment_reward_number = good_comment_reward_number;
        }

        public String getGood_comment_reward() {
            return good_comment_reward;
        }

        public void setGood_comment_reward(String good_comment_reward) {
            this.good_comment_reward = good_comment_reward;
        }

        public String good_comment_reward_number;
        public String good_comment_reward;
    }
    public String getNow_hms() {
        return now;
    }

    public void setNow_hms(String now_hms) {
        this.now = now_hms;
    }

    public String now;

    public String getLast_box_id() {
        return last_box_id;
    }

    public void setLast_box_id(String last_box_id) {
        this.last_box_id = last_box_id;
    }

    public String getToday_share_number() {
        return today_share_number;
    }

    public void setToday_share_number(String today_share_number) {
        this.today_share_number = today_share_number;
    }

    public List<BoxList> getBox_list() {
        return box_list;
    }

    public void setBox_list(List<BoxList> box_list) {
        this.box_list = box_list;
    }

    public List<BoxList> box_list;
    public class BoxList{
        public String id;
        public String open_time;
        public String time_length;

        public String getDraw_lottery_list_id() {
            return draw_lottery_list_id;
        }

        public void setDraw_lottery_list_id(String draw_lottery_list_id) {
            this.draw_lottery_list_id = draw_lottery_list_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String draw_lottery_list_id;
        public String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOpen_time() {
            return open_time;
        }

        public void setOpen_time(String open_time) {
            this.open_time = open_time;
        }

        public String getTime_length() {
            return time_length;
        }

        public void setTime_length(String time_length) {
            this.time_length = time_length;
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

        public String created_at;
        public String modified_at;

        public String getTrim_value() {
            return trim_value;
        }

        public void setTrim_value(String trim_value) {
            this.trim_value = trim_value;
        }

        public String trim_value;
    }
}

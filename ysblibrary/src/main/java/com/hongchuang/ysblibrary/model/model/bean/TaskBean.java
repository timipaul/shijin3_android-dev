package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Copyright (C)
 * FileName: TaskBean
 * Author: paul
 * Date: 2019/7/23 19:45
 * Description: 任务实体类
 */
public class TaskBean {

    //挑战任务
    private Tasks challengeTasks;
    //日常任务
    private Tasks dailyTasks;
    //新手任务
    private Tasks newbieTask;

    //新手任务
    public enum NewbieType{
        SUPPLEMENTARY_INFORMATION,//补齐资料
        FILL_IN_THE_INVITATION_CODE,//填写邀请码
        TO_SEND_PIRCTURES_AND_TEXTS,//发图文
        TO_SEND_VIDEO,//视频
        INVITE_FRIENDS,//邀请好友
        INTERACTIVE_QUESTION_ANSWER//互动问答
    }
    //日常任务
    public enum DailyYype{
        SIGN_IN,//签到
        FORWARD,//转发
        GIVE_THE_THUMBS_UP,//点赞
        COMMENT,//评论
        FOLLOW,//关注
        INTERACTIVE_QUESTION_ANSWER,//问答
        RELEASE//发布
    }
    //挑战任务
    public enum ChallengeType{
        GIVE_THE_THUMBS_UP_NUM,//点赞
        FORWARD_NUM,//转发
        COMMENT_NUM,//评论
        FOLLOW_NUM,//关注
        COLLECT_NUM,//收藏
        INVITE_FRIENDS_NUM,//邀请好友
        RELEASE_NUM,//发布内容
        ANSWERS_NUM//答题数
    }


    //任务
    public class Tasks{
        private String title;
        private List<Data> data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Tasks{" +
                    "title='" + title + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public class Data{
        private String chTitle;
        private int num;
        private int next;
        //任务状态 0未完成 1未领取 2已领取
        private String flag;
        //当前阶段的金币奖励
        private int reward;
        private String enTitle;

        public String getChTitle() {
            return chTitle;
        }

        public void setChTitle(String chTitle) {
            this.chTitle = chTitle;
        }

        public String getEnTitle() {
            return enTitle;
        }

        public void setEnTitle(String enTitle) {
            this.enTitle = enTitle;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getNext() {
            return next;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        @Override
        public String toString() {
            return "Data{" +
                    ", num=" + num +
                    ", next=" + next +
                    ", flag='" + flag + '\'' +
                    ", reward='" + reward + '\'' +
                    '}';
        }
    }

    public Tasks getChallengeTasks() {
        return challengeTasks;
    }

    public void setChallengeTasks(Tasks challengeTasks) {
        this.challengeTasks = challengeTasks;
    }

    public Tasks getDailyTasks() {
        return dailyTasks;
    }

    public void setDailyTasks(Tasks dailyTasks) {
        this.dailyTasks = dailyTasks;
    }

    public Tasks getNewbieTask() {
        return newbieTask;
    }

    public void setNewbieTask(Tasks newbieTask) {
        this.newbieTask = newbieTask;
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "challengeTasks=" + challengeTasks +
                ", dailyTasks=" + dailyTasks +
                ", newbieTask=" + newbieTask +
                '}';
    }
}

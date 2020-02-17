package com.hongchuang.ysblibrary.model.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yrdan on 2018/12/21.
 */

public class Ads implements Serializable {
    public String id;
    public String ad_mode;
    public String created_at;
    public String history_change;
    public String change;

    public Object getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(Object nativeAd) {
        this.nativeAd = nativeAd;
    }

    public Object nativeAd;

    public Object getAdhub() {
        return adhub;
    }

    public void setAdhub(Object adhub) {
        this.adhub = adhub;
    }

    public Object adhub;

    public String getCheck_info() {
        return check_detail;
    }

    public void setCheck_info(String check_info) {
        this.check_detail = check_info;
    }

    public String check_detail;
    public String points;
    public String history_points;
    public String modified_at;
    public String user_id;
    public String ad_title;
    public String ad_content;
    public String question;
    public Options options;
    public Options answer;
    public String range_mode;
    public String range;
    public String ad_type;
    public String total_number;
    public String people_number;

    public String getAnswer_right_number() {
        return answer_right_number;
    }

    public void setAnswer_right_number(String answer_right_number) {
        this.answer_right_number = answer_right_number;
    }

    public String answer_right_number;
    public Filter filter;
    public Ads(Object nativeAd,String id, String ad_mode, String created_at, String history_change, String change, Object adhub, String check_info, String points, String history_points, String modified_at, String user_id, String ad_title, String ad_content, String question, Options options, Options answer, String url, String range_mode, String range, String ad_type, String total_number, String peopel_number, String answer_right_number, Filter filter, List<String> ad_title_pics, List<String> interests, String reward_mode, String ad_like_number, String ad_comment_number, String ad_collection_number, String ad_share_number, String reward, List<String> tags, String answer_number, String status, String adshow, Record release_record, User user) {
        this.nativeAd=nativeAd;
        this.adhub=adhub;
        this.id = id;
        this.ad_mode = ad_mode;
        this.created_at = created_at;
        this.history_change = history_change;
        this.check_detail=check_info;
        this.change = change;
        this.points = points;
        this.history_points = history_points;
        this.modified_at = modified_at;
        this.user_id = user_id;
        this.ad_title = ad_title;
        this.ad_content = ad_content;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.range_mode = range_mode;
        this.range = range;
        this.ad_type = ad_type;
        this.total_number = total_number;
        this.people_number = peopel_number;
        this.filter = filter;
        this.ad_title_pics = ad_title_pics;
        this.interests = interests;
        this.reward_mode = reward_mode;
        this.ad_like_number = ad_like_number;
        this.ad_comment_number = ad_comment_number;
        this.ad_collection_number = ad_collection_number;
        this.ad_share_number = ad_share_number;
        this.reward = reward;
        this.tags = tags;
        this.answer_number = answer_number;
        this.status = status;
        this.ad_show = adshow;
        this.answer_right_number=answer_right_number;
        this.release_record = release_record;
        this.user=user;
        this.url=url;
    }

    public String getHistory_change() {
        return history_change;
    }

    public void setHistory_change(String history_change) {
        this.history_change = history_change;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
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



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd_mode() {
        return ad_mode;
    }

    public void setAd_mode(String ad_mode) {
        this.ad_mode = ad_mode;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public List<String> getAd_title_pics() {
        return ad_title_pics;
    }

    public void setAd_title_pics(List<String> ad_title_pics) {
        this.ad_title_pics = ad_title_pics;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getReward_mode() {
        return reward_mode;
    }

    public void setReward_mode(String reward_mode) {
        this.reward_mode = reward_mode;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getAnswer_number() {
        return answer_number;
    }

    public void setAnswer_number(String answer_number) {
        this.answer_number = answer_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdshow() {
        return ad_show;
    }

    public void setAdshow(String adshow) {
        this.ad_show = adshow;
    }

    public Record getRelease_record() {
        return release_record;
    }

    public void setRelease_record(Record release_record) {
        this.release_record = release_record;
    }


    public String getAd_content() {
        return ad_content;
    }

    public void setAd_content(String ad_content) {
        this.ad_content = ad_content;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Options getAnswer() {
        return answer;
    }

    public void setAnswer(Options answer) {
        this.answer = answer;
    }

    public String getRange_mode() {
        return range_mode;
    }

    public void setRange_mode(String range_mode) {
        this.range_mode = range_mode;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }

    public String getPeopel_number() {
        return people_number;
    }

    public void setPeopel_number(String peopel_number) {
        this.people_number = peopel_number;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public class Filter implements Serializable{
        public String age;
        public String job;
        public String gender;
        public String income;

        public Filter(String age, String job, String gender, String income, List<String> interests) {
            this.age = age;
            this.job = job;
            this.gender = gender;
            this.income = income;
            this.interests = interests;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public List<String> getInterests() {
            return interests;
        }

        public void setInterests(List<String> interests) {
            this.interests = interests;
        }

        public List<String> interests;
    }
    public class Options implements Serializable{
        public String options1;
        public String options2;

        public Options(String options1, String options2, String options3, String options4) {
            this.options1 = options1;
            this.options2 = options2;
            this.options3 = options3;
            this.options4 = options4;
        }

        public String getOptions1() {
            return options1;
        }

        public void setOptions1(String options1) {
            this.options1 = options1;
        }

        public String getOptions2() {
            return options2;
        }

        public void setOptions2(String options2) {
            this.options2 = options2;
        }

        public String getOptions3() {
            return options3;
        }

        public void setOptions3(String options3) {
            this.options3 = options3;
        }

        public String getOptions4() {
            return options4;
        }

        public void setOptions4(String options4) {
            this.options4 = options4;
        }

        public String options3;
        public String options4;
    }
    public List<String> ad_title_pics;
    public List<String> interests;
    public String reward_mode;
    public String ad_like_number;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String url;

    public String getAd_like_number() {
        return ad_like_number;
    }

    public void setAd_like_number(String ad_like_number) {
        this.ad_like_number = ad_like_number;
    }

    public String getAd_comment_number() {
        return ad_comment_number;
    }

    public void setAd_comment_number(String ad_comment_number) {
        this.ad_comment_number = ad_comment_number;
    }

    public String ad_comment_number;
    public String ad_collection_number;

    public String getAd_collection_number() {
        return ad_collection_number;
    }

    public void setAd_collection_number(String ad_collection_number) {
        this.ad_collection_number = ad_collection_number;
    }

    public String getAd_share_number() {
        return ad_share_number;
    }

    public void setAd_share_number(String ad_share_number) {
        this.ad_share_number = ad_share_number;
    }

    public String ad_share_number;
    public String reward;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> tags;
    public String answer_number;
    public String status;
    public String ad_show;
    public Record release_record;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User user;
    public class User implements Serializable{
        public String id;
        public String username;
        public String nickname;
        public String imgurl;

        public User(String id, String username, String nickname, String imgurl, String is_advertiser) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
            this.imgurl = imgurl;
            this.is_advertiser = is_advertiser;
        }

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
    public class Record implements Serializable{
        String nickname;

        public Record(String nickname, String imgurl, String release_time) {
            this.nickname = nickname;
            this.imgurl = imgurl;
            this.release_time = release_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getImagurl() {
            return imgurl;
        }

        public void setImagurl(String imagurl) {
            this.imgurl = imagurl;
        }

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        String imgurl;
        String release_time;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "id='" + id + '\'' +
                ", ad_mode='" + ad_mode + '\'' +
                ", created_at='" + created_at + '\'' +
                ", history_change='" + history_change + '\'' +
                ", change='" + change + '\'' +
                ", nativeAd=" + nativeAd +
                ", adhub=" + adhub +
                ", check_detail='" + check_detail + '\'' +
                ", points='" + points + '\'' +
                ", history_points='" + history_points + '\'' +
                ", modified_at='" + modified_at + '\'' +
                ", user_id='" + user_id + '\'' +
                ", ad_title='" + ad_title + '\'' +
                ", ad_content='" + ad_content + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", answer=" + answer +
                ", range_mode='" + range_mode + '\'' +
                ", range='" + range + '\'' +
                ", ad_type='" + ad_type + '\'' +
                ", total_number='" + total_number + '\'' +
                ", people_number='" + people_number + '\'' +
                ", answer_right_number='" + answer_right_number + '\'' +
                ", filter=" + filter +
                ", ad_title_pics=" + ad_title_pics +
                ", interests=" + interests +
                ", reward_mode='" + reward_mode + '\'' +
                ", ad_like_number='" + ad_like_number + '\'' +
                ", url='" + url + '\'' +
                ", ad_comment_number='" + ad_comment_number + '\'' +
                ", ad_collection_number='" + ad_collection_number + '\'' +
                ", ad_share_number='" + ad_share_number + '\'' +
                ", reward='" + reward + '\'' +
                ", tags=" + tags +
                ", answer_number='" + answer_number + '\'' +
                ", status='" + status + '\'' +
                ", ad_show='" + ad_show + '\'' +
                ", release_record=" + release_record +
                ", user=" + user +
                '}';
    }
}

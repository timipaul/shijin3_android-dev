package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 */

public class UserBean {
    public UserDetailBean user;

    public UserDetailBean getUser() {
        return user;
    }

    public void setUser(UserDetailBean user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token;


    public AccessesBean getAccesses() {
        return accesses;
    }

    public void setAccesses(AccessesBean accesses) {
        this.accesses = accesses;
    }

    public AccessesBean accesses;

    public class AccessesBean{
        public List<String> one_level;
        public List<String> two_level;

        public List<String> getOne_level() {
            return one_level;
        }

        public void setOne_level(List<String> one_level) {
            this.one_level = one_level;
        }

        public List<String> getTwo_level() {
            return two_level;
        }

        public void setTwo_level(List<String> two_level) {
            this.two_level = two_level;
        }

        public List<String> getThree_level() {
            return three_level;
        }

        public void setThree_level(List<String> three_level) {
            this.three_level = three_level;
        }

        public List<String> three_level;

    }
    public class UserDetailBean{
        public String id;
        public String username;

        public String wx_nickname;
        public String qqid;
        public String invitation_code;
        public String new_one_status;
        public String one_withdraw_status;
        public String five_withdraw_status;

        public String member_status;//判断是否会员
        public String expiration_at;
        public String try_member_status; //判断是否领取过免费会员
        public String newbieTaskCheck;//判断是否完成新手任务

        public String app_openid;
        public String openid;
        public String app_qqid;

        private String first_open_member;//是否首冲
        private String invitation;  //判断是否填写过邀请码

        public String getInvitation() {
            return invitation;
        }

        public void setInvitation(String invitation) {
            this.invitation = invitation;
        }

        public String getNewbieTaskCheck() {
            return newbieTaskCheck;
        }

        public void setNewbieTaskCheck(String newbieTaskCheck) {
            this.newbieTaskCheck = newbieTaskCheck;
        }

        public String getFirst_open_member() {
            return first_open_member;
        }

        public void setFirst_open_member(String first_open_member) {
            this.first_open_member = first_open_member;
        }

        public String getTry_member_status() {
            return try_member_status;
        }

        public void setTry_member_status(String try_member_status) {
            this.try_member_status = try_member_status;
        }

        public String getExpiration_at() {
            return expiration_at;
        }

        public void setExpiration_at(String expiration_at) {
            this.expiration_at = expiration_at;
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

        public String getNickname_update_number() {
            return nickname_update_number;
        }

        public void setNickname_update_number(String nickname_update_number) {
            this.nickname_update_number = nickname_update_number;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getQqid() {
            return qqid;
        }

        public void setQqid(String qqid) {
            this.qqid = qqid;
        }

        public String getInvitation_code() {
            return invitation_code;
        }

        public void setInvitation_code(String invitation_code) {
            this.invitation_code = invitation_code;
        }

        public String getNew_one_status() {
            return new_one_status;
        }

        public void setNew_one_status(String new_one_status) {
            this.new_one_status = new_one_status;
        }

        public String getOne_withdraw_status() {
            return one_withdraw_status;
        }

        public void setOne_withdraw_status(String one_withdraw_status) {
            this.one_withdraw_status = one_withdraw_status;
        }

        public String getIs_advertiser() {
            return is_advertiser;
        }

        public void setIs_advertiser(String is_advertiser) {
            this.is_advertiser = is_advertiser;
        }

        public String getAd_release_number() {
            return ad_release_number;
        }

        public void setAd_release_number(String ad_release_number) {
            this.ad_release_number = ad_release_number;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getImageurl() {
            return imgurl;
        }

        public void setImageurl(String imageurl) {
            this.imgurl = imageurl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getChange() {
            return change;
        }

        public void setChange(String change) {
            this.change = change;
        }

        public String nickname;
        public String nickname_update_number;

        public String getApp_openid() {
            return app_openid;
        }

        public void setApp_openid(String app_openid) {
            this.app_openid = app_openid;
        }

        public String getApp_qqid() {
            return app_qqid;
        }

        public void setApp_qqid(String app_qqid) {
            this.app_qqid = app_qqid;
        }

        public String getQq_nickname() {
            return qq_nickname;
        }

        public void setQq_nickname(String qq_nickname) {
            this.qq_nickname = qq_nickname;
        }

        public String qq_nickname;

        public String getWx_nickname() {
            return wx_nickname;
        }

        public void setWx_nickname(String wx_nickname) {
            this.wx_nickname = wx_nickname;
        }



        public String getMember_status() {
            return member_status;
        }

        public void setMember_status(String member_status) {
            this.member_status = member_status;
        }

        public String getService_charge_percent() {
            return service_charge_percent;
        }

        public void setService_charge_percent(String service_charge_percent) {
            this.service_charge_percent = service_charge_percent;
        }

        public String service_charge_percent;

        public String getFive_withdraw_status() {
            return five_withdraw_status;
        }

        public void setFive_withdraw_status(String five_withdraw_status) {
            this.five_withdraw_status = five_withdraw_status;
        }

        public String getEight_withdraw_status() {
            return eight_withdraw_status;
        }

        public void setEight_withdraw_status(String eight_withdraw_status) {
            this.eight_withdraw_status = eight_withdraw_status;
        }

        public String eight_withdraw_status;
        public String is_advertiser;
        public String ad_release_number;
        public String gender;
        public String age;
        public String birth;
        public String income;
        public String imgurl;
        public String address;
        public String job;
        public String points;
        public String change;

        public String getConversion_ratio() {
            return conversion_ratio;
        }

        public void setConversion_ratio(String conversion_ratio) {
            this.conversion_ratio = conversion_ratio;
        }

        public String conversion_ratio;

        public String getWithdraw_change_number() {
            return withdraw_change_number;
        }

        public void setWithdraw_change_number(String withdraw_change_number) {
            this.withdraw_change_number = withdraw_change_number;
        }

        public String withdraw_change_number;

        public String getFirst_in_tantan_status() {
            return first_in_tantan_status;
        }

        public void setFirst_in_tantan_status(String first_in_tantan_status) {
            this.first_in_tantan_status = first_in_tantan_status;
        }

        public String first_in_tantan_status;

        public String getIs_complete_info() {
            return is_complete_info;
        }

        public void setIs_complete_info(String is_complete_info) {
            this.is_complete_info = is_complete_info;
        }

        public String is_complete_info;

        public List<String> getInterests() {
            return interests;
        }

        public void setInterests(List<String> interests) {
            this.interests = interests;
        }

        public List<String> interests;
        public String history_points;
        public String history_change;
        public String history_recharge_change;

        public String getDraw_lottery_number() {
            return draw_lottery_number;
        }

        public void setDraw_lottery_number(String draw_lottery_number) {
            this.draw_lottery_number = draw_lottery_number;
        }

        public String draw_lottery_number;

        public String getFirst_login_status() {
            return first_login_status;
        }

        public void setFirst_login_status(String first_login_status) {
            this.first_login_status = first_login_status;
        }

        public String first_login_status;

        public String getHistory_points() {
            return history_points;
        }

        public void setHistory_points(String history_points) {
            this.history_points = history_points;
        }

        public String getHistory_change() {
            return history_change;
        }

        public void setHistory_change(String history_change) {
            this.history_change = history_change;
        }

        public String getHistory_recharge_change() {
            return history_recharge_change;
        }

        public void setHistory_recharge_change(String history_recharge_change) {
            this.history_recharge_change = history_recharge_change;
        }

        public String getHistory_recharge_points() {
            return history_recharge_points;
        }

        public void setHistory_recharge_points(String history_recharge_points) {
            this.history_recharge_points = history_recharge_points;
        }

        public String history_recharge_points;

        public String getActivation() {
            return activation;
        }

        public void setActivation(String activation) {
            this.activation = activation;
        }

        public String activation;

        public String getFan_number() {
            return fan_number;
        }

        public void setFan_number(String fan_number) {
            this.fan_number = fan_number;
        }

        public String fan_number;

//        public class InterestBean{
//            public String getSports() {
//                return sports;
//            }
//
//            public void setSports(String sports) {
//                this.sports = sports;
//            }
//
//            public String getEntertainment() {
//                return entertainment;
//            }
//
//            public void setEntertainment(String entertainment) {
//                this.entertainment = entertainment;
//            }
//
//            public String entertainment;
//            public String sports;
//        }


        @Override
        public String toString() {
            return "UserDetailBean{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", nickname_update_number='" + nickname_update_number + '\'' +
                    ", app_openid='" + app_openid + '\'' +
                    ", openid='" + openid + '\'' +
                    ", app_qqid='" + app_qqid + '\'' +
                    ", qq_nickname='" + qq_nickname + '\'' +
                    ", wx_nickname='" + wx_nickname + '\'' +
                    ", qqid='" + qqid + '\'' +
                    ", invitation_code='" + invitation_code + '\'' +
                    ", new_one_status='" + new_one_status + '\'' +
                    ", one_withdraw_status='" + one_withdraw_status + '\'' +
                    ", five_withdraw_status='" + five_withdraw_status + '\'' +
                    ", member_status='" + member_status + '\'' +
                    ", service_charge_percent='" + service_charge_percent + '\'' +
                    ", eight_withdraw_status='" + eight_withdraw_status + '\'' +
                    ", is_advertiser='" + is_advertiser + '\'' +
                    ", ad_release_number='" + ad_release_number + '\'' +
                    ", gender='" + gender + '\'' +
                    ", age='" + age + '\'' +
                    ", birth='" + birth + '\'' +
                    ", income='" + income + '\'' +
                    ", imgurl='" + imgurl + '\'' +
                    ", address='" + address + '\'' +
                    ", job='" + job + '\'' +
                    ", points='" + points + '\'' +
                    ", change='" + change + '\'' +
                    ", conversion_ratio='" + conversion_ratio + '\'' +
                    ", withdraw_change_number='" + withdraw_change_number + '\'' +
                    ", first_in_tantan_status='" + first_in_tantan_status + '\'' +
                    ", is_complete_info='" + is_complete_info + '\'' +
                    ", interests=" + interests +
                    ", history_points='" + history_points + '\'' +
                    ", history_change='" + history_change + '\'' +
                    ", history_recharge_change='" + history_recharge_change + '\'' +
                    ", draw_lottery_number='" + draw_lottery_number + '\'' +
                    ", first_login_status='" + first_login_status + '\'' +
                    ", history_recharge_points='" + history_recharge_points + '\'' +
                    ", activation='" + activation + '\'' +
                    ", fan_number='" + fan_number + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", accesses=" + accesses +
                '}';
    }
}

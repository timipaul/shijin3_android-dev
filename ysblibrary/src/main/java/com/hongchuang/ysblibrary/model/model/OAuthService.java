package com.hongchuang.ysblibrary.model.model;


import android.app.Activity;
import android.content.Context;

import com.hongchuang.ysblibrary.dao.BlackListBean;
import com.hongchuang.ysblibrary.model.model.bean.AdAllianceBean;
import com.hongchuang.ysblibrary.model.model.bean.AdBean;
import com.hongchuang.ysblibrary.model.model.bean.AdCommentBean;
import com.hongchuang.ysblibrary.model.model.bean.AdLikeBean;
import com.hongchuang.ysblibrary.model.model.bean.AdReleaseBean;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.AnswersBean;
import com.hongchuang.ysblibrary.model.model.bean.BannerBean;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.BasketBean;
import com.hongchuang.ysblibrary.model.model.bean.BoxBean;
import com.hongchuang.ysblibrary.model.model.bean.BoxlistBean;
import com.hongchuang.ysblibrary.model.model.bean.CategoriesBean;
import com.hongchuang.ysblibrary.model.model.bean.CertificationBean;
import com.hongchuang.ysblibrary.model.model.bean.ChangeUserInfoBean;
import com.hongchuang.ysblibrary.model.model.bean.CommodityCardBean;
import com.hongchuang.ysblibrary.model.model.bean.FatherCommentBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowNumberBean;
import com.hongchuang.ysblibrary.model.model.bean.FollowUserBean;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationInfoBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.LotteryListBean;
import com.hongchuang.ysblibrary.model.model.bean.MessageBean;
import com.hongchuang.ysblibrary.model.model.bean.PayChangeBean;
import com.hongchuang.ysblibrary.model.model.bean.PaymentBean;
import com.hongchuang.ysblibrary.model.model.bean.PerfecterBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.PointBean;
import com.hongchuang.ysblibrary.model.model.bean.PointDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.PromotionBean;
import com.hongchuang.ysblibrary.model.model.bean.RecomWordsBean;
import com.hongchuang.ysblibrary.model.model.bean.SearchBean;
import com.hongchuang.ysblibrary.model.model.bean.SearchedBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.hongchuang.ysblibrary.model.model.bean.SignBean;
import com.hongchuang.ysblibrary.model.model.bean.SignInsBean;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.hongchuang.ysblibrary.model.model.bean.SystemMessageBean;
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.hongchuang.ysblibrary.model.model.bean.TodayFollowBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.model.model.bean.UserDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.VIpStateBean;
import com.hongchuang.ysblibrary.model.model.bean.VipPriceBean;
import com.hongchuang.ysblibrary.model.model.bean.VipRechangeBean;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.hongchuang.ysblibrary.model.model.bean.YesterdayBean;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginHandle;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginResultBean;

import java.util.List;
import java.util.Map;

import retrofit.callback.YRequestCallback;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/11/22
 ***/

public interface OAuthService {


    /* 卡账号密码提交验证 */
    void card_verify(Map<String, Object> map, YRequestCallback<CommodityCardBean> callback);
    /*提交卡片兑换地址信息*/
    void set_card_data(Map<String, Object> map, YRequestCallback<CommodityCardBean> callback);
    /* 提交商品信息 收货地址 */
    void set_goods_data(Map<String, Object> map, YRequestCallback<GoodsBean> callback);
    /* 提交转发分享信息 */
    void set_transmit_data(YRequestCallback<TaskBean> callback);
    /* 获取任务奖励 */
    void get_task_award(String username,Map<String, Object> map, YRequestCallback<TaskBean> callback);
    /* 获取任务完成状况 */
    void get_task_detail(String username,YRequestCallback<TaskBean> callback);
    /* 点击投票 */
    void join_vote(Map<String, Object> map,YRequestCallback<VoteBean> callback);
    /* 获取投票记录 */
    void get_vote_record(String username,Map<String, Object> map,YRequestCallback<BaseBean<VoteBean>> callback);
    /* 投票列表 */
    void get_vote_list(String uid,Map<String, Object> map,YRequestCallback<BaseBean<VoteBean>> callback);
    /* 获取联盟广告显示 */
    void get_alliance_ad(Map<String, Object> map,YRequestCallback<BaseBean<AdAllianceBean>> callback);
    /* 提交邀请码 */
    void submit_invitation_code(Map<String, Object> map,YRequestCallback<ShenmiBean> callback);
    /* 获取投票地址 */
    void get_vote(YRequestCallback<VoteBean> callback);
    /*抢购记录 */
    void rush_goods(Map<String, Object> map,YRequestCallback<BaseBean<PointDetailBean>> callback);
    /* 普通用户每日登陆加金币 */
    void points_add(Map<String, Object> map,YRequestCallback<PointDetailBean> callback);
    /*抢购记录 */
    void exchange_goods(Map<String, Object> map,YRequestCallback<BaseBean<PointDetailBean>> callback);
    /* 0元购地址填写 */
    void put_site_data(Map<String, String> map,YRequestCallback<GoodsBean> callback);
    /* 兑换专区 */
    void get_exchange_data(Map<String, String> map,YRequestCallback<BaseBean<GoodsBean>> callback);
    /* 会员金币增加数量 */
    void get_gold_sum(Map<String, String> map,YRequestCallback<VipRechangeBean> callback);
    /* 显示红包list */
    void get_red_list(Map<String, String> map,YRequestCallback<BaseBean<VipRechangeBean>> callback);
    /* 会员打开红包 */
    void open_red_package(Map<String, String> map,YRequestCallback<PicCodeBean> callback);
    /* 会员价格数据 */
    void get_vip_price(YRequestCallback<BaseBean<VipPriceBean>> callback);
    /*免费领取三天会员*/
    void free_vip(Map<String, Object> map,YRequestCallback<VIpStateBean> callback);

    void getad(Map bean, YRequestCallback<ShenmiBean> callback);
    /*
    获取图形验证码
     */
    void pic_code(Map<String, String> map, YRequestCallback<PicCodeBean> callback);
/*
    人机验证-投篮（安卓和ios专用）
     */
    void basket(Map<String, String> map, YRequestCallback<BasketBean> callback);

    /*
     校验图形验证码
     */
    void pic_code_comparison(Map<String, String> map, YRequestCallback<PicCodeBean> callback);

    /*
        发送短信
        */
    void cellphone_code(Map<String, String> map, YRequestCallback<PicCodeBean> callback);

    /*
        验证短信
        */
    void cellphone_code_comparison(Map<String, String> map, YRequestCallback<PicCodeBean> callback);

 /*
        注册
        */
    void signup(Map<String, String> map, YRequestCallback<UserBean> callback);
 /*
        登录
        */
    void login(Map<String, String> map, YRequestCallback<UserBean> callback);
 /*
        忘记密码
        */
    void forgetpassword(Map<String, String> map, YRequestCallback<PicCodeBean> callback);
    /**
     * 调起QQ登录,兼容低端手机
     *
     * @param activity
     * @param callback
     */
    void tencentAuthorized(Activity activity, QQLoginHandle handle, YRequestCallback<QQLoginResultBean> callback);


    /**
     * 调起微信登录
     *
     * @param activity
     */
    void wechatAuthorized(Activity activity, final TencentCallback callback);

    /**
     * 微信登录
     *
     */
    void wechat(Map<String, String> map, YRequestCallback<UserBean> callback);

    /**
     * QQ登录
     *
     */
    void qq(Map<String, String> map, YRequestCallback<UserBean> callback);

    /*
    绑定手机
     */
    void bindphone(String uid,Map<String, String> map,YRequestCallback<PicCodeBean> callback);
  /*
    获取Banner
     */
    void banners(Map<String, String> map,YRequestCallback<BannerBean> callback);
/*
    获取广告列表
     */
    void ads(Map<String, String> map,YRequestCallback<AdsBean> callback);
/*
    获取广告列表
     */
    void tantan_ads(Map<String, String> map,YRequestCallback<AdsBean> callback);
/*
    对广告进行屏蔽（首页的x选）
     */
    void shield(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    获取广告页
     */
    void start_pages(Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    修改个人信息
     */
    void change_info(String uis, ChangeUserInfoBean map, YRequestCallback<PicCodeBean> callback);/*
/*
    修改密码
     */
    void change_password(String uis,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);/*
/*
    修改个人信息
     */
    void prefect_info(String uis,PerfecterBean map, YRequestCallback<PicCodeBean> callback);/*
    微信解绑
     */
    void delete_wechat(String uid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    获取图片和视频统一上传的sts临时凭证
     */
    void stst(Map<String, Object> map,YRequestCallback<StsBean> callback);
/*
    拉黑用户的记录
     */
    void user_black(Map<String, Object> map,YRequestCallback<BlackListBean> callback);
/*
    获取当前未读消息的个数(包括子目录的个数)
     */
    void get_message(String uid,Map<String, Object> map,YRequestCallback<MessageBean> callback);
/*
    账户消息：积分的消息
     */
    void point(Map<String, Object> map,YRequestCallback<PointBean> callback);
/*
    账户消息：积分的消息
     */
    void change(Map<String, Object> map,YRequestCallback<PointBean> callback);
/*
    删除我的各类消息
     */
    void delete_messages(Map<String, Object> map,List<String> ids,YRequestCallback<PicCodeBean> callback);
/*
    系统通知
     */
    void circular(Map<String, Object> map,YRequestCallback<SystemMessageBean> callback);
/*
    动态消息：评论我的广告的消息
     */
    void ad_comment(Map<String, Object> map,YRequestCallback<AdCommentBean> callback);
/*
    动态消息：我评论广告的消息
     */
    void comment_ad(Map<String, Object> map,YRequestCallback<AdCommentBean> callback);
/*
    广告进行子评论
     */
    void child_comments(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    广告进行父评论
     */
    void father_comments_put(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    动态消息：关注我的消息
     */
    void user_like(Map<String, Object> map,YRequestCallback<FollowBean> callback);
/*
    动态消息：点赞我的广告的消息
     */
    void ad_like(Map<String, Object> map,YRequestCallback<AdLikeBean> callback);
/*
    广告商认证
     */
    void promotion(String uid, CertificationBean map, YRequestCallback<PicCodeBean> callback);
/*
    广告商认证
     */
    void getpromotion(String uid,Map<String, Object> map,YRequestCallback<PromotionBean> callback);
    /*
    上传图片
     */
    void upload_oss(Context context,String ObjectKey, String filePath, OssCallback callback);
 /*
    用户-获取个人信息
     */
    void getuserinfo(String uid,Map<String, Object> map,YRequestCallback<UserBean.UserDetailBean> callback);
/*
    用户每天的关注总数的记录
     */
    void daily_follow_number(Map<String, Object> map,YRequestCallback<BaseBean<FollowNumberBean>> callback);
/*
    数据统计：用户的关注的今天的参数（取消和关注数）
     */
    void today_follow_number(String uid,Map<String, Object> map,YRequestCallback<TodayFollowBean> callback);
/*
    数据统计：用户的所有广告的昨天参数概况
     */
    void yesterday_record(String uid,Map<String, Object> map,YRequestCallback<YesterdayBean> callback);
/*
    广告发布记录
     */
    void ad_release(Map<String, Object> map,YRequestCallback<AdReleaseBean> callback);
/*
    草稿香港
     */
    void get_draft(Map<String, Object> map,YRequestCallback<AdsBean> callback);
/*
    广告看过的记录
     */
    void ad_look(Map<String, Object> map,YRequestCallback<AdReleaseBean> callback);
/*
    广告看过的记录
     */
    void ad_collection(Map<String, Object> map,YRequestCallback<AdReleaseBean> callback);
/*
    用户发布过的广告
     */
    void get_put(Map<String, Object> map,YRequestCallback<AdsBean> callback);
/*
    （个人主页）获取他人的关注列表
     */
    void user_follow(Map<String, Object> map,YRequestCallback<BaseBean<FollowUserBean>> callback);
/*
    关注用户：变成粉丝
     */
    void fans(String uid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    取消关注：变成路人
     */
    void unfans(String uid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    意见反馈：新建意见反馈
     */
    void feedbacks(Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    获取父评论

     */
    void father_comments(String uid,Map<String, Object> map,YRequestCallback<BaseBean<FatherCommentBean>> callback);
/*
    获取子评论

     */
    void child_comment(String uid,Map<String, Object> map,YRequestCallback<BaseBean<FatherCommentBean.Comments>> callback);
/*
    广告内推荐列表

     */
    void recommend_ads(Map<String, Object> map,YRequestCallback<AdsBean> callback);
/*
    广告详情

     */
    void ads_detail(String aid,Map<String, Object> map,YRequestCallback<BaseBean<AdBean>> callback);
/*
    广告详情

     */
    void answers(String aid,Map<String, Object> map,YRequestCallback<AnswersBean> callback);
/*
    对广告进行答题的状态切换

     */
    void answer_status(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    积分兑换：获取待兑换商品的列表

     */
    void goods(Map<String, Object> map,YRequestCallback<BaseBean<GoodsBean>> callback);
/*
      积分兑换：进行兑换
     */
    void exchange(Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
      积分记录
     */
    void point_detail(Map<String, Object> map,YRequestCallback<BaseBean<PointDetailBean>> callback);
/*
      积分记录
     */
    void change_detail(Map<String, Object> map,YRequestCallback<BaseBean<PointDetailBean>> callback);
/*
      获取个人的奖励的列表
     */
    void lotteries(String uid,Map<String, Object> map,YRequestCallback<BaseBean<BoxBean>> callback);
/*
      提现记录
     */
    void payment(Map<String, Object> map,YRequestCallback<BaseBean<PaymentBean>> callback);
/*
      提现记录
     */
    void lotterylist(Map<String, Object> map,YRequestCallback<BaseBean<LotteryListBean>> callback);
/*
      邀请动态
     */
    void invitation_record(Map<String, Object> map,YRequestCallback<BaseBean<InvitationRecordBean>> callback);
/*
      邀请记录
     */
    void invitation(Map<String, Object> map,YRequestCallback<BaseBean<InvitationBean>> callback);
/*
      邀请首页
     */
    void invitation_info(Map<String, Object> map,YRequestCallback<InvitationInfoBean> callback);
/*
      推荐关键词
     */
    void recom_words(Map<String, Object> map,YRequestCallback<RecomWordsBean> callback);
/*
      搜索记录
     */
    void search(Map<String, Object> map,YRequestCallback<BaseBean<SearchBean>> callback);
/*
      搜索记录
     */
    void getsearch(Map<String, Object> map,YRequestCallback<SearchedBean> callback);
/*
      （个人主页）获取其他人的关注数、粉丝数、发布列表、答题列表信息
     */
    void follow(String uid,Map<String, Object> map,YRequestCallback<UserDetailBean> callback);
/*
      （个人主页）获取他人的发布广告记录
     */
    void otherpulish(String uid,Map<String, Object> map,YRequestCallback<AdsBean> callback);
/*
     （个人主页）获取他人的答题广告记录
     */
    void otheranswer(String uid,Map<String, Object> map,YRequestCallback<AdsBean> callback);
/*
     （个人主页）获取他人的答题广告记录
     */
    void follow_details(String uid,Map<String, Object> map,YRequestCallback<FollowDetailBean> callback);
/*
     （个人主页）获取他人的答题广告记录
     */
    void other_user_like(Map<String, Object> map,YRequestCallback<FollowDetailBean> callback);
/*
     （个人主页）获取他人的答题广告记录
     */
    void other_fans(Map<String, Object> map,YRequestCallback<FollowDetailBean> callback);
/*
     微信支付-充值-统一下单(app)
     */
    void preorder(Map<String,Object> map, YRequestCallback<WechatPayBean> callback);
/*
     抽奖：进行抽奖
     */
    void lottery(Map<String, Object> map,YRequestCallback<BaseBean<LotteryListBean>> callback);
/*
     宝箱：开启宝箱
     */
    void open_box(Map<String, Object> map,YRequestCallback<BaseBean<LotteryListBean>> callback);
/*
     宝箱：获取宝箱的开箱计划
     */
    void box_list(Map<String, Object> map,YRequestCallback<BoxlistBean> callback);
/*
     签到：获取当前用户的签到信息及其他信息
     */
    void sign_ins(Map<String, Object> map,YRequestCallback<SignBean> callback);
/*
     签到
     */
    void sign(Map<String, Object> map,YRequestCallback<SignInsBean> callback);
/*
     补签
     */
    void addsign(Map<String, Object> map,YRequestCallback<SignInsBean> callback);
/*
     新增广告：标题和内容
     */
    void new_title(Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     更新广告
     */
    void updata_ads(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     发布广告
     */
    void releases(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     给个人广告 发布时充值或续费：使用零钱
     */
    void recharge(String aid, PayChangeBean map, YRequestCallback<PicCodeBean> callback);
/*
     广告点赞
     */
    void likes(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     广告取消点赞
     */
    void unlikes(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     广告收藏
     */
    void collections(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     取消收藏
     */
    void uncollections(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     评论点赞
     */
    void like_comments(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     评论取消点赞
     */
    void unlike_comments(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     评论删除
     */
    void delete_comments(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     举报
     */
    void complaints(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     qqbind
     */
    void qqbindphone(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     qqbind
     */
    void black(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     qqbind
     */
    void unblack(String aid,Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
    微信支付-提现-企业付款 qqbind
     */
    void transfer(Map<String, Object> map,YRequestCallback<PicCodeBean> callback);
/*
     获取新手红包
     */
    void new_red_package(YRequestCallback<PicCodeBean> callback);
/*
     对广告不喜欢
     */
    void adunlikes(String aid,Map<String,Object> map,YRequestCallback<PicCodeBean> callback);
/*
     获取分享的信息（标题、简介、图片地址）
     */
    void share_infos(String mode,YRequestCallback<ShareBean> callback);
/*
     登出
     */
    void logoff(YRequestCallback<PicCodeBean> callback);
/*
     广告详情内直达链接的点击
     */
    void url_click(String aid,YRequestCallback<PicCodeBean> callback);
/*
     广告分享转发
     */
    void shares(String aid,Map map,YRequestCallback<PicCodeBean> callback);
/*
     轮播图点击
     */
    void click(String id,Map map,YRequestCallback<PicCodeBean> callback);
/*
    进行广告页的点击
     */
    void start_click(String id,Map map,YRequestCallback<PicCodeBean> callback);
/*
    删除搜索记录
     */
    void delete_search(String map,YRequestCallback<PicCodeBean> callback);
/*
    积分兑换：兑换成零钱
     */
    void convert(String points,YRequestCallback<PicCodeBean> callback);
/*
    删除草稿箱
     */
    void delete_draft(String aid,Map map,YRequestCallback<PicCodeBean> callback);

    /**
     * V2
     */
    /*
    首页：获取自定义的动态频道
     */
    void categories(YRequestCallback<BaseBean<CategoriesBean>> callback);

    /*
    获取广告列表
     */
    void ads(String channel,Map<String, String> map,YRequestCallback<AdsBean> callback);

}

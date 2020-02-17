package com.hongchuang.ysblibrary.common;


import com.hongchuang.hclibrary.view.FlowTag;
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
import com.hongchuang.ysblibrary.model.model.bean.GoodsClassifyBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationInfoBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.InviteRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.LotteryListBean;
import com.hongchuang.ysblibrary.model.model.bean.LoveShareGroupBean;
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
import com.hongchuang.ysblibrary.model.model.bean.ShareUserBean;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.SignBean;
import com.hongchuang.ysblibrary.model.model.bean.SignInsBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreRushBean;
import com.hongchuang.ysblibrary.model.model.bean.StroeCommentBean;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.hongchuang.ysblibrary.model.model.bean.SystemMessageBean;
import com.hongchuang.ysblibrary.model.model.bean.TakeMoneyRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.hongchuang.ysblibrary.model.model.bean.TodayFollowBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.model.model.bean.UserDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.UserRankingBean;
import com.hongchuang.ysblibrary.model.model.bean.UserSiteBean;
import com.hongchuang.ysblibrary.model.model.bean.VIpStateBean;
import com.hongchuang.ysblibrary.model.model.bean.VipPriceBean;
import com.hongchuang.ysblibrary.model.model.bean.VipRechangeBean;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.hongchuang.ysblibrary.model.model.bean.YesterdayBean;
import com.hongchuang.ysblibrary.model.model.bean.storeUserBean;

import java.util.List;
import java.util.Map;

import retrofit.callback.BaseResult;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/***
 * 功能描述:结果管理
 ***/

public interface IYSBApi {
    //    String urltop = "/mock/31be2c67af5dd7c22875ca0de396e276/";
    String urltop = "";


    /* 获取用户分享信息 */
    @GET("user/ishare")
    Observable<BaseResult<ShareUserBean>> getUserShare(@QueryMap Map<String, Object> map);
    /* 爱分享 分享成功 */
    @POST("goods/{id}")
    Observable<BaseResult<storeUserBean>> getShareSucceed(@Path("id") String id);
    /* 爱分享 团队 */
    @GET("user/subordinate")
    Observable<BaseResult<LoveShareGroupBean>> getSubordinate(@QueryMap Map<String, Object> map);
    /* 绑定微信 */
    @PUT("user/bindWx")
    Observable<BaseResult<storeUserBean>> putBindWx(@Body Map<String, Object> map);

    /* 商城用户信息 */
    @POST("user/info")
    Observable<BaseResult<storeUserBean>> getStoreUser(@Body Map<String, Object> map);

    /* 商城礼包 */
    @GET("goods/gift/pcakage/all")
    Observable<BaseResult<BaseBean<StoreGoodsBean>>> getGiftAll(@QueryMap Map<String, Object> map);

    /* 我的团队 */
    @GET("inviteRecord")
    Observable<BaseResult<InviteRecordBean>> getInviteRecord(@QueryMap Map<String, Object> map);

    /* 分类列表 */
    @GET("classify/format")
    Observable<BaseResult<BaseBean<GoodsClassifyBean>>> getClassifyList(@QueryMap Map<String, Object> map);

    /* 添加合伙人*/
    @POST("inviteRecord")
    Observable<BaseResult<InviteRecordBean>> putMember(@Body Map<String, Object> map);
    /* 现金余额*/

    /* 提现记录*/
    @GET("capitalFlow")
    Observable<BaseResult<BaseBean<TakeMoneyRecordBean>>> getCapitalFlow(@QueryMap Map<String, Object> map);

    /* 商城提现 */
    @POST("capitalFlow")
    Observable<BaseResult<TakeMoneyRecordBean>> postCapitalFlow(@QueryMap Map<String, Object> map);

    /* 商城评论数据 */
    @GET("comment/{goodsId}")
    Observable<BaseResult<BaseBean<StroeCommentBean>>> getStoreComment(@Path("goodsId") String goodsId, @QueryMap Map<String, Object> map);

    /* 商城评论新增*/
    @POST("comment")
    Observable<BaseResult<StroeCommentBean>> putStoreComment(@Body Map<String, Object> map);

    /* 店铺搜索 */
    @GET("goods")
    Observable<BaseResult<BaseBean<ShoppingShopBean>>> getShopSeek(@QueryMap Map<String, Object> map);

    /* 订单删除 */
    @GET("order/{id}")
    Observable<BaseResult<ShoppingShopBean>> delOrderRecord(@Path("id") String id);

    /* 订单列表查询 */
    @GET("order")
    Observable<BaseResult<BaseBean<ShoppingShopBean>>> getOrderRecord(@QueryMap Map<String, Object> map);

    /* 收货地址列表*/
    @GET("user/address/{username}")
    Observable<BaseResult<BaseBean<UserSiteBean>>> getAddress(@Path("username") String username);

    /* 收货地址删除 */
    //@DELETE("user/address/{username}")
    @HTTP(method = "DELETE", path = "/mall/service/user/address/{username}", hasBody = true)
    Observable<BaseResult<UserSiteBean>> delAddress(@Path("username") String username, @Body Map<String, Object> map);

    /* 收货地址修改 */
    @PUT("user/address/{username}")
    Observable<BaseResult<UserSiteBean>> putAddress(@Path("username") String username, @Body Map<String, Object> map);

    /* 商铺列表 */
    @GET("shop")
    Observable<BaseResult<BaseBean<StoreBean>>> getShopList();

    /* 商城店铺信息 */
    @GET("")
    Observable<BaseResult<StoreGoodsBean>> getShopOne(@QueryMap Map<String, Object> map);

    /* 商城商品分类 */
    @GET("goods")
    Observable<BaseResult<BaseBean<StoreGoodsBean>>> getShopGoods(@QueryMap Map<String, Object> map);

    /* 提交购物车订单 */
    @POST("order/other")
    Observable<BaseResult<WechatPayBean>> putCarGoods(@Body Map<String, Object> map);

    /* 购物车增加 */
    @POST("shoppingCart/{goods_id}")
    Observable<BaseResult<StoreGoodsBean>> addCarGoods(@Path("goods_id") String goods_id, @Body Map<String, Object> map);

    /* 购物车删除 */
    @HTTP(method = "DELETE", path = "/mall/service/shoppingCart", hasBody = true)
    Observable<BaseResult<StoreGoodsBean>> deleteCarGoods(@Body Map<String, Object> map);

    /* 购物车修改 */
    @PUT("shoppingCart/{goods_id}")
    Observable<BaseResult<StoreGoodsBean>> updateCarGoods(@Path("goods_id") String goods_id, @Body Map<String, Object> map);

    /* 购物车查询 */
    @GET("shoppingCart")
    Observable<BaseResult<BaseBean<ShoppingShopBean>>> getCarGoods(@QueryMap Map<String, Object> map);

    /* 商城详情 */
    @GET("goods/{id}")
    Observable<BaseResult<ShoppingShopBean>> getGoodsDetails(@Path("id") String _id);

    /* 商城限时抢购 */
    @GET(urltop + "panicBuying")
    Observable<BaseResult<StoreRushBean>> getStoreRush(@QueryMap Map<String, Object> map);

    /* 商城热门搜素 */
    @GET("setting/popularSearch")
    Observable<BaseResult<BaseBean<FlowTag>>> getStoreSeekHot();

    /* 商城轮播图 */
    @GET(urltop + "setting/homeAds")
    Observable<BaseResult<StoreBean>> getStoreCarousel();

    /* 商城视频 */
    @GET(urltop + "setting/homeVideo")
    Observable<BaseResult<BaseBean<StoreBean>>> getStoreVideo();

    /* 视频列表 */
    @GET("video")
    Observable<BaseResult<BaseBean<StoreBean>>> getVideo(@QueryMap Map<String, Object> map);

    /* 排行榜数据 */
    @GET("ranking")
    Observable<BaseResult<UserRankingBean>> getRanking(@QueryMap Map<String, Object> map);

    /* 游戏入口是否显示 广告是否允许播放 */
    @GET(urltop + "game/switch")
    Observable<BaseResult<ShenmiBean>> getGameStatue();

    /* 验证兑换卡 卡号密码 */
    @POST(urltop + "cardDetails/check")
    Observable<BaseResult<CommodityCardBean>> card_verify(@Body Map<String, Object> map);

    /* 优惠卡领取 */
    @POST(urltop + "order/card")
    Observable<BaseResult<WechatPayBean>> set_card_data(@Body Map<String, Object> map);

    @POST(urltop + "goods/info")
    Observable<BaseResult<GoodsBean>> set_goods_data(@Body Map<String, Object> map);

    /* 提交转发记录 */
    @PUT(urltop + "revolution/task")
    Observable<BaseResult<TaskBean>> set_transmit_data();

    /* 完成任务领取奖励 */
    @POST(urltop + "revolution/task/{username}")
    Observable<BaseResult<TaskBean>> get_task_award(@Path("username") String username, @Body Map<String, Object> map);

    /* 获取任务完成状况 */
    @GET(urltop + "revolution/task/{username}")
    Observable<BaseResult<TaskBean>> get_task_detail(@Path("username") String username);

    /*点击投票 */
    @POST(urltop + "revolution/voteRecord")
    Observable<BaseResult<VoteBean>> join_vote(@Body Map<String, Object> map);

    /* 获取投票记录 */
    @GET(urltop + "revolution/voteRecord/{username}")
    Observable<BaseResult<BaseBean<VoteBean>>> get_vote_record(@Path("username") String username, @QueryMap Map<String, Object> map);

    /* 获取投票显示数据 */
    @GET(urltop + "revolution/vote/{username}")
    Observable<BaseResult<BaseBean<VoteBean>>> get_vote_list(@Path("username") String uid, @QueryMap Map<String, Object> map);

    /* 获取优惠劵记录显示 */
    @GET(urltop + "revolution/taobao")
    Observable<BaseResult<BaseBean<AdAllianceBean>>> get_alliance_ad(@QueryMap Map<String, Object> map);

    /* 提交回填邀请码 */
    @PUT(urltop + "records/call_back")
    Observable<BaseResult<ShenmiBean>> submit_invitation_code(@Body Map<String, Object> map);

    /*获取投票地址*/
    @GET(urltop + "vote/getdata")
    Observable<BaseResult<VoteBean>> get_vote();

    /*兑换记录*/
    @POST(urltop + "goods/goodsrecord")
    Observable<BaseResult<BaseBean<PointDetailBean>>> rush_goods(@QueryMap Map<String, Object> map);

    /* 每日登陆加金币 */
    @POST(urltop + "points_add")
    Observable<BaseResult<PointDetailBean>> points_add(@QueryMap Map<String, Object> map);

    /*抢购记录*/
    @POST(urltop + "goods/zeroshoprecord")
    Observable<BaseResult<BaseBean<PointDetailBean>>> exchange_goods(@QueryMap Map<String, Object> map);

    /* 提交抢购数据 */
    @POST(urltop + "goods/zeroshop")
    Observable<BaseResult<GoodsBean>> put_site_data(@Body Map<String, String> map);

    /* 获取兑换专区 */
    @POST(urltop + "goods")
    Observable<BaseResult<BaseBean<GoodsBean>>> get_exchange_data(@Body Map<String, String> map);

    /* 金币增加数量 */
    @POST(urltop + "member_points_gadd")
    Observable<BaseResult<VipRechangeBean>> get_gold_sum(@Body Map<String, String> map);

    /* 显示红包list */
    @POST(urltop + "everyday_red_package_number")
    Observable<BaseResult<BaseBean<VipRechangeBean>>> get_red_list(@Body Map<String, String> map);

    /* 会员打开红包 */
    @POST(urltop + "everyday_red_package")
    Observable<BaseResult<PicCodeBean>> open_red_package(@Body Map<String, String> map);

    /* 获取开通会员价格 */
    @GET(urltop + "member_setting")
    Observable<BaseResult<BaseBean<VipPriceBean>>> get_vip_price();

    /* 免费领取三天会员 */
    @POST(urltop + "free_membership")
    Observable<BaseResult<VIpStateBean>> free_vip(@Body Map<String, Object> map);


    @POST("getad")
    Observable<ShenmiBean> getad(@Body Map bean);

    /*
    获取图形验证码
     */
    @GET(urltop + "common/pic_code")
    Observable<BaseResult<PicCodeBean>> pic_code(@QueryMap Map<String, String> map);

    /*
      人机验证-投篮（安卓和ios专用）
       */
    @GET(urltop + "common/afs/basket")
    Observable<BaseResult<BasketBean>> basket(@QueryMap Map<String, String> map);

    /*
    校验图形验证码
     */
    @GET(urltop + "common/pic_code_comparison")
    Observable<BaseResult<PicCodeBean>> pic_code_comparison(@QueryMap Map<String, String> map);

    /*
        发送短信
         */
    @GET(urltop + "common/cellphone_code")
    Observable<BaseResult<PicCodeBean>> cellphone_code(@QueryMap Map<String, String> map);

    /*
        验证短信
         */
    @GET(urltop + "common/cellphone_code_comparison")
    Observable<BaseResult<PicCodeBean>> cellphone_code_comparison(@QueryMap Map<String, String> map);

    /*
           注册
            */
    @POST(urltop + "signup")
    Observable<BaseResult<UserBean>> signup(@Body Map<String, String> map);

    /*
               登录
                */
    @POST(urltop + "login")
    Observable<BaseResult<UserBean>> login(@Body Map<String, String> map);

    /*
               用户-获取个人信息
                */
    @GET(urltop + "users/{uid}")
    Observable<BaseResult<UserBean.UserDetailBean>> getuserinfo(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
           wechat登录
            */
    @GET(urltop + "login/wechat")
    Observable<BaseResult<UserBean>> wechat(@QueryMap Map<String, String> map);

    /*
              qq登录
               */
    @GET(urltop + "login/qq")
    Observable<BaseResult<UserBean>> qq(@QueryMap Map<String, String> map);


    /*
        忘记密码
            */
    @PUT(urltop + "users/password")
    Observable<BaseResult<PicCodeBean>> forgetpassword(@Body Map<String, String> map);

    /*
      绑定手机
     */
    @PUT(urltop + "users/{uid}/wechat")
    Observable<BaseResult<PicCodeBean>> bindphone(@Path("uid") String uid, @Body Map<String, String> map);

    /*
         qq绑定手机
        */
    @PUT(urltop + "users/{uid}/qq")
    Observable<BaseResult<PicCodeBean>> qqbindphone(@Path("uid") String uid, @Body Map<String, Object> map);

    /*
      获取Banner图
     */
    @GET(urltop + "banners")
    Observable<BaseResult<BannerBean>> banners(@QueryMap Map<String, String> map);

    /*
          获取广告列表
         */
    @GET(urltop + "ads")
    Observable<BaseResult<AdsBean>> ads(@QueryMap Map<String, String> map);

    /*
            探探式答题的广告列表
           */
    @GET(urltop + "ads")
    Observable<BaseResult<AdsBean>> tantan_ads(@QueryMap Map<String, String> map);

    /*
            对广告进行屏蔽（首页的x选）
           */
    @POST(urltop + "ads/{aid}/shields")
    Observable<BaseResult<PicCodeBean>> shield(@Path("aid") String aidl, @Body Map<String, Object> map);

    /*
              获取开启页
             */
    @GET(urltop + "start_pages")
    Observable<BaseResult<PicCodeBean>> start_pages(@QueryMap Map<String, Object> map);

    /*
              修改个人信息
             */
    @PUT(urltop + "users/{uid}")
    Observable<BaseResult<PicCodeBean>> change_info(@Path("uid") String uid, @Body ChangeUserInfoBean map);

    /*
                  完善个人信息
                 */
    @PUT(urltop + "users/{uid}")
    Observable<BaseResult<PicCodeBean>> prefect_info(@Path("uid") String uid, @Body PerfecterBean map);

    /*
                  完善个人信息
                 */
    @PUT(urltop + "users/{uid}")
    Observable<BaseResult<PicCodeBean>> change_password(@Path("uid") String uid, @Body Map<String, Object> map);

    /*
                 获取图片和视频统一上传的sts临时凭证
                */
    @GET(urltop + "common/sts")
    Observable<BaseResult<StsBean>> stst(@QueryMap Map<String, Object> map);

    /*
                 拉黑用户的记录
                 */
    @GET(urltop + "records/user_black")
    Observable<BaseResult<BlackListBean>> user_black(@QueryMap Map<String, Object> map);

    /*
              微信解绑
                 */
    @DELETE(urltop + "users/{uid}")
    Observable<BaseResult<PicCodeBean>> delete_wechat(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
                 获取当前未读消息的个数(包括子目录的个数)
                    */
    @GET(urltop + "users/{uid}")
    Observable<BaseResult<MessageBean>> get_message(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
                 账户消息：积分的消息
                    */
    @GET(urltop + "messages/point")
    Observable<BaseResult<PointBean>> point(@QueryMap Map<String, Object> map);

    /*
                 账户消息：零钱的消息
                    */
    @GET(urltop + "messages/change")
    Observable<BaseResult<PointBean>> change(@QueryMap Map<String, Object> map);

    /*
                 删除我的各类消息
                    */
    @DELETE(urltop + "messages")
    Observable<BaseResult<PicCodeBean>> delete_messages(@QueryMap Map<String, Object> map, @Query("message_ids") List<String> ids);

    /*
                 系统通知
                     */
    @GET(urltop + "messages/circular")
    Observable<BaseResult<SystemMessageBean>> circular(@QueryMap Map<String, Object> map);

    /*
                 动态消息：评论我的广告的消息
                     */
    @GET(urltop + "messages/ad_comment")
    Observable<BaseResult<AdCommentBean>> ad_comment(@QueryMap Map<String, Object> map);

    /*
                 动态消息：我评论广告的消息
                     */
    @GET(urltop + "messages/comment_ad")
    Observable<BaseResult<AdCommentBean>> comment_ad(@QueryMap Map<String, Object> map);

    /*
                 广告进行子评论
                     */
    @POST(urltop + "ads/{aid}/child_comments")
    Observable<BaseResult<PicCodeBean>> child_comments(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
                 广告进行子评论
                     */
    @POST(urltop + "ads/{aid}/father_comments")
    Observable<BaseResult<PicCodeBean>> father_comments_put(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
                 动态消息：关注我的消息
                     */
    @GET(urltop + "messages/user_like")
    Observable<BaseResult<FollowBean>> user_like(@QueryMap Map<String, Object> map);

    /*
                 动态消息：点赞我的广告的消息
                     */
    @GET(urltop + "messages/ad_like")
    Observable<BaseResult<AdLikeBean>> ad_like(@QueryMap Map<String, Object> map);

    /*
                 广告商认证
                     */
    @POST(urltop + "users/{uid}/promotion")
    Observable<BaseResult<PicCodeBean>> promotion(@Path("uid") String uid, @Body CertificationBean map);

    /*
                 获取广告商认证状态
                     */
    @GET(urltop + "users/{uid}/promotion")
    Observable<BaseResult<PromotionBean>> getpromotion(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
                 用户每天的关注总数的记录
                     */
    @GET(urltop + "records/daily_follow_number")
    Observable<BaseResult<BaseBean<FollowNumberBean>>> daily_follow_number(@QueryMap Map<String, Object> map);

    /*
                 数据统计：用户的关注的今天的参数（取消和关注数）
                     */
    @GET(urltop + "users/{uid}/today_follow_number")
    Observable<BaseResult<TodayFollowBean>> today_follow_number(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
                数据统计：用户的所有广告的昨天参数概况
                     */
    @GET(urltop + "users/{uid}/ads_yesterday_record")
    Observable<BaseResult<YesterdayBean>> yesterday_record(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
                数据统计：广告发布记录
                     */
    @GET(urltop + "records/ad_release")
    Observable<BaseResult<AdReleaseBean>> ad_release(@QueryMap Map<String, Object> map);

    /*
                数据统计：获取草稿箱广告
                     */
    @GET(urltop + "ads")
    Observable<BaseResult<AdsBean>> get_draft(@QueryMap Map<String, Object> map);

    /*
                删除草稿箱
                     */
    @DELETE(urltop + "ads/{aid}")
    Observable<BaseResult<PicCodeBean>> delete_draft(@Path("aid") String aid, @QueryMap Map<String, Object> map);

    /*
                用户发布过的广告
                     */
    @GET(urltop + "ads")
    Observable<BaseResult<AdsBean>> get_put(@QueryMap Map<String, Object> map);

    /*
                获取他人的关注列表
                     */
    @GET(urltop + "records/user_like")
    Observable<BaseResult<BaseBean<FollowUserBean>>> user_follow(@QueryMap Map<String, Object> map);

    /*
                关注用户：变成粉丝
                     */
    @POST(urltop + "users/{uid}/fans")
    Observable<BaseResult<PicCodeBean>> fans(@Path("uid") String uid, @Body Map<String, Object> map);

    /*
                取消关注：变成路人
                     */
    @DELETE(urltop + "users/{uid}/fans")
    Observable<BaseResult<PicCodeBean>> unfans(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
                广告看过的记录
                     */
    @GET(urltop + "records/ad_click")
    Observable<BaseResult<AdReleaseBean>> ad_look(@QueryMap Map<String, Object> map);

    /*
               广告收藏记录
                     */
    @GET(urltop + "records/ad_collection")
    Observable<BaseResult<AdReleaseBean>> ad_collection(@QueryMap Map<String, Object> map);

    /*意见反馈：新建意见反馈
     */
    @POST(urltop + "feedbacks")
    Observable<BaseResult<PicCodeBean>> feedbacks(@Body Map<String, Object> map);

    /*查询广告的父评论（一级评论）
     */
    @GET(urltop + "ads/{aid}/father_comments")
    Observable<BaseResult<BaseBean<FatherCommentBean>>> father_comments(@Path("aid") String uid, @QueryMap Map<String, Object> map);

    /*查询广告的子评论（二级评论或三级评论）
     */
    @GET(urltop + "ads/{aid}/child_comments")
    Observable<BaseResult<BaseBean<FatherCommentBean.Comments>>> child_comment(@Path("aid") String uid, @QueryMap Map<String, Object> map);

    /*广告内推荐列表
     */
    @GET(urltop + "ads")
    Observable<BaseResult<AdsBean>> recommend_ads(@QueryMap Map<String, Object> map);

    /*广告详情
     */
    @GET(urltop + "ads/{aid}")
    Observable<BaseResult<BaseBean<AdBean>>> ads_detail(@Path("aid") String uid, @QueryMap Map<String, Object> map);

    /*对广告进行答题
     */
    @POST(urltop + "ads/{aid}/answers")
    Observable<BaseResult<AnswersBean>> answers(@Path("aid") String uid, @Body Map<String, Object> map);

    /*对广告进行答题的状态切换
     */
    @POST(urltop + "ads/{aid}/answer_status")
    Observable<BaseResult<PicCodeBean>> answer_status(@Path("aid") String uid, @Body Map<String, Object> map);

    /*积分兑换：获取待兑换商品的列表
     */
    @GET(urltop + "goods")
    Observable<BaseResult<BaseBean<GoodsBean>>> goods(@QueryMap Map<String, Object> map);

    /*积分兑换：获取待兑换商品的列表
     */
    @POST(urltop + "goods/exchange")
    Observable<BaseResult<PicCodeBean>> exchange(@Body Map<String, Object> map);

    /*
         积分记录
                     */
    @GET(urltop + "records/point")
    Observable<BaseResult<BaseBean<PointDetailBean>>> point_detail(@QueryMap Map<String, Object> map);

    /*
         零钱记录
                     */
    @GET(urltop + "records/change")
    Observable<BaseResult<BaseBean<PointDetailBean>>> change_detail(@QueryMap Map<String, Object> map);

    /*
         获取个人的奖励的列表
                     */
    @GET(urltop + "users/{uid}")
    Observable<BaseResult<BaseBean<BoxBean>>> lotteries(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
         提现记录
                     */
    @GET(urltop + "records/payment")
    Observable<BaseResult<BaseBean<PaymentBean>>> payment(@QueryMap Map<String, Object> map);

    /*
         抽奖：获取抽奖的待抽奖品列表
                     */
    @GET(urltop + "v2/lottery/list")
    Observable<BaseResult<BaseBean<LotteryListBean>>> lotterylist(@QueryMap Map<String, Object> map);

    /*
         邀请：最近获得邀请奖励的记录动态（虚拟数据）
                     */
    @GET(urltop + "records/invitation")
    Observable<BaseResult<BaseBean<InvitationRecordBean>>> invitation_record(@QueryMap Map<String, Object> map);

    /*
          邀请：邀请首页的信息
                 */
    @GET(urltop + "invitation/invitation_info")
    Observable<BaseResult<InvitationInfoBean>> invitation_info(@QueryMap Map<String, Object> map);

    /*
         邀请记录
                     */
    @GET(urltop + "records/invitation")
    Observable<BaseResult<BaseBean<InvitationBean>>> invitation(@QueryMap Map<String, Object> map);

    /*
            搜索：获取推荐和默认的关键字
                        */
    @GET(urltop + "search/recom_words")
    Observable<BaseResult<RecomWordsBean>> recom_words(@QueryMap Map<String, Object> map);

    /*
             搜索记录
                         */
    @GET(urltop + "records/search")
    Observable<BaseResult<BaseBean<SearchBean>>> search(@QueryMap Map<String, Object> map);

    /*
             搜索：进行搜索
                         */
    @GET(urltop + "search")
    Observable<BaseResult<SearchedBean>> getsearch(@QueryMap Map<String, Object> map);

    /*
             （个人主页）获取其他人的关注数、粉丝数、发布列表、答题列表信息
                         */
    @GET(urltop + "users/{uid}/follows")
    Observable<BaseResult<UserDetailBean>> follow(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
             （个人主页）获取他人的发布广告记录
                         */
    @GET(urltop + "records/ad_release")
    Observable<BaseResult<AdsBean>> otherpulish(@QueryMap Map<String, Object> map);

    /*
             （个人主页）获取他人的答题广告记录
                         */
    @GET(urltop + "records/ad_answer")
    Observable<BaseResult<AdsBean>> otheranswer(@QueryMap Map<String, Object> map);

    /*
             （个人主页）获取其他人的关注用户列表， 粉丝列表
                         */
    @GET(urltop + "users/{uid}/follow_details")
    Observable<BaseResult<FollowDetailBean>> follow_details(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
             （个人主页）获取他人的关注列表
                         */
    @GET(urltop + "records/user_like")
    Observable<BaseResult<FollowDetailBean>> other_user_like(@QueryMap Map<String, Object> map);

    /*
             （个人主页）获取他人的粉丝列表
                         */
    @GET(urltop + "records/user_like")
    Observable<BaseResult<FollowDetailBean>> other_fans(@QueryMap Map<String, Object> map);

    /*
             微信支付-充值-统一下单(app)
                         */
    @GET(urltop + "payment/preorder")
    Observable<BaseResult<WechatPayBean>> preorder(@QueryMap Map<String, Object> map);

    /*
             宝箱：获取宝箱的开箱计划
                         */
    @GET(urltop + "lottery/box_list")
    Observable<BaseResult<BoxlistBean>> box_list(@QueryMap Map<String, Object> map);

    /*
             抽奖：进行抽奖
                         */
    @POST(urltop + "lottery")
    Observable<BaseResult<BaseBean<LotteryListBean>>> lottery(@Body Map<String, Object> map);

    /*
             宝箱：开启宝箱
                         */
    @POST(urltop + "lottery")
    Observable<BaseResult<BaseBean<LotteryListBean>>> open_box(@Body Map<String, Object> map);

    /*
             签到：获取当前用户的签到信息及其他信息
                         */
    @GET(urltop + "sign_ins")
    Observable<BaseResult<SignBean>> sign_ins(@QueryMap Map<String, Object> map);

    /*
             签到：进行签到
                         */
    @POST(urltop + "sign_ins")
    Observable<BaseResult<SignInsBean>> sign(@Body Map<String, Object> map);

    /*
             奖品使用：补签卡
                         */
    @PUT(urltop + "lottery")
    Observable<BaseResult<SignInsBean>> addsign(@Body Map<String, Object> map);

    /*
             新增广告：标题和内容
                         */
    @POST(urltop + "ads")
    Observable<BaseResult<PicCodeBean>> new_title(@Body Map<String, Object> map);

    /*
             更新广告
                         */
    @PUT(urltop + "ads/{aid}")
    Observable<BaseResult<PicCodeBean>> updata_ads(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
             发布广告
                         */
    @POST(urltop + "ads/{aid}/releases")
    Observable<BaseResult<PicCodeBean>> releases(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
             给个人广告 发布时充值或续费：使用零钱
                         */
    @POST(urltop + "ads/{aid}/recharge")
    Observable<BaseResult<PicCodeBean>> recharge(@Path("aid") String aid, @Body PayChangeBean map);

    /*
             广告点赞
               */
    @POST(urltop + "ads/{aid}/likes")
    Observable<BaseResult<PicCodeBean>> likes(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
             广告取消点赞
               */
    @DELETE(urltop + "ads/{aid}/likes")
    Observable<BaseResult<PicCodeBean>> unlikes(@Path("aid") String aid, @QueryMap Map<String, Object> map);

    /*
             广告不喜欢
               */
    @POST(urltop + "ads/{aid}/unlikes")
    Observable<BaseResult<PicCodeBean>> adunlikes(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
             收藏广告
               */
    @POST(urltop + "ads/{aid}/collections")
    Observable<BaseResult<PicCodeBean>> collections(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
             广告取消收藏
               */
    @DELETE(urltop + "ads/{aid}/collections")
    Observable<BaseResult<PicCodeBean>> uncollections(@Path("aid") String aid, @QueryMap Map<String, Object> map);

    /*
             广告的父和子评论的点赞（一级， 二级或三级）
               */
    @POST(urltop + "ads/{aid}/like_comments")
    Observable<BaseResult<PicCodeBean>> like_comments(@Path("aid") String aid, @Body Map<String, Object> map);

    /*
            删除广告的父和子评论的点赞（一级， 二级或三级）
               */
    @DELETE(urltop + "ads/{aid}/like_comments")
    Observable<BaseResult<PicCodeBean>> unlike_comments(@Path("aid") String aid, @QueryMap Map<String, Object> map);

    /*
            删除广告的父和子评论（一级， 二级或三级）
               */
    @DELETE(urltop + "ads/{aid}/comments")
    Observable<BaseResult<PicCodeBean>> delete_comments(@Path("aid") String aid, @QueryMap Map<String, Object> map);

    /*
            对用户投诉或举报
               */
    @POST(urltop + "users/{uid}/complaints")
    Observable<BaseResult<PicCodeBean>> complaints(@Path("uid") String uid, @Body Map<String, Object> map);

    /*
            拉黑广告商
               */
    @POST(urltop + "users/{uid}/black")
    Observable<BaseResult<PicCodeBean>> black(@Path("uid") String uid, @Body Map<String, Object> map);

    /*
            对用户投诉或举报
               */
    @DELETE(urltop + "users/{uid}/black")
    Observable<BaseResult<PicCodeBean>> unblack(@Path("uid") String uid, @QueryMap Map<String, Object> map);

    /*
            获取新手随机红包
               */
    @GET(urltop + "new_red_package")
    Observable<BaseResult<PicCodeBean>> new_red_package();

    /*
           获取分享的信息（标题、简介、图片地址）
               */
    @GET(urltop + "share_infos")
    Observable<BaseResult<ShareBean>> share_infos(@Query("mode") String mode);

    /*
           微信支付-提现-企业付款
               */
    @GET(urltop + "payment/transfer")
    Observable<BaseResult<PicCodeBean>> transfer(@QueryMap Map<String, Object> map);

    /*
           登出
               */
    @POST(urltop + "logoff")
    Observable<BaseResult<PicCodeBean>> logoff();

    /*
           广告详情内直达链接的点击
               */
    @POST(urltop + "ads/{aid}/url_click")
    Observable<BaseResult<PicCodeBean>> url_click(@Path("aid") String aid);

    /*
           广告转发分享
               */
    @POST(urltop + "ads/{aid}/shares")
    Observable<BaseResult<PicCodeBean>> shares(@Path("aid") String aid, @Body Map map);

    /*
           进行首页轮播图的点击
               */
    @POST(urltop + "banners/{id}/click")
    Observable<BaseResult<PicCodeBean>> click(@Path("id") String id, @Body Map map);

    /*
           进行首页轮播图的点击
               */
    @POST(urltop + "start_pages/{id}/click")
    Observable<BaseResult<PicCodeBean>> start_click(@Path("id") String id, @Body Map map);

    /*
           删除搜索记录
               */
    @DELETE(urltop + "records/search")
    Observable<BaseResult<PicCodeBean>> delete_search(@Query("mode") String map);


    //    V2版本
    /*
       获取分享的信息（标题、简介、图片地址）
           */
    @GET(urltop + "v2/ads/categories")
    Observable<BaseResult<BaseBean<CategoriesBean>>> categories();

    /*
        获取广告列表
       */
    @GET(urltop + "v2/ads/{channel}")
    Observable<BaseResult<AdsBean>> ads(@Path("channel") String channel, @QueryMap Map<String, String> map);

    /*
           积分兑换：兑换成零钱
          */
    @GET(urltop + "payment/convert")
    Observable<BaseResult<PicCodeBean>> convert(@Query("points") String points);
}

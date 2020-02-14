package com.hongchuang.ysblibrary.model.model;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.FlowTag;
import com.hongchuang.ysblibrary.common.IYSBApi;
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
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginHandle;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginResultBean;

import java.util.List;
import java.util.Map;

import retrofit.Network;
import retrofit.callback.BaseAction;
import retrofit.callback.BaseResult;
import retrofit.callback.YRequestCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/11/22
 ***/

public class OAuthServiceImp implements OAuthService {
    private static final String TAG = "OAuthService";

    public void callbackReturn(BaseResult result, YRequestCallback callback) {
        if (result.isSuccess()) {
            callback.onSuccess(result.getData());
        } else {
            callback.onFailed(result.code, result.msg);
        }
    }

    @Override
    public void getUserShare(Map<String,Object> map, final YRequestCallback<ShareUserBean> callback) {
        Network.getApi2(IYSBApi.class).getUserShare(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<ShareUserBean>>() {
                    public void call(BaseResult<ShareUserBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getShareSucceed(String id,final YRequestCallback<storeUserBean> callback) {
        Network.getApi2(IYSBApi.class).getShareSucceed(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<storeUserBean>>() {
                    public void call(BaseResult<storeUserBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getSubordinate(Map<String, Object> map,final YRequestCallback<LoveShareGroupBean> callback) {
        Network.getApi2(IYSBApi.class).getSubordinate(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<LoveShareGroupBean>>() {
                    public void call(BaseResult<LoveShareGroupBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }


    @Override
    public void getVideo(Map<String, Object> map,final YRequestCallback<BaseBean<StoreBean>> callback) {
        Network.getApi2(IYSBApi.class).getVideo(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<StoreBean>>>() {
                    public void call(BaseResult<BaseBean<StoreBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void putBindWx(Map<String, Object> map,final YRequestCallback<storeUserBean> callback) {
        Network.getApi2(IYSBApi.class).putBindWx(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<storeUserBean>>() {
                    public void call(BaseResult<storeUserBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getStoreUser(Map<String, Object> map, final YRequestCallback<storeUserBean> callback) {
        Network.getApi2(IYSBApi.class).getStoreUser(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<storeUserBean>>() {
                    public void call(BaseResult<storeUserBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getCapitalFlow(Map<String, Object> map,final YRequestCallback<BaseBean<TakeMoneyRecordBean>> callback) {
        Network.getApi2(IYSBApi.class).getCapitalFlow(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<TakeMoneyRecordBean>>>() {
                    public void call(BaseResult<BaseBean<TakeMoneyRecordBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void postCapitalFlow(Map<String,Object> map,final YRequestCallback<TakeMoneyRecordBean> callback) {
        Network.getApi2(IYSBApi.class).postCapitalFlow(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<TakeMoneyRecordBean>>() {
                    public void call(BaseResult<TakeMoneyRecordBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void putMember(Map<String, Object> map, final YRequestCallback<InviteRecordBean> callback) {
        Network.getApi2(IYSBApi.class).putMember(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<InviteRecordBean>>() {
                    public void call(BaseResult<InviteRecordBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getInviteRecord(Map<String, Object> map, final YRequestCallback<InviteRecordBean> callback) {
        Network.getApi2(IYSBApi.class).getInviteRecord(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<InviteRecordBean>>() {
                    public void call(BaseResult<InviteRecordBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getClassifyList(Map<String, Object> map,final YRequestCallback<BaseBean<GoodsClassifyBean>> callback) {
        Network.getApi2(IYSBApi.class).getClassifyList(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<GoodsClassifyBean>>>() {
                    public void call(BaseResult<BaseBean<GoodsClassifyBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getGiftAll(Map<String, Object> map, final YRequestCallback<BaseBean<StoreGoodsBean>> callback) {
        Network.getApi2(IYSBApi.class).getGiftAll(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<StoreGoodsBean>>>() {
                    public void call(BaseResult<BaseBean<StoreGoodsBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getStoreComment(String goodsId, Map<String, Object> map, final YRequestCallback<BaseBean<StroeCommentBean>> callback) {
        Network.getApi2(IYSBApi.class).getStoreComment(goodsId, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<StroeCommentBean>>>() {
                    public void call(BaseResult<BaseBean<StroeCommentBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void putStoreComment(Map<String, Object> map, final YRequestCallback<StroeCommentBean> callback) {
        Network.getApi2(IYSBApi.class).putStoreComment(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StroeCommentBean>>() {
                    public void call(BaseResult<StroeCommentBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getShopSeek(Map<String, Object> map, final YRequestCallback<BaseBean<ShoppingShopBean>> callback) {
        Network.getApi2(IYSBApi.class).getShopSeek(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<ShoppingShopBean>>>() {
                    public void call(BaseResult<BaseBean<ShoppingShopBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void delOrderRecord(String id, final YRequestCallback<ShoppingShopBean> callback) {
        Network.getApi2(IYSBApi.class).delOrderRecord(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<ShoppingShopBean>>() {
                    public void call(BaseResult<ShoppingShopBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getOrderRecord(Map<String, Object> map, final YRequestCallback<BaseBean<ShoppingShopBean>> callback) {
        Network.getApi2(IYSBApi.class).getOrderRecord(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<ShoppingShopBean>>>() {
                    public void call(BaseResult<BaseBean<ShoppingShopBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getAddress(String id, final YRequestCallback<BaseBean<UserSiteBean>> callback) {
        Network.getApi2(IYSBApi.class).getAddress(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<UserSiteBean>>>() {
                    public void call(BaseResult<BaseBean<UserSiteBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void putAddress(String id, Map<String, Object> map, final YRequestCallback<UserSiteBean> callback) {
        Network.getApi2(IYSBApi.class).putAddress(id, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserSiteBean>>() {
                    public void call(BaseResult<UserSiteBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void delAddress(String id, Map<String, Object> map, final YRequestCallback<UserSiteBean> callback) {
        Network.getApi2(IYSBApi.class).delAddress(id, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserSiteBean>>() {
                    public void call(BaseResult<UserSiteBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getShopList(final YRequestCallback<BaseBean<StoreBean>> callback) {
        Network.getApi2(IYSBApi.class).getShopList().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<StoreBean>>>() {
                    public void call(BaseResult<BaseBean<StoreBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getShopOne(Map<String, Object> map, final YRequestCallback<StoreGoodsBean> callback) {
        Network.getApi2(IYSBApi.class).getShopOne(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StoreGoodsBean>>() {
                    public void call(BaseResult<StoreGoodsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getShopGoods(Map<String, Object> map, final YRequestCallback<BaseBean<StoreGoodsBean>> callback) {
        Network.getApi2(IYSBApi.class).getShopGoods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<StoreGoodsBean>>>() {
                    public void call(BaseResult<BaseBean<StoreGoodsBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void putCarGoods(Map<String, Object> map, final YRequestCallback<WechatPayBean> callback) {
        Network.getApi2(IYSBApi.class).putCarGoods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<WechatPayBean>>() {
                    public void call(BaseResult<WechatPayBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void addCarGoods(String goods_id, Map<String, Object> map, final YRequestCallback<StoreGoodsBean> callback) {
        Network.getApi2(IYSBApi.class).addCarGoods(goods_id, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StoreGoodsBean>>() {
                    public void call(BaseResult<StoreGoodsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void deleteCarGoods(Map<String, Object> map, final YRequestCallback<StoreGoodsBean> callback) {
        Network.getApi2(IYSBApi.class).deleteCarGoods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StoreGoodsBean>>() {
                    public void call(BaseResult<StoreGoodsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void updateCarGoods(String goods_id, Map<String, Object> map, final YRequestCallback<StoreGoodsBean> callback) {
        Network.getApi2(IYSBApi.class).updateCarGoods(goods_id, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StoreGoodsBean>>() {
                    public void call(BaseResult<StoreGoodsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }


    @Override
    public void getCarGoods(Map<String, Object> map, final YRequestCallback<BaseBean<ShoppingShopBean>> callback) {
        Network.getApi2(IYSBApi.class).getCarGoods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<ShoppingShopBean>>>() {
                    public void call(BaseResult<BaseBean<ShoppingShopBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getGoodsDetails(String id, final YRequestCallback<ShoppingShopBean> callback) {
        Network.getApi2(IYSBApi.class).getGoodsDetails(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<ShoppingShopBean>>() {
                    public void call(BaseResult<ShoppingShopBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getRanking(Map<String, Object> map, final YRequestCallback<UserRankingBean> callback) {
        Network.getApi(IYSBApi.class).getRanking(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserRankingBean>>() {
                    public void call(BaseResult<UserRankingBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getStoreSeekHot(final YRequestCallback<BaseBean<FlowTag>> callback) {
        Network.getApi2(IYSBApi.class).getStoreSeekHot().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<FlowTag>>>() {
                    public void call(BaseResult<BaseBean<FlowTag>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getStoreCarousel(final YRequestCallback<StoreBean> callback) {

        Network.getApi2(IYSBApi.class).getStoreCarousel().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StoreBean>>() {
                    public void call(BaseResult<StoreBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getStoreVideo(final YRequestCallback<BaseBean<StoreBean>> callback) {
        Network.getApi2(IYSBApi.class).getStoreVideo().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<StoreBean>>>() {
                    public void call(BaseResult<BaseBean<StoreBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getStoreRush(Map<String, Object> map, final YRequestCallback<StoreRushBean> callback) {
        Network.getApi2(IYSBApi.class).getStoreRush(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StoreRushBean>>() {
                    public void call(BaseResult<StoreRushBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getGameStatue(final YRequestCallback<ShenmiBean> callback) {
        Network.getApi(IYSBApi.class).getGameStatue().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<ShenmiBean>>() {
                    public void call(BaseResult<ShenmiBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void card_verify(Map<String, Object> map, final YRequestCallback<CommodityCardBean> callback) {
        Network.getApi2(IYSBApi.class).card_verify(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<CommodityCardBean>>() {
                    public void call(BaseResult<CommodityCardBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void set_card_data(Map<String, Object> map, final YRequestCallback<WechatPayBean> callback) {
        Network.getApi2(IYSBApi.class).set_card_data(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<WechatPayBean>>() {
                    public void call(BaseResult<WechatPayBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void set_goods_data(Map<String, Object> map, final YRequestCallback<GoodsBean> callback) {
        Network.getApi(IYSBApi.class).set_goods_data(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<GoodsBean>>() {
                    public void call(BaseResult<GoodsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void set_transmit_data(final YRequestCallback<TaskBean> callback) {
        Network.getApi(IYSBApi.class).set_transmit_data().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<TaskBean>>() {
                    public void call(BaseResult<TaskBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_task_award(String username, Map<String, Object> map, final YRequestCallback<TaskBean> callback) {
        Network.getApi(IYSBApi.class).get_task_award(username, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<TaskBean>>() {
                    public void call(BaseResult<TaskBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_task_detail(String username, final YRequestCallback<TaskBean> callback) {
        Network.getApi(IYSBApi.class).get_task_detail(username).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<TaskBean>>() {
                    public void call(BaseResult<TaskBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void join_vote(Map<String, Object> map, final YRequestCallback<VoteBean> callback) {
        Network.getApi(IYSBApi.class).join_vote(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<VoteBean>>() {
                    public void call(BaseResult<VoteBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_vote_record(String username, Map<String, Object> map, final YRequestCallback<BaseBean<VoteBean>> callback) {
        Network.getApi(IYSBApi.class).get_vote_record(username, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<VoteBean>>>() {
                    public void call(BaseResult<BaseBean<VoteBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_vote_list(String uid, Map<String, Object> map, final YRequestCallback<BaseBean<VoteBean>> callback) {
        Network.getApi(IYSBApi.class).get_vote_list(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<VoteBean>>>() {
                    public void call(BaseResult<BaseBean<VoteBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_alliance_ad(Map<String, Object> map, final YRequestCallback<BaseBean<AdAllianceBean>> callback) {
        Network.getApi(IYSBApi.class).get_alliance_ad(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<AdAllianceBean>>>() {
                    public void call(BaseResult<BaseBean<AdAllianceBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void submit_invitation_code(Map<String, Object> map, final YRequestCallback<ShenmiBean> callback) {
        Network.getApi(IYSBApi.class).submit_invitation_code(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<ShenmiBean>>() {
                    public void call(BaseResult<ShenmiBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_vote(final YRequestCallback<VoteBean> callback) {
        Network.getApi(IYSBApi.class).get_vote().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<VoteBean>>() {
                    public void call(BaseResult<VoteBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void points_add(Map<String, Object> map, final YRequestCallback<PointDetailBean> callback) {
        Network.getApi(IYSBApi.class).points_add(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PointDetailBean>>() {
                    public void call(BaseResult<PointDetailBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void rush_goods(Map<String, Object> map, final YRequestCallback<BaseBean<PointDetailBean>> callback) {
        Network.getApi(IYSBApi.class).rush_goods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<PointDetailBean>>>() {
                    public void call(BaseResult<BaseBean<PointDetailBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void exchange_goods(Map<String, Object> map, final YRequestCallback<BaseBean<PointDetailBean>> callback) {
        Network.getApi(IYSBApi.class).exchange_goods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<PointDetailBean>>>() {
                    public void call(BaseResult<BaseBean<PointDetailBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void put_site_data(Map<String, String> map, final YRequestCallback<GoodsBean> callback) {
        Network.getApi(IYSBApi.class).put_site_data(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<GoodsBean>>() {
                    public void call(BaseResult<GoodsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_exchange_data(Map<String, String> map, final YRequestCallback<BaseBean<GoodsBean>> callback) {
        Network.getApi(IYSBApi.class).get_exchange_data(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<GoodsBean>>>() {
                    public void call(BaseResult<BaseBean<GoodsBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_gold_sum(Map<String, String> map, final YRequestCallback<VipRechangeBean> callback) {
        Network.getApi(IYSBApi.class).get_gold_sum(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<VipRechangeBean>>() {
                    public void call(BaseResult<VipRechangeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void open_red_package(Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).open_red_package(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void free_vip(Map<String, Object> map, final YRequestCallback<VIpStateBean> callback) {
        Network.getApi(IYSBApi.class).free_vip(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<VIpStateBean>>() {
                    public void call(BaseResult<VIpStateBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_red_list(Map<String, String> map, final YRequestCallback<BaseBean<VipRechangeBean>> callback) {
        Network.getApi(IYSBApi.class).get_red_list(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<VipRechangeBean>>>() {
                    public void call(BaseResult<BaseBean<VipRechangeBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_vip_price(final YRequestCallback<BaseBean<VipPriceBean>> callback) {
        Network.getApi(IYSBApi.class).get_vip_price().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<VipPriceBean>>>() {
                    public void call(BaseResult<BaseBean<VipPriceBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getad(Map bean, final YRequestCallback<ShenmiBean> callback) {
        Network.getApi(IYSBApi.class).getad(bean).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<ShenmiBean>() {
                    public void call(ShenmiBean o) {
                        if (o.getCode() == 0) {
                            callback.onSuccess(o);
                        } else {
                            callback.onFailed(o.getCode() + "", o.getMsg());
                        }
                    }
                }, new BaseAction<Throwable>(callback));
    }


    @Override
    public void delete_draft(String aid, Map map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).delete_draft(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void pic_code(Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).pic_code(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void basket(Map<String, String> map, final YRequestCallback<BasketBean> callback) {
        Network.getApi(IYSBApi.class).basket(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BasketBean>>() {
                    public void call(BaseResult<BasketBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void pic_code_comparison(Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).pic_code_comparison(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void cellphone_code(Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).cellphone_code(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void cellphone_code_comparison(Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).cellphone_code_comparison(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void signup(Map<String, String> map, final YRequestCallback<UserBean> callback) {
        Network.getApi(IYSBApi.class).signup(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserBean>>() {
                    public void call(BaseResult<UserBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void login(Map<String, String> map, final YRequestCallback<UserBean> callback) {
        Network.getApi(IYSBApi.class).login(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserBean>>() {
                    public void call(BaseResult<UserBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void forgetpassword(Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).forgetpassword(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void tencentAuthorized(Activity activity, QQLoginHandle handle, final YRequestCallback<QQLoginResultBean> callback) {
//        LogManager.getInstance().w(TAG, "调起QQ登录");
//        LogUtils.w("调起QQ登录");
//        handle.setOnResultProcess(new QQLoginResult() {
//            @Override
//            public void onResult(int requestCode, int resultCode, Intent data) {
//                if (requestCode == Constants.REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
//                    YSBLibrary.getLibrary().getTencent().handleLoginData(data, new BaseUiListener(callback));
//                }
//            }
//        });
//        YSBLibrary.getLibrary().getTencent().login(activity, "all", null);
    }

    @Override
    public void wechatAuthorized(Activity activity, TencentCallback callback) {
//        LogManager.getInstance().w(TAG, "调起微信登录");
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = new Random().nextLong() + "aa" + System.currentTimeMillis();
//        boolean success = YSBLibrary.getLibrary().getWeChat().sendReq(req);
//        if (!success) {
//            LogManager.getInstance().w(TAG, "微信登录失败，打开微信失败");
//            if (callback != null) {
//                callback.onError(new UiError(-99, "登录失败", "无法打开微信"));
//            }
//        }
    }

    @Override
    public void wechat(Map<String, String> map, final YRequestCallback<UserBean> callback) {
        Network.getApi(IYSBApi.class).wechat(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserBean>>() {
                    public void call(BaseResult<UserBean> o) {
                        if (o.getCode().equals("200")) {
                            callback.onSuccess(o.getData());
                        } else if (o.getCode().equals("300")) {
                            callback.onFailed(o.getCode(), o.getData().getToken());
                        } else {
                            callback.onFailed(o.getCode(), o.getMsg());
                        }
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void qqbindphone(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).qqbindphone(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void qq(Map<String, String> map, final YRequestCallback<UserBean> callback) {
        Network.getApi(IYSBApi.class).qq(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserBean>>() {
                    public void call(BaseResult<UserBean> o) {
                        if (o.getCode().equals("200")) {
                            callback.onSuccess(o.getData());
                        } else if (o.getCode().equals("300")) {
                            callback.onFailed(o.getCode(), o.getData().getToken());
                        } else {
                            callback.onFailed(o.getCode(), o.getMsg());
                        }
//                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void bindphone(String uid, Map<String, String> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).bindphone(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void categories(final YRequestCallback<BaseBean<CategoriesBean>> callback) {
        Network.getApi(IYSBApi.class).categories().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<CategoriesBean>>>() {
                    public void call(BaseResult<BaseBean<CategoriesBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void banners(Map<String, String> map, final YRequestCallback<BannerBean> callback) {
        Network.getApi(IYSBApi.class).banners(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BannerBean>>() {
                    public void call(BaseResult<BannerBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void father_comments(String uid, Map<String, Object> map, final YRequestCallback<BaseBean<FatherCommentBean>> callback) {
        Network.getApi(IYSBApi.class).father_comments(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<FatherCommentBean>>>() {
                    public void call(BaseResult<BaseBean<FatherCommentBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void child_comment(String uid, Map<String, Object> map, final YRequestCallback<BaseBean<FatherCommentBean.Comments>> callback) {
        Network.getApi(IYSBApi.class).child_comment(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<FatherCommentBean.Comments>>>() {
                    public void call(BaseResult<BaseBean<FatherCommentBean.Comments>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ads(Map<String, String> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).ads(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ads(String channel, Map<String, String> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).ads(channel, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }


    @Override
    public void tantan_ads(Map<String, String> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).tantan_ads(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void recommend_ads(Map<String, Object> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).recommend_ads(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void answers(String aid, Map<String, Object> map, final YRequestCallback<AnswersBean> callback) {
        Network.getApi(IYSBApi.class).answers(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AnswersBean>>() {
                    public void call(BaseResult<AnswersBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void answer_status(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).answer_status(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ads_detail(String aid, Map<String, Object> map, final YRequestCallback<BaseBean<AdBean>> callback) {
        Network.getApi(IYSBApi.class).ads_detail(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<AdBean>>>() {
                    public void call(BaseResult<BaseBean<AdBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void shield(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).shield(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void start_pages(Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).start_pages(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void change_info(String uid, ChangeUserInfoBean map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).change_info(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void change_password(String uid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).change_password(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void prefect_info(String uid, PerfecterBean map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).prefect_info(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void delete_wechat(String uid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).delete_wechat(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void stst(Map<String, Object> map, final YRequestCallback<StsBean> callback) {
        Network.getApi(IYSBApi.class).stst(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<StsBean>>() {
                    public void call(BaseResult<StsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void user_black(Map<String, Object> map, final YRequestCallback<BlackListBean> callback) {
        Network.getApi(IYSBApi.class).user_black(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BlackListBean>>() {
                    public void call(BaseResult<BlackListBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_message(String uid, Map<String, Object> map, final YRequestCallback<MessageBean> callback) {
        Network.getApi(IYSBApi.class).get_message(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<MessageBean>>() {
                    public void call(BaseResult<MessageBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void point(Map<String, Object> map, final YRequestCallback<PointBean> callback) {
        Network.getApi(IYSBApi.class).point(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PointBean>>() {
                    public void call(BaseResult<PointBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void change(Map<String, Object> map, final YRequestCallback<PointBean> callback) {
        Network.getApi(IYSBApi.class).change(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PointBean>>() {
                    public void call(BaseResult<PointBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void delete_messages(Map<String, Object> map, List<String> ids, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).delete_messages(map, ids).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void black(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).black(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void unblack(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).unblack(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void circular(Map<String, Object> map, final YRequestCallback<SystemMessageBean> callback) {
        Network.getApi(IYSBApi.class).circular(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<SystemMessageBean>>() {
                    public void call(BaseResult<SystemMessageBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ad_comment(Map<String, Object> map, final YRequestCallback<AdCommentBean> callback) {
        Network.getApi(IYSBApi.class).ad_comment(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdCommentBean>>() {
                    public void call(BaseResult<AdCommentBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void comment_ad(Map<String, Object> map, final YRequestCallback<AdCommentBean> callback) {
        Network.getApi(IYSBApi.class).comment_ad(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdCommentBean>>() {
                    public void call(BaseResult<AdCommentBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void child_comments(String uid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).child_comments(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void father_comments_put(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).father_comments_put(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void user_like(Map<String, Object> map, final YRequestCallback<FollowBean> callback) {
        Network.getApi(IYSBApi.class).user_like(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<FollowBean>>() {
                    public void call(BaseResult<FollowBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ad_like(Map<String, Object> map, final YRequestCallback<AdLikeBean> callback) {
        Network.getApi(IYSBApi.class).ad_like(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdLikeBean>>() {
                    public void call(BaseResult<AdLikeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void promotion(String uid, CertificationBean map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).promotion(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getpromotion(String uid, Map<String, Object> map, final YRequestCallback<PromotionBean> callback) {
        Network.getApi(IYSBApi.class).getpromotion(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PromotionBean>>() {
                    public void call(BaseResult<PromotionBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getuserinfo(String uid, Map<String, Object> map, final YRequestCallback<UserBean.UserDetailBean> callback) {
        Network.getApi(IYSBApi.class).getuserinfo(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserBean.UserDetailBean>>() {
                    public void call(BaseResult<UserBean.UserDetailBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void goods(Map<String, Object> map, final YRequestCallback<BaseBean<GoodsBean>> callback) {
        Network.getApi(IYSBApi.class).goods(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<GoodsBean>>>() {
                    public void call(BaseResult<BaseBean<GoodsBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void exchange(Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).exchange(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void point_detail(Map<String, Object> map, final YRequestCallback<BaseBean<PointDetailBean>> callback) {
        Network.getApi(IYSBApi.class).point_detail(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<PointDetailBean>>>() {
                    public void call(BaseResult<BaseBean<PointDetailBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void change_detail(Map<String, Object> map, final YRequestCallback<BaseBean<PointDetailBean>> callback) {
        Network.getApi(IYSBApi.class).change_detail(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<PointDetailBean>>>() {
                    public void call(BaseResult<BaseBean<PointDetailBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void lotteries(String uid, Map<String, Object> map, final YRequestCallback<BaseBean<BoxBean>> callback) {
        Network.getApi(IYSBApi.class).lotteries(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<BoxBean>>>() {
                    public void call(BaseResult<BaseBean<BoxBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void payment(Map<String, Object> map, final YRequestCallback<BaseBean<PaymentBean>> callback) {
        Network.getApi(IYSBApi.class).payment(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<PaymentBean>>>() {
                    public void call(BaseResult<BaseBean<PaymentBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void daily_follow_number(Map<String, Object> map, final YRequestCallback<BaseBean<FollowNumberBean>> callback) {
        Network.getApi(IYSBApi.class).daily_follow_number(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<FollowNumberBean>>>() {
                    public void call(BaseResult<BaseBean<FollowNumberBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void today_follow_number(String uid, Map<String, Object> map, final YRequestCallback<TodayFollowBean> callback) {
        Network.getApi(IYSBApi.class).today_follow_number(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<TodayFollowBean>>() {
                    public void call(BaseResult<TodayFollowBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void yesterday_record(String uid, Map<String, Object> map, final YRequestCallback<YesterdayBean> callback) {
        Network.getApi(IYSBApi.class).yesterday_record(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<YesterdayBean>>() {
                    public void call(BaseResult<YesterdayBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ad_release(Map<String, Object> map, final YRequestCallback<AdReleaseBean> callback) {
        Network.getApi(IYSBApi.class).ad_release(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdReleaseBean>>() {
                    public void call(BaseResult<AdReleaseBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_draft(Map<String, Object> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).get_draft(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ad_look(Map<String, Object> map, final YRequestCallback<AdReleaseBean> callback) {
        Network.getApi(IYSBApi.class).ad_look(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdReleaseBean>>() {
                    public void call(BaseResult<AdReleaseBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void ad_collection(Map<String, Object> map, final YRequestCallback<AdReleaseBean> callback) {
        Network.getApi(IYSBApi.class).ad_collection(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdReleaseBean>>() {
                    public void call(BaseResult<AdReleaseBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void get_put(Map<String, Object> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).get_put(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void user_follow(Map<String, Object> map, final YRequestCallback<BaseBean<FollowUserBean>> callback) {
        Network.getApi(IYSBApi.class).user_follow(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<FollowUserBean>>>() {
                    public void call(BaseResult<BaseBean<FollowUserBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void sign(Map<String, Object> map, final YRequestCallback<SignInsBean> callback) {
        Network.getApi(IYSBApi.class).sign(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<SignInsBean>>() {
                    public void call(BaseResult<SignInsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void addsign(Map<String, Object> map, final YRequestCallback<SignInsBean> callback) {
        Network.getApi(IYSBApi.class).addsign(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<SignInsBean>>() {
                    public void call(BaseResult<SignInsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void new_title(Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).new_title(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));

    }

    @Override
    public void open_box(Map<String, Object> map, final YRequestCallback<BaseBean<LotteryListBean>> callback) {
        Network.getApi(IYSBApi.class).open_box(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<LotteryListBean>>>() {
                    public void call(BaseResult<BaseBean<LotteryListBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void sign_ins(Map<String, Object> map, final YRequestCallback<SignBean> callback) {
        Network.getApi(IYSBApi.class).sign_ins(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<SignBean>>() {
                    public void call(BaseResult<SignBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void lotterylist(Map<String, Object> map, final YRequestCallback<BaseBean<LotteryListBean>> callback) {
        Network.getApi(IYSBApi.class).lotterylist(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<LotteryListBean>>>() {
                    public void call(BaseResult<BaseBean<LotteryListBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void invitation_record(Map<String, Object> map, final YRequestCallback<BaseBean<InvitationRecordBean>> callback) {
        Network.getApi(IYSBApi.class).invitation_record(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<InvitationRecordBean>>>() {
                    public void call(BaseResult<BaseBean<InvitationRecordBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void invitation(Map<String, Object> map, final YRequestCallback<BaseBean<InvitationBean>> callback) {
        Network.getApi(IYSBApi.class).invitation(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<InvitationBean>>>() {
                    public void call(BaseResult<BaseBean<InvitationBean>> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void invitation_info(Map<String, Object> map, final YRequestCallback<InvitationInfoBean> callback) {
        Network.getApi(IYSBApi.class).invitation_info(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<InvitationInfoBean>>() {
                    public void call(BaseResult<InvitationInfoBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void recom_words(Map<String, Object> map, final YRequestCallback<RecomWordsBean> callback) {
        Network.getApi(IYSBApi.class).recom_words(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<RecomWordsBean>>() {
                    public void call(BaseResult<RecomWordsBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void search(Map<String, Object> map, final YRequestCallback<BaseBean<SearchBean>> callback) {
        Network.getApi(IYSBApi.class).search(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<SearchBean>>>() {
                    public void call(BaseResult<BaseBean<SearchBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void getsearch(Map<String, Object> map, final YRequestCallback<SearchedBean> callback) {
        Network.getApi(IYSBApi.class).getsearch(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<SearchedBean>>() {
                    public void call(BaseResult<SearchedBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void follow(String uid, Map<String, Object> map, final YRequestCallback<UserDetailBean> callback) {
        Network.getApi(IYSBApi.class).follow(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<UserDetailBean>>() {
                    public void call(BaseResult<UserDetailBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void otherpulish(String uid, Map<String, Object> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).otherpulish(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void otheranswer(String uid, Map<String, Object> map, final YRequestCallback<AdsBean> callback) {
        Network.getApi(IYSBApi.class).otheranswer(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<AdsBean>>() {
                    public void call(BaseResult<AdsBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void follow_details(String uid, Map<String, Object> map, final YRequestCallback<FollowDetailBean> callback) {
        Network.getApi(IYSBApi.class).follow_details(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<FollowDetailBean>>() {
                    public void call(BaseResult<FollowDetailBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void other_user_like(Map<String, Object> map, final YRequestCallback<FollowDetailBean> callback) {
        Network.getApi(IYSBApi.class).other_user_like(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<FollowDetailBean>>() {
                    public void call(BaseResult<FollowDetailBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void other_fans(Map<String, Object> map, final YRequestCallback<FollowDetailBean> callback) {
        Network.getApi(IYSBApi.class).other_fans(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<FollowDetailBean>>() {
                    public void call(BaseResult<FollowDetailBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void preorder(Map<String, Object> map, final YRequestCallback<WechatPayBean> callback) {
        Network.getApi(IYSBApi.class).preorder(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<WechatPayBean>>() {
                    public void call(BaseResult<WechatPayBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void lottery(Map<String, Object> map, final YRequestCallback<BaseBean<LotteryListBean>> callback) {
        Network.getApi(IYSBApi.class).lottery(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BaseBean<LotteryListBean>>>() {
                    public void call(BaseResult<BaseBean<LotteryListBean>> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void box_list(Map<String, Object> map, final YRequestCallback<BoxlistBean> callback) {
        Network.getApi(IYSBApi.class).box_list(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<BoxlistBean>>() {
                    public void call(BaseResult<BoxlistBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void fans(String uid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).fans(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void unfans(String uid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).unfans(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void feedbacks(Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).feedbacks(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }

                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void upload_oss(Context context, String ObjectKey, String filePath, final OssCallback callback) {
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_AccessKeyId), ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_AccessKeySecret), ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_SecurityToken));

        OSS oss = new OSSClient(context, endpoint, credentialProvider, null);
//        imgurl="dev/img_"+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+System.currentTimeMillis()+".jpg";
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("prod-shijinsz", ObjectKey, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (callback != null)
                    callback.onSuccess(request, result);
//                Log.d("PutObject", "UploadSuccess");
//                imgurl = "static.shijinsz.net"+imgurl;
//                Log.e(TAG, "onSuccess: "+imgurl );

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                if (callback != null) {
                    callback.onFailure(request, clientExcepion, serviceException);
                }
            }
        });
    }

    @Override
    public void updata_ads(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).updata_ads(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void releases(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).releases(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void recharge(String aid, PayChangeBean map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).recharge(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void likes(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).likes(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void unlikes(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).unlikes(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void collections(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).collections(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void uncollections(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).uncollections(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void like_comments(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).like_comments(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void unlike_comments(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).unlike_comments(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void delete_comments(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).delete_comments(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void complaints(String uid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).complaints(uid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void new_red_package(final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).new_red_package().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void adunlikes(String aid, Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).adunlikes(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void share_infos(String mode, final YRequestCallback<ShareBean> callback) {
        Network.getApi(IYSBApi.class).share_infos(mode).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<ShareBean>>() {
                    public void call(BaseResult<ShareBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void transfer(Map<String, Object> map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).transfer(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void logoff(final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).logoff().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void url_click(String aid, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).url_click(aid).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void shares(String aid, Map map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).shares(aid, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void click(String id, Map map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).click(id, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void start_click(String id, Map map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).start_click(id, map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void delete_search(String map, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).delete_search(map).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }

    @Override
    public void convert(String points, final YRequestCallback<PicCodeBean> callback) {
        Network.getApi(IYSBApi.class).convert(points).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BaseResult<PicCodeBean>>() {
                    public void call(BaseResult<PicCodeBean> o) {
                        callbackReturn(o, callback);
                    }
                }, new BaseAction<Throwable>(callback));
    }
}

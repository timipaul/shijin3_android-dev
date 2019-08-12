package com.shijinsz.shijin.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.mine.ReportActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/3.
 */

public class ShareDialog {
    public Context mContext;
    public LayoutInflater inflater;

    public ShareDialog(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }

    /**
     * 提现弹框
     * type:1-广告详情，2-个人详情，3-邀请好友，4-邀请码分享
     */

    private static final String TAG = "ShareDialog";
    private PopupWindow window;
    private View popupView;
    private TextView wechat;
    private TextView wechatFriend;
    private TextView qq;
    private TextView qqzone;
    private TextView weibo;
    private TextView copyLink;
    private TextView cancel;
    private TextView black;
    private Platform.ShareParams sp;
    private StateView mStateView;
    public interface OnShareListen{
        void success(String channel);
    }
    public OnShareListen onShareListen;
    public void setOnShareListen(OnShareListen onShareListen){
        this.onShareListen=onShareListen;
    }
    public void showWithdrapDialog(Activity mActivity,int type,String title,String content,String imgurl,String url) {
        View viewGroup = ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        mStateView = StateView.inject(viewGroup);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.page_detail_error);
        }
        if (window == null) {
            popupView = inflater.inflate(R.layout.choice_invite_pop, null);
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            wechat = popupView.findViewById(R.id.wechat);
            wechatFriend = popupView.findViewById(R.id.wechat_friend);
            qq = popupView.findViewById(R.id.qq);
            qqzone = popupView.findViewById(R.id.qqzone);
            weibo = popupView.findViewById(R.id.weibo);
            copyLink = popupView.findViewById(R.id.copy_link);
            cancel = popupView.findViewById(R.id.cancel);
            black=popupView.findViewById(R.id.black);
        }
        sp= new Platform.ShareParams();
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开微信...");
//                mStateView.showLoading();
                switch (type){
                    case 1:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=wx"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx");
                        sp.setUrl(url+"&channel=wx");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=wx"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx");
                        sp.setUrl(url+"&channel=wx");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.VISIBLE);
                        copyLink.setText(mContext.getString(R.string.report));
                        copyLink.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(R.mipmap.weigui),null,null);
                        break;
                    case 3:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=wx"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx");
                        sp.setUrl(url+"&channel=wx");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
//                sp.setTitle(title);
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText(content);
//                sp.setImageUrl(imgurl);
                        sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx");
//                sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_IMAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        sp.setTitle(title);
                        sp.setTitleUrl(url); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url);
                        sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                }
                Platform wechat = ShareSDK.getPlatform (Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wechat.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        mStateView.showContent();
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息

                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {

                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                        if (type==1){
                            if (onShareListen!=null){
                                onShareListen.success("wx");
                            }
                        }

                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();

                    }
                });
// 执行图文分享
                wechat.share(sp);
                dismissWithdrapDialog();
            }
        });
        qqzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开QQ...");

//                mStateView.showLoading();
                switch (type){
                    case 1:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=qq_zone"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq_zone");
                        sp.setUrl(url+"&channel=qq_zone");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=qq_zone"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq_zone");
                        sp.setUrl(url+"&channel=qq_zone");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.VISIBLE);
                        copyLink.setText(mContext.getString(R.string.report));
                        copyLink.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(R.mipmap.weigui),null,null);
                        break;
                    case 3:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=qq_zone"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq_zone");
                        sp.setUrl(url+"&channel=qq_zone");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
//                sp.setTitle(title);
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText(content);
//                sp.setImageUrl(imgurl);
                        sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq_zone");
//                sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_IMAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        sp.setTitle(title);
                        sp.setTitleUrl(url); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url);
                        sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                }
                Platform qzone = ShareSDK.getPlatform (QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        //分享成功的回调
                        shareSucceedData();
                        mStateView.showContent();
                        if (type==1){
                            if (onShareListen!=null){
                                onShareListen.success("qq_zone");
                            }
                        }
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        mStateView.showContent();
                        //取消分享的回调
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开QQ...");

//                mStateView.showLoading();
                switch (type){
                    case 1:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=qq"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq");
                        sp.setUrl(url+"&channel=qq");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=qq"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq");
                        sp.setUrl(url+"&channel=qq");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.VISIBLE);
                        copyLink.setText(mContext.getString(R.string.report));
                        copyLink.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(R.mipmap.weigui),null,null);
                        break;
                    case 3:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=qq"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq");
                        sp.setUrl(url+"&channel=qq");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
//                sp.setTitle(title);
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText(content);
//                sp.setImageUrl(imgurl);
                        sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=qq");
//                sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_IMAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        sp.setTitle(title);
                        sp.setTitleUrl(url); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url);
                        sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                }
                Platform qzone = ShareSDK.getPlatform (QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                        if (type==1){
                            if (onShareListen!=null){
                                onShareListen.success("qq");
                            }
                        }
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        wechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开微信...");

//                mStateView.showLoading();
                switch (type){
                    case 1:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=wx_circle"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx_circle");
                        sp.setUrl(url+"&channel=wx_circle");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=wx_circle"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx_circle");
                        sp.setUrl(url+"&channel=wx_circle");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.VISIBLE);
                        copyLink.setText(mContext.getString(R.string.report));
                        copyLink.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(R.mipmap.weigui),null,null);
                        break;
                    case 3:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=wx_circle"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx_circle");
                        sp.setUrl(url+"&channel=wx_circle");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
//                sp.setTitle(title);
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText(content);
//                sp.setImageUrl(imgurl);
                        sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=wx_circle");
//                sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_IMAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        sp.setTitle(title);
                        sp.setTitleUrl(url); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url);
                        sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                }
                Platform qzone = ShareSDK.getPlatform (WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                        if (type==1){
                            if (onShareListen!=null){
                                onShareListen.success("wx_circle");
                            }
                        }
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开微博...");

//                mStateView.showLoading();
                Platform qzone = ShareSDK.getPlatform (SinaWeibo.NAME);
                switch (type){
                    case 1:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=weibo"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=weibo");
                        sp.setUrl(url+"&channel=weibo");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=weibo"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=weibo");
                        sp.setUrl(url+"&channel=weibo");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.VISIBLE);
                        copyLink.setText(mContext.getString(R.string.report));
                        copyLink.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(R.mipmap.weigui),null,null);
                        break;
                    case 3:
                        sp.setTitle(title);
                        sp.setTitleUrl(url+"&channel=weibo"); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=weibo");
                        sp.setUrl(url+"&channel=weibo");
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
//                sp.setTitle(title);
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText(content);
//                sp.setImageUrl(imgurl);
                        sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url+"&channel=weibo");
//                sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_IMAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        sp.setTitle(title);
                        sp.setTitleUrl(url); // 标题的超链接
                        sp.setText(content);
                        sp.setImageUrl(imgurl);
//                sp.setImagePath(imgurl);
                        sp.setSite(mContext.getString(R.string.app_name));
                        sp.setSiteUrl(url);
                        sp.setUrl(url);
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        black.setVisibility(View.INVISIBLE);
                        break;
                }
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                        if (type==1){
                            if (onShareListen!=null){
                                onShareListen.success("weibo");
                            }
                        }
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type!=2){
                    ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    if (type!=5) {
                        cm.setText(url + "&channel=copy");
                    }else {
                        cm.setText(url);
                    }
                    ToastUtil.showToast("复制成功，可以发给朋友们了");
                    window.dismiss();
                }else {
                    mActivity.startActivity(new Intent(mActivity, ReportActivity.class));
                    window.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }
    public void showWithdrapDialog(Activity mActivity, String isBlack, String title, String content, String imgurl, String url, View.OnClickListener listener,String id) {
        View viewGroup = ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        mStateView = StateView.inject(viewGroup);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.page_detail_error);
        }
        if (window == null) {
            popupView = inflater.inflate(R.layout.choice_invite_pop, null);
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            wechat = popupView.findViewById(R.id.wechat);
            wechatFriend = popupView.findViewById(R.id.wechat_friend);
            qq = popupView.findViewById(R.id.qq);
            qqzone = popupView.findViewById(R.id.qqzone);
            weibo = popupView.findViewById(R.id.weibo);
            copyLink = popupView.findViewById(R.id.copy_link);
            cancel = popupView.findViewById(R.id.cancel);
            black=popupView.findViewById(R.id.black);
        }
        sp= new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url); // 标题的超链接
        sp.setText(content);
        sp.setImageUrl(imgurl);
//        sp.setImagePath(imgurl);
        sp.setSite(mContext.getString(R.string.app_name));
        sp.setSiteUrl(url);
        sp.setUrl(url);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        black.setVisibility(View.VISIBLE);
        copyLink.setText(mContext.getString(R.string.report));
        copyLink.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(R.mipmap.weigui),null,null);
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开微信...");

//                mStateView.showLoading();
                Platform wechat = ShareSDK.getPlatform (Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wechat.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        mStateView.showContent();
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                wechat.share(sp);
                dismissWithdrapDialog();
            }
        });
        qqzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开QQ...");

//                mStateView.showLoading();
//                Platform.ShareParams sp = new Platform.ShareParams();
//                sp.setTitle("测试分享的标题");
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText("测试分享的文本");
//                sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
//                sp.setSite("发布分享的网站名称");
//                sp.setSiteUrl("发布分享网站的地址");
                Platform qzone = ShareSDK.getPlatform (QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        mStateView.showContent();
                        //取消分享的回调
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开QQ...");

//                mStateView.showLoading();
//                Platform.ShareParams sp = new Platform.ShareParams();
//                sp.setTitle("测试分享的标题");
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText("测试分享的文本");
//                sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
//                sp.setSite("发布分享的网站名称");
//                sp.setSiteUrl("发布分享网站的地址");
                Platform qzone = ShareSDK.getPlatform (QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        wechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开微信...");

//                mStateView.showLoading();
//                Platform.ShareParams sp = new Platform.ShareParams();
//                sp.setTitle("测试分享的标题");
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setText("测试分享的文本");
//                sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
//                sp.setSite("发布分享的网站名称");
//                sp.setSiteUrl("发布分享网站的地址");
//                sp.setShareType(Platform.SHARE_WEBPAGE);
                Platform qzone = ShareSDK.getPlatform (WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast("正在打开微博...");

//                mStateView.showLoading();
                Platform qzone = ShareSDK.getPlatform (SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener (new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Log.e(TAG, "onError: "+arg0.getDb().exportData()+"  **arg1=="+arg1+"  **arg2==" );
                        arg2.printStackTrace();
                        mStateView.showContent();
                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                    }
                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                        shareSucceedData();
                        //分享成功的回调
                        mStateView.showContent();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //取消分享的回调
                        mStateView.showContent();
                    }
                });
// 执行图文分享
                qzone.share(sp);
                dismissWithdrapDialog();
            }
        });
        copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mActivity,ReportActivity.class);
                intent.putExtra("id",id);
                mActivity.startActivity(intent);
                window.dismiss();
            }
        });
        if (isBlack.equals("off")){
            black.setCompoundDrawablesWithIntrinsicBounds(null,mActivity.getResources().getDrawable(R.mipmap.icon_unblack),null,null);
        }else {
            black.setCompoundDrawablesWithIntrinsicBounds(null,mActivity.getResources().getDrawable(R.mipmap.people_back),null,null);
        }
        black.setOnClickListener(listener);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }

    public void dismissWithdrapDialog() {
        window.dismiss();
    }

    //分享成功调用
    public void shareSucceedData(){
        YSBSdk.getService(OAuthService.class).set_transmit_data(new YRequestCallback<TaskBean>() {
            @Override
            public void onSuccess(TaskBean var1) {
                //System.out.println("分享成功任务增加");
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }


}
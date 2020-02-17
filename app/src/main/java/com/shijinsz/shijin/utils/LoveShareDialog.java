package com.shijinsz.shijin.utils;

import android.app.Activity;
import android.content.Context;
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
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.hongchuang.ysblibrary.model.model.bean.storeUserBean;
import com.hongchuang.ysblibrary.model.model.tencent.WeChatLoginResultBean;
import com.shijinsz.shijin.R;

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
 * 爱分享类
 */
public class LoveShareDialog implements View.OnClickListener {

    public Context mContext;
    public LayoutInflater inflater;
    private StateView mStateView;
    private PopupWindow window;
    private View popupView;
    private TextView wechat;
    private TextView wechatFriend;
    private TextView qq;
    private TextView qqzone;
    private TextView weibo;
    private Platform.ShareParams sp;

    private String mTitle;
    private String mContent;
    private String mImgurl;
    private String mUrl;
    private TextView cancel;
    //商品id
    private String mGoodsid;
    int mtype;



    public LoveShareDialog(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }

    private static final String TAG = "LoveShareDialog";


    public interface OnShareListen {
        void success(String channel);
    }

    public OnShareListen onShareListen;

    public void setOnShareListen(OnShareListen onShareListen) {
        this.onShareListen = onShareListen;
    }

    public void showWithdrapDialog(Activity mActivity, String title, String content, String imgurl, String url,String goodsid,int type) {
        mTitle = title;
        mContent = content;
        mImgurl = imgurl;
        mUrl = url;
        mGoodsid = goodsid;
        mtype = type;
        View viewGroup = ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        mStateView = StateView.inject(viewGroup);

        if (window == null) {
            popupView = inflater.inflate(R.layout.choice_invite_pop, null);
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            wechat = popupView.findViewById(R.id.wechat);
            wechatFriend = popupView.findViewById(R.id.wechat_friend);
            qq = popupView.findViewById(R.id.qq);
            qqzone = popupView.findViewById(R.id.qqzone);
            weibo = popupView.findViewById(R.id.weibo);
            cancel = popupView.findViewById(R.id.cancel);
        }

        wechat.setOnClickListener(this);
        wechatFriend.setOnClickListener(this);
        qq.setOnClickListener(this);
        qqzone.setOnClickListener(this);
        weibo.setOnClickListener(this);

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat:
                toShare("wx",Wechat.NAME);
                break;
            case R.id.wechat_friend:
                toShare("wx_circle", WechatMoments.NAME);
                break;
            case R.id.qq:
                toShare("qq", QQ.NAME);
                break;
            case R.id.qqzone:
                toShare("qq_zone", QZone.NAME);
                break;
            case R.id.weibo:
                toShare("weibo", SinaWeibo.NAME);
                break;
        }

    }

    public void toShare(String type,String name) {

        String showTyle = "";
        if (type.equals("wx") || type.equals("wx_circle")) {
            showTyle = "微信";
        } else if (type.equals("qq") || type.equals("qq_zone")) {
            showTyle = "qq";
        } else if (type.equals("weibo")) {
            showTyle = "微博";
        }

        ToastUtil.showToast("正在打开" + showTyle + "...");
        sp = new Platform.ShareParams();
        sp.setTitle(mTitle);
        //sp.setTitleUrl(mUrl + "&channel=" + type); // 标题的超链接
        sp.setTitleUrl(mUrl); // 标题的超链接
        sp.setText(mContent);
        sp.setImageUrl(mImgurl);
//                sp.setImagePath(imgurl);
        sp.setSite(mContext.getString(R.string.app_name));
        //sp.setSiteUrl(mUrl + "&channel=" + type);
        sp.setSiteUrl(mUrl);
        //sp.setUrl(mUrl + "&channel="+type);
        sp.setUrl(mUrl);
        sp.setShareType(Platform.SHARE_WEBPAGE);


        Platform wechat = ShareSDK.getPlatform(name);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //System.out.println("分享成功.....");
                //分享成功的回调
                mStateView.showContent();
                shareSucceedData();
                if (onShareListen!=null){
                    onShareListen.success(type);
                }

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                mStateView.showContent();
                Log.e(TAG, "onError: " + platform.getDb().exportData() + "  **arg1==" + i + "  **arg2==");
                throwable.printStackTrace();
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }

            @Override
            public void onCancel(Platform platform, int i) {
                //取消分享的回调
                mStateView.showContent();
            }
        });
        wechat.share(sp);
        dismissWithdrapDialog();
    }


    public void dismissWithdrapDialog() {
        window.dismiss();
    }

    //分享成功调用
    public void shareSucceedData(){

        if(mtype != 1){
            return;
        }
        YSBSdk.getService(OAuthService.class).getShareSucceed(mGoodsid,new YRequestCallback<storeUserBean>() {
            @Override
            public void onSuccess(storeUserBean var1) {
                //System.out.println("分享成功。。。。");
                ToastUtil.showToast("分享成功");
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

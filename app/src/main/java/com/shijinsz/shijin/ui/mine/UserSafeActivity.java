package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginHandle;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginResult;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;
//import com.tencent.tauth.UiError;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/7.
 */

public class UserSafeActivity extends BaseActivity implements QQLoginHandle{
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    private String phone,wechat,qq;
    @Override
    public int bindLayout() {
        return R.layout.user_safe_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.user_and_safe));
        phone= ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        wechat= ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_app_openid);
        qq= ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_app_qqid);
        tvPhone.setText(phone);
        if (wechat.isEmpty()){
            tvWechat.setText(getString(R.string.unbind));
        }else {
            tvWechat.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_wx_nickname));
        }
        if (qq.isEmpty()){
            tvQq.setText(getString(R.string.unbind));
        }else {
            tvQq.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_qq_nickname));
        }
    }

    private void bindWechat(String code,String name) {
        Map map = new HashMap();
        map.put("mode","app");
        map.put("openid",code);
        map.put("wx_nickname",name);
        YSBSdk.getService(OAuthService.class).bindphone(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_app_openid,code);
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_wx_nickname,name);
                mStateView.showContent();
                tvWechat.setText(name);
                ToastUtil.showToast(getString(R.string.bind_success));
                wechat=code;
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
    private void bindqq(String code, final String name) {
        Map map = new HashMap();
        map.put("mode","app");
        map.put("qqid",code);
        map.put("qq_nickname",name);
        YSBSdk.getService(OAuthService.class).qqbindphone(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_app_qqid,code);
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_qq_nickname,name);
                mStateView.showContent();
                ToastUtil.showToast(getString(R.string.bind_success));
                tvQq.setText(name);
                qq=code;
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    public void unbind(final String mode){
        Map map = new HashMap();
        map.put("mode",mode);
        YSBSdk.getService(OAuthService.class).delete_wechat(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ToastUtil.showToast(getString(R.string.unbind_success));
                if (mode.equals("app_qq")) {
                    ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_app_qqid,"");
                    tvQq.setText(getString(R.string.unbind));
                    qq="";
                }else {
                    ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_app_openid,"");
                    tvWechat.setText(getString(R.string.unbind));
                    wechat="";
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });

    }

    @OnClick({R.id.rl_change_psw, R.id.rl_bind_wechat, R.id.rl_bind_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_change_psw:
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.rl_bind_wechat:
                if (wechat.isEmpty()){
                    wechatAuthorized();
                }else {
                    showUnbindDialog("app_wx");
                }
                break;
            case R.id.rl_bind_qq:
                if (qq.isEmpty()){
                    tencentAuthorized();
                }else {
                    showUnbindDialog("app_qq");
                }
                break;
        }
    }

    private QQLoginResult loginResult;

    @Override
    public void setOnResultProcess(QQLoginResult result) {
        loginResult = result;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            mStateView.showContent();
        }
        if (loginResult != null) {
            loginResult.onResult(requestCode, resultCode, data);
        }
    }



    private void tencentAuthorized() {
        mStateView.showLoading();
        Platform qq= ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);
        qq.removeAccount(true);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                platform.getDb().exportData();
                bindqq(platform.getDb().getUserId(),platform.getDb().getUserName());


            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                mStateView.showContent();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                mStateView.showContent();
            }
        });
        qq.authorize();
    }

    private void wechatAuthorized() {
        mStateView.showLoading();
        Platform wechat= ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isAuthValid()){
            wechat.removeAccount(true);
        }
        wechat.SSOSetting(false);
//                wechat.removeAccount(true);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                platform.getDb().exportData();
                bindWechat(platform.getDb().getUserId(),platform.getDb().getUserName());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                mStateView.showContent();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                mStateView.showContent();
            }
        });
        wechat.authorize();

    }

    RelativeLayout mailBoxLay;
    NoticeDialog mailDialog;
    public void showUnbindDialog(final String mode) {
        if (mailBoxLay==null)
        mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.nickname_dialog, null);
        if (mailDialog == null)
        mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        TextView content = mailBoxLay.findViewById(R.id.content);
        if (mode.equals("app_wx")){
            content.setText(getString(R.string.wechat_dialog));
        }else {
            content.setText(getString(R.string.qq_dialog));
        }
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbind(mode);
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
    }
}

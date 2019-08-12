package com.shijinsz.shijin.ui.user;

import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DeviceUuidFactory;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginHandle;
import com.hongchuang.ysblibrary.model.model.tencent.QQLoginResult;
import com.hongchuang.ysblibrary.utils.NetworkUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.PerfectInformation1Activiity;
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
import retrofit.ToKenUtil;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/25.
 */

public class LoginActivity extends BaseActivity implements QQLoginHandle {

    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.login)
    Button login;

    @BindView(R.id.bt_wechat)
    ImageView btWechat;
    @BindView(R.id.bt_qq)
    ImageView btQq;
    private String click="";
    @Override
    public int bindLayout() {
        return R.layout.login_activity;
    }
    private DeviceUuidFactory deviceUuidFactory;
    @Override
    public void initView(View view) {
        deviceUuidFactory = new DeviceUuidFactory(this);
        StatusBarUtil.setStatusTextColor(true, mActivity);
        click=getIntent().getStringExtra("click");
        if (click!=null) {
            if (click.equals("on")) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_click_status, "pending");
            } else {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_click_status, "off");
            }
        }else {
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_click_status, "off");
        }

        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMobileNO(edPhone.getText().toString()) && edPassword.getText().toString().length()>5) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMobileNO(edPhone.getText().toString()) && edPassword.getText().toString().length()>5) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        Subscription subscription = RxBus.getInstance().toObserverable(WeChatLoginResultBean.class)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<WeChatLoginResultBean>() {
//                    @Override
//                    public void call(WeChatLoginResultBean weChatLoginResultBean) {
//                        Log.e(TAG, "call: " + weChatLoginResultBean.code);
//                        if (weChatLoginResultBean.code!=null){
//                        loginWechat(weChatLoginResultBean.code);
//                        }else {
//                            mStateView.showContent();
//                        }
//
//                    }
//                });
//        RxBus.getInstance().addSubscription(subscription);
    }

    public boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[56789]\\d{8}|15[012356789]\\d{8}|18\\d{9}|17[012345678]\\d{8}|166\\d{8}|19[89]\\d{8}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ToKenUtil.deleteToken();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String result = NetworkUtil.doGet("http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/3101644_075445358000_2.png");

            Message msg = new Message();
            msg.arg1 = 1;
//            handler.sendMessage(msg);
        }
    };
    @OnClick({R.id.back, R.id.login, R.id.bt_register, R.id.forget_password, R.id.bt_wechat, R.id.bt_qq})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                ToKenUtil.deleteToken();
                finish();
                break;
            case R.id.login:
//                new Thread(runnable).start();
                if (isMobileNO(edPhone.getText().toString())) {
                    Login();
                }else {

                }
                break;
            case R.id.bt_register:
                intent = new Intent(mContext, RegisterActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.forget_password:
                intent = new Intent(mContext, RegisterActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.bt_wechat:
                wechatAuthorized();
                break;
            case R.id.bt_qq:
                tencentAuthorized();
                break;
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
                loginqq(platform.getDb().getUserId(),platform.getDb().getUserName(),platform.getDb().getUserIcon());
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

                ToastUtil.showToast("登录成功");
                platform.getDb().exportData();
                loginWechat(platform.getDb().getUserId(),platform.getDb().getUserName(),platform.getDb().getUserIcon());

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                ToastUtil.showToast("登录异常 onError");
                mStateView.showContent();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtil.showToast("登录异常 onCancel");
                mStateView.showContent();
            }
        });
        wechat.authorize();

    }


    private void Login() {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("username", edPhone.getText().toString());
        map.put("password", edPassword.getText().toString());
        map.put("_uuid", "android7d6802d01b  "+deviceUuidFactory.getDeviceUuid().toString());
        map.put("_platform", "android7d6802d01b");
        map.put("_location", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATION));
        YSBSdk.getService(OAuthService.class).login(map, new YRequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean var1) {
                SaveData(var1);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_USER_NAME,edPhone.getText().toString());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_PASSWORD,edPassword.getText().toString());
                if (var1.getUser().getFirst_login_status().equals("on")){
                    startActivity(PerfectInformation1Activiity.class);
                }
                mStateView.showContent();
                ToastUtil.showToast("登陆成功");
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                ToastUtil.showToast(var1.getMessage());
            }
        });
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
    private void loginqq(String code,String name,String imgurl) {
        Map map = new HashMap();
        map.put("qqid", code);
        map.put("qq_name", name);
        map.put("mode", "app");
        map.put("imgurl", imgurl);
        map.put("_uuid", "android7d6802d01b  "+deviceUuidFactory.getDeviceUuid().toString());
        map.put("_platform", "android7d6802d01b");
        map.put("_location", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATION));
        YSBSdk.getService(OAuthService.class).qq(map, new YRequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean var1) {
                SaveData(var1);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_USER_NAME,edPhone.getText().toString());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_PASSWORD,edPassword.getText().toString());
                mStateView.showContent();
                ToastUtil.showToast("登陆成功");
                finish();
//                if (!var1.getUser().getQqid().isEmpty()) {
//                    Intent intent = new Intent(mContext, RegisterActivity.class);
//                    intent.putExtra("type", "3");
//                    startActivity(intent);
//                }else {
//                }
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                if (var1.equals("300")){
                    ToKenUtil.saveToken(message);
                    Intent intent = new Intent(mContext, RegisterActivity.class);
                    intent.putExtra("type", "3");
                    intent.putExtra("token",message);
                    startActivity(intent);
                }else {
                    ErrorUtils.error(mContext,var1,message);
                }

            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();

            }
        });
    }

    private void loginWechat(String code,String nickname,String imgurl) {
        Map map = new HashMap();
        map.put("mode", "app");
        map.put("openid", code);
        map.put("wx_nickname", nickname);
        map.put("imgurl", imgurl);
        map.put("_uuid", "android7d6802d01b  "+deviceUuidFactory.getDeviceUuid().toString());
        map.put("_platform", "android7d6802d01b");
        map.put("_location", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATION));

        YSBSdk.getService(OAuthService.class).wechat(map, new YRequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean var1) {

                SaveData(var1);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_USER_NAME,edPhone.getText().toString());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_PASSWORD,edPassword.getText().toString());
                mStateView.showContent();
                ToastUtil.showToast("登陆成功");
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                if (var1.equals("300")){
                    ToKenUtil.saveToken(message);
                    Intent intent = new Intent(mContext, RegisterActivity.class);
                    intent.putExtra("type", "4");
                    intent.putExtra("token",message);
                    //跳转到手机号码绑定
                    startActivity(intent);
                }else {
                    ErrorUtils.error(mContext,var1,message);
                }

            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    public void SaveData(UserBean bean){

        System.out.println("是否会员: " + bean.getUser().getMember_status());

        ToKenUtil.saveToken(bean.getToken());
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_first_login_status,bean.getUser().getFirst_login_status());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_nickname,bean.getUser().getNickname());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_ID,bean.getUser().getId());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_OPENID,bean.getUser().getOpenid());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_qqid,bean.getUser().getQqid());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_app_openid,bean.getUser().getApp_openid());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_app_qqid,bean.getUser().getApp_qqid());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_wx_nickname,bean.getUser().getWx_nickname());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_qq_nickname,bean.getUser().getQq_nickname());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_invitation_code ,bean.getUser().getInvitation_code());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_advertiser,bean.getUser().getIs_advertiser());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_new_one_status,bean.getUser().getNew_one_status());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_gender,bean.getUser().getGender());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_age,bean.getUser().getAge());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_birth,bean.getUser().getBirth());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_income,bean.getUser().getIncome());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_job,bean.getUser().getJob());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_nickname_update_number,bean.getUser().getNickname_update_number());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_imageurl,bean.getUser().getImageurl());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_points,bean.getUser().getPoints());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_address,bean.getUser().getAddress());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_change,bean.getUser().getChange());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_interest,bean.getUser().getInterests());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.Key_isLogin,"on");
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_complete_info,bean.getUser().getIs_complete_info());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_first_in_tantan_status,bean.getUser().getFirst_in_tantan_status());
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_click_status).equals("pending")){
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_click_status,"on");
        }
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_new_person_task,bean.getUser().getNewbieTaskCheck());
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_invitation_code_state,bean.getUser().getInvitation());
    }
}

package com.shijinsz.shijin.ui.user;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DeviceUuidFactory;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.PerfectInformation1Activiity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.ToKenUtil;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/26.
 */

public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ed_pwd)
    EditText edPwd;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.bt_commit)
    Button btCommit;
    private boolean open =false;
    private String phone = "",type = "1";
    private DeviceUuidFactory deviceUuidFactory;

    @Override
    public int bindLayout() {
        return R.layout.setpwd_activity;
    }
    
    @Override
    public void initView(View view) {
        deviceUuidFactory = new DeviceUuidFactory(this);
        StatusBarUtil.setStatusTextColor(true, mActivity);
        phone = getIntent().getStringExtra("phone");
        type = getIntent().getStringExtra("type");

    }


    @OnClick({R.id.back, R.id.img_code, R.id.bt_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                showDialog();
                break;
            case R.id.img_code:
                if (open) {
                    open=false;
                    imgCode.setImageResource(R.mipmap.icon_eyes_close);
                    edPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    open=true;
                    imgCode.setImageResource(R.mipmap.icon_eyes_open);
                    edPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.bt_commit:
                if (edPwd.getText().toString().length()>18){
                    ToastUtil.showToast(getString(R.string.cant_over_18));
                    return;
                }
                if (edPwd.getText().toString().length()<6){
                    ToastUtil.showToast(getString(R.string.cant_less_6));
                    return;
                }
                if (!ispsd(edPwd.getText().toString())){
                    ToastUtil.showToast(getString(R.string.include_num_word));
                    return;
                }
                SignUp();
                break;
        }
    }

    public boolean ispsd(String psd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(psd);

        return m.matches();
    }

//    private void bindPhone(){
//        Map map = new HashMap();
//        map.put("username",phone);
//        map.put("password",edPwd.getText().toString());
//        map.put("invitation_code",edCode.getText().toString()+"");
//        if (type.equals("3")){
//            map.put("mode","qq");
//        }else if (type.equals("4")){
//            map.put("mode","wechat");
//        }
//        YSBSdk.getService(OAuthService.class).bindphone(map, new YRequestCallback<PicCodeBean>() {
//            @Override
//            public void onSuccess(PicCodeBean var1) {
//                ToastUtil.showToast("绑定成功");
//                finish();
//            }
//
//            @Override
//            public void onFailed(String var1, String message) {
//                ToastUtil.showToast(message);
//            }
//
//            @Override
//            public void onException(Throwable var1) {
//                ToastUtil.showToast(var1.getMessage());
//            }
//        });
//    }

    private void SignUp() {
        Map map = new HashMap();
        map.put("username",phone);
        map.put("password",edPwd.getText().toString());
        map.put("invitation_code",edCode.getText().toString()+"");
        map.put("_uuid", "android7d6802d01b  "+deviceUuidFactory.getDeviceUuid().toString());
        map.put("_platform", "android7d6802d01b");
        map.put("_location", ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATION));

        if (type.equals("3")){
            map.put("mode","qq");
        }else if (type.equals("4")){
            map.put("mode","wechat");
        }
        YSBSdk.getService(OAuthService.class).signup(map, new YRequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean var1) {
                SaveData(var1);
                if (var1.getUser().getFirst_login_status().equals("on")){
                    MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                    finish();
                    startActivity(PerfectInformation1Activiity.class);
                    return;
                }
                ToastUtil.showToast(getString(R.string.register_success));
                startActivity(MainActivity.class);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast(var1.getMessage());
            }
        });
    }

    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.normal_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToKenUtil.deleteToken();
                finish();
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.unregister_success));
    }

    public void SaveData(UserBean bean){
        ToKenUtil.saveToken(bean.getToken());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_ID,bean.getUser().getId());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_nickname,bean.getUser().getNickname());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_OPENID,bean.getUser().getOpenid());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_qqid,bean.getUser().getQqid());
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
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_change,bean.getUser().getChange());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_wx_nickname,bean.getUser().getWx_nickname());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.Key_isLogin,"on");
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_first_login_status,bean.getUser().getFirst_login_status());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_qq_nickname,bean.getUser().getQq_nickname());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_app_qqid,bean.getUser().getApp_qqid());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_complete_info,bean.getUser().getIs_complete_info());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_first_in_tantan_status,bean.getUser().getFirst_in_tantan_status());
        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_app_openid,bean.getUser().getApp_openid());
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_click_status).equals("pending")){
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_click_status,"on");
        }
    }
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

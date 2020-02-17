package com.shijinsz.shijin.ui.wallet;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.PaySuccessActivity;
import com.shijinsz.shijin.ui.task.SignInActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/30.
 */

/** 十金提现页面 */
public class WithdrawActivity extends BaseActivity {
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.tv_1yuan)
    TextView tv1yuan;
    @BindView(R.id.tv_5yuan)
    TextView tv5yuan;
    @BindView(R.id.tv_10yuan)
    TextView tv10yuan;
    @BindView(R.id.rb_alipay)
    ImageView rbAlipay;
    @BindView(R.id.img_alipay)
    ImageView imgAlipay;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.tv_alipay2)
    TextView tvAlipay2;
    @BindView(R.id.tv_alipay3)
    TextView tvAlipay3;
    @BindView(R.id.rb_wechat)
    ImageView rbWechat;
    @BindView(R.id.img_wechat)
    ImageView imgWechat;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_wechat2)
    TextView tvWechat2;
    @BindView(R.id.tv_wechat3)
    TextView tvWechat3;
    @BindView(R.id.bt_pay_now)
    Button btPayNow;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.ln_alipay)
    LinearLayout lnAlipay;
    @BindView(R.id.ln_wechat)
    LinearLayout lnWechat;
    @BindView(R.id.ed_money)
    EditText edMoney;
    @BindView(R.id.withdraw_all)
    TextView withdrawAll;
    private float num;
    private DialogUtils dialogUtils;

    @Override
    public int bindLayout() {
        return R.layout.withdraw_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change).isEmpty()) {
            float mo=Float.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
            num = mo;
        } else {
            num = 0;
        }
        dialogUtils = new DialogUtils(mContext);
        SpannableStringBuilder style = new SpannableStringBuilder(getString(R.string.withdraw_prompt3));
        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), 11, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrompt.setText(style);
        tv1yuan.setText("1" + getString(R.string.yuan));
        tv5yuan.setText("5" + getString(R.string.yuan));
        tv10yuan.setText("8" + getString(R.string.yuan));
        tvAlipay2.setText(String.format(getString(R.string.limit), "3000"));
        tvWechat2.setText(String.format(getString(R.string.limit), "10000"));
        money.setText(num + "");
        edMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edMoney.getText().toString().isEmpty()) {
                    edMoney.setTextSize(24);
                    choice_type = 4;
                    Visibility();
                    tv1yuan.setTextColor(getResources().getColor(R.color.text_33));
                    tv1yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                    tv5yuan.setTextColor(getResources().getColor(R.color.text_33));
                    tv5yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                    tv10yuan.setTextColor(getResources().getColor(R.color.text_33));
                    tv10yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                } else {
                    edMoney.setTextSize(12);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private int choice_type = 1;

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    private void getUserInfo() {
        Map map = new HashMap();
        map.put("mode", "profile");
        YSBSdk.getService(OAuthService.class).getuserinfo(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<UserBean.UserDetailBean>() {
            @Override
            public void onSuccess(UserBean.UserDetailBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_change, var1.getChange());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_points, var1.getPoints());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_fan_number, var1.getFan_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_change, var1.getHistory_change());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_points, var1.getHistory_points());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_recharge_change, var1.getHistory_recharge_change());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_recharge_points, var1.getHistory_recharge_points());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draw_lottery_number, var1.getDraw_lottery_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_imageurl, var1.getImageurl());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_nickname, var1.getNickname());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_birth, var1.getBirth());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_gender, var1.getGender());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_address, var1.getAddress());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_income, var1.getIncome());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_job, var1.getJob());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_interest, var1.getInterests());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_advertiser, var1.getIs_advertiser());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_USER_NAME, var1.getUsername());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_withdraw_change_number, var1.getWithdraw_change_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_OPENID, var1.getOpenid());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_one_withdraw_status,var1.getOne_withdraw_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_five_withdraw_status,var1.getFive_withdraw_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_eight_withdraw_status,var1.getEight_withdraw_status());
                float mo=Float.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
                num = mo;
                money.setText(num + "");
            }

            @Override
            public void onFailed(String var1, String message) {
//                ErrorUtils.error(getContext(),var1);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    private boolean isAlipay = false;

    @OnClick({R.id.tv_prompt, R.id.withdraw_all, R.id.iv_back, R.id.tv_right, R.id.tv_1yuan, R.id.tv_5yuan, R.id.tv_10yuan, R.id.ln_alipay, R.id.ln_wechat, R.id.bt_pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_prompt:
                joinQQGroup(getString(R.string.QQ_GROUP_KEY));
                break;
            case R.id.tv_right:
                startActivity(PaymentActivity.class);
                break;
            case R.id.withdraw_all:
                edMoney.setText((int)num + "");
                break;
            case R.id.tv_1yuan:
                choice_type = 1;
                edMoney.setText("");
                Visibility();
                tv1yuan.setTextColor(getResources().getColor(R.color.white));
                tv1yuan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv5yuan.setTextColor(getResources().getColor(R.color.text_33));
                tv5yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                tv10yuan.setTextColor(getResources().getColor(R.color.text_33));
                tv10yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                break;
            case R.id.tv_5yuan:
                choice_type = 2;
                edMoney.setText("");
                Visibility();
                tv5yuan.setTextColor(getResources().getColor(R.color.white));
                tv5yuan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv1yuan.setTextColor(getResources().getColor(R.color.text_33));
                tv1yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                tv10yuan.setTextColor(getResources().getColor(R.color.text_33));
                tv10yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                break;
            case R.id.tv_10yuan:
                choice_type = 3;
                edMoney.setText("");
                Visibility();
                tv10yuan.setTextColor(getResources().getColor(R.color.white));
                tv10yuan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv5yuan.setTextColor(getResources().getColor(R.color.text_33));
                tv5yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                tv1yuan.setTextColor(getResources().getColor(R.color.text_33));
                tv1yuan.setBackground(getResources().getDrawable(R.drawable.gray_biankuang_1));
                break;
            case R.id.ln_alipay:
                isAlipay = true;
                rbAlipay.setImageResource(R.mipmap.radio_button_on);
                imgAlipay.setImageResource(R.mipmap.icon_alipay);
                tvAlipay.setTextColor(getResources().getColor(R.color.text_33));
                tvAlipay2.setTextColor(getResources().getColor(R.color.text_999999));
                tvAlipay3.setTextColor(getResources().getColor(R.color.text_999999));
                rbWechat.setImageResource(R.mipmap.radio_button_off);
                imgWechat.setImageResource(R.mipmap.icon_wechat_pay);
                tvWechat.setTextColor(getResources().getColor(R.color.text_hint));
                tvWechat2.setTextColor(getResources().getColor(R.color.text_hint));
                tvWechat3.setTextColor(getResources().getColor(R.color.text_hint));
                break;
            case R.id.ln_wechat:
                isAlipay = false;
                rbAlipay.setImageResource(R.mipmap.radio_button_off);
                imgAlipay.setImageResource(R.mipmap.icon_alipay_2);
                tvAlipay.setTextColor(getResources().getColor(R.color.text_hint));
                tvAlipay2.setTextColor(getResources().getColor(R.color.text_hint));
                tvAlipay3.setTextColor(getResources().getColor(R.color.text_hint));
                rbWechat.setImageResource(R.mipmap.radio_button_on);
                imgWechat.setImageResource(R.mipmap.icon_wechat_2);
                tvWechat.setTextColor(getResources().getColor(R.color.text_33));
                tvWechat2.setTextColor(getResources().getColor(R.color.text_999999));
                tvWechat3.setTextColor(getResources().getColor(R.color.text_999999));
                break;
            case R.id.bt_pay_now:
                if (choice_type == 4) {
                    float with = 0;
                    if (edMoney.getText().toString().isEmpty()) {
                        with = 0;
                    } else {
                        with = Float.valueOf(edMoney.getText().toString());
                    }
                    if (with < 10) {
                        ToastUtil.showToast(getString(R.string.more_than_10));
                        return;
                    }
                    if (with > num) {
                        dialogUtils.showWithdrapDialog(mActivity, 3, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(MainActivity.class);
                                dialogUtils.dismissWithdrapDialog();
                            }
                        });
                        return;
                    }
                }
                if (isAlipay) {
                    if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_alipay_account).isEmpty()) {
                        dialogUtils.showWithdrapDialog(mActivity, 1, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogUtils.dismissWithdrapDialog();
                            }
                        });
                        return;
                    }
                }else {
                    if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_OPENID).isEmpty()) {
                        dialogUtils.showbindWechatDialog(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getWechatApi();
                                dialogUtils.dismissbindWechatDialog();
                            }
                        });
                        return;
                    }
                }
                if (choice_type==1) {
                    if (Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_withdraw_change_number))>0){
                        if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_one_withdraw_status).equals("on")) {
                            dialogUtils.showWithdrapDialog(mActivity, 4, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(SignInActivity.class);
                                    dialogUtils.dismissWithdrapDialog();
                                }
                            });
                            return;
                        }
                    }

                }
                if (choice_type==2) {
                    if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_five_withdraw_status).equals("on")) {
                        dialogUtils.showWithdrapDialog(mActivity, 5, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(SignInActivity.class);
                                dialogUtils.dismissWithdrapDialog();
                            }
                        });
                        return;
                    }
                }
                if (choice_type==3) {
                    if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_eight_withdraw_status).equals("on")) {
                        dialogUtils.showWithdrapDialog(mActivity, 6, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(SignInActivity.class);
                                dialogUtils.dismissWithdrapDialog();
                            }
                        });
                        return;
                    }
                }
                btPayNow.setEnabled(false);
                transfer();

                break;
        }
    }
    /**
     * 跳转到微信
     */
    private void getWechatApi() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast("检查到您手机没有安装微信，请安装后使用该功能");
        }
    }
    private void transfer() {
        Map map = new HashMap();
        map.put("mode","wxpay");
        switch (choice_type){
            case 1:
                map.put("change",1);
                break;
            case 2:
                map.put("change",5);
                break;
            case 3:
                map.put("change",8);
                break;
            case 4:
                map.put("change",edMoney.getText().toString());
                break;
        }
        YSBSdk.getService(OAuthService.class).transfer(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                btPayNow.setEnabled(true);
                switch (choice_type){
                    case 1:
                        num=num-1;
                        ShareDataManager.getInstance().save(mActivity,SharedPreferencesKey.KEY_pay_money,"1");
                        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,num+"");
                        money.setText(num+"");
                        break;
                    case 2:
                        num=num-5;
                        ShareDataManager.getInstance().save(mActivity,SharedPreferencesKey.KEY_pay_money,"5");
                        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,num+"");
                        money.setText(num+"");
                        break;
                    case 3:
                        num=num-8;
                        ShareDataManager.getInstance().save(mActivity,SharedPreferencesKey.KEY_pay_money,"8");
                        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,num+"");
                        money.setText(num+"");
                        break;
                    case 4:
                        ShareDataManager.getInstance().save(mActivity,SharedPreferencesKey.KEY_pay_money,edMoney.getText().toString());
                        num=num-Integer.parseInt(edMoney.getText().toString());
                        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,num+"");
                        money.setText(num+"");
                        break;
                }
                getUserInfo();
                ShareDataManager.getInstance().save(mActivity,SharedPreferencesKey.KEY_pay_type,"100");
                Intent intent=new Intent(mContext, PaySuccessActivity.class);
                intent.putExtra("type","100");
                startActivity(intent);
            }

            @Override
            public void onFailed(String var1, String message) {
                btPayNow.setEnabled(true);
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1){
                try {
                    btPayNow.setEnabled(true);
                    var1.printStackTrace();
                }catch (Exception e){

                }
            }
        });

    }

    public void Visibility() {
        if (choice_type == 1) {
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv3.setText(String.format(getString(R.string.withdraw_prompt4), "1"));
            tv4.setText(String.format(getString(R.string.withdraw_prompt5), "5", "1"));
        } else if (choice_type == 2) {
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv3.setText(String.format(getString(R.string.withdraw_prompt4), "5"));
            tv4.setText(String.format(getString(R.string.withdraw_prompt5), "10", "5"));
        } else if (choice_type == 3) {
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv3.setText(String.format(getString(R.string.withdraw_prompt4), "8"));
            tv4.setText(String.format(getString(R.string.withdraw_prompt5), "15", "8"));
        } else {
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.GONE);
            tv4.setVisibility(View.GONE);
        }
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
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
}

package com.shijinsz.shijin.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.verificationsdk.ui.IActivityCallback;
import com.alibaba.verificationsdk.ui.VerifyActivity;
import com.alibaba.verificationsdk.ui.VerifyType;
import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BasketBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.ToKenUtil;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/25.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.img_code)
    ImageView imgCode;
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.bt_get_code)
    Button btGetCode;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ln_code)
    LinearLayout lnCode;
    @BindView(R.id.line)
    View line;
    private String token = "";
    private String type = "1";

    @Override
    public int bindLayout() {
        return R.layout.register_main;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        type = getIntent().getStringExtra("type");
        if (type != null) {
            switch (type) {
                case "1":
                    title.setText(getString(R.string.register));
                    break;
                case "2":
                    title.setText(getString(R.string.forget_pwd));
                    lnCode.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    title.setText(getString(R.string.perfect_info));//qq
                    token = getIntent().getStringExtra("token");
                    break;
                case "4":
                    title.setText(getString(R.string.perfect_info));//wechat
                    token = getIntent().getStringExtra("token");
                    break;
            }
        }
        getCode();
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMobileNO(edPhone.getText().toString())) {
                    btGetCode.setEnabled(true);
                } else {
                    btGetCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        edCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (isMobileNO(edPhone.getText().toString()) && !edCode.getText().toString().isEmpty()) {
//                    btGetCode.setEnabled(true);
//                } else {
//                    btGetCode.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


    }

    private void getCode() {
        YSBSdk.getService(OAuthService.class).pic_code(new HashMap<String, String>(), new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                Log.e(TAG, var1.getImage());

                Glide.with(mActivity).load(var1.getImage()).into(imgCode);
                if (!type.equals("3") || type.equals("4")) {
                    ToKenUtil.saveToken(var1.getToken());
                } else {
                    ToKenUtil.saveToken(token);
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                Log.e(TAG, var1.getMessage());
            }
        });
    }


    @OnClick({R.id.back, R.id.bt_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt_get_code:
                if (type.equals("2")) {
                    if (edCode.getText().toString().isEmpty()){
                        ToastUtil.showToast(getString(R.string.plz_into_code));
                        return;
                    }
                    pic_code_comparison();
                    return;
                }
                VerifyActivity.startSimpleVerifyUI(RegisterActivity.this, VerifyType.NOCAPTCHA, "0335", null, new IActivityCallback() {
                    @Override
                    //返回按钮回调
                    public void onNotifyBackPressed() {
//                        RegisterActivity.this.setLogMessage("操作取消");
                    }

                    @Override
                    public void onResult(int retInt, Map code) {
                        RegisterActivity.this.verifyDidFinishedWithResult(retInt, code);
                    }
                });

                break;
        }
    }

    /**
     * 验签回调功能函数
     *
     * @param retInt 返回码
     * @param code   返回内容
     */
    public void verifyDidFinishedWithResult(int retInt, Map code) {
        switch (retInt) {
            case VerifyActivity.VERIFY_SUCC:
//                Log.e(TAG, (String) code.get("sessionID"));
//                sessionid = (String) code.get("sessionID");
                LogUtils.e(TAG, "验证通过:\nsessionId=" + (String) code.get("sessionID"));
                basket((String) code.get("sessionID"));
//                checkWithSessionId();
                break;

            case VerifyActivity.VERIFY_FAILED:
                Log.e(TAG, (String) code.get("errorCode"));
                Log.e(TAG, (String) (null != code.get("errorMsg") ? code.get("errorMsg") : ""));
//                setLogMessage((String) (null != code.get("errorMsg") ? code.get("errorMsg") : ""));

                VerifyActivity.finishVerifyUI();
                break;
        }
    }

    public void basket(String session_id) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "android");
        map.put("session_id", session_id);
        map.put("cellphone", edPhone.getText().toString());
        switch (type) {
            case "1":
                map.put("purpose", "signup");
                break;
            case "2":
                map.put("purpose", "forgot_password");
                break;
            case "3":
                map.put("purpose", "qq_login");
                break;
            case "4":
                map.put("purpose", "wx_login");
                break;
        }
        YSBSdk.getService(OAuthService.class).basket(map, new YRequestCallback<BasketBean>() {
            @Override
            public void onSuccess(BasketBean var1) {
                mStateView.showContent();
                Intent intent = new Intent(mContext, SendMsgActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("phone", edPhone.getText().toString());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                var1.printStackTrace();
            }
        });
    }

    public boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[56789]\\d{8}|15[012356789]\\d{8}|18\\d{9}|17[012345678]\\d{8}|166\\d{8}|19[89]\\d{8}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void pic_code_comparison() {
        Map map = new HashMap();
        map.put("pic_code", edCode.getText().toString());
        map.put("cellphone", edPhone.getText().toString());
        switch (type) {
            case "1":
                map.put("purpose", "signup");
                break;
            case "2":
                map.put("purpose", "forgot_password");
                break;
            case "3":
                map.put("purpose", "qq_login");
                break;
            case "4":
                map.put("purpose", "wx_login");
                break;
        }

        YSBSdk.getService(OAuthService.class).pic_code_comparison(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                Intent intent = new Intent(mContext, SendMsgActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("phone", edPhone.getText().toString());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast(var1.getMessage());
            }
        });
    }

}

package com.shijinsz.shijin.ui.user;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/26.
 */

public class ForgetPassWordActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.bt_get_code)
    Button btGetCode;

    @Override
    public int bindLayout() {
        return R.layout.forgetpassword_activity;
    }

    @Override
    public void initView(View view) {
        getCode();
        StatusBarUtil.setStatusTextColor(true, mActivity);
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMobileNO(edPhone.getText().toString()) && !edCode.getText().toString().isEmpty()) {
                    btGetCode.setEnabled(true);
                }else {
                    btGetCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMobileNO(edPhone.getText().toString()) && !edCode.getText().toString().isEmpty()) {
                    btGetCode.setEnabled(true);
                }else {
                    btGetCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void getCode() {
        YSBSdk.getService(OAuthService.class).pic_code(new HashMap<String, String>(), new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                Log.e(TAG, var1.getImage());
                Glide.with(mActivity).load(var1.getImage()).into(imgCode);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
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
                pic_code_comparison();
                break;
        }
    }

    public boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[56789]\\d{8}|15[012356789]\\d{8}|18\\d{9}|17[012345678]\\d{8}|166\\d{8}|19[89]\\d{8}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void pic_code_comparison() {
        Map map = new HashMap();
        map.put("pic_code", edCode.getText().toString());
        YSBSdk.getService(OAuthService.class).pic_code_comparison(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                Intent intent = new Intent(mContext, SendMsgActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("phone", edPhone.getText().toString());
                startActivity(intent);
                finish();
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

}

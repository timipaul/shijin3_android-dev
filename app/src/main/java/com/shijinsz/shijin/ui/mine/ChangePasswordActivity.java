package com.shijinsz.shijin.ui.mine;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/7.
 */

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ed_first)
    EditText edFirst;
    @BindView(R.id.ed_new)
    EditText edNew;
    @BindView(R.id.ed_pwd_again)
    EditText edPwdAgain;
    @BindView(R.id.bt_get_code)
    Button btGetCode;

    @Override
    public int bindLayout() {
        return R.layout.reset_password_main;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        edFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edFirst.getText().toString().isEmpty()&&!edNew.getText().toString().isEmpty()&&!edPwdAgain.getText().toString().isEmpty()){
                    btGetCode.setEnabled(true);
                }else {
                    btGetCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edFirst.getText().toString().isEmpty()&&!edNew.getText().toString().isEmpty()&&!edPwdAgain.getText().toString().isEmpty()){
                    btGetCode.setEnabled(true);
                }else {
                    btGetCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edPwdAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edFirst.getText().toString().isEmpty()&&!edNew.getText().toString().isEmpty()&&!edPwdAgain.getText().toString().isEmpty()){
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


    @OnClick({R.id.back, R.id.bt_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt_get_code:
                if (edNew.getText().toString().length()>18){
                    ToastUtil.showToast("密码不能超过18个字符噢");
                    return;
                }
                if (edNew.getText().toString().length()<6){
                    ToastUtil.showToast("请至少输入6个字符噢");
                    return;
                }
                if (!edNew.getText().toString().equals(edPwdAgain.getText().toString())){
                    ToastUtil.showToast(getString(R.string.nosame_pwd));
                }else {
                    if (!ispsd(edNew.getText().toString())){
                        ToastUtil.showToast("密码需要包含数字和字母");
                        return;
                    }
                    changePassword();
                }
                break;
        }
    }
    public boolean ispsd(String psd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(psd);

        return m.matches();
    }
    private void changePassword() {
        Map map = new HashMap();
        map.put("mode","change_password");
        map.put("old_password",edFirst.getText().toString());
        map.put("new_password",edNew.getText().toString());
        YSBSdk.getService(OAuthService.class).change_password(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ToastUtil.showToast(getString(R.string.change_success));
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_PASSWORD,edNew.getText().toString());
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast(var1.getMessage());
            }
        });
    }
}

package com.shijinsz.shijin.ui.user;

import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
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
 * Created by Administrator on 2018/7/26.
 */

public class SetNewPasswordActivity extends BaseActivity {
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
    private boolean open = false;
    private String phone = "";

    @Override
    public int bindLayout() {
        return R.layout.setnewpwd_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        phone = getIntent().getStringExtra("phone");

    }


    @OnClick({R.id.back, R.id.img_code, R.id.bt_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                showDialog();
                break;
            case R.id.img_code:
                if (open) {
                    open = false;
                    imgCode.setImageResource(R.mipmap.icon_eyes_close);
                    edPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    open = true;
                    imgCode.setImageResource(R.mipmap.icon_eyes_open);
                    edPwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.bt_commit:
                if (!edPwd.getText().toString().equals(edCode.getText().toString())){
                    ToastUtil.showToast(getString(R.string.nosame_pwd));
                    return;
                }
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

    private void SignUp() {
        Map map = new HashMap();
        map.put("mode", "forgot_password");
        map.put("new_password", edPwd.getText().toString());
        YSBSdk.getService(OAuthService.class).forgetpassword(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ToastUtil.showToast(getString(R.string.pwd_set_success));
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

    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.normal_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView) mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                mailDialog.dismiss();
            }
        });
        ((TextView) mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
        ((TextView) mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.pwd_set_fail));
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

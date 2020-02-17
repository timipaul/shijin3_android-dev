package com.shijinsz.shijin.ui.task;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: WriteInvitationCodeActivity
 * Author: paul
 * Date: 2019/6/20 15:32
 * Description: 填写邀请码
 */
public class WriteInvitationCodeActivity extends BaseActivity {

    @BindView(R.id.edit_code)
    EditText mEdit_code;
    @BindView(R.id.code_button)
    Button mButton_code;
    @BindView(R.id.error_hint)
    TextView mError_hint;

    private String userid;

    @Override
    public int bindLayout() {
        return R.layout.import_invite_code;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("输入邀请码");
        showTitleBackButton();

        userid = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);

        mEdit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    mEdit_code.setLetterSpacing(0);
                }else{
                    mEdit_code.setLetterSpacing(1);
                    mError_hint.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.code_button})
    public void onClickCode(View view){
        //提交数据
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("userid",userid);
        map.put("code",mEdit_code.getText().toString());
        YSBSdk.getService(OAuthService.class).submit_invitation_code(map, new YRequestCallback<ShenmiBean>() {

            @Override
            public void onSuccess(ShenmiBean var1) {
                //修改按钮文字
                mButton_code.setText("已填写");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_invitation_code_state,"true");
                mError_hint.setVisibility(View.GONE);
                mEdit_code.setFocusable(false);
                mEdit_code.setFocusableInTouchMode(false);
                Toast.makeText(mContext,"邀请成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String var1, String message) {
                //显示文字异常信息
                mError_hint.setVisibility(View.VISIBLE);

            }

            @Override
            public void onException(Throwable var1) {

            }
        });

    }
}

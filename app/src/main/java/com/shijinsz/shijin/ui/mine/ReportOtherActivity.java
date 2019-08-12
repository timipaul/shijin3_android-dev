package com.shijinsz.shijin.ui.mine;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hongchuang.hclibrary.manager.MActivityManager;
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
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/19.
 */

public class ReportOtherActivity extends BaseActivity {
    @BindView(R.id.edt)
    EditText edt;
    @BindView(R.id.num)
    TextView num;

    @Override
    public int bindLayout() {
        return R.layout.report1_layout;
    }
    private String id;

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.report));
        id=getIntent().getStringExtra("id");
        showTitleRightText(getString(R.string.finish), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt.getText().toString().isEmpty()){
                    report();
                }
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                num.setText(edt.getText().toString().length()+"/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void report(){
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("complaint_content",edt.getText().toString());
        YSBSdk.getService(OAuthService.class).complaints(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("举报成功");
                MActivityManager.getInstance().delACT(ReportActivity.class);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
}

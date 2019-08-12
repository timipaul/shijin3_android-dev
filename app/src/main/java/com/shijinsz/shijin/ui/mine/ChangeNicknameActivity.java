package com.shijinsz.shijin.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ChangeUserInfoBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/6.
 */

public class ChangeNicknameActivity extends BaseActivity {
    @BindView(R.id.ed_name)
    EditText edName;

    @Override
    public int bindLayout() {
        return R.layout.change_name_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        showTitleRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edName.getText().toString().isEmpty()){
                    ToastUtil.showToast("昵称不能为空");
                    return;
                }else {
                    showDialog();
                }
            }
        });
        edName.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname));
        setTitle("修改昵称");
    }
    public void change_info(String key, String value){
        mStateView.showLoading();
        ChangeUserInfoBean map =new ChangeUserInfoBean();
        map.setKey(key);
        map.setValue(value);
        map.setMode("profile");
        YSBSdk.getService(OAuthService.class).change_info(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map,new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("修改成功");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_nickname,edName.getText().toString());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_nickname_update_number,"1");
                Intent intent = new Intent(mContext,UserInformationActivity.class);
                intent.putExtra("nickname",edName.getText().toString());
                setResult(Activity.RESULT_OK,intent);
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
                mStateView.showRetry();
            }
        });

    }
    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.nickname_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        TextView content = mailBoxLay.findViewById(R.id.content);
        String srt="你确定将昵称设置为"+edName.getText().toString()+"吗？ 确定以后将不可以修改";
        SpannableStringBuilder style = new SpannableStringBuilder(srt);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),9,edName.getText().length()+9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(style);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_info("nickname",edName.getText().toString());
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

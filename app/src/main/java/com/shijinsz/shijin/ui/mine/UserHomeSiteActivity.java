package com.shijinsz.shijin.ui.mine;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C)
 * FileName: UserHomeSiteActivity
 * Author: Administrator
 * Date: 2019/8/22 0022 下午 16:59
 * Description: 用户地址信息
 */
public class UserHomeSiteActivity extends BaseActivity {

    @BindView(R.id.user_name)
    EditText mName;
    @BindView(R.id.user_phone)
    EditText mPhone;
    @BindView(R.id.user_site)
    EditText mSite;
    @BindView(R.id.user_remark)
    EditText mRemark;
    @BindView(R.id.button)
    Button mButton;

    @Override
    public int bindLayout() {
        return R.layout.user_home_site;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("收货地址");
        showTitleBackButton();


        //设置本地保存地址信息
        mName.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee));
        mPhone.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_phone));
        mSite.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_site));
        mRemark.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_consignee_remark));

    }

    @OnClick(R.id.button)
    public void buttonClick(View view){
        //判断数据是否为空  空就不能提交
        String name = mName.getText().toString();
        String phone = mPhone.getText().toString();
        String site = mSite.getText().toString();
        String remark = mRemark.getText().toString();

        if(TextUtil.isPhoneNumber(phone)){
            System.out.println("手机号码正确");
        }else{
            System.out.println("手机号码不正确");
            Toast.makeText(mContext,"手机号码不正确",Toast.LENGTH_SHORT).show();
            return;
        }

        if(name.trim().equals("") ||phone.trim().equals("") || site.trim().equals("")){
            Toast.makeText(mContext,"内容不能为空",Toast.LENGTH_SHORT).show();
        }else{
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee,name);
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_phone,phone);
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_site,site);
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_remark,remark);
            finish();
        }

    }
}

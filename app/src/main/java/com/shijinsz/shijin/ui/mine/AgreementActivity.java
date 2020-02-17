package com.shijinsz.shijin.ui.mine;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.shijinsz.shijin.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;

import static com.shijinsz.shijin.ui.mine.MyVipActivity.KEY_privacy_code;
import static com.shijinsz.shijin.ui.mine.MyVipActivity.KEY_vip_code;
import static com.shijinsz.shijin.ui.mine.MyVipActivity.KEY_user_code;

/**
 * Copyright (C)
 * FileName: AgreementActivity
 * Author: paul
 * Date: 2019/5/16 9:35
 * Description: 通用协议页面
 */
public class AgreementActivity extends BaseActivity {


    @BindView(R.id.tv_protocol)
    TextView mTextView;

    @Override
    public int bindLayout() {
        return R.layout.common_agreement;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);

        showTitleBackButton();

        Bundle bundle = getIntent().getExtras();
        String codeStr = null;
        if(bundle != null){
            codeStr = bundle.getString("code");
        }



        InputStream inputStream = null;
        if(codeStr != null){
            if(codeStr.equals(KEY_vip_code)){
                //会员协议
                setTitle("会员协议");
                inputStream = getResources().openRawResource(R.raw.vip_hint);
            }else if(codeStr.equals(KEY_privacy_code)){
                //隐私协议
                setTitle("隐私协议");
                inputStream = getResources().openRawResource(R.raw.agreement_hint);
            }else if(codeStr.equals(KEY_user_code)){
                //用户协议
                setTitle("用户协议");
                inputStream = getResources().openRawResource(R.raw.user_hint);
            }
        }


        String content = TextUtils.getTextString(inputStream);
        mTextView.setText(Html.fromHtml(content));


    }

    //被工具类替换
//    public static String getTextString(InputStream inputStream) {
//        InputStreamReader inputStreamReader = null;
//        try {
//            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }
//        BufferedReader reader = new BufferedReader(inputStreamReader);
//        StringBuffer sb = new StringBuffer("");
//        String line;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//                sb.append("\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }
}

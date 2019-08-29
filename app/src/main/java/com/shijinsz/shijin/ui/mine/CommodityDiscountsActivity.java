package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.CommodityCardBean;
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
 * Copyright (C)
 * FileName: CommodityDiscountsActivity
 * Author: Administrator
 * Date: 2019/8/21 0021 下午 15:31
 * Description: 商品提货卡
 */
public class CommodityDiscountsActivity extends BaseActivity {


    @BindView(R.id.card_account)
    EditText mAccount;
    @BindView(R.id.card_password)
    EditText mPassword;
    @BindView(R.id.tv_error)
    TextView mError;
    @BindView(R.id.bt_submit)
    Button mSubmit;

    @Override
    public int bindLayout() {
        return R.layout.commodity_discounts;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("提货码");
        showTitleBackButton();


        //内容监听
        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //setErrorData(editable.toString(),"卡号");
                mError.setVisibility(View.INVISIBLE);
                if(!mAccount.getText().toString().equals("") || mPassword.getText().toString().equals("")){
                    mSubmit.setEnabled(true);
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                //setErrorData(editable.toString(),"卡密");
                mError.setVisibility(View.INVISIBLE);
                if(!mAccount.getText().toString().equals("") || mPassword.getText().toString().equals("")){
                    mSubmit.setEnabled(true);
                }



            }
        });


    }

    public boolean setErrorData(){
        if(mAccount.getText().toString().equals("") && mPassword.getText().toString().equals("")){
            setBttonType("卡号卡密不能为空",2);
            return false;
        }else if(mAccount.getText().length()!= 10 || mPassword.getText().length() != 6){
            setBttonType("卡号卡密错误",2);
            return false;
        }else{
            setBttonType("",1);
            return true;
        }
    }



    @OnClick(R.id.bt_submit)
    public void PutSubmit(View view){

        /*if(!setErrorData()){
            return;
        }*/

        Map map = new HashMap();
        map.put("username", mAccount.getText().toString());
        map.put("password", mPassword.getText().toString());
        YSBSdk.getService(OAuthService.class).card_verify(map, new YRequestCallback<CommodityCardBean>() {
            @Override
            public void onSuccess(CommodityCardBean var1) {
                Intent intent = new Intent(CommodityDiscountsActivity.this,DiscountsParticularsActivity.class);
                Bundle data = new Bundle();
                data.putString("cardId",var1.getCardId());
                data.putString("cardName",var1.getCategoryName());
                data.putString("price",var1.getPrice());
                data.putString("postage",var1.getPostage());
                data.putString("cardNumber",var1.getNum());
                intent.putExtra("data",data);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(CommodityDiscountsActivity.this, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                setBttonType("账号密码错误",2);
                mSubmit.setEnabled(true);
            }
        });


    }

    //设置按钮点击状态
    public void setBttonType(String str,int type){
        if(type == 1){
            mError.setVisibility(View.INVISIBLE);
            mSubmit.setEnabled(true);
        }else{
            mError.setText(str);
            mError.setVisibility(View.VISIBLE);
            mSubmit.setEnabled(false);
        }

    }

}

package com.shijinsz.shijin.ui.mine;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: UserSiteDataActivity
 * Author: m1342
 * Date: 2019/5/20 17:56
 * Description: 0元购填表页面
 */
public class UserSiteDataActivity extends BaseActivity {

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
    @BindView(R.id.img)
    ImageView mImg;
    @BindView(R.id.name)
    TextView mSname;


    private String user_id;
    private String user_name;
    private String goods_name;
    private String goods_id;

    private DialogUtils dialogUtils;

    Handler mHandler = new Handler();

    @Override
    public int bindLayout() {
        return R.layout.user_site_data;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("0元购填表页面");
        showTitleBackButton();

        getUserData();
    }

    //获取用户数据
    private void getUserData() {

        if (dialogUtils == null) {
            dialogUtils = new DialogUtils(mActivity);
        }

        user_id = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        user_name = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Bundle data = getIntent().getExtras();
        goods_name = data.getString("goods_name");
        goods_id = data.getString("goods_id");
        String url = data.getString("url");
        mSname.setText(goods_name);

        Glide.with(this).load(url).into(mImg);

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

        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee,name);
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_phone,phone);
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_site,site);
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_consignee_remark,remark);

        if(name.trim().equals("") ||phone.trim().equals("") || site.trim().equals("")){
            Toast.makeText(mContext,"内容不能为空",Toast.LENGTH_SHORT).show();
        }else{
            Map map = new HashMap();
            //map.put("mode", "person");
            map.put("user_id", user_id);
            map.put("goods_id", goods_id);
            map.put("username", user_name);
            map.put("consignee", name);
            map.put("contact_way", phone);
            map.put("shipping_address", site);
            map.put("remark", remark);
            YSBSdk.getService(OAuthService.class).put_site_data(map,new YRequestCallback<GoodsBean>() {
                @Override
                public void onSuccess(GoodsBean var1) {
                    /**抢购成功弹框*/
                    puyOkPop();
                }

                @Override
                public void onFailed(String var1, String message) {
                    ErrorUtils.error(mContext, var1, message);
                }

                @Override
                public void onException(Throwable var1) { }
            });
        }
    }

    private View zeroBuyView;
    private NoticeDialog zeroBuyDialog;
    //抢购成功弹框
    private void puyOkPop() {
        zeroBuyView = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                R.layout.zero_buy_pop, null);
        zeroBuyDialog = new NoticeDialog(mContext);
        zeroBuyDialog.setCanceledOnTouchOutside(false);
        zeroBuyDialog.setCancelable(false);
        zeroBuyDialog.showDialog(zeroBuyView, 0, 0,1);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },3000);
    }
}

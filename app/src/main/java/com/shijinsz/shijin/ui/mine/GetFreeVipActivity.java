package com.shijinsz.shijin.ui.mine;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.VIpStateBean;
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
 * FileName: GetFreeVipActivity
 * Author: paul
 * Date: 2019/5/16 16:36
 * Description: 获取免费会员页面
 */
public class GetFreeVipActivity extends BaseActivity {

    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.button)
    Button mButton;

    ArrayAdapter mAdapter;
    String vip_state;
    private String userid;



    @Override
    public int bindLayout() {
        return R.layout.get_free_vip;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("免费体验");
        showTitleBackButton();

        String[] datas = {"1.体验时间总计3天（包含开通当日），若已开通会员，则延长3天；",
                "2.免费会员享受同等正式会员待遇；",
                "3.活动时间：2019.05.20-2019.07.20",
                "4.活动对象：针对所有十金新老用户",
                "5.本活动最终解释权归十金官方所有"
        };
        mAdapter = new ArrayAdapter(mContext,R.layout.textview_item,datas);
        mListView.setAdapter(mAdapter);
    }

    @OnClick(R.id.button)
    public void butOnClick(View view){
        //获取会员状态 判断是否可以点击
        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KET_free_vip_state).equals("on")){
            getTryVip();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userid = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        //获取会员状态 判断是否可以点击
        vip_state = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KET_free_vip_state);
        updata();
    }

    private void updata() {

        if(vip_state.equals("on")){
            mButton.setText("领取");
            mButton.setBackground(getResources().getDrawable(R.drawable.button_red_bg));
        }else {
            mButton.setText("已领取");
            mButton.setBackground(getResources().getDrawable(R.drawable.button_gray_bg));
            mButton.setClickable(false);

        }
    }

    //获取三天试用VIp
    private void getTryVip() {
        Map map = new HashMap();
        map.put("user_id", userid);
        YSBSdk.getService(OAuthService.class).free_vip(map, new YRequestCallback<VIpStateBean>() {
            @Override
            public void onSuccess(VIpStateBean var1) {
                if(var1 != null){
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KET_free_vip_state, "off");
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_user_vip_state, "on");
                    ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_user_vip_end,var1.getExpiration_at());
                    mButton.setText("已领取");
                    mButton.setBackground(getResources().getDrawable(R.drawable.button_gray_bg));
                }else {
                    //设置会员数据 修改会员状态
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KET_free_vip_state, "off");
                    ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_user_vip_state, "on");
                    //Toast.makeText(mContext, "领取成功", Toast.LENGTH_SHORT).show();
                    vip_state = "on";
                    mButton.setText("已领取");
                    mButton.setBackground(getResources().getDrawable(R.drawable.button_gray_bg));
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

}

package com.shijinsz.shijin.ui.wallet;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ChangeChangeBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.CommonAdapter;
import com.shijinsz.shijin.base.ViewHolder;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/11/26.
 */

public class ChangeChangeActivity extends BaseActivity {
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.about_yuan)
    TextView aboutYuan;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.commit)
    TextView commit;
    private CommonAdapter<ChangeChangeBean> adapter;
    private List<ChangeChangeBean> list=new ArrayList<>();
    private int rate=0;
    @Override
    public int bindLayout() {
        return R.layout.change_change_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    public void initView(View view) {
        setTitle("兑换零钱");
        showTitleBackButton();
        adapter=new CommonAdapter<ChangeChangeBean>(mContext,list,R.layout.change_change_item) {
            @Override
            public void convert(ViewHolder helper, ChangeChangeBean item, int position) {
                helper.setText(R.id.change,item.getChange()+"元");
                helper.setText(R.id.point,String.format(getString(R.string.need_x_point),item.getPoint()));
                if (item.isSelect()){
                    helper.setBackgroundResource(R.id.ln,R.drawable.change_after);
                    helper.setTextColor(R.id.change,R.color.white);
                    helper.setTextColor(R.id.point,R.color.white);
                }else {
                    helper.setBackgroundResource(R.id.ln,R.drawable.change_before);
                    helper.setTextColor(R.id.change,R.color.text_33);
                    helper.setTextColor(R.id.point,R.color.text_6);
                }
            }
        };
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    if (i==i1){
                        list.get(i1).setSelect(true);
                    }else {
                        list.get(i1).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void getUserInfo() {
        Map map = new HashMap();
        map.put("mode", "profile");
        YSBSdk.getService(OAuthService.class).getuserinfo(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<UserBean.UserDetailBean>() {
            @Override
            public void onSuccess(UserBean.UserDetailBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_change, var1.getChange());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_points, var1.getPoints());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_fan_number, var1.getFan_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_change, var1.getHistory_change());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_points, var1.getHistory_points());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_recharge_change, var1.getHistory_recharge_change());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_history_recharge_points, var1.getHistory_recharge_points());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draw_lottery_number, var1.getDraw_lottery_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_imageurl, var1.getImageurl());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_nickname, var1.getNickname());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_birth, var1.getBirth());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_gender, var1.getGender());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_address, var1.getAddress());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_income, var1.getIncome());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_job, var1.getJob());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_interest, var1.getInterests());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_advertiser, var1.getIs_advertiser());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_USER_NAME, var1.getUsername());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_withdraw_change_number, var1.getWithdraw_change_number());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_OPENID, var1.getOpenid());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_one_withdraw_status,var1.getOne_withdraw_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_five_withdraw_status,var1.getFirst_in_tantan_status());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_eight_withdraw_status,var1.getEight_withdraw_status());
                if (var1.getConversion_ratio()!=null) {
                    if (!var1.getConversion_ratio().isEmpty()) {
                        list.clear();
                        rate = Integer.parseInt(var1.getConversion_ratio());
                        rate=rate/10;
                        list.add(new ChangeChangeBean("0.1", rate+"", true));
                        list.add(new ChangeChangeBean("0.5", rate*5+"", false));
                        list.add(new ChangeChangeBean("1", rate*10+"", false));
                        list.add(new ChangeChangeBean("2", rate*20+"", false));
                        list.add(new ChangeChangeBean("5", rate*50+"", false));
                        list.add(new ChangeChangeBean("10", rate*100+"", false));
                        adapter.notifyDataSetChanged();
                    }
                }
                tvPoint.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));
                float num=Float.valueOf(tvPoint.getText().toString());
                num = num/(rate*10);
                DecimalFormat fnum  =   new  DecimalFormat("##0.00");
                String dd=fnum.format(num);
                aboutYuan.setText(String.format(getString(R.string.about_x_yuan),dd+""));


            }

            @Override
            public void onFailed(String var1, String message) {
                tvPoint.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));
//                ErrorUtils.error(getContext(),var1);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }
    @OnClick(R.id.commit)
    public void onViewClicked() {
        commit.setEnabled(false);
        for (ChangeChangeBean changeChangeBean : list) {
            if (changeChangeBean.isSelect()){
                float point = Float.valueOf(changeChangeBean.getPoint());
                float point2 = Float.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));
                if (point>point2){
                    ToastUtil.showToast("金币不足呢~");
                    commit.setEnabled(false);
                }else {
                    convert(point);
                }
            }
        }
    }
    private void convert(float points){
        mStateView.showLoading();
       YSBSdk.getService(OAuthService.class).convert(points + "", new YRequestCallback<PicCodeBean>() {
           @Override
           public void onSuccess(PicCodeBean var1) {
               mStateView.showContent();
               commit.setEnabled(true);
               startActivity(ChangeSuccessActivity.class);
           }

           @Override
           public void onFailed(String var1, String message) {
               mStateView.showContent();
               commit.setEnabled(true);
               ErrorUtils.error(mContext,var1,message);
           }

           @Override
           public void onException(Throwable var1) {
               mStateView.showContent();
               commit.setEnabled(true);
           }
       });

    }
}

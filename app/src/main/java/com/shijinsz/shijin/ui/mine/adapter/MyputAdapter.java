package com.shijinsz.shijin.ui.mine.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.ad.PayActivity;
import com.shijinsz.shijin.ui.mine.RenewActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.List;

/**
 * Created by yrdan on 2018/8/14.
 */

public class MyputAdapter extends BaseQuickAdapter<Ads,BaseViewHolder> {
    public MyputAdapter(int layoutResId, @Nullable List<Ads> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ads item) {
        ImageView img = helper.getView(R.id.img);
        Button renew = helper.getView(R.id.tv_renew);
        GlideApp.with(mContext).load(item.getAd_title_pics().get(0)).into(img);
        helper.setText(R.id.tv_content,item.getAd_title());
        helper.setText(R.id.tv_time, item.getCreated_at());
        helper.setText(R.id.money,item.getChange());
        switch (item.getAdshow()){
            case "topay":
                helper.setText(R.id.tv_status,mContext.getString(R.string.topay));
                helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.colorPrimary));
                renew.setText(mContext.getString(R.string.pay));
                helper.setGone(R.id.money,true);
                renew.setEnabled(true);
                renew.setBackgroundResource(R.drawable.bt_buy);
                break;
            case "pending":
                helper.setText(R.id.tv_status,mContext.getString(R.string.under_review));
                helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.colorPrimary));
                renew.setText(mContext.getString(R.string.renew));
                helper.setGone(R.id.money,true);
                renew.setEnabled(false);
                renew.setBackgroundResource(R.drawable.bt_nobuy);
                break;
            case "on":
                helper.setText(R.id.tv_status,mContext.getString(R.string.success_review));
                helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.text_999999));
                renew.setText(mContext.getString(R.string.renew));
                helper.setGone(R.id.money,true);
                renew.setEnabled(true);
                renew.setBackgroundResource(R.drawable.bt_buy);
                break;
            case "back":
                helper.setText(R.id.tv_status,mContext.getString(R.string.fail_review));
                helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.colorPrimary));
                renew.setText(mContext.getString(R.string.change_put));
                helper.setGone(R.id.money,false);
                renew.setEnabled(true);
                renew.setBackgroundResource(R.drawable.bt_buy);
                break;
            case "renew":
                helper.setText(R.id.tv_status,mContext.getString(R.string.renew));
                helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.colorPrimary));
                renew.setText(mContext.getString(R.string.renew));
                helper.setGone(R.id.money,true);
                renew.setEnabled(true);
                renew.setBackgroundResource(R.drawable.bt_buy);
                break;
            case "off":
                helper.setText(R.id.tv_status,mContext.getString(R.string.down_off));
                helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.colorPrimary));
                renew.setText(mContext.getString(R.string.renew));
                helper.setGone(R.id.money,false);
                renew.setEnabled(false);
                renew.setBackgroundResource(R.drawable.bt_nobuy);
                break;
        }
        helper.setOnClickListener(R.id.tv_renew, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getAdshow()){
                    case "topay":
                        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                            ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                            return;
                        }
                        Intent intent3 =new Intent(mContext,PayActivity.class);
                        intent3.putExtra("id",item.getId());
                        intent3.putExtra("money",item.getTotal_number());
                        intent3.putExtra("num",item.getPeopel_number());
                        mContext.startActivity(intent3);
                        break;
                    case "pengding":
                        break;
                    case "on":
                        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                            ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                            return;
                        }
                        Intent intent2 =new Intent(mContext,RenewActivity.class);
                        intent2.putExtra("id",item.getId());
                        mContext.startActivity(intent2);
                        break;
                    case "back":
                        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                            ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                            return;
                        }
                        new DialogUtils(mContext).showBackDialog(item);
                        break;
                    case "renew":
                        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("disable")){
                            ToastUtil.showToast("该账号已被禁用，请联系十金客服");
                            return;
                        }
                        Intent intent =new Intent(mContext,RenewActivity.class);
                        intent.putExtra("id",item.getId());
                        mContext.startActivity(intent);
                        break;
                    case "off":
                        break;
                }
            }
        });
    }
}

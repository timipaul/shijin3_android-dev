package com.shijinsz.shijin.ui.wallet.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.PointDetailBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by yrdan on 2018/8/29.
 */

public class PointDetailApater extends BaseQuickAdapter<PointDetailBean,BaseViewHolder> {

    public PointDetailApater(int layoutResId, @Nullable List<PointDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointDetailBean item) {
        helper.setText(R.id.tv_content,item.getChannel_info().getTitle());
        helper.setText(R.id.tv_time, item.getCreated_at());

        if(item.getPoints() == null){
            helper.setTextColor(R.id.tv_num,mContext.getResources().getColor(R.color.black));
            helper.setOnClickListener(R.id.tv_num,null);
            String num = Float.valueOf(item.getChange())+"";
            if (Float.valueOf(num)>0){
                helper.setText(R.id.tv_num,"+"+num);
            }else{
                helper.setText(R.id.tv_num,""+num);
            }

        }else if(item.getPoints().equals("-1")){
            //联系客服
            helper.setText(R.id.tv_num,"联系客服");
            helper.setTextColor(R.id.tv_num,mContext.getResources().getColor(R.color.orange));
            helper.setOnClickListener(R.id.tv_num, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkApkExist(mContext, "com.tencent.mobileqq")) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + mContext.getString(R.string.QQNUM) + "&version=1")));
                    } else {
                        Toast.makeText(mContext, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else if(item.getPoints().equals("-2")){
            //已抢
            helper.setText(R.id.tv_num,"已抢");
        }else{
            helper.setTextColor(R.id.tv_num,mContext.getResources().getColor(R.color.black));
            helper.setOnClickListener(R.id.tv_num,null);
            String num="";
            if (item.getPoints()!=null){
                num=Integer.parseInt(item.getPoints())+"";
                if (Float.valueOf(num)>0){
                    helper.setText(R.id.tv_num,"+"+item.getPoints());
                }else {
                    helper.setText(R.id.tv_num,item.getPoints());
                }
            }

        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}

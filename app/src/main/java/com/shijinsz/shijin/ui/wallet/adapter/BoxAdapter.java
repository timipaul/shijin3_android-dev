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
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.model.model.bean.BoxBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.task.InviteFriendActivity;
import com.shijinsz.shijin.ui.task.SignInActivity;

import java.util.List;

/**
 * Created by yrdan on 2018/8/30.
 */

public class BoxAdapter extends BaseQuickAdapter<BoxBean,BaseViewHolder> {
    public BoxAdapter(int layoutResId, @Nullable List<BoxBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BoxBean item) {
        helper.setText(R.id.tv_name,item.getLottery_name());
        helper.setText(R.id.tv_description,"有效期至"+ TimeUtil.format(Long.valueOf(item.getExpire_time())*1000,"yyyy-MM-dd"));
        helper.setOnClickListener(R.id.tv_contact_service, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkApkExist(mContext, "com.tencent.mobileqq")) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + mContext.getString(R.string.QQNUM) + "&version=1")));
                } else {
                    Toast.makeText(mContext, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        helper.setOnClickListener(R.id.tv_use, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getLottery_code().equals("patch_card")){
                    mContext.startActivity(new Intent(mContext, SignInActivity.class));
                }else if (item.getLottery_code().equals("invitation_card")){
                    mContext.startActivity(new Intent(mContext, InviteFriendActivity.class));
                }
            }
        });
        switch (item.getLottery_mode()){
            case "staff":
                switch (item.getLottery_status()){
                    case "used":
                        helper.setGone(R.id.img_used,true);
                        helper.setGone(R.id.tv_use,false);
                        helper.setGone(R.id.tv_contact_service,false);
                        helper.setGone(R.id.img_expired,false);
                        break;
                    case "contact_service":
                        helper.setGone(R.id.img_used,false);
                        helper.setGone(R.id.tv_use,false);
                        helper.setGone(R.id.tv_contact_service,true);
                        helper.setGone(R.id.img_expired,false);
                        break;
                    case "overdue":
                        helper.setGone(R.id.img_used,false);
                        helper.setGone(R.id.tv_use,false);
                        helper.setGone(R.id.tv_contact_service,false);
                        helper.setGone(R.id.img_expired,true);
                        break;
                }
                break;
            case "oneself":
                switch (item.getLottery_status()){
                    case "used":
                        helper.setGone(R.id.img_used,true);
                        helper.setGone(R.id.tv_use,false);
                        helper.setGone(R.id.tv_contact_service,false);
                        helper.setGone(R.id.img_expired,false);
                        break;
                    case "use":
                        helper.setGone(R.id.img_used,false);
                        helper.setGone(R.id.tv_use,true);
                        helper.setGone(R.id.tv_contact_service,false);
                        helper.setGone(R.id.img_expired,false);
                        break;
                    case "overdue":
                        helper.setGone(R.id.img_used,false);
                        helper.setGone(R.id.tv_use,false);
                        helper.setGone(R.id.tv_contact_service,false);
                        helper.setGone(R.id.img_expired,true);
                        break;
                }
                break;
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

package com.hongchuang.ysblibrary.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.hongchuang.hclibrary.utils.TextUtil;


/***
 * 功能描述:Dialog实例管理
 * 作者:qiujialiu
 * 时间:2016/11/28
 * 版本:1.0
 ***/

public class BaseDialogManager {
    public static final int LEFT_BUTTON = YunBaseDialog.Builder.LEFT_BUTTON;
    public static final int RIGHT_BUTTON = YunBaseDialog.Builder.RIGHT_BUTTON;
    private static BaseDialogManager instance;
    private YunBaseDialog mDialog;

    private BaseDialogManager() {
    }

    public static synchronized BaseDialogManager getInstance() {
        if (instance == null) {
            instance = new BaseDialogManager();
        }
        return instance;
    }

    public static void showTip(Activity context, String title, String message) {
        YunBaseDialog.Builder builder = getInstance().getBuilder(context)
                .setMessage(message)
                .setRightButtonText("我知道了")
                .setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (TextUtil.isNotEmpty(title)) {
            builder.setTitle(title).isHaveTitle(true);
        } else {
            builder.isHaveTitle(false);
        }
        builder.create().show();
    }

    public Builder getBuilder(Activity context) {
        return new Builder(context);
    }

    public Builder getBuilder(Context context) {
        return new Builder(context);
    }

    public class Builder extends YunBaseDialog.Builder {
        public Builder(Context context) {
            super(context);
        }

        public YunBaseDialog createNewDialog() {
            mDialog = super.create();
            return mDialog;
        }

        @Override
        public YunBaseDialog create() {
            if (mDialog != null && mDialog.isShowing()) {
                try {
                    mDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mDialog = super.create();
            return mDialog;
        }

        @Override
        public YunBaseDialog.Builder setOnClickListener(DialogInterface.OnClickListener listener) {
            return super.setOnClickListener(listener);
        }
    }
}

package com.hongchuang.ysblibrary.common.toast;


/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/4/24
 * 版本:1.0
 ***/

public class ToastManager {
    private ToastDailog mDialog;

    private ToastManager() {
    }

    public static ToastManager getInstance() {
        return ToastManagerHolder.manager;
    }

    public void showToast(String text) {
//        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
//        mDialog = new ToastDailog(HuiDengApplication.getInstance().getTopActivity(), R.style.ToastDialog);
//        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.setText(text);
//        Window win = mDialog.getWindow();
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.y = 250;//设置y坐标
//        win.setAttributes(params);
//        mDialog.show();
        ToastUtil.showToast(text);
    }


    private static class ToastManagerHolder {
        public static final ToastManager manager = new ToastManager();
    }
}

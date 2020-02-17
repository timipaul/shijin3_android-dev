package com.xiqu.sdklibrary.view;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.xiqu.sdklibrary.util.AppUtil;

import java.io.File;

import static com.xiqu.sdklibrary.view.DownLoadService.SP_TASK_APKNAME;
import static com.xiqu.sdklibrary.view.DownLoadService.SP_TASK_ID;

public class DownLoadReceiver extends BroadcastReceiver {
    private long mTaskId;
    private String apkName = "";
    private Context mContext;
    private String downloadPath;
    private File file;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        mTaskId = AppUtil.getValue(context.getApplicationContext(), SP_TASK_ID, 0);
        apkName = AppUtil.getValue(context.getApplicationContext(), SP_TASK_APKNAME, "");

        if (myDwonloadID != -1) {
            mTaskId = myDwonloadID;
            if (AppUtil.getDownloadStatus(mContext, mTaskId) == DownloadManager.STATUS_SUCCESSFUL) {
                apkName = AppUtil.getNameByTaskId(context, mTaskId);
                if (!TextUtils.isEmpty(apkName)) {
                    downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xiqu" + File.separator + apkName;
                    file = new File(downloadPath);
                    if (file.exists()) {
                        AppUtil.installAPK(context, file);
                    }
                }
            }
        }
    }
}

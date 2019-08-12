package com.shijinsz.shijin.utils;

/**
 * Created by yrdan on 2018/11/20.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.hongchuang.hclibrary.utils.AndroidSystemUtil;
import com.shijinsz.shijin.BuildConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载APK的异步任务
 */

public class DownloadAPK extends AsyncTask<String, Integer, String> {
    ProgressDialog progressDialog;
    File file;
    Context context;

    public onDownloadAPK getDownloadAPK() {
        return downloadAPK;
    }

    public void setDownloadAPK(onDownloadAPK downloadAPK) {
        this.downloadAPK = downloadAPK;
    }

    public onDownloadAPK downloadAPK;
    public interface onDownloadAPK{
        void onStart();
        void onFinish();
        void onOpen();
    }
    public DownloadAPK(Context context,ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url;
        HttpURLConnection conn;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;

        try {
            url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            bis = new BufferedInputStream(conn.getInputStream());
            int fileLength = conn.getContentLength();
            String fileName = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .getAbsolutePath() + "/magkare/action.apk";
            file = new File(fileName);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            byte data[] = new byte[4 * 1024];
            long total = 0;
            int count;
            while ((count = bis.read(data)) != -1) {
                total += count;
                publishProgress((int) (total * 100 / fileLength));
                fos.write(data, 0, count);
                fos.flush();
            }
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        openFile(file);
        progressDialog.dismiss();
        if (downloadAPK!=null){
            downloadAPK.onFinish();
            downloadAPK.onOpen();
        }
    }

    private void openFile(File file) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(activity,"包名.fileprovider", takeImageFile));
//            } else {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takeImageFile));
//            }
        if (file != null) {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(),
                    PackageManager.GET_ACTIVITIES);
            if (info != null) {
                String packageName = info.packageName;
                int versionCode = info.versionCode;
                Log.i("APKNAME", "openFile: "+packageName);
                AndroidSystemUtil androidSystemUtil=new AndroidSystemUtil(context);
                if (androidSystemUtil.isAppInstalled(packageName)==1) {
                    Intent intent = pm.getLaunchIntentForPackage(packageName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return;
                }
            }
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                startActivity(intent);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                // 声明需要的零时权限
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 第二个参数，即第一步中配置的authorities
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        }

    }
}

package com.hongchuang.ysblibrary.utils;

import android.app.Activity;
import android.os.Environment;

import com.hongchuang.hclibrary.manager.UpdateManager;
import com.hongchuang.hclibrary.storage.StorageManager;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/12/4
 ***/

public class AppUpdateManager {
    private UpdateManager manager;
    public AppUpdateManager(Activity context) {
        manager = UpdateManager.newInstance();
    }

    public AppUpdateManager() {
        manager = UpdateManager.newInstance();
    }

//    public void checkVersion(final YRequestCallback<AppInfoBean> callback) {
//        Network.getApi(IWPHApi.class)
//                .checkAppVersion()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<YunShlResult<AppInfoBean>>() {
//                    @Override
//                    public void call(YunShlResult<AppInfoBean> result) {
//                        if (result.isSuccess()) {
//                            if (callback != null) {
//                                callback.onSuccess(result.body);
//                            }
//                        }else {
//                            if (callback != null) {
//                                callback.onFailed(result.status,result.msg);
//                            }
//                        }
//                    }
//                }, new YunShlAction<Throwable>(callback));
//    }

    public void downloadApp(String url, UpdateManager.IDownloadStatus downloadStatus) {
        manager.setDownloadStatus(downloadStatus);
        manager.downLoad(url, getAppDir(),"apk");
//        manager.downLoad(url, StorageManager.getFileDir()+"/app","apk");
    }

    private static boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    private static String getAppDir() {

        if (hasSDCard) { // SD卡根目录的hello.text
            return Environment.getExternalStorageDirectory() + "/CarSystem";
        } else {  // 系统下载缓存根目录的hello.text
            return Environment.getDownloadCacheDirectory() + "/CarSystem";
        }
    }

    public void download(String url, UpdateManager.IDownloadStatus downloadStatus) {
        manager.setDownloadStatus(downloadStatus);
        manager.downLoad(url, StorageManager.getMediaPath(),url.substring(url.lastIndexOf(".")+1));
    }

    public void download(String url, String path, UpdateManager.IDownloadStatus downloadStatus) {
        manager.setDownloadStatus(downloadStatus);
        manager.downLoad(url, StorageManager.getMediaPath(),url.substring(url.lastIndexOf(".")+1));
    }

//    public static boolean haveNewVersion(AppInfoBean appInfoBean) {
//        if (appInfoBean == null) {
//            return false;
//        }else {
//            String version = DevicesUtil.getVersion(WPHLibrary.getLibrary().getContext());
//            if (version.compareTo(appInfoBean.getVersion()) < 0) {
//                return true;
//            }
//            if (version.compareTo(appInfoBean.getVersion()) == 0) {
//                StorageManager.deleteDirectory(StorageManager.getFileDir()+"/app");
//            }
//        }
//        return false;
//    }


}

package com.hongchuang.hclibrary.manager;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.hclibrary.utils.TextUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Thread.sleep;

/**
 * Created by Samj on 2016/5/17.
 */
public class UpdateManager {
    private String updatePath = "";
    private static final String TAG = "UpdateManager";
    private File checkFile;
    private Thread downloadThread;
    private IDownloadStatus downloadStatus;
    private RandomAccessFile downloadingFile = null;
    private Long lastLangth = 0L;
    private boolean finishDwonload = false;
    private long contentLength;
    private int countTime = 0;
    private int currentRate;
    private boolean downloading = false;
    private boolean updateLogSuccess = false;
    private String downUrl;

    private void setContentLength(long length) {
        this.contentLength = length;
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (contentLength > 100) {
                        if (!finishDwonload) {
                            try {
                                if (downloadingFile != null) {
                                    if ((downloadingFile.length() - lastLangth) > 0) {
                                        countTime++;
                                        if (countTime == 5) {
                                            currentRate = (int) ((downloadingFile.length() - lastLangth) / 10);
                                            lastLangth = downloadingFile.length();
                                            countTime = 0;
                                        }
                                        float progress = (float) downloadingFile.length() / (float) contentLength;
                                        int progressInt = (int) (progress * 1000);
                                        int second = (int) ((contentLength - downloadingFile.length()) / (currentRate + 1));
                                        if (downloadStatus != null && second >= 0) {
                                            downloadStatus.downLoading(progressInt, second);
                                        }
                                        Log.d("UpdateManager", "downloading progress=" + progressInt + "\n---lastLength=" + lastLangth + "\n----"
                                                + "currentRate=" + currentRate + "\n-----second=" + second + "\n-----fileLength=" + downloadingFile.length());
                                        handler.sendEmptyMessageDelayed(1, 500);
                                    } else {
                                        handler.sendEmptyMessageDelayed(1, 500);
                                    }
                                } else {
                                    if (downloadStatus != null) {
                                        downloadStatus.downLoadError("downing io exception");
                                        finishDwonload = false;
                                        if (checkFile.exists()) {
                                            checkFile.delete();
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                Log.e("UpdateManager", "downing io exception");
                                e.printStackTrace();
                                if (downloadStatus != null) {
                                    downloadStatus.downLoadError("downing io exception");
                                    finishDwonload = false;
                                    if (checkFile.exists()) {
                                        checkFile.delete();
                                    }
                                }
                            }
                        } else {
                            handler.removeMessages(1);
                        }
                    }
                    break;
            }
        }
    };

    public boolean isDownloading(){
        return downloading;
    }

    public void setDownLoading(boolean downLoading){
        this.downloading = downLoading;
    }


    public interface IDownloadStatus{
        void downloadFinish(String fileName) throws IOException;
        void downloadPause();
        void downloadStart(long fileLength);
        void downLoading(int progress, int second);
        void downLoadError(String message);
    }

    private UpdateManager(){}

    public static UpdateManager newInstance(){
        return new UpdateManager();
    }

    public void setDownloadStatus(IDownloadStatus downloadStatus){
        this.downloadStatus = downloadStatus;
    }

    public void releaseDownloadStatus(){
        this.downloadStatus = null;
    }

    public void downLoad(final String url, final String path, final String suffix) {
        File fileUpload = new File(path);
        if (!fileUpload.exists()) {
            boolean successB = fileUpload.mkdirs();
            if (!successB) {
                if (downloadStatus != null) {
                    downloadStatus.downLoadError("目标地址有误，请检查地址或确认是否开启文件存取权限");
                }
                return;
            }
        }
        updatePath = path;
        downUrl = url;
        finishDwonload = false;
        if (downloadStatus!=null){
            downloadStatus.downloadStart(0);
        }
         Log.e("download", "start download");
                downloadThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            downloadingFile = new RandomAccessFile(updatePath+ File.separator+ TextUtil.md5Lower(url)+(TextUtil.isEmpty(suffix)?"":("."+suffix)), "rw");
                            URL url1 = null;
                            url1 = new URL(url);
                            HttpURLConnection connection = null;
                            connection = (HttpURLConnection) url1.openConnection();
                            connection.setConnectTimeout(15000);
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("Connection", "Keep-Alive");
                            connection.setRequestProperty("User-Agent", "NetFox");
                            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                                connection.setRequestProperty("Connection", "close");
                            }
                            long thisFileLenght = 0;
                            thisFileLenght = downloadingFile.length();
                            try {
                                contentLength = connection.getContentLength();
                            } catch (Exception e) {
                                e.printStackTrace();
                                downLoadNoLength(url, path, suffix);
                                return;
                            }
                            if (contentLength > 0) {
                                if (thisFileLenght >= contentLength) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            finishDownload(url, suffix);
                                        }
                                    });
                                    return;
                                } else {
                                    downloadingFile.seek(thisFileLenght);
                                }
                                final long finalThisFileLenght = thisFileLenght;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (downloadStatus != null) {
                                            downloadStatus.downloadStart(finalThisFileLenght);
                                        }
                                    }
                                });
                                if (thisFileLenght > 0) {
                                    connection.setRequestProperty("RANGE", "bytes=" + thisFileLenght + "-");
                                }
                            }else {
                                downloadingFile.seek(0);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (downloadStatus != null) {
                                            downloadStatus.downloadStart(0);
                                        }
                                    }
                                });
                            }
                            InputStream inputStream = null;
                            inputStream = connection.getInputStream();
                            handler.removeMessages(1);
                            handler.sendEmptyMessageDelayed(1,500);
                            byte[] buffer = new byte[1024];
                            int lenght;
                            while ((lenght = inputStream.read(buffer, 0, 1024)) > 0) {
                                downloadingFile.write(buffer, 0, lenght);
                                downloading = true;
                            }
                            handler.removeMessages(2);
                            handler.removeMessages(1);
                            try{
                                sleep(200);
                                inputStream.close();
                            }catch (Exception e){
                                Log.e("UpdateManager", "sleep or close stream exception");
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finishDownload(url, suffix);

                                }
                            }, 1000);
                        } catch (final Exception e) {
                            e.printStackTrace();
                            Log.e("UpdateManager","exception message:"+e.getMessage());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (e.getMessage().contains("Connection timed out")){
                                        downLoad(url, path, suffix);
                                    }else {
                                        if (downloadStatus != null) {
                                            downloadStatus.downLoadError("下载失败，未知错误");
                                            downloading = false;
                                        }
                                    }
                                }
                            });
                        }

                    }
                };
                downloadThread.start();
    }

    public void downLoadNoLength(final String url, final String path, final String suffix) {
        File fileUpload = new File(path);
        if (!fileUpload.exists()) {
            boolean successB = fileUpload.mkdirs();
            if (!successB) {
                if (downloadStatus != null) {
                    downloadStatus.downLoadError("目标地址有误，请检查地址或确认是否开启文件存取权限");
                }
                return;
            }
        }
        updatePath = path;
        downUrl = url;
        finishDwonload = false;
        if (downloadStatus!=null){
            downloadStatus.downloadStart(0);
        }
        Log.e("download", "start download");
        final Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                // 下载失败
                if (e != null) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (e!=null && e.getMessage()!=null && e.getMessage().contains("Connection timed out")){
                            downLoad(url, path, suffix);
                        }else {
                            if (downloadStatus != null) {
                                downloadStatus.downLoadError("下载失败，未知错误");
                                downloading = false;
                            }
                        }
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream inputStream;
                inputStream = response.body().byteStream();
                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1,500);
                byte[] buffer = new byte[1024];
                int lenght;
                while ((lenght = inputStream.read(buffer, 0, 1024)) > 0) {
                    downloadingFile.write(buffer, 0, lenght);
                    downloading = true;
                }
                handler.removeMessages(2);
                handler.removeMessages(1);
                try{
                    sleep(200);
                    inputStream.close();
                }catch (Exception e){
                    Log.e("UpdateManager", "sleep or close stream exception");
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishDownload(url, suffix);

                    }
                }, 1000);
            }
        });
    }

    private void finishDownload(String url, String suffix) {
        checkFile = new File(updatePath, TextUtil.md5Lower(url)+(TextUtil.isEmpty(suffix)?"":("."+suffix)));
        if (checkFile.exists()) {
            LogUtils.d("UpdateManager", "download finish checkFile file size = " + checkFile.length());
        }else {
            LogUtils.d("UpdateManager", "download finish checkFile is not exit");
            if (downloadStatus != null) {
                downloadStatus.downLoadError("download finish checkFile is not exit");
                downloading = false;
                return;
            }
        }
        if (checkFileColplete(checkFile, "check")) {
            LogUtils.d("UpdateManager", "download finish success");
            if (downloadStatus != null) {
                try {
                    downloadStatus.downloadFinish(checkFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                downloading = false;
            }
            finishDwonload = true;
        } else {
            LogUtils.d("UpdateManager", "download finish error");
            if (downloadStatus != null) {
                downloadStatus.downLoadError("完整性校验失败");
                downloading = false;
            }
            finishDwonload = false;
            if (checkFile.exists()) {
                checkFile.delete();
            }
        }
    }

    private static boolean checkFileColplete(File file, String checkStr){
        return true;
    }

}

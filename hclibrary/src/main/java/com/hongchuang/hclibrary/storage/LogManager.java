package com.hongchuang.hclibrary.storage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hongchuang.hclibrary.permission.MPermission;
import com.hongchuang.hclibrary.utils.TimeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/***
 * 功能描述: log日志管理
 * 作者:qiujialiu
 * 时间:2016/8/3
 * 版本:
 ***/
public class LogManager {
    private static final int NEXT = 101;
    private static final char VERBOSE = 'v';
    private static final char DEBUG = 'd';
    private static final char INFO = 'i';
    private static final char WARN = 'w';
    private static final char ERROR = 'e';
    private static String TAG = "LogManager";
    private static String logPath = null;//log日志存放路径
    private StringBuilder stringBuilder;
    private Queue<LogClass> logQueue;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT:
                    if (logQueue != null && logQueue.size() > 0) {
                        LogClass logClass = logQueue.poll();
                        stringBuilder.append("\r\n").append(TimeUtil.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")).append("\r\n").append(logClass.tag).append("\r\n").append(logClass.msg);
                        if (stringBuilder.length() >= 1024) {
                            writeLog(stringBuilder.toString());
                            mHandler.removeMessages(NEXT);
                            mHandler.sendEmptyMessageDelayed(NEXT, 1500);
                            stringBuilder.delete(0, stringBuilder.length());
                        } else if (logQueue.size() == 0) {
                            writeLog(stringBuilder.toString());
                            mHandler.removeMessages(NEXT);
                            mHandler.sendEmptyMessageDelayed(NEXT, 1500);
                            stringBuilder.delete(0, stringBuilder.length());
                        } else {
                            mHandler.removeMessages(NEXT);
                            mHandler.sendEmptyMessageDelayed(NEXT, 200);
                        }
                    }
                    break;
            }
        }
    };

    private LogManager() {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Build.BRAND).append(" ").append(Build.MODEL).append("\r\n");
        stringBuilder.append("android version ").append(Build.VERSION.RELEASE).append("\r\n");
    }

    /**
     * 初始化，须在使用之前设置，最好在Application创建时调用
     *
     * @param context
     */
    public static void init(Context context) {
        if (MPermission.havePermission(context, MPermission.Type.PERMISSION_STORAGE)) {
            logPath = StorageManager.getLogPath();//获得文件储存路径,在后面加"/Logs"建立子文件夹
        } else {
            logPath = context.getFilesDir() + "/logs";//获得文件储存路径,在后面加"/Logs"建立子文件夹
        }
    }

    public static LogManager getInstance() {
        return LogManagerHolder.INSTANCE;
    }

    /**
     * 获得文件存储路径
     *
     * @return
     */
    private static String getFilePath(Context context) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
            return context.getExternalFilesDir(null).getPath();//获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.waka.workspace.logtofile/files/Logs/log_2016-03-14_16-15-09.log
        } else {
            return context.getFilesDir().getPath();//直接存在/data/data里，非root手机是看不到的
        }
    }

    /**
     * 将log信息写入文件中
     *
     * @param msg
     */
    private static void writeLog(String msg) {
        final String message = msg;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //如果父路径不存在
                File file = new File(logPath);
                if (!file.exists()) {
                    file.mkdirs();//创建父路径
                }
                File file1 = new File(logPath, "wanpinghui.txt");
                if (file1.exists()) {
                    if (file1.length() > (6291456 * 2)) {//当文件大于12M时
                        delete(file1);
                        try {
                            file1.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        file1.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos;//FileOutputStream会自动调用底层的close()方法，不用关闭
                BufferedWriter bw = null;
                try {
                    fos = new FileOutputStream(file1, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
                    bw = new BufferedWriter(new OutputStreamWriter(fos));
                    bw.write(message);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();//关闭缓冲流
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        if (null == logPath) {
            Log.e(TAG, "logPath == null ，未初始化LogToFile");
        }
    }

    private static void delete(File file1) {
        try {
            file1.delete();
//            File tmpFile = new File(file1.getParent(), "logTmp");
//            if (!tmpFile.exists()) {
//                tmpFile.createNewFile();
//            }
//            FileInputStream fileInputStream  = new FileInputStream(file1);
//            FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
//            //fileOutputStream.write(fileInputStream.);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void v(String tag, String msg) {
        push(VERBOSE, tag, msg);
    }

    public void d(String tag, String msg) {
        push(DEBUG, tag, msg);
    }

    public void i(String tag, String msg) {
        push(INFO, tag, msg);
    }

    public void w(String tag, String msg) {
        push(WARN, tag, msg);
    }

    public void e(String tag, String msg) {
        push(ERROR, tag, msg);
    }

    private void push(final char type, final String tag, final String msg) {
        if (logQueue == null) {
            logQueue = new LinkedBlockingQueue();
        }
        logQueue.add(new LogClass(type, tag, msg));
        if (logQueue.size() == 1) {
            mHandler.removeMessages(NEXT);
            mHandler.sendEmptyMessageDelayed(NEXT, 1000);
        }
    }

    private static class LogManagerHolder {
        public static final LogManager INSTANCE = new LogManager();
    }

    class LogClass {
        char type = 'd';
        String tag = "WanPingHui";
        String msg;

        public LogClass(char type, String tag, String msg) {
            this.type = type;
            this.tag = tag;
            this.msg = msg;
        }
    }

}



package com.shijinsz.shijin.base;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.SecurityInit;
import com.alibaba.wireless.security.jaq.SecurityVerification;
import com.baidu.mapapi.SDKInitializer;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.utils.LogUtils;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.Constants;
import com.hongchuang.ysblibrary.common.GrobalListener;
import com.hongchuang.ysblibrary.dao.DaoMaster;
import com.hongchuang.ysblibrary.dao.DaoSession;
import com.hongchuang.ysblibrary.dao.MySQLiteOpenHelper;
import com.meituan.android.walle.WalleChannelReader;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.jpush.android.api.JPushInterface;
import iknow.android.utils.BaseUtils;

/**
 * Created by tg596 on 2018/3/17.
 */

public class MyApplication extends MultiDexApplication implements GrobalListener {

    private Activity topActivity;
    private MySQLiteOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SecurityVerification securityVerification = null;
    //静态单例
    public static MyApplication instances;

    private boolean isDebug=false;//debug模式
    @Override
    public void onCreate() {
        super.onCreate();
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        if (channel==null||channel.equals("debug")) {
            isDebug=true;
        }else {
            isDebug=false;
        }
        //AdHub.initialize(this,"277");//2522
        LogUtils.setDEBUG(isDebug);
        ShareDataManager.getInstance().init(this, "app_data");
        UMConfigure.setLogEnabled(isDebug);
        Log.e("channel", "onCreate: "+ channel);
        UMConfigure.init(this,"5b98d6a7f43e480a7a000062",channel,UMConfigure.DEVICE_TYPE_PHONE,null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.setSecret(this,);
        YSBSdk.init(this);
        MobSDK.init(this);
        TCAgent.LOG_ON=isDebug;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this,"BEC37202538042D897558032A777EC09",channel);
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true);
        if (!"generic".equalsIgnoreCase(Build.BRAND)) {
            SDKInitializer.initialize(getApplicationContext());
        }
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String s) {
                LogUtils.e("bugly","下载地址"+s);
            }

            @Override
            public void onDownloadReceived(long l, long l1) {
                LogUtils.e("bugly","进度："+l*100+"/"+l1);
            }

            @Override
            public void onDownloadSuccess(String s) {
                LogUtils.e("bugly","补丁下载成功");

            }

            @Override
            public void onDownloadFailure(String s) {
                LogUtils.e("bugly","补丁下载失败");

            }

            @Override
            public void onApplySuccess(String s) {
                LogUtils.e("bugly","补丁应用成功");

            }

            @Override
            public void onApplyFailure(String s) {
                LogUtils.e("bugly","补丁应用失败"+s);

            }

            @Override
            public void onPatchRollback() {
                LogUtils.e("bugly","补丁回滚");

            }
        };

        //公司qq注册 热跟新插件
        Bugly.init(getApplicationContext(), Constants.BuglyAppId, isDebug);


        if (channel!=null&&channel.equals("debug")) {
            Bugly.setIsDevelopmentDevice(this, true);
        }
        JPushInterface.setDebugMode(isDebug);
        JPushInterface.init(this);
        try {
            CopySqliteFileFromRawToDatabases("wuchesq.db");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SecurityInit.Initialize(getApplicationContext());
            securityVerification = new SecurityVerification(getApplicationContext());
        } catch (JAQException e) {
            e.printStackTrace();
        }
        activityLifeObserver();
        BaseUtils.init(this);
        initImageLoader(this);
        initFFmpegBinary(this);
//        initImageLoader();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        Beta.installTinker();
    }

    public static void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 10);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCache(new LRULimitedMemoryCache(memoryCacheSize))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void initFFmpegBinary(Context context) {

        try {
            FFmpeg.getInstance(context).loadBinary(new LoadBinaryResponseHandler() {
                @Override public void onFailure() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }
    public static MyApplication getInstances(){
        return instances;
    }

    /**
     * 设置greenDao
     */
//    private void setDatabase() {
//        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
//        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
//        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
//        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper = new MySQLiteOpenHelper(this, "hc_db", null);
//        db = mHelper.getWritableDatabase();
//        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        mDaoMaster = new DaoMaster(db);
//        mDaoSession = mDaoMaster.newSession();
//    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

    private void initImageLoader() {
//        设置证书
//        这个不是必须的,当你没有证书的时候,就不需要获取,如果有就拿到InputStream,传到下面的getOkHttpClient()的方法中
//        try {
//            InputStream[] InputStream = new InputStream[1];
//            InputStream input = getAssets().open("CA_.cer");
//            InputStream[0] = input;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        让Glide能用HTTPS
//        GlideApp.get(getApplicationContext()).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpsUtils.getOkHttpClient()));

    }


    /**
     * Activity生命周期记录
     */
    public void activityLifeObserver() {
        registerActivityLifecycleCallbacks(
                new ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(
                            Activity activity, Bundle bundle) {
                        MActivityManager.getInstance().addACT(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        topActivity = activity;
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                    }

                    @Override
                    public void onActivitySaveInstanceState(
                            Activity activity, Bundle bundle) {
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        MActivityManager.getInstance().remove(activity);
                    }
                });

    }

    @Override
    public void requestFial(int type, int code) {

    }

    @Override
    public void networkLost() {

    }

    @Override
    public void connectError(int type, String code) {

    }

    @Override
    public void networkAvailable() {

    }

    // 复制和加载区域数据库中的数据
    public  String  CopySqliteFileFromRawToDatabases(String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        File dir = new File("data/data/" + "com.shijinsz.shijin" + "/databases");
//        LogUtil.i("!dir.exists()=" + !dir.exists());
//        LogUtil.i("!dir.isDirectory()=" + !dir.isDirectory());

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file= new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream =null;

        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = getAssets().open(SqliteFileName);

//                        MyApplication.getContext().getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len ;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }


            } catch (IOException e) {
                e.printStackTrace();

            }

            finally {
                if (outputStream != null) {

                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return file.getPath();
    }
}

package com.hongchuang.ysblibrary.dao;

import android.app.Application;
import android.content.Context;


/**
 * Created by lenovo on 2017/8/5.
 */

public class CityManager extends Application{
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static CityManager mInstance;
    //单例
    public static CityManager getInstance(Context context){
        if (mInstance==null){
            //保证异步处理安全操作
            synchronized (CityManager.class){
                if (mInstance==null){
                    mInstance=new CityManager(context);
                }
            }
        }
        return mInstance;
    }

    private CityManager(Context context){
        if (mInstance==null){
            DaoMaster.DevOpenHelper openHelper=new DaoMaster.DevOpenHelper(context, "wuchesq.db",null);
            mDaoMaster=new DaoMaster(openHelper.getWritableDatabase());
            mDaoSession=mDaoMaster.newSession();
        }
    }

    public DaoMaster getMaster(){
        return mDaoMaster;
    }
    public DaoSession getSession(){
        return mDaoSession;
    }
    public DaoSession getNewSession(){
        mDaoSession=mDaoMaster.newSession();
        return mDaoSession;
    }
}
package com.hongchuang.hclibrary.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * 功能描述:用户数据共享类，SharePreference存储
 *
 * 这里只作数据的存储与读取
 * 有关业务的数据的存取请另写存取的方法再调用些方法
 *
 * 作者:qiujialiu
 * 时间:2017/05/31
 ***/

public class ShareDataManager {
    private static ShareDataManager instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public static ShareDataManager getInstance() {
        if (instance == null) {
            instance = new ShareDataManager();
        }
        return instance;
    }


    /**
     * init
     *
     * @param mContext
     */
    public void init(Context mContext, String name) {
        if (mContext != null) {
            this.mContext = mContext;
            sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
    }

    /**
     * 清除所有数据
     */
    public void clean() {
        if (editor != null) {
            editor.clear();
            editor.commit();
        }
    }


    public void save(Context context, String key, String value) {
        try {
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Context context, String key, List<String> value){
       try {
           Set<String> listSet = new HashSet<String>(value);
           editor.putStringSet(key,listSet);
           editor.commit();
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    public void save(Context context, String key, long value) {
        try {
            editor.putLong(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void delete(Context context, String key) {
        editor.remove(key);
        editor.commit();
    }

    public String getPara(String key) {
        try {
            if (sp == null) {
                return null;
            } else {
                return sp.getString(key, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public long getParaLong(String key) {
        try {
            if (sp == null) {
                return 0;
            } else {
                return sp.getLong(key, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public List<String> getParaList(String key) {
        try {
            if (sp == null) {
                return null;
            } else {
                List<String> setList = new ArrayList<String>(sp.getStringSet(key, null));
                return setList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

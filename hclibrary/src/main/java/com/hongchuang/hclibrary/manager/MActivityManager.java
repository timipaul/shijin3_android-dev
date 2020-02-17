package com.hongchuang.hclibrary.manager;


import android.app.Activity;

import com.hongchuang.hclibrary.utils.TextUtil;

import java.util.Stack;

/**
 * 功能描述:管理app 中activity
 * 作者:qiujialiu
 * 时间:2016/12/16
 * 版本:
 */
public class MActivityManager {

    private static MActivityManager instance;

    private Stack<Activity> activitys;

    private MActivityManager() {
    }

    public synchronized static MActivityManager getInstance() {
        if (instance == null) {
            instance = new MActivityManager();
        }
        return instance;
    }

    public void addACT(Activity act) {
        if (activitys == null) {
            activitys = new Stack<>();
        }
        activitys.push(act);
    }

    public void delACT(Activity act) {
        if (activitys == null)
            return;
        if (activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); ) {
                Object activity = activitys.get(i);
                if (activity.equals(act)) {
                    try {
                        activitys.remove(i);
                        ((Activity) activity).finish();
                    } catch (Exception e) {
                        i++;
                    }
                } else {
                    i++;
                }
            }
        }
    }

    public void delACT(String name) {
        if (activitys == null)
            return;
        if (activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); ) {
                Object activity = activitys.get(i);
                if (activity.getClass().getName().equals(name)) {
                    ((Activity) activity).finish();
                    activitys.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    public void delACT(Class<? extends Activity> name) {
        if (activitys == null)
            return;
        if (activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); i++) {
                Object activity = activitys.get(i);
                if (activity.getClass().getName().equals(name.getName())) {
                    ((Activity) activity).finish();
                    activitys.remove(i);
                }
            }
        }
    }

    public void delAllActivity() {
        if (activitys != null && activitys.size() > 0) {
            while (activitys.size() > 0) {
                Object activity = activitys.pop();
                ((Activity) activity).finish();
            }
        }
    }

    public void delAllACTWithout(Class<? extends Activity> name) {
        Object current = null;
        if (activitys != null && activitys.size() > 0) {
            while (activitys.size() > 0) {
                Object activity = activitys.pop();
                if (!TextUtil.equals(activity.getClass().getName(), name.getName())) {
                    ((Activity) activity).finish();
                } else {
                    current = activity;
                }
            }
            if (current != null) {
                activitys.push((Activity) current);
            }
        }

    }

    public void remove(Activity act) {
        if (activitys == null)
            return;
        if (activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); ) {
                Object activity = activitys.get(i);
                if (activity.equals(act)) {
                    try {
                        activitys.remove(i);
                    } catch (Exception e) {
                        i++;
                    }
                } else {
                    i++;
                }
            }
        }
    }

    public boolean haveActivity(Class<? extends Activity> name) {
        if (activitys != null && activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); i++) {
                if (TextUtil.equals(activitys.get(i).getClass().getName(), name.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Activity getActivity(Class<? extends Activity> name) {
        if (activitys != null && activitys.size() > 0) {
            for (int i = 0; i < activitys.size(); i++) {
                if (TextUtil.equals(activitys.get(i).getClass().getName(), name.getName())) {
                    return activitys.get(i);
                }
            }
        }
        return null;
    }

    public void deleteTopThis(Class<? extends Activity> name) {
        if (activitys != null && activitys.size() > 0) {
            while (!TextUtil.equals(activitys.peek().getClass().getName(), name.getName())) {
                Object activity = activitys.pop();
                ((Activity) activity).finish();
            }
        }
    }

    /**
     * @param index 如果大于0，返回index位置，小于零，返回逆向的index的绝对值的位置
     * @return
     */
    public Activity getActivityByIndex(int index) {
        if (index >= 0) {
            if (activitys != null && activitys.size() > index) {
                return activitys.get(index);
            } else {
                return null;
            }
        } else {
            if (activitys != null && (activitys.size() - Math.abs(index) >= 0)) {
                return activitys.get(activitys.size() - Math.abs(index));
            } else {
                return null;
            }
        }
    }
}

package com.xiqu.sdklibrary.util;


import android.content.Context;

public class MResource {

    /**
     * 反射获取布局
     *
     * @param context
     * @param className
     * @param resName
     * @return
     */

    public static int getIdByName(Context context, String className, String resName) {
        String packageName = context.getPackageName();
        int id = context.getResources().getIdentifier(resName, className, context.getPackageName());
        if (id > 0) {
            return id;
        }
        try {
            Class r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (Class cls : classes) {
                if (cls.getName().split("\\$")[1].equals(className)) {
                    desireClass = cls;
                    break;
                }
            }
            if (desireClass != null) {
                id = desireClass.getField(resName).getInt(desireClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}


package com.hongchuang.hclibrary.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.hongchuang.hclibrary.permission.annotation.OnMPermissionDenied;
import com.hongchuang.hclibrary.permission.annotation.OnMPermissionGranted;
import com.hongchuang.hclibrary.permission.annotation.OnMPermissionNeverAskAgain;
import com.hongchuang.hclibrary.permission.util.MPermissionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static com.hongchuang.hclibrary.permission.util.MPermissionUtil.getActivity;


public class MPermission {
    private final Object object; // activity or fragment
    private String[] permissions;
    private int requestCode;

    /**
     * ********************* init *********************
     */

    private MPermission(Object object) {
        this.object = object;
    }

    /**
     * ********************* util *********************
     */

    public static List<String> getDeniedPermissions(Activity activity, String[] permissions) {
        return getDeniedPermissions((Object) activity, permissions);
    }

    public static List<String> getDeniedPermissions(Fragment fragment, String[] permissions) {
        return getDeniedPermissions((Object) fragment, permissions);
    }

    private static List<String> getDeniedPermissions(Object activity, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }

        return MPermissionUtil.findDeniedPermissions(getActivity(activity), permissions);
    }

    public static List<String> getNeverAskAgainPermissions(Activity activity, String[] permissions) {
        return getNeverAskAgainPermissions((Object) activity, permissions);
    }

    public static List<String> getNeverAskAgainPermissions(Fragment fragment, String[] permissions) {
        return getNeverAskAgainPermissions((Object) fragment, permissions);
    }

    private static List<String> getNeverAskAgainPermissions(Object activity, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }

        return MPermissionUtil.findNeverAskAgainPermissions(getActivity(activity), permissions);
    }

    public static List<String> getDeniedPermissionsWithoutNeverAskAgain(Activity activity, String[] permissions) {
        return getDeniedPermissionsWithoutNeverAskAgain((Object) activity, permissions);
    }

    public static List<String> getDeniedPermissionsWithoutNeverAskAgain(Fragment fragment, String[] permissions) {
        return getDeniedPermissionsWithoutNeverAskAgain((Object) fragment, permissions);
    }

    private static List<String> getDeniedPermissionsWithoutNeverAskAgain(Object activity, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }

        return MPermissionUtil.findDeniedPermissionWithoutNeverAskAgain(getActivity(activity), permissions);
    }

    public static MPermission with(Activity activity) {
        return new MPermission(activity);
    }

    public static MPermission with(Fragment fragment) {
        return new MPermission(fragment);
    }

    private static void needPermission(Activity activity, int requestCode, String[] permissions) {
        requestPermissions(activity, requestCode, permissions);
    }

    private static void needPermission(Fragment fragment, int requestCode, String[] permissions) {
        requestPermissions(fragment, requestCode, permissions);
    }

    public static void needPermission(Activity activity, int requestCode, String permission) {
        needPermission(activity, requestCode, new String[]{permission});
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission) {
        needPermission(fragment, requestCode, new String[]{permission});
    }

    public static boolean havePermission(Context context, String permission) {
        return Build.VERSION.SDK_INT < M || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(value = M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions) {
        if (!MPermissionUtil.isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return;
        }
        List<String> deniedPermissions = MPermissionUtil.findDeniedPermissions(getActivity(object), permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }
        } else {
            doExecuteSuccess(object, requestCode);
        }
    }

    /**
     * ********************* on result *********************
     */

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private static void requestResult(Object obj, int requestCode, String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            if (MPermissionUtil.hasNeverAskAgainPermission(getActivity(obj), deniedPermissions)) {
                doExecuteFailAsNeverAskAgain(obj, requestCode);
            } else {
                doExecuteFail(obj, requestCode);
            }
        } else {
            doExecuteSuccess(obj, requestCode);
        }
    }

    /**
     * ********************* reflect execute result *********************
     */

    private static void doExecuteSuccess(Object activity, int requestCode) {
        executeMethod(activity, MPermissionUtil.findMethodWithRequestCode(activity.getClass(), OnMPermissionGranted.class, requestCode));
    }

    private static void doExecuteFail(Object activity, int requestCode) {
        executeMethod(activity, MPermissionUtil.findMethodWithRequestCode(activity.getClass(), OnMPermissionDenied.class, requestCode));
    }

    private static void doExecuteFailAsNeverAskAgain(Object activity, int requestCode) {
        executeMethod(activity, MPermissionUtil.findMethodWithRequestCode(activity.getClass(), OnMPermissionNeverAskAgain.class, requestCode));
    }

    /**
     * ********************* reflect execute method *********************
     */

    private static void executeMethod(Object activity, Method executeMethod) {
        executeMethodWithParam(activity, executeMethod);
    }

    private static void executeMethodWithParam(Object activity, Method executeMethod, Object... args) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) {
                    executeMethod.setAccessible(true);
                }
                executeMethod.invoke(activity, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public MPermission permissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public MPermission addRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * ********************* request *********************
     */

    @TargetApi(value = M)
    public void request() {
        requestPermissions(object, requestCode, permissions);
    }

    public abstract class Type {
        /**
         * 读取和写入联系人
         */
        public static final String PERMISSION_CONTACTS = Manifest.permission.WRITE_CONTACTS;
        /**
         * 电话相关
         */
        public static final String PERMISSION_PHONE = Manifest.permission.CALL_PHONE;
        /**
         * 读写日历
         */
        public static final String PERMISSION_CALENDAR = Manifest.permission.WRITE_CALENDAR;
        /**
         * 相机功能
         */
        public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
        /**
         * 传感器相关
         */
        public static final String PERMISSION_SENSORS = Manifest.permission.BODY_SENSORS;
        /**
         * 位置相关
         */
        public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        /**
         * 外部存储器读写
         */
        public static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        /**
         * 麦克风
         */
        public static final String PERMISSION_MICROPHONE = Manifest.permission.RECORD_AUDIO;
        /**
         * 短信相关
         */
        public static final String PERMISSION_SMS = Manifest.permission.SEND_SMS;

    }
}

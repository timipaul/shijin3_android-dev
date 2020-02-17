package retrofit.callback;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/***
 * 功能描述:exception type
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public abstract class FailOrExceptionType {

    public static final int NORMAL_TYPE = 0;

    public static final int NO_LOGIN = 1;//未登录

    public static final int CONNECT_FAIL = 2;//连接失败

    public static final int CONNECT_ERROR= 5;//连接失败

    public static final int NO_RESPONSE = 3;//未响应
    /**
     * 未绑定手机
     */
    public static final int NO_BIND = 5;

    @IntDef({
            NORMAL_TYPE,
            NO_LOGIN,
            CONNECT_FAIL,
            NO_RESPONSE,
            CONNECT_ERROR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FailType {
    }
}

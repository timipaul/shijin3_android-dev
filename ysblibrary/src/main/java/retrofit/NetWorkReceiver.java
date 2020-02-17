package retrofit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hongchuang.ysblibrary.YSBLibrary;

import retrofit.utils.NetWorkUtil;


/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/6/15
 ***/

public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetWorkUtil.isNetworkConnected(context)) {
            YSBLibrary.getLibrary().lostNetwork();
        }else {
            YSBLibrary.getLibrary().availableNetwork();
        }
    }
}

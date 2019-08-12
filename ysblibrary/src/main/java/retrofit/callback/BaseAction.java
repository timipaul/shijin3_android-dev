package retrofit.callback;


import com.hongchuang.hclibrary.storage.LogManager;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.YSBContext;
import com.hongchuang.ysblibrary.YSBLibrary;

import retrofit.ToKenUtil;
import retrofit2.HttpException;
import rx.functions.Action1;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/6/15
 ***/

public class BaseAction<T> implements Action1<T> {
    protected YRequestCallback callback;
    private String message;

    public BaseAction(YRequestCallback callback) {
        this.callback = callback;
    }

    public BaseAction(YRequestCallback callback, String saveToLogMessage) {
        this.callback = callback;
        this.message = saveToLogMessage;
    }


    @Override
    public void call(T o) {
        if (TextUtil.isNotEmpty(message + "")) {
            LogManager.getInstance().w("BaseAction", message + " : " + ((Throwable) o).getMessage());
        }
        if (o instanceof NetworkException) {
            if (((NetworkException) o).getType() == FailOrExceptionType.NO_LOGIN  ) {
                ToKenUtil.deleteToken();
                YSBContext.getLibrary().setNoLogin();
            } else if ( ((NetworkException) o).getType() == FailOrExceptionType.CONNECT_ERROR ){
                YSBContext.getLibrary().setConnectError();
                if (callback != null) {
                    callback.onFailed("500", ((NetworkException) o).getMessage()+"");
                }
            }else {
                if (callback != null) {
                    callback.onException((Throwable) o);
                }
            }
        }else if(o instanceof HttpException){
            if (callback != null) {
                callback.onFailed(""+((HttpException)o).code(), ((HttpException)o).message());
            }
            YSBLibrary.getLibrary().connectError(((HttpException)o).code(),((HttpException)o).message());
        }else {
            if (callback != null) {
                callback.onException((Throwable) o);
            }
        }
    }

}

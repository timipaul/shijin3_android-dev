package retrofit.callback;

/***
 * 功能描述:请求的回调
 * 作者:qiujialiu
 * 时间:2017/5/27
 ***/

public interface YRequestCallback<T> {
    void onSuccess(T var1);

    void onFailed(String var1, String message);

    void onException(Throwable var1);
}

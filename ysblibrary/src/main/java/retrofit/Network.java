package retrofit;

import com.hongchuang.ysblibrary.common.UrlConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * 功能描述:网络请求retrofit的封闭类
 * 作者:qiujialiu
 * 时间:2016/12/17
 ***/
public class Network<T> {

    private static final String TAG = "Network";
    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    private static Map<String, Object> mApiMap = new HashMap<>();
    private static Map<String, Object> mYSApiMap = new HashMap<>();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static List<ReqStatusListener> reqStatusListeners;
    private static TokenInterceptor.RequestStatusListener requestStatusListener;
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
            .hostnameVerifier(new SSLSocketFactoryUtils.TrustAllHostnameVerifier())
            .addInterceptor(new TokenInterceptor())
            .connectTimeout(15000, TimeUnit.MILLISECONDS)
            .readTimeout(15000, TimeUnit.MILLISECONDS)
            .writeTimeout(15000, TimeUnit.MILLISECONDS)
            .build();

    private synchronized static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(UrlConstants.URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit;
    }

    private synchronized static Retrofit getRetrofit2() {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(UrlConstants.SHENMIURL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit2;
    }
    public static void removeOnRequestStatusListener(ReqStatusListener listener) {
        if (reqStatusListeners != null) {
            reqStatusListeners.remove(listener);
            if (reqStatusListeners.size() == 0) {
                reqStatusListeners = null;
                requestStatusListener = null;
            }
        }
    }

    public static void addOnRequestStatusListener(ReqStatusListener listener) {
        List<Interceptor> interceptors = okHttpClient.interceptors();
        if (interceptors != null && interceptors.size() > 0) {
            if (reqStatusListeners == null) reqStatusListeners = new ArrayList<>();
            reqStatusListeners.add(listener);
            for (Interceptor interceptor : interceptors) {
                if (interceptor instanceof TokenInterceptor) {
                    if (requestStatusListener == null) {
                        requestStatusListener = new TokenInterceptor.RequestStatusListener() {
                            @Override
                            public void onStart(String url) {
                                if (reqStatusListeners != null && reqStatusListeners.size() > 0) {
                                    for (ReqStatusListener listener1 : reqStatusListeners) {
                                        listener1.onStart(url);
                                    }
                                }
                            }

                            @Override
                            public void onComplete(String url) {
                                if (reqStatusListeners != null && reqStatusListeners.size() > 0) {
                                    for (ReqStatusListener listener1 : reqStatusListeners) {
                                        listener1.onComplete(url);
                                    }
                                }
                            }

                            @Override
                            public void onError(String url) {
                                if (reqStatusListeners != null && reqStatusListeners.size() > 0) {
                                    for (ReqStatusListener listener1 : reqStatusListeners) {
                                        listener1.onError(url);
                                    }
                                }
                            }
                        };
                    }
                    TokenInterceptor tokenInterceptor = (TokenInterceptor) interceptor;
                    tokenInterceptor.setListener(requestStatusListener);
                }
            }
        }
    }

    public synchronized static <T> T getApi(Class<T> apiClass) {
        if (mApiMap == null) {
            mApiMap = new HashMap<>();
        }
        Object api = mApiMap.get(apiClass.getName());
        if (api == null) {
            api = getRetrofit().create(apiClass);
            mApiMap.put(apiClass.getName(), api);
        }
        return (T) api;
    }
//
    public synchronized static <T> T getApi2(Class<T> apiClass) {
        if (mYSApiMap == null) {
            mYSApiMap = new HashMap<>();
        }
        Object api = mYSApiMap.get(apiClass.getName());
        if (api == null) {
            api = getRetrofit2().create(apiClass);
            mYSApiMap.put(apiClass.getName(), api);
        }
        return (T) api;
    }
    public static void setRetrofitNull(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl + "/")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        if (mApiMap != null) mApiMap.clear();
    }

    public interface ReqStatusListener {
        void onStart(String url);

        void onComplete(String url);

        void onError(String url);
    }


}

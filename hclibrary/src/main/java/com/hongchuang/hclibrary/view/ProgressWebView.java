package com.hongchuang.hclibrary.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hongchuang.hclibrary.R;
import com.hongchuang.hclibrary.utils.LogUtils;


/**
 * 功能描述:
 * 版本:
 */

public class ProgressWebView extends WebView {
    private static final String TAG = "HDWebView";
    private boolean finished;
    private boolean isNoError = true;
    private String baseUrl;
    private OnErrorListener listener;
    private ProgressBar mProgressBar;

    public ProgressWebView(Context context) {
        super(context);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mProgressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 8);
        mProgressBar.setLayoutParams(layoutParams);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebChromeClient(new WebChromeClient());
    }

    public boolean isNoError() {
        return isNoError;
    }

    public void setNoError(boolean noError) {
        isNoError = noError;
    }

//    public void addJsMethod(JSMethod jsMethod) {
//        addJavascriptInterface(jsMethod, "huideng");
//    }

    public OnErrorListener getListener() {
        return listener;
    }

    public void setListener(OnErrorListener listener) {
        this.listener = listener;
    }


    public Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings wSet = getSettings();
        wSet.setJavaScriptEnabled(true);
        wSet.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        wSet.setDatabaseEnabled(true);   //开启 database storage API 功能
        wSet.setAppCacheEnabled(true);//开启 Application Caches 功能


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //支持https网页里引用http的资源
            wSet.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                //    Log.d(TAG, "webview =====>onConsoleMessage"+consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

        });

        setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(TAG, "webview =====>onConsoleMessage" + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.d(TAG, "webview =====>" + "onPageFinished()" + url + " at " + System.currentTimeMillis() +
                        "-------- url : " + url);
                LoadingDialog.dismissDialog();
                finished = true;
                if (isNoError && listener != null) {
                    listener.onSuccess();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.d(TAG, "webview =====>" + "shouldOverrideUrlLoading()" + "-------- url : " + url);
//                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
//                if (url.startsWith("weixin://wap/pay?")) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    activity.startActivity(intent);
//                    return true;
//                }  else {
//                        Map<String, String> extraHeaders = new HashMap<String, String>();
//                        extraHeaders.put("Referer", "http://net.zt717.com");
//                        view.loadUrl(url, extraHeaders);
//                    }

                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                if (url.contains("api/backToApp")) {
//                    QueryLogActivity.startQueryLog(activity,QueryLogActivity.DEALQUERY);
//                    MActivityManager.getInstance().delACT(CashOutActivity.class);
//                    activity.finish();
//                }
                LogUtils.d(TAG, "webview =====>" + "onPageStarted()" + url + " at " + System.currentTimeMillis());
                finished = false;
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                isNoError = false;
                if (listener != null)
                    listener.onError();
                //  showErrorDialog(finished+"---onReceivedSslError:"+error.toString());
                super.onReceivedSslError(view, handler, error);
            }

            @SuppressLint("NewApi")
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                isNoError = false;
                if (listener != null)
                    listener.onError();
                //showErrorDialog(finished+"---onReceivedHttpError:"+errorResponse.getStatusCode());
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @SuppressLint("NewApi")
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                isNoError = false;
                if (listener != null)
                    listener.onError();
                //showErrorDialog(finished+"---onReceivedError:"+error.getErrorCode()+"--"+error.getDescription());
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                super.onLoadResource(view, url);
            }


        });

    }

    public interface OnErrorListener {
        void onError();

        void onSuccess();
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        AbsoluteLayout. LayoutParams lp;
//        lp = (AbsoluteLayout.LayoutParams) mProgressBar.getLayoutParams();
////        lp.x = l;
////        lp.y = t;
//        lp.weight=l;
//        lp.height=t;
//        mProgressBar.setLayoutParams(lp);
//        super.onScrollChanged(l, t, oldl, oldt);
//    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * dataType == 0 时 dataString为字符串格式
     * dataType == 1 时 dataString为json格式
     * 单个如下
     */
    class WebParams {
        int dataType;
        String dataString;
    }

}


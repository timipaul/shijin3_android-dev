package com.shijinsz.shijin.ui.home;

import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.hongchuang.hclibrary.view.ProgressWebView;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by tg596 on 2018/4/2.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.webview)
    ProgressWebView webview;

    private String url;//判断是否为开通完美账单

    private String title;//判断是否为境外消费

    private String type;//判断是否为我的保险



    @Override
    public int bindLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        url = getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
        if (title!=null){
            setTitle(title);
        }
        WebSettings settings = webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        if (title == null) {
            if (url != null) {
                if (url.startsWith("http:")||url.startsWith("https:")){
                    webview.loadUrl(url);
                }else {
                    webview.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
                }
//            webview.loadUrl(url);
            } else {
//                if (type!=null){
//                    if (type.equals("2")){
//                        setTitle("信用卡申请");
//                        showTitleRightText("我的申请", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(mContext,WebViewActivityTow.class);
//                                intent.putExtra("type","1");
//                                startActivity(intent);
//                            }
//                        });
//                        webview.loadUrl("http://app-statics.hcuse.com/index.html?token="+ ToKenUtil.getToken());
//                    }else if (type.equals("3")){
//                        setTitle("贷款申请");
//                        showTitleRightText("我的申请", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(mContext,WebViewActivityTow.class);
//                                intent.putExtra("type","2");
//                                startActivity(intent);
//                            }
//                        });
//                        webview.loadUrl("http://app-statics.hcuse.com/loan.html?token="+ ToKenUtil.getToken());
//                    }else {
//                        setTitle("我的保险");
//                        if (getResources().getString(R.string.TOPBRANCHNO).equals("OEM201806155651101534")){
//                            webview.loadUrl("https://a.zhongan.com/p/84859837?bizOrigin=APPgoods&from=singlemessage&isappinstalled=0");
//                        }else {
//                            webview.loadUrl("https://epcis-ptp.pingan.com.cn/");
//                        }
//                    }
//
//
//                }else {
//                    setTitle("境外消费");
//                    webview.loadUrl("http://out.71cn.com/test/index.html?agent_id=10");
//                }
            }
//
//        }else {
//            setTitle(title);
//            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            webview.getSettings().setLoadWithOverviewMode(true);
//            webview.setActivity(this);
//            webview.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
//
//        }
    }

    @Override
    public void onBackPressed() {

        if (webview.canGoBack()) {
            webview.goBack();

        } else {
            finish();
        }
    }
}

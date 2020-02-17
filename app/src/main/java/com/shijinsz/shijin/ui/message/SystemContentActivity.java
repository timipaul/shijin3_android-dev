package com.shijinsz.shijin.ui.message;

import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.ProgressWebView;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by yrdan on 2018/8/8.
 */

public class SystemContentActivity extends BaseActivity {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.webview)
    ProgressWebView webview;

    @Override
    public int bindLayout() {
        return R.layout.system_content_activity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(getString(R.string.system_title));
        String time = getIntent().getStringExtra("time");
        String url = getIntent().getStringExtra("url");
        tvTime.setText(TimeUtil.format(Long.valueOf(time)*1000,"yyyy-MM-dd"));
        webview.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
    }
}

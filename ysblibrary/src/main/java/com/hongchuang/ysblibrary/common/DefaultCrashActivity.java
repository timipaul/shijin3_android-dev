package com.hongchuang.ysblibrary.common;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.R;
//import com.hongchuang.ysb_app_for_android.utils.exception.AppOnCrash;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;

/***
 * 功能描述:默认crash界面
 * 作者:xiongning
 * 时间:2016/12/16
 * 版本:1.0
 ***/

public class DefaultCrashActivity extends AppCompatActivity {

    private boolean isPad;

    private Button btn_error_detail;

    private Button btn_restart;

    private String errorInformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.layout_activity_app_crash);

        initViews();
        bindEvents();
        initData();
    }



    private void initViews() {

        btn_error_detail = (Button)findViewById(R.id.btn_error_detail);
        btn_restart = (Button)findViewById(R.id.btn_restart);

    }

    public void bindEvents() {

//        btn_error_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new AlertDialog.Builder(DefaultCrashActivity.this)
//                        .setTitle("灯无忧错误信息")
//                        .setMessage(AppOnCrash.getAllErrorDetailsFromIntent(DefaultCrashActivity.this, getIntent()))
//                        .setPositiveButton("关闭", null)
//                        .setNeutralButton("拷贝",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        copyErrorToClipboard();
//                                        ToastUtil.showToast("已经拷贝", Toast.LENGTH_SHORT);
//                                        Toast.makeText(DefaultCrashActivity.this, "已经拷贝", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                        .show();
//            }
//        });

        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.exit(0);

//                Intent intent = new Intent(DefaultCrashActivity.this, SplashActivity.class);
//
//                AppOnCrash.restartApplicationWithIntent(DefaultCrashActivity.this, intent, null);
            }
        });

    }


    public void initData() {


        new Thread(){

            @Override
            public void run() {

//                errorInformation = AppOnCrash.getAllErrorDetailsFromIntent(DefaultCrashActivity.this, getIntent());

                if(TextUtil.isNotEmpty(errorInformation)) {

//                    MobclickAgent.reportError(DefaultCrashActivity.this, errorInformation);
                }
            }
        }.start();


    }

    private void copyErrorToClipboard() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("错误信息", errorInformation);
            clipboard.setPrimaryClip(clip);
        } else {
            //noinspection deprecation
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(errorInformation);
        }
    }

    public boolean isPad() {
        return isPad;
    }

    public void setPad(boolean pad) {
        isPad = pad;
    }
}

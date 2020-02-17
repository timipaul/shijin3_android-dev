package com.shijinsz.shijin.ui.home;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BackService;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.base.IBackService;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/9/10.
 */

public class NewGuideActivity extends BaseActivity {
    @BindView(R.id.resultText)
    TextView resultText;

    @Override
    public int bindLayout() {
        return R.layout.new_guide_activity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(getString(R.string.news_notide));
//        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
//        mReciver = new MessageBackReciver(resultText);
//        mServiceIntent = new Intent(this, BackService.class);
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
//        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
//        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
//        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
    }

    private Intent mServiceIntent;

    private IBackService iBackService;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBackService = null;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBackService = IBackService.Stub.asInterface(service);
        }
    };

    class MessageBackReciver extends BroadcastReceiver {
        private WeakReference<TextView> textView;

        public MessageBackReciver(TextView tv) {
            textView = new WeakReference<TextView>(tv);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TextView tv = textView.get();
            if (action.equals(BackService.HEART_BEAT_ACTION)) {
                if (null != tv) {
                    Log.i("danxx", "Get a heart heat");
                    tv.setText("Get a heart heat");
                }
            } else {
                Log.i("danxx", "Get a heart heat");
                String message = intent.getStringExtra("message");
                tv.setText("服务器消息:" + message);
            }
        }

        ;
    }

    private MessageBackReciver mReciver;

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unbindService(conn);
//        mLocalBroadcastManager.unregisterReceiver(mReciver);
//    }


    @OnClick({R.id.rl_cash_answer, R.id.rl_put, R.id.rl_usefull, R.id.rl_make_money, R.id.rl_user_know, R.id.rl_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_cash_answer:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Comment.url + "text_cashanswerlist#isapp=on");
                intent.putExtra("title", getString(R.string.case_answer));
                startActivity(intent);
                break;
            case R.id.rl_put:
                Intent intent2 = new Intent(mContext, WebViewActivity.class);
                intent2.putExtra("url", Comment.url + "text_lssueredlist#isapp=on");
                intent2.putExtra("title", getString(R.string.put_red));
                startActivity(intent2);
                break;
            case R.id.rl_usefull:
                Intent intent3 = new Intent(mContext, WebViewActivity.class);
                intent3.putExtra("url", Comment.url + "text_effective#isapp=on");
                intent3.putExtra("title", getString(R.string.usefull_get));
                startActivity(intent3);
                break;
            case R.id.rl_make_money:
                Intent intent4 = new Intent(mContext, WebViewActivity.class);
                intent4.putExtra("url", Comment.url + "text_makemoneylist#isapp=on");
                intent4.putExtra("title", getString(R.string.mack_money));
                startActivity(intent4);
                break;
            case R.id.rl_user_know:
                Intent intent5 = new Intent(mContext, WebViewActivity.class);
                intent5.putExtra("url", Comment.url + "text_userknowledge#isapp=on");
                intent5.putExtra("title", getString(R.string.user_need_know));
                startActivity(intent5);
                break;
            case R.id.rl_about:
//                try {
//                    boolean isSend = iBackService.sendMessage("biubiubiu");//Send Content by socket
//                    Toast.makeText(this, isSend ? "success" : "fail",
//                            Toast.LENGTH_SHORT).show();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                Intent intent6 = new Intent(mContext, WebViewActivity.class);
                intent6.putExtra("url", Comment.url + "text_aboutus#isapp=on");
                intent6.putExtra("title", getString(R.string.about_shijin));
                startActivity(intent6);
                break;
        }
    }
}

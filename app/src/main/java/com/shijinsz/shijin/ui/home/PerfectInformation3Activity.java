package com.shijinsz.shijin.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/27.
 */

public class PerfectInformation3Activity extends BaseActivity {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_2k)
    TextView tv2k;
    @BindView(R.id.tv_5k)
    TextView tv5k;
    @BindView(R.id.tv_10k)
    TextView tv10k;
    @BindView(R.id.tv_20k)
    TextView tv20k;

    @Override
    public int bindLayout() {
        return R.layout.perfect_information3_activity;
    }

    @Override
    public void initView(View view) {

    }

    @OnClick({R.id.tv_other, R.id.tv_2k, R.id.tv_5k, R.id.tv_10k, R.id.tv_20k})
    public void onViewClicked(View view) {
        Intent intent=new Intent(mContext,PerfectInformation4Activity.class);
        intent.putExtra("gender",getIntent().getStringExtra("gender"));
        intent.putExtra("age",getIntent().getStringExtra("age"));
        switch (view.getId()) {
            case R.id.tv_other:
                intent.putExtra("income","other");
                startActivity(intent);
                break;
            case R.id.tv_2k:
                intent.putExtra("income","2000");
                startActivity(intent);
                break;
            case R.id.tv_5k:
                intent.putExtra("income","5000");
                startActivity(intent);
                break;
            case R.id.tv_10k:
                intent.putExtra("income","10000");
                startActivity(intent);
                break;
            case R.id.tv_20k:
                intent.putExtra("income","20000");
                startActivity(intent);
                break;
        }
    }
}

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

public class PerfectInformation2Activity extends BaseActivity {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_90s)
    TextView tv90s;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_00s)
    TextView tv00s;
    @BindView(R.id.tv_70s)
    TextView tv70s;
    @BindView(R.id.tv_80s)
    TextView tv80s;

    @Override
    public int bindLayout() {
        return R.layout.perfect_information2_activity;
    }

    @Override
    public void initView(View view) {

    }


    @OnClick({R.id.tv_90s, R.id.tv_other, R.id.tv_00s, R.id.tv_70s, R.id.tv_80s})
    public void onViewClicked(View view) {
        Intent intent=new Intent(mContext,PerfectInformation3Activity.class);
        intent.putExtra("gender",getIntent().getStringExtra("gender"));
        switch (view.getId()) {
            case R.id.tv_90s:
                intent.putExtra("age","90");
                startActivity(intent);
                break;
            case R.id.tv_other:
                intent.putExtra("age","other");
                startActivity(intent);
                break;
            case R.id.tv_00s:
                intent.putExtra("age","00");
                startActivity(intent);
                break;
            case R.id.tv_70s:
                intent.putExtra("age","70");
                startActivity(intent);
                break;
            case R.id.tv_80s:
                intent.putExtra("age","80");
                startActivity(intent);
                break;
        }
    }
}

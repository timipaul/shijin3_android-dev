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

public class PerfectInformation4Activity extends BaseActivity {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_mom)
    TextView tvMom;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_bailing)
    TextView tvBailing;
    @BindView(R.id.tv_it)
    TextView tvIt;
    @BindView(R.id.tv_free)
    TextView tvFree;
    @BindView(R.id.tv_teacher)
    TextView tvTeacher;
    @BindView(R.id.tv_other)
    TextView tvOther;

    @Override
    public int bindLayout() {
        return R.layout.perfect_information4_activity;
    }

    @Override
    public void initView(View view) {

    }


    @OnClick({R.id.tv_mom, R.id.tv_doctor, R.id.tv_bailing, R.id.tv_it, R.id.tv_free, R.id.tv_teacher, R.id.tv_other})
    public void onViewClicked(View view) {
        Intent intent = new Intent(mContext, PerfectInformation5Activity.class);
        intent.putExtra("gender", getIntent().getStringExtra("gender"));
        intent.putExtra("age", getIntent().getStringExtra("age"));
        intent.putExtra("income", getIntent().getStringExtra("income"));
        switch (view.getId()) {
            case R.id.tv_mom:
                intent.putExtra("job","mother");
                startActivity(intent);
                break;
            case R.id.tv_doctor:
                intent.putExtra("job","doctor");
                startActivity(intent);
                break;
            case R.id.tv_bailing:
                intent.putExtra("job","white_collar");
                startActivity(intent);
                break;
            case R.id.tv_it:
                intent.putExtra("job","IT");
                startActivity(intent);
                break;
            case R.id.tv_free:
                intent.putExtra("job","freelance");
                startActivity(intent);
                break;
            case R.id.tv_teacher:
                intent.putExtra("job","teacher");
                startActivity(intent);
                break;
            case R.id.tv_other:
                intent.putExtra("job","other");
                startActivity(intent);
                break;
        }
    }
}

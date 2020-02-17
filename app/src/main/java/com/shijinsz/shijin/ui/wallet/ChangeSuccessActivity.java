package com.shijinsz.shijin.ui.wallet;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/11/26.
 */

public class ChangeSuccessActivity extends BaseActivity {
    @BindView(R.id.tv_success)
    TextView tvSuccess;
    @BindView(R.id.return_list)
    Button returnList;

    @Override
    public int bindLayout() {
        return R.layout.change_success_activity;
    }

    @Override
    public void initView(View view) {
        setTitle("兑换零钱");
        showTitleBackButton();
        String str = tvSuccess.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),11,str.length()-2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSuccess.setText(style);
    }


    @OnClick(R.id.return_list)
    public void onViewClicked() {
        finish();
    }
}

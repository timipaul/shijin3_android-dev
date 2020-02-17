package com.shijinsz.shijin.ui.ad;

import android.view.View;
import android.widget.ImageView;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/15.
 */

public class NewAdActivity extends BaseActivity {
    @BindView(R.id.img_graphic)
    ImageView imgGraphic;
    @BindView(R.id.img_video)
    ImageView imgVideo;

    @Override
    public int bindLayout() {
        return R.layout.new_ad_activity;
    }

    @Override
    public void initView(View view) {
        setTitle(getString(R.string.new_ad));
        showTitleBackButton();
    }


    @OnClick({R.id.img_graphic, R.id.img_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_graphic:
                startActivity(NewGraphicActivity.class);
                break;
            case R.id.img_video:
                startActivity(NewVideoActivity.class);

                break;
        }
    }


}

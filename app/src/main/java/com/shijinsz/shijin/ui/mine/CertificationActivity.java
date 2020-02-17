package com.shijinsz.shijin.ui.mine;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PromotionBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/10.
 */

public class CertificationActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.commit1)
    Button commit1;
    @BindView(R.id.commit2)
    Button commit2;

    private int type = 1;

    @Override
    public int bindLayout() {
        return R.layout.certification_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.my_ad));
        getStatus();
    }

    @OnClick({R.id.commit1, R.id.commit2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commit1:
                switch (type){
                    case 1:
                        startActivity(PersonalCertificationActivity.class);
                        finish();
                        break;
                    case 2:
                        startActivity(MyAdActivity.class);
                        finish();
                        break;
                    case 3:
                        ((MainActivity)MActivityManager.getInstance().getActivity(MainActivity.class)).setCurrentItem(0);
                        finish();
                        break;
                    case 4:
                        type=1;
                        img.setImageResource(R.mipmap.icon_no_certification);
                        tv1.setText(getString(R.string.certification_no1));
                        tv2.setText(getString(R.string.certification_no2));
                        commit1.setText(getString(R.string.personal_certification));
                        commit2.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.commit2:
                startActivity(BusinessCertificationActivity.class);
                finish();
                break;
        }
    }

    public void getStatus() {
        mStateView.showLoading();
        Map map=new HashMap();
        YSBSdk.getService(OAuthService.class).getpromotion(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<PromotionBean>() {
            @Override
            public void onSuccess(PromotionBean var1) {
                mStateView.showContent();
                if (var1.getUser_promotion().getPromotion_status().equals("on")) {
                    if (var1.getUser_promotion().getAdvertiser_mode().equals("person")) {
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_advertiser, "person");
                    }else {
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_advertiser, "on");
                    }
                    type=2;
                    img.setImageResource(R.mipmap.icon_shcg);
                    tv1.setText(getString(R.string.certification_success1));
                    tv2.setText(getString(R.string.certification_success2));
                    commit1.setText(getString(R.string.certification_success3));
                    commit2.setVisibility(View.GONE);
                }else if (var1.getUser_promotion().getPromotion_status().equals("pending")){
                    type=3;
                    img.setImageResource(R.mipmap.icon_shz);
                    tv1.setText(getString(R.string.certification_doing1));
                    tv2.setText(getString(R.string.certification_doing2));
                    commit1.setText(getString(R.string.certification_doing3));
                    commit2.setVisibility(View.GONE);
                }else if (var1.getUser_promotion().getPromotion_status().equals("back")){
                    type=4;
                    img.setImageResource(R.mipmap.icon_shwtg);
                    tv1.setText(getString(R.string.certification_dont1));
                    tv2.setText(var1.getUser_promotion().getCheck_info());
                    commit1.setText(getString(R.string.certification_dont3));
                    commit2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
}

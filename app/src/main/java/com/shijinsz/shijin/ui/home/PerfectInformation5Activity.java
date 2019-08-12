package com.shijinsz.shijin.ui.home;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;

import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PerfectBean;
import com.hongchuang.ysblibrary.model.model.bean.PerfecterBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.adapter.PerfectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/27.
 */

public class PerfectInformation5Activity extends BaseActivity {
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private List<PerfectBean> bean = new ArrayList<>();
    private PerfectAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.perfect_information5_activity;
    }

    @Override
    public void initView(View view) {
        bean.add(new PerfectBean(R.mipmap.icon_skincare, getString(R.string.skincare),"skincare"));
        bean.add(new PerfectBean(R.mipmap.icon_emotional, getString(R.string.emotion),"emotion"));
        bean.add(new PerfectBean(R.mipmap.icon_beautymakeup, getString(R.string.beauty),"beauty"));
        bean.add(new PerfectBean(R.mipmap.icon_household, getString(R.string.house),"house"));
        bean.add(new PerfectBean(R.mipmap.icon_sale, getString(R.string.special_sale),"special_sale"));
        bean.add(new PerfectBean(R.mipmap.icon_maternalandinfant,  getString(R.string.childcare),"childcare"));
        bean.add(new PerfectBean(R.mipmap.icon_shipin, getString(R.string.video),"video"));
        bean.add(new PerfectBean(R.mipmap.icon_life, getString(R.string.life),"life"));
        bean.add(new PerfectBean(R.mipmap.icon_food, getString(R.string.food),"food"));
        bean.add(new PerfectBean(R.mipmap.icon_app, getString(R.string.APP),"APP"));
        bean.add(new PerfectBean(R.mipmap.icon_tourism, getString(R.string.tourism),"tourism"));
        bean.add(new PerfectBean(R.mipmap.icon_other, getString(R.string.other),"other"));
        adapter = new PerfectAdapter(mContext, bean, R.layout.perfect_information_item);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (bean.get(i).isCheck()) {
                    bean.get(i).setCheck(false);
                }else {
                    bean.get(i).setCheck(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        scrollView.smoothScrollTo(0,0);
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        List<String> interests=new ArrayList<>();
        for (PerfectBean perfectBean : bean) {
            if (perfectBean.isCheck()){
                interests.add(perfectBean.getInterest());
            }
        }
        if (interests.size()>0)
        changeInfo(interests);
    }

    public void changeInfo(List<String>interests){
        PerfecterBean map = new PerfecterBean();
        map.setMode("complete");
        map.setGender(getIntent().getStringExtra("gender"));
        map.setAge(getIntent().getStringExtra("age"));
        map.setIncome(getIntent().getStringExtra("income"));
        map.setJob(getIntent().getStringExtra("job"));
        map.setInterests(interests);

        YSBSdk.getService(OAuthService.class).prefect_info(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_is_complete_info,"on");
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                finish();
            }

            @Override
            public void onException(Throwable var1) {
                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
                finish();
            }
        });
    }

}

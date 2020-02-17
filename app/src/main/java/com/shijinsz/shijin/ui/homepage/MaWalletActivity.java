package com.shijinsz.shijin.ui.homepage;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.TakeMoneyRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.storeUserBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.homepage.adapter.CapitalFlowAdapter;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;


/** 商城我的钱包 */
public class MaWalletActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.price_num)
    TextView mPrice;
    @BindView(R.id.not_page)
    ImageView mNotPage;

    CapitalFlowAdapter adapter;
    List<TakeMoneyRecordBean> data = new ArrayList<>();


    @Override
    public int bindLayout() {
        return R.layout.ma_wallet_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle("钱包");


        adapter = new CapitalFlowAdapter(R.layout.item_award_layout,data);
        recyclerView.setAdapter(adapter);

        getPrice();

        initData();
    }

    @OnClick({R.id.bt_take})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.bt_take:
                //提现
                startActivity(new Intent(mActivity, DrawMoneyActivity.class));

                break;
        }
    }

    private void getPrice(){
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        YSBSdk.getService(OAuthService.class).getStoreUser(map, new YRequestCallback<storeUserBean>() {
            @Override
            public void onSuccess(storeUserBean var1) {
                String state;
                if(var1.getPrivilege().equals("MEMBER") || var1.getPrivilege().equals("PARTNER")){
                    state = "true";
                }else{
                    state = "false";
                }
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_is_store_vip,state);
                mPrice.setText(String.valueOf(var1.getCash()));

            }

            @Override
            public void onFailed(String var1, String message) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_is_store_vip,"false");
            }

            @Override
            public void onException(Throwable var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_is_store_vip,"false");
            }
        });
    }

    private void initData() {
        Map<String,Object> map = new HashMap();
        map.put("pageSize","100");
        map.put("pageIndex","1");
        YSBSdk.getService(OAuthService.class).getCapitalFlow(map,new YRequestCallback<BaseBean<TakeMoneyRecordBean>>() {
            @Override
            public void onSuccess(BaseBean<TakeMoneyRecordBean> var1) {
                if(var1.getResult().size() > 0){
                    mNotPage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                data.addAll(var1.getResult());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

}

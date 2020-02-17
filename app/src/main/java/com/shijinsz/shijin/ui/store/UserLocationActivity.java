package com.shijinsz.shijin.ui.store;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.google.gson.Gson;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.PointDetailBean;
import com.hongchuang.ysblibrary.model.model.bean.UserSiteBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.LocationAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/***
 * 用户收货地址列表
 */

public class UserLocationActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener, View.OnClickListener {

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_right)
    TextView mAddSite;
    @BindView(R.id.title_layout)
    RelativeLayout mLayout;
    @BindView(R.id.top_view)
    TextView mTopView;
    private LocationAdapter adpater;
    private List<UserSiteBean> list = new ArrayList<>();
    private boolean isRefresh = true;

    private String userId;
    private boolean select_site = true;


    @Override
    public int bindLayout() {
        return R.layout.my_wallet_activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("收货地址");
        showTitleBackButton();
        mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_f1));

        mAddSite.setText("添加新地址");
        mAddSite.setLetterSpacing(0);
        mAddSite.setVisibility(View.VISIBLE);
        mAddSite.setOnClickListener(this);
        mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.color_f1));

        userId = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        adpater = new LocationAdapter(R.layout.user_take_list, list);
        recyclerView.setAdapter(adpater);
        recyclerView.setBackgroundColor(mContext.getResources().getColor(R.color.color_f1));
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                getData();
            }
        });
        adpater.setOnListen(new LocationAdapter.onListen() {
            @Override
            public void callback(int pos) {
                Intent intent = new Intent(mContext,NewLocationActivity.class);
                intent.putExtra("bean",new Gson().toJson(list.get(pos)));
                startActivityForResult(intent,101);
            }
        });
        adpater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(select_site){
                    Intent intent = new Intent();
                    intent.putExtra("site",new Gson().toJson(list.get(position)));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        try {
            String select = getIntent().getStringExtra("select");
            if(select.equals("true")){
                select_site = true;
            }else{
                select_site = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mStateView.showLoading();
        getData();
    }

    private void getData() {
        YSBSdk.getService(OAuthService.class).getAddress(userId, new YRequestCallback<BaseBean<UserSiteBean>>() {
            @Override
            public void onSuccess(BaseBean<UserSiteBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();

                if (isRefresh) {
                    if (var1.getResult().size() == 0){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }else{
                        rlEmpty.setVisibility(View.GONE);
                    }
                    list.clear();
                }
                list.addAll(var1.getResult());
                if (var1.getResult().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }

                adpater.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showContent();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_right:
                //添加新地址
                Intent intent = new Intent(mContext,NewLocationActivity.class);
                intent.putExtra("id",-1);
                startActivityForResult(intent,101);
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            getData();
        }
    }
}

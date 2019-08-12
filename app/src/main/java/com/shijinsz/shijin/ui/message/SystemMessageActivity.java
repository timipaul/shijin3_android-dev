package com.shijinsz.shijin.ui.message;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.SystemMessageBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.message.adapter.SystemMessageAdapter;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/8.
 */

public class SystemMessageActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<SystemMessageBean.Circulars> list = new ArrayList<>();
    private SystemMessageAdapter adapter;
    private boolean isRefresh = true;
    private View empty;

    @Override
    public int bindLayout() {
        return R.layout.system_message_activity;
    }

    @Override
    public void initView(View view) {
        int num=Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_circular_total_number));
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_circular_total_number,"0");
        showTitleBackButton();
        setTitle(getString(R.string.system_title));
        empty=mInflater.inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=empty.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.message_empty));
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_message),null,null);
        adapter = new SystemMessageAdapter(R.layout.system_item, list);
        recyclerView.setAdapter(adapter);
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
                getData(System.currentTimeMillis()/1000 + "");
            }
        });
        mStateView.showLoading();
        getData(System.currentTimeMillis()/1000 + "");
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent =new Intent(mContext,SystemContentActivity.class);
                intent.putExtra("time",list.get(position).getCreated_at());
                intent.putExtra("url",list.get(position).getCircular_content());
                startActivity(intent);
            }
        });
    }

    private void getData(String s) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("size", "10");
        map.put("cursor", s);
        YSBSdk.getService(OAuthService.class).circular(map, new YRequestCallback<SystemMessageBean>() {
            @Override
            public void onSuccess(SystemMessageBean var1) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                mStateView.showContent();
                if (isRefresh) {
                    adapter.setEmptyView(empty);
                    list.clear();
                }
                list.addAll(var1.getCirculars());
                if (var1.getCirculars().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                mStateView.showContent();
                adapter.setEmptyView(empty);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                    mStateView.showRetry();
                }catch (Exception e){

                }

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        getData(list.get(list.size() - 1).getCreated_at());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        getData(System.currentTimeMillis()/1000 + "");
    }

}

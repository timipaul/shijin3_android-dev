package com.shijinsz.shijin.ui.store;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.SeekGoodsAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/** 零售商品列表 */
public class StoreGoodsActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    SeekGoodsAdapter goodsAdapter;
    List<StoreGoodsBean> list = new ArrayList();
    private boolean isRefresh = true;
    private int index = 1;

    //四大分类
    private String special = null;

    @Override
    public int bindLayout() {
        return R.layout.store_goods_view;
    }


    @Override
    public void initView(View view) {

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);

        try {
            special = getIntent().getStringExtra("special");
        } catch (Exception e) {

        }


        goodsAdapter = new SeekGoodsAdapter(mContext,list,R.layout.store_seek_goods_item);
        mGridView.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemClickListener(new SeekGoodsAdapter.ItemOnclick() {
            @Override
            public void itemClickInto(String goodsId) {
                Intent intent = new Intent(mContext, StoreGoodsDetails.class);
                intent.putExtra("goodsId",goodsId);
                startActivityForResult(intent,102);
            }

            @Override
            public void addClick(String goodsId, TextView num, Button bt) {

            }

            @Override
            public void minusClick(String goodsId, TextView num, Button bt) {

            }
        });
        mStateView.showLoading();

        initData();
    }


    private void initData() {
        Map<String,Object> map = new HashMap<>();
        map.put("pageIndex",index);
        map.put("pageSize","10");
        if(special != null){
            map.put("special",special);
        }
        YSBSdk.getService(OAuthService.class).getShopGoods(map, new YRequestCallback<BaseBean<StoreGoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreGoodsBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    if (var1.getGoods().size()==0){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }else{
                        rlEmpty.setVisibility(View.GONE);
                    }
                    list.clear();
                }
                list.addAll(var1.getGoods());
                if (var1.getGoods().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                goodsAdapter.notifyDataSetChanged();
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
                    var1.printStackTrace();
                    mStateView.showRetry();
                    refresh.finishLoadMore(false);
                    refresh.finishRefresh(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.tv_seek})
    public void onClickView(View view){
        //搜索
        startActivity(new Intent(mContext,StoreSeekActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102 && resultCode == 102){
            setResult(102);
            finish();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        index++;
        initData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        index = 1;
        isRefresh = true;
        initData();
    }
}

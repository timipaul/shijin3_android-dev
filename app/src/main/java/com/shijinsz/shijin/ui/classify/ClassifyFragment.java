package com.shijinsz.shijin.ui.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.GoodsClassifyBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.classify.adapter.LeftAdapter;
import com.shijinsz.shijin.ui.classify.adapter.RightAdapter;
import com.shijinsz.shijin.ui.classify.adapter.RightMenuAdapter;
import com.shijinsz.shijin.ui.store.StoreGoodsDetails;
import com.shijinsz.shijin.ui.store.StoreHomeActivity;
import com.shijinsz.shijin.ui.store.StoreSeekActivity;
import com.shijinsz.shijin.ui.store.adapter.SeekGoodsAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * 商城商品分类
 */
public class ClassifyFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.left_recycler)
    PowerfulRecyclerView left_recycler;
    @BindView(R.id.goods_view)
    GridView gridView;
    @BindView(R.id.right_recycler)
    PowerfulRecyclerView right_recycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    LeftAdapter leftAdapter;
    RightAdapter rightAdapter;
    SeekGoodsAdapter goodsAdapter;

    List<GoodsClassifyBean> leftData = new ArrayList<>();
    List<StoreGoodsBean> listGoods = new ArrayList();
    List<GoodsClassifyBean.Child> rightMenuData = new ArrayList<>();

    private boolean isRefresh = true;
    private int index = 1;
    private String categoryId;


    @Override
    protected int provideContentViewId() {
        return R.layout.store_goods_classify;
    }

    @Override
    protected void loadData() {
        //加载数据
        getClassifyData();
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        SetStateViewRoot(rootView);

        refresh.setRefreshHeader(new HeaderView(getContext()));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        //设置视图
        setView();
    }

    private void setView() {
        leftAdapter = new LeftAdapter(R.layout.item_left_goods_classify, leftData);
        rightAdapter = new RightAdapter(R.layout.item_right_goods_classify,rightMenuData);
        goodsAdapter = new SeekGoodsAdapter(getContext(),listGoods,R.layout.store_seek_goods_item);
        goodsAdapter.setOnItemClickListener(new SeekGoodsAdapter.ItemOnclick() {
            @Override
            public void addClick(String goodsId, TextView num, Button bt) {

            }

            @Override
            public void minusClick(String goodsId, TextView num, Button bt) {

            }

            @Override
            public void itemClickInto(String goodsId) {
                Intent intent = new Intent(getContext(), StoreGoodsDetails.class);
                intent.putExtra("goodsId",goodsId);
                startActivityForResult(intent,102);
            }
        });

        left_recycler.setAdapter(leftAdapter);
        right_recycler.setAdapter(rightAdapter);
        gridView.setAdapter(goodsAdapter);
        leftAdapter.setOnListen(new LeftAdapter.onListen() {
            @Override
            public void callback(GoodsClassifyBean item, int pos) {

                right_recycler.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                listGoods.clear();
                goodsAdapter.mDatas.clear();
                //让左右联动起来的
                //当点击时显示当前条目的背景和文字的颜色
                for (int i = 0; i < leftData.size(); i++) {
                    if (pos == i) {
                        leftData.get(i).setClick(true);

                    } else {
                        leftData.get(i).setClick(false);
                    }
                }
                //更新视图(必须有)
                rightMenuData.clear();
                if (item.getChild() != null) {
                    rightMenuData.addAll(item.getChild());
                }
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();

            }
        });



        rightAdapter.setOnListen(new RightAdapter.onListen() {
            @Override
            public void callback(String id) {
                getGoodsData(id);
            }
        });

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                index = 1;
                getGoodsData(categoryId);
            }
        });


    }


    private void getClassifyData() {
        Map<String,Object> map = new HashMap<>();
        map.put("type","GOODS");
        //获取分类数据
        YSBSdk.getService(OAuthService.class).getClassifyList(map,new YRequestCallback<BaseBean<GoodsClassifyBean>>() {
            @Override
            public void onSuccess(BaseBean<GoodsClassifyBean> var1) {
                leftData.addAll(var1.getResult());
                if(leftData.size() > 0){
                    leftData.get(0).setClick(true);
                    rightMenuData.addAll(leftData.get(0).getChild());

                }

                leftAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    public void getGoodsData(String id){
        categoryId = id;
        Map<String,Object> map = new HashMap<>();
        map.put("pageIndex",index);
        map.put("pageSize","10");
        map.put("categoryId",id);
        YSBSdk.getService(OAuthService.class).getShopGoods(map, new YRequestCallback<BaseBean<StoreGoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreGoodsBean> var1) {
                right_recycler.setVisibility(View.GONE);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();

                if (isRefresh) {
                    listGoods.clear();
                }
                gridView.setVisibility(View.VISIBLE);
                listGoods.addAll(var1.getGoods());
                if (var1.getGoods().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                goodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    @OnClick({R.id.tv_seek})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.tv_seek:
                //跳转搜索
                startActivity(new Intent(getContext(), StoreSeekActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102 && resultCode == 102){
            ((StoreHomeActivity)getActivity()).showModelFragment(2);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        index++;
        getGoodsData(categoryId);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        index = 1;
        isRefresh = true;
        getGoodsData(categoryId);
    }
}

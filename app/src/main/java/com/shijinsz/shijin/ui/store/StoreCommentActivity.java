package com.shijinsz.shijin.ui.store;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.model.model.bean.StroeCommentBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.CommentAdapter;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/** 商城评论页 */
public class StoreCommentActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.top_view)
    View topView;


    private String cursor;
    private boolean isRefresh = true;

    private CommentAdapter adapter;
    List<StroeCommentBean> commentData = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.store_comment_activity;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("商品评论");
        showTitleBackButton();
        topView.setBackgroundColor(Color.WHITE);

        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        adapter = new CommentAdapter(R.layout.store_comment_item, commentData);
        recyclerView.setAdapter(adapter);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                cursor = System.currentTimeMillis()/1000 + "";
                getData();
            }
        });
        cursor = System.currentTimeMillis()/1000 + "";
        mStateView.showLoading();
        getData();


    }

    private void getData() {
        Map map = new HashMap();

        /*StroeCommentBean data = new StroeCommentBean();
        data.setName("冬瓜");
        data.setImg("");
        data.setComment_time("2019-09-13");
        data.setGoods_measure("6cm 1.8米长");
        data.setContent("随便给点评价吧....");
        commentData.add(data);
        commentData.add(data);
        commentData.add(data);
        commentData.add(data);
        commentData.add(data);
        adapter.notifyDataSetChanged();*/


        //map.put("mode", "person");
        //map.put("cursor", cursor);
        //map.put("user_id",userId);
        // map.put("size", "10");

        //exchange_goods
        //rush_goods
        /*YSBSdk.getService(OAuthService.class).exchange_goods(map, new YRequestCallback<BaseBean<StroeCommentBean>>() {
            @Override
            public void onSuccess(BaseBean<StroeCommentBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    if (var1.getRecords().size()==0){
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                    commentData.clear();
                }

                commentData.addAll(var1.getRecords());
                if (var1.getRecords().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }
                adapter.notifyDataSetChanged();
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
                    mStateView.showRetry();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        cursor = System.currentTimeMillis() / 1000 + "";
        isRefresh = true;
        getData();
    }
}

package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.NewAdActivity;
import com.shijinsz.shijin.ui.ad.NewGraphicActivity;
import com.shijinsz.shijin.ui.ad.NewVideoActivity;
import com.shijinsz.shijin.ui.mine.adapter.DraftAdapter;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/14.
 */

public class DraftActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<Ads> list = new ArrayList<>();
    private DraftAdapter adapter;
    private TextView total;
    private boolean isRefresh=true;
    private View empty;
    @Override
    public int bindLayout() {
        return R.layout.draft_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.draft));
        empty=mInflater.inflate(R.layout.center_empty_layout,null);
        TextView tvEmpty = empty.findViewById(R.id.tv_empty);
        tvEmpty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_empty),null,null);
        tvEmpty.setText(getString(R.string.empty));
        TextView put=empty.findViewById(R.id.tv_put);
        put.setVisibility(View.VISIBLE);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NewAdActivity.class);
            }
        });
        adapter=new DraftAdapter(R.layout.draft_item,list);
        RelativeLayout head = (RelativeLayout) mInflater.inflate(R.layout.draft_header_layout,null);
        total=head.findViewById(R.id.tv_total);
        adapter.addHeaderView(head);
        recyclerView.setAdapter(adapter);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshHeader(new HeaderView(mContext));
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                cursor=System.currentTimeMillis()/1000+"";
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.get(position).getAd_type().equals("picture")) {
                    Intent intent = new Intent(mContext, NewGraphicActivity.class);
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("ads",list.get(position));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, NewVideoActivity.class);
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("ads",list.get(position));
                    startActivity(intent);
                }
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                DialogUtils dialogUtils=new DialogUtils(mActivity);
                dialogUtils.showCommentDialog("是否删除该草稿?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogUtils.dismissCommentDialog();
                        delete_draft(position);
                    }
                });
                return false;
            }
        });
        cursor=System.currentTimeMillis()/1000+"";
        mStateView.showLoading();
        getData();
    }

    private void delete_draft(int id) {
        Map map=new HashMap();
        map.put("mode","draft");
        YSBSdk.getService(OAuthService.class).delete_draft(list.get(id).getId(), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                list.remove(id);
                ToastUtil.showToast("删除成功");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    private String cursor="";
    private void getData() {
        Map map = new HashMap();
        map.put("mode","draft");
        map.put("cursor",cursor);
        map.put("size","10");
        YSBSdk.getService(OAuthService.class).get_draft(map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                total.setText(String.format(getString(R.string.total_draft),var1.getTotal_size()));
                if (isRefresh){
                    adapter.setEmptyView(empty);
                    list.clear();
                }
                if (var1.getAds()!=null) {
                    list.addAll(var1.getAds());
                    if (var1.getAds().size() < 10) {
                        refresh.setNoMoreData(true);
                    } else {
                        refresh.setNoMoreData(false);
                    }
                }else {
                    refresh.setNoMoreData(true);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext,var1,message);
                adapter.setEmptyView(empty);
                refresh.finishLoadMore(false);
                refresh.finishRefresh(false);
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh=false;
        cursor=list.get(list.size()-1).getCreated_at();
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh=true;
        cursor=System.currentTimeMillis()/1000+"";
        getData();
    }
}

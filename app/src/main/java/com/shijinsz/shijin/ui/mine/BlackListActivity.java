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
import com.hongchuang.ysblibrary.dao.BlackListBean;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.BlackListAdaper;
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
 * Created by yrdan on 2018/8/6.
 */

public class BlackListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.listview)
    PowerfulRecyclerView listview;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<BlackListBean.Records> list = new ArrayList<>();
    private BlackListAdaper adaper;
    private NoticeDialog mailDialog1;
    private RelativeLayout mailBoxLay;
    private boolean isReflash = true;
    private View empty;
    @Override
    public int bindLayout() {
        return R.layout.black_list_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.black_list));
        empty=mInflater.inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=empty.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.black_empty));
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_blacklist),null,null);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(mContext));
        adaper = new BlackListAdaper(R.layout.black_list_item, list);
        adaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",list.get(position).getBlack().getId());
                startActivity(intent);
            }
        });
        listview.setAdapter(adaper);
        adaper.setOnListen(new BlackListAdaper.onListen() {
            @Override
            public void callback(int pos) {
                showRemoveDialog(pos);
            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isReflash=true;
                cursor=System.currentTimeMillis()/1000  + "";
                getBlackData();
            }
        });
        mStateView.showLoading();
        cursor=System.currentTimeMillis()/1000  + "";
        getBlackData();
    }
    String cursor;
    private void getBlackData() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).user_black(map, new YRequestCallback<BlackListBean>() {
            @Override
            public void onSuccess(BlackListBean var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isReflash) {
                    adaper.setEmptyView(empty);
                    list.clear();
                }
                list.addAll(var1.getRecords());
                if (var1.getRecords().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    cursor=var1.getRecords().get(var1.getRecords().size()-1).getCreated_at();
                    refresh.setNoMoreData(false);
                }
                adaper.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                adaper.setEmptyView(empty);
                refresh.finishLoadMore();
                refresh.finishRefresh();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                }catch (Exception e){

                }

            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isReflash=true;
        cursor=System.currentTimeMillis()/1000  + "";
        getBlackData();

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isReflash = false;
        getBlackData();
    }

    public void showRemoveDialog(final int pos) {
        if (mailBoxLay == null)
            mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        if (mailDialog1 == null)
            mailDialog1 = new NoticeDialog(mContext);
        mailDialog1.showDialog(mailBoxLay, 0, 0);
        ((TextView) mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.remove_dialog));
        ((TextView) mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unblack(pos);
            }
        });
        ((TextView) mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog1.dismiss();
            }
        });
    }

    private void unblack(int pos) {
        Map map=new HashMap();
        YSBSdk.getService(OAuthService.class).unblack(list.get(pos).getBlack().getId(), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                list.remove(pos);
                ToastUtil.showToast("已移除黑名单");
                mailDialog1.dismiss();
                adaper.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                mailDialog1.dismiss();
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mailDialog1.dismiss();
            }
        });
    }
}

package com.shijinsz.shijin.ui.message;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.PointBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.message.adapter.PointMessageAdapter;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/8.
 */

public class ChangeMessageActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.delete)
    TextView delete;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private boolean isRefresh=false,isEdit = false;
    private List<PointBean.Messages> list = new ArrayList<>();
    private PointMessageAdapter adapter;
    private String cursor="";
    private View empty;

    @Override
    public int bindLayout() {
        return R.layout.point_message_activity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(getString(R.string.change));
        empty=mInflater.inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=empty.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.message_empty));
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_message),null,null);
        int num=Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change_number));
        int asset = Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_asset_total_number));
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_asset_total_number,asset-num+"");
        ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change_number,"0");
        showTitleLeftText(getString(R.string.select_all), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() > 0) {
                    boolean isALL = true;
                    for (PointBean.Messages bean : list) {
                        if (!bean.isCheck()) {
                            isALL = false;
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (isALL) {
                            list.get(i).setCheck(false);
                        } else {
                            list.get(i).setCheck(true);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        showTitleLeftTextVisibility(false);
        showTitleRightText(getString(R.string.edit), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit){
                    isEdit=false;
                    tvRight.setText(getString(R.string.edit));
                    showTitleLeftTextVisibility(false);
                    delete.setVisibility(View.GONE);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setShow(false);
                        list.get(i).setCheck(false);
                    }
                }else {
                    isEdit = true;
                    tvRight.setText(getString(R.string.finish));
                    showTitleLeftTextVisibility(true);
                    delete.setVisibility(View.VISIBLE);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setShow(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new PointMessageAdapter(R.layout.point_message_item,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isEdit) {
                    list.get(position).setCheck(!list.get(position).isCheck);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(mContext));
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
                getPoint(System.currentTimeMillis()/1000+"");
            }
        });
        isRefresh=true;
        mStateView.showLoading();
        getPoint(System.currentTimeMillis()/1000+"");
    }


    @OnClick(R.id.delete)
    public void onViewClicked() {
        showDialog();
    }
    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.delete_message_dialog));
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_messages();
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
    }
    private void delete_messages() {
        mStateView.showLoading();
        List<String> delete = new ArrayList<>();
        int j = 0;
        int size=list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i-j).isCheck()){
                delete.add(list.get(i-j).getId());
                list.remove(i-j);
                j++;
            }
        }
        Map map = new HashMap();
        map.put("mode","change");
//        map.put("message_ids",delete);
        YSBSdk.getService(OAuthService.class).delete_messages(map, delete,new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                adapter.notifyDataSetChanged();
                ToastUtil.showToast("删除成功");
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }


    public void getPoint(String cursor1) {
        Map map = new HashMap();
        map.put("cursor",cursor1);
        map.put("mode","person");
        map.put("size","10");
        YSBSdk.getService(OAuthService.class).change(map, new YRequestCallback<PointBean>() {
            @Override
            public void onSuccess(PointBean var1) {
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
                if (isRefresh){
                    adapter.setEmptyView(empty);
                    list.clear();
                    list.addAll(var1.getMessages());
                    isEdit=false;
                    tvRight.setText(getString(R.string.edit));
                    showTitleLeftTextVisibility(false);
                    delete.setVisibility(View.GONE);
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setShow(false);
                            list.get(i).setCheck(false);
                        }
                    }
                }else {
                    list.addAll(var1.getMessages());
                    if (isEdit){
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setShow(true);
                        }
                    }
                }


                if (var1.getMessages().size()<10){
                    refresh.setNoMoreData(true);
                }else {
                    refresh.setNoMoreData(false);
                }
                if(list.size()>0)
                cursor = list.get(list.size()-1).getCreated_at();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                adapter.setEmptyView(empty);
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    var1.printStackTrace();
                    refresh.finishRefresh();
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh=true;
        getPoint(System.currentTimeMillis()/1000+"");
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh=false;
        getPoint(cursor);
    }
}

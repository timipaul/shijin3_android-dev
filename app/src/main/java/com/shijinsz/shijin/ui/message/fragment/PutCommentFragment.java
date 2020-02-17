package com.shijinsz.shijin.ui.message.fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdCommentBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.message.CommentActivity;
import com.shijinsz.shijin.ui.message.adapter.GetCommentAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/9.
 */

public class PutCommentFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<AdCommentBean.MessagesBean> list =new ArrayList<>();
    private GetCommentAdapter adapter;
    private boolean isEdit=false;
    private View empty;
    @Override
    protected int provideContentViewId() {
        return R.layout.get_comment_fragment;
    }
    @Override
    protected void loadData() {
        empty=mInflater.from(mActivity).inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=empty.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.message_empty));
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_message),null,null);
        refresh.setRefreshHeader(new HeaderView(getContext()));
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        adapter = new GetCommentAdapter(R.layout.get_comment_item,list,2);
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
        ((CommentActivity)getActivity()).setOnPutListen(new CommentActivity.OnPutListen() {
            @Override
            public void callback(boolean isShow) {
                isEdit=isShow;
                for (int i = 0; i < list.size(); i++) {
                    if (!isShow){
                        list.get(i).setCheck(false);
                    }
                    list.get(i).setShow(isShow);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSelectAll() {
                if (list.size() > 0) {
                    boolean isALL = true;
                    for (AdCommentBean.MessagesBean bean : list) {
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

            @Override
            public void onDelete() {
                showDialog();
            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
                cursor = System.currentTimeMillis()/1000+"";
                getData();
            }
        });
        cursor = System.currentTimeMillis()/1000+"";
        mStateView.showLoading();
        getData();
    }
    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(getContext());
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
        map.put("mode","comment_ad");
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
                ErrorUtils.error(getContext(),var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
    private void getData() {
        Map map = new HashMap();
        map.put("cursor",cursor);
        map.put("size","10");
        map.put("mode","person");
        YSBSdk.getService(OAuthService.class).comment_ad(map, new YRequestCallback<AdCommentBean>() {
            @Override
            public void onSuccess(AdCommentBean var1) {
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
                if (isRefresh){
                    adapter.setEmptyView(empty);
                    list.clear();
                }
                list.addAll(var1.getMessages());
                if (isEdit){
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setShow(true);
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
                ErrorUtils.error(getContext(),var1,message);
                refresh.finishRefresh();
                adapter.setEmptyView(empty);
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    refresh.finishRefresh();
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                }catch (Exception e){

                }

            }
        });
    }

    private String cursor="";
    private boolean isRefresh=true;
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh=true;
        cursor = System.currentTimeMillis()/1000+"";
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh=false;
        getData();
    }
}

package com.shijinsz.shijin.ui.message.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
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

public class GetCommentFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<AdCommentBean.MessagesBean> list =new ArrayList<>();
    private GetCommentAdapter adapter;
    private boolean isEdit=false;
    private EditText msg;
    private TextView zy_btn;
    private View empty;

    @Override
    protected int provideContentViewId() {
        return R.layout.get_comment_fragment;
    }
    @Override
    protected void loadData() {
        empty= mInflater.from(mActivity).inflate(R.layout.center_empty_layout,null);
        TextView tv_empty=empty.findViewById(R.id.tv_empty);
        tv_empty.setText(getString(R.string.message_empty));
        tv_empty.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.icon_placeholder_message),null,null);
        refresh.setRefreshHeader(new HeaderView(getContext()));
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        adapter = new GetCommentAdapter(R.layout.get_comment_item,list,1);
        recyclerView.setAdapter(adapter);
        adapter.setOnRequestListen(new GetCommentAdapter.OnRequestListen() {
            @Override
            public void callback(int pos,int type) {
                showEarlyDialog(pos,type);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isEdit) {
                    list.get(position).setCheck(!list.get(position).isCheck);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ((CommentActivity)getActivity()).setOnGetListen(new CommentActivity.OnGetListen() {
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
                cursor = System.currentTimeMillis()/1000+"";
                isRefresh=true;
                getData();
            }
        });
        cursor = System.currentTimeMillis()/1000+"";
        mStateView.showLoading();
        getData();
    }

    private void getData() {
        Map map = new HashMap();
        map.put("cursor",cursor);
        map.put("size","10");
        map.put("mode","person");
        YSBSdk.getService(OAuthService.class).ad_comment(map, new YRequestCallback<AdCommentBean>() {
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
                ErrorUtils.error(getContext(),var1,message);
                mStateView.showContent();
                adapter.setEmptyView(empty);
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showRetry();
                    refresh.finishRefresh();
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

    private PopupWindow window;
    public void showEarlyDialog(final int pos, final int type) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.putmessage, null);


        msg = (EditText) popupView.findViewById(R.id.msg);
        zy_btn = (TextView) popupView.findViewById(R.id.put);
        msg.setHint(getContext().getString(R.string.request)+" "+list.get(pos).getUser().getNickname()+":");
        if (window==null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, (int)getContext().getResources().getDimension(R.dimen.dp_50), true);

        window.showAtLocation(getStateViewRoot(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(00000000));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
//        msg.setFocusable(true);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(msg, InputMethodManager.SHOW_FORCED);
        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (msg.getText().toString().length() > 0) {
                    zy_btn.setEnabled(true);
                } else {
                    zy_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        zy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childComments(pos,type,msg.getText().toString());
//                putMessage(msg.getText().toString());
            }
        });
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
        map.put("mode","ad_comment");
//        map.put("message_ids",delete);
        YSBSdk.getService(OAuthService.class).delete_messages(map,delete, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                adapter.notifyDataSetChanged();
                ToastUtil.showToast("删除成功");
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    public void childComments(int pos,int type,String comment){
        Map map = new HashMap();
        map.put("mode","person");
        map.put("father_comment_id",list.get(pos).getFather_comment().getId());
        if (type==1){
            map.put("child_comment_id",list.get(pos).getChild_comment().getId());
        }
        map.put("comment",comment);
        YSBSdk.getService(OAuthService.class).child_comments(list.get(pos).getAd().getId(),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ToastUtil.showToast("发表成功");
                window.dismiss();
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
}

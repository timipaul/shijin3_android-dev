package com.shijinsz.shijin.ui.task;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.ui.task.adapter.InvitionRecordAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.ShareDialog;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InviteRecordActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.invite_money)
    TextView inviteMoney;
    @BindView(R.id.invite_people)
    TextView invitePeople;
    @BindView(R.id.empty)
    LinearLayout empty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    String money = "";
    String people = "";
    public boolean isRefresh = true;
    public String cursor = "";
    private List<InvitationBean> list = new ArrayList<>();
    private InvitionRecordAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.invited_friend;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.invited_friend));
        money = getIntent().getStringExtra("money")+"";
        people = getIntent().getStringExtra("people")+"";
        String srt = String.format(getString(R.string.invite_money), money);
        SpannableStringBuilder style = new SpannableStringBuilder(srt);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, money.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        inviteMoney.setText(style);
        String srt1 = String.format(getString(R.string.invite_people), people);
        SpannableStringBuilder style1 = new SpannableStringBuilder(srt1);
        style1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, people.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        invitePeople.setText(style1);
        refresh.setOnRefreshLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(mContext));
        adapter = new InvitionRecordAdapter(R.layout.invited_friend_item, list);
        recyclerView.setAdapter(adapter);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh=true;
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
        map.put("mode", "person");
        map.put("cursor", cursor);
        map.put("size", "10");

        System.out.println("时间值: " + cursor);

        YSBSdk.getService(OAuthService.class).invitation(map, new YRequestCallback<BaseBean<InvitationBean>>() {
            @Override
            public void onSuccess(BaseBean<InvitationBean> var1) {
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
                if (isRefresh) {
                    if (var1.getRecords().size() == 0) {
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    list.clear();
                }
                if(var1.getRecords().size()>0){
                    cursor = var1.getRecords().get(var1.getRecords().size() - 1).getCreated_at();
                    for (int i = 0; i < var1.getRecords().size(); i++) {
                        var1.getRecords().get(i).setCreated_at(TimeUtil.format(Long.valueOf(var1.getRecords().get(i).getCreated_at()) * 1000, "yyyy-MM-dd"));
                    }
                }
                list.addAll(var1.getRecords());
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
                refresh.finishRefresh(false);
                refresh.finishLoadMore(false);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    var1.printStackTrace();
                    mStateView.showContent();
                    mStateView.showRetry();
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        cursor = System.currentTimeMillis()/1000 + "";
        getData();
    }


    @OnClick(R.id.tv_put)
    public void onViewClicked() {
        getShare();
    }
    //获取分享信息
    public void getShare(){
        mStateView.showLoading();
        String mode ="share_to_friend";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                mStateView.showContent();
                new ShareDialog(mActivity).showWithdrapDialog(mActivity,3,var1.getShare_title(),var1.getShare_info(),var1.getShare_pic(), Comment.url+"invitation_registration?nickname="+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname)+"&username="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+"&imageurl="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl));
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
}

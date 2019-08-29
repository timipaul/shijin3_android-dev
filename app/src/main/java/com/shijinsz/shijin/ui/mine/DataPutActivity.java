package com.shijinsz.shijin.ui.mine;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdReleaseBean;
import com.hongchuang.ysblibrary.utils.PopSetectDate;
import com.hongchuang.ysblibrary.utils.SetText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.DataPutAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/14.
 */

public class DataPutActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.tv_show_num)
    TextView tvShowNum;
    @BindView(R.id.tv_click_new)
    TextView tvClickNew;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_near_7day)
    TextView tvNear7day;
    @BindView(R.id.tv_near_30day)
    TextView tvNear30day;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.total_num)
    TextView totalNum;
    @BindView(R.id.use_num)
    TextView useNum;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1;
    private boolean is7day = true, is30day = false,isChange=true;
    private PopSetectDate popSetectDate, popSetectDateEnd;
    private long endtime;
    private long time;
    private List<AdReleaseBean.Records> list = new ArrayList<>();
    private DataPutAdapter adapter;
    @Override
    public int bindLayout() {
        return R.layout.data_put_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.data_put));
        View empty = mInflater.inflate(R.layout.data_empty_layout,null);
        tvShowNum.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_history_recharge_change));
        tvClickNew.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
        adapter = new DataPutAdapter(R.layout.data_put_item, list);
        adapter.setEmptyView(empty);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        ClassicsFooter classicsFooter=new ClassicsFooter(mContext);
        classicsFooter.setFinishDuration(0);
        refresh.setRefreshFooter(classicsFooter);
        refresh.setOnLoadMoreListener(this);
        refresh.setEnableRefresh(false);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent= new Intent(mContext, VideoDetailActivity.class);
//                intent.putExtra("id",list.get(position).getAd().getId());
//                startActivity(intent);
            }
        });
        endtime = System.currentTimeMillis() / 1000;
        time = System.currentTimeMillis() / 1000 - 3600 * 24 * 7;
        tvEndTime.setText(TimeUtil.format(endtime * 1000, "yyyy年MM月dd日"));
        tvStartTime.setText(TimeUtil.format((endtime - 3600 * 24 * 7) * 1000, "yyyy年MM月dd日"));
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                page=1;
                getAdData();
            }
        });
        getAdData();
    }
    private String soft="-created_at";
    private void getAdData() {
        if (page==1){
            mStateView.showLoading();
        }
        Map map = new HashMap();
        if (isChange)
        map.put("mode", "change");
        else
            map.put("mode", "point");
        if (is7day) {
            map.put("start_time", System.currentTimeMillis() / 1000 - 3600 * 24 * 7);
            map.put("end_time", System.currentTimeMillis() / 1000);
        } else if (is30day) {
            map.put("start_time", System.currentTimeMillis() / 1000 - 3600 * 24 * 30);
            map.put("end_time", System.currentTimeMillis() / 1000);
        } else {
            map.put("start_time", time);
            map.put("end_time", endtime+3600 * 24);
        }

        map.put("sort", soft);
        map.put("page_size", "10");
        map.put("current_page", page + "");
        YSBSdk.getService(OAuthService.class).ad_release(map, new YRequestCallback<AdReleaseBean>() {
            @Override
            public void onSuccess(AdReleaseBean var1) {
                if (isChange) {
                    tvShowNum.setText(var1.getTotal_history_change());
                    tvClickNew.setText(var1.getTotal_change());
                }else {
                    tvShowNum.setText(var1.getTotal_history_points());
                    tvClickNew.setText(var1.getTotal_points());
                }
                float num=Float.valueOf(var1.getTotal_history_reward())-Float.valueOf(var1.getTotal_reward());
                DecimalFormat fnum  =   new  DecimalFormat("##0.00");
                String   dd=fnum.format(num);
                useNum.setText(dd);
                total.setText("/"+var1.getTotal_history_reward());
                mStateView.showContent();
                refresh.finishLoadMore();
                if (page == 1) {
                    list.clear();
                }
                if (var1.getRecords().size()>0) {
                    for (int i = 0; i < var1.getRecords().size(); i++) {
                        var1.getRecords().get(i).setCreated_at(TimeUtil.format(Long.valueOf(var1.getRecords().get(i).getCreated_at())*1000,"yyyy-MM-dd HH-mm"));
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
                mStateView.showContent();
                ErrorUtils.error(mContext,var1,message);
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.tv_change, R.id.tv_point, R.id.tv_near_7day, R.id.tv_near_30day, R.id.tv_start_time, R.id.tv_end_time, R.id.tv_sort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change:
                if (isChange){
                    return;
                }else {
                    isChange=true;
                    tvChange.setTextColor(getResources().getColor(R.color.white));
                    tvChange.setBackground(getResources().getDrawable(R.drawable.bt_choice_day));
                    tvPoint.setTextColor(getResources().getColor(R.color.text_33));
                    tvPoint.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                     tv1.setText(getString(R.string.total_change));
                    tv2.setText(getString(R.string.last_change));
                    page = 1;
                    getAdData();
                }
                break;
            case R.id.tv_point:
                if (isChange){
                    isChange=false;
                    tvPoint.setTextColor(getResources().getColor(R.color.white));
                    tvPoint.setBackground(getResources().getDrawable(R.drawable.bt_choice_day));
                    tvChange.setTextColor(getResources().getColor(R.color.text_33));
                    tvChange.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
//                    tvShowNum.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_history_recharge_points));
//                    tvClickNew.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points));
                    tv1.setText(getString(R.string.total_point));
                    tv2.setText(getString(R.string.last_point));
                    page = 1;
                    getAdData();
                }else {
                    return;
                }
                break;
            case R.id.tv_near_7day:
                if (is7day) {
                    return;
                } else {
                    is30day = false;
                    is7day = true;
                    tvNear7day.setTextColor(getResources().getColor(R.color.white));
                    tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_choice_day));
                    tvNear30day.setTextColor(getResources().getColor(R.color.text_33));
                    tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                    page = 1;
                    getAdData();
                }
                break;
            case R.id.tv_near_30day:
                if (is30day) {
                    return;
                } else {
                    is30day = true;
                    is7day = false;
                    tvNear30day.setTextColor(getResources().getColor(R.color.white));
                    tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_choice_day));
                    tvNear7day.setTextColor(getResources().getColor(R.color.text_33));
                    tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                    page = 1;
                    getAdData();
                }
                break;
            case R.id.tv_start_time:
                if (popSetectDate == null)
                    popSetectDate = new PopSetectDate(mActivity);
                popSetectDate.showPopupWindow(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), new SetText() {
                    @Override
                    public void listenResult(String text) {
                        time = TimeUtil.parseTime(text, "yyyy.MM.dd");
                        if (time > System.currentTimeMillis() / 1000) {
                            time = System.currentTimeMillis() / 1000;
                            tvStartTime.setText(TimeUtil.format(endtime * 1000, "yyyy年MM月dd日"));
                            return;
                        }
                        tvStartTime.setText(TimeUtil.format(time * 1000, "yyyy年MM月dd日"));
                        if (time > endtime) {

                        } else {
                            is7day = false;
                            is30day = false;
                            tvNear30day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            tvNear7day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            page = 1;
                            getAdData();
                        }
                    }
                });
                break;
            case R.id.tv_end_time:
                if (popSetectDateEnd == null)
                    popSetectDateEnd = new PopSetectDate(mActivity);
                popSetectDateEnd.showPopupWindow(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), new SetText() {
                    @Override
                    public void listenResult(String text) {
                        endtime = TimeUtil.parseTime(text, "yyyy.MM.dd");
                        if (endtime > System.currentTimeMillis() / 1000) {
                            endtime = System.currentTimeMillis() / 1000;
                            tvEndTime.setText(TimeUtil.format(endtime * 1000, "yyyy年MM月dd日"));
                            return;
                        }
                        tvEndTime.setText(TimeUtil.format(endtime * 1000, "yyyy年MM月dd日"));
                        if (time > endtime) {

                        } else {
                            is7day = false;
                            is30day = false;
                            tvNear30day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear30day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            tvNear7day.setTextColor(getResources().getColor(R.color.text_33));
                            tvNear7day.setBackground(getResources().getDrawable(R.drawable.bt_unchoice_day));
                            page = 1;
                            getAdData();
                        }
                    }
                });
                break;
            case R.id.tv_sort:
                showEarlyDialog();
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getAdData();
    }

    private PopupWindow window;

    public void showEarlyDialog() {
        View popupView = mInflater.inflate(R.layout.soft_pop2, null);
        if (window == null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView cancel = popupView.findViewById(R.id.cancel);
        TextView tv1 = popupView.findViewById(R.id.tv1);
        TextView tv2 = popupView.findViewById(R.id.tv2);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-reward";
                tvSort.setText(getString(R.string.up_to_down));
                getAdData();
                window.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "+reward";
                tvSort.setText(getString(R.string.down_to_up));
                getAdData();
                window.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }
}

package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
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
import com.hongchuang.ysblibrary.model.model.bean.YesterdayBean;
import com.hongchuang.ysblibrary.utils.PopSetectDate;
import com.hongchuang.ysblibrary.utils.SetText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.mine.adapter.DataAdAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/13.
 */

public class DataAdActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.tv_show_num)
    TextView tvShowNum;
    @BindView(R.id.tv_click_new)
    TextView tvClickNew;
    @BindView(R.id.tv_share_num)
    TextView tvShareNum;
    @BindView(R.id.tv_star_num)
    TextView tvStarNum;
    @BindView(R.id.tv_like_num)
    TextView tvLikeNum;
    @BindView(R.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R.id.tv_near_7day)
    TextView tvNear7day;
    @BindView(R.id.tv_near_30day)
    TextView tvNear30day;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.total_num)
    TextView totalNum;
    private List<AdReleaseBean.Records> list = new ArrayList<>();
    private DataAdAdapter adapter;
    private int page = 1;
    private boolean is7day = true, is30day = false;
    private PopSetectDate popSetectDate, popSetectDateEnd;
    private long endtime;
    private long time;

    @Override
    public int bindLayout() {
        return R.layout.data_ad_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.data_ad));
        Long time = System.currentTimeMillis()-24*60*60*1000;
        tvUpload.setText("数据更新至" + TimeUtil.format(time, "yyyy/MM/dd"));
        adapter = new DataAdAdapter(R.layout.data_ad_item, list);
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
                list.get(position).setShow(!list.get(position).isShow());
                adapter.notifyDataSetChanged();
            }
        });
        endtime = System.currentTimeMillis() / 1000;
        time = System.currentTimeMillis() / 1000 - 3600 * 24 * 7;
        tvEndTime.setText(TimeUtil.format(endtime * 1000, "yyyy年MM月dd日"));
        tvStartTime.setText(TimeUtil.format((endtime - 3600 * 24 * 7) * 1000, "yyyy年MM月dd日"));
//        recyclerView.setHasFixedSize(true);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                page=1;
                getAdData();
            }
        });
        getYesterDayData();
        getAdData();
    }

    private void getAdData() {
        if (page==1){
            mStateView.showLoading();
        }
        Map map = new HashMap();
        map.put("mode", "all");
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
                mStateView.showContent();
                totalNum.setText("共"+var1.getTotal_size()+"条资讯");
                refresh.finishLoadMore();
                if (page == 1) {
                    list.clear();
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
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try{
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                }catch (Exception e){

                }

            }
        });
    }

    private void getYesterDayData() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).yesterday_record(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID),map, new YRequestCallback<YesterdayBean>() {
            @Override
            public void onSuccess(YesterdayBean var1) {
                tvShowNum.setText(var1.getAd_exposure_number());
                tvClickNew.setText(var1.getAd_click_number());
                tvShareNum.setText(var1.getAd_share_number());
                tvStarNum.setText(var1.getAd_collection_number());
                tvLikeNum.setText(var1.getAd_like_number());
                tvCommentNum.setText(var1.getAd_comment_number());
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }


    @OnClick({R.id.tv_near_7day, R.id.tv_near_30day, R.id.tv_start_time, R.id.tv_end_time, R.id.tv_sort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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

    private String soft = "-created_at";
    private PopupWindow window;

    public void showEarlyDialog() {
        View popupView = mInflater.inflate(R.layout.soft_pop, null);
        if (window == null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView cancel = popupView.findViewById(R.id.cancel);
        TextView tv1 = popupView.findViewById(R.id.tv1);
        TextView tv2 = popupView.findViewById(R.id.tv2);
        TextView tv3 = popupView.findViewById(R.id.tv3);
        TextView tv4 = popupView.findViewById(R.id.tv4);
        TextView tv5 = popupView.findViewById(R.id.tv5);
        TextView tv6 = popupView.findViewById(R.id.tv6);
        TextView tv7 = popupView.findViewById(R.id.tv7);
        TextView tv8 = popupView.findViewById(R.id.tv8);
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
                soft = "-answer";
                tvSort.setText(getString(R.string.ans_num));
                getAdData();
                window.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-answer_right";
                tvSort.setText(getString(R.string.true_num));
                getAdData();
                window.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-ad_exposure";
                tvSort.setText(getString(R.string.ad_show_num));
                getAdData();
                window.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-ad_click";
                tvSort.setText(getString(R.string.ad_click_num));
                getAdData();
                window.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-ad_like";
                tvSort.setText(getString(R.string.ad_share_num));
                getAdData();
                window.dismiss();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-ad_share";
                tvSort.setText(getString(R.string.ad_star_num));
                getAdData();
                window.dismiss();
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-ad_comment";
                tvSort.setText(getString(R.string.ad_like_num));
                getAdData();
                window.dismiss();
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                soft = "-ad_collection";
                tvSort.setText(getString(R.string.ad_comment));
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

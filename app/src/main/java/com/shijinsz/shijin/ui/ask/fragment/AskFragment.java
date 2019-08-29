package com.shijinsz.shijin.ui.ask.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShieidsBean;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.ask.adapter.AskAdapter;
import com.shijinsz.shijin.ui.home.NewGuideActivity;
import com.shijinsz.shijin.ui.home.SearchActivity;
import com.shijinsz.shijin.ui.home.adapter.NewsAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.LoginUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/30.
 */

public class AskFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = "AskFragment";
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    LucklyPopopWindow mLucklyPopopWindow;
    private AskAdapter adapter;
    private List<Ads> list = new ArrayList<>();
    private String cursor = "";
    private ShieidsBean shieidsBean;
    private boolean isRefresh = true;
    private View footView;

    @BindView(R.id.tv_locate)
    TextView tvLocate;
    @BindView(R.id.tv_guide)
    TextView tvGuide;
    @BindView(R.id.tv_selete)
    TextView tvSelete;

    @Override
    protected int provideContentViewId() {
        return R.layout.my_follow_activity;
    }


    @Override
    protected void loadData() {
        mInflater = LayoutInflater.from(getContext());
        footView = mInflater.inflate(R.layout.empty_layout, null);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(getContext()));
        adapter = new AskAdapter(R.layout.home_big_pic_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, VideoDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("purpose", "purpose");
                startActivity(intent);
            }
        });
        adapter.isUseEmpty(true);
        adapter.setEmptyView(footView);
        adapter.setHeaderAndEmpty(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setCloseClickListen(new NewsAdapter.OnCloseClickListen() {
            @Override
            public void onClick(View view, final int pos) {
                mLucklyPopopWindow = new LucklyPopopWindow(getContext(), list.get(pos).getRelease_record().getNickname(), list.get(pos).getInterests());
                DisplayMetrics dm = getResources().getDisplayMetrics();
                mLucklyPopopWindow.setWidth(dm.widthPixels);
                //监听事件
                mLucklyPopopWindow.setOnItemClickListener(new LucklyPopopWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(boolean see, boolean interested, boolean content_level, boolean black_user, boolean black_label1, boolean black_label2, boolean black_label3) {
                        mLucklyPopopWindow.dismiss();
                        if (!LoginUtil.isLogin(mActivity)) {
                            return;
                        }
                        String user_id = "";
                        List<String> interests = new ArrayList<>();
                        if (black_user) {
                            user_id = list.get(pos).getUser_id();
                        }
                        if (black_label1) {
                            interests.add(list.get(pos).getInterests().get(0));
                        }
                        if (black_label2) {
                            interests.add(list.get(pos).getInterests().get(1));
                        }
                        if (black_label3) {
                            interests.add(list.get(pos).getInterests().get(2));
                        }
                        shieidsBean = new ShieidsBean(see, interested, content_level, user_id, interests);
                        black_ad(pos);

                    }
                });

                mLucklyPopopWindow.showAtLocation(getActivity().getWindow().getDecorView(), view);
            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                cursor = System.currentTimeMillis() / 1000 + 60000 + "";
                isRefresh = true;
                getAds();
//                getBanner();
            }
        });
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        mStateView.showLoading();

        setMenu();
        getAds();
    }

    private void setMenu(){
        String caty = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_LOCATE);
        tvLocate.setText(caty);

        tvGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, NewGuideActivity.class));
            }
        });

        tvSelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
    }

    private void black_ad(final int pos) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("shields", shieidsBean);
        String id = "";
        id = list.get(pos).getId();
        YSBSdk.getService(OAuthService.class).shield(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                list.remove(pos);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
//                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                var1.printStackTrace();
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        getAds();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        if (list.size() > 0) {
            cursor = list.get(list.size() - 1).getCreated_at();
            getAds();
        } else {
            refresh.setNoMoreData(true);
            refresh.finishLoadMore();
        }
    }

    private void getAds() {

        Map map = new HashMap();
//        map.put("mode", "recommend");
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).ads("video",map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {
                mStateView.showContent();
                refresh.finishLoadMore();

                refresh.finishRefresh();
                if (isRefresh) {
                    refresh.setNoMoreData(false);
                    list.clear();
                    list.addAll(var1.getAds());
//                        if (list.size() == 0) {
//                            banner.setVisibility(View.GONE);
//                        } else {
//                            banner.setVisibility(View.VISIBLE);
//                        }
                    if (list.size() < 10) {
                        refresh.setNoMoreData(true);
                    }
                } else {
                    list.addAll(var1.getAds());
                    if (var1.getAds().size() < 10) {
                        refresh.setNoMoreData(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
//                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showRetry();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                    var1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

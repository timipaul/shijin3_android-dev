package com.shijinsz.shijin.ui.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.Constants;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShieidsBean;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import com.qq.e.comm.util.GDTLogger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
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
 * Created by yrdan on 2018/11/12.
 */

public class NewListFragment2 extends BaseFragment implements OnRefreshListener, OnLoadMoreListener ,NativeExpressAD.NativeExpressADListener{
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private static final String TAG = "NewListFragment";
    public static NewListFragment2 instance = null;
    LucklyPopopWindow mLucklyPopopWindow;
    private NewsAdapter adapter;
    private List<Ads> list = new ArrayList<>();
    private String cursor = "";
    private ShieidsBean shieidsBean;
    private boolean isRefresh = true;
    private long ad_cursor;
    @Override
    protected int provideContentViewId() {
        return R.layout.new_list_fragment;
    }
    private String type;

    public static final int MAX_ITEMS = 50;
    public static int AD_COUNT = 10;    // 加载广告的条数，取值范围为[1, 10]
    public static int FIRST_AD_POSITION = 5; // 第一条广告的位置
    public static int ITEMS_PER_AD = 10;     // 每间隔10个条目插入一条广告

    private CustomAdapter mAdapter;
    private NativeExpressAD mADManager;
    private List<NativeExpressADView> mAdViewList;
    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap = new HashMap<NativeExpressADView, Integer>();

    private View footView;
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    public static NewListFragment2 getInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(TAG, type);
        NewListFragment2 videoArticleView = new NewListFragment2();
        videoArticleView.setArguments(bundle);
        return videoArticleView;
    }



    @Override
    protected void loadData() {

        type=getArguments().getString(TAG);
        mInflater = LayoutInflater.from(getContext());
        footView = mInflater.inflate(R.layout.empty_layout, null);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setRefreshHeader(new HeaderView(getContext()));

        adapter = new NewsAdapter(R.layout.home_big_pic_item, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!list.get(position).getAd_mode().equals("adhub")){
                    for (String s : list.get(position).getTags()) {
                        if (s.equals("ad")){
                            Uri uri= Uri.parse(list.get(position).getUrl());
                            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent2);
                            return;
                        }
                    }
                    Intent intent = new Intent(mActivity, VideoDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("purpose", "purpose");
                    startActivity(intent);
                }
            }
        });
        adapter.isUseEmpty(true);
        adapter.setEmptyView(footView);
        adapter.setHeaderAndEmpty(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        mAdapter = new CustomAdapter(list);

        recyclerView.setAdapter(mAdapter);

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
                isRefresh = true;
                getData(System.currentTimeMillis() / 1000 + 60000 + "");
//                getBanner();
            }
        });
        mStateView.showLoading();
        getData(System.currentTimeMillis() / 1000 + 60000 + "");

        //long temp_data = System.currentTimeMillis() / 1000;
        /*System.out.println("上一时间:" + temp_data);
        System.out.println("下一时间:" + ad_cursor);

        if(ad_cursor == 0 || temp_data - ad_cursor > 5000){
            ad_cursor = temp_data;
            initNativeExpressAD();
        }*/
        initNativeExpressAD();
    }




    /**
     * 如果选择支持视频的模版样式，请使用{@link }
     */
    private void initNativeExpressAD() {
        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT
        mADManager = new NativeExpressAD(getContext(), adSize, Constants.APPID, Constants.NativePosID, this);
        //mADManager.setMaxVideoDuration(getMaxVideoDuration());
        mADManager.loadAD(AD_COUNT);
    }

    private int getMaxVideoDuration() {
        //return getActivity().getIntent().getIntExtra(Constants.MAX_VIDEO_DURATION, 0);
        return 0;
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

        getData(System.currentTimeMillis() / 1000 + 60000 + "");
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
            if (list.size() > 0) {
                getData(list.get(list.size() - 1).getCreated_at());
            } else {
                refresh.setNoMoreData(true);
                refresh.finishLoadMore();
            }
    }

    private void getData(String cursor1) {

        String channel = type;
        Map map = new HashMap();
//        map.put("mode", "index");
        map.put("cursor", cursor1);
        map.put("size", "15");
        map.put("channel",channel);
//        if (!type.equals("individuation")&&!type.equals("follow")){
////            channel="dynamic";
////            map.put("category",type);
////        }
        YSBSdk.getService(OAuthService.class).ads(channel,map, new YRequestCallback<AdsBean>() {
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
                        //这里应是设置不加载了
                        refresh.setNoMoreData(true);
                    }
                }


                long temp_data = System.currentTimeMillis() / 1000;
                System.out.println("时间: " + (temp_data - ad_cursor));
                if(ad_cursor == 0 || temp_data - ad_cursor > 50){
                    System.out.println("加载广告*****************");
                    AD_COUNT++;
                    initNativeExpressAD();
                    ad_cursor = temp_data;

                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
//                ToastUtil.showToast(message);
                System.out.println("1  异常错误");
            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("2  异常错误");
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



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdViewList != null) {
            for (NativeExpressADView view : mAdViewList) {
                view.destroy();
            }
        }
    }

    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        Log.i(TAG, "onADLoaded: " + adList.size());
        mAdViewList = adList;
        for (int i = 0; i < mAdViewList.size(); i++) {
            int position = FIRST_AD_POSITION + ITEMS_PER_AD * i;
            if (position < list.size()) {
                NativeExpressADView view = mAdViewList.get(i);
                GDTLogger.i("ad load[" + i + "]: " + getAdInfo(view));
                if (view.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                    view.setMediaListener(mediaListener);
                }
                mAdViewPositionMap.put(view, position); // 把每个广告在列表中位置记录下来
                mAdapter.addADViewToPosition(position, mAdViewList.get(i));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ")
                        .append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            return infoBuilder.toString();
        }
        return null;
    }

    private String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition());
            return videoBuilder.toString();
        }
        return null;
    }

    @Override
    public void onRenderFail(NativeExpressADView adView) {
        Log.i(TAG, "onRenderFail: " + adView.toString());
    }

    @Override
    public void onRenderSuccess(NativeExpressADView adView) {
        Log.i(TAG, "onRenderSuccess: " + adView.toString() + ", adInfo: " + getAdInfo(adView));
    }

    @Override
    public void onADExposure(NativeExpressADView adView) {
        Log.i(TAG, "onADExposure: " + adView.toString());
    }

    @Override
    public void onADClicked(NativeExpressADView adView) {
        Log.i(TAG, "onADClicked: " + adView.toString());
    }

    @Override
    public void onADClosed(NativeExpressADView adView) {
        Log.i(TAG, "onADClosed: " + adView.toString());
        if (mAdapter != null) {
            int removedPosition = mAdViewPositionMap.get(adView);
            mAdapter.removeADView(removedPosition, adView);
        }
    }

    @Override
    public void onADLeftApplication(NativeExpressADView adView) {
        Log.i(TAG, "onADLeftApplication: " + adView.toString());
    }

    @Override
    public void onADOpenOverlay(NativeExpressADView adView) {
        Log.i(TAG, "onADOpenOverlay: " + adView.toString());
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
        Log.i(TAG, "onADCloseOverlay");
    }

    @Override
    public void onNoAD(AdError adError) {
        Log.i(
                TAG,
                String.format("onNoAD, error code: %d, error msg: %s", adError.getErrorCode(),
                        adError.getErrorMsg()));
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoInit: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoLoading: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
            Log.i(TAG, "onVideoReady: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoStart: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPause: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoComplete: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
            Log.i(TAG, "onVideoError");
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageOpen");
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageClose");
        }
    };

    /** RecyclerView的Adapter */
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        static final int TYPE_DATA = 0;
        static final int TYPE_AD = 1;

        private static final int BIG_IMG = 100;
        private static final int THREE_IMG = 200;
        private static final int SMALL_IMG = 300;
        private static final int HOT = 400;
        private static final int ADHUB =500;
        private static final int ADHUB2 =600;

        private List<Object> mData;

        public CustomAdapter(List list) {
            mData = list;
        }

        // 把返回的NativeExpressADView添加到数据集里面去
        public void addADViewToPosition(int position, NativeExpressADView adView) {
            if (position >= 0 && position < mData.size() && adView != null) {
                mData.add(position, adView);
            }
        }

        // 移除NativeExpressADView的时候是一条一条移除的
        public void removeADView(int position, NativeExpressADView adView) {
            mData.remove(position);
            mAdapter.notifyItemRemoved(position); // position为adView在当前列表中的位置
            mAdapter.notifyItemRangeChanged(0, mData.size() - 1);
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {

            if(mData.get(position) instanceof NativeExpressADView){
                return TYPE_AD;
            }else{

                for (String s : ((Ads) mData.get(position)).getTags()) {
                    if (s.equals("fixed")){
                        return HOT;
                    }
                }

                String mode = ((Ads) mData.get(position)).getAd_mode();
                if (mode.equals("one_small_pic")) {
                    return SMALL_IMG;
                } else if (mode.equals("one_big_pic")) {
                    return BIG_IMG;
                } else if (mode.equals("adhub")){
                    return ADHUB;
                }  else if (mode.equals("adhub2")){
                    return ADHUB2;
                }  else {
                    return THREE_IMG;
                }
            }
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
            int type = getItemViewType(position);
            if (TYPE_AD == type) {
                final NativeExpressADView adView = (NativeExpressADView) mData.get(position);
                mAdViewPositionMap.put(adView, position); // 广告在列表中的位置是可以被更新的
                if (customViewHolder.container.getChildCount() > 0
                        && customViewHolder.container.getChildAt(0) == adView) {
                    return;
                }

                if (customViewHolder.container.getChildCount() > 0) {
                    customViewHolder.container.removeAllViews();
                }

                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                customViewHolder.container.addView(adView);
                adView.render(); // 调用render方法后sdk才会开始展示广告
            } else {

                Ads ads = (Ads) mData.get(position);

                switch (type){
                    case HOT:
                        customViewHolder.red_type.setVisibility(View.VISIBLE);
                        customViewHolder.red_type.setText("置顶");
                        break;
                    case THREE_IMG:
                        customViewHolder.red_type.setVisibility(View.GONE);
                        Glide.with(getContext()).load(ads.getAd_title_pics().get(0)).into(customViewHolder.img1);
                        Glide.with(getContext()).load(ads.getAd_title_pics().get(1)).into(customViewHolder.img2);
                        Glide.with(getContext()).load(ads.getAd_title_pics().get(2)).into(customViewHolder.img3);
                        customViewHolder.img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toClickIntent(position);
                            }
                        });
                        customViewHolder.img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toClickIntent(position);
                        }
                    });
                        customViewHolder.img3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toClickIntent(position);
                        }
                    });
                        break;
                    case BIG_IMG:
                        customViewHolder.red_type.setVisibility(View.GONE);
                        if (ads.getAd_type().equals("picture")) {
                            customViewHolder.img_big.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            //helper.setGone(R.id.im_play,false);
                        }else {
                            customViewHolder.img_big.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            //helper.setGone(R.id.im_play,true);
                        }
                        //helper.setGone(R.id.tv_red_type,false);
                        //helper.setText(R.id.tv_red_type,"广告");
                        Glide.with(getContext()).load(ads.getAd_title_pics().get(0)).into(customViewHolder.img_big);
                        customViewHolder.img_big.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toClickIntent(position);
                            }
                        });
                        break;
                    case SMALL_IMG:
                        customViewHolder.red_type.setVisibility(View.GONE);
                        Glide.with(getContext()).load(ads.getAd_title_pics().get(0)).into(customViewHolder.img_small);
                        customViewHolder.img_small.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("小图点击");
                                toClickIntent(position);
                            }
                        });
                        break;
                }

                customViewHolder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toClickIntent(position);
                    }
                });

                switch (ads.getTags().toString()){
                    case "fixed":
                        customViewHolder.red_type.setText("置顶");
                        break;
                    case "change":
                        customViewHolder.tv_type.setText("现金");
                        break;
                    case "point":
                        customViewHolder.tv_type.setText("金币");
                        break;

                }
                customViewHolder.title.setText(ads.getAd_title());
                customViewHolder.name.setText(ads.getRelease_record().getNickname());
                customViewHolder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toClickIntent(position);
                    }
                });

                customViewHolder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //关闭
                        closeClick(customViewHolder.close,position);
                    }
                });


            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            int layoutId = 0;
            if(viewType == TYPE_AD){
                layoutId = R.layout.item_express_ad;
            }else if(viewType == SMALL_IMG){
                layoutId = R.layout.home_small_pic_item;
            }else if(viewType == BIG_IMG){
                layoutId = R.layout.home_big_pic_item;
            }else if(viewType == THREE_IMG){
                layoutId = R.layout.home_three_pic_item;
            }else if(viewType == HOT){
                layoutId = R.layout.home_recommend_item;
            }else if(viewType == ADHUB){
                layoutId = R.layout.home_adhub_item;
            }else if(viewType == ADHUB2){
                layoutId = R.layout.home_ad_pic_item;
            }

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view,viewType);
            return viewHolder;

        }

        public void onViewClick(int pos){
            toClickIntent(pos);
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView title;
            public TextView red_type;
            public TextView tv_type;
            public ImageView img_big;
            public ImageView img_small;
            public ImageView img1;
            public ImageView img2;
            public ImageView img3;
            public  View close;
            //广告
            public ViewGroup container;


            public CustomViewHolder(View view,int type) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                title = (TextView) view.findViewById(R.id.title);
                red_type = (TextView) view.findViewById(R.id.tv_red_type);
                tv_type = (TextView) view.findViewById(R.id.tv_type);
                close = (View) view.findViewById(R.id.close);
                container = (ViewGroup) view.findViewById(R.id.express_ad_container);
                if(type == THREE_IMG){

                    img1 = (ImageView) view.findViewById(R.id.img1);
                    img2 = (ImageView) view.findViewById(R.id.img2);
                    img3 = (ImageView) view.findViewById(R.id.img3);
                }else if(type == BIG_IMG){
                    img_big = (ImageView) view.findViewById(R.id.img);
                }else if(type == SMALL_IMG){
                    img_small = (ImageView) view.findViewById(R.id.small_img);
                }
            }
        }
    }

    //关闭不感兴趣的
    public void closeClick(View view,int pos){
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

    public void toClickIntent(int position){
        for (String s : list.get(position).getTags()) {
            if (s.equals("ad")){
                Uri uri= Uri.parse(list.get(position).getUrl());
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
                return;
            }
        }
        Intent intent = new Intent(mActivity, VideoDetailActivity.class);
        intent.putExtra("id", list.get(position).getId());
        intent.putExtra("purpose", "purpose");
        startActivity(intent);
    }
}

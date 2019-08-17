package com.shijinsz.shijin.ui.video.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DevicesUtil;
import com.hongchuang.hclibrary.view.MyJzvdStd;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.utils.GlideApp;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Copyright (C)
 * FileName: VideoFragment
 * Author: Administrator
 * Date: 2019/8/6 0006 上午 11:59
 * Description:
 */
public class VideoItemFragment extends BaseFragment {

    //@BindView(R.id.txv_video)
    //TXCloudVideoView txvVideo;
    /*@BindView(R.id.rl_back_right)
    RelativeLayout rlBackRight;*/
    /*@BindView(R.id.dl_back_play)
    DrawerLayout dlBackPlay;*/
    @BindView(R.id.iv_play_thun)
    ImageView ivPlayThun;
    //private TXVodPlayer mVodPlayer;
    private String ads_id;
    @BindView(R.id.player)
    MyJzvdStd playerStandard;

    //播放
    @BindView(R.id.play_but)
    public Button mPlay;

    public MyBroadcastReceiver localReceiver;


    public static final String URL = "URL";
    //当前界面是否可见
    public boolean ViewStatue = false;
    //是否允许流量播放
    public String flow_play = "false";

    @Override
    protected int provideContentViewId() { return R.layout.fm_video; }

    @Override
    protected void loadData() {
        ViewStatue = true;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        ads_id = getArguments().getString(URL);

        System.out.println("接收到要显示的数据ID： " + ads_id);

        //创建player对象
        //mVodPlayer = new TXVodPlayer(getContext());
        //关键player对象与界面view
        //mVodPlayer.setPlayerView(txvVideo);
//        url = "http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4";
        //mVodPlayer.setLoop(true);

        /*Glide.with(getContext())
                .load(url)
                .into(ivPlayThun);*/


        /*if (mVodPlayer != null) {
            mVodPlayer.pause();
        }*/
        /*Glide.with(getContext())
                .load(url)
                .into(playerStandard.thumbImageView);*/

        //playerStandard.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        playerStandard.startVideo();
        flow_play = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_flow_play_video);


        /** 注册广播 */
        localReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.shijinsz.VideoItemFragment");//只有持有相同的action的接受者才能接收此广播  
        getContext().registerReceiver(localReceiver, filter);

        System.out.println("父fragment： " + getParentFragment());



        //mPlay = (Button) rootView.findViewById(R.id.play_but);
        //rootView

        //mPlay = (Button) getParentFragment().getParentFragment().getView().findViewById(R.id.play_but);

        /*dlBackPlay.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (mVodPlayer != null) {
                    mVodPlayer.pause();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (mVodPlayer != null) {
                    mVodPlayer.resume();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });*/


        //设置菜单内容之外其他区域的背景色
        //dlBackPlay.setScrimColor(Color.TRANSPARENT);
        //设置 全屏滑动
        //setDrawerRightEdgeSize(getActivity(), dlBackPlay, 1);

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlay.setVisibility(View.GONE);
                playerStandard.startVideo();
                //mVodPlayer.startPlay(url);
                ShareDataManager.getInstance().save(mActivity, SharedPreferencesKey.KEY_flow_play_video, "true");
            }
        });

        hideStatusNavigationBar();
    }

    private void hideStatusNavigationBar(){

        if(Build.VERSION.SDK_INT<16){
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        /*if (mVodPlayer == null) {
            return;
        }
        ViewStatue = isVisibleToUser;
        System.out.println("显示隐藏播放" );
        if (isVisibleToUser) {
            if (DevicesUtil.isWifi(getContext())) {
                if(mVodPlayer != null){
                    mVodPlayer.startPlay(url);
                    mPlay.setVisibility(View.GONE);
                }
            }else{
                if(flow_play.equals("true")){
                    mVodPlayer.startPlay(url);
                    mPlay.setVisibility(View.GONE);
                }else{
                    mVodPlayer.pause();
                    mPlay.setVisibility(View.VISIBLE);
                }

            }
            //mVodPlayer.resume();
        } else {
            //注销广播
            //getActivity().unregisterReceiver(localReceiver);
            mVodPlayer.stopPlay(true);
            //mVodPlayer.pause();
        }*/




    }

    @OnClick({R.id.play_but})
    public void playClick(View view){
       // mVodPlayer.startPlay(url);
        mPlay.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {

        super.onResume();
        /*if (mVodPlayer != null) {
            mVodPlayer.resume();
        }*/

    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (mVodPlayer != null) {
            mVodPlayer.pause();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (mVodPlayer != null) {
            // true代表清除最后一帧画面
            mVodPlayer.stopPlay(true);
        }
        if (txvVideo != null) {
            txvVideo.onDestroy();
        }*/
    }

    /**
     * 设置 全屏滑动
     *
     * @param activity
     * @param drawerLayout
     * @param displayWidthPercentage
     */
    private void setDrawerRightEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field mRightDragger = drawerLayout.getClass().getDeclaredField("mRightDragger");//Right
            mRightDragger.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) mRightDragger.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String vis = intent.getExtras().getString("isVisible");
            System.out.println("广播显示状态： " + ViewStatue);
            System.out.println("广播接收播放" );
            /*if(vis.equals("true") && ViewStatue){
                if (DevicesUtil.isWifi(getContext())) {
                    if(mVodPlayer != null){
                        mVodPlayer.startPlay(url);
                        mPlay.setVisibility(View.GONE);
                        //mVodPlayer.pause();
                        //mPlay.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(flow_play.equals("true")){
                        mVodPlayer.startPlay(url);
                        mPlay.setVisibility(View.GONE);
                    }else{
                        mVodPlayer.pause();
                        mPlay.setVisibility(View.VISIBLE);
                    }

                }
            }else{
                mVodPlayer.pause();
                mPlay.setVisibility(View.VISIBLE);
            }*/

        }
    }
}

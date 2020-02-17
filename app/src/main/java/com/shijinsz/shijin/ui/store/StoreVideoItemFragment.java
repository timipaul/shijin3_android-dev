package com.shijinsz.shijin.ui.store;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.MyJzvdStd;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdBean;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.ad.VideoCommentActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;
import com.shijinsz.shijin.utils.ShareDialog;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: VideoFragment
 * Author: Administrator
 * Date: 2019/8/6 0006 上午 11:59
 * Description:
 */
public class StoreVideoItemFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.iv_play_thun)
    ImageView ivPlayThun;
    @BindView(R.id.player)
    MyJzvdStd playerStandard;

    private String video_url;
    private String video_title;
    //头像 关注 点赞 评论 转发 用户名 详情 购物车
    private View mView;

    private TextView mTv_User;
    private TextView mTv_user_title;
    private Button mBt_car;

    ShareDialog shareDialog;

    //播放
    @BindView(R.id.play_but)
    public Button mPlay;

    public StoreBean bean;

    public MyBroadcastReceiver localReceiver;


    public static final String URL = "URL";
    public static final String TITLE = "TITLE";

    //当前界面是否可见
    public boolean ViewStatue = false;
    //是否允许流量播放
    public String flow_play = "false";
    private String TAG = "video";

    @Override
    protected int provideContentViewId() { return R.layout.fm_video; }

    @Override
    protected void loadData() {
        ViewStatue = true;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        video_url = getArguments().getString(URL);
        video_title = getArguments().getString(TITLE);


        //父类控件和监听
        setParentView();

        flow_play = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_flow_play_video);

        shareDialog = new ShareDialog(getContext());
        shareDialog.setOnShareListen(new ShareDialog.OnShareListen() {
            @Override
            public void success(String channel) {

            }
        });


        /** 注册广播 */
        localReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.shijinsz.StoreVideoItemFragment");//只有持有相同的action的接受者才能接收此广播  
        getContext().registerReceiver(localReceiver, filter);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlay.setVisibility(View.GONE);
                playerStandard.startVideo();
                ShareDataManager.getInstance().save(mActivity, SharedPreferencesKey.KEY_flow_play_video, "true");
            }
        });

        getVideoData();

    }

    //设置父类控件和监听
    private void setParentView(){
       //mView = getParentFragment().getView();

        //mTv_User = mView.findViewById(R.id.username);
       // mTv_user_title = mView.findViewById(R.id.video_title);
        //mBt_car = mView.findViewById(R.id.commodity);

        //mBt_car.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        /*switch (view.getId()){
            case R.id.commodity:
                break;

        }*/
    }

    //获取视频数据
    public void getVideoData(){
        playerStandard.setUp(video_url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        Glide.with(getContext())
                .load(video_url)
                .into(playerStandard.thumbImageView);
        setShowData();
        playerStandard.startVideo();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(bean != null){
                setParentView();
                setShowData();
                playerStandard.startVideo();
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
                //设置显示数据

            }
        }else{
            playerStandard.releaseAllVideos();
        }
        ViewStatue = isVisibleToUser;

    }

    //设置界面数据
    private void setShowData(){
        if(bean != null){
            //mTv_User.setText(bean.getUser().nickname);
            //mTv_user_title.setText(bean.getAd_title());
        }else{
        }
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
            if(vis.equals("true") && ViewStatue){
                playerStandard.startVideo();
                setShowData();
            }else{
                playerStandard.releaseAllVideos();
            }
            //使用完释放掉
            //context.unregisterReceiver(this);
        }
    }
}

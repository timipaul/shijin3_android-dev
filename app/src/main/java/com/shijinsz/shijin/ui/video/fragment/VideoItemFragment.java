package com.shijinsz.shijin.ui.video.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
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
public class VideoItemFragment extends BaseFragment implements View.OnClickListener {

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

    //头像 关注 点赞 评论 转发 用户名 详情 购物车
    private View mView;
    private ImageView mImg_head;
    private Button mBu_attention;
    private Button mBu_like;
    private Button mBu_comment;
    private Button mBu_transmit;
    private TextView mTv_User;
    private TextView mTv_user_title;
    private Button mBt_car;

    private boolean isLike = false;
    private boolean isCollect = false;

    ShareDialog shareDialog;

    //播放
    @BindView(R.id.play_but)
    public Button mPlay;

    public MyBroadcastReceiver localReceiver;


    public static final String URL = "URL";
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
        ads_id = getArguments().getString(URL);
        //System.out.println("接收到要显示的数据ID： " + ads_id);



        //父类控件和监听
        setParentView();
        flow_play = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_flow_play_video);

        shareDialog = new ShareDialog(getContext());
        shareDialog.setOnShareListen(new ShareDialog.OnShareListen() {
            @Override
            public void success(String channel) {
                share(channel);
            }
        });


        /** 注册广播 */
        localReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.shijinsz.VideoItemFragment");//只有持有相同的action的接受者才能接收此广播  
        getContext().registerReceiver(localReceiver, filter);

        System.out.println("父fragment： " + getParentFragment());

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

        getVideoData();

    }



    private ScreenBroadcastReceiver mScreenReceiver;
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;


        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 锁屏
                System.out.println("我锁屏啦-********************");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // 解锁
            }
        }
    }
    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getActivity().registerReceiver(mScreenReceiver, filter);
    }



    //设置父类控件和监听
    private void setParentView(){
        mView = getParentFragment().getView();
        mImg_head = mView.findViewById(R.id.user_img);
        mBu_attention = mView.findViewById(R.id.user_attention);
        mBu_like = mView.findViewById(R.id.user_like_video);
        mBu_comment = mView.findViewById(R.id.user_comment);
        mBu_transmit = mView.findViewById(R.id.user_transmit);
        mTv_User = mView.findViewById(R.id.username);
        mTv_user_title = mView.findViewById(R.id.video_title);
        mBt_car = mView.findViewById(R.id.commodity);

        mImg_head.setOnClickListener(this);
        mBu_like.setOnClickListener(this);
        mBu_comment.setOnClickListener(this);
        mBu_transmit.setOnClickListener(this);
        mBt_car.setOnClickListener(this);
    }

    private void share(String channel) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("channel", channel);
        YSBSdk.getService(OAuthService.class).shares(ads_id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

                mBu_transmit.setText(Integer.parseInt(mBu_transmit.getText().toString()) + 1 + "");
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_img:
                System.out.println("头像点击关注...");
                follow();
                break;
            case R.id.user_like_video:
                mBu_like.setClickable(false);
                System.out.println("点赞点击");
                if (LoginUtil.isLogin(getContext())) {
                    if (isLike) {
                        System.out.println("点赞");
                        unLike();
                    } else {
                        System.out.println("取消点赞");
                        giveaLike();
                    }
                }

                break;
            case R.id.user_comment:
                System.out.println("评论点击");
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }

                Intent intent = new Intent(getContext(), VideoCommentActivity.class);
                //intent.putExtra("num", comment_num);
                intent.putExtra("id", ads_id);
                //intent.putExtra("pos", "" + pos);
                //intent.putExtra("index", "" + index);
                //intent.putExtra("type", "4");
                startActivityForResult(intent, 111);
                getActivity().overridePendingTransition(R.anim.bottom_in,R.anim.bottom_silent);

                //showEarlyDialog();

                break;
            case R.id.user_transmit:
                System.out.println("转发点击");
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                getShare();
                break;
            case R.id.commodity:
                System.out.println("购物车点击");
                break;

        }
    }

    private AdBean bean;
    //获取视频数据
    public void getVideoData(){
        Map map = new HashMap();
        map.put("mode", "detail");
        map.put("purpose", "answer");
        YSBSdk.getService(OAuthService.class).ads_detail(ads_id, map, new YRequestCallback<BaseBean<AdBean>>() {
            @Override
            public void onSuccess(BaseBean<AdBean> var1) {
                //adData = var1
                bean = var1.getAd();
                playerStandard.setUp(bean.getAd_content(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
                Glide.with(getContext())
                        .load(bean.getAd_content())
                        .into(playerStandard.thumbImageView);

                if(getParentFragment().getUserVisibleHint()){
                    //System.out.println("数据播放++++");
                    setShowData();
                    playerStandard.startVideo();

                }


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
                mStateView.showContent();
                //mStateView.showRetry();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                //mStateView.showRetry();
            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(bean != null){
                setShowData();
                playerStandard.startVideo();
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
                //设置显示数据

            }
        }else{
            playerStandard.releaseAllVideos();
        }
        ViewStatue = isVisibleToUser;
        /*if (mVodPlayer == null) {
            return;
        }

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

    //设置界面数据
    private void setShowData(){
        System.out.println("设置数据信息*********");
        if(bean != null){
            mTv_User.setText(bean.getUser().nickname);
            mTv_user_title.setText(bean.getAd_title());
            mBu_like.setText(bean.getAd_like_number());
            mBu_transmit.setText(bean.getAd_share_number());
            mBu_comment.setText(bean.getAd_comment_number());
            Glide.with(getContext()).load(bean.getUser().getImgurl()).into(mImg_head);

            //System.out.println("播放视频" + bean.getAd_content());
            if(bean.getIs_collection().equals("on")){
                mBu_attention.setVisibility(View.GONE);
            }else{
                mBu_attention.setVisibility(View.VISIBLE);
            }

            if(bean.getIs_follow().equals("on")){
                mBu_attention.setVisibility(View.GONE);
            }else{
                mBu_attention.setVisibility(View.VISIBLE);
            }

            if(bean.getIs_like().equals("on")){
                isLike = true;
                mBu_like.setCompoundDrawablesWithIntrinsicBounds(null,getContext().getResources().getDrawable(R.mipmap.video_like_on_ic),null,null);
            }else{
                isLike = false;
                mBu_like.setCompoundDrawablesWithIntrinsicBounds(null,getContext().getResources().getDrawable(R.mipmap.video_like_off_ic),null,null);
            }
        }else{
            System.out.println("数据是空的******");
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
            //System.out.println("收到广播");
            String vis = intent.getExtras().getString("isVisible");
            if(vis.equals("true") && ViewStatue){
                playerStandard.startVideo();
                setShowData();
            }else{
                //System.out.println("暂停播放");
                playerStandard.releaseAllVideos();
            }
            //使用完释放掉
            //context.unregisterReceiver(this);
        }
    }

    public void unLike() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).unlikes(bean.getId(), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mBu_like.setClickable(true);
                isLike = false;
                mBu_like.setCompoundDrawablesWithIntrinsicBounds(null,getContext().getResources().getDrawable(R.mipmap.video_like_off_ic),null,null);
                int num = Integer.parseInt(mBu_like.getText().toString()) - 1;
                mBu_like.setText(num + "");
            }

            @Override
            public void onFailed(String var1, String message) {
                mBu_like.setClickable(true);
                ErrorUtils.error(getContext(),var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mBu_like.setClickable(true);
            }
        });
    }

    public void giveaLike() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).likes(bean.getId(), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mBu_like.setClickable(true);
                isLike = true;
                mBu_like.setCompoundDrawablesWithIntrinsicBounds(null,getContext().getResources().getDrawable(R.mipmap.video_like_on_ic),null,null);
                int num = Integer.parseInt(mBu_like.getText().toString()) + 1;
                mBu_like.setText(num + "");
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
                mBu_like.setClickable(true);
            }

            @Override
            public void onException(Throwable var1) {
                mBu_like.setClickable(true);
            }
        });
    }

    //获取分享信息
    public void getShare() {
        String mode = "";
        mode = "video_detail";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                shareDialog.showWithdrapDialog(mActivity, 1, mTv_user_title.getText().toString(), var1.getShare_info(), bean.getAd_title_pics().get(0), "https://prod.shijinsz.net/app_h5/ads_detail#aid=" + ads_id + "&time=12121511331");
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //关注
    private void follow() {
        Map map = new HashMap();
        System.out.println("关注人的ID " + bean.getUser().getId());
        YSBSdk.getService(OAuthService.class).fans(bean.getUser().getId(), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

                mBu_attention.setVisibility(View.GONE);
                ToastUtil.showToast("关注成功");

            }

            @Override
            public void onFailed(String var1, String message) {
                System.out.println("关注失败 1");
                mBu_attention.setVisibility(View.GONE);

            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("关注失败 2");
            }
        });
    }
}

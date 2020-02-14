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

    public AdBean bean;

    public MyBroadcastReceiver localReceiver;


    public static final String URL = "URL";

    //当前界面是否可见
    public boolean ViewStatue = false;
    //是否允许流量播放
    public String flow_play = "false";
    private String TAG = "video";

    //关注用户的id
    private String follow_id;
    //点赞id
    private String like_id;

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
                follow();
                break;
            case R.id.user_like_video:
                mBu_like.setClickable(false);
                if (LoginUtil.isLogin(getContext())) {
                    if (isLike) {
                        //点赞
                        unLike();
                    } else {
                        //取消点赞
                        giveaLike();
                    }
                }

                break;
            case R.id.user_comment:
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
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                getShare();
                break;
            case R.id.commodity:
                break;

        }
    }



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
        /*if (mVodPlayer == null) {
            return;
        }


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
        if(bean != null){

            mTv_User.setText(bean.getUser().nickname);
            mTv_user_title.setText(bean.getAd_title());
            mBu_like.setText(bean.getAd_like_number());
            mBu_transmit.setText(bean.getAd_share_number());
            mBu_comment.setText(bean.getAd_comment_number());

            try {
                Glide.with(getContext()).load(bean.getUser().getImgurl()).into(mImg_head);
            } catch (Exception e) {

            }


            like_id = bean.getId();
            follow_id = bean.getUser().getId();

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

    public void unLike() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).unlikes(like_id, map, new YRequestCallback<PicCodeBean>() {
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
        YSBSdk.getService(OAuthService.class).likes(like_id, map, new YRequestCallback<PicCodeBean>() {
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
        YSBSdk.getService(OAuthService.class).fans(follow_id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mBu_attention.setVisibility(View.GONE);
                ToastUtil.showToast("关注成功");

            }

            @Override
            public void onFailed(String var1, String message) {
                mBu_attention.setVisibility(View.GONE);

            }

            @Override
            public void onException(Throwable var1) {
            }
        });
    }
}

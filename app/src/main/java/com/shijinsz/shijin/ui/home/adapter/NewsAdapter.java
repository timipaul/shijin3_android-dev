package com.shijinsz.shijin.ui.home.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hubcloud.adhubsdk.NativeAd;
import com.hubcloud.adhubsdk.NativeAdResponse;
import com.hubcloud.adhubsdk.internal.nativead.NativeAdEventListener;
import com.hubcloud.adhubsdk.internal.nativead.NativeAdUtil;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class NewsAdapter extends BaseQuickAdapter<Ads, BaseViewHolder> {
    private static final int BIG_IMG = 100;
    private static final int THREE_IMG = 200;
    private static final int SMALL_IMG = 300;
    private static final int HOT = 400;
    private static final int ADHUB =500;
    private static final int ADHUB2 =600;
    public interface OnCloseClickListen{
        void onClick(View view,int pos);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key="";
    public void setCloseClickListen(OnCloseClickListen closeClickListen) {
        CloseClickListen = closeClickListen;
    }

    public OnCloseClickListen CloseClickListen;
    private NativeAd nativeAd;

    public NewsAdapter(int layoutResId, @Nullable List<Ads> data) {
        super(layoutResId, data);
        setMultiTypeDelegate(new MultiTypeDelegate<Ads>() {
            @Override
            protected int getItemType(Ads news) {
                for (String s : news.getTags()) {
                    if (s.equals("fixed")){
                        return HOT;
                    }
                }
                if (news.getAd_mode().equals("one_small_pic")) {
                    return SMALL_IMG;
                } else if (news.getAd_mode().equals("one_big_pic")) {
                    return BIG_IMG;
                } else if (news.getAd_mode().equals("adhub")){
                    return ADHUB;
                }  else if (news.getAd_mode().equals("adhub2")){
                    return ADHUB2;
                }  else {
                    return THREE_IMG;
                }
            }
        });
        //Step .2
        getMultiTypeDelegate()
                .registerItemType(SMALL_IMG, R.layout.home_small_pic_item)//小图布局
                .registerItemType(BIG_IMG, R.layout.home_big_pic_item)//大图布局
                .registerItemType(THREE_IMG, R.layout.home_three_pic_item)//三图布局
                .registerItemType(HOT, R.layout.home_recommend_item)//置顶
                .registerItemType(ADHUB,R.layout.home_adhub_item)
                .registerItemType(ADHUB2,R.layout.home_ad_pic_item);

    }

    @Override
    protected void convert(final BaseViewHolder helper, Ads item) {
        ImageView bigimg = helper.getView(R.id.img);
        switch (helper.getItemViewType()) {
            case HOT:
                helper.setGone(R.id.tv_red_type,true);
                helper.setText(R.id.tv_red_type,"置顶");
                break;
            case BIG_IMG:
                if (item.getAd_type().equals("picture")) {
                    bigimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //helper.setGone(R.id.im_play,false);
                }else {
                    bigimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    //helper.setGone(R.id.im_play,true);
                }
                helper.setGone(R.id.tv_red_type,false);
                helper.setText(R.id.tv_red_type,"广告");
                Glide.with(mContext).load(item.getAd_title_pics().get(0)).into(bigimg);
                break;
            case SMALL_IMG:
                helper.setGone(R.id.tv_red_type,false);
                helper.setText(R.id.tv_red_type,"广告");
                ImageView smallimg = helper.getView(R.id.small_img);
                Glide.with(mContext).load(item.getAd_title_pics().get(0)).into(smallimg);
                break;
            case THREE_IMG:
                helper.setGone(R.id.tv_red_type,false);
                helper.setText(R.id.tv_red_type,"广告");
                ImageView img1 = helper.getView(R.id.img1);
                ImageView img2 = helper.getView(R.id.img2);
                ImageView img3 = helper.getView(R.id.img3);
                Glide.with(mContext).load(item.getAd_title_pics().get(0)).into(img1);
                Glide.with(mContext).load(item.getAd_title_pics().get(1)).into(img2);
                Glide.with(mContext).load(item.getAd_title_pics().get(2)).into(img3);
                break;
            case ADHUB:
                LinearLayout lnAd=helper.getView(R.id.adhub);
                lnAd.removeAllViews();
                lnAd.addView((View)item.getAdhub());
                ((NativeAd)item.getNativeAd()).nativeRender((View)item.getAdhub());
                break;
            case ADHUB2:
                helper.setGone(R.id.tv_red_type,true);
                helper.setText(R.id.tv_red_type,"广告");
                Glide.with(mContext).load(item.getAd_title_pics().get(0)).into(bigimg);
                bigimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //helper.setGone(R.id.im_play,false);
                helper.setGone(R.id.ln_bottom,false);
                ImageView adimg=helper.getView(R.id.img_ad);
                if (((NativeAdResponse)item.getAdhub()).getAdUrl().getType()==0) {
                    Glide.with(mContext).load(((NativeAdResponse)item.getAdhub()).getAdUrl().getAdurl()).into(adimg);
                }else {
                    helper.setText(R.id.tv_ad,((NativeAdResponse)item.getAdhub()).getAdUrl().getAdurl());
                }
                ImageView adimg2=helper.getView(R.id.img_ad2);
                if (((NativeAdResponse)item.getAdhub()).getAdUrl().getType()==0) {
                    Glide.with(mContext).load(((NativeAdResponse)item.getAdhub()).getlogoUrl().getAdurl()).into(adimg2);
                }else {
                    helper.setText(R.id.tv_ad2,((NativeAdResponse)item.getAdhub()).getlogoUrl().getAdurl());
                }
                break;

        }
        if (helper.getItemViewType()!=ADHUB) {
            boolean isFixed=false;
            for (String s : item.getTags()) {
                switch (s){
                    case "fixed":
                        isFixed=true;
                        helper.setGone(R.id.tv_type,true);
                        helper.setGone(R.id.tv_red_type,true);
                        helper.setText(R.id.tv_red_type,"置顶");
                        break;
                    case "change":
                        helper.setGone(R.id.tv_type,true);
                        helper.setText(R.id.tv_type,"现金");
                        break;
                    case "point":
                        helper.setGone(R.id.tv_type,true);
                        helper.setText(R.id.tv_type,"金币");
                        break;
                    case "ad":
                        helper.setGone(R.id.tv_type,true);
                        helper.setGone(R.id.tv_red_type,true);
                        helper.setText(R.id.tv_red_type,"广告");
                        break;
                    case "adhub":
                        isFixed=true;
                        helper.setGone(R.id.tv_type,false);
                        helper.setGone(R.id.tv_red_type,true);
                        helper.setText(R.id.tv_red_type,"广告");
                        break;
                }
            }
            if (isFixed) {
                helper.setVisible(R.id.close,false);
            }else {
                helper.setVisible(R.id.close,true);
            }
//        if (item.getReward_mode().equals("change")) {
//            helper.setText(R.id.tv_type,"现金");
//        } else {
//            helper.setText(R.id.tv_type,"金币");
//        }
            helper.setText(R.id.title, item.getAd_title() + "");

            if (helper.getItemViewType()!=ADHUB2){
                helper.setText(R.id.name, item.getRelease_record().getNickname() + "");
                helper.setText(R.id.time, item.getRelease_record().getRelease_time() + "");
            }else {
                helper.setText(R.id.name, "");
                helper.setText(R.id.time, "");
            }
            final View close = helper.getView(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CloseClickListen != null){
                        CloseClickListen.onClick(close,helper.getPosition());
                    }
                }
            });
            if (helper.getItemViewType()==ADHUB2){
                NativeAdUtil.registerTracking((NativeAdResponse)item.getAdhub(), helper.getView(R.id.ln_hub), new NativeAdEventListener() {
                    @Override
                    //显示素材的view被点击时回调
                    public void onAdWasClicked() {
                        Log.i(TAG, "onAdWasClicked: ");
                    }
                    @Override
                    public void onAdWillLeaveApplication() {
                        Log.i(TAG, "onAdWillLeaveApplication: ");
//                        Toast.makeText(mContext, "onAdWillLeaveApplication", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
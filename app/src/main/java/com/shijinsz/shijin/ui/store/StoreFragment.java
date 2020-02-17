package com.shijinsz.shijin.ui.store;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.hclibrary.view.MyScrollView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreRushBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.store.adapter.GiftChangeAdapter;
import com.shijinsz.shijin.ui.store.adapter.SeekGoodsAdapter;
import com.shijinsz.shijin.ui.store.adapter.StoreVideoImgAdapter;
import com.shijinsz.shijin.utils.AnimationUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HorizontalListView;
import com.shijinsz.shijin.utils.SimpleCountDownTimer;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: StoreFragment
 * Author: Administrator
 * Date: 2019/9/5 0005 下午 15:46
 * Description: 商城模块
 */
public class StoreFragment extends BaseFragment {

    @BindView(R.id.xbanner)
    public XBanner mBanner;
    @BindView(R.id.myScroll)
    MyScrollView myScroll;
    @BindView(R.id.classify_layout)
    LinearLayout mClassify_layout;
    @BindView(R.id.time_down)
    TextView mTime;
    @BindView(R.id.listview)
    MyGridView listView;
    @BindView(R.id.horizontal_view)
    HorizontalListView horListview;
    @BindView(R.id.horizontal_gift_view)
    HorizontalListView giftView;
    @BindView(R.id.radiogroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    List<String> imgs = new ArrayList<>();
    List<StoreBean> dataVideo = new ArrayList<>();

    StoreBean imgsBean;

    //抢购适配器
    SeekGoodsAdapter goodsAdapter;
    //视频适配器
    StoreVideoImgAdapter imgAdapter;
    List<StoreGoodsBean> list = new ArrayList();
    //礼品兑换适配器
    GiftChangeAdapter giftAdapter;
    List<StoreGoodsBean> giftData = new ArrayList<>();
    boolean classifyState = false;
    StoreRushBean rushData = new StoreRushBean();
    SimpleCountDownTimer downTime;
    private static String TAG = "StoreFragment";

    private int bg_index = -2;


    @Override
    protected int provideContentViewId() {
        return R.layout.store_home;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);

        //隐藏视图
        AnimationUtils.invisibleAnimator(mClassify_layout);

        goodsAdapter = new SeekGoodsAdapter(getContext(), list, R.layout.store_seek_goods_item);
        listView.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemClickListener(new SeekGoodsAdapter.ItemOnclick() {
            @Override
            public void addClick(String goodsId, TextView tv_num, Button bt) {
                try {
                    int num = Integer.valueOf(tv_num.getText().toString()) + 1;
                    if (num > 0) {
                        tv_num.setVisibility(View.VISIBLE);
                        bt.setVisibility(View.VISIBLE);
                        tv_num.setText(String.valueOf(num));
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void minusClick(String goodsId, TextView tv_num, Button bt) {
                try {
                    int num = Integer.valueOf(tv_num.getText().toString()) - 1;
                    if (num <= 0) {
                        tv_num.setVisibility(View.GONE);
                        bt.setVisibility(View.GONE);
                    }
                    tv_num.setText(String.valueOf(num));
                } catch (Exception e) {

                }
            }

            @Override
            public void itemClickInto(String goodsId) {
                Intent intent = new Intent(getContext(), StoreGoodsDetails.class);
                intent.putExtra("goodsId",goodsId);
                startActivityForResult(intent,102);
            }
        });

        imgAdapter = new StoreVideoImgAdapter(getContext(), R.layout.store_video_img_item, dataVideo);
        horListview.setAdapter(imgAdapter);
        horListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(),StoreVideoActivity.class);
                intent.putExtra("index",i);
                startActivity(intent);
            }
        });

        giftAdapter = new GiftChangeAdapter(getContext(), R.layout.item_image, giftData);
        giftView.setAdapter(giftAdapter);
        giftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), StoreGoodsDetails.class);
                intent.putExtra("goodsId", giftData.get(i).get_id());
                startActivityForResult(intent,102);
            }
        });
        giftAdapter.setOnItemClick(new GiftChangeAdapter.OnItemClick() {
            @Override
            public void buttonOnclick(StoreGoodsBean data) {
            }
        });
        imgAdapter.setOnItemClick(new StoreVideoImgAdapter.OnItemClick() {
            @Override
            public void buttonOnclick(StoreBean data, int index) {
            }
        });

        myScroll.setOnScrollChangeListener(new MyScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(MyScrollView view, int x, int y, int oldx, int oldy) {

            }

            @Override
            public void onScrollBottomListener() {

            }

            @Override
            public void onScrollTopListener() {

            }
        });


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                try {
                    downTime.onFinish();
                } catch (Exception e) {

                }

                switch (i) {
                    case R.id.bt_now:
                        //正在抢购
                        getRushData(1);
                        break;
                    case R.id.bt_wait:
                        //即将开抢
                        getRushData(2);
                        break;
                }

            }
        });
    }

    @Override
    protected void loadData() {
        initBanner(mBanner);
        //initLocalImage();
        //加载轮播图
        getCarouselData();
        //加载视频信息
        getVideoData();
        //获取礼品信息
        getGiftData();
        //加载抢购信息
        getRushData(1);


    }

    //获取礼品信息
    private void getGiftData() {
        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).getGiftAll(map, new YRequestCallback<BaseBean<StoreGoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreGoodsBean> var1) {
                giftData.clear();
                giftData.addAll(var1.getGoods());
                giftAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //抢购数据
    private void getRushData(int index) {
        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).getStoreRush(map, new YRequestCallback<StoreRushBean>() {
            @Override
            public void onSuccess(StoreRushBean var1) {
                rushData = var1;
                //设置数据
                setRushData(index);
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    public void setRushData(int index) {
        list.clear();
        if (index == 1) {
            if (rushData.getNow() != null) {
                list.addAll(rushData.getNow().getGoods());
                setCountData(rushData.getNow().getEndTime());
            }
        } else if (index == 2) {
            if (rushData.getNext() != null) {
                list.addAll(rushData.getNext().getGoods());
                setCountData(rushData.getNext().getEndTime());
            }
        }
        goodsAdapter.notifyDataSetChanged();
    }

    //设置倒计时数据
    public void setCountData(long time) {
        downTime.onFinish();
        long stateTime = System.currentTimeMillis();
        // 总时间
        long totalTime = stateTime - time;
        System.out.println("获取到的毫秒时间 " + System.currentTimeMillis());
        System.out.println("总时间：" + totalTime);
        // 初始化并启动倒计时
        downTime = new SimpleCountDownTimer(totalTime / 100, mTime).setOnFinishListener(new SimpleCountDownTimer.OnFinishListener() {
            @Override
            public void onFinish() {

            }
        });
        downTime.start();


    }


    //视频数据
    private void getVideoData() {
        YSBSdk.getService(OAuthService.class).getStoreVideo(new YRequestCallback<BaseBean<StoreBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreBean> var1) {
                dataVideo.addAll(var1.getResult());
                imgAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //轮播图数据
    private void getCarouselData() {
        YSBSdk.getService(OAuthService.class).getStoreCarousel(new YRequestCallback<StoreBean>() {
            @Override
            public void onSuccess(StoreBean var1) {
                for (int i = 0; i < var1.getVal().size(); i++) {
                    imgs.add(var1.getVal().get(i).getUrl());
                }

                imgsBean = var1;
                mBanner.setAutoPlayAble(true);
                mBanner.setData(imgs, null);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }


    /**
     * 初始化XBanner
     */
    private void initBanner(XBanner banner) {
        //设置广告图片点击事件
        banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {



            }

        });
        //加载广告图片
        banner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {

                Glide.with(getContext()).load(imgsBean.getVal().get(position).getUrl())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into((ImageView) view);

                //判断第一次设置
                if (bg_index == -2) {
                    bg_index = position;
                }
                //正常
                if (bg_index == position) {
                    bg_index++;
                    if (bg_index == 5) {
                        bg_index = 0;
                    }
                } else {
                    //回退
                    bg_index--;
                    if (bg_index < 0) {
                        bg_index = 4;
                    }
                }

                //插件BUG 同步其它地方
                int temp = bg_index - 2;
                if (temp == -1) {
                    temp = 4;
                } else if (temp == -2) {
                    temp = 3;
                }

                int color = Color.parseColor(imgsBean.getVal().get(temp).getColor());
                topLayout.setBackgroundColor(color);


            }
        });
    }

    @OnClick({R.id.bt_stock, R.id.bt_bazaar, R.id.bt_retail, R.id.bt_property, R.id.bt_classify,
            R.id.classify_seafood, R.id.classify_goods, R.id.classify_snacks, R.id.classify_beauty, R.id.classify_infant,
            R.id.tv_seek, R.id.img_message})
    public void menuOnclick(View view) {

        String goods_classify = null;

        switch (view.getId()) {
            case R.id.bt_stock:
                //家乡美食
                //Intent intent = new Intent(getContext(),StoreGoodsDetails.class);
                //intent.putExtra("goodsId","5d8de20ad599a070c9b00a1e");
                //startActivity(intent);
                goods_classify = "HOMETOWN";
                break;
            case R.id.bt_bazaar:
                //海关拍卖
                goods_classify = "CUSTOMS";
                //startActivity(new Intent(getContext(),UserLocationActivity.class));
                break;
            case R.id.bt_retail:
                //香港直邮
                goods_classify = "HOME-KONG";
                break;
            case R.id.bt_property:
                //农庄游玩
                goods_classify = "FARM";
                break;
            case R.id.bt_classify:
                //分类
                //setShowOrhind();
                break;
            case R.id.classify_seafood:
                //水果生鲜
                break;
            case R.id.classify_goods:
                //家具百货
                break;
            case R.id.classify_snacks:
                //休闲零食

                break;
            case R.id.classify_beauty:
                //美妆服饰
                break;
            case R.id.classify_infant:
                //母婴专场

                break;
            case R.id.tv_seek:
                //搜索
                startActivity(new Intent(getContext(), StoreSeekActivity.class));
                break;
            case R.id.img_message:
                //消息
                break;
        }

        if (goods_classify != null) {
            Intent intent = new Intent(getContext(), StoreGoodsActivity.class);
            intent.putExtra("special", goods_classify);
            startActivityForResult(intent,102);
        }
    }

    //设置更多分类显示或隐藏
    private void setShowOrhind() {
        if (classifyState) {
            //显示视图
            AnimationUtils.invisibleAnimator(mClassify_layout);
        } else {
            //隐藏视图
            AnimationUtils.visibleAnimator(mClassify_layout);

        }
        classifyState = !classifyState;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (mBanner != null) {
            if (isVisibleToUser) {
                mBanner.startAutoPlay();
            } else {
                mBanner.stopAutoPlay();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102 && resultCode == 102){
            ((StoreHomeActivity)getActivity()).showModelFragment(2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downTime != null) {
            downTime.onFinish();
        }
    }
}

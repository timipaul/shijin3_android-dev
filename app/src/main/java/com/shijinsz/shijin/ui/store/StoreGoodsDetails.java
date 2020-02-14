package com.shijinsz.shijin.ui.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.utils.AndroidSystemUtil;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.MyScrollView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.UrlConstants;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.StroeCommentBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.CommentAdapter;
import com.shijinsz.shijin.ui.store.adapter.SimpleImageAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoveShareDialog;
import com.shijinsz.shijin.utils.MyListView;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;


/**
 * 商城商品详情页
 */
public class StoreGoodsDetails extends BaseActivity {

    @BindView(R.id.rg_menu)
    RadioGroup mRadio_group;
    @BindView(R.id.xbanner)
    public XBanner mBanner;
    @BindView(R.id.tv_price)
    TextView mPrice;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.tv_hint)
    TextView mHint;
    @BindView(R.id.goods_type)
    TextView mGoodsType;
    @BindView(R.id.goods_parameter)
    TextView mUserSite;
    @BindView(R.id.comment_list)
    PowerfulRecyclerView myListView;
    @BindView(R.id.goods_details_img)
    MyListView imgs;
    @BindView(R.id.scroll)
    MyScrollView mScroll;
    @BindView(R.id.tv_comment_all)
    TextView mTvComment;
    @BindView(R.id.top_menu)
    LinearLayout mTopMenu;
    @BindView(R.id.details_layout)
    RelativeLayout mDetails_layout;
    @BindView(R.id.shop_img)
    ImageView mShopImg;
    @BindView(R.id.shop_name)
    TextView mShopName;
    @BindView(R.id.shop_hint)
    TextView mShopHint;
    @BindView(R.id.rebate_price)
    TextView mRebatePrice;

    List<StroeCommentBean> commentData = new ArrayList<>();
    CommentAdapter commentAdapter;
    SimpleImageAdapter imgsAdapter;

    StoreGoodsBean goodsData;

    private String goodsId;
    LoveShareDialog shareDialog;
    //屏幕宽度
    private int phone_width;
    //详情距上位置
    private int details_top;
    //评论距上位置
    private int comment_top;

    @Override
    public int bindLayout() {
        return R.layout.store_goods_details;
    }

    @Override
    public void initView(View view) {

        goodsId = getIntent().getStringExtra("goodsId");
        shareDialog = new LoveShareDialog(mContext);
        mScroll.setOnScrollChangeListener(new MyScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(MyScrollView view, int x, int y, int oldx, int oldy) {
                if (y < comment_top) {
                    mRadio_group.check(R.id.rb_goods);
                } else if (y > comment_top && y < details_top) {
                    mRadio_group.check(R.id.rb_comment);
                } else if (y > details_top) {
                    mRadio_group.check(R.id.goods_details);
                }
            }

            @Override
            public void onScrollBottomListener() {

            }

            @Override
            public void onScrollTopListener() {

            }
        });


        phone_width = new AndroidSystemUtil(mContext).Width();

        initBanner(mBanner);
        //获取详情页信息
        getDetailsData();
        //获取评论信息
        //getCommentData();

    }

    //设置滑动位置
    private void setScrollSite(int site) {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                if (site == 0) {
                    mScroll.fullScroll(ScrollView.FOCUS_UP);
                } else {
                    mScroll.smoothScrollTo(0, site);
                }
            }
        });
    }


    //获取详情信息
    private void getDetailsData() {
        YSBSdk.getService(OAuthService.class).getGoodsDetails(goodsId, new YRequestCallback<ShoppingShopBean>() {
            @Override
            public void onSuccess(ShoppingShopBean var1) {

                goodsData = var1.getGoods();
                if (goodsData.getDiscount() == 0) {
                    mPrice.setText("¥" + goodsData.getPrice());
                } else {
                    //富文本
                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder("会员价¥" + goodsData.getDiscount());
                    AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30);
                    spannableStringBuilder3.setSpan(absoluteSizeSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    mPrice.setText(spannableStringBuilder3);
                    mRebatePrice.setText("" + goodsData.getPrice());
                    mRebatePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线（删除线）
                }
                mName.setText(goodsData.getName());
                mHint.setText("已售" + goodsData.getSalesVolume() + "件");
                Glide.with(mContext).load(var1.getImgUrl()).into(mShopImg);
                mShopName.setText(var1.getName());
                mShopHint.setText(var1.getDescribe());

                mBanner.setAutoPlayAble(true);
                mBanner.setData(Arrays.asList(goodsData.getCoverImg()), null);
                //设置详情图片
                imgsAdapter = new SimpleImageAdapter(mContext,R.layout.common_image,Arrays.asList(goodsData.getDetailsImg()));
                imgs.setAdapter(imgsAdapter);


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });


    }

    //获取评论信息
    private void getCommentData() {

        Map<String, Object> map = new HashMap();
        YSBSdk.getService(OAuthService.class).getStoreComment("", map, new YRequestCallback<BaseBean<StroeCommentBean>>() {
            @Override
            public void onSuccess(BaseBean<StroeCommentBean> var1) {


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });

        //评论适配器
        commentAdapter = new CommentAdapter(R.layout.store_comment_item, commentData);
        myListView.setAdapter(commentAdapter);
    }




    @OnClick({R.id.bt_return, R.id.bt_share, R.id.bt_headset, R.id.bt_car, R.id.add_shop_car,
            R.id.tv_comment_all, R.id.bt_into, R.id.rb_goods, R.id.rb_comment, R.id.goods_details,
            R.id.goods_type, R.id.goods_parameter})
    public void onClickView(View view) {
        Class clazz = null;
        Bundle data = null;

        switch (view.getId()) {
            case R.id.bt_return:
                //返回
                finish();
                break;
            case R.id.bt_share:
                //分享
                shareDialog.showWithdrapDialog(mActivity,
                        "商品分享",
                        goodsData.getName(),
                        goodsData.getCoverImg()[0],
                        UrlConstants.ISHARE+"ishare/app/storeDetail/?goodsid="+goodsData.get_id(),
                        goodsData.get_id(),
                        2);
                break;
            case R.id.bt_headset:
                //客服

                break;
            case R.id.bt_car:
                //购物车
                setResult(102);
                finish();
                break;
            case R.id.tv_comment_all:
                //跳转评论页
                clazz = StoreCommentActivity.class;
                data = new Bundle();
                data.putString("goodsId", "123");
                break;
            case R.id.bt_into:
                //进入店铺

                break;
            case R.id.rb_goods:
                //商品菜单
                setScrollSite(0);
                break;
            case R.id.rb_comment:
                //评论菜单
                setScrollSite(comment_top);
                break;
            case R.id.goods_details:
                //详情菜单
                setScrollSite(details_top);
                break;
            case R.id.goods_type:
            case R.id.add_shop_car:
                //商品规格  加入购物车
                Intent intentSize = new Intent(mContext, GoodsSpecificationActivity.class);
                intentSize.putExtra("goodsId", goodsId);
                startActivityForResult(intentSize, 111);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;
            case R.id.goods_parameter:
                //商品参数
                Intent intentParame = new Intent(mContext, GoodsSpecificationActivity.class);
                //Intent intentParame = new Intent(mContext, GoodsParameterActivity.class);
                //intentParame.putExtra("id", ads_id);
                startActivityForResult(intentParame, 111);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;
        }

        if (clazz != null) {
            Intent intent = new Intent(mContext, clazz);
            if (data != null) {
                intent.putExtra("data", data);
            }
            startActivity(intent);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //设置顶部跳转位置
        int[] detailsSite = new int[2];
        int[] commentSite = new int[2];
        mTvComment.getLocationOnScreen(commentSite);
        mDetails_layout.getLocationOnScreen(detailsSite);
        details_top = detailsSite[1] - mTopMenu.getHeight();
        comment_top = commentSite[1] - mTopMenu.getHeight();
    }

    /**
     * 初始化XBanner
     */
    private void initBanner(XBanner banner) {

        //加载广告图片
        banner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(mContext).load(goodsData.getCoverImg()[position])
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into((ImageView) view);

            }
        });
    }
}

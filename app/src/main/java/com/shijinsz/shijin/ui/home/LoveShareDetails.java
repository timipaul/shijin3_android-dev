package com.shijinsz.shijin.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.UrlConstants;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.ShareUserBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ishare.IshareOrderPay;
import com.shijinsz.shijin.ui.store.adapter.SimpleImageAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;
import com.shijinsz.shijin.utils.LoveShareDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;


/** 爱分享详情 */
public class LoveShareDetails extends BaseActivity {

    @BindView(R.id.image)
    ImageView imageView;
    /*@BindView(R.id.end_time)
    TextView mEndNum;*/
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.person_num)
    TextView mPerson_num;
    @BindView(R.id.share_num)
    TextView mShare_num;
    @BindView(R.id.bt_pay)
    Button mPay;
    @BindView(R.id.details_img)
    ListView mListView;
    @BindView(R.id.bt_share)
    Button mBt_share;

    private String goods_id;
    LoveShareDialog shareDialog;

    StoreGoodsBean goodsData;
    SimpleImageAdapter imgsAdapter;

    @Override
    public int bindLayout() {
        return R.layout.love_share_details;
    }

    @Override
    public void initView(View view) {

        showTitleBackButton();
        setTitle("商品详情");

        goods_id = getIntent().getStringExtra("goodsId");
        shareDialog = new LoveShareDialog(mContext);

        getData();

        getUserData();
    }


    //获取用户分享信息
    private void getUserData(){
        Map<String,Object> map = new HashMap();
        map.put("goodsId",goods_id);
        YSBSdk.getService(OAuthService.class).getUserShare(map, new YRequestCallback<ShareUserBean>() {

            @Override
            public void onSuccess(ShareUserBean var1) {
                mBt_share.setText(String.format(getString(R.string.love_share_hint),var1.getNow_num(),var1.getSum_num()));
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //获取详情
    private void getData() {
        YSBSdk.getService(OAuthService.class).getGoodsDetails(goods_id, new YRequestCallback<ShoppingShopBean>() {
            @Override
            public void onSuccess(ShoppingShopBean var1) {
                goodsData = var1.getGoods();
                Glide.with(mContext).load(goodsData.getCoverImg()[0]).into(imageView);
                mTitle.setText(goodsData.getName());
                //String num = String.format(mContext.getString(R.string.share_period), 0);
                //mEndNum.setText(num);
                goodsData.setPostage("0");
                mPrice.setText("¥" + goodsData.getPrice());
                mPerson_num.setText("0人");
                mShare_num.setText("0次");





                imgsAdapter = new SimpleImageAdapter(mContext,R.layout.common_image, Arrays.asList(goodsData.getDetailsImg()));
                mListView.setAdapter(imgsAdapter);

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

    @OnClick({R.id.bt_share,R.id.bt_pay})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.bt_share:
                //分享
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                getShare();
                break;
            case R.id.bt_pay:
                //支付
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                //商品规格  加入购物车
                Intent intentSize = new Intent(mContext, IshareOrderPay.class);
                intentSize.putExtra("goodsId", goodsData.get_id());
                startActivityForResult(intentSize, 111);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                break;
        }
    }


    private void getShare() {
        //1.活动 2.自定义类型 3.标题 4.提示文字  5.提示图片  6.H5地址
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);

        shareDialog.showWithdrapDialog(mActivity,
                "爱分享",
                goodsData.getName(),
                //"瓜分5000万广告费  分享 赚钱",
                goodsData.getCoverImg()[0],
                UrlConstants.ISHARE+"?username="+username+"&goodsid="+goodsData.get_id(),
                goodsData.get_id(),
                1);

        //?username=13736351554&goodsid=5dccd052d1978f0fb85e4c61


    }
}

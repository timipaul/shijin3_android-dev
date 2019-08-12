package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.VipPriceBean;
import com.hongchuang.ysblibrary.model.model.bean.VipRechangeBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import com.shijinsz.shijin.ui.mine.adapter.CommodityAdapter;
import com.shijinsz.shijin.ui.mine.adapter.RechangeVipAdapter;
import com.shijinsz.shijin.ui.mine.adapter.RedPacketAdapter;
import com.shijinsz.shijin.ui.mine.adapter.ZeroAdapter;
import com.shijinsz.shijin.ui.wallet.PointActivity;
import com.shijinsz.shijin.ui.wallet.PointDetailActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.HorizontalListView;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;


/**
 * 十金大使  会员
 */
public class MyVipActivity extends BaseActivity {

    @BindView(R.id.scroll)
    ScrollView mScroll;
    @BindView(R.id.img_avatar)
    ImageView mImg_avatar;
    @BindView(R.id.tv_name)
    TextView mTv_name;
    @BindView(R.id.vip_state)
    ImageView mVip_state;
    @BindView(R.id.tv_vip_hint)
    TextView mVip_hint;
    @BindView(R.id.vip_hint)
    ImageView mImg_vip_hint;
    @BindView(R.id.end_date)
    TextView mEnd_date;
    @BindView(R.id.img_vip_try)
    ImageView mVip_try;//免费体验入口
    @BindView(R.id.gridview)
    GridView mRechange_grid;
    @BindView(R.id.horizontal_view)
    HorizontalListView mHor_list;
    @BindView(R.id.conversion_gridview)
    GridView mConversionGrid;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView mRecycler;
    @BindView(R.id.gold_number)
    TextView mGold_number;
    @BindView(R.id.tv_1)
    TextView mTv_hint;
    @BindView(R.id.gold_zero)
    TextView mGoldZero;
    @BindView(R.id.gold_layout)
    LinearLayout mGold_layout;

    private IWXAPI api;
    public String vip_state = "month";
    public String[] vip_data = {"month","quarter","year"};
    public String vip_price;

    //会员协议
    public static final String KEY_vip_code = "vip_code";
    //隐私协议
    public static final String KEY_privacy_code = "privacy_code";

    DialogUtils mDialogUtils;
    //零元购数据
    private List<GoodsBean> zero_data = new ArrayList<>();

    //充值适配器
    RechangeVipAdapter mRechangeAdapter;
    //红包点击适配器
    RedPacketAdapter mPacketAdapter;
    //兑换专区适配器
    CommodityAdapter mCommodityAdapter;
    //0元购适配器
    ZeroAdapter mZeroAdapter;
    //vip选择下标
    private int vip_click_index;

    private DialogUtils dialogUtils;
    Handler mHandler;
    private String userid;

    private static final int FREE_VIP_PAGE_CODE = 100;
    //抢购页面跳转
    public static final int KEY_resh_page = 101;

    @Override
    public int bindLayout() {
        return R.layout.become_vip;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(getString(R.string.ten_gold_vip));
        showTitleBackButton();

         mDialogUtils = new DialogUtils(mContext);
        userid = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils(mActivity);
        }
        if(mHandler == null){
            mHandler = new Handler();
        }

        try {
            //设置基本数据
            setBasicData();
            //获取会员开通价格信息
            getVipPriceData();
            //获取红包显示数据
            getRedShowList();
            //获取累计金币数量
            getGoldSum();
            //获取兑换专区
            getExchangeData();
        }catch (Exception e) {
            System.out.println("获取数据异常");
        }



        mRechange_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(vip_click_index != position){
                    for(int i = 0;i < mRechange_grid.getChildCount(); i++){
                        View v = mRechange_grid.getChildAt(i);
                        if(i == position){
                            try {
                                if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_VIP_first_open).equals("on")){
                                    v.findViewById(R.id.first_vip).setVisibility(View.VISIBLE);
                                }
                            }catch (Exception e) {

                            }

                            v.setBackground(getResources().getDrawable(R.mipmap.rechang_view_on_bg));
                            vip_click_index = position;
                            vip_state = vip_data[position];
                            vip_price = mRechangeAdapter.mList.get(position).getSum();

                        }else{
                            v.findViewById(R.id.first_vip).setVisibility(View.GONE);
                            v.setBackground(getResources().getDrawable(R.mipmap.rechang_view_off_bg));
                        }
                    }
                }
            }
        });

        //零元购
        mZeroAdapter = new ZeroAdapter(R.layout.zero_money_buy_item, zero_data);
        mRecycler.setAdapter(mZeroAdapter);
        mZeroAdapter.setOnListen(new ZeroAdapter.OnListen() {
            @Override
            public void call(GoodsBean item) {

                //判断是否会员
                if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_vip_state).equals("on")){
                    Bundle data = new Bundle();
                    data.putString("goods_id",item.getId());
                    data.putString("goods_name",item.getGoods_title());
                    data.putString("url",item.getGoods_imgurl());
                    Intent intent = new Intent(mContext,UserSiteDataActivity.class);
                    intent.putExtras(data);
                    startActivityForResult(intent,KEY_resh_page);
                }else{
                    mDialogUtils.showGuidanceVipDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mScroll.fullScroll(ScrollView.FOCUS_UP);
                            mDialogUtils.guidanceVipDialog();
                        }
                    });
                }
            }
        });
        getZeroGoods();
    }

    //获取兑换专区
    private void getExchangeData() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("membership", "on");
        YSBSdk.getService(OAuthService.class).goods(map,new YRequestCallback<BaseBean<GoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<GoodsBean> var1) {

                //兑换专区
                mCommodityAdapter = new CommodityAdapter(mContext,var1.goods,R.layout.conversion_grid_item);
                mConversionGrid.setAdapter(mCommodityAdapter);
                mConversionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //跳转到兑换页面
                        Bundle data = new Bundle();
                        data.putInt("code",1);
                        data.putString("goods_id",var1.goods.get(position).getId());
                        System.out.println("当前点击的商品 " + var1.goods.get(position).getGoods_title());
                        startActivity(PointActivity.class,data);
                    }
                });
            }
            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) { }
        });
    }

    //获取金币加成数量
    private void getGoldSum() {
        Map<String, String> map = new HashMap();
        map.put("user_id", userid);
        YSBSdk.getService(OAuthService.class).get_gold_sum(map,new YRequestCallback<VipRechangeBean>() {
            @Override
            public void onSuccess(VipRechangeBean var1) {
                //mGoldZero
                if(var1.getMember_give_points().equals("0")){
                    mGold_layout.setVisibility(View.GONE);
                    mGoldZero.setVisibility(View.VISIBLE);
                }else{
                    mGold_number.setText(""+ var1.getMember_give_points());
                    mGoldZero.setVisibility(View.GONE);
                    mGold_layout.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) { }
        });
    }

    //显示红包列表
    private void getRedShowList(){
        Map<String, String> map = new HashMap();
        map.put("user_id", userid);
        YSBSdk.getService(OAuthService.class).get_red_list(map,new YRequestCallback<BaseBean<VipRechangeBean>>() {
            @Override
            public void onSuccess(BaseBean<VipRechangeBean> var1) {
                //加载充值数据
                mPacketAdapter = new RedPacketAdapter(mContext,var1.getVip_setting(),R.layout.red_packet_select_item);
                mHor_list.setAdapter(mPacketAdapter);
                mPacketAdapter.setOnItemClickListener(new RedPacketAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int index,View view) {

                        view.setClickable(false);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setClickable(true);
                                System.out.println("不可点击...");
                            }
                        },5000);

                        //判断用户是否是vip
                        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_vip_state).equals("on")){
                            //打开红包
                            openRedPackage(index);
                        }else{
                            mDialogUtils.showGuidanceVipDialog(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mScroll.fullScroll(ScrollView.FOCUS_UP);
                                    mDialogUtils.guidanceVipDialog();
                                }
                            });
                        }
                    }
                });
            }
            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) { }
        });
    }

    //打开红包
    private void openRedPackage(int index) {
        Map<String, String> map = new HashMap();
        map.put("user_id", userid);
        YSBSdk.getService(OAuthService.class).open_red_package(map,new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                getRedShowList();
                //加载充值数据
                double num = Double.valueOf(var1.getRed_package_number());
                dialogUtils.showVipRedPacketDialog(num);

            }
            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) { }
        });
    }

    //获取开通会员价格信息
    private void getVipPriceData() {
        YSBSdk.getService(OAuthService.class).get_vip_price(new YRequestCallback<BaseBean<VipPriceBean>>() {
            @Override
            public void onSuccess(BaseBean<VipPriceBean> var1) {
                //加载充值数据
                vip_price = var1.getVip_setting().get(0).getSum();
                mRechangeAdapter = new RechangeVipAdapter(mContext,var1.getVip_setting(),R.layout.rechange_vip_item);
                mRechange_grid.setAdapter(mRechangeAdapter);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //零元购
    private void getZeroGoods() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("is_zero_shop", "on");
        YSBSdk.getService(OAuthService.class).goods(map, new YRequestCallback<BaseBean<GoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<GoodsBean> var1) {
                //mStateView.showContent();
                zero_data.clear();
                zero_data.addAll(var1.getGoods());
                mZeroAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    @OnClick({R.id.img_vip_try,R.id.tv_rule_one,R.id.tv_rule_two,
            R.id.tv_gold_detail,R.id.more_goods,R.id.vip_agreement,
            R.id.privacy_agreement,R.id.button_pay,R.id.gold_zero,
            R.id.zero_rules})
    public void onViewClick(View view){
        Bundle bundle;
        switch (view.getId()) {
            case R.id.img_vip_try:
                //免费领取会员
                startActivityForResult(new Intent(mContext,GetFreeVipActivity.class),FREE_VIP_PAGE_CODE);
                break;
            case R.id.tv_rule_one:
                //每日红包规则
                String[] datas_one = {"1.每日限领一个红包;","2.当日未领取红包自动作废;","3.仅在会员存续期间才能领取红包。"};
                dialogUtils.showVipRuleHintDialog(mContext,"每日红包活动规则",datas_one);
                break;
            case R.id.tv_rule_two:
                //金币规则
                String[] datas_tow = {"1.仅在金币答题时才参与加成;","2.仅在会员存续期间才享受加成;","3.加成率为50%, 既答对1个10金币题,加成为5金币,总计获得15金币。"};
                dialogUtils.showVipRuleHintDialog(mContext,"金币活动规则",datas_tow);
                break;
            case R.id.tv_gold_detail:
                //跳转金币明细
                startActivity(PointDetailActivity.class);
                break;
            case R.id.more_goods:
                //更多兑换
                startActivity(PointActivity.class);
                break;
            case R.id.vip_agreement:
                //会员协议
                bundle = new Bundle();
                bundle.putString("code",KEY_vip_code);
                startActivity(AgreementActivity.class,bundle);
                break;
            case R.id.privacy_agreement:
                //隐私协议
                bundle = new Bundle();
                bundle.putString("code",KEY_privacy_code);
                startActivity(AgreementActivity.class,bundle);
                break;
            case R.id.button_pay:
                //调支付
                getPayData();
                break;
            case R.id.gold_zero:
                //跳转到阅读界面
                setResult(404);
                finish();
                break;
            case R.id.zero_rules:
                //0元购兑换规则
                String[] datas_three = {"1.抢购的商品邮费需要自理;","2.请仔细核对抢购信息，提交后不可修改;","3.如有问题请联系客服微信 shijinsz1。"};
                dialogUtils.showVipRuleHintDialog(mContext,"0元购活动规则",datas_three);
                break;
        }
    }

    //获取支付数据
    private void getPayData() {

        Map map = new HashMap();
        map.put("mode","wxpay");
        map.put("channel","app_member");
        map.put("change",vip_price);
        Map attachBean =new HashMap();
        Map rewardPlan=new HashMap();
        Gson gson=new Gson();
        rewardPlan.put("member_type",vip_state);
        rewardPlan.put("user_id",userid);
        attachBean.put("reward_plan",rewardPlan);
        map.put("attach",gson.toJson(attachBean)+"");


        System.out.println("会员充值");
        System.out.println("change:" + vip_price);
        System.out.println("member_type: " + vip_state);
        System.out.println("user_id: " + userid);



        YSBSdk.getService(OAuthService.class).preorder(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_type,"2");
                //ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_money,total_number+"");
                PayReq request = new PayReq();
                request.appId = var1.getAppid();
                request.partnerId = var1.getPartnerid();
                request.prepayId= var1.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr= var1.getNoncestr();
                request.timeStamp= var1.getTimestamp();
                request.sign= var1.getSign();
                api.sendReq(request);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    //设置基本信息
    private void setBasicData(){

        api= WXAPIFactory.createWXAPI(this,getString(R.string.WEIXIN_APPID),true);
        api.registerApp(getString(R.string.WEIXIN_APPID));

        GlideApp.with(this).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).placeholder(R.mipmap.icon_header).into(mImg_avatar);
        mTv_name.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname));
        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_vip_state).equals("on")){
            Glide.with(mContext).load(R.mipmap.icon_vip_on).into(mVip_state);
            //mImg_vip_hint.setBackgroundResource(R.mipmap.vip_on_hint_bg);
            mImg_vip_hint.setImageDrawable(getResources().getDrawable(R.mipmap.vip_on_hint_bg));
            mVip_hint.setText(getString(R.string.vip_state_on));
            mEnd_date.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_vip_end));
        }else{
            Glide.with(mContext).load(R.mipmap.icon_vip_off).into(mVip_state);
            //mImg_vip_hint.setBackgroundResource(R.mipmap.vip_off_hint_bg);
            mImg_vip_hint.setImageDrawable(getResources().getDrawable(R.mipmap.vip_off_hint_bg));
            mVip_hint.setText(getString(R.string.vip_state_off));
            mEnd_date.setText("");
        }

        //设置文字颜色
        //String str = getString(R.string.rechange_vip_hint);
        String text1 = "<font color='#e01f22'>充值199元返99500金币</font>";
        String text2 = "<font color='#e01f22'>我的金币</font>";
        String str = String.format(mContext.getString(R.string.rechange_vip_hint),text1,text2);


        SpannableString hint = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#e01f22"));
        //rechange_vip_hint
        //String_1.setSpan(colorSpan,0,text1.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //String_2.setSpan(colorSpan,0,text2.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //String_1.setSpan(colorSpan,);

        mTv_hint.setText(Html.fromHtml(str));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FREE_VIP_PAGE_CODE){
            String freeVip = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_vip_state);
            if(freeVip.equals("on")){
                setBasicData();
            }
        }else if(requestCode == KEY_resh_page){
            getZeroGoods();
        }
    }
}

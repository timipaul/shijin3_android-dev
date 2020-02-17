package com.shijinsz.shijin.ui.homepage;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.Validator;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.GoodsClassifyBean;
import com.hongchuang.ysblibrary.model.model.bean.InviteRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.WechatPayBean;
import com.hongchuang.ysblibrary.model.model.bean.storeUserBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.callback.YRequestCallback;


/**
 * 商城成为合伙人
 */
public class AddMemberActivity extends BaseActivity {

    @BindView(R.id.invite_code)
    EditText mInviteCode;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.rg_select)
    RadioGroup mRbSelect;
    @BindView(R.id.layout_vip)
    LinearLayout mLayoutVip;
    @BindView(R.id.layout_team)
    LinearLayout mLayoutTeam;

    private String username;

    //成为会员或合伙人 MEMBER 会员 PARTNER 合伙人
    String addType = "MEMBER";
    private IWXAPI api;

    @Override
    public int bindLayout() {
        return R.layout.add_member_activity;
    }

    @Override
    public void initView(View view) {

        showTitleBackButton();
        setTitle("申请成为合伙人&会员");
        api = WXAPIFactory.createWXAPI(this, getString(R.string.WEIXIN_APPID), true);
        api.registerApp(getString(R.string.WEIXIN_APPID));


        username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);

        mRbSelect.check(R.id.rb_vip);
        mRbSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_vip:
                        //会员
                        addType = "MEMBER";
                        mLayoutVip.setVisibility(View.VISIBLE);
                        mLayoutTeam.setVisibility(View.GONE);
                        break;
                    case R.id.rb_partnership:
                        //合伙人
                        addType = "PARTNER";


                        mLayoutVip.setVisibility(View.GONE);
                        mLayoutTeam.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }


    @OnClick({R.id.bt_pay})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.bt_pay:
                //绑定微信
                new LoginUtil().isWxData(mContext);
                //提交支付
                puyData();


                break;
        }
    }



    //提交支付
    private void puyData() {
        String code = mInviteCode.getText().toString();
        String name = mUsername.getText().toString();
        if (name.equals("")) {
            ToastUtil.showToast("姓名不能为空");
            return;
        } else {
            Map<String, Object> map = new HashMap();
            if (!code.equals("")) {
                map.put("code", code);
            }
            map.put("privilege", addType);
            map.put("name", name);

            //获取分类数据
            YSBSdk.getService(OAuthService.class).putMember(map, new YRequestCallback<InviteRecordBean>() {
                @Override
                public void onSuccess(InviteRecordBean var1) {
                    //创建订单 调支付
                    payOrder(addType);

                }

                @Override
                public void onFailed(String var1, String message) {
                    ErrorUtils.error(mContext, var1, message);
                }

                @Override
                public void onException(Throwable var1) {
                    ToastUtil.showToast("数据异常");
                }
            });
        }


    }

    //提交订单支付
    private void payOrder(String addType) {
        Map map = new HashMap();
        map.put("type", addType);
        map.put("attach", "");
        map.put("payType", "WX");
        /*map.put("name",siteData.getName());
        map.put("phone",siteData.getPhone());
        map.put("address",siteData.getAddress());
        List<Map> listmap = new ArrayList();
        Map goodsMap = new HashMap();
        goodsMap.put("goods_id",goods.getGoods_id());
        goodsMap.put("sku_id",goods.getSku_id());
        goodsMap.put("num",goods.getNum());*/
        //listmap.add(goodsMap);
        //map.put("goods",listmap);
        YSBSdk.getService(OAuthService.class).putCarGoods(map, new YRequestCallback<WechatPayBean>() {
            @Override
            public void onSuccess(WechatPayBean var1) {
                Toast.makeText(mContext, "提交订单成功", Toast.LENGTH_LONG).show();
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_pay_type, "6");
                //ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_pay_money,total_number+"");
                PayReq request = new PayReq();
                request.appId = var1.getAppid();
                request.partnerId = var1.getPartnerid();
                request.prepayId = var1.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = var1.getNoncestr();
                request.timeStamp = var1.getTimestamp();
                request.sign = var1.getSign();
                api.sendReq(request);
                finish();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                Toast.makeText(mContext,"message",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable var1) {
                Toast.makeText(mContext,"提交订单失败二",Toast.LENGTH_LONG).show();

            }
        });
    }
}

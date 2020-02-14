package com.shijinsz.shijin.ui.homepage;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.common.SysOSUtil;
import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.activity.adapter.ActivityRuleActivity;
import com.shijinsz.shijin.ui.store.ShopOrderRecord;
import com.shijinsz.shijin.ui.store.UserLocationActivity;
import com.shijinsz.shijin.utils.LoginUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.internal.Util;

/**
 * 商城 我的模块
 */
public class StoreMyFragment extends BaseFragment {

    @BindView(R.id.user_img)
    CircleImageView mUserImg;
    @BindView(R.id.user_name)
    TextView mUsername;
    @BindView(R.id.add_member)
    TextView mAddMember;

    @Override
    protected int provideContentViewId() {
        //个人中心 R.layout.store_message_view
        return R.layout.store_mine_view;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);

        String name = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        if (name.equals("")) {
            mUsername.setText("请登录");
            mUserImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginUtil.isLogin(getContext());
                }
            });

        } else {
            mUsername.setText(name);
            String img = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl);
            Glide.with(mActivity).load(img).into(mUserImg);
        }

        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip).equals("true")){
            mAddMember.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.tv_user_site, R.id.bt_payment, R.id.bt_shipments, R.id.bt_take, R.id.bt_accomplish,
            R.id.my_group, R.id.my_wallet, R.id.add_member})
    public void onClickView(View view) {

        String type = "null";
        if (!LoginUtil.isLogin(getContext())) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_user_site:
                Intent intent = new Intent(mActivity, UserLocationActivity.class);
                intent.putExtra("select","false");
                startActivity(intent);
                break;
            case R.id.all_order:
                type = "";
                break;
            case R.id.bt_payment:
                //待支付
                type = "WAITING-PAYMENT";
                break;
            case R.id.bt_shipments:
                //待发货
                type = "WAITING-DELIVER-GOODS";
                break;
            case R.id.bt_take:
                //待收货
                type = "WAITING-RECEIVEING-GOODS";
                break;
            case R.id.bt_accomplish:
                //已完成
                type = "END";
                break;
            case R.id.my_group:
                //我的团队
                startActivity(new Intent(mActivity, MaGroupActivity.class));
                break;
            case R.id.my_wallet:
                //我的钱包
                startActivity(new Intent(mActivity, MaWalletActivity.class));
                break;
            case R.id.add_member:
                //成为合伙人
                startActivity(new Intent(mActivity, AddMemberActivity.class));
                break;
        }


        if (!type.equals("null")) {
            Intent intent = new Intent(mActivity, ShopOrderRecord.class);
            intent.putExtra("type", "GOODS");
            intent.putExtra("state", type);
            startActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip).equals("true")){
            mAddMember.setVisibility(View.GONE);
        }


    }
}

package com.shijinsz.shijin.ui.mine;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.hclibrary.view.FlowTag;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.UserRankingBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.RankingAdapter;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.GlideCircleTransform;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * 用户排行榜
 */
public class UserRankingActivity extends BaseActivity {

    @BindView(R.id.one_img)
    ImageView mOneImg;
    @BindView(R.id.one_name)
    TextView mOneName;
    @BindView(R.id.one_gold)
    TextView mOneGold;
    @BindView(R.id.two_img)
    ImageView mTwoImg;
    @BindView(R.id.two_name)
    TextView mTwoName;
    @BindView(R.id.two_gold)
    TextView mTwoGold;
    @BindView(R.id.three_img)
    ImageView mThreeImg;
    @BindView(R.id.three_name)
    TextView mThreeName;
    @BindView(R.id.three_gold)
    TextView mThreeGold;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView mRecycler;

    @BindView(R.id.my_index)
    TextView myIndex;
    @BindView(R.id.my_img)
    CircleImageView myImg;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.my_num)
    TextView myNum;

    UserRankingBean listData = new UserRankingBean();
    RankingAdapter adapter;

    private String username;



    @Override
    public int bindLayout() {
        return R.layout.user_gold_ranking;
    }

    @Override
    public void initView(View view) {


        username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        initData();

    }

    //获取数据
    private void initData() {

        Map<String,Object> map = new HashMap<>();
        map.put("loginName",username);

        YSBSdk.getService(OAuthService.class).getRanking(map,new YRequestCallback<UserRankingBean>() {
            @Override
            public void onSuccess(UserRankingBean var1) {
                listData = var1;
                for (int i = 0; i <= 2 ; i++) {
                    if (i == 0) {
                        setData(mOneImg, mOneName, mOneGold, listData.getList().get(i));
                    } else if (i == 1) {
                        setData(mTwoImg, mTwoName, mTwoGold, listData.getList().get(i));
                    } else {
                        setData(mThreeImg, mThreeName, mThreeGold, listData.getList().get(i));
                    }
                }

                //移除前三名
                listData.getList().remove(0);
                listData.getList().remove(0);
                listData.getList().remove(0);
                adapter = new RankingAdapter(R.layout.user_ranking_item,listData.getList());
                mRecycler.setAdapter(adapter);
                //设置自己的数据
                setMyData(listData.getUser());
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }


    //圆形图片边框颜色
    private void setData(ImageView img, TextView name, TextView gold, UserRankingBean.Ranking data){
        GlideApp.with(mContext).load(data.getImgUrl()).centerCrop()
                .transform(new GlideCircleTransform(mContext,2,mContext.getResources().getColor(R.color.color_f6)))
                .into(img);
        name.setText(data.getNickname());
        gold.setText(String.valueOf(data.getGoldCoin()));
    }

    //设置自己的排行信息
    private void setMyData(UserRankingBean.Ranking data){
        myIndex.setText(String.valueOf(data.getIndex()));
        Glide.with(mContext).load(data.getImgUrl()).into(myImg);
        myName.setText(data.getNickname());
        myNum.setText(String.valueOf(data.getGoldCoin()));
    }

    @OnClick({R.id.ranking_explain,R.id.tv_return})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.ranking_explain:
                //榜单说明
                DialogUtils dialog = new DialogUtils(mContext);
                dialog.showRankingExplainDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager systemService = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        systemService.setPrimaryClip(ClipData.newPlainText("text", "shijinsz1"));
                        dialog.dismissRankingExplainDialog();
                        getWechatApi();
                    }
                });
                break;
            case R.id.tv_return:
                finish();
                break;
        }
    }

    /**
     * 跳转到微信
     */
    private void getWechatApi() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast("检查到您手机没有安装微信，请安装后使用该功能");
        }
    }



}

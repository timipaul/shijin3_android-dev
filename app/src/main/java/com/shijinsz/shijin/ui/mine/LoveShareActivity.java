package com.shijinsz.shijin.ui.mine;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.LoveShareGroupBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.LoveShareGroupAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.GlideCircleTransform;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/** 爱分享 我的团队 */
public class LoveShareActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.gold_num)
    TextView mGoldNum;
    @BindView(R.id.group_num)
    TextView mGroupNum;
    @BindView(R.id.user_img)
    CircleImageView mUserImg;
    @BindView(R.id.user_name)
    TextView mUsername;
    @BindView(R.id.my_num)
    TextView My_num;

    LoveShareGroupAdapter adapter;
    List<LoveShareGroupBean.Subordinate.Child> mList = new ArrayList<>();

    @Override
    public int bindLayout() { return R.layout.love_share_group_activity; }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(getString(R.string.my_group));
        showTitleBackButton();

        refresh.setRefreshHeader(new HeaderView(mContext));
        //refresh.setOnLoadMoreListener(this);
        //refresh.setOnRefreshListener(this);

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData();
            }
        });

        adapter = new LoveShareGroupAdapter(R.layout.item_love_share_group,mList);
        recyclerView.setAdapter(adapter);

        mStateView.showLoading();

        String img = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl);
        String name = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname);
        mUsername.setText(name);
        GlideApp.with(mContext).load(img).centerCrop()
                .transform(new GlideCircleTransform(mContext,5,mContext.getResources().getColor(R.color.color_6f)))
                .into(mUserImg);
        mGroupNum.setText(getString(R.string.love_share_group,0));
        getData();

    }

    private void getData(){

        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).getSubordinate(map, new YRequestCallback<LoveShareGroupBean>() {
            @Override
            public void onSuccess(LoveShareGroupBean var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();

                My_num.setText(getString(R.string.love_share_my,var1.getSv()));
                mGoldNum.setText(String.valueOf(var1.getAchievement()));
                mGroupNum.setText(getString(R.string.love_share_group,var1.getPeopleNum()));

                //if (var1.getMySubordinate().getList().size() == 0){
                    //rlEmpty.setVisibility(View.VISIBLE);
                //}else{
                    //rlEmpty.setVisibility(View.GONE);
                //}
                if(var1.getMySubordinate() != null){
                    mList.addAll(var1.getMySubordinate().getList());
                }

                adapter.notifyDataSetChanged();
                mStateView.showContent();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                Log.i(TAG,"-------onException--------");
                mStateView.showContent();
            }
        });
    }
}

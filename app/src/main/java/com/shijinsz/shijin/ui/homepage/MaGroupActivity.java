package com.shijinsz.shijin.ui.homepage;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.hclibrary.view.Utils;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.InviteRecordBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.homepage.adapter.RecommendAwardAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/** 商城团队 */
public class MaGroupActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.group_num)
    TextView groupNum;
    @BindView(R.id.user_img)
    CircleImageView mUserImg;
    @BindView(R.id.user_name)
    TextView mUsername;
    @BindView(R.id.not_data_show)
    ImageView not_show;

    RecommendAwardAdapter adapter;
    List<InviteRecordBean.Child> data = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.my_group_activity;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.my_group));

        String name = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        mUsername.setText(name);
        String img = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl);
        Glide.with(mActivity).load(img).into(mUserImg);

        adapter = new RecommendAwardAdapter(R.layout.item_award_layout,data);
        recyclerView.setAdapter(adapter);

        initData();
    }


    private void initData() {
        Map<String,Object> map = new HashMap();
        map.put("pageSize","100");
        map.put("pageIndex","1");
        YSBSdk.getService(OAuthService.class).getInviteRecord(map,new YRequestCallback<InviteRecordBean>() {
            @Override
            public void onSuccess(InviteRecordBean var1) {
                if(var1.getChild().size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    not_show.setVisibility(View.GONE);
                }


                data.addAll(var1.getChild());
                groupNum.setText("" + var1.getNum());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
            }
        });
    }

}

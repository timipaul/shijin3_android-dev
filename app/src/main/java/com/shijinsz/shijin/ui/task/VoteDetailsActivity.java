package com.shijinsz.shijin.ui.task;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.task.adapter.VoteSelectAdapter;
import com.shijinsz.shijin.ui.task.adapter.VoteSelectGridAdapter;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: VoteDetailsActivity
 * Author: m1342
 * Date: 2019/6/25 16:16
 * Description: 投票详情
 */
public class VoteDetailsActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.listView)
    ListView mListview;
    @BindView(R.id.gridview)
    GridView mGridview;
    @BindView(R.id.start_date)
    TextView mStart_date;
    @BindView(R.id.number)
    TextView mNumber;
    @BindView(R.id.img_title)
    TextView mImg_title;
    @BindView(R.id.submit)
    Button mSubmit;

    private VoteBean voteBean = new VoteBean();
    private VoteSelectAdapter adapter;
    private VoteSelectGridAdapter gr_adapter;

    private Map<String,Integer> radioMap = new HashMap<>();
    //private List<String> radioList = new ArrayList<>();

    DialogUtils mDialogUtils;

    @Override
    public int bindLayout() { return R.layout.vote_context_select; }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("投票");
        showTitleBackButton();


        mDialogUtils = new DialogUtils(mContext);

        View view_head = View.inflate(mContext,R.layout.vote_head_title,null);
        TextView title = view_head.findViewById(R.id.title_name);
        Bundle data = getIntent().getExtras();
        Gson gson = new Gson();
        voteBean = gson.fromJson(data.getString("voteBean"),VoteBean.class);
        title.setText(voteBean.getContent().getTitle());

        mListview.addHeaderView(view_head);

        if(voteBean.getContent().getVoteType().equals("1")){
            findViewById(R.id.buttom).setVisibility(View.GONE);
            View view_footer = View.inflate(mContext,R.layout.common_vote_bottom,null);
            ((TextView)view_footer.findViewById(R.id.start_date)).setText(getString(R.string.start_date,voteBean.getEnd_time()));
            ((TextView)view_footer.findViewById(R.id.number)).setText(getString(R.string.join_people_number,voteBean.getCount()));
            mListview.addFooterView(view_footer);
            if(voteBean.getVoted().equals("true")){
                ((Button)view_footer.findViewById(R.id.submit)).setText("已提交");
                view_footer.findViewById(R.id.submit).setClickable(false);

            }else{
                view_footer.findViewById(R.id.submit).setOnClickListener(this);
            }

            //文字
            adapter = new VoteSelectAdapter(mContext,voteBean,R.layout.vote_selece_item,R.layout.vote_context_item);
            mListview.setAdapter(adapter);
            mGridview.setVisibility(View.GONE);
            mImg_title.setVisibility(View.GONE);
            mListview.setVisibility(View.VISIBLE);
            adapter.setOnItemClickListener(new VoteSelectAdapter.OnItemClickListener() {


                @Override
                public void buttonDataMore(int item,int index) {
                    radioMap.put(item+"",index);
                    //System.out.println("当前选中数据" + radioMap.toString());
                }
            });
        }else if(voteBean.getContent().getVoteType().equals("2")){
            //图片
            mImg_title.setText(voteBean.getContent().getTitle());
            mStart_date.setText(getString(R.string.start_date,voteBean.getEnd_time()));
            mNumber.setText(getString(R.string.join_people_number,voteBean.getCount()));
            mImg_title.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.GONE);
            mGridview.setVisibility(View.VISIBLE);
            gr_adapter = new VoteSelectGridAdapter(mContext,voteBean,R.layout.vote_select_img_item);
            mGridview.setAdapter(gr_adapter);
            gr_adapter.setOnItemClickListener(new VoteSelectGridAdapter.OnItemClickListener() {
                @Override
                public void layoutOnclick(int position,String index) {
                    for(int i = 0;i < voteBean.getContent().getInfo().size(); i++){
                        View layout = mGridview.getChildAt(i).findViewById(R.id.button_layout);
                        if(position == i){
                            radioMap.clear();
                            radioMap.put(position+"",Integer.valueOf(index));
                            layout.setBackgroundResource(R.drawable.color_red_5);
                        }else{
                            layout.setBackgroundResource(R.drawable.color_gray_5);
                        }

                    }
                }
            });
            if(voteBean.getVoted().equals("true")){
                mSubmit.setText("已提交");
                mSubmit.setClickable(false);
            }else{
                mSubmit.setOnClickListener(this);
            }
        }
    }


    @Override
    public void onClick(View v) {

        v.setClickable(false);

        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        List<Integer> result = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : radioMap.entrySet()){
            result.add(entry.getValue());
        }
        Map map = new HashMap();
        map.put("username", username);
        map.put("voteId", voteBean.getId());
        map.put("result", result);
        YSBSdk.getService(OAuthService.class).join_vote(map, new YRequestCallback<VoteBean>() {
            @Override
            public void onSuccess(VoteBean var1) {
                mDialogUtils.showVoteSucceedDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogUtils.dismissVoteSucceedDialog();
                        finish();
                    }
                });
            }

            @Override
            public void onFailed(String var1, String message) {
                Toast toast=Toast.makeText(getApplicationContext(), "已投票,不要重复投", Toast.LENGTH_SHORT);
                //第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶
                //第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移
                //第三个参数：同的第二个参数道理一样
                //如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示
                //toast.setGravity(Gravity.TOP|Gravity.CENTER, -50, 100);
                //屏幕居中显示，X轴和Y轴偏移量都是0
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }
}

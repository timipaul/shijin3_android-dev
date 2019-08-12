package com.shijinsz.shijin.ui.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.MyProgressBar;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;
import retrofit2.http.GET;

/**
 * Copyright (C)
 * FileName: ContentTaskActivity
 * Author: m1342
 * Date: 2019/7/22 9:48
 * Description: 任务子页面
 */
public class ContentTaskActivity extends BaseActivity {

    @BindView(R.id.like_hint)
    TextView mLike_hint;
    @BindView(R.id.transmit_hint)
    TextView mTransmit_hint;
    @BindView(R.id.comment_hint)
    TextView mComment_hint;
    @BindView(R.id.attention_hint)
    TextView mAttention_hint;
    @BindView(R.id.collect_hint)
    TextView mCollect_hint;
    @BindView(R.id.like_but)
    Button mLike_but;
    @BindView(R.id.transmit_but)
    Button mTransmit_but;
    @BindView(R.id.comment_but)
    Button mComment_but;
    @BindView(R.id.attention_but)
    Button mAttention_but;
    @BindView(R.id.collect_but)
    Button mCollect_but;
    @BindView(R.id.likebar)
    MyProgressBar mLikebar;
    @BindView(R.id.transmit_bar)
    MyProgressBar mTransmit_bar;
    @BindView(R.id.comment_bar)
    MyProgressBar mComment_bar;
    @BindView(R.id.attention_bar)
    MyProgressBar mAttention_bar;
    @BindView(R.id.collect_bar)
    MyProgressBar mCollect_bar;



    DialogUtils mDialogUtils;

    private static final String UNCLAIMED = "0";
    private static final String GET = "1";
    private static final String RECEIVED = "2";

    @Override
    public int bindLayout() {
        return R.layout.task_subtask;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("内容任务");
        showTitleBackButton();

        mDialogUtils = new DialogUtils(mContext);

        getTaskData();
    }

    @OnClick({R.id.like_but,R.id.transmit_but,R.id.comment_but,R.id.attention_but,R.id.collect_but})
    public void onButtonClick(View view){

        view.setClickable(false);

        if(view.getTag(R.id.tag_task_statue) == null ){
            setResult(300);
            finish();
            return;
        }

        //设置tag判断点击状态,是返回还是调接口
        if(view.getTag(R.id.tag_task_statue).equals(GET)){
            //领取奖励
            try {
                getTaskAward(view,view.getTag(R.id.tag_task_button_statue).toString());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获取任务信息
    private void getTaskData() {

        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        YSBSdk.getService(OAuthService.class).get_task_detail(username, new YRequestCallback<TaskBean>() {

            @Override
            public void onSuccess(TaskBean var1) {

                TaskBean.Tasks data = var1.getChallengeTasks();
                for(int i = 0;i < data.getData().size(); i++){
                    String num = String.valueOf(data.getData().get(i).getNum());
                    String next = String.valueOf(data.getData().get(i).getNext());
                    String flag = data.getData().get(i).getFlag();
                    if(flag == null){
                        flag = UNCLAIMED;
                    }
                    switch (data.getData().get(i).getEnTitle()){
                        case "GIVE_THE_THUMBS_UP_NUM":
                            //点赞
                            mLike_hint.setText(String.format(getString(R.string.subtask_schedule),num,next));
                            setButtonState(mLike_but,flag,"GIVE_THE_THUMBS_UP_NUM");
                            setBarData(mLikebar,num,next);
                            break;
                        case "FORWARD_NUM":
                            //转发
                            mTransmit_hint.setText(String.format(getString(R.string.subtask_schedule),num,next));
                            setButtonState(mTransmit_but,flag,"FORWARD_NUM");
                            setBarData(mTransmit_bar,num,next);
                            break;
                        case "COMMENT_NUM":
                            //评论
                            mComment_hint.setText(String.format(getString(R.string.subtask_schedule),num,next));
                            setButtonState(mComment_but,flag,"COMMENT_NUM");
                            setBarData(mComment_bar,num,next);
                            break;
                        case "FOLLOW_NUM":
                            //关注
                            mAttention_hint.setText(String.format(getString(R.string.subtask_schedule),num,next));
                            setButtonState(mAttention_but,flag,"FOLLOW_NUM");
                            setBarData(mAttention_bar,num,next);
                            break;
                        case "COLLECT_NUM":
                            //收藏
                            mCollect_hint.setText(String.format(getString(R.string.subtask_schedule),num,next));
                            setButtonState(mCollect_but,flag,"COLLECT_NUM");
                            setBarData(mCollect_bar,num,next);
                            break;
                    }


                }

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });

    }

    //设置进度条状态
    public void setBarData(MyProgressBar bar,String num,String next){
        bar.setProgress(Integer.valueOf(num));
        bar.setMax(Integer.valueOf(next));
    }

    //获取任务奖励
    public void getTaskAward(View view,String type){
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Map<String,Object> map = new HashMap<>();
        map.put("type","0");
        map.put("taskType",type);
        YSBSdk.getService(OAuthService.class).get_task_award(username,map, new YRequestCallback<TaskBean>() {

            @Override
            public void onSuccess(TaskBean var1) {
                System.out.println("领取成功");
                getTaskData();

                setButtonState((Button) view,RECEIVED,type);

            }
            @Override
            public void onFailed(String var1, String message) {
                System.out.println(var1 + "状态信息: " + message);
                Toast.makeText(mContext,"领取成功",Toast.LENGTH_LONG).show();

            }
            @Override
            public void onException(Throwable var1) {
                System.out.println("状态信息: " + var1);
            }
        });
    }

    public void setButtonState(Button button,String flag,String button_type){

        if(flag.equals(GET)){
            button.setText("领取");
            button.setTag(R.id.tag_task_statue,GET);
            button.setBackgroundResource(R.drawable.task_button_orange_fill);
            button.setTextColor(Color.WHITE);
            button.setTag(R.id.tag_task_button_statue,button_type);
            button.setClickable(true);
        }else if(flag.equals(RECEIVED)){
            button.setBackgroundResource(R.drawable.task_button_gray_border);
            button.setText("已领取");
            button.setTag(RECEIVED);
            button.setClickable(false);
        }
    }
}

package com.shijinsz.shijin.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Copyright (C)
 * FileName: NewPersonTaskActivity
 * Author: m1342
 * Date: 2019/7/18 16:07
 * Description: 日常任务
 */
public class DayTaskActivity extends BaseActivity {

    //签到
    @BindView(R.id.signin_hint)
    TextView mSigin_hint;
    @BindView(R.id.signin_but)
    Button mSignin_but;
    //转发
    @BindView(R.id.transpond_hint)
    TextView mTranspond_hint;
    @BindView(R.id.transpond_but)
    Button mTranspond_but;
    //点赞
    @BindView(R.id.like_hint)
    TextView mLike_hint;
    @BindView(R.id.like_but)
    Button mLike_but;
    //评论
    @BindView(R.id.comment_hint)
    TextView mComment_hint;
    @BindView(R.id.comment_but)
    Button mComment_but;
    //发布
    @BindView(R.id.issue_hint)
    TextView mIssue_hint;
    @BindView(R.id.issue_but)
    Button mIssue_but;
    //关注
    @BindView(R.id.attention_hint)
    TextView mAttention_hint;
    @BindView(R.id.attention_but)
    Button mAttention_but;
    //互动
    @BindView(R.id.answer_hint)
    TextView mAnswer_hint;
    @BindView(R.id.answer_but)
    Button mAnswer_but;

    private static final String UNCLAIMED = "0";
    private static final String GET = "1";
    private static final String RECEIVED = "2";

    @Override
    public int bindLayout() { return R.layout.day_task; }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("日常任务");
        showTitleBackButton();


        getTaskData();
    }

    @OnClick({R.id.signin_but,R.id.transpond_but,R.id.like_but,R.id.comment_but,R.id.issue_but,
            R.id.attention_but,R.id.answer_but})
    public void onButtonClick(View view){

        view.setClickable(false);

        if(view.getTag(R.id.tag_task_statue) == null){
            switch (view.getId()){
                case R.id.signin_but:
                    //签到

                    break;
                case R.id.transpond_but:
                case R.id.like_but:
                case R.id.comment_but:
                case R.id.issue_but:
                case R.id.attention_but:
                case R.id.answer_but:
                    //跳转首页阅读
                    Bundle data = new Bundle();
                    data.putInt("code",0);
                    setResult(300,new Intent().putExtras(data));
                    finish();
                    break;

            }
            return;
        }

        if(view.getTag(R.id.tag_task_statue).equals(GET)){
            //领取奖励
            try {
                //System.out.println("领取内容: " + view.getTag(R.id.tag_task_button_statue));
                getTaskAward(view.getTag(R.id.tag_task_button_statue).toString());
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
                TaskBean.Tasks data = var1.getDailyTasks();
                for(int i = 0;i < data.getData().size(); i++){
                    String reward = String.valueOf(data.getData().get(i).getReward());
                    String flag = data.getData().get(i).getFlag();
                    switch (data.getData().get(i).getEnTitle()){
                        case "SIGN_IN":
                            //签到
                            mSigin_hint.setText(getString(R.string.signin_hint,reward));
                            setButtonState(mSignin_but,flag,"SIGN_IN");
                            break;
                        case "FORWARD":
                            //转发
                            mTranspond_hint.setText(getString(R.string.transpond_hint,reward));
                            setButtonState(mTranspond_but,flag,"FORWARD");
                            break;
                        case "GIVE_THE_THUMBS_UP":
                            //点赞
                            mLike_hint.setText(getString(R.string.day_task_like_hint,reward));
                            setButtonState(mLike_but,flag,"GIVE_THE_THUMBS_UP");
                            break;
                        case "COMMENT":
                            //评论
                            mComment_hint.setText(getString(R.string.day_task_comment_hint,reward));
                            setButtonState(mComment_but,flag,"COMMENT");
                            break;
                        case "RELEASE":
                            //发布
                            mIssue_hint.setText(getString(R.string.day_task_issue_hint,reward));
                            setButtonState(mIssue_but,flag,"RELEASE");
                            break;
                        case "FOLLOW":
                            //关注
                            mAttention_hint.setText(getString(R.string.day_task_attention_hint,reward));
                            setButtonState(mAttention_but,flag,"FOLLOW");
                            break;
                        case "INTERACTIVE_QUESTION_ANSWER":
                            //问答
                            mAnswer_hint.setText(getString(R.string.day_task_answer_hint,reward));
                            setButtonState(mAnswer_but,flag,"INTERACTIVE_QUESTION_ANSWER");
                            break;

                    }
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                System.out.println("失败返回数据  1 " + message);
            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("失败返回数据  2 ");
            }
        });
    }

    public void setButtonState(Button button,String flag,String button_type){
        if(flag.equals(GET)){
            button.setText("领取");
            button.setTag(R.id.tag_task_statue,GET);
            button.setTag(R.id.tag_task_button_statue,button_type);
            button.setClickable(true);
        }else if(flag.equals(RECEIVED)){
            button.setBackgroundResource(R.mipmap.button_gray_bg);
            button.setText("已完成");
            button.setTag(RECEIVED);
            button.setClickable(false);
        }
    }

    //获取任务奖励
    public void getTaskAward(String type){
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Map<String,Object> map = new HashMap<>();
        map.put("type",UNCLAIMED);
        map.put("taskType",type);
        YSBSdk.getService(OAuthService.class).get_task_award(username,map, new YRequestCallback<TaskBean>() {

            @Override
            public void onSuccess(TaskBean var1) {
                System.out.println("领取成功");
                Toast.makeText(mContext,"领取成功",Toast.LENGTH_LONG).show();
                getTaskData();

            }

            @Override
            public void onFailed(String var1, String message) {
                System.out.println(var1 + "状态信息: " + message);

            }

            @Override
            public void onException(Throwable var1) {
                System.out.println("状态信息: " + var1);
            }
        });
    }

}

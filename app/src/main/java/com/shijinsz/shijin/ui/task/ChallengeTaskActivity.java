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
 * Description: 挑战任务
 */
public class ChallengeTaskActivity extends BaseActivity {

    @BindView(R.id.signin_hint)
    TextView mSignin_hint;
    @BindView(R.id.invite_hint)
    TextView mInvite_hint;
    @BindView(R.id.issue_hint)
    TextView mIssue_hint;
    @BindView(R.id.answer_hint)
    TextView mAnswer_hint;
    @BindView(R.id.signin_but)
    Button mSignin_but;
    @BindView(R.id.invite_but)
    Button mInvite_but;
    @BindView(R.id.issue_but)
    Button mIssue_but;
    @BindView(R.id.answer_but)
    Button mAnswer_but;

    private static final int CONTENT_TASK_CODE = 101;
    private static final String UNCLAIMED = "0";
    private static final String GET = "1";
    private static final String RECEIVED = "2";

    @Override
    public int bindLayout() { return R.layout.challenge_task; }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("挑战任务");
        showTitleBackButton();

        getTaskData();
    }

    @OnClick({R.id.signin_but,R.id.invite_but,R.id.issue_but,R.id.answer_but})
    public void onButtonClick(View view){
        view.setClickable(false);
        if(view.getId() == R.id.signin_but){
            //内容容物列表
            startActivityForResult(new Intent(mContext,ContentTaskActivity.class),CONTENT_TASK_CODE);
            return;
        }

        if(view.getTag(R.id.tag_task_statue) == null){
            return;
        }

        if(view.getTag(R.id.tag_task_statue).equals(GET)){
            //领取奖励
            try {
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

                TaskBean.Tasks data = var1.getChallengeTasks();
                for(int i = 0;i < data.getData().size(); i++){
                    String num = String.valueOf(data.getData().get(i).getNum());
                    String next = String.valueOf(data.getData().get(i).getNext());
                    String flag = data.getData().get(i).getFlag();
                    if(flag == null){
                        flag = UNCLAIMED;
                    }
                    switch (data.getData().get(i).getEnTitle()){
                        case "INVITE_FRIENDS_NUM":
                            //邀请好友
                            mInvite_hint.setText(String.format(getString(R.string.count_invitation),num,next));
                            setButtonState(mInvite_but,flag,"INVITE_FRIENDS_NUM");
                            break;
                        case "RELEASE_NUM":
                            //发布内容数
                            mIssue_hint.setText(String.format(getString(R.string.count_issue),num,next));
                            setButtonState(mIssue_but,flag,"RELEASE_NUM");
                            break;
                        case "ANSWERS_NUM":
                            //答题数
                            mAnswer_hint.setText(String.format(getString(R.string.count_answer),num,next));
                            setButtonState(mAnswer_but,flag,"ANSWERS_NUM");
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

    //获取任务奖励
    public void getTaskAward(String type){
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Map<String,Object> map = new HashMap<>();
        map.put("type",GET);
        map.put("taskType",type);
        YSBSdk.getService(OAuthService.class).get_task_award(username,map, new YRequestCallback<TaskBean>() {

            @Override
            public void onSuccess(TaskBean var1) {
                Toast.makeText(mContext,"领取成功",Toast.LENGTH_LONG).show();
                getTaskData();

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 300){
            Bundle code = new Bundle();
            code.putInt("code",0);
            setResult(300,new Intent().putExtras(code));
            finish();
        }
    }


}

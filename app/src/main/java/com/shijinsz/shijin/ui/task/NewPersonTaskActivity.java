package com.shijinsz.shijin.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ModelLoader;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BoxlistBean;
import com.hongchuang.ysblibrary.model.model.bean.TaskBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.UserInformationActivity;
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
 * Description: 新手任务
 */
public class NewPersonTaskActivity extends BaseActivity {

    @BindView(R.id.my_data_hint)
    TextView mData_hint;
    @BindView(R.id.code_hint)
    TextView mCode_hint;
    @BindView(R.id.invite_hint)
    TextView mInvite_hint;
    @BindView(R.id.send_img_hint)
    TextView mSend_img_hint;
    @BindView(R.id.send_video_hint)
    TextView mSend_video_hint;
    @BindView(R.id.answer_hint)
    TextView mAnswer_hint;
    @BindView(R.id.my_data_but)
    Button mData_but;
    @BindView(R.id.code_but)
    Button mCode_but;
    @BindView(R.id.invite_but)
    Button mInvite_but;
    @BindView(R.id.send_img_but)
    Button mSend_img_but;
    @BindView(R.id.send_video_but)
    Button mSend_video_but;
    @BindView(R.id.answer_but)
    Button mAnswer_but;

    private static final String UNCLAIMED = "0";
    private static final String GET = "1";
    private static final String RECEIVED = "2";

    @Override
    public int bindLayout() { return R.layout.new_person_task; }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("新手任务");
        showTitleBackButton();

        getTaskData();
    }


    @OnClick({R.id.my_data_but,R.id.code_but,R.id.invite_but,R.id.send_img_but,
            R.id.send_video_but,R.id.answer_but})
    public void onButtonClick(View view){

        view.setClickable(false);

        if(view.getTag(R.id.tag_task_statue) == null){
            Bundle data;
            switch (view.getId()){
                case R.id.my_data_but:
                    //完善信息
                    startActivity(UserInformationActivity.class);
                    break;
                case R.id.code_but:
                    //填写邀请码
                    startActivity(WriteInvitationCodeActivity.class);
                    break;
                case R.id.invite_but:
                    //邀请好友
                    startActivity(InviteFriendActivity.class);
                    break;
                case R.id.send_img_but:
                case R.id.send_video_but:
                    //发图文视频
                    data = new Bundle();
                    data.putInt("code",1);
                    setResult(300,new Intent().putExtras(data));
                    finish();

                    break;
                case R.id.answer_but:
                    //互动问答
                    data = new Bundle();
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
                getTaskAward(view.getTag(R.id.tag_task_button_statue).toString());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("", "onResume: " );

    }

    //获取任务信息
    private void getTaskData() {

        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        YSBSdk.getService(OAuthService.class).get_task_detail(username, new YRequestCallback<TaskBean>() {

            @Override
            public void onSuccess(TaskBean var1) {
                TaskBean.Tasks data = var1.getNewbieTask();
                for(int i = 0;i < data.getData().size(); i++){
                    String reward = String.valueOf(data.getData().get(i).getReward());
                    String flag = data.getData().get(i).getFlag();
                    if(flag == null){
                        flag = UNCLAIMED;
                    }
                    switch (data.getData().get(i).getEnTitle()){
                        case "SUPPLEMENTARY_INFORMATION":
                            //补齐资料
                            mData_hint.setText(getString(R.string.my_data_task_hint,reward));
                            setButtonState(mData_but,flag,"SUPPLEMENTARY_INFORMATION");

                            break;
                        case "FILL_IN_THE_INVITATION_CODE":
                            //填写邀请码
                            mCode_hint.setText(getString(R.string.invitation_code_task_hint,reward));
                            setButtonState(mCode_but,flag,"FILL_IN_THE_INVITATION_CODE");

                            break;
                        case "TO_SEND_PIRCTURES_AND_TEXTS":
                            //发图文
                            mSend_img_hint.setText(getString(R.string.first_issue_img_hint,reward));
                            setButtonState(mSend_img_but,flag,"TO_SEND_PIRCTURES_AND_TEXTS");

                            break;
                        case "TO_SEND_VIDEO":
                            //视频
                            mSend_video_hint.setText(getString(R.string.first_issue_video_hint,reward));
                            setButtonState(mSend_video_but,flag,"TO_SEND_VIDEO");

                            break;
                        case "INVITE_FRIENDS":
                            //邀请好友
                            mInvite_hint.setText(getString(R.string.invitation_friend_hint,reward));
                            setButtonState(mInvite_but,flag,"INVITE_FRIENDS");

                            break;
                        case "INTERACTIVE_QUESTION_ANSWER":
                            //互动问答
                            mAnswer_hint.setText(getString(R.string.first_finish_answer_hint,reward));
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

    //获取任务奖励
    public void getTaskAward(String type){
        String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);
        Map<String,Object> map = new HashMap<>();
        map.put("type",RECEIVED);
        map.put("taskType",type);
        YSBSdk.getService(OAuthService.class).get_task_award(username,map, new YRequestCallback<TaskBean>() {

            @Override
            public void onSuccess(TaskBean var1) {
                Toast.makeText(mContext,"领取成功",Toast.LENGTH_LONG).show();
                getTaskData();

            }

            @Override
            public void onFailed(String var1, String message) {
                System.out.println(var1 + "状态信息: " + message);
                Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();

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
            button.setTag(R.id.tag_task_button_statue,button_type);
            button.setClickable(true);
        }else if(flag.equals(RECEIVED)){
            button.setBackgroundResource(R.mipmap.button_gray_bg);
            button.setText("已完成");
            button.setTag(RECEIVED);
            button.setClickable(false);
        }

    }
}

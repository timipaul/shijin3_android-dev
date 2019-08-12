package com.shijinsz.shijin.ui.task;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationInfoBean;
import com.hongchuang.ysblibrary.model.model.bean.InvitationRecordBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.utils.ShareDialog;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/3.
 * 邀请好友页面
 */
public class InviteFriendActivity extends BaseActivity {
    @BindView(R.id.marquee_view)
    MarqueeView marqueeView;
    @BindView(R.id.tv_get_money)
    TextView tvGetMoney;
    @BindView(R.id.tv_people_num)
    TextView tvPeopleNum;
    public List<String> data=new ArrayList<>();
    private String money,people;
    @Override
    public int bindLayout() {
        return R.layout.invate_friend;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(R.string.invite_friend);
        showTitleRightBackButton(R.mipmap.icon_gantan, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext,InviteRuleActivity.class);
                intent.putExtra("rule1",rule1);
                intent.putExtra("rule2",rule2);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
        getList();

    }

    @Override
    protected void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }

    //顶部切换信息展示
    private void getList() {
        Map map= new HashMap();
        map.put("mode","open");
        YSBSdk.getService(OAuthService.class).invitation_record(map, new YRequestCallback<BaseBean<InvitationRecordBean>>() {
            @Override
            public void onSuccess(BaseBean<InvitationRecordBean> var1) {
                if (var1.getInvitations().size()>0){
                    for (InvitationRecordBean invitationRecordBean : var1.getInvitations()) {
                        String str="";
                        if (invitationRecordBean.getReward_mode().equals("change"))
                        str=String.format(getString(R.string.invited_record),invitationRecordBean.getNickname(),invitationRecordBean.getInvite_people_number(),invitationRecordBean.getInviter_reward()+getString(R.string.yuan));
                        else
                            str=String.format(getString(R.string.invited_record),invitationRecordBean.getNickname(),invitationRecordBean.getInvite_people_number(),invitationRecordBean.getInviter_reward()+getString(R.string.point));
                        data.add(str);
                    }
                    marqueeView.startWithList(data);
                }else{

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

    private String rule1;
    private String rule2;
    //获取邀请信息
    private void getInfo() {
        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).invitation_info(map, new YRequestCallback<InvitationInfoBean>() {
            @Override
            public void onSuccess(InvitationInfoBean var1) {


                tvGetMoney.setText(String.format(getString(R.string.invite_money),var1.getInvite_change_number()));
                tvPeopleNum.setText(String.format(getString(R.string.invite_people),var1.getInvite_people_number()));

                money=var1.getInvite_change_number();
                people=var1.getInvite_people_number();
                rule1=var1.getReward_rule().getRule1();
                rule2=var1.getReward_rule().getRole2();
                if (var1.getInvitation_code()!=null){
                    ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_invitation_code,var1.getInvitation_code());
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

    @Override
    protected void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }

    //获取分享信息
    public void getShare(){
        mStateView.showLoading();
        String mode ="share_to_friend";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                mStateView.showContent();
                new ShareDialog(mActivity).showWithdrapDialog(mActivity,3,var1.getShare_title(),var1.getShare_info(),var1.getShare_pic(), Comment.url+"invitation_registration?nickname="+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname)+"&username="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+"&imageurl="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl));
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
    @OnClick({R.id.bt_invite_now, R.id.bt_invite_people, R.id.tv_invite_now, R.id.tv_invite_code, R.id.tv_invite_face, R.id.tv_invite_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_invite_now:
                getShare();
                break;
            case R.id.bt_invite_people:
                Intent intent = new Intent(mContext,InviteRecordActivity.class);
                intent.putExtra("money",money);
                intent.putExtra("people",people);
                startActivity(intent);
                break;
            case R.id.tv_invite_now:
                getShare();
                break;
            case R.id.tv_invite_code:
                startActivity(InviteCodeActivity.class);
                break;
            case R.id.tv_invite_face:
                startActivity(InviteFaceActivity.class);
                break;
            case R.id.tv_invite_group:
                startActivity(InviteGroupActivity.class);
                break;
        }
    }

}

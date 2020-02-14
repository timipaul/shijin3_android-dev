package com.shijinsz.shijin.ui.task.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hongchuang.hclibrary.manager.MActivityManager;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.Constants;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.BoxlistBean;
import com.hongchuang.ysblibrary.model.model.bean.LotteryListBean;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.hongchuang.ysblibrary.model.model.bean.UserBean;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.comm.util.AdError;
import com.shijinsz.shijin.MainActivity;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.ad.NewAdActivity;
import com.shijinsz.shijin.ui.home.PerfectInformation1Activiity;
import com.shijinsz.shijin.ui.task.ChallengeTaskActivity;
import com.shijinsz.shijin.ui.task.DayTaskActivity;
import com.shijinsz.shijin.ui.task.InviteFriendActivity;
import com.shijinsz.shijin.ui.task.NewPersonTaskActivity;
import com.shijinsz.shijin.ui.task.SignInActivity;
import com.shijinsz.shijin.ui.task.VoteShowListActivity;
import com.shijinsz.shijin.ui.task.WriteInvitationCodeActivity;
import com.shijinsz.shijin.utils.BoxGifUtils;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LoginUtil;
import com.xiqu.sdklibrary.util.XWUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/30.
 */

public class TaskFragment extends BaseFragment implements UnifiedBannerADListener {
    @BindView(R.id.cutdown)
    TextView cutdown;
    @BindView(R.id.box)
    RelativeLayout box;
    @BindView(R.id.tv_share)
    TextView tvShare;
    //    @BindView(R.id.gif)
//    GifImageView gif;
    Unbinder unbinder2;
    @BindView(R.id.img_box)
    ImageView imgBox;
    @BindView(R.id.sign_prompt)
    TextView signPrompt;
    @BindView(R.id.vote_prompt)
    TextView votePrompt;
    @BindView(R.id.invite_prompt)
    TextView invitePrompt;

    @BindView(R.id.write_prompt)
    TextView writePrompt;

    @BindView(R.id.income_prompt)
    TextView incomePrompt;
    @BindView(R.id.prefect_prompt)
    TextView prefectPrompt;
    @BindView(R.id.comment_prompt)
    TextView commentPrompt;
    @BindView(R.id.share_prompt)
    TextView sharePrompt;
    @BindView(R.id.feedback_prompt)
    TextView feedbackPrompt;
    @BindView(R.id.invite_code_layout)
    LinearLayout mInvite_code_layout;
    @BindView(R.id.bannerContainer)
    ViewGroup bannerContainer;
    @BindView(R.id.ln_new_person)
    LinearLayout mNewPerson;
    @BindView(R.id.view_button)
    View mView_but;

    @BindView(R.id.game_entrance)
    ImageView mGameInto;

    private static final int INVITATION_CODE = 100;
    private static final int NEW_PERSON_TASK_PAGE_CODE = 101;
    private static final int DAT_TASK_CODE = 102;
    private static final int CHALLENGE_TASK_CODE = 103;
    private static final String TAG = TaskFragment.class.getSimpleName();

    Unbinder unbinder;
    UnifiedBannerView bv;
    String posId;
    private GifDrawable gifDrawable;

    @Override
    protected int provideContentViewId() {
        return R.layout.task_fragment;
    }

    @Override
    protected void loadData() {
        //广告轮播图  暂时被游戏入口代替
        //this.getBanner().loadAD();

        //设置华为暂时隐藏广告
        if(Build.MANUFACTURER.toLowerCase().contains("huawei")){
            YSBSdk.getService(OAuthService.class).getGameStatue(new YRequestCallback<ShenmiBean>() {
                @Override
                public void onSuccess(ShenmiBean var1) {
                    mGameInto.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailed(String var1, String message) {
                    mGameInto.setVisibility(View.GONE);
                }

                @Override
                public void onException(Throwable var1) {

                }
            });
        }

    }
    private boolean isVisible;

    private UnifiedBannerView  getBanner() {
        String posId = Constants.BannerPosID;
        if( this.bv != null && this.posId.equals(posId)) {
            return this.bv;
        }
        if(this.bv != null){
            bannerContainer.removeView(bv);
            bv.destroy();
        }
        this.posId = posId;
        this.bv = new UnifiedBannerView(getActivity(), Constants.APPID, posId, this);
        bannerContainer.addView(bv, getUnifiedBannerLayoutParams());

        return this.bv;
    }



    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x,  Math.round(screenSize.x / 6.4F));
    }

    @Override
    public void onNoAD(AdError adError) {
        String msg = String.format(Locale.getDefault(), "onNoAD, error code: %d, error msg: %s",
                adError.getErrorCode(), adError.getErrorMsg());
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onADReceive() {
        Log.i(TAG, "onADReceive");
        bannerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onADExposure() {
        Log.i(TAG, "onADExposure");
    }

    @Override
    public void onADClosed() {
        Log.i(TAG, "onADClosed");
        bannerContainer.setVisibility(View.GONE);
    }

    @Override
    public void onADClicked() {
        Log.i(TAG, "onADClicked");
    }

    @Override
    public void onADLeftApplication() {
        Log.i(TAG, "onADLeftApplication");
    }

    @Override
    public void onADOpenOverlay() {
        Log.i(TAG, "onADOpenOverlay");
    }

    @Override
    public void onADCloseOverlay() {
        Log.i(TAG, "onADCloseOverlay");
    }




    @Override
    public void onResume() {
        super.onResume();
        Log.e("", "onResume: " );
        if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.Key_isLogin).equals("on")) {
            box.setEnabled(true);
            cutdown.setText("立即登录");
        } else {
            box.setEnabled(false);
        }

        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_invitation_code_state).equals("true")){
            mInvite_code_layout.setVisibility(View.GONE);
        }else {
            mInvite_code_layout.setVisibility(View.VISIBLE);
        }

        //判断新手任务完成状态
        //ShareDataManager.getInstance().save(getContext(),SharedPreferencesKey.KEY_new_person_task,"off");
        if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_new_person_task).equals("true")){
            mNewPerson.setVisibility(View.GONE);
        }

        getBox();

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        Log.e("", "onFragmentVisibleChange: " );
        this.isVisible=isVisible;
        if (isVisible&&ShareDataManager.getInstance().getPara(SharedPreferencesKey.Key_isLogin).equals("on")) {
//
            if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.Key_isLogin).equals("on")) {
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_new_one_status).equals("on")) {
                    //new DialogUtils(getContext()).showNewPacketDialog();
                }
            }
        }
    }

    private CountDownTimer timer;
    private int next = -1;
    private int last = -1;

    public void getBox() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).box_list(map, new YRequestCallback<BoxlistBean>() {
            @Override
            public void onSuccess(BoxlistBean var1) {



                ForegroundColorSpan colorSpan = new ForegroundColorSpan(mActivity.getResources().getColor(R.color.colorPrimary));

                String srt=String.format(getString(R.string.share_answer), var1.getToday_share_number()+"/"+var1.getDaily_task_setting().getAllow_page_share_reward_number());
                SpannableStringBuilder style = new SpannableStringBuilder(srt);
                style.setSpan(colorSpan,5,5+var1.getToday_share_number().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvShare.setText(style);
                String sign=String.format(getString(R.string.signin_prompt),var1.getDaily_task_setting().getSign_in_lottery_day_number());
                SpannableStringBuilder signstyle=new SpannableStringBuilder(sign);
                signstyle.setSpan(colorSpan,8,8+var1.getDaily_task_setting().getSign_in_lottery_day_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                signPrompt.setText(signstyle);

                String vote_num = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KET_vote_number);
                String votePro = String.format(getString(R.string.vote_prompt), vote_num + "金币");
                SpannableStringBuilder votestyle = new SpannableStringBuilder(votePro);
                votestyle.setSpan(colorSpan, 12, 12 + vote_num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                votePrompt.setText(votestyle);

                String writeNum = "300";
                String writePro = String.format(getString(R.string.write_invite_prompt), writeNum);
                SpannableStringBuilder writeStyle = new SpannableStringBuilder(writePro);
                writeStyle.setSpan(colorSpan, 10, 10 + writeNum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                writePrompt.setText(writeStyle);

                if (var1.getDaily_task_setting().getInvitation_reward_mode().equals("change")) {
                    String invite = String.format(getString(R.string.invite_friend_prompt), var1.getDaily_task_setting().getInvitation_reward_number()+"元现金");
                    SpannableStringBuilder invitestyle = new SpannableStringBuilder(invite);
                    invitestyle.setSpan(colorSpan, 9, 9 + var1.getDaily_task_setting().getInvitation_reward_number().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    invitePrompt.setText(invitestyle);
                }else {
                    String invite = String.format(getString(R.string.invite_friend_prompt), var1.getDaily_task_setting().getInvitation_reward_number()+"金币");
                    SpannableStringBuilder invitestyle = new SpannableStringBuilder(invite);
                    invitestyle.setSpan(colorSpan, 9, 9 + var1.getDaily_task_setting().getInvitation_reward_number().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    invitePrompt.setText(invitestyle);
                }

                if (var1.getDaily_task_setting().getIncome_bask_reward_mode().equals("change")) {
                    String income=String.format(getString(R.string.show_income_prompt),var1.getDaily_task_setting().getIncome_bask_reward_number()+"元现金");
                    SpannableStringBuilder incomStytle=new SpannableStringBuilder(income);
                    incomStytle.setSpan(colorSpan,18,18+var1.getDaily_task_setting().getIncome_bask_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    incomePrompt.setText(incomStytle);
                }else {
                    String income=String.format(getString(R.string.show_income_prompt),var1.getDaily_task_setting().getIncome_bask_reward_number()+"金币");
                    SpannableStringBuilder incomStytle=new SpannableStringBuilder(income);
                    incomStytle.setSpan(colorSpan,18,18+var1.getDaily_task_setting().getIncome_bask_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    incomePrompt.setText(incomStytle);
                }
                if (var1.getDaily_task_setting().getInfo_complete_reward_mode().equals("change")) {
                    String prefect=String.format(getString(R.string.prefect_information_prompt),var1.getDaily_task_setting().getInfo_complete_reward_number()+"元现金");
                    SpannableStringBuilder prefectStytle=new SpannableStringBuilder(prefect);
                    prefectStytle.setSpan(colorSpan,21,21+var1.getDaily_task_setting().getInfo_complete_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    prefectPrompt.setText(prefectStytle);
                }else {
                    String prefect=String.format(getString(R.string.prefect_information_prompt),var1.getDaily_task_setting().getInfo_complete_reward_number()+"金币");
                    SpannableStringBuilder prefectStytle=new SpannableStringBuilder(prefect);
                    prefectStytle.setSpan(colorSpan,21,21+var1.getDaily_task_setting().getInfo_complete_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    prefectPrompt.setText(prefectStytle);
                }
                if (var1.getDaily_task_setting().getPage_share_reward_mode().equals("change")) {
                    String share=String.format(getString(R.string.share_answer_prompt),var1.getDaily_task_setting().getPage_share_reward_number()+"元现金",var1.getDaily_task_setting().getAllow_page_share_reward_number());
                    SpannableStringBuilder shareStytle=new SpannableStringBuilder(share);
                    shareStytle.setSpan(colorSpan,22,22+var1.getDaily_task_setting().getPage_share_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sharePrompt.setText(shareStytle);
                }else {
                    String share=String.format(getString(R.string.share_answer_prompt),var1.getDaily_task_setting().getPage_share_reward_number()+"金币",var1.getDaily_task_setting().getAllow_page_share_reward_number());
                    SpannableStringBuilder shareStytle=new SpannableStringBuilder(share);
                    shareStytle.setSpan(colorSpan,22,22+var1.getDaily_task_setting().getPage_share_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sharePrompt.setText(shareStytle);
                }
                if (var1.getDaily_task_setting().getGood_feedback_reward_mode().equals("change")) {
                    String feedback=String.format(getString(R.string.good_feedback_prompt),var1.getDaily_task_setting().getGood_feedback_reward_number()+"元现金");
                    SpannableStringBuilder feedbackStytle=new SpannableStringBuilder(feedback);
                    feedbackStytle.setSpan(colorSpan,22,22+var1.getDaily_task_setting().getGood_feedback_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    feedbackPrompt.setText(feedbackStytle);
                }else {
                    String feedback=String.format(getString(R.string.good_feedback_prompt),var1.getDaily_task_setting().getGood_feedback_reward_number()+"金币");
                    SpannableStringBuilder feedbackStytle=new SpannableStringBuilder(feedback);
                    feedbackStytle.setSpan(colorSpan,22,22+var1.getDaily_task_setting().getGood_feedback_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    feedbackPrompt.setText(feedbackStytle);
                }
                if (var1.getDaily_task_setting().getGood_comment_reward_mode().equals("change")) {
                    String comment=String.format(getString(R.string.good_comment_prompt),var1.getDaily_task_setting().getGood_comment_reward_number()+"元现金");
                    SpannableStringBuilder commentStytle=new SpannableStringBuilder(comment);
                    commentStytle.setSpan(colorSpan,19,19+var1.getDaily_task_setting().getGood_comment_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    commentPrompt.setText(commentStytle);
                }else {
                    String comment=String.format(getString(R.string.good_comment_prompt),var1.getDaily_task_setting().getGood_comment_reward_number()+"金币");
                    SpannableStringBuilder commentStytle=new SpannableStringBuilder(comment);
                    commentStytle.setSpan(colorSpan,19,19+var1.getDaily_task_setting().getGood_comment_reward_number().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    commentPrompt.setText(commentStytle);
                }


                if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.Key_isLogin).equals("on")) {
                    return;
                }
                String str = TimeUtil.format(Long.valueOf(var1.getNow_hms()) * 1000, "HH:mm:ss");
                String HH = str.substring(0, 2);
                String mm = str.substring(3, 5);
                String ss = str.substring(6, 8);
                int hour = Integer.parseInt(HH);
                int minut = Integer.parseInt(mm);
                int secd = Integer.parseInt(ss);
                if (hour - Integer.parseInt(var1.getBox_list().get(0).getTrim_value()) < 0) {
                    hour = hour - Integer.parseInt(var1.getBox_list().get(0).getTrim_value()) + 24;
                } else {
                    hour = hour - Integer.parseInt(var1.getBox_list().get(0).getTrim_value());
                }
                boolean isYesterday = false;//判断是否为前一天
                //判断前后两个宝箱索引
                for (int i = 0; i < var1.getBox_list().size(); i++) {
                    int opentime = Integer.parseInt(var1.getBox_list().get(i).getOpen_time());
                    if (opentime <= hour) {
                        continue;
                    } else {
                        next = i;
                        if (next != 0) {
                            last = next - 1;
                        } else {
                            isYesterday = true;
                            last = var1.getBox_list().size() - 1;
                        }
                        break;
                    }
                }

                if (next == -1) {
                    next = 0;
                    last = var1.getBox_list().size() - 1;
                }
                //判断是否开始倒计时
                int last_time = 0;
                int next_time = 0;
                long nowtime = 0;
                long lasttime = 0;
                long nexttime = 0;
                if (var1.getLast_box_id().equals(var1.getBox_list().get(last).getId())) {//判断上个宝箱是否开启
                    //已开启
                    boxid = var1.getBox_list().get(next).getId();
                    next_time = Integer.parseInt(var1.getBox_list().get(next).getOpen_time());
                    if (next == 0) {//判断是否要加24小时
                        if (!isYesterday) {
                            next_time = next_time + 24;
                        }
                    }
                    nexttime = next_time * 3600 * 1000;
                    nowtime = hour * 3600 * 1000 + minut * 60 * 1000 + secd * 1000;
                    timer = new CountDownTimer(nexttime - nowtime, 1000) {
                        @Override
                        public void onTick(long l) {
                            String t = "";
                            int h = (int) (l / (3600 * 1000));
                            int m = (int) ((l - (3600 * 1000 * h)) / (60 * 1000));
                            int s = (int) ((l - 3600 * 1000 * h - 60 * 1000 * m) / 1000);
                            if (h < 10) {
                                t = "0" + h;
                            } else {
                                t = h + "";
                            }
                            if (m < 10) {
                                t = t + ":0" + m;
                            } else {
                                t = t + ":" + m;
                            }
                            if (s < 10) {
                                t = t + ":0" + s;
                            } else {
                                t = t + ":" + s;
                            }
                            box.setEnabled(false);
                            mView_but.setVisibility(View.VISIBLE);
                            imgBox.setImageResource(R.mipmap.icon_open_box);
                            cutdown.setText(t);
                        }

                        @Override
                        public void onFinish() {
                            box.setEnabled(true);
                            mView_but.setVisibility(View.GONE);
                            if (isAdded()) {
                                imgBox.setImageResource(R.mipmap.icon_open_box);
                                cutdown.setText(getString(R.string.open_box));
                            }
//                edCode.setEnabled(false);
                        }
                    }.start();
                } else {
                    //未开启
                    if (isYesterday) {
                        //不是前一天
                        hour = hour + 24;
                    }
                    int time_length = Integer.parseInt(var1.getBox_list().get(last).getTime_length());
                    nowtime = hour * 3600 * 1000 + minut * 60 * 1000 + secd * 1000;
                    last_time = Integer.parseInt(var1.getBox_list().get(last).getOpen_time());
                    lasttime = (last_time + time_length) * 3600 * 1000;
                    //判断是否超过开启时间
                    if (nowtime < lasttime) {
                        //未超过
                        boxid = var1.getBox_list().get(last).getId();
                        imgBox.setImageResource(R.mipmap.icon_open_box);
                        box.setEnabled(true);
                        cutdown.setText(getString(R.string.open_box));
                    } else {
                        boxid = var1.getBox_list().get(next).getId();
                        //已超过
                        next_time = Integer.parseInt(var1.getBox_list().get(next).getOpen_time());
                        if (next == 0) {//判断是否要加24小时
                            if (!isYesterday) {
                                next_time = next_time + 24;
                            } else {
                                hour = hour - 24;
                            }
                        }
                        nexttime = next_time * 3600 * 1000;
                        nowtime = hour * 3600 * 1000 + minut * 60 * 1000 + secd * 1000;
                        timer = new CountDownTimer(nexttime - nowtime, 1000) {
                            @Override
                            public void onTick(long l) {
                                String t = "";
                                int h = (int) (l / (3600 * 1000));
                                int m = (int) ((l - (3600 * 1000 * h)) / (60 * 1000));
                                int s = (int) ((l - 3600 * 1000 * h - 60 * 1000 * m) / 1000);
                                if (h < 10) {
                                    t = "0" + h;
                                } else {
                                    t = h + "";
                                }
                                if (m < 10) {
                                    t = t + ":0" + m;
                                } else {
                                    t = t + ":" + m;
                                }
                                if (s < 10) {
                                    t = t + ":0" + s;
                                } else {
                                    t = t + ":" + s;
                                }
                                box.setEnabled(false);
                                imgBox.setImageResource(R.mipmap.icon_open_box);
                                cutdown.setText(t);
                            }

                            @Override
                            public void onFinish() {
                                box.setEnabled(true);
                                imgBox.setImageResource(R.mipmap.icon_open_box);
                                cutdown.setText(getString(R.string.open_box));
//                edCode.setEnabled(false);
                            }
                        }.start();
                    }


                }

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    private String boxid = "";

    //点击打开宝箱
    public void openBox() {
        Map map = new HashMap();
        map.put("mode", "box");
        map.put("box_id", boxid);
        YSBSdk.getService(OAuthService.class).open_box(map, new YRequestCallback<BaseBean<LotteryListBean>>() {
            @Override
            public void onSuccess(BaseBean<LotteryListBean> var1) {
                new BoxGifUtils().showGif(mActivity, var1.getReward_lottery().getLottery_name());
                getBox();
                getUserInfo();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(),var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }
    private void getUserInfo() {
        Map map = new HashMap();
        map.put("mode", "profile");
        YSBSdk.getService(OAuthService.class).getuserinfo(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<UserBean.UserDetailBean>() {
            @Override
            public void onSuccess(UserBean.UserDetailBean var1) {
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_change, var1.getChange());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_points, var1.getPoints());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_fan_number, var1.getFan_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_change, var1.getHistory_change());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_points, var1.getHistory_points());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_recharge_change, var1.getHistory_recharge_change());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_history_recharge_points, var1.getHistory_recharge_points());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_draw_lottery_number, var1.getDraw_lottery_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_imageurl, var1.getImageurl());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_nickname, var1.getNickname());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_birth, var1.getBirth());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_gender, var1.getGender());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_address, var1.getAddress());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_income, var1.getIncome());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_job, var1.getJob());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_interest, var1.getInterests());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_is_advertiser, var1.getIs_advertiser());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_USER_NAME, var1.getUsername());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_withdraw_change_number, var1.getWithdraw_change_number());
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_OPENID, var1.getOpenid());
            }

            @Override
            public void onFailed(String var1, String message) {
//                ErrorUtils.error(getContext(),var1);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    @OnClick({R.id.ln_sign, R.id.ln_invite, R.id.ln_show_income,R.id.game_entrance,
            R.id.ln_prefect_information, R.id.good_comment, R.id.ln_share_answer, R.id.ln_good_feedback,
            R.id.ln_follow_wechat, R.id.box,R.id.ln_vote,R.id.invite_code_layout,R.id.ln_new_person,
            R.id.ln_day_person,R.id.ln_challenge_person,R.id.view_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_sign:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                startActivity(new Intent(mActivity, SignInActivity.class));
                break;
            case R.id.ln_invite:
                //邀请好友
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                startActivity(new Intent(mActivity, InviteFriendActivity.class));
                break;
            case R.id.ln_show_income:
//                startActivity(new Intent(mActivity,SignupActivity.class));
//                if (!LoginUtil.isLogin(mActivity)){
//                    return;
//                }
                gotoQQ();
                break;
            case R.id.ln_prefect_information:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_complete_info).equals("off")) {
                    startActivity(new Intent(mActivity, PerfectInformation1Activiity.class));
                } else {
                    ToastUtil.showToast("您的信息已完善");
                }

                break;
            case R.id.good_comment:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                gotoQQ();
                break;
            case R.id.ln_share_answer:
                ((MainActivity)mActivity).setCurrentItem(0);
//                mActivity.startActivity(new Intent(mActivity,MainActivity.class));
                break;
            case R.id.ln_good_feedback:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                gotoQQ();
                break;
            case R.id.ln_follow_wechat:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                //关注微信公众号
                DialogUtils dialogWeChat = new DialogUtils(getContext());
                dialogWeChat.showTaskAttentionWeChatDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager systemService = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        systemService.setPrimaryClip(ClipData.newPlainText("text", "十金时代"));
                        dialogWeChat.dismissTaskAttentionWeChatDialog();
                        getWechatApi();
                    }
                });
                break;
            case R.id.box:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                openBox();
//                gif.setVisibility(View.VISIBLE);
//                gifDrawable.reset();
                break;
            case R.id.ln_vote:
                /*String url = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_vote_url);
                if(url != null && !url.equals("")){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);//此处填链接
                    intent.setData(content_url);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"链接地址异常",Toast.LENGTH_LONG).show();
                }*/

                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                //跳转到投票页面
                startActivity(new Intent(getContext(),VoteShowListActivity.class));
                break;
            case R.id.invite_code_layout:
                //跳转到邀请码回填页面
                startActivityForResult(new Intent(getContext(), WriteInvitationCodeActivity.class),INVITATION_CODE);
                break;
            case R.id.ln_new_person:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                //新手任务
                startActivityForResult(new Intent(getContext(),NewPersonTaskActivity.class),NEW_PERSON_TASK_PAGE_CODE);
                //showAD();
                //getIAD().loadAD();
                break;
            case R.id.ln_day_person:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                //日常任务
                startActivityForResult(new Intent(getContext(),DayTaskActivity.class),DAT_TASK_CODE);
                //showAD();
                break;
            case R.id.ln_challenge_person:
                if (!LoginUtil.isLogin(mActivity)){
                    return;
                }
                //挑战任务
                startActivityForResult(new Intent(getContext(),ChallengeTaskActivity.class),CHALLENGE_TASK_CODE);
                break;
            case R.id.view_button:
                Toast toast = Toast.makeText(getContext(), "等待倒计时结束再领取吧！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case R.id.game_entrance:
                //推广游戏入口
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }

                //嘻趣游戏
                String username = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
                XWUtils.getInstance(getContext()).init(Constants.XIQU_APPID,Constants.XIQU_SECRET,username);
                //跳转进入广告展示页面
                XWUtils.getInstance(getContext()).jumpToAd();
                break;
        }
    }

    private InterstitialAD iad;

    private InterstitialAD getIAD() {
        String posId = "8575134060152130849";
        //String posId = "1000277230686372";
        if (iad != null && this.posId.equals(posId)) {
            return iad;
        }
        this.posId = posId;
        if (this.iad != null) {
            iad.closePopupWindow();
            iad.destroy();
            iad = null;
        }
        if (iad == null) {
            iad = new InterstitialAD(getActivity(), "1101152570", posId);
        }
        return iad;
    }

    private void showAD() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s",
                                error.getErrorCode(), error.getErrorMsg()));
            }

            @Override
            public void onADReceive() {
                Log.i("AD_DEMO", "onADReceive");
                iad.show();
            }
        });
        iad.loadAD();
    }

    private void showAsPopup() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s",
                                error.getErrorCode(), error.getErrorMsg()));
            }

            @Override
            public void onADReceive() {
                iad.showAsPopupWindow();
            }
        });
        iad.loadAD();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INVITATION_CODE){
            if(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_invitation_code_state).equals("true")){
                mInvite_code_layout.setVisibility(View.GONE);
            }
        }else if(requestCode == NEW_PERSON_TASK_PAGE_CODE || requestCode == DAT_TASK_CODE || requestCode == CHALLENGE_TASK_CODE){
            if(resultCode == 300){
                int code = data.getExtras().getInt("code");
                if(code == 0){
                    ((MainActivity)MActivityManager.getInstance().getActivity(MainActivity.class)).setCurrentItem(0);
                }else if(code == 1){
                    //发图文视频
                    startActivity(new Intent(getContext(),NewAdActivity.class));
                }
            }

        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void gotoQQ() {
        if (checkApkExist(getContext(), "com.tencent.mobileqq")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + getString(R.string.QQNUM) + "&version=1")));
        } else {
            Toast.makeText(getContext(), "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
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

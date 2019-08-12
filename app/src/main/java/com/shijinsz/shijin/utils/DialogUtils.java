package com.shijinsz.shijin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.ui.ad.NewGraphicActivity;
import com.shijinsz.shijin.ui.ad.NewVideoActivity;
import com.shijinsz.shijin.ui.mine.MyVipActivity;
import com.shijinsz.shijin.ui.user.LoginActivity;
import com.shijinsz.shijin.ui.wallet.BoxListActivity;
import com.shijinsz.shijin.ui.wallet.PointActivity;
import com.shijinsz.shijin.ui.wallet.WalletActivity;
import com.shijinsz.shijin.ui.wallet.WithdrawActivity;

import org.w3c.dom.Text;

import java.util.List;

import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/28.
 */

public class DialogUtils {
    public Context mContext;
    public Activity mActivity;
    public LayoutInflater inflater;
    public RelativeLayout redPacketView,openRedPacketView,openPointpacketView,lateView;
    public NoticeDialog redPacketDialog,openRedPacketDialog,openPointpacketDialog,lateDialog;

    public DialogUtils(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public DialogUtils(Activity mContext) {
        this.mActivity = mContext;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public RelativeLayout commentView;
    public NoticeDialog commentDialog;

    /**
     * 通用弹框
     * @param content
     * @param listener
     */
    public void showCommentDialog(String content, View.OnClickListener listener) {

        commentView = (RelativeLayout) inflater.inflate(R.layout.logout_dialog, null);
        commentDialog = new NoticeDialog(mContext);
        ((TextView)commentView.findViewById(R.id.content)).setText(content);
        commentDialog.showDialog(commentView, 0, 0);
        ((TextView)commentView.findViewById(R.id.yes)).setOnClickListener(listener);
        ((TextView)commentView.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentDialog.dismiss();
            }
        });
    }
    /**
     * 通用弹框
     * @param content
     * @param listener
     */
    public void showCommentDialog(String content,View.OnClickListener listener,View.OnClickListener dislistener) {

        commentView = (RelativeLayout) inflater.inflate(R.layout.logout_dialog, null);
        commentDialog = new NoticeDialog(mContext);
        ((TextView)commentView.findViewById(R.id.content)).setText(content);
        commentDialog.showDialog(commentView, 0, 0);
        ((TextView)commentView.findViewById(R.id.yes)).setOnClickListener(listener);
        ((TextView)commentView.findViewById(R.id.no)).setOnClickListener(dislistener);
    }
    public void dismissCommentDialog(){
        commentDialog.dismiss();
    }

    /**
     * 挑战任务完成
     */
    private View taskAwardView;
    private NoticeDialog taskAwardDialog;
    public void showTaskAwardDialog(String num){
        taskAwardView = (RelativeLayout) inflater.inflate(
                R.layout.task_achieve_pop, null);

        String hint = mContext.getString(R.string.subtask_schedule_hint,num);
        SpannableString spannable = new SpannableString(hint);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#face2e"));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan (1.5f);
        spannable.setSpan(colorSpan, 5, 5+num.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannable.setSpan(sizeSpan, 5, 5+num.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ((TextView)taskAwardView.findViewById(R.id.gold_num)).setText(spannable);
        ((ImageView)taskAwardView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissTaskAwardDialog();
            }
        });

        taskAwardDialog = new NoticeDialog(mContext);
        taskAwardDialog.setCanceledOnTouchOutside(false);
        taskAwardDialog.setCancelable(false);
        taskAwardDialog.showDialog(taskAwardView, 0, 0,1);
    }
    public void dismissTaskAwardDialog(){
        taskAwardDialog.dismiss();
    }




    /**
     * 投票提交成功弹框
     * */
    private View voteSucceedView;
    private NoticeDialog voteSucceedDialog;
    public void showVoteSucceedDialog(View.OnClickListener listener){
        voteSucceedView = (LinearLayout) inflater.inflate(
                R.layout.vote_submit_success_activity, null);
        ((Button)voteSucceedView.findViewById(R.id.return_list)).setOnClickListener(listener);

        voteSucceedDialog = new NoticeDialog(mContext);
        voteSucceedDialog.setCanceledOnTouchOutside(false);
        voteSucceedDialog.setCancelable(false);
        voteSucceedDialog.showDialog(voteSucceedView, 0, 0,1);
    }
    public void dismissVoteSucceedDialog(){
        voteSucceedDialog.dismiss();
    }





    /**
     * 引导会员弹框
     */
    private View guidanceVipView;
    private NoticeDialog guidanceVipDialog;
    public void showGuidanceVipDialog(View.OnClickListener listener){
        guidanceVipView = (RelativeLayout) inflater.inflate(
                R.layout.guidance_vip_pop, null);

        ((TextView)guidanceVipView.findViewById(R.id.open)).setOnClickListener(listener);
        ((TextView)guidanceVipView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guidanceVipDialog();
            }
        });

        guidanceVipDialog = new NoticeDialog(mContext);
        guidanceVipDialog.setCanceledOnTouchOutside(false);
        guidanceVipDialog.setCancelable(false);
        guidanceVipDialog.showDialog(guidanceVipView, 0, 0,1);
    }
    public void guidanceVipDialog(){
        guidanceVipDialog.dismiss();
    }



    /**
     * 会员每日打开弹框
     */
    private View openVipView;
    private NoticeDialog OpenVipDialog;
    public void showOpenVipDialog(){
        openVipView = (RelativeLayout) inflater.inflate(
                R.layout.open_vip_pop, null);

        ((RelativeLayout)openVipView.findViewById(R.id.layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, MyVipActivity.class));
                dismissOpenVipDialog();
            }
        });
        ((ImageView)openVipView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenVipDialog();
            }
        });

        OpenVipDialog = new NoticeDialog(mContext);
        OpenVipDialog.setCanceledOnTouchOutside(false);
        OpenVipDialog.setCancelable(false);
        OpenVipDialog.showDialog(openVipView, 0, 0,1);
    }
    public void dismissOpenVipDialog(){
        OpenVipDialog.dismiss();
    }

    /**
     * 普通用户每日
     */
    private View openCommonView;
    private NoticeDialog OpenCommonDialog;
    public void showOpenCommonDialog(View.OnClickListener listener){
        openCommonView = (RelativeLayout) inflater.inflate(
                R.layout.open_common_pop, null);

        ((Button)openCommonView.findViewById(R.id.button)).setOnClickListener(listener);
        ((ImageView)openCommonView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenCommonDialog();
            }
        });

        OpenCommonDialog = new NoticeDialog(mContext);
        OpenCommonDialog.setCanceledOnTouchOutside(false);
        OpenCommonDialog.setCancelable(false);
        OpenCommonDialog.showDialog(openCommonView, 0, 0,1);
    }
    public void dismissOpenCommonDialog(){
        OpenCommonDialog.dismiss();
    }


    /**
     * 已领取金币跳转VIP页面弹框
     */
    private View openGetGoldView;
    private NoticeDialog openGetGoldDialog;
    public void showopenGetGoldDialog(){
        openGetGoldView = (RelativeLayout) inflater.inflate(
                R.layout.open_common_gold_pop, null);

        ((Button)openGetGoldView.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MyVipActivity.class));
                dismissOpenGetGoldDialog();
            }
        });
        ((ImageView)openGetGoldView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenGetGoldDialog();
            }
        });

        openGetGoldDialog = new NoticeDialog(mContext);
        openGetGoldDialog.setCanceledOnTouchOutside(false);
        openGetGoldDialog.setCancelable(false);
        openGetGoldDialog.showDialog(openGetGoldView, 0, 0,1);
    }
    public void dismissOpenGetGoldDialog(){
        openGetGoldDialog.dismiss();
    }


    /**
     * 会员每日红包
     */
    private View vipRedPacketView;
    private NoticeDialog VipRedPacketDialog;
    public void showVipRedPacketDialog(double num) {
        vipRedPacketView = (RelativeLayout) inflater.inflate(
                R.layout.vip_red_packet_pop, null);
        ((TextView)vipRedPacketView.findViewById(R.id.money_num)).setText("" + num);
        String hint = String.format(mContext.getString(R.string.vip_packet_money_hint),num+"");
        ((TextView)vipRedPacketView.findViewById(R.id.hint_num)).setText(hint);
        ((TextView)vipRedPacketView.findViewById(R.id.get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, WithdrawActivity.class));
                dismissVipRedPacketDialog();
            }
        });
        ((ImageView)vipRedPacketView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissVipRedPacketDialog();
            }
        });

        VipRedPacketDialog = new NoticeDialog(mContext);
        VipRedPacketDialog.setCanceledOnTouchOutside(false);
        VipRedPacketDialog.setCancelable(false);
        VipRedPacketDialog.showDialog(vipRedPacketView, 0, 0,1);
    }
    public void dismissVipRedPacketDialog(){
        VipRedPacketDialog.dismiss();
    }

    /**
     * 活动规则通用弹框
     */
    private View ruleHintView;
    private NoticeDialog ruleHintDialog;
    ArrayAdapter<String> ruleHintAdapter;
    public void showVipRuleHintDialog(Context context,String title, String[] data) {
        ruleHintView = (RelativeLayout) inflater.inflate(
                R.layout.vip_rule_hint_pop, null);

        ((TextView)ruleHintView.findViewById(R.id.title)).setText(title);
        ListView listView = ruleHintView.findViewById(R.id.listView);
        ruleHintAdapter = new ArrayAdapter(context,R.layout.textview_item,data);
        listView.setAdapter(ruleHintAdapter);

        ((Button)ruleHintView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissVipRuleHintDialog();
            }
        });

        ruleHintDialog = new NoticeDialog(mContext);
        ruleHintDialog.setCanceledOnTouchOutside(false);
        ruleHintDialog.setCancelable(false);
        ruleHintDialog.showDialog(ruleHintView, 0, 0,1);
    }
    public void dismissVipRuleHintDialog(){
        ruleHintDialog.dismiss();
    }



    /**
     * 签到成功弹窗
     */
    public RelativeLayout SignView;
    public NoticeDialog SignDialog;
    public TextView tvPoint;
    public void showSignDialog(String content) {
        if (SignView==null) {
            SignView = (RelativeLayout) inflater.inflate(R.layout.sign_success_pop, null);
            SignDialog = new NoticeDialog(mContext);
            tvPoint=SignView.findViewById(R.id.tv);
        }
        tvPoint.setText(content);
        SignDialog.showDialog(SignView, 0, 0);
    }
    public void dismissSignDialog(){
        SignDialog.dismiss();
    }

    /**
     * 提现弹框
     */

    private PopupWindow window;
    private  ImageView img;
    private  TextView tv;
    private Button bt ;
    private  View popupView ;

    public void showWithdrapDialog(Activity mActivity, int type, View.OnClickListener listener) {

        if (window == null){
            popupView = inflater.inflate(R.layout.withdraw_pop, null);
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            img = popupView.findViewById(R.id.img);
            tv = popupView.findViewById(R.id.tv);
            bt = popupView.findViewById(R.id.bt);
        }
        switch (type){
            case 1:
                img.setImageResource(R.mipmap.icon_alipay_3);
                tv.setText(mContext.getString(R.string.unbind_alipay));
                bt.setText(mContext.getString(R.string.bind_now));
                break;
            case 2:
                img.setImageResource(R.mipmap.icon_wechat_3);
                tv.setText(mContext.getString(R.string.unbind_wechat));
                bt.setText(mContext.getString(R.string.bind_now));
                break;
            case 3:
                img.setImageResource(R.mipmap.icon_balance_3);
                tv.setText(mContext.getString(R.string.no_money));
                bt.setText(mContext.getString(R.string.get_money_now));
                break;
            case 4:
                img.setImageResource(R.mipmap.icon_signin);
                tv.setText(String.format(mContext.getString(R.string.withdraw_limit),"5","1"));
                bt.setText(mContext.getString(R.string.go_to_signin));
                break;
            case 5:
                img.setImageResource(R.mipmap.icon_signin);
                tv.setText(String.format(mContext.getString(R.string.withdraw_limit),"10","5"));
                bt.setText(mContext.getString(R.string.go_to_signin));
                break;
            case 6:
                img.setImageResource(R.mipmap.icon_signin);
                tv.setText(String.format(mContext.getString(R.string.withdraw_limit),"15","8"));
                bt.setText(mContext.getString(R.string.go_to_signin));
                break;
        }
        bt.setOnClickListener(listener);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }

    public void dismissWithdrapDialog(){
        window.dismiss();
    }

    /**
     * 中奖弹窗
     */
    private View lotteryView;
    private NoticeDialog lotteryDialog;
    private ImageView gif;
    private TextView gifName,tvGoto;
    public void showLotteryDialog(String url,String name,int type){
        if (lotteryView==null) {
            lotteryView = (RelativeLayout) inflater.inflate(
                    R.layout.winning_pop, null);
            gif= lotteryView.findViewById(R.id.img);
            gifName=lotteryView.findViewById(R.id.tv);
            tvGoto=lotteryView.findViewById(R.id.tv2);
            lotteryDialog = new NoticeDialog(mContext);
        }
        Glide.with(mContext).load(url).into(gif);
        String str;
        if (type==1) {
            str = String.format(mContext.getString(R.string.putin_your), name);
            SpannableStringBuilder style=new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),str.length()-13,str.length()-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            gifName.setText(style);
        }else if (type==2){
            str = String.format(mContext.getString(R.string.putin_your_money), name);
            SpannableStringBuilder style=new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),str.length()-8,str.length()-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            gifName.setText(style);
        }else if (type==3){
            str = String.format(mContext.getString(R.string.putin_your_point), name);
            SpannableStringBuilder style=new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),str.length()-8,str.length()-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            gifName.setText(style);
        }

        tvGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissLotteryDialog();
                if (type==1){
                    mContext.startActivity(new Intent(mContext, BoxListActivity.class));
                }else if (type==2){
                    mContext.startActivity(new Intent(mContext, WalletActivity.class));
                }else if (type==3){
                    mContext.startActivity(new Intent(mContext, PointActivity.class));
                }
            }
        });
        ((ImageView)lotteryView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissLotteryDialog();
            }
        });
        lotteryDialog.setCanceledOnTouchOutside(false);
        lotteryDialog.setCancelable(false);
        lotteryDialog.showDialog(lotteryView, 0, 0,1);
    }

    public void dismissLotteryDialog(){
        lotteryDialog.dismiss();
    }
    /**
     * 七天签到弹窗
     */
    private View sevenView;
    private NoticeDialog sevenDialog;
    private ImageView sevengif;
    private TextView sevengifName,sevengifName2,seventvGoto;
    public void showSevenDialog(String url, String name, View.OnClickListener listener){
        if (sevenView==null) {
            sevenView = (RelativeLayout) inflater.inflate(
                    R.layout.sevenday_pop, null);
            sevengif= sevenView.findViewById(R.id.img);
            sevengifName=sevenView.findViewById(R.id.tv);
            sevengifName2=sevenView.findViewById(R.id.tv3);
            seventvGoto=sevenView.findViewById(R.id.tv2);
            sevenDialog = new NoticeDialog(mContext);
        }
//        if (!name.contains(mContext.getString(R.string.point))){
//            Glide.with(mContext).load(url).into(sevengif);
//        }
        String str= String.format(mContext.getString(R.string.sevenday_get),name);
        sevengifName.setText(str);
        String str2= String.format(mContext.getString(R.string.sevenday_get2),name);
        sevengifName2.setText(str2);
        seventvGoto.setOnClickListener(listener);
        ((ImageView)sevenView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissSevenDialog();
            }
        });
        sevenDialog.setCanceledOnTouchOutside(false);
        sevenDialog.setCancelable(false);
        sevenDialog.showDialog(sevenView, 0, 0,1);
    }

    public void dismissSevenDialog(){
        sevenDialog.dismiss();
    }
    /**
     * 补签弹窗
     */
    private View addsignView;
    private NoticeDialog addsignDialog;
    private TextView signgifName,signtvGoto;
    public void showAddSignDialog(int num, View.OnClickListener listener){
        if (addsignView==null) {
            addsignView = (RelativeLayout) inflater.inflate(
                    R.layout.addsign_pop, null);
            signgifName=addsignView.findViewById(R.id.tv);
            signtvGoto=addsignView.findViewById(R.id.tv2);
            addsignDialog = new NoticeDialog(mContext);
        }
        if (num>0){
            signtvGoto.setText(mContext.getString(R.string.sure_addsign));
        }else {
            signtvGoto.setText(mContext.getString(R.string.get_addsign));
        }
        signgifName.setText(String.format(mContext.getString(R.string.addsign_prompt),num+""));
        signtvGoto.setOnClickListener(listener);
        ((ImageView)addsignView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAddSignDialog();
            }
        });
        addsignDialog.showDialog(addsignView, 0, 0,1);
    }

    public void dismissAddSignDialog(){
        addsignDialog.dismiss();
    }
    /**
     * 绑定微信
     */
    private View bindWechatView;
    private NoticeDialog bindWechatDialog;
    private TextView bindnow;
    public void showbindWechatDialog(View.OnClickListener listener){
        if (bindWechatView==null) {
            bindWechatView = (RelativeLayout) inflater.inflate(
                    R.layout.wechatbind_pop, null);
            bindnow=bindWechatView.findViewById(R.id.tv2);
            bindWechatDialog = new NoticeDialog(mContext);
        }
        bindnow.setOnClickListener(listener);
        ((ImageView)bindWechatView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissbindWechatDialog();
            }
        });
        bindWechatDialog.showDialog(bindWechatView, 0, 0,1);
    }

    public void dismissbindWechatDialog(){
        bindWechatDialog.dismiss();
    }
    /**
     * 机会用完弹框
     */

    private PopupWindow changeWindow;
    private  View changePopupView ;

    public void showChangeDialog(Activity mActivity) {

        if (window == null){
            changePopupView = inflater.inflate(R.layout.no_times_pop, null);
            changeWindow = new PopupWindow(changePopupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        changeWindow.setBackgroundDrawable(new BitmapDrawable());
        changeWindow.setFocusable(true);
        changeWindow.setOutsideTouchable(true);
        changeWindow.showAtLocation(((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        changeWindow.update();
        changeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }

    public void dismissChangeDialog(){
        changeWindow.dismiss();
    }


    /**
     * 答题红包弹框
     * @param type
     * @param num
     */
    public void showRedpacketDialog(int type,String num,String vip_num,Uri uri) {
        redPacketView = (RelativeLayout) inflater.inflate(
                R.layout.redpacket_pop, null);
        LinearLayout ln = redPacketView.findViewById(R.id.ln_redpackter);
        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type==1){
                    showOpenRedpacketDialog(num,uri);
                }else {
                    showOpenPointpacketDialog(num,vip_num,uri);
                }
                dismissRedpacketDialog();
            }
        });
        ((ImageView)redPacketView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissRedpacketDialog();
            }
        });
        redPacketDialog = new NoticeDialog(mContext);
        redPacketDialog.setCanceledOnTouchOutside(false);
        redPacketDialog.setCancelable(false);
        redPacketDialog.showDialog(redPacketView, 0, 0);
    }
    public void dismissRedpacketDialog(){
        redPacketDialog.dismiss();
    }
    public void showOpenRedpacketDialog(String num,Uri uri) {
        openRedPacketView = (RelativeLayout) inflater.inflate(
                R.layout.open_redpacket_pop, null);
        ((TextView)openRedPacketView.findViewById(R.id.tv_money)).setText(num);
        ((ImageView)openRedPacketView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenRedpacketDialog();
            }
        });
        ((TextView)openRedPacketView.findViewById(R.id.bt_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenRedpacketDialog();
                getShareFriend();
            }
        });
        openRedPacketDialog = new NoticeDialog(mContext);
        openRedPacketDialog.setCanceledOnTouchOutside(false);
        openRedPacketDialog.setCancelable(false);
        openRedPacketDialog.showDialog(openRedPacketView, 0, 0);
        openRedPacketDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (uri!=null){
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mActivity.startActivity(intent);
                }
            }
        });
    }
    public void dismissOpenRedpacketDialog(){
        openRedPacketDialog.dismiss();
    }
    //获取分享信息
    public void getShareFriend(){
        String mode ="";
        mode="share_to_friend";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                new ShareDialog(mActivity).showWithdrapDialog(mActivity,3,var1.getShare_title(),var1.getShare_info(),var1.getShare_pic(), Comment.url+"invitation_registration?nickname="+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname)+"&username="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+"&imageurl="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl));
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
    public void showOpenPointpacketDialog(String num,String vip_num,Uri uri) {
        openPointpacketView = (RelativeLayout) inflater.inflate(
                R.layout.open_redpacket_pop2, null);

        ((TextView) openPointpacketView.findViewById(R.id.tv_point)).setText(num);
        if(!vip_num.equals("0")){

            ((TextView) openPointpacketView.findViewById(R.id.vip_num)).setText(" +" + Integer.valueOf(vip_num));
            ((TextView) openPointpacketView.findViewById(R.id.tv_money)).setText(Integer.valueOf(num)+Integer.valueOf(vip_num)+"");
        }else{
            ((TextView) openPointpacketView.findViewById(R.id.tv_money)).setText(num);
        }

        ((ImageView) openPointpacketView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenPointpacketDialog();
            }
        });
        ((TextView)openPointpacketView.findViewById(R.id.bt_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenPointpacketDialog();
                mContext.startActivity(new Intent(mContext,PointActivity.class));
            }
        });
        openPointpacketDialog = new NoticeDialog(mContext);
        openPointpacketDialog.setCanceledOnTouchOutside(false);
        openPointpacketDialog.setCancelable(false);
        openPointpacketDialog.showDialog(openPointpacketView, 0, 0);
        openPointpacketDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (uri!=null){
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    public void dismissOpenPointpacketDialog() {
        openPointpacketDialog.dismiss();
    }
    public void showLateDialog() {
        lateView = (RelativeLayout) inflater.inflate(
                R.layout.tolate_pop, null);
        ((ImageView) lateView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissLateDialog();
            }
        });
        lateDialog = new NoticeDialog(mContext);
        lateDialog.showDialog(lateView, 0, 0,1);
    }

    public void dismissLateDialog() {
        lateDialog.dismiss();
    }


    /**
     * 审核不通过
     */
    private View failView;
    private NoticeDialog failDialog;
    private TextView tvcontent,tvreturn,tvchange;
    public void showBackDialog(Ads ads){
        if (failView==null) {
            failView = (RelativeLayout) inflater.inflate(
                    R.layout.back_pop, null);
            tvcontent= failView.findViewById(R.id.tv_content);
            tvreturn=failView.findViewById(R.id.tv_return);
            tvchange=failView.findViewById(R.id.change_now);
            failDialog = new NoticeDialog(mContext);
        }
        tvcontent.setText(ads.getCheck_info());
        tvchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ads.getAd_type().equals("picture")) {
                    Intent intent = new Intent(mContext, NewGraphicActivity.class);
                    intent.putExtra("id",ads.getId());
                    intent.putExtra("ads",ads);
                    mContext.startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, NewVideoActivity.class);
                    intent.putExtra("id",ads.getId());
                    intent.putExtra("ads",ads);
                    mContext.startActivity(intent);
                }
                failDialog.dismiss();
            }
        });
        tvreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failDialog.dismiss();
            }
        });
        failDialog.setCanceledOnTouchOutside(false);
        failDialog.setCancelable(false);
        failDialog.showDialog(failView, 0, 0,1);
    }

    public void dismissBackDialog(){
        failDialog.dismiss();
    }


    /**
     * 新手红包
     */
    private View newPacketView;
    private NoticeDialog newPacketDialog;
    public void showNewPacketDialog() {
        newPacketView = (RelativeLayout) inflater.inflate(
                R.layout.newspacket_pop, null);
        ((TextView)newPacketView.findViewById(R.id.get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_login).equals("on")) {
                    getNewRed();
                }else {
                    Intent intent=new Intent(mContext,LoginActivity.class);
                    intent.putExtra("click","on");
                    mContext.startActivity(intent);
                    dismissNewPacketDialog();
                }
            }
        });
        ((ImageView)newPacketView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNewPacketDialog();
            }
        });

        newPacketDialog = new NoticeDialog(mContext);
        newPacketDialog.setCanceledOnTouchOutside(false);
        newPacketDialog.setCancelable(false);
        newPacketDialog.showDialog(newPacketView, 0, 0,1);
    }

    public void getNewRed() {
        YSBSdk.getService(OAuthService.class).new_red_package(new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                dismissNewPacketDialog();
                showOpenNewDialog(var1.getRed_package_number());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,var1.getChange());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_new_one_status,"off");
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    public void dismissNewPacketDialog(){
        if (newPacketDialog!=null)
        newPacketDialog.dismiss();
    }
    /**
     * 新手红包
     */
    private View openNewView;
    private NoticeDialog openNewDialog;
    public void showOpenNewDialog(String money) {
        openNewView = (RelativeLayout) inflater.inflate(
                R.layout.opennew_pop, null);
        ((TextView)openNewView.findViewById(R.id.money)).setText(money+mContext.getString(R.string.yuan));
        ((TextView)openNewView.findViewById(R.id.get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                double num =Double.valueOf(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_change));
//                num=num+Double.valueOf(money);
//                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_change,num+"");
                mContext.startActivity(new Intent(mContext, WalletActivity.class));
                dismissOpenNewDialog();
            }
        });
        ((ImageView)openNewView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenNewDialog();
            }
        });
        openNewDialog = new NoticeDialog(mContext);
        openNewDialog.setCanceledOnTouchOutside(false);
        openNewDialog.setCancelable(false);
        openNewDialog.showDialog(openNewView, 0, 0,1);
    }
    public void dismissOpenNewDialog(){
        openNewDialog.dismiss();
    }

    /**
     * 定位权限
     */
    private View locateView;
    private NoticeDialog locateDialog;
    public void showlocateDialog(View.OnClickListener listener) {
        locateView = (RelativeLayout) inflater.inflate(
                R.layout.locate_pop, null);
//        ((TextView)locateView.findViewById(R.id.money)).setText(money+mContext.getString(R.string.yuan));
        ((TextView)locateView.findViewById(R.id.open)).setOnClickListener(listener);
        ((TextView)locateView.findViewById(R.id.pass)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismisslocateDialog();
            }
        });
        locateDialog = new NoticeDialog(mContext);
        locateDialog.setCanceledOnTouchOutside(false);
        locateDialog.setCancelable(false);
        locateDialog.showDialog(locateView, 0, 0,1);
    }
    public void dismisslocateDialog(){
        locateDialog.dismiss();
    }


    /**
     * 获取活动奖励
     */
    private View openActivityView;
    private NoticeDialog openActivityDialog;
    public void showOpenActivityDialog(String money) {
        openActivityView = (RelativeLayout) inflater.inflate(R.layout.activity_pop
                , null);
        ((TextView)openActivityView.findViewById(R.id.money)).setText("￥"+money);
        ((TextView)openActivityView.findViewById(R.id.get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenActivityDialog();
            }
        });
        ((ImageView)openActivityView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissOpenActivityDialog();
            }
        });
        openActivityDialog = new NoticeDialog(mContext);
        openActivityDialog.setCanceledOnTouchOutside(false);
        openActivityDialog.setCancelable(false);
        openActivityDialog.showDialog(openActivityView, 0, 0,1);
    }
    public void dismissOpenActivityDialog(){
        openActivityDialog.dismiss();
    }

    /*
      * 提交活动
     */
    private View commitActivityView;
    private NoticeDialog commitActivitDialog;
    public void showCommitActivitDialog() {
        commitActivityView = (RelativeLayout) inflater.inflate(R.layout.activity_commit_pop
                , null);
        ((TextView)commitActivityView.findViewById(R.id.get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissCommitActivitDialog();
            }
        });
        ((ImageView)commitActivityView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissCommitActivitDialog();
            }
        });
        commitActivitDialog = new NoticeDialog(mContext);
        commitActivitDialog.setCanceledOnTouchOutside(false);
        commitActivitDialog.setCancelable(false);
        commitActivitDialog.showDialog(commitActivityView, 0, 0,1);
    }
    public void dismissCommitActivitDialog(){
        commitActivitDialog.dismiss();
    }
}

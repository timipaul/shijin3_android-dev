package com.shijinsz.shijin.ui.activity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.activity.adapter.ActivityRuleActivity;
import com.shijinsz.shijin.ui.activity.adapter.RankAdapter;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/10/23.
 */

public class SignAfterActivity extends BaseActivity {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.cutdown)
    TextView cutdown;
    @BindView(R.id.ln1)
    LinearLayout ln1;
    @BindView(R.id.all_money)
    TextView allMoney;
    @BindView(R.id.join_num)
    TextView joinNum;
    @BindView(R.id.finish0)
    TextView finish0;
    @BindView(R.id.finish1)
    TextView finish1;
    @BindView(R.id.finish2)
    TextView finish2;
    @BindView(R.id.finish3)
    TextView finish3;
    @BindView(R.id.finish4)
    TextView finish4;
    @BindView(R.id.finish5)
    TextView finish5;
    @BindView(R.id.finish6)
    TextView finish6;
    @BindView(R.id.finish7)
    TextView finish7;
    @BindView(R.id.finish8)
    TextView finish8;
    @BindView(R.id.finish9)
    TextView finish9;
    @BindView(R.id.finish10)
    TextView finish10;
    @BindView(R.id.sleep)
    ImageView sleep;
    @BindView(R.id.team4)
    CircleImageView team4;
    @BindView(R.id.team3)
    CircleImageView team3;
    @BindView(R.id.team2)
    CircleImageView team2;
    @BindView(R.id.team1)
    CircleImageView team1;
    @BindView(R.id.rl_team)
    RelativeLayout rlTeam;
    @BindView(R.id.invite_friend)
    ImageView inviteFriend;
    @BindView(R.id.rule)
    ImageView rule;
    @BindView(R.id.tom_num)
    TextView tomNum;
    @BindView(R.id.tom_invite)
    TextView tomInvite;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.my_money)
    TextView myMoney;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bt_right)
    ImageView btRight;
    @BindView(R.id.top)
    RelativeLayout top;

    @Override
    public int bindLayout() {
        return R.layout.activity_signafter_activity;
    }

    private RankAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    public void initView(View view) {
        SpannableStringBuilder style=new SpannableStringBuilder(getString(R.string.ask_num0));
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish0.setText(style);
        SpannableStringBuilder style1=new SpannableStringBuilder(getString(R.string.ask_num1));
        style1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish1.setText(style1);
        SpannableStringBuilder style2=new SpannableStringBuilder(getString(R.string.ask_num2));
        style2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish2.setText(style2);
        SpannableStringBuilder style3=new SpannableStringBuilder(getString(R.string.ask_num3));
        style3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish3.setText(style3);
        SpannableStringBuilder style4=new SpannableStringBuilder(getString(R.string.ask_num4));
        style4.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish4.setText(style4);
        SpannableStringBuilder style5=new SpannableStringBuilder(getString(R.string.ask_num5));
        style5.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish5.setText(style5);
        SpannableStringBuilder style6=new SpannableStringBuilder(getString(R.string.ask_num6));
        style6.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish6.setText(style6);
        SpannableStringBuilder style7=new SpannableStringBuilder(getString(R.string.ask_num7));
        style7.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish7.setText(style7);
        SpannableStringBuilder style8=new SpannableStringBuilder(getString(R.string.ask_num8));
        style8.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish8.setText(style8);
        SpannableStringBuilder style9=new SpannableStringBuilder(getString(R.string.ask_num9));
        style9.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish9.setText(style9);
        SpannableStringBuilder style10=new SpannableStringBuilder(getString(R.string.ask_num10));
        style10.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),4,6,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style10.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ffeb7b)),style10.length()-4,style10.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        finish10.setText(style10);
        GlideApp.with(mContext).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).into(team1);
        SpannableStringBuilder tom = new SpannableStringBuilder(String.format(getString(R.string.tomorrow_num),"1000人","¥10000"));
        tom.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)),7,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tom.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)),tom.length()-6,tom.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tomNum.setText(tom);

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new RankAdapter(R.layout.paihang_item, list);
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.finish0, R.id.finish1, R.id.finish2, R.id.finish3, R.id.finish4, R.id.finish5, R.id.finish6, R.id.finish7, R.id.finish8, R.id.finish9, R.id.finish10,R.id.rl_team, R.id.invite_friend, R.id.rule, R.id.tom_invite, R.id.iv_back, R.id.bt_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish0:
                finish0.setVisibility(View.GONE);
                finish1.setVisibility(View.VISIBLE);
                break;
            case R.id.finish1:
                finish1.setVisibility(View.GONE);
                finish2.setVisibility(View.VISIBLE);
                break;
            case R.id.finish2:
                finish2.setVisibility(View.GONE);
                finish3.setVisibility(View.VISIBLE);
                break;
            case R.id.finish3:
                finish3.setVisibility(View.GONE);
                finish4.setVisibility(View.VISIBLE);
                break;
            case R.id.finish4:
                finish4.setVisibility(View.GONE);
                finish5.setVisibility(View.VISIBLE);
                break;
            case R.id.finish5:
                finish5.setVisibility(View.GONE);
                finish6.setVisibility(View.VISIBLE);
                break;
            case R.id.finish6:
                finish6.setVisibility(View.GONE);
                finish7.setVisibility(View.VISIBLE);
                break;
            case R.id.finish7:
                finish7.setVisibility(View.GONE);
                finish8.setVisibility(View.VISIBLE);
                break;
            case R.id.finish8:
                finish8.setVisibility(View.GONE);
                finish9.setVisibility(View.VISIBLE);
                break;
            case R.id.finish9:
                finish9.setVisibility(View.GONE);
                finish10.setVisibility(View.VISIBLE);
                break;
            case R.id.finish10:
                finish10.setVisibility(View.GONE);
                sleep.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_team:
                startActivity(TeamStatusActivity.class);
                break;
            case R.id.invite_friend:
                new DialogUtils(mActivity).showOpenActivityDialog("3.21");
                break;
            case R.id.rule:
                startActivity(ActivityRuleActivity.class);
                break;
            case R.id.tom_invite:
                new DialogUtils(mActivity).showCommitActivitDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_right:
                startActivity(ActivityRecordActivity.class);
                break;
        }
    }


}

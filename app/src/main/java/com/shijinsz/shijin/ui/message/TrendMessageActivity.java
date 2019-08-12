package com.shijinsz.shijin.ui.message;

import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/8/7.
 */

public class TrendMessageActivity extends BaseActivity {
    @BindView(R.id.tv_comment_noread)
    TextView tvCommentNoread;
    @BindView(R.id.tv_like_noread)
    TextView tvLikeNoread;
    @BindView(R.id.tv_follow_noread)
    TextView tvFollowNoread;
    @BindView(R.id.tv_notification_noread)
    TextView tvNotificationNoread;

    @Override
    public int bindLayout() {
        return R.layout.trend_message_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        noread();
    }
    private String comment,like,follow,notification;
    private void noread() {
        comment = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ad_comment_number);
        like = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ad_like_number);
        follow = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_user_like_number);
        notification = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_notification_number);
        if (comment.equals("0")){
            tvCommentNoread.setVisibility(View.GONE);
        }else {
            tvCommentNoread.setVisibility(View.VISIBLE);
            if (Integer.parseInt(comment)>99){
                tvCommentNoread.setText("99");
            }else {
                tvCommentNoread.setText(comment);
            }
        }
        if (like.equals("0")){
            tvLikeNoread.setVisibility(View.GONE);
        }else {
            tvLikeNoread.setVisibility(View.VISIBLE);
            if (Integer.parseInt(like)>99){
                tvLikeNoread.setText("99");
            }else {
                tvLikeNoread.setText(like);
            }
        }
        if (follow.equals("0")){
            tvFollowNoread.setVisibility(View.GONE);
        }else {
            tvFollowNoread.setVisibility(View.VISIBLE);
            if (Integer.parseInt(follow)>99){
                tvFollowNoread.setText("99");
            }else {
                tvFollowNoread.setText(follow);
            }
        }
        if (notification.equals("0")){
            tvNotificationNoread.setVisibility(View.GONE);
        }else {
            tvNotificationNoread.setVisibility(View.VISIBLE);
            if (Integer.parseInt(notification)>99){
                tvNotificationNoread.setText("99");
            }else {
                tvNotificationNoread.setText(notification);
            }
        }
    }

    @Override
    public void initView(View view) {
        setTitle(getString(R.string.dynamic_message));
        showTitleBackButton();

    }


    @OnClick({R.id.ln_comment_message, R.id.ln_like_message, R.id.ln_follow_message, R.id.ln_notification_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_comment_message:
                startActivity(CommentActivity.class);
                break;
            case R.id.ln_like_message:
                startActivity(LikeMessageActivity.class);
                break;
            case R.id.ln_follow_message:
                startActivity(FollowMessageActivity.class);
                break;
            case R.id.ln_notification_message:
                break;
        }
    }
}

package com.shijinsz.shijin.ui.ad;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.hclibrary.view.MyJzvdStd;
import com.hongchuang.hclibrary.view.ProgressWebView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/20.
 */

public class PreviewActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.webview)
    ProgressWebView webview;
    @BindView(R.id.player)
    MyJzvdStd player;
    private String type = "";
    private String path="";
    @Override
    public int bindLayout() {
        return R.layout.preview_activity;
    }

    @Override
    public void initView(View view) {
        setTitle(getString(R.string.preview));
        showTitleBackButton();
        showTitleRightText(getString(R.string.publish), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releases();
            }
        });
        webview.setLayerType(View.LAYER_TYPE_NONE, null);
        type = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_put_type);
        if (type.equals("video")){
            path=ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_put_content);
            player.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            player.setUp(path
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "" );
            GlideApp.with(mContext).load(path + "?x-oss-process=video/snapshot,t_0,f_jpg").into(player.thumbImageView);
        }else {
            webview.setVisibility(View.VISIBLE);
            player.setVisibility(View.GONE);
            webview.loadDataWithBaseURL(null, ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_put_content), "text/html", "UTF-8", null);
        }
        tvTitle.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_put_title));
        GlideApp.with(mContext).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).into(imgHead);
        tvUsername.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname));
    }
    @Override
    protected void onPause() {
        super.onPause();
        player.releaseAllVideos();
    }
    private void releases() {
        Map map= new HashMap<>();
        YSBSdk.getService(OAuthService.class).releases(getIntent().getStringExtra("id"), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                Intent intent = new Intent(mContext, PayActivity.class);
                intent.putExtra("money", getIntent().getStringExtra("money"));
                intent.putExtra("id", var1.getNew_ad_id());
                intent.putExtra("num",getIntent().getStringExtra("num"));
                startActivity(intent);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                ToastUtil.showToast(message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast("发布失败，请重新上传");
                mStateView.showContent();
            }
        });
    }
}

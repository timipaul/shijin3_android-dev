package com.shijinsz.shijin.ui.ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DevicesUtil;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.hclibrary.view.CircleTextProgressbar;
import com.hongchuang.hclibrary.view.MyJzvdStd;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.UrlConstants;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.AdBean;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.AnswersBean;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.FatherCommentBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.adapter.FatherAdapter;
import com.shijinsz.shijin.ui.ad.adapter.RecommendAdapter;
import com.shijinsz.shijin.ui.mine.UserDetailActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.LoginUtil;
import com.shijinsz.shijin.utils.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import retrofit.callback.YRequestCallback;

import static android.widget.PopupWindow.INPUT_METHOD_NEEDED;

/**
 * Created by yrdan on 2018/8/24.
 * 内容答题页
 */

public class VideoDetailActivity extends BaseActivity {
    @BindView(R.id.progress)
    CircleTextProgressbar progress;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.img_givealike)
    ImageView imgGivealike;
    @BindView(R.id.tv_givealike)
    TextView tvGivealike;
    @BindView(R.id.ln_givealike)
    LinearLayout lnGivealike;
    @BindView(R.id.img_comments)
    ImageView imgComments;
    @BindView(R.id.tv_comments)
    TextView tvComments;
    @BindView(R.id.ln_comments)
    LinearLayout lnComments;
    @BindView(R.id.img_forwarding)
    ImageView imgForwarding;
    @BindView(R.id.tv_forwarding)
    TextView tvForwarding;
    @BindView(R.id.ln_forwarding)
    LinearLayout lnForwarding;
    @BindView(R.id.img_collection)
    ImageView imgCollection;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.ln_collection)
    LinearLayout lnCollection;
    @BindView(R.id.tv_drap)
    TextView tvDrap;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.ln_center)
    LinearLayout lnCenter;
    @BindView(R.id.img_head_vip)
    ImageView imgHeadVip;
    @BindView(R.id.answer_layout)
    RelativeLayout mAnswerLayout;

    private TextView title;
    private LinearLayout lnHead;
    private List<FatherCommentBean> list = new ArrayList<>();
    private List<Ads> recommendList = new ArrayList<>();
    private FatherAdapter adapter;
    private RecommendAdapter recommendAdapter;
    private View view;
    private View more;
    private NoticeDialog mailBoxDialog;
    private View emptyView;
    private long videotime = 0;

    @Override
    public int bindLayout() {
        return R.layout.video_detail_activity;
    }

    ShareDialog shareDialog;

    @Override
    public void initView(View view) {
        showTitleBackButton();
        showTitleRightBackButton(R.mipmap.icon_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                getShare();
            }
        });
        if (getIntent().getStringExtra("purpose")!=null&&getIntent().getStringExtra("purpose").equals("purpose")){
            isPurpose=true;
        }else {
            isPurpose=false;
        }

        shareDialog = new ShareDialog(mContext);
        shareDialog.setOnShareListen(new ShareDialog.OnShareListen() {
            @Override
            public void success(String channel) {
                share(channel);
            }
        });
        id = getIntent().getStringExtra("id");
        initAdapter();
        initProgress();

    }

    private void share(String channel) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("channel", channel);
        YSBSdk.getService(OAuthService.class).shares(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                tvForwarding.setText(Integer.parseInt(tvForwarding.getText().toString()) + 1 + "");
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    public void urlClick() {
        YSBSdk.getService(OAuthService.class).url_click(id, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    private void initAdapter() {
        adapter = new FatherAdapter(R.layout.father_comment_item, list);
        adapter.setOnListen(new FatherAdapter.OnListen() {
            @Override
            public void call(int pos) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Intent intent = new Intent(mContext, FatherCommentActivity.class);
                intent.putExtra("num", comment_num);
                intent.putExtra("id", id);
                intent.putExtra("pos", "" + pos);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 111);
            }

            @Override
            public void like(int pos) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                if (list.get(pos).getIs_like().equals("on")) {
                    UncommentLike(pos);
                } else {
                    CommentLike(pos);
                }
            }

            @Override
            public void fathercall(int pos) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Intent intent = new Intent(mContext, FatherCommentActivity.class);
                intent.putExtra("num", comment_num);
                intent.putExtra("id", id);
                intent.putExtra("pos", "" + pos);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 111);
            }

            @Override
            public void fatharComment(int pos) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Intent intent = new Intent(mContext, FatherCommentActivity.class);
                intent.putExtra("num", comment_num);
                intent.putExtra("id", id);
                intent.putExtra("pos", "" + pos);
                intent.putExtra("type", "3");
                startActivityForResult(intent, 111);
            }

            @Override
            public void child(int pos, int index) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Intent intent = new Intent(mContext, FatherCommentActivity.class);
                intent.putExtra("num", comment_num);
                intent.putExtra("id", id);
                intent.putExtra("pos", "" + pos);
                intent.putExtra("index", "" + index);
                intent.putExtra("type", "4");
                startActivityForResult(intent, 111);
            }
        });
        emptyView = mInflater.inflate(R.layout.comment_empty_layout, null);
        view = mInflater.inflate(R.layout.ads_detail_heard, null);
        more = mInflater.inflate(R.layout.more_comment, null);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                Intent intent = new Intent(mContext, FatherCommentActivity.class);
                intent.putExtra("num", comment_num);
                intent.putExtra("id", id);
                startActivityForResult(intent, 111);
//                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter.addHeaderView(view);
        initHead();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                if (Build.VERSION.SDK_INT >= 23) {
                if (isViewCovered(lnHead)) {
                    lnCenter.setVisibility(View.VISIBLE);
                } else {
                    if (layoutManager.findFirstVisibleItemPosition() != 0) {
                        lnCenter.setVisibility(View.VISIBLE);
                    } else {
                        lnCenter.setVisibility(View.GONE);
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getDetail();
            }
        });
        getDetail();

    }

    //改变答题状态
    private void changeState(String status) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("answer_status", status);
        YSBSdk.getService(OAuthService.class).answer_status(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //获取分享信息
    public void getShare() {
        String mode = "";
        if (isPic) {
            mode = "picture_detail";
        } else {
            mode = "video_detail";
        }
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                shareDialog.showWithdrapDialog(mActivity, 1, title.getText().toString(), var1.getShare_info(), bean.getAd_title_pics().get(0), UrlConstants.ISHARE+"ishare/app/adsDetail?aid=" + id + "&time=12121511331");
            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    //获取广告详情
    private boolean isFirst = true;
    private boolean isPic = true;
    private int ad_type;
    private String num = "";
    private String comment_num = "";
    private String id = "";
    private String userid = "";
    private String content = "";
    private boolean isLike = false;
    private boolean isCollect = false;
    private AdBean bean;
    private boolean isFinish=false;
    private boolean isPurpose=false;
    private void getDetail() {

        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "detail");
        if (isPurpose)
        map.put("purpose", "answer");
        YSBSdk.getService(OAuthService.class).ads_detail(id, map, new YRequestCallback<BaseBean<AdBean>>() {
            @Override
            public void onSuccess(BaseBean<AdBean> var1) {
                bean = var1.getAd();

                userid = var1.getAd().getUser().getId();
                if (var1.getAd().getReward_mode().equals("change")) {
                    ad_type = 1;
                } else {
                    ad_type = 2;
                }

                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("on")){
                    imgVip2.setVisibility(View.VISIBLE);
                }else {
                    imgVip2.setVisibility(View.GONE);
                }


                if (var1.getAd().getUser().getIs_advertiser() == null || var1.getAd().getUser().getIs_advertiser().equals("on")){
                    imgVip.setVisibility(View.VISIBLE);
                    imgHeadVip.setVisibility(View.VISIBLE);
                }else{
                    imgVip.setVisibility(View.GONE);
                    imgHeadVip.setVisibility(View.GONE);
                }




                num = var1.getAd().getReward();
                comment_num = var1.getAd().getAd_comment_number();
                id = var1.getAd().getId();
                if (var1.getAd().getAd_type().equals("picture")) {

                    isPic = true;
                    playerStandard.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    webView.setFocusable(false);
                    webView.setLayerType(View.LAYER_TYPE_NONE, null);
                    webView.getSettings().setJavaScriptEnabled(true);//设置JS可用
//                    webView.addJavascriptInterface(new ShowPicRelation(mContext), NICK);//绑定JS和Java的联系类，以及使用到的昵称
                    int width=DevicesUtil.getWidth(mContext);
                    float density=DevicesUtil.getDensity(mContext);
                    width=(int)(width/density);
                    String htmlPart1 = "<!DOCTYPE HTML html>\n" +
                            "<head><meta charset=\"utf-8\"/>\n" +
                            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=no\"/>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<style> \n" +
                            "img{width:100% !important;height:auto !important}\n" +
//                            "body{\n" +
//                            "    width:"+width+"!important;\n" +
//                            "}\n" +
//                            "body{\n" +
//                            "    overflow-x:hidden !important;\n" +
//                            "}\n"+
                            " </style>";
                    String htmlPart2 = "</body></html>";
                    String body = var1.getAd().getAd_content().replace("&quot;", "\"");
//                    body = body.replace("&amp;", "&");
//                    body = body.replace("&lt;", "<");
//                    body = body.replace("&gt;", ">");
//                    body = body.replace("&nbsp;", " ");
                    String html = htmlPart1 + body + htmlPart2;
                    webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            mStateView.showContent();
                            try{
                                if (recyclerView!=null) {
                                    recyclerView.scrollToPosition(0);
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }

                    });
//                    webView.loadDataWithBaseURL(null,, "text/html", "UTF-8", null);
                } else {

                    String[] length = var1.getAd().getAd_content().split("length=");
                    if (length.length > 1) {
                        Log.e(TAG, "onSuccess: " + length[1]);
                        try {
                            videotime = Long.valueOf(length[1]);
                        } catch (Exception e) {
                            videotime = 15000;
                        }
                    }
                    mStateView.showContent();
                    isPic = false;
                    playerStandard.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    playerStandard.setUp(var1.getAd().getAd_content(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, var1.getAd().getAd_title());
                    GlideApp.with(mContext).load(var1.getAd().getAd_title_pics().get(0)).into(playerStandard.thumbImageView);
                    if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_AUTO).equals("on")) {
                        if (DevicesUtil.isWifi(mContext)) {
                            playerStandard.startVideo();
                        }
                    }
                }
                if (var1.getAd().getUrl() == null || var1.getAd().getUrl().isEmpty()) {
                    tvDrap.setVisibility(View.GONE);
                } else {
                    if (var1.getAd().getUrl().startsWith("http")) {
                        uri = Uri.parse(var1.getAd().getUrl());
                    } else {
                        uri = Uri.parse("http://" + var1.getAd().getUrl());
                    }
                    tvDrap.setVisibility(View.VISIBLE);
                }

                if(var1.getAd().getOptions() == null){
                    //没有答题 隐藏
                    mAnswerLayout.setVisibility(View.GONE);
                }

                tvOption1 = var1.getAd().getOptions().getOption1();
                tvOption2 = var1.getAd().getOptions().getOption2();
                tvOption3 = var1.getAd().getOptions().getOption3();
                tvOption4 = var1.getAd().getOptions().getOption4();
                tvtitle = var1.getAd().getQuestion();
                title.setText(var1.getAd().getAd_title());
                GlideApp.with(mContext).load(var1.getAd().getUser().getImgurl()).into(head);
                nickname.setText(var1.getAd().getUser().getNickname());
                GlideApp.with(mContext).load(var1.getAd().getUser().getImgurl()).into(imgHead);
                tvGivealike.setText(var1.getAd().getAd_like_number());
                tvComments.setText(var1.getAd().getAd_comment_number());
                tvForwarding.setText(var1.getAd().getAd_share_number());
                tvCollection.setText(var1.getAd().getAd_collection_number());
                if (var1.getAd().getIs_like().equals("on")) {
                    isLike = true;
                    imgGivealike.setImageResource(R.mipmap.icon_givealike_clickonthe);
                }
                if (var1.getAd().getIs_collection().equals("on")) {
                    isCollect = true;
                    imgCollection.setImageResource(R.mipmap.icon_collection_clickonthe);
                }

                tvTime.setText(TimeUtil.format(Long.valueOf(var1.getAd().getCreated_at()) * 1000, "MM-dd HH:mm"));
                if (var1.getAd().getIs_follow().equals("on")) {
                    isFollow = true;
                    btFollow.setText(mContext.getString(R.string.isfollow));
                    btFollow.setTextColor(mContext.getResources().getColor(R.color.text_999999));
                    //btFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_isfollow), null, null, null);
                    btFollow.setBackgroundResource(R.drawable.bg_follow);
                    btFollow.setPadding(10, 0, 0, 0);
                    tvFollow.setText(mContext.getString(R.string.isfollow));
                    tvFollow.setTextColor(mContext.getResources().getColor(R.color.text_999999));
                    //tvFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_isfollow), null, null, null);
                    tvFollow.setBackgroundResource(R.drawable.bg_follow);
                    tvFollow.setPadding(0, 0, 0, 0);
                } else {
                    isFollow = false;
                    btFollow.setText(mContext.getString(R.string.user_like));
                    btFollow.setTextColor(mContext.getResources().getColor(R.color.white));
                    //btFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_addfollow), null, null, null);
                    btFollow.setBackgroundResource(R.drawable.bg_addfollow);
                    //btFollow.setPadding(24, 0, 0, 0);
                    tvFollow.setText(mContext.getString(R.string.user_like));
                    tvFollow.setTextColor(mContext.getResources().getColor(R.color.white));
                    //tvFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_addfollow), null, null, null);
                    tvFollow.setBackgroundResource(R.drawable.bg_addfollow);
                    //tvFollow.setPadding(20, 0, 0, 0);
                }


                text.setTextSize(16);
                if (var1.getAd().getIs_answer().equals("off")) {
                    text.setText(getString(R.string.clickanswar));
                    progress.start();
                    text.setEnabled(false);
                } else if (var1.getAd().getIs_answer().equals("first_answer")) {
                    isFirst = false;
                    changeState("second_answer");
                    if (isPic) {
                        text.setTextSize(30);
                        progress.setTimeMillis(15000);
                        progress.start();
                        text.setEnabled(false);
                    } else {
//                        text.setTextSize(30);
                        if (videotime > 0) {
                            progress.setTimeMillis(videotime);
                        } else {
                            progress.setTimeMillis(15000);
                        }
                        progress.start();
                        text.setEnabled(true);
                    }
                } else if (var1.getAd().getIs_answer().equals("on")) {
                    progress.setProgress(0);

                    text.setText(getString(R.string.isanswar));
                    text.setEnabled(true);
                } else {
                    progress.setProgress(0);
                    text.setText(getString(R.string.nochance));
                    text.setEnabled(true);
                }


                getComment();
                getRecommend();
            }

            @Override
            public void onFailed(String var1, String message) {

                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                mStateView.showRetry();
            }

            @Override
            public void onException(Throwable var1) {

                mStateView.showContent();
                mStateView.showRetry();
            }
        });
    }

    private void getRecommend() {
        Map map = new HashMap();
        map.put("mode", "inside");
        map.put("size", "3");
        map.put("cursor", System.currentTimeMillis() / 1000 + 60000);
        map.put("ad_id", id);
        YSBSdk.getService(OAuthService.class).recommend_ads(map, new YRequestCallback<AdsBean>() {
            @Override
            public void onSuccess(AdsBean var1) {
                recommendList.addAll(var1.getAds());
                recommendAdapter.notifyDataSetChanged();
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

    private PowerfulRecyclerView recyclerView1;
    private CircleImageView head;
    private CircleImageView userhead;
    private TextView nickname;
    private TextView tv_comment;
    private TextView tvTime;
    private TextView btFollow;
    private MyJzvdStd playerStandard;
    private WebView webView;
    private ImageView imgVip;
    private ImageView imgVip2;
    private Uri uri;

    private void initHead() {
        recyclerView1 = view.findViewById(R.id.recyclerView);
        head = view.findViewById(R.id.img_head);
        userhead = view.findViewById(R.id.img_userhead);
        GlideApp.with(mContext).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).into(userhead);
        nickname = view.findViewById(R.id.tv_username);
        tv_comment = view.findViewById(R.id.tv_comment);
        tvTime = view.findViewById(R.id.tv_time);
        btFollow = view.findViewById(R.id.tv_follow);
        playerStandard = view.findViewById(R.id.player);
        webView = view.findViewById(R.id.webview);
        title = view.findViewById(R.id.tv_title);
        lnHead = view.findViewById(R.id.ln_head);
        imgVip = view.findViewById(R.id.img_vip_into);
        imgVip2 = view.findViewById(R.id.img_vip2);
        recommendAdapter = new RecommendAdapter(R.layout.recommend_item, recommendList);
        recyclerView1.setFocusable(false);
        recyclerView1.setAdapter(recommendAdapter);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("id", userid);
                startActivity(intent);
            }
        });
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("id", recommendList.get(position).getId());
                intent.putExtra("purpose", "purpose");
                startActivity(intent);
            }
        });
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                showEarlyDialog();
            }
        });
        btFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                if (isFollow) {
                    unfollow();
                } else {
                    follow();
                }
            }
        });
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                if (isFollow) {
                    unfollow();
                } else {
                    follow();
                }
            }
        });
//        playerStandard.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
//                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

    }

    private void initProgress() {
        progress.setProgressType(CircleTextProgressbar.ProgressType.COUNT_BACK);
        progress.setProgressLineWidth(10);
        progress.setOutLineColor(getResources().getColor(R.color.orange));
        progress.setProgressColor(getResources().getColor(R.color.color_f8b));
        progress.setTimeMillis(5000);
        progress.setCountdownProgressListener(1, new CircleTextProgressbar.OnCountdownProgressListener() {
            @Override
            public void onProgress(int what, int progress) {
                if (isFirst) {
                    int time = (int) (5 * ((float) progress / 100));
//                    text.setText(time + "");
                    if (progress == 0) {
                        text.setTextSize(16);
                        text.setText(getString(R.string.clickanswar));
                        text.setEnabled(true);
                    }
                } else {
                    if (isPic) {
                        int time = (int) (15 * ((float) progress / 100));
                        text.setText(time + "");
                        if (progress == 0) {
                            showAnswarDialog();
                        }
                    } else {
//                        int time = (int) (15 * ((float) progress / 100));
//                        text.setText(time + "");
                        if (progress == 0) {
                            if (playerStandard.currentScreen == JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN)
                                playerStandard.fullscreenButton.performClick();
                            showAnswarDialog();
                        }

                }
                }
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean==null){
                    ToastUtil.showToast("这道题已经答过了噢，去看看别的题目吧！");
                    return;
                }
                if (bean.getIs_answer()==null){
                    ToastUtil.showToast("这道题已经答过了噢，去看看别的题目吧！");
                    return;
                }
                if (bean.getIs_answer().equals("on")) {
                    ToastUtil.showToast("这道题已经答过了噢，去看看别的题目吧！");
                    return;
                }
                if (bean.getIs_answer().equals("second_answer")) {
                    ToastUtil.showToast("2次机会已经用完了噢，去看看别的题目吧！");
                    return;

                }
                if (!LoginUtil.isLogin(mActivity)) {
                    progress.stop();
                    return;
                }
                showAnswarDialog();
                progress.stop();
            }
        });
    }

    @OnClick({R.id.ln_givealike, R.id.ln_comments, R.id.ln_forwarding, R.id.ln_collection, R.id.tv_drap})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_givealike:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                if (LoginUtil.isLogin(mContext)) {
                    if (isLike) {
                        unLike();
                    } else {
                        giveaLike();
                    }
                }
                break;
            case R.id.ln_comments:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                showEarlyDialog();
                break;
            case R.id.ln_forwarding:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                getShare();
                break;
            case R.id.ln_collection:
                if (!LoginUtil.isLogin(mActivity)) {
                    return;
                }
                if (LoginUtil.isLogin(mContext)) {
                    if (isCollect) {
                        unCollection();
                    } else {
                        collection();
                    }
                }
                break;
            case R.id.tv_drap:
                try {
                    urlClick();
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void giveaLike() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).likes(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                isLike = true;
                imgGivealike.setImageResource(R.mipmap.icon_givealike_clickonthe);
                int num = Integer.parseInt(tvGivealike.getText().toString()) + 1;
                tvGivealike.setText(num + "");
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

    public void unLike() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).unlikes(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                isLike = false;
                imgGivealike.setImageResource(R.mipmap.icon_givealike_3);
                int num = Integer.parseInt(tvGivealike.getText().toString()) - 1;
                tvGivealike.setText(num + "");
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

    public void collection() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).collections(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ToastUtil.showToast("收藏成功");
                isCollect = true;
                imgCollection.setImageResource(R.mipmap.icon_collection_clickonthe);
                int num = Integer.parseInt(tvCollection.getText().toString()) + 1;
                tvCollection.setText(num + "");
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

    public void unCollection() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).uncollections(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                isCollect = false;
                imgCollection.setImageResource(R.mipmap.icon_collection);
                int num = Integer.parseInt(tvCollection.getText().toString()) - 1;
                tvCollection.setText(num + "");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            getComment();
        }
    }

    public boolean isRefesh = true;

    public void getComment() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", System.currentTimeMillis() / 1000 + 60000);
        map.put("size", "3");
        YSBSdk.getService(OAuthService.class).father_comments(id, map, new YRequestCallback<BaseBean<FatherCommentBean>>() {
            @Override
            public void onSuccess(BaseBean<FatherCommentBean> var1) {
                comment_num = var1.getTotal_size();
                tvComments.setText(var1.getTotal_size());
                adapter.setEmptyView(emptyView);
                adapter.setHeaderAndEmpty(true);
                list.clear();
                list.addAll(var1.getComments());
                if (var1.getComments().size() > 2) {
                    adapter.removeAllFooterView();
                    adapter.addFooterView(more);
                }
                adapter.notifyDataSetChanged();
                if (isRefesh) {
                    isRefesh = false;
                } else {
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
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


    public boolean isViewCovered(final View view) {
        View currentView = view;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        if (!totalViewVisible)//if any part of the view is clipped by any of its parents,return true
            return true;
        return false;
    }

    @Override
    protected void onDestroy() {
        progress.stop();
        isFinish=true;
        super.onDestroy();
    }

    private EditText msg;
    private TextView zy_btn;
    private PopupWindow window;
    View popupView;

    @SuppressLint("WrongConstant")
    public void showEarlyDialog() {
        if (popupView == null) {
            popupView = LayoutInflater.from(this).inflate(R.layout.putmessage, null);
            msg = (EditText) popupView.findViewById(R.id.msg);
            zy_btn = (TextView) popupView.findViewById(R.id.put);
        }
        msg.setHint(getString(R.string.commend));
        if (window == null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_50), true);
        window.setBackgroundDrawable(new ColorDrawable());
        window.setSoftInputMode(INPUT_METHOD_NEEDED);

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//设置模式，和Activity的一样，覆盖，调整大小。
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.update();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        msg.setFocusable(true);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(msg, InputMethodManager.SHOW_FORCED);
        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (msg.getText().toString().length() > 0) {
                    zy_btn.setEnabled(true);
                } else {
                    zy_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        zy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatherComment(msg.getText().toString());
//                putMessage(msg.getText().toString());
            }
        });
        recyclerView.scrollToPosition(1);
    }

    private ImageView close;
    private ImageView icon_true;
    private CircleTextProgressbar circleTextProgressbar;
    private TextView option1;
    private TextView answar_title;
    private TextView option2;
    private TextView option3;
    private TextView option4;
    private TextView onemore;
    private TextView chutdowm;
    private TextView tv_wrong;
    private boolean isoption1;
    private boolean isoption2;
    private boolean isoption3;
    private boolean isoption4;
    private String tvOption1, tvOption2, tvOption3, tvOption4, tvtitle;
    private List<String> listoption = new ArrayList<>();

    public void showAnswarDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout mailBoxLay = (RelativeLayout) inflater.inflate(
                R.layout.answar_pop, null);
        close = mailBoxLay.findViewById(R.id.close);
        icon_true = mailBoxLay.findViewById(R.id.icon_true);
        circleTextProgressbar = mailBoxLay.findViewById(R.id.circleProgress);
        answar_title = mailBoxLay.findViewById(R.id.title);
        option1 = mailBoxLay.findViewById(R.id.option1);
        option2 = mailBoxLay.findViewById(R.id.option2);
        option3 = mailBoxLay.findViewById(R.id.option3);
        option4 = mailBoxLay.findViewById(R.id.option4);
        listoption.clear();
        listoption.add(tvOption1);
        listoption.add(tvOption2);
        listoption.add(tvOption3);
        listoption.add(tvOption4);
        Random random = new Random();
        for (String s : listoption) {
            Log.i(TAG, "showAnswarDialog: " + s);
        }
        for (int i = 0; i < listoption.size(); i++) {
            int randomPos = random.nextInt(listoption.size());
            String temp = listoption.get(i);
            listoption.set(i, listoption.get(randomPos));
            listoption.set(randomPos, temp);
            Log.i(TAG, "乱序中：" + listoption.get(i));
        }
        for (String s : listoption) {
            Log.i(TAG, "乱序后: " + s);
        }
        tv_wrong = mailBoxLay.findViewById(R.id.tv_wrong);
        option1.setText(setOptionStyle("A",listoption.get(0)));
        option2.setText(setOptionStyle("B",listoption.get(1)));
        option3.setText(setOptionStyle("C",listoption.get(2)));
        option4.setText(setOptionStyle("D",listoption.get(3)));
        answar_title.setText(tvtitle);
        onemore = mailBoxLay.findViewById(R.id.bt_onemore);
        chutdowm = mailBoxLay.findViewById(R.id.chutdowm);
        if (isFirst) {
            changeState("first_answer");
        } else {
            //onemore.setVisibility(View.GONE);
            onemore.setEnabled(false);
        }
        circleTextProgressbar.setProgressType(CircleTextProgressbar.ProgressType.COUNT_BACK);
        circleTextProgressbar.setProgressLineWidth(5);
        circleTextProgressbar.setOutLineColor(getResources().getColor(R.color.color_cccccc));
        circleTextProgressbar.setProgressColor(getResources().getColor(R.color.color_f8b));
        circleTextProgressbar.setTimeMillis(15000);
        circleTextProgressbar.setProgress(100);
        circleTextProgressbar.setCountdownProgressListener(1, new CircleTextProgressbar.OnCountdownProgressListener() {
            @Override
            public void onProgress(int what, int progress1) {
                int time = (int) (15 * ((float) progress1 / 100));
                chutdowm.setText(time + "");
                if (progress1 == 0) {
                    if (isFirst) {
                        isFirst = false;
                        mailBoxDialog.dismiss();
                        changeState("second_answer");
                        if (isPic) {
                            text.setTextSize(30);
                            progress.setTimeMillis(15000);
                            progress.reStart();
                            text.setEnabled(false);
                        } else {
//                        text.setTextSize(30);
                            if (videotime > 0) {
                                progress.setTimeMillis(videotime);
                            } else {
                                progress.setTimeMillis(15000);
                            }
                            progress.reStart();
                            text.setEnabled(true);
                        }
                    } else {
                        changeState("second_answer");
                        progress.setProgress(0);
                        text.setTextSize(16);
//                    if (!isAnswer)
                        text.setText(getString(R.string.nochance));
                        text.setEnabled(false);
                        circleTextProgressbar.stop();
                        mailBoxDialog.dismiss();

                    }
                }
            }
        });
        mailBoxDialog = new NoticeDialog(this);
        mailBoxDialog.setCanceledOnTouchOutside(false);
        mailBoxDialog.setCancelable(false);
        mailBoxDialog.showDialog(mailBoxLay, 0, 0, 1);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirst) {
                    isFirst = false;
                    changeState("second_answer");
                    circleTextProgressbar.stop();
                    if (isPic) {
                        text.setTextSize(30);
                        progress.setTimeMillis(15000);
                        progress.reStart();
                        text.setEnabled(false);
                    } else {
//                        text.setTextSize(30);
                        if (videotime > 0) {
                            progress.setTimeMillis(videotime);
                        } else {
                            progress.setTimeMillis(15000);
                        }
                        progress.reStart();
                        text.setEnabled(true);
                    }
                } else {
                    changeState("second_answer");
                    progress.setProgress(0);
                    text.setTextSize(16);
//                    if (!isAnswer)
                    text.setText(getString(R.string.nochance));
                    text.setEnabled(false);
                }
                circleTextProgressbar.stop();
                mailBoxDialog.dismiss();
            }
        });
        onemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isoption1 && !isoption2 && !isoption3 && !isoption4) {
                    circleTextProgressbar.stop();
                    isFirst = false;
                    changeState("second_answer");
                    if (isPic) {
                        text.setTextSize(30);
                        progress.setTimeMillis(15000);
                        progress.reStart();
                        text.setEnabled(false);
                    } else {
//                        text.setTextSize(30);
                        if (videotime > 0) {
                            progress.setTimeMillis(videotime);
                        } else {
                            progress.setTimeMillis(15000);

                        }
                        progress.reStart();
                        text.setEnabled(true);
                    }
                    mailBoxDialog.dismiss();
                } else {
                    Map map = new HashMap();
                    if (isoption1) {
                        map = setOptionDMap(option1);
                    }
                    if (isoption2) {
                        map = setOptionDMap(option2);
                    }
                    if (isoption3) {
                        map = setOptionDMap(option3);
                    }
                    if (isoption4) {
                        map = setOptionDMap(option4);
                    }
                    answar(map);

                }
                circleTextProgressbar.stop();

            }
        });

        isoption1 = false;
        isoption2 = false;
        isoption3 = false;
        isoption4 = false;
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isoption1 = !isoption1;
                setOptionClick(isoption1,option1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isoption2 = !isoption2;
                setOptionClick(isoption2,option2);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isoption3 = !isoption3;
                setOptionClick(isoption3,option3);
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isoption4 = !isoption4;
                setOptionClick(isoption4,option4);
            }
        });
        circleTextProgressbar.start();

    }

    //设置点击内容
    public void setOptionClick(boolean isoption,TextView option){
        if (isoption) {
            option.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_on));
        } else {
            option.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg));
        }
        if (!isoption1 && !isoption2 && !isoption3 && !isoption4) {
            if (isFirst) {
                onemore.setText(getString(R.string.one_more));
            } else {
                onemore.setEnabled(false);
                //onemore.setVisibility(View.INVISIBLE);
            }

        } else {
            onemore.setVisibility(View.VISIBLE);
            onemore.setText(getString(R.string.define));
            onemore.setEnabled(true);
        }
    }

    //设置数据内容
    public Map setOptionDMap(TextView option){
        Map map = new HashMap();
        if (option.getText().toString().substring(6, option.getText().toString().length()).equals(tvOption1)) {
            map.put("option1", tvOption1);
        } else if (option.getText().toString().substring(6, option.getText().toString().length()).equals(tvOption2)) {
            map.put("option2", tvOption2);
        } else if (option.getText().toString().substring(6, option.getText().toString().length()).equals(tvOption3)) {
            map.put("option3", tvOption3);
        } else {
            map.put("option4", tvOption4);
        }
        return map;
    }

    //设置样式
    public SpannableString setOptionStyle(String option,String str){
        ForegroundColorSpan text_color = new ForegroundColorSpan(Color.WHITE);
        SpannableString string = new SpannableString(option + "     " + str);
        string .setSpan(text_color,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
    //设置样式
    public SpannableString setTextStyle(String str){
        ForegroundColorSpan text_color = new ForegroundColorSpan(Color.WHITE);
        SpannableString string = new SpannableString(str);
        string .setSpan(text_color,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    @Override
    public void onBackPressed() {
        if (playerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerStandard.releaseAllVideos();
    }

    private DialogUtils dialogUtils;
    private boolean isAnswer = false;

    //答题
    private void answar(Map maps) {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils(mActivity);
        }
        mStateView.showLoading();
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("answers", maps);
        YSBSdk.getService(OAuthService.class).answers(id, map, new YRequestCallback<AnswersBean>() {
            @Override
            public void onSuccess(AnswersBean var1) {
                isFirst = false;
                isAnswer = true;
                changeState("on");
                mStateView.showContent();
                progress.setProgress(0);
                text.setTextSize(16);
                text.setText(getString(R.string.isanswar));
                text.setEnabled(false);
                if (var1.getIs_right().equals("on")) {
                    //onemore.setVisibility(View.INVISIBLE);
                    onemore.setEnabled(false);
                    icon_true.setVisibility(View.VISIBLE);
                    chutdowm.setVisibility(View.GONE);
                    circleTextProgressbar.setProgress(100);
                    circleTextProgressbar.setInCircleColor(getResources().getColor(R.color.white));

                    for (String s : var1.getAnswers_right()) {
                        switch (s) {
                            case "option1":
                                answerResultSet(tvOption1);
                                break;
                            case "option2":
                                answerResultSet(tvOption2);
                                break;
                            case "option3":
                                answerResultSet(tvOption3);
                                break;
                            case "option4":
                                answerResultSet(tvOption4);
                                break;
                        }
                    }

                    if (var1.getIs_reward().equals("on")) {
                        try {
                            String num = var1.getReward_number_add();
                            //答题红包改为直接显示
                            String number;
                            if(ad_type == 1){
                                number = var1.getReward_number();
                                dialogUtils.showOpenRedpacketDialog(number,uri);
                            }else{
                                //判断是否为会员
                                if(num.equals("0")){
                                    number = var1.getReward_number();
                                }else{
                                    number = var1.getReward_number() +" +"+ num;
                                }
                                dialogUtils.showTaskAwardDialog(number);
                            }


                        }catch (Exception e) {
                            //dialogUtils.showTaskAwardDialog(var1.getReward_number());
                            //dialogUtils.showRedpacketDialog(ad_type, var1.getReward_number(),"0",uri);
                        }

                    } else {
                        dialogUtils.showLateDialog();
                    }
                    mailBoxDialog.dismiss();
                } else {
                    onemore.setVisibility(View.GONE);
                    //onemore.setEnabled(false);
                    tv_wrong.setVisibility(View.VISIBLE);
                    icon_true.setVisibility(View.VISIBLE);
                    icon_true.setImageResource(R.mipmap.icon_wrong);
                    chutdowm.setVisibility(View.GONE);
                    circleTextProgressbar.setInCircleColor(getResources().getColor(R.color.white));
                    circleTextProgressbar.setProgress(100);

                    if (isoption1) {
                        option1.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_off));
                    }
                    if (isoption2) {
                        option2.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_off));
                    }
                    if (isoption3) {
                        option3.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_off));
                    }
                    if (isoption4) {
                        option4.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_off));
                    }

                    for (String s : var1.getAnswers_right()) {
                        switch (s) {
                            case "option1":
                                answerResultSet(tvOption1);
                                break;
                            case "option2":
                                answerResultSet(tvOption2);
                                break;
                            case "option3":
                                answerResultSet(tvOption3);
                                break;
                            case "option4":
                                answerResultSet(tvOption4);
                                break;
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mailBoxDialog.dismiss();
                        }
                    }, 2000);    //延时1s执行

                }
            }

            @Override
            public void onFailed(String var1, String message) {
//                if (var1.equals("403")) {
//                    ToastUtil.showToast("机会已用完");
//                }
                ErrorUtils.error(mContext,var1,message);
                isFirst = false;
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    //答题结果选项显示
    public void answerResultSet(String tvOption){
        if (option1.getText().toString().substring(6, option1.getText().toString().length()).equals(tvOption)) {
            option1.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_on));
        } else if (option2.getText().toString().substring(6, option2.getText().toString().length()).equals(tvOption)) {
            option2.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_on));
        } else if (option3.getText().toString().substring(6, option3.getText().toString().length()).equals(tvOption)) {
            option3.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_on));
        } else {
            option4.setBackground(getResources().getDrawable(R.mipmap.answer_option_bg_on));
        }
    }

    public void fatherComment(String comment) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("comment", comment);
        YSBSdk.getService(OAuthService.class).father_comments_put(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("评论成功");
                window.dismiss();
                msg.setText("");
                getComment();
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

    private boolean isFollow = false;

    private void follow() {
        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).fans(userid, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {


                isFollow = true;
                btFollow.setText(mContext.getString(R.string.isfollow));
                btFollow.setTextColor(mContext.getResources().getColor(R.color.text_999999));
                //btFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_isfollow), null, null, null);
                btFollow.setBackgroundResource(R.drawable.bg_follow);
                btFollow.setPadding(10, 0, 0, 0);
                tvFollow.setText(mContext.getString(R.string.isfollow));
                tvFollow.setTextColor(mContext.getResources().getColor(R.color.text_999999));
                //tvFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_isfollow), null, null, null);
                tvFollow.setBackgroundResource(R.drawable.bg_follow);
                tvFollow.setPadding(0, 0, 0, 0);
            }

            @Override
            public void onFailed(String var1, String message) {
            }

            @Override
            public void onException(Throwable var1) {
            }
        });
    }

    private void unfollow() {
        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView) mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.unfollow_dialog));
        ((TextView) mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                YSBSdk.getService(OAuthService.class).unfans(userid, map, new YRequestCallback<PicCodeBean>() {
                    @Override
                    public void onSuccess(PicCodeBean var1) {
                        isFollow = false;
                        btFollow.setText(mContext.getString(R.string.user_like));
                        btFollow.setTextColor(mContext.getResources().getColor(R.color.white));
                        //btFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_addfollow), null, null, null);
                        btFollow.setBackgroundResource(R.drawable.bg_addfollow);
                        //btFollow.setPadding(24, 0, 0, 0);
                        tvFollow.setText(mContext.getString(R.string.user_like));
                        tvFollow.setTextColor(mContext.getResources().getColor(R.color.white));
                        //tvFollow.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_addfollow), null, null, null);
                        tvFollow.setBackgroundResource(R.drawable.bg_addfollow);
                        //tvFollow.setPadding(20, 0, 0, 0);
                        mailDialog.dismiss();
                    }

                    @Override
                    public void onFailed(String var1, String message) {
                    }

                    @Override
                    public void onException(Throwable var1) {
                    }
                });
            }
        });
        ((TextView) mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
    }

    public void CommentLike(int pos) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("father_comment_id", list.get(pos).getId());
        YSBSdk.getService(OAuthService.class).like_comments(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                list.get(pos).setIs_like("on");
                list.get(pos).setLike_number(Integer.parseInt(list.get(pos).getLike_number()) + 1 + "");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });

    }

    public void UncommentLike(int pos) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("father_comment_id", list.get(pos).getId());
        YSBSdk.getService(OAuthService.class).unlike_comments(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                list.get(pos).setIs_like("off");
                list.get(pos).setLike_number(Integer.parseInt(list.get(pos).getLike_number()) - 1 + "");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ToastUtil.showToast(message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

}

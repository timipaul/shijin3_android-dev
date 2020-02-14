package com.shijinsz.shijin.ui.ad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.FatherCommentBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.ad.adapter.CommentAdatper;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.HeaderView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/28.
 */

public class VideoCommentActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.img_userhead)
    CircleImageView imgUserhead;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.img_vip_into)
    ImageView imgVip;
    @BindView(R.id.tv_hide)
    TextView mTvhide;
    @BindView(R.id.comment_num)
    TextView mComment_num;
    private List<FatherCommentBean> list = new ArrayList<>();
    private CommentAdatper adatper;

    @Override
    public int bindLayout() {
        return R.layout.video_comment_activity;
    }

    private String id = "", num = "", pos = "1", userid = "", type = "1", index = "1";
    private boolean isFirst = true;

    @Override
    public void initView(View view) {
        /*showTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent();
                setResult(1);
                finish();
            }
        });*/

        //设置视图背景透明
        setTheme(R.style.myTransparent);

        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("on")){
            imgVip.setVisibility(View.VISIBLE);
        }else {
            imgVip.setVisibility(View.GONE);
        }

        translucentActivity(VideoCommentActivity.this);

        GlideApp.with(mContext).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).into(imgUserhead);
        userid = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
        id = getIntent().getStringExtra("id");
        //num = getIntent().getStringExtra("num");
        //pos = getIntent().getStringExtra("pos");
        //type = getIntent().getStringExtra("type");
        //index = getIntent().getStringExtra("index");
        //setTitle("评论");
        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        adatper = new CommentAdatper(R.layout.comment_list_item, list);
        recyclerView.setAdapter(adatper);
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    if (pos != null) {
                        if (!pos.isEmpty()) {
                            if (Integer.parseInt(pos) == 0) {

                            } else {
                                //smoothMoveToPosition(recyclerView, Integer.parseInt(pos));
                            }
                        }
                    }
                }
            }
        });*/
        adatper.setOnShowListen(new CommentAdatper.onShowListen() {
            @Override
            public void call(int pos) {

                Log.e(TAG,"call ------- pos " + pos);

                if (list.get(pos).getComments().size() < Integer.parseInt(list.get(pos).getComment_number())) {
                    getChild(list.get(pos).getComments().get(list.get(pos).getComments().size() - 1).getCreated_at(), pos);
                } else {
                    List<FatherCommentBean.Comments> bean = new ArrayList<>();
                    bean.addAll(list.get(pos).getComments());
                    list.get(pos).getComments().clear();
                    list.get(pos).getComments().add(bean.get(0));
                    list.get(pos).getComments().add(bean.get(1));
                    adatper.notifyDataSetChanged();
                }
            }

            @Override
            public void callfathercall(int pos) {

                Log.e(TAG,"call ------- pos " + pos);

                showEarlyDialog(pos, -1, 1);
            }

            @Override
            public void childcall(int pos, int index) {
                showEarlyDialog(pos, index, 2);
            }

            @Override
            public void onlike(int pos) {
                if (list.get(pos).getIs_like().equals("on")) {
                    UncommentLike(pos, -1);
                } else {
                    CommentLike(pos, -1);
                }
            }

            @Override
            public void onChilLike(int pod, int index) {
                if (list.get(pod).getComments().get(index).getIs_like().equals("on")) {
                    UncommentLike(pod, index);
                } else {
                    CommentLike(pod, index);
                }
            }

            @Override
            public void onComment(int pos) {
                showRequestEarlyDialog(pos, -1, 1);
            }

            @Override
            public void onChilComment(int pos, int index) {
                showRequestEarlyDialog(pos, index, 2);
            }
        });
//        adatper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        mStateView.showLoading();
        getFather();

        mTvhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void CommentLike(int pos, int index) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("father_comment_id", list.get(pos).getId());
        if (index != -1) {
            map.put("child_comment_id", list.get(pos).getComments().get(index).getId());
        }
        YSBSdk.getService(OAuthService.class).like_comments(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                if (index != -1) {
                    list.get(pos).getComments().get(index).setIs_like("on");
                    list.get(pos).getComments().get(index).setLike_number(Integer.parseInt(list.get(pos).getComments().get(index).getLike_number()) + 1 + "");
                } else {
                    list.get(pos).setIs_like("on");
                    list.get(pos).setLike_number(Integer.parseInt(list.get(pos).getLike_number()) + 1 + "");
                }
                adatper.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });

    }

    public void UncommentLike(int pos, int index) {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("father_comment_id", list.get(pos).getId());
        if (index != -1) {
            map.put("child_comment_id", list.get(pos).getComments().get(index).getId());
        }
        YSBSdk.getService(OAuthService.class).unlike_comments(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                if (index != -1) {
                    list.get(pos).getComments().get(index).setIs_like("off");
                    list.get(pos).getComments().get(index).setLike_number(Integer.parseInt(list.get(pos).getComments().get(index).getLike_number()) - 1 + "");
                } else {
                    list.get(pos).setIs_like("off");
                    list.get(pos).setLike_number(Integer.parseInt(list.get(pos).getLike_number()) - 1 + "");
                }
                adatper.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }

    private PopupWindow window;
    private TextView request;
    private TextView content;
    private TextView report;
    private TextView cancel;
    private View popupView;

    public void showEarlyDialog(int pos, int index, int type) {

        if (window == null) {
            popupView = mInflater.inflate(R.layout.choice_request_pop, null);
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            request = popupView.findViewById(R.id.request);
            content = popupView.findViewById(R.id.tv_content);
            report = popupView.findViewById(R.id.report);
            cancel = popupView.findViewById(R.id.cancel);
        }
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        if (type == 1) {
            if (list.get(pos).getUser_id().equals(userid)) {
                report.setText(getString(R.string.delete));
            } else {
                report.setText(getString(R.string.report));
            }
            content.setText(list.get(pos).getUser().getNickname() + "：" + list.get(pos).getContent());
        } else {
            if (list.get(pos).getComments().get(index).getUser_id().equals(userid)) {
                report.setText(getString(R.string.delete));
            } else {
                report.setText(getString(R.string.report));
            }
            content.setText(list.get(pos).getComments().get(index).getUser().getNickname() + "：" + list.get(pos).getComments().get(index).getContent());
        }
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    if (list.get(pos).getUser_id().equals(userid)) {
                        deleteComment(pos, index);
                    } else {
                        complaints(pos, index);
                    }
                    window.dismiss();
                } else {
                    if (list.get(pos).getComments().get(index).getUser_id().equals(userid)) {
                        deleteComment(pos, index);
                    } else {
                        complaints(pos, index);
                    }
                    window.dismiss();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                showRequestEarlyDialog(pos, index, type);
            }
        });
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        // 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new BitmapDrawable());
        // 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        //2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        window.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    private EditText msg;
    private TextView zy_btn;
    private PopupWindow requestWindow;
    private View popupView1;

    @SuppressLint("WrongConstant")
    public void showRequestEarlyDialog(int pos, int index, int type) {

        if (requestWindow == null) {
            popupView1 = LayoutInflater.from(this).inflate(R.layout.putmessage, null);
            requestWindow = new PopupWindow(popupView1, ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_50), true);
            msg = (EditText) popupView1.findViewById(R.id.msg);
            zy_btn = (TextView) popupView1.findViewById(R.id.put);
        }
        //2016/5/17 设置背景颜色

        if (type == 1) {
            msg.setHint(getString(R.string.request) + list.get(pos).getUser().getNickname() + "：");
        } else if (type == 2) {
            msg.setHint(getString(R.string.request) + list.get(pos).getComments().get(index).getUser().getNickname() + "：");
        } else {
            msg.setHint(getString(R.string.commend));
        }
        requestWindow.setBackgroundDrawable(new ColorDrawable());
        requestWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);

        requestWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//设置模式，和Activity的一样，覆盖，调整大小。
        //2016/5/17 设置可以获取焦点
        requestWindow.setFocusable(true);
        //2016/5/17 设置可以触摸弹出框以外的区域
        requestWindow.setOutsideTouchable(true);
        requestWindow.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        requestWindow.update();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
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
                if (type == 3) {
                    fatherComment(msg.getText().toString());
                } else {
                    childComment(pos, index, msg.getText().toString());
                }
            }
        });
    }

    private void getChild(String cursor1, int pos) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", cursor1);
        map.put("size", "10");
        map.put("father_comment_id", list.get(pos).getId());
        YSBSdk.getService(OAuthService.class).child_comment(id, map, new YRequestCallback<BaseBean<FatherCommentBean.Comments>>() {
            @Override
            public void onSuccess(BaseBean<FatherCommentBean.Comments> var1) {
                mStateView.showContent();
                list.get(pos).getComments().addAll(var1.getComments());
                adatper.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    private String cursor;
    private boolean isRefresh = true;

    private void getFather() {

        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", cursor);
        map.put("size", "10");
        YSBSdk.getService(OAuthService.class).father_comments(id, map, new YRequestCallback<BaseBean<FatherCommentBean>>() {
            @Override
            public void onSuccess(BaseBean<FatherCommentBean> var1) {
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();


                if (isRefresh) {
                    mComment_num.setText(String.format(getString(R.string.comment_num), var1.getTotal_size()));
                    list.clear();
                }

                Log.i(TAG,var1.getComments().size()+"");

                if (var1.getComments().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }

                list.addAll(var1.getComments());
                adatper.notifyDataSetChanged();
                /*if (isFirst) {
                    isFirst = false;
                    if (pos != null) {
                        if (!pos.isEmpty()) {
                            if (Integer.parseInt(pos) == 0) {

                            } else {
                                smoothMoveToPosition(recyclerView, Integer.parseInt(pos) + 1);
                            }
                            if (type != null) {
                                if (type.equals("2")) {
                                    showEarlyDialog(Integer.parseInt(pos), -1, 1);
                                } else if (type.equals("3")) {
                                    showRequestEarlyDialog(Integer.parseInt(pos), -1, 1);
                                } else if (type.equals("4")) {
                                    showRequestEarlyDialog(Integer.parseInt(pos), Integer.parseInt(index), 2);
                                }
                            }
                        }
                    }
                }*/
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            @Override
            public void onException(Throwable var1) {
                try{
                    mStateView.showContent();
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                }catch (Exception e){
                    Log.d(TAG,"处理报错");
                }

            }
        });
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        cursor = System.currentTimeMillis() / 1000 + 60000 + "";
        isRefresh = true;
        getFather();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        try {
            cursor = list.get(list.size() - 1).getCreated_at();
            isRefresh = false;
            getFather();
        }catch (Exception e) {

        }

    }

    //父评论
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
                requestWindow.dismiss();
                msg.setText("");
                isRefresh = true;
                cursor = System.currentTimeMillis() / 1000 + 60000 + "";
                getFather();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext, var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }


    //子评论
    public void childComment(int pos, int index, String comment) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("father_comment_id", list.get(pos).getId());
        map.put("comment", comment);
        if (index != -1) {
            map.put("child_comment_id", list.get(pos).getComments().get(index).getId());
        }
        YSBSdk.getService(OAuthService.class).child_comments(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("评论成功");
                requestWindow.dismiss();
                msg.setText("");
                isRefresh = true;
                cursor = System.currentTimeMillis() / 1000 + 60000 + "";
                getFather();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    //删除评论
    public void deleteComment(int pos, int index) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("father_comment_id", list.get(pos).getId());
        if (index != -1) {
            map.put("child_comment_id", list.get(pos).getComments().get(index).getId());
        }
        YSBSdk.getService(OAuthService.class).delete_comments(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("删除成功");
                if (index != -1) {
                    list.get(pos).getComments().remove(index);
                } else {
                    list.remove(pos);
                }
                adatper.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }


    //举报
    public void complaints(int pos, int index) {
        mStateView.showLoading();
        String uid = "";
        Map map = new HashMap();
        if (index != -1) {
            map.put("complaint_content", id + "," + list.get(pos).getId() + "," + list.get(pos).getComments().get(index).getContent());
            uid = list.get(pos).getComments().get(index).getUser_id();
        } else {
            map.put("complaint_content", id + "," + list.get(pos).getId() + "," + list.get(pos).getContent());
            uid = list.get(pos).getUser_id();
        }


        YSBSdk.getService(OAuthService.class).complaints(uid, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("举报成功");
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    @OnClick(R.id.tv_comment)
    public void onViewClicked() {
        showRequestEarlyDialog(0, 0, 3);
    }

    //设置视图透明
    private void translucentActivity(Activity activity) {

        try {
            activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            activity.getWindow().getDecorView().setBackground(null);
            Method activityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            activityOptions.setAccessible(true);
            Object options = activityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> aClass = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    aClass = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                    aClass, ActivityOptions.class);
            method.setAccessible(true);
            method.invoke(activity, null, options);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
